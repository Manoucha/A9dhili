package com.android.a9dhili.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.a9dhili.Models.Course;
import com.android.a9dhili.Models.Post;
import com.android.a9dhili.Models.User;
import com.android.a9dhili.R;
import com.android.a9dhili.Retrofit.INodeJS;
import com.android.a9dhili.Retrofit.RetrofitClient;
import com.android.a9dhili.myApp;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AdapterMyCourse  extends RecyclerView.Adapter<AdapterMyCourse.ViewHolder> {

    private Context mContext;
    private List<Post> posts;
    INodeJS myAPI;

    @NonNull
    @Override
    public AdapterMyCourse.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = LayoutInflater.from(mContext).inflate(R.layout.mycourse, parent, false);
        return new ViewHolder(mItemView);
        //Init the API

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Post singleItem = posts.get(position);

        holder.tv_contenu_course.setText(singleItem.getContenu());
        String etat = "non approuvé";
        if (singleItem.getEtat() == 1) {
            etat = "en attente";
        } else if (singleItem.getEtat() == 2) {
            etat = "livré";
        }
        holder.tv_etat.setText(etat);
        holder.tv_tarif_course.setText(String.format("%s", singleItem.getTarif()));
        //supprimer user
        final Post singleItemUser = posts.get(position);
        Retrofit retrofit = RetrofitClient.getInstance();

        myAPI = retrofit.create(INodeJS.class);
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<ResponseBody> call = myAPI.deletePost(singleItem.getId());
                User user  = ((myApp) mContext.getApplicationContext()).getUser();
                final Call<List<Post>> call1 = myAPI.getMyCourses(user.getUnique_id());

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        call1.enqueue(new Callback<List<Post>>() {
                            @Override
                            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                                List<Post> liste = response.body();

                                AdapterMyCourse.this.notifyChange(liste);

                            }
                            @Override
                            public void onFailure(Call<List<Post>> call, Throwable t) {
                                // Log error here since request failed
                                Log.d("fail ","fail");
                                Log.e("erreur",t.toString());
                            }

                        });


                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        // Log error here since request failed
                        Log.d("fail ","fail");
                        Log.e("erreur",t.toString());
                    }

                });


            } });

    }
    public void notifyChange(List<Post> posts)
    {
        this.posts=posts;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
    public AdapterMyCourse(Context mContext, List<Post> productList) { this.mContext = mContext;
        this.posts = productList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_contenu_course,tv_etat,tv_courseurC,tv_tarif_course;
        Button btn_delete ;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_contenu_course = itemView.findViewById(R.id.tv_contenu_course);
            tv_etat = itemView.findViewById(R.id.tv_etat);
            tv_courseurC = itemView.findViewById(R.id.tv_courseurC);
            tv_tarif_course = itemView.findViewById(R.id.tv_tarifCard);
            btn_delete = itemView.findViewById(R.id.btn_delete);


        }
    }
}
