package com.example.translator;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestApi {

    @GET("translate")
    Call<Adapter> getText (@Query("key") String key,
                           @Query("lang") String lang,
                           @Query("text") String text,
                           @Query("options") int options);
    @GET("detect")
    Call<Adapter> getText (@Query("key") String key,
                           @Query("text") String text);
}
