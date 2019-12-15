package com.android.a9dhili.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.a9dhili.ChoixActivity;
import com.android.a9dhili.LocationPostActivity;
import com.android.a9dhili.Models.Post;
import com.android.a9dhili.Models.User;
import com.android.a9dhili.R;
import com.android.a9dhili.Retrofit.INodeJS;
import com.android.a9dhili.Retrofit.RetrofitClient;
import com.android.a9dhili.SignInActivity;
import com.android.a9dhili.TragetActivity;
import com.android.a9dhili.myApp;
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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

public class AdapterPosts extends  RecyclerView.Adapter<AdapterPosts.ViewHolder> implements
        OnMapReadyCallback {

    //map

    private MapView mapView;
    private MapboxMap mapboxMap;
    private static final LatLng locationOne = new LatLng(36.790692, 10.186333);


    INodeJS myAPI;

    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String ICON_ID = "ICON_ID";
    private static final String LAYER_ID = "LAYER_ID";

    private Context mContext;
    private List<Post> posts;
    User user;


    @NonNull
    @Override
    public AdapterPosts.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //map instance
        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.

        Mapbox.getInstance(this.mContext, this.mContext.getString(R.string.mapbox_access_token));


        // This contains the MapView in XML and needs to be called after the access token is configured.


        View mItemView = LayoutInflater.from(mContext).inflate(R.layout.single_post, parent, false);

        //Init the API
        Retrofit retrofit = RetrofitClient.getInstance();
        user = ((myApp) mContext).getUser();


        myAPI = retrofit.create(INodeJS.class);
        return new ViewHolder(mItemView);

    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final Post singleItem = posts.get(position);

        Call<User> call = myAPI.getUserById(singleItem.getId_user());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                User user = response.body();
                Log.d("test User ", "nom" + user.getNom());
                Log.d("test User ", "prenom" + user.getPrenom());
                holder.tv_nameUser.setText(String.format("%s %s", user.getPrenom(), user.getNom()));

            }


            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Log error here since request failed
                Log.d("fail ", "fail");
                Log.e("erreur", t.toString());
            }

        });


        holder.contenu.setText(singleItem.getContenu());
        String etat = "non approuvé";
        if (singleItem.getEtat() == 1) {
            etat = "en attente";
        } else if (singleItem.getEtat() == 2) {
            etat = "livré";
        }
        holder.tv_etat.setText(etat);
        holder.tags.setText(singleItem.getTags());
        holder.tarif.setText(String.format("%s", singleItem.getTarif()));
        holder.adresse.setText(String.format("%s", singleItem.getLongitude()));

//approuver post
        holder.btn_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = RetrofitClient.getInstance();

                myAPI = retrofit.create(INodeJS.class);
                Log.d("test post a approuver ",""+singleItem.getId());

                Call<Void> callApp = myAPI.approuverPost(user.getUnique_id(),singleItem.getId_user(),1,singleItem.getId());
                callApp.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> callApp, Response<Void> response) {
                       Log.d("test approuver : ","DONNNNNNNNNE");

                        Call<List<Post>> call = myAPI.getPosts();
                        call.enqueue(new Callback<List<Post>>() {
                            @Override
                            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                                List<Post> liste = response.body();
                                AdapterPosts.this.notifyChange(liste);

                            }

                            @Override
                            public void onFailure(Call<List<Post>> call, Throwable t) {
                                // Log error here since request failed
                                Log.d("fail ", "fail");
                                Log.e("erreur", t.toString());
                            }

                        });

                    }

                    @Override
                    public void onFailure(Call<Void> callApp, Throwable t) {
                        // Log error here since request failed
                        Log.d("fail ", "fail");
                        Log.e("erreur", t.toString());
                    }

                });




            }
        });
        holder.btn_locationPost.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext.getApplicationContext(), TragetActivity.class);
                intent.putExtra("post", singleItem);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

                // Launch the new activity and add the additional flags to the intent
                mContext.getApplicationContext().startActivity(intent);
            }



        });

    }

    public void notifyChange(List<Post> posts) {
        this.posts = posts;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public AdapterPosts(Context mContext, List<Post> productList) {
        this.mContext = mContext;
        this.posts = productList;
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nameUser, tv_etat, adresse, tags, contenu, tarif;
        Button btn_app , btn_locationPost;

        public ViewHolder(@NonNull View mView) {
            super(mView);

            tv_nameUser = (TextView) mView.findViewById(R.id.tv_nameUser);
            tv_etat = (TextView) mView.findViewById(R.id.tv_etat);
            adresse = (TextView) mView.findViewById(R.id.adr_post);
            tags = (TextView) mView.findViewById(R.id.tv_tags);
            contenu = (TextView) mView.findViewById(R.id.tv_contenu);
            tarif = (TextView) mView.findViewById(R.id.tv_tarif);
            btn_app = (Button) mView.findViewById(R.id.btn_approuver);
            btn_locationPost=(Button) mView.findViewById(R.id.btn_seeLocation);

        }
    }






}