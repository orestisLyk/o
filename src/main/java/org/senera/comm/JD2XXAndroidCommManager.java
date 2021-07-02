/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.senera.comm;

import android.content.Context;

import com.ftdi.j2xx.D2xxManager;
import com.ftdi.j2xx.FT_Device;

import org.senera.SeneraConfiguration;
import org.senera.manager.KeepAliveThread;
import org.senera.ritec.RitecCommand;
import org.senera.ritec.dataarray.InvalidRitecDataArrayException;
import org.senera.ritec.dataarray.RitecStateDataArray;
import org.senera.util.HexUtils;

import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author George
 */
public class JD2XXAndroidCommManager implements CommManager {

    private static final Logger LOG = Logger.getLogger(JD2XXAndroidCommManager.class.getName());
     
    private FT_Device jd2xxDevice;

    
    /** The serial number of the connected serial device*/
    private final String deviceSerialNumber;

    private D2xxManager d2xxManager;

    private int jd2xxLoopWait;
    
    private static final byte[] SET_COMMUNICATION_SPEED = { -86, 0, 16, 0, 0, -124, 0, 0, 112, 0, -62, 1, 0, -78, 12, -47 }; //115200bps / 3250ms timeout
    private static final byte[] SET_COMMUNICATION_SPEED_LONG_TIMEOUT = { -86, 0, 16, 0, 0, -124, 0, 0, 112, 0, -62, 1, 0, -1, -1, -111 }; //115200bps / 65535ms timeout
    
    public JD2XXAndroidCommManager(String deviceSerialNumber, JD2XXDeviceType deviceType, D2xxManager d2xxManager, Context context) throws CommException {
        this.deviceSerialNumber = deviceSerialNumber;
        this.d2xxManager = d2xxManager;

        this.jd2xxLoopWait = SeneraConfiguration.getJd2xxPortTimeout(context) * 1000000;

//        //Print information about the attached devices
//        Object[] arrayOfObject = jd2xxDevice.listDevicesBySerialNumber();
//        System.out.println("listDevicesBySerialNumber: " + arrayOfObject.length);
//        for (Object object : arrayOfObject) {
//            System.out.println(object);
//        }
//
//        arrayOfObject = jd2xxDevice.listDevicesByDescription();
//        System.out.println("listDevicesByDescription: " + arrayOfObject.length);
//        for (Object object : arrayOfObject) {
//            System.out.println(object);
//        }
//
//        arrayOfObject = jd2xxDevice.listDevicesByLocation();
//        System.out.println("listDevicesByLocation: " + arrayOfObject.length);
//        for (Object object : arrayOfObject) {
//            System.out.println(Integer.toHexString(((Integer) object).intValue()));
//        }
//
//        int i = jd2xxDevice.createDeviceInfoList();
//        System.out.println("createDeviceInfoList: " + i);
//        //JD2XX.DeviceInfo localDeviceInfo = jd2xxDevice.getDeviceInfoDetail(0);
//        //System.out.println(localDeviceInfo.toString());

        // initialize JD2XX

        if (deviceType == JD2XXDeviceType.MGS) {
            int devices = d2xxManager.createDeviceInfoList(context);
            jd2xxDevice = d2xxManager.openBySerialNumber(context, deviceSerialNumber);
        }

        if (deviceType == JD2XXDeviceType.RITEC) {
            //try to find the device with the required serial number
            byte[] command = RitecCommand.getCmdQueryState();
            int index = -1; //This will be the device index if it is found.
            int devices = d2xxManager.createDeviceInfoList(context);
            for (int i = 0; i < devices; i++) {
                jd2xxDevice = d2xxManager.openByIndex(context, i);
                jd2xxDevice.setBaudRate(115200);
                jd2xxDevice.setDataCharacteristics((byte)8, (byte)0, (byte)0);
                jd2xxDevice.setFlowControl((short)0, (byte)0, (byte)0);

                //jd2xxDevice.setTimeouts(1000, 1000);

                int serial = 0;
                boolean baudRateError;
                //if the sensor and the PC have different baud rates
                //then the sensor returns an error and switches to
                //the same baud rate as the PC. After this we can
                //resend the command.
                do {
                    baudRateError = false;
                    sendImpl(command);
                    try {
                        byte[] data = readImpl(136);
                        RitecStateDataArray sda = new RitecStateDataArray(data);
                        serial = sda.getMcaSerialNumber();
                    } catch (CommException ex) {
                        LOG.log(Level.SEVERE, ex.getMessage(), ex);
                    } catch (InvalidRitecDataArrayException irdaex) {
                        baudRateError = (irdaex.getErrorCode() == 0xA5AA);
                        LOG.log(Level.SEVERE, irdaex.getMessage(), irdaex);
                    }
                } while (baudRateError);
                System.out.println("Device index:" + i + " serial number:" + serial);
                if (Integer.toString(serial).equals(deviceSerialNumber)) {
                    index = i;
                    break;
                }

                jd2xxDevice.close();
            }
            if (index == -1) { //No device with the specified serial number was found.
                LOG.log(Level.SEVERE, "Ritec sensor with serial number " + deviceSerialNumber + " was not found.");
                throw new CommException("Ritec sensor with serial number " + deviceSerialNumber + " was not found.");
            }

        }


        if (jd2xxDevice == null){
            throw new CommException("D2xxManager openBySerialNumber returned null.");
        }

        if (deviceType == JD2XXDeviceType.MGS) {
            jd2xxDevice.setBaudRate(9600);
            jd2xxDevice.setDataCharacteristics((byte) 8, (byte) 0, (byte) 0);
            jd2xxDevice.setFlowControl((short) 0, (byte) 0, (byte) 0);

            //jd2xxDevice.setTimeouts(1000, 1000);

// setting communication speed to 115200 disabled because it was causing errors
//            LOG.finer("Setting communication speed");
//            sendImpl(SET_COMMUNICATION_SPEED_LONG_TIMEOUT);
//            try {
//                readImpl(16);
//            } catch (CommException ex) {
//                //We don't care about exceptions
//            }
        }


// setting communication speed to 115200 for MGS sensors disabled because it was causing errors
        if (deviceType == JD2XXDeviceType.RITEC) {
            jd2xxDevice.setBaudRate(115200);
        }

// keep alive thread for MGS sensors disabled because it was causing problems. Since we are now using 9600 bps, the keepalive thread is not needed anyway.
//        if (deviceType == JD2XXDeviceType.MGS) {
//            Timer timer = new Timer();
//            KeepAliveThread keepAlive = new KeepAliveThread(this);
//            timer.schedule(keepAlive, 0, 30000);
//        }
    }

    

    @Override
    public synchronized byte[] sendReceive(byte[] data, int returnLength) {
        byte[] returnData = null;
        //initializeJD2XX(); //Initialization is done only once.

        sendImpl(data);
        try {
            returnData = readImpl(returnLength);
        } catch (CommException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        
        //jd2xxDevice.close();
        
        return returnData;

    }


    /**
     * Internal implementation of the send method.
     * Sends the data passed as parameter to the sensor.
     * Must be followed immediately by a call to the read
     * method.
     * 
     * @param data the data to be sent to the sensor.
     */
    private void sendImpl(byte[] data) {
        jd2xxDevice.write(data);
    }

    /**
     * Internal implementation of the read method.
     * 
     * @param returnLength the expected length of the data that will be read from the sensor.
     * @return the data read from the sensor.
     * @throws CommException when no data or less than expected data is read from the sensor.
     */
    private byte[] readImpl(int returnLength) throws CommException{
        byte[] buffer = null;
        int i;
        for (i = jd2xxLoopWait; (jd2xxDevice.getQueueStatus() < returnLength) && (i != 0); i--) {
        }

        if (i == 0) {
            // Clear the buffer
            read(jd2xxDevice, jd2xxDevice.getQueueStatus());
            throw new CommException("No response from device, only " + jd2xxDevice.getQueueStatus() + " on request");
        } else {
            buffer = read(jd2xxDevice, returnLength);
            LOG.log(Level.FINEST, "<<<<Read from JD2XX port: {0}", HexUtils.bytesToHex(buffer));
        }
        return buffer;
    }

    private byte[] read(FT_Device jd2xxDevice, int numBytes) {
        byte[] buffer = new byte[numBytes];
        int i = jd2xxDevice.read(buffer);
        if (i == buffer.length) {
            return buffer;
        }
        byte[] arrayOfByte2 = new byte[i];
        System.arraycopy(buffer, 0, arrayOfByte2, 0, i);
        return arrayOfByte2;
    }

}
