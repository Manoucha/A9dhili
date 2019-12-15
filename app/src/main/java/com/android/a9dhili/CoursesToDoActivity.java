package com.android.a9dhili;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.a9dhili.Adapters.AdapterMyCourse;
import com.android.a9dhili.Models.Post;
import com.android.a9dhili.Models.User;
import com.android.a9dhili.Retrofit.INodeJS;
import com.android.a9dhili.Retrofit.RetrofitClient;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

/**
 * Use a recyclerview with a Mapbox map to easily explore content all on one screen
 */
public class CoursesToDoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String SYMBOL_ICON_ID = "SYMBOL_ICON_ID";
    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String LAYER_ID = "LAYER_ID";
    public MapboxMap mapboxMap;
    private MapView mapView;
    private FeatureCollection featureCollection;


    INodeJS myAPI;

    RecyclerView recyclerView1;
    List<Post> liste;


    ArrayList<LatLng> coordinates = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Retrofit retrofit = RetrofitClient.getInstance();

        myAPI = retrofit.create(INodeJS.class);

        User user  = ((myApp) getApplication()).getUser();

        Call<List<Post>> call = myAPI.getCoursesToDo(user.getUnique_id());

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                 liste = response.body();
                for(Post p : liste)
                {

                    coordinates.add(new LatLng(p.getLatitude(), p.getLongitude()));
                }



            }
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                // Log error here since request failed
                Log.d("fail ","fail");
                Log.e("erreur",t.toString());
            }

        });
// Mapbox access token is configured here. This needs to be called either in your application
// object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

// This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_courses_to_do);

// Initialize the map view
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        CoursesToDoActivity.this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.DARK, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                initFeatureCollection();
                initMarkerIcons(style);
                initRecyclerView();
                Toast.makeText(CoursesToDoActivity.this, R.string.toast_instruction, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initFeatureCollection() {
        featureCollection = FeatureCollection.fromFeatures(new Feature[] {});
        List<Feature> featureList = new ArrayList<>();
        if (featureCollection != null) {
            for (LatLng latLng : coordinates) {
                featureList.add(Feature.fromGeometry(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude())));
            }
            featureCollection = FeatureCollection.fromFeatures(featureList);
        }
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rv_on_top_of_map);
        LocationRecyclerViewAdapter locationAdapter =
                new LocationRecyclerViewAdapter(createRecyclerViewLocations(), mapboxMap);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(locationAdapter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
    }

    private void initMarkerIcons(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addImage(SYMBOL_ICON_ID, BitmapFactory.decodeResource(
                this.getResources(), R.drawable.redmarker));
        loadedMapStyle.addSource(new GeoJsonSource(SOURCE_ID, featureCollection));
        loadedMapStyle.addLayer(new SymbolLayer(LAYER_ID, SOURCE_ID).withProperties(
                iconImage(SYMBOL_ICON_ID),
                iconAllowOverlap(true),
                iconOffset(new Float[] {0f, -4f})
        ));
    }

    private List<SingleRecyclerViewLocation> createRecyclerViewLocations() {
        ArrayList<SingleRecyclerViewLocation> locationList = new ArrayList<>();
        for (int x = 0; x < coordinates.size(); x++) {
            SingleRecyclerViewLocation singleLocation = new SingleRecyclerViewLocation();
            singleLocation.setContenu(liste.get(x).getContenu());
            singleLocation.setTarif(""+liste.get(x).getTarif());
            singleLocation.setLocationCoordinates(coordinates.get(x));
            locationList.add(singleLocation);
        }
        return locationList;
    }

    // Add the mapView lifecycle to the activity's lifecycle methods
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * POJO model class for a single location in the recyclerview
     */
    class SingleRecyclerViewLocation {

        private String contenu;
        private String tarif;
        private LatLng locationCoordinates;

        public String getContenu() {
            return contenu;
        }

        public void setContenu(String contenu) {
            this.contenu = contenu;
        }

        public String getTarif() {
            return tarif;
        }

        public void setTarif(String tarif) {
            this.tarif = tarif;
        }

        public LatLng getLocationCoordinates() {
            return locationCoordinates;
        }

        public void setLocationCoordinates(LatLng locationCoordinates) {
            this.locationCoordinates = locationCoordinates;
        }
    }

    static class LocationRecyclerViewAdapter extends
            RecyclerView.Adapter<LocationRecyclerViewAdapter.MyViewHolder> {

        private List<SingleRecyclerViewLocation> locationList;
        private MapboxMap map;

        public LocationRecyclerViewAdapter(List<SingleRecyclerViewLocation> locationList, MapboxMap mapBoxMap) {
            this.locationList = locationList;
            this.map = mapBoxMap;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rv_on_top_of_map_card, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            SingleRecyclerViewLocation singleRecyclerViewLocation = locationList.get(position);
            holder.tv_contenuCard.setText(singleRecyclerViewLocation.getContenu());
            holder.tv_tarifCard.setText(singleRecyclerViewLocation.getTarif());
            holder.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position) {
                    LatLng selectedLocationLatLng = locationList.get(position).getLocationCoordinates();
                    CameraPosition newCameraPosition = new CameraPosition.Builder()
                            .target(selectedLocationLatLng)
                            .build();
                    map.easeCamera(CameraUpdateFactory.newCameraPosition(newCameraPosition));
                }
            });
        }

        @Override
        public int getItemCount() {
            return locationList.size();
        }

        static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView name,tv_contenuCard,tv_tarifCard;
            TextView numOfBeds;
            CardView singleCard;
            Button btn_annuler;
            ItemClickListener clickListener;

            MyViewHolder(View view) {
                super(view);
                name = view.findViewById(R.id.location_title_tv);
                tv_tarifCard = view.findViewById(R.id.tv_tarifCard);
                tv_contenuCard = view.findViewById(R.id.tv_contenuCard);
                btn_annuler = view.findViewById(R.id.btn_annuler);
                singleCard = view.findViewById(R.id.single_location_cardview);
                singleCard.setOnClickListener(this);
            }

            public void setClickListener(ItemClickListener itemClickListener) {
                this.clickListener = itemClickListener;
            }

            @Override
            public void onClick(View view) {
                clickListener.onClick(view, getLayoutPosition());
            }
        }
    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }
}
