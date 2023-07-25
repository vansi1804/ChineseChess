package com.example.chinesechesstrainning.api;

import com.example.chinesechesstrainning.model.training.TrainingDTO;
import com.example.chinesechesstrainning.model.training.TrainingDetailDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TrainingAPI {

    @GET("/trainings")
    Call<List<TrainingDTO>> findAll();

    @GET("/trainings/id={id}/children")
    Call<List<TrainingDTO>> findAllChildrenById(@Path("id") long id);

    @GET("/trainings/id={id}")
    TrainingDTO findById(@Path("id") long id);

    @GET("/trainings/id={id}/details")
    TrainingDetailDTO findDetailsById(@Path("id") long id);

}
