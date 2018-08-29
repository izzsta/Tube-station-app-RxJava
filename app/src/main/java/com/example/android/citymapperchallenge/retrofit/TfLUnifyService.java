package com.example.android.citymapperchallenge.retrofit;

import com.example.android.citymapperchallenge.model.NearbyEndPoint.StationsWithinRadius;
import com.example.android.citymapperchallenge.model.ArrivalsEndPoint.NextArrivals;
import com.example.android.citymapperchallenge.model.SequenceEndPoint.LineSequence;

import java.util.List;
import java.util.Map;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by izzystannett on 17/04/2018.
 */

public interface TfLUnifyService {

    String URL = "https://api.tfl.gov.uk/";

    @GET("StopPoint")
    Observable<StationsWithinRadius> getNearbyStations(@QueryMap Map<String, String> options);

    @GET("StopPoint/{naptanId}/Arrivals/")
    Observable<List<NextArrivals>> getNextArrivals(@Path("naptanId") String naptanId);

    @GET("/Line/{lineId}/Route/Sequence/outbound")
    Observable<LineSequence> getLineSequence(@Path("lineId") String lineId);

}
