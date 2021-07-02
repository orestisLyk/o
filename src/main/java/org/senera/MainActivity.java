package org.senera;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.senera.senera.BuildConfig;
import org.senera.senera.R;
import org.senera.server.ws.Measurement;

import java.util.HashMap;
import java.util.Iterator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static org.senera.senera.R.id.chart;


public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel viewModel;


    private UsbDevice device;

    Context context = this;



    /**
     * List of USB devices that are available.
     */
    private String[] devices;
    private HashMap<String, UsbDevice> deviceList;

    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    // String for LogCat documentation
    private final static String TAG = "SENERA-MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        if (viewModel.getBarData() != null){
            drawChart();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //fill the list of the USB devices
                devices = enumerateDevices();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.select_usb_title);
                builder.setItems(devices, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        setUsbDevice(deviceList.get(devices[which]), false);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        Button stopServiceButton = (Button) findViewById(R.id.stopServiceBtn);
        stopServiceButton.setEnabled(((SeneraApp)getApplication()).isServiceStarted());
        stopServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService();
            }
        });

        Button startServiceButton = (Button) findViewById(R.id.startServiceBtn);
        startServiceButton.setEnabled(!((SeneraApp)getApplication()).isServiceStarted());
        startServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startService();
            }
        });

        // Set preferences to default values if they have not already been modified by the user
        PreferenceManager.setDefaultValues(this, R.xml.pref_senera, false);

        // Define an intent filter for receiving status updates from the command service
        // and register the StatusReceiver inner class as a receiver for status intents.
        IntentFilter statusIntentFilter = new IntentFilter(Constants.BROADCAST_ACTION);
        StatusReceiver statusReceiver = new StatusReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(statusReceiver, statusIntentFilter);

        // Define an intent filter for receiving measurement
        // and register the ChartReceiver inner class as a receiver for measurement intents.
        IntentFilter chartIntentFilter = new IntentFilter(Constants.LISTENER_ACTION);
        ChartReceiver chartReceiver = new ChartReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(chartReceiver, chartIntentFilter);


        //Request permission for all USB devices.
//        devices = enumerateDevices();
//        for (int i = 0; i < devices.length; i++) {
//            String s = devices[i];
//            UsbDevice device = deviceList.get(s);
//            Intent intent = new Intent(ACTION_USB_PERMISSION);
//            PendingIntent mPermissionIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//            IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
//            registerReceiver(mUsbReceiver, filter);
//            UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
//            manager.requestPermission(device, mPermissionIntent);
//        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_version) {
            showVersionDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Sets the USB device.
     *
     * @param device
     * @param startService indicates whether the service will be started after the
     *                     device is set.
     */
    public void setUsbDevice(UsbDevice device, boolean startService) {
        this.device = device;
        Toast.makeText(getApplicationContext(), device.toString(), Toast.LENGTH_SHORT).show();

        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);

        //Request permission
        Intent intent = new Intent(ACTION_USB_PERMISSION);
        intent.putExtra("org.senera.startService", startService);
        PendingIntent mPermissionIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(mUsbReceiver, filter);
        manager.requestPermission(device, mPermissionIntent);
    }


    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            //call method to set up device communication
                            SeneraConfiguration config = ((SeneraApp) getApplication()).getSeneraConfiguration();
                            config.setDevice(device);
                            if (intent.getBooleanExtra("org.senera.startService", false)) {
                                startServiceInternal();
                            }
                        }
                    } else {
                        Log.d(TAG, "permission denied for device " + device);
                    }
                }
            }
        }
    };


    /**
     * Starts the command service
     */
    private void startService() {



        SeneraConfiguration config = ((SeneraApp) getApplication()).getSeneraConfiguration();
        String sensorType = config.getSensorType(this);
        if (device == null ) { //prompt the user to select a USB device if we are using an ANSeeN sensor.
            //fill the list of the USB devices

            devices = enumerateDevices();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.select_usb_title);
            builder.setItems(devices, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //set the usb device indicating that we also want to start the service.
                    setUsbDevice(deviceList.get(devices[which]), true);
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            startServiceInternal();
        }
    }

    private void startServiceInternal() {
        // startForegroundService was added in Android Oreo.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (startForegroundService(new Intent(getBaseContext(), ControllerService.class)) != null){
                toggleServiceButtons(true);
            }
        } else {
            if (startService(new Intent(getBaseContext(), ControllerService.class)) != null){
                toggleServiceButtons(true);
            }
        }
    }

    /**
     * Stops the command service
     */
    private void stopService() {
        stopService(new Intent(getBaseContext(), ControllerService.class));
        toggleServiceButtons(false);
    }

    /**
     * Enables and disables the start/stop service buttons
     * based on whether the service is running or not.
     *
     * @param serviceRunning
     */
    private void toggleServiceButtons(boolean serviceRunning){
        Button startServiceButton = (Button) findViewById(R.id.startServiceBtn);
        startServiceButton.setEnabled(!serviceRunning);
        Button stopServiceButton = (Button) findViewById(R.id.stopServiceBtn);
        stopServiceButton.setEnabled(serviceRunning);
    }

    /**
     * Returns a list of all USB devices that are available.
     *
     * @return
     */
    private String[] enumerateDevices() {
        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        deviceList = manager.getDeviceList();
        int len = deviceList.size();
        String[] devices = new String[len];
        int i = 0;
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        while (deviceIterator.hasNext()) {
            UsbDevice device = deviceIterator.next();
            devices[i] = device.getDeviceName();
            i++;
        }
        return devices;
    }

    /**
     * Shows an alert dialog containing the current version of
     * the software.
     */
    private void showVersionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.version_title);
        builder.setMessage("version: " + Version.getVersion());

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(mUsbReceiver);
        } catch (IllegalArgumentException e) {
            //receiver was already unregistered.
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Broadcast receiver for receiving status updates from the command service.
     *
     */
    private class StatusReceiver extends BroadcastReceiver {
        // Called when the BroadcastReceiver gets an Intent it's registered to receive
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra(Constants.EXTENDED_DATA_STATUS);
            EditText logMessages = (EditText) findViewById(R.id.logMessages);
            // Check if we are in the middle or the end of a loop
            if (message.startsWith("100 Repetition ")){
                if (viewModel.isLoopFinished()){
                    viewModel.count = 0;
                    viewModel.setLoopFinished(false);
                }
            } else if (message.startsWith("200 LOOP finished")){
                viewModel.setLoopFinished(true);
            }
            logMessages.append(message + "\n");
            // if (isloopfinished() && graph is uncalibrated) insert prompt to use the graph for calibration(if possible,the
            //graph should stay visible while the user types the values of the parameters for graphCallibrationLinear(...)
        }
    }


    /**
     * Broadcast receiver for receiving measurement data.
     *
     */
    private class ChartReceiver extends BroadcastReceiver {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String calType = sharedPref.getString("pref_key_calibration_type","manual");
        // Called when the BroadcastReceiver gets an Intent it's registered to receive
        @Override

        public void onReceive(Context context, Intent intent) {

            int[] chartValues = intent.getIntArrayExtra(Constants.EXTENDED_DATA_CHART);
            if(calType.equalsIgnoreCase("manual")) {

                viewModel.setBarDataCalibrated(chartValues, Float.parseFloat(sharedPref.getString("energy_per_channel", "1")), Float.parseFloat(sharedPref.getString("energy_constant", "0")));
            }else if(calType.equalsIgnoreCase("graph")){
                viewModel.setBarDataGraphCalibrated(chartValues,Float.parseFloat(sharedPref.getString("pref_key_Calibration_energy_1", "1")),Float.parseFloat(sharedPref.getString("pref_key_Calibration_energy_2", "2")),Integer.parseInt(sharedPref.getString("pref_key_Calibration_channel_for_energy_1","1")),Integer.parseInt(sharedPref.getString("pref_key_Calibration_channel_for_energy_2","2")));
            }
            else{
                viewModel.setBarData(chartValues);
            }
            viewModel.count++;

            drawChart();


        }
    }

    /**
     * Draws the chart based on the data stored in the view model.
     */
    private void drawChart() {
        BarChart chart = (BarChart) findViewById(R.id.chart);
        chart.setData(viewModel.getBarData());
        chart.setFitBars(true);
        chart.getLegend().setEnabled(false);
        //chart.getDescription().setEnabled(false);
        chart.getDescription().setText(Integer.toString(viewModel.count));
        chart.invalidate(); // refresh
        //notifyDataSetChanged()
        //
          }






    }


