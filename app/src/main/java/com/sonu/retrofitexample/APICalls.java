package com.sonu.retrofitexample;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface APICalls {

    @GET("posts")
    Call<List<Post>> getAllPosts();

    @GET("posts/{id}")
    Call<Post> getSpecificPost(@Path("id") int num);

    @GET("posts/{id}/comments")
    Call<List<Post>> getSpecificPostComments(@Path("id") int num);

    @GET("comments")
    Call<List<Post>> getCoomentsofSpecificUser(@Query("postId") int num);


    @GET("comments")
    Call<List<Post>> getCoomentsofSpecificUser(@QueryMap Map<String, String> params);

    @POST("posts")
    Call<Post> createPost(@Body Post post);


    @FormUrlEncoded
    @POST("posts")
    Call<Post> createPost(@Field("userId") int userId, @Field("title") String title, @Field("body") String b);

    @FormUrlEncoded
    @POST("posts")
    Call<Post> createPost(@FieldMap Map<String, String> post);

    @PUT("posts/{id}")
    Call<Post> updatePostByPostId(@Path("id") int abc, @Body Post post);

    @PATCH("posts/{id")
    Call<Post> updatePost(@Path("id") int abc, @Body Post post);

    @DELETE("posts/{id}")
    Call<Void> deletePost(@Path("id") int id);


    @Multipart
    @POST("upload")
    Call<ResponseBody> upload(
            @Part("description") RequestBody requestBody,
            @Part MultipartBody.Part photo
    );
}
