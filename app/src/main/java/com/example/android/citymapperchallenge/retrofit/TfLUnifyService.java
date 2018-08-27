package com.example.android.citymapperchallenge.retrofit;

import com.example.android.citymapperchallenge.model.NearbyEndPoint.StationsWithinRadius;
import com.example.android.citymapperchallenge.model.ArrivalsEndPoint.NextArrivals;
import com.example.android.citymapperchallenge.model.SequenceEndPoint.LineSequence;

import java.util.List;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by izzystannett on 17/04/2018.
 */

public interface TfLUnifyService {

    String URL = "https://api.tfl.gov.uk/";

    @GET("StopPoint?lat=51.5025&lon=-0.1348&stopTypes=NaptanMetroStation&radius=1000&modes=tube")
    Observable<StationsWithinRadius> getNearbyStations();

    @GET("StopPoint/{naptanId}/Arrivals")
    Observable<List<NextArrivals>> getNextArrivals(@Path("naptanId") String naptanId);

    @GET("/Line/{lineId}/Route/Sequence/outbound")
    Observable<LineSequence> getLineSequence(@Path("lineId") String lineId);

}
