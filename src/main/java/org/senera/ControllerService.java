package org.senera;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.senera.controller.ControllerImpl;
import org.senera.controller.SeneraControlEventListener;
import org.senera.manager.SensorManager;
import org.senera.senera.R;
import org.senera.ws.RegistrationWSClient;

public class ControllerService extends Service {

    private static final int NOTIFICATION_ID = 1;

    private static final String NOTIFICATION_CHANNEL_ID = "senera_service_channel";

    private ControllerImpl controller;

    public ControllerService() {
    }

    @Override
    public void onCreate() {
        SeneraConfiguration config = ((SeneraApp)getApplication()).getSeneraConfiguration();
        config.loadConfiguration();
        SensorManager sensorManager = config.getSensorManager();
        if (sensorManager == null){
            stopSelf();
        } else {
            controller = new ControllerImpl(getApplicationContext(), this);
            controller.addCommandListener(new SeneraControlEventListener(sensorManager));
            sensorManager.setController(controller);
            createNotificationChannel();
            Notification notification = createNotification();
            startForeground(NOTIFICATION_ID, notification);
            register();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ((SeneraApp)getApplication()).setServiceStarted(true);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        //remove notification
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.cancel(NOTIFICATION_ID);

        if (controller != null) {//if the sensor manager is null for a reason then the service is stopped before creating the controller
            controller.stopThread();
        }

        ((SeneraApp)getApplication()).setServiceStarted(false);

        super.onDestroy();
    }

    /**
     * Creates a notification channel, as required for Android Oreo and later versions.
     */
    private void createNotificationChannel() {

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    /**
     * Creates a notification that is displayed in the
     * notification area.
     * The notification is displayed even if the application is terminated.
     * It is through this notification that the user can reactivate the
     * application and terminate the service.
     */
    private Notification createNotification(){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_radiation_white)
                        .setContentTitle("SENERA service")
                        .setContentText("SENERA service is running.")
                        .setOngoing(true)
                        .setCategory(Notification.CATEGORY_SERVICE);

        // Creates an explicit intent for an Activity in your app
        Intent mainIntent = new Intent(this, MainActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(mainIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = mBuilder.build();
        mNotificationManager.notify(NOTIFICATION_ID, notification);

        return notification;
    }


    /**
     * Register this client to the fusion server
     */
    private void register(){
        SeneraConfiguration config = ((SeneraApp)getApplication()).getSeneraConfiguration();

        final String serverHostname = config.getFusionServerAddress(getApplicationContext());
        final int serverPort = config.getFusionServerPort(getApplicationContext());
        final String sensorId = Short.toString(config.getSensorId(getApplicationContext()));
        final String sensorType = config.getSensorType(getApplicationContext());
        final int controlPort = config.getCONTROL_PORT(getApplicationContext());

        new Thread(new Runnable() {
            public void run() {
                RegistrationWSClient client = new RegistrationWSClient(serverHostname, serverPort);
                client.register(sensorId, controlPort, sensorType);
            }
        }).start();
    }

    /**
     * Reports status to the main activity
     * @param message
     */
    public void reportStatus(String message){
        Intent localIntent = new Intent(Constants.BROADCAST_ACTION).putExtra(Constants.EXTENDED_DATA_STATUS, message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }
}
