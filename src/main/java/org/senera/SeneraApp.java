package org.senera;

import android.app.Application;

/**
 * Created by George on 12/5/2017.
 */

public class SeneraApp extends Application {
    private static SeneraApp instance;

    private SeneraConfiguration seneraConfiguration;

    /** Indicates whether the senera service is running. */
    private boolean serviceStarted = false;

    public static SeneraApp getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        seneraConfiguration = new SeneraConfiguration(this);
        //Do not start the service automatically
        //startService(new Intent(getBaseContext(), ControllerService.class));
    }

    public SeneraConfiguration getSeneraConfiguration() {
        return seneraConfiguration;
    }

    public boolean isServiceStarted() {
        return serviceStarted;
    }

    public void setServiceStarted(boolean serviceStarted) {
        this.serviceStarted = serviceStarted;
    }
}
