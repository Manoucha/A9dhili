package com.android.a9dhili;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.android.a9dhili.Adapters.AdapterMyCourse;
import com.android.a9dhili.Adapters.AdapterPosts;
import com.android.a9dhili.Models.Course;
import com.android.a9dhili.Models.Post;
import com.android.a9dhili.Models.User;
import com.android.a9dhili.Retrofit.INodeJS;
import com.android.a9dhili.Retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MesCoursesActivity extends AppCompatActivity {

    INodeJS myAPI;

    RecyclerView recyclerView1;
    List<Post> liste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes_courses);
        //Init the API
        Retrofit retrofit = RetrofitClient.getInstance();

        myAPI = retrofit.create(INodeJS.class);
         final RecyclerView recyclerView = findViewById(R.id.recy_feed_mescourses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));




        User user  = ((myApp) getApplication()).getUser();
        Call<List<Post>> call = myAPI.getMyCourses(user.getUnique_id());

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                List<Post> liste = response.body();

                recyclerView.setAdapter(new AdapterMyCourse(getApplicationContext() ,liste));

            }
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                // Log error here since request failed
                Log.d("fail ","fail");
                Log.e("erreur",t.toString());
            }

        });




    }
    private void populaterecyclerview() {

        final RecyclerView recyclerView = findViewById(R.id.recy_feed_mescourses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));




        User user  = ((myApp) getApplication()).getUser();
        Call<List<Post>> call = myAPI.getMyCourses(user.getUnique_id());

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                List<Post> liste = response.body();

                recyclerView.setAdapter(new AdapterMyCourse(getApplicationContext() ,liste));

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
