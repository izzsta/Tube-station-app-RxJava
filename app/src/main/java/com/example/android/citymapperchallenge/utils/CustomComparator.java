package com.example.android.citymapperchallenge.utils;

import com.example.android.citymapperchallenge.model.ArrivalsEndPoint.NextArrivals;

import java.util.Comparator;

public class CustomComparator implements Comparator<NextArrivals>{
    @Override
    public int compare(NextArrivals o1, NextArrivals o2) {
        return Long.compare(o1.getTimeToStation(), o2.getTimeToStation());
    }
}
