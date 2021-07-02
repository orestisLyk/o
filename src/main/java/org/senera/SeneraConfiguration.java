package org.senera;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.usb.UsbDevice;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.ftdi.j2xx.D2xxManager;

import org.senera.anseen.AnseenConfiguration;
import org.senera.anseen.AnseenManagerImpl;
import org.senera.anseen.AnseenAndroidUsbController;
import org.senera.comm.JD2XXAndroidCommManager;
import org.senera.comm.JD2XXDeviceType;
import org.senera.manager.ChartSpectrumListener;
import org.senera.manager.WSSpectrumListener;
import org.senera.comm.CommException;
import org.senera.comm.CommManager;
import org.senera.manager.SensorManager;
import org.senera.mgs.DummyManagerImpl;
import org.senera.mgs.MgsManagerImpl;
import org.senera.ritec.RitecConfiguration;
import org.senera.ritec.RitecManagerImpl;

/**
 * Created by George on 15/4/2017.
 */

public class SeneraConfiguration {
    private final static String TAG = "SENERA-SeneraConfiguration";

    private UsbDevice device;
    private Context context;

    //Sensor type constants
    public static String SENSOR_TYPE_MGS = "MGS";
    public static String SENSOR_TYPE_ANSEEN = "ANSEEN";
    public static String SENSOR_TYPE_DUMMY = "DUMMY";
    public static String SENSOR_TYPE_RITEC ="RITEC";

    //Default values

    //Sensor configuration
    //private static final short SENSOR_ID = 1;
    private static final String SENSOR_ID = "1";
    //Sensor type: MGS, ANSEEN, DUMMY
    private static final String SENSOR_TYPE = "RITEC";//test version of this line to see if sensor type bug continues
    //original line: private static final String SENSOR_TYPE = "DUMMY"

    //SERIAL PORT CONFIGURATION
    private static final String SERIAL_PORT = "COM7";
    private static final int BAUDRATE = 9600;
    private static final int DATABITS = 8;
    private static final int STOPBITS = 1;
    private static final int PARITY = 0;

    //JD2XX SERIAL PORT CONFIGURATION
    private static final String DEVICE_SERIAL_NUMBER = "FT90UGUY";

    //CONTROL PORT CONFIGURATION
    //private static final int CONTROL_PORT = 6000;
    private static final String CONTROL_PORT = "6000";

    //FUSION SERVER CONFIGURATION
    private static final String FUSION_SERVER_ADDRESS = "localhost";
    //private static final int FUSION_SERVER_PORT = 8080;
    private static final String FUSION_SERVER_PORT = "8080";

    //STATIC LOCATION CONFIGURATION
    private static final boolean USE_STATIC_LOCATION = true; //indicates whether static location information will be used
    private static final float STATIC_LOC_LON = 23.819060f; //longitude
    private static final float STATIC_LOC_LAT = 37.999069f; //latitude
    private static final float STATIC_LOC_ALT = 266.0f; //altitude

    //SPECTRUM LISTENERS
    private static final boolean USE_CONSOLE_SPECTRUM_LISTENER = true;
    private static final boolean USE_FILE_SPECTRUM_LISTENER = true;
    private static final String FILE_SPECTRUM_LISTENER_FILENAME = "spectrum.txt";
    private static final boolean USE_CSV_FILE_SPECTRUM_LISTENER = true;
    private static final String CSV_FILE_SPECTRUM_LISTENER_FILENAME = "spectrum.csv";
    private static final boolean USE_WS_SPECTRUM_LISTENER = true;

    //ADVANCED SETTINGS
    private static final String JD2XX_PORT_TIMEOUT = "50"; //jd2xx wait loop in millions

    //END OF CONFIGURATION


//    private short sensorId;
//    private String sensorType;
//    private int CONTROL_PORT;
//    private String fusionServerAddress;
//    private int fusionServerPort;
//    private String deviceSerialNumber;
//    private String deviceSerialPort;
//    private boolean useStaticLocation;
//    private float staticLon;
//    private float staticLat;
//    private float staticAlt;
//    private boolean useConsoleSpectrumListener;
//    private boolean useFileSpectrumListener;
//    private String fileSpectrumListenerFilename;
//    private boolean useCsvFileSpectrumListener;
//    private String csvFileSpectrumListenerFilename;
//    private boolean useWSSpectrumListener;


    private static SensorManager sensorManager;
    private static CommManager commManager;





    public SeneraConfiguration(Context context) {
        this.context = context;
        //Load the configuration
        loadConfiguration();

    }

    /**
     * Selects the appropriate sensor manager and configures it.
     * Also adds configured spectrum listeners..
     */
    public void loadConfiguration() {
        if (sensorManager == null) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            String sensorType = getSensorType(context);
            if (sensorType.equalsIgnoreCase("MGS")){
                try {
                    this.commManager = new JD2XXAndroidCommManager(getDeviceSerialNumber(context), JD2XXDeviceType.MGS, D2xxManager.getInstance(context), context);
                    this.sensorManager = new MgsManagerImpl(commManager);
                } catch (D2xxManager.D2xxException e) {
                    Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                } catch (CommException e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }else if (sensorType.equalsIgnoreCase("ANSEEN")) {
                AnseenConfiguration sensorConfig = new AnseenConfiguration();
                sensorConfig.setGain(Integer.parseInt(sharedPref.getString("pref_key_anseen_gain", Integer.toString(AnseenConfiguration.DEFAULT_GAIN))));
                sensorConfig.setLLD1(Integer.parseInt(sharedPref.getString("pref_key_anseen_lld1", Integer.toString(AnseenConfiguration.DEFAULT_LLD1))));
                sensorConfig.setULD1(Integer.parseInt(sharedPref.getString("pref_key_anseen_uld1", Integer.toString(AnseenConfiguration.DEFAULT_ULD1))));

                sensorConfig.setHvEnabled(Integer.parseInt(sharedPref.getString("pref_key_anseen_hv_enabled", Integer.toString(AnseenConfiguration.DEFAULT_HV_ENABLED))));
                sensorConfig.setHvValue(Integer.parseInt(sharedPref.getString("pref_key_anseen_hv_value", Integer.toString(AnseenConfiguration.DEFAULT_HV_VALUE))));
                sensorConfig.setOffset(Integer.parseInt(sharedPref.getString("pref_key_anseen_offset", Integer.toString(AnseenConfiguration.DEFAULT_OFFSET))));

                //Find the USB device based on the device serial number in the configuration
                //The following requires API level 25 (Android 5.0) or later.
//                UsbManager manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
//                HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
//                UsbDevice device;
//                String deviceSerialNr = getDeviceSerialNumber(context);
//                Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
//                while(deviceIterator.hasNext()){
//                    UsbDevice d = deviceIterator.next();
//                    if (d.getSerialNumber().equals(deviceSerialNr)){
//                        device = d;
//                        break;
//                    }
//                }
                if (device != null) {
                    this.sensorManager = new AnseenManagerImpl(new AnseenAndroidUsbController(device, context), sensorConfig);
                }
            } else if (sensorType.equalsIgnoreCase("RITEC")) {
                RitecConfiguration sensorConfig = new RitecConfiguration();
                sensorConfig.setChannels(Integer.parseInt(sharedPref.getString("pref_key_ritec_channels", Integer.toString(RitecConfiguration.DEFAULT_CHANNELS))));
                sensorConfig.setLld(Integer.parseInt(sharedPref.getString("pref_key_ritec_lld", Integer.toString(RitecConfiguration.DEFAULT_LLD))));
                sensorConfig.setUld(Integer.parseInt(sharedPref.getString("pref_key_ritec_uld", Integer.toString(RitecConfiguration.DEFAULT_ULD))));
                sensorConfig.setHighVoltage(Integer.parseInt(sharedPref.getString("pref_key_ritec_high_voltage", Integer.toString(RitecConfiguration.DEFAULT_HIGH_VOLTAGE))));
                sensorConfig.setHvInhibit(Integer.parseInt(sharedPref.getString("pref_key_ritec__hv_inhibit", Integer.toString(RitecConfiguration.DEFAULT_HV_INHIBIT))));
                sensorConfig.setEnergyPerChannelA(Float.parseFloat(sharedPref.getString("pref_key_ritec_energy_per_channel_a", Float.toString(RitecConfiguration.DEFAULT_RITEC_ENERGY_PER_CHANNEL_A))));
                sensorConfig.setEnergyPerChannelB(Float.parseFloat(sharedPref.getString("pref_key_ritec_energy_per_channel_b", Float.toString(RitecConfiguration.DEFAULT_RITEC_ENERGY_PER_CHANNEL_B))));
                try {
                    this.commManager = new JD2XXAndroidCommManager(getDeviceSerialNumber(context), JD2XXDeviceType.RITEC, D2xxManager.getInstance(context), context);
                    this.sensorManager = new RitecManagerImpl(commManager , sensorConfig);
                } catch (D2xxManager.D2xxException e) {
                    Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                } catch (CommException e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }else if (sensorType.equalsIgnoreCase("DUMMY")){
                this.sensorManager = new DummyManagerImpl();
            }else{
                this.sensorManager = new DummyManagerImpl();
            }

            //Add spectrum listeners
            //only WS spectrum listener is implemented
            if (sensorManager != null) {
//        if (SeneraConfiguration.useConsoleSpectrumListener(context)){
//            sensorManager.addSpectrumListener(new ConsoleSpectrumListener());
//        }
//        if (SeneraConfiguration.useFileSpectrumListener(context)){
//            sensorManager.addSpectrumListener(new FileSpectrumListener(SeneraConfiguration.getFileSpectrumListenerFilename()));
//        }
//        if (SeneraConfiguration.useCsvFileSpectrumListener(context)){
//            sensorManager.addSpectrumListener(new CsvFileSpectrumListener(SeneraConfiguration.getCsvFileSpectrumListenerFilename()));
//        }
                if (useWSSpectrumListener(context)){
                    sensorManager.addSpectrumListener(new WSSpectrumListener(context));
                }

                //Chart spectrum listener is allways enabled in android
                sensorManager.addSpectrumListener(new ChartSpectrumListener(context));
            }


        }


        //DEBUG
//        System.out.println("***************************************************");
//        System.out.println("Loaded configuration:");
//        System.out.println("Sensor Id: " + getSensorId());
//        System.out.println("Sensor Type: " + getSensorType());
//        System.out.println("Device serial number: " + getDeviceSerialNumber());
//        System.out.println("Device serial port: " + getDeviceSerialPort());
//        System.out.println("Control port: " + getCONTROL_PORT());
//        System.out.println("Fusion server address: " + getFusionServerAddress());
//        System.out.println("Fusion server port: " + getFusionServerPort());
//        System.out.println("Use static location (T/F): " + useStaticLocation());
//        System.out.println("Static Longitude: " + getStaticLon());
//        System.out.println("Static Latitude: " + getStaticLat());
//        System.out.println("Static Altitude: " + getStaticAlt());
//        System.out.println("Console Spectrum Listener (T/F): " + useConsoleSpectrumListener());
//        System.out.println("File Spectrum Listener (T/F): " + useFileSpectrumListener());
//        if (useFileSpectrumListener()){
//            System.out.println("File Spectrum Listener Filename: " + getFileSpectrumListenerFilename());
//        }
//        System.out.println("CSV File Spectrum Listener (T/F): " + useCsvFileSpectrumListener());
//        if (useFileSpectrumListener()){
//            System.out.println("CSV File Spectrum Listener Filename: " + getCsvFileSpectrumListenerFilename());
//        }
//        System.out.println("WebService Spectrum Listener (T/F): " + useWSSpectrumListener());
//        System.out.println("Working Directory = " + System.getProperty("user.dir"));
//        //System.out.println("java.library.path = " + System.getProperty("java.library.path"));
//        System.out.println("***************************************************");

    }


    public static short getSensorId(final Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return Short.parseShort(sharedPref.getString("pref_key_sensor_id", SENSOR_ID));
    }

    public static String getSensorType(final Context context){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString("pref_key_sensor_type", SENSOR_TYPE);
    }

    public static int getCONTROL_PORT(final Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.parseInt(sharedPref.getString("pref_key_sensor_control_port", CONTROL_PORT));
    }

    public static String getFusionServerAddress(final Context context){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString("pref_key_fusion_server_address", FUSION_SERVER_ADDRESS);
    }

    public static int getFusionServerPort(final Context context){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.parseInt(sharedPref.getString("pref_key_fusion_server_port", FUSION_SERVER_PORT));
    }

    public static String getDeviceSerialNumber(final Context context){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString("pref_key_device_serial_number", DEVICE_SERIAL_NUMBER);
    }

    public static boolean useStaticLocation(final Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getBoolean("pref_key_use_static_location", USE_STATIC_LOCATION);
    }

    public static float getStaticLon(final Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return Float.parseFloat(sharedPref.getString("pref_key_static_loc_lon", Float.toString(STATIC_LOC_LON)));
    }

    public static float getStaticLat(final Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return Float.parseFloat(sharedPref.getString("pref_key_static_loc_lat", Float.toString(STATIC_LOC_LAT)));
    }

    public static float getStaticAlt(final Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return Float.parseFloat(sharedPref.getString("pref_key_static_loc_alt", Float.toString(STATIC_LOC_ALT)));
    }

    public static boolean useConsoleSpectrumListener(final Context context){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getBoolean("pref_key_use_console_spectrum_listener", USE_CONSOLE_SPECTRUM_LISTENER);
    }

    public static boolean useFileSpectrumListener(final Context context){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getBoolean("pref_key_use_file_spectrum_listener", USE_FILE_SPECTRUM_LISTENER);
    }

    public static String getFileSpectrumListenerFilename(final Context context){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString("pref_key_file_spectrum_listener_filename", FILE_SPECTRUM_LISTENER_FILENAME);
    }

    public static boolean useCsvFileSpectrumListener(final Context context){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getBoolean("pref_key_use_csv_spectrum_listener", USE_CSV_FILE_SPECTRUM_LISTENER);
    }

    public static String getCsvFileSpectrumListenerFilename(final Context context){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString("pref_key_csv_spectrum_listener_filename", CSV_FILE_SPECTRUM_LISTENER_FILENAME);
    }

    public static boolean useWSSpectrumListener(final Context context){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getBoolean("pref_key_use_ws_spectrum_listener", USE_WS_SPECTRUM_LISTENER);
    }

    public static int getJd2xxPortTimeout(final Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.parseInt(sharedPref.getString("pref_key_jd2xx_port_timeout", JD2XX_PORT_TIMEOUT));
    }



    public SensorManager getSensorManager() {
        return sensorManager;
    }

    public UsbDevice getDevice() {
        return device;
    }

    public void setDevice(UsbDevice device) {
        this.device = device;
        //reload configuration
        loadConfiguration();
    }
}
