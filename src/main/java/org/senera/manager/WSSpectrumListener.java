/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.senera.manager;

import android.content.Context;

import java.util.Date;

import org.senera.SeneraConfiguration;
import org.senera.location.SensorLocation;
import org.senera.server.ws.Measurement;
import org.senera.server.ws.MeasurementSession;
import org.senera.ws.MeasurementWSClient;

/**
 *
 * @author George
 */
public class WSSpectrumListener implements SpectrumListener {

    private Context mContext;

    public WSSpectrumListener(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void processSpectrum(Spectrum spectrum) {
        Measurement measurement = new Measurement();
        
        MeasurementSession ses = new MeasurementSession();
        ses.setSessionId(spectrum.getSessionId());
        measurement.setSessionId(ses);
        measurement.setRepetitionId(spectrum.getRepetitionId());
        measurement.setSpectrum(spectrum.getChannelValuesAsString());
        //measurement.setSpectrum(HexUtils.bytesToHex(spectrum.getChannelValuesAsBytes()));
        measurement.setAcquisitionTime(spectrum.getAcquisitionTime());
        measurement.setEnergyPerChannel(spectrum.getEnergyPerChannel());
        measurement.setEnergyPerChannel2(spectrum.getEnergyPerChannel2());
        measurement.setCps(spectrum.getCps());
        
        measurement.setMeasurementTimestamp(new Date());

        measurement.setSensorId(SeneraConfiguration.getSensorId(mContext));
        measurement.setSensorType(SeneraConfiguration.getSensorType(mContext));

        //Location data
        measurement.setLongitude(SensorLocation.getLon(mContext));
        measurement.setLatitude(SensorLocation.getLat(mContext));
        measurement.setAltitude(SensorLocation.getAlt(mContext));
       
        MeasurementWSClient wsClient = new MeasurementWSClient(SeneraConfiguration.getFusionServerAddress(mContext), SeneraConfiguration.getFusionServerPort(mContext));
        wsClient.saveMeasurement(measurement);
    }
    
}
