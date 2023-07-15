package com.example.chinesechesstrainning.service;

import com.example.chinesechesstrainning.model.Training;
import com.example.chinesechesstrainning.model.Piece;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IAPIService {
    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
    IAPIService apiService = new Retrofit.Builder()
            .baseUrl("http://192.168.145.1:8080/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(IAPIService.class);


    @GET("/pieces")
    Call<ArrayList<Piece>> getPieces();

    @GET("/matches")
    Call<ArrayList<Training>> getMatches();

    @GET("/match/{matchId}/parent")
    Call<Training> getParentMatchById(@Path("matchId") long matchId);

    @GET("/match/{matchId}/children")
    Call<ArrayList<Training>> getChildMatchesById(@Path("matchId") long matchId);

    @GET("/match/{matchId}/children")
    Call<ArrayList<Training>> getMatchDetailsById(@Path("matchId") long matchId);

}
