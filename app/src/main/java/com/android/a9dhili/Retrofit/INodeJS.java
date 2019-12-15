package com.android.a9dhili.Retrofit;

import com.android.a9dhili.Models.Course;
import com.android.a9dhili.Models.Post;
import com.android.a9dhili.Models.User;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface INodeJS {
    @POST("register")
    @FormUrlEncoded
    Observable<String> registerUser(@Field("nom") String nom,
                                    @Field("prenom") String prenom,
                                    @Field("email") String email,
                                    @Field("password") String password,
                                    @Field("latitude") double latitude,
                                    @Field("longitude") double longitude);

    @POST("login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("email") String email,
    @Field("password") String password);

    @GET("users/{email}")
    Call<User> getUserByEmail(@Path(value="email") String email);

    @GET("user/{unique_id}")
    Call<User> getUserById(@Path(value="unique_id") String unique_id);


    @GET("coursesToDo/{idUser}")
    Call<List<Post>> getCoursesToDo(@Path(value="idUser") String id);

    @GET("/mycourses/{idUser}")
    Call<List<Post>> getMyCourses(@Path(value="idUser") String id);

    @PUT("approuver/{idCourseur}/{idUser}/{etat}/{idPost}")
    Call<Void> approuverPost(@Path(value="idCourseur") String idCourseu , @Path(value="idUser") String idUser ,@Path(value="etat") int etat,@Path(value = "idPost") int idPost);


    @POST("addPost")
    @FormUrlEncoded
    Call<String>addPost(@Field("contenu") String contenu,
                        @Field("tags") String prenom,
                        @Field("etat") int etat,
                        @Field("id_user") String id_user,
                        @Field("tarif") float tarif,
                        @Field("longitude") double latitude,
                        @Field("latitude") double longitude);


    @GET("posts/")
    Call<List<Post>> getPosts();

    @POST("addPost")
    Call<Post>addPost1(Post p );

    @POST("addCourse")
    @FormUrlEncoded
    Call<Void>addCourse(@Field("idCourseur") String idC,
                        @Field("idUser") String id,
                        @Field("etat") int etat
                      );

    @DELETE("deletePost/{id}")
    Call<ResponseBody> deletePost(@Path("id") int id);

}
