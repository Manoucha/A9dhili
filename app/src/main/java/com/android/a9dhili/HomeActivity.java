package com.android.a9dhili;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.a9dhili.Adapters.AdapterPosts;
import com.android.a9dhili.Adapters.RecyclerViewAdapterFeed;
import com.android.a9dhili.Models.FeedModel;
import com.android.a9dhili.Models.Post;
import com.android.a9dhili.Models.User;
import com.android.a9dhili.Retrofit.INodeJS;
import com.android.a9dhili.Retrofit.RetrofitClient;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.POST;

public class HomeActivity extends AppCompatActivity   {

    RecyclerView recyclerView1;

    List<Post> posts=new ArrayList<>();

    Adapter adapterFeed;
    INodeJS myAPI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //
        //Init the API
        Retrofit retrofit = RetrofitClient.getInstance();

        myAPI = retrofit.create(INodeJS.class);
        //
        FloatingActionButton fab = findViewById(R.id.btn_addPost);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user  = ((myApp) getApplication()).getUser();
                Snackbar.make(view, "Here's a Snackbar"+user.getNom(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                addPost();
            }
        });



        populaterecyclerview();

    }
    private void addPost()
    {
        final View register_view = LayoutInflater.from(this).inflate(R.layout.addpost,null);
        new MaterialStyledDialog.Builder(this)
                .setTitle("9adhytek")
                .setDescription("dalel alina !")
                .setCustomView(register_view)
                .setNegativeText("batalt")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveText("yes")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //get value from editText
                        MaterialEditText edt_contenu = (MaterialEditText)register_view.findViewById(R.id.et_contenu);
                        EditText edt_tarif  = (EditText)register_view.findViewById(R.id.et_tarif);
                        float tarif = Float.parseFloat(edt_tarif.getText().toString());
                        EditText edt_tags = (MaterialEditText)register_view.findViewById(R.id.et_tags);
                        User user  = ((myApp) getApplication()).getUser();

                        Post post = new Post(edt_contenu.getText().toString(),edt_tags.getText().toString(),0,user.getUnique_id(),tarif,user.getLongitude(),user.getLatitude());

                       Call<String> call = myAPI.addPost(post.getContenu(),post.getTags(),post.getEtat(),post.getId_user(),post.getTarif(),post.getLongitude(),post.getLatitude());
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                int statusCode = response.code();
                                Log.d("success ","yessss ajoutééééééééés");
                                populaterecyclerview();
                            }
                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                // Log error here since request failed
                                Log.d("fail ","fail");
                                Log.e("erreur",t.toString());
                            }

                        });




                    }
                }).show();


    }
    private void populaterecyclerview() {

        final RecyclerView recyclerView = findViewById(R.id.recy_feed);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));



        Call<List<Post>> call = myAPI.getPosts();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                List<Post> liste = response.body();
                for (Post p : liste)
                {
                    Log.d("poste ",""+p.getContenu());

                    Post post=new Post(p.getId(),p.getContenu(),p.getTags(),p.getEtat(),p.getId_user(),p.getTarif(),p.getLongitude(),p.getLatitude());

                    posts.add(post);
                    Log.d("taille de possts ",""+posts.size());

                }

                recyclerView.setAdapter(new AdapterPosts(getApplicationContext() , posts));


            }
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                // Log error here since request failed
                Log.d("fail ","fail");
                Log.e("erreur",t.toString());
            }

        });







    }
}
