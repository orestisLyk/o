/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.senera.anseen;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;

import com.anseen.dmcalib.DMCA001;
import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/**
 *
 * @author George
 */
public class AnseenAndroidUsbController implements AnseenSerialController {

    private static final Logger LOG = Logger.getLogger(AnseenAndroidUsbController.class.getName());

    private final Context context;
    private final UsbDevice device;
    private UsbSerialDevice serial;
    private boolean isPortOpen = false;

    private List<AnseenSerialEventListener> listeners = new ArrayList();

    public AnseenAndroidUsbController(UsbDevice device, Context context) {
        this.device = device;
        this.context = context;
        openPort();

    }

    @Override
    public void addSerialEventListener(AnseenSerialEventListener listener) {
        listeners.add(listener);
    }

    private void notifySpectrumListeners(byte[] data) {
        for (AnseenSerialEventListener listener : listeners){
            listener.process(data);
        }
    }

    private void openPort() {
        if (!isPortOpen) {
            UsbManager manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
            UsbDeviceConnection connection = manager.openDevice(device);
            serial = UsbSerialDevice.createUsbSerialDevice(device, connection);
            serial.open();
            serial.setBaudRate(DMCA001.BAUD_RATE);
            serial.setDataBits(DMCA001.DATA_BITS);
            serial.setStopBits(DMCA001.STOP_BITS);
            serial.setParity(DMCA001.PARITY);
            serial.setFlowControl(DMCA001.FLOW_CONTROL);

            isPortOpen = true;
        }
    }



    private void closePort(){
        if (isPortOpen){
            serial.close();
        }
    }

    @Override
    public void sendCommand(byte[] command){
        if (isPortOpen){
            serial.read(mCallback); 
            serial.write(command);
        }
    }
    
    
    private void process(byte[] data) {
        if (data == null) {
            return;
        }
        notifySpectrumListeners(data);
    }



    private UsbSerialInterface.UsbReadCallback mCallback = new UsbSerialInterface.UsbReadCallback() {
        @Override
        public void onReceivedData(byte[] bytes) {
            process(DMCA001.addReceivedData(bytes));
        }
    };

}
