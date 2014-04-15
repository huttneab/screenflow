package com.ridecharge.android.screenflow;

import java.util.HashMap;

/**
 * Created by ahuttner on 4/14/14.
 */
public class MajorStep {

    String activity;
    String screenCap;
    String minorStepFile;
    HashMap<String, Integer> messagesMap;


    public MajorStep(String activityName, String screenCapLocation) {
        activity = activityName;
        screenCap = screenCapLocation;

        messagesMap = new HashMap<String, Integer>();
    }

    public void setMinorStepFile(String file){
        minorStepFile = file;
    }

    public void addMinorStep(String message) {
        int count = 1;
        if (messagesMap.containsKey(message)) {
            count = messagesMap.get(message);
            count++;
        }

        messagesMap.put(message, count);
    }


}
