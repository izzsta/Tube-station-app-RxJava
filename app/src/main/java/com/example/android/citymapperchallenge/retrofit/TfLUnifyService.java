package com.example.android.citymapperchallenge.retrofit;

import com.example.android.citymapperchallenge.nearbyStations.StationsWithinRadius;
import com.example.android.citymapperchallenge.nextArrivals.NextTenTrains;

import java.util.ArrayList;
import java.util.List;


import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by izzystannett on 17/04/2018.
 */

public interface TfLUnifyService {

    String URL = "https://api.tfl.gov.uk/";

    @GET("StopPoint?lat=51.5025&lon=-0.1348&stopTypes=NaptanMetroStation&radius=1000&modes=tube")
    Observable<StationsWithinRadius> getNearbyStations();

    @GET("StopPoint/940GZZLUSJP/Arrivals")
    //TODO: change 940... to {naptanID}
    Call<List<NextTenTrains>> getNextArrivals();
}
