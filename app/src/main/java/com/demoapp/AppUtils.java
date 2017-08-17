package com.demoapp;

/**
 * Created by aslan on 8/17/2017.
 */

public class AppUtils {
    static int randomWithRange(int min, int max) {
        int range = (max - min) + 1;
        return (int) (Math.random() * range) + min;
    }
}
