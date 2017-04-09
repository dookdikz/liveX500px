package com.example.tanawat.liveat500px.manager.http;

import com.example.tanawat.liveat500px.dao.PhotoItemCollectionDao;
import com.example.tanawat.liveat500px.dao.TestAir;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

import retrofit2.http.Path;

/**
 * Created by Tanawat on 23/9/2559.
 */
public interface ApiService {
    @POST("list")
    Call<PhotoItemCollectionDao> loadPhotoList();
    @POST("list/after/{id}")
    Call<PhotoItemCollectionDao> loadPhotoListAfterId(@Path("id") int id);
    @POST("list/before/{id}")
    Call<PhotoItemCollectionDao> loadPhotoListBeforeId(@Path("id") int id);



}
