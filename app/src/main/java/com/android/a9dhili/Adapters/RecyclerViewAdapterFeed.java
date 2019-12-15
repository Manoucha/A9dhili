package com.android.a9dhili.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.a9dhili.Models.FeedModel;
import com.android.a9dhili.Models.Post;
import com.android.a9dhili.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapterFeed extends RecyclerView.Adapter<RecyclerViewAdapterFeed.MyViewHolder> {

    private Context context;
    private List<Post> mData;
    RequestManager glide;

    public RecyclerViewAdapterFeed(Context context, List<Post> mData) {
        this.context = context;
        this.mData = mData;
        this.glide = Glide.with(context);
    }

    @NonNull
    @Override
    public RecyclerViewAdapterFeed.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater= LayoutInflater.from(context);
        view=inflater.inflate(R.layout.single_post,parent,false);

        return new RecyclerViewAdapterFeed.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterFeed.MyViewHolder holder, final int position) {

        holder.contenu.setText(mData.get(position).getContenu());
        holder.tv_etat.setText(mData.get(position).getEtat());
        holder.tags.setText(mData.get(position).getTags());
        holder.tv_nameUser.setText(mData.get(position).getId_user());
        holder.tarif.setText(String.format("%s", mData.get(position).getTarif()));
        holder.adresse.setText(String.format("%s", mData.get(position).getLongitude()));





    }

    @Override
    public int getItemCount() {
        return  mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView tv_nameUser,tv_etat,adresse,tags,contenu,tarif;

        public MyViewHolder(@NonNull View mView) {
            super(mView);

            tv_nameUser=(TextView) mView.findViewById(R.id.tv_nameUser);
            tv_etat=(TextView) mView.findViewById(R.id.tv_etat);
            adresse=(TextView) mView.findViewById(R.id.adr_post);
            tags=(TextView) mView.findViewById(R.id.tv_tags);
            contenu=(TextView) mView.findViewById(R.id.tv_contenu);
            tarif=(TextView) mView.findViewById(R.id.tv_tarif);


        }
    }

}
