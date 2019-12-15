package com.android.a9dhili;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.a9dhili.Models.User;
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
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
/**
 * Fit a map to a bounding box
 */

public class UpdateAdrActivity extends AppCompatActivity implements OnMapReadyCallback,
        MapboxMap.OnMapClickListener {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private static final LatLng locationOne = new LatLng(36.790692, 10.186333);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

// Mapbox access token is configured here. This needs to be called either in your application
// object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

// This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_update_adr);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {

        UpdateAdrActivity.this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {

                addMarkerIconsToMap(style);

                // Toast instructing user to tap on the map to start animation and set bounds
                Toast.makeText(
                        UpdateAdrActivity.this,
                        "show my current address",
                        Toast.LENGTH_LONG
                ).show();

                mapboxMap.addOnMapClickListener(UpdateAdrActivity.this);


            }
        });
    }


    private void addMarkerIconsToMap(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addImage("icon-id", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.redmarker)));

        loadedMapStyle.addSource(new GeoJsonSource("source-id",
                FeatureCollection.fromFeatures(new Feature[] {
                        Feature.fromGeometry(Point.fromLngLat(locationOne.getLongitude(), locationOne.getLatitude())),
                })));

        loadedMapStyle.addLayer(new SymbolLayer("layer-id",
                "source-id").withProperties(
                iconImage("icon-id"),
                iconOffset(new Float[]{0f,-8f})
        ));
    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {

        User user  = ((myApp) getApplication()).getUser();

        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(user.getLatitude(), user.getLongitude())) // Sets the new camera position
                .zoom(17) // Sets the zoom
                .bearing(180) // Rotate the camera
                .tilt(30) // Set the camera tilt
                .build(); // Creates a CameraPosition from the builder

        mapboxMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), 7000);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    @SuppressWarnings( {"MissingPermission"})
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapboxMap != null) {
            mapboxMap.removeOnMapClickListener(this);
        }
        mapView.onDestroy();
    }

}

