package com.example.android.vrgtest.Networking;

import com.example.android.vrgtest.Networking.JSONData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface WebApi {

    String API_KEY = "Ujq1d1bnDgwfOEuJNDjVZROXGUm9LO2m";
    String BASR_URL ="https://api.nytimes.com/";

    @GET("svc/mostpopular/v2/emailed/30.json?api-key=" + API_KEY)
    Call<JSONData> getMostEmailedArticles();

    @GET("svc/mostpopular/v2/shared/30.json?api-key=" + API_KEY)
    Call<JSONData> getMostSharedArticles();

    @GET("svc/mostpopular/v2/viewed/30.json?api-key=" + API_KEY)
    Call<JSONData> getMostViewedArticles();


}
