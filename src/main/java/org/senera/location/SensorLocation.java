/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.senera.location;

import android.content.Context;
import android.util.Log;

import org.senera.SeneraConfiguration;

/**
 *
 * @author George
 */
public class SensorLocation {

    private static final String TAG = "SENERA-SensorLocation";

    public static float getLon(Context context){
        if (SeneraConfiguration.useStaticLocation(context)){
            return SeneraConfiguration.getStaticLon(context);
        } else {
            Log.w(TAG, "Dynamic location not supported yet. Using static location.");
            return SeneraConfiguration.getStaticLon(context);
        }
    }
    
    public static float getLat(Context context){
        if (SeneraConfiguration.useStaticLocation(context)){
            return SeneraConfiguration.getStaticLat(context);
        } else {
            Log.w(TAG, "Dynamic location not supported yet. Using static location.");
            return SeneraConfiguration.getStaticLat(context);
        }     
    }
    
    public static float getAlt(Context context){
        if (SeneraConfiguration.useStaticLocation(context)){
            return SeneraConfiguration.getStaticAlt(context);
        } else {
            Log.w(TAG, "Dynamic location not supported yet. Using static location.");
            return SeneraConfiguration.getStaticAlt(context);
        }
    }
}
