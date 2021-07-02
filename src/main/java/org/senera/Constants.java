package org.senera;

public final class Constants {

    /** Custom Intent action used for sending status from the service to the main activity */
    public static final String BROADCAST_ACTION = "org.senera.BROADCAST";

    /** Defines the key for the status "extra" in the broadcast action Intent */
    public static final String EXTENDED_DATA_STATUS = "org.senera.STATUS";

    /** Custom Intent action used for sending measurement data to the main activity */
    public static final String LISTENER_ACTION = "org.senera.LISTENER";

    /** Defines the key for the chart "extra" in the listener action Intent */
    public static final String EXTENDED_DATA_CHART = "org.senera.CHART";

}
