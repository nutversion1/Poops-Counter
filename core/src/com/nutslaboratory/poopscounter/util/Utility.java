package com.nutslaboratory.poopscounter.util;

public class Utility {

    public static String convertSecondToMinuteFormat(int total){
        String minute = String.valueOf(total / 60);
        /*
        if(Integer.parseInt(minute) < 10){
            minute = "0" + minute;
        }*/

        String second = String.valueOf(total % 60);
        if(Integer.parseInt(second) < 10){
            second = "0" + second;
        }

        String timeString = minute + ":" + second;

        return timeString;
    }
}
