
package org.senera.server.ws;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Measurement {

    protected Short acquisitionTime;
    protected Float altitude;
    protected Integer cps;
    protected Float energyPerChannel;
	protected Float energyPerChannel2;
    protected Integer entries;
    protected Float latitude;
    protected Float longitude;
    protected Integer measurementId;
    protected Date measurementTimestamp;
    protected Integer repetitionId;
    protected Short sensorId;
    protected String sensorType;
    protected MeasurementSession sessionId;
    protected String spectrum;

    /**
     * Gets the value of the acquisitionTime property.
     * 
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getAcquisitionTime() {
        return acquisitionTime;
    }

    /**
     * Sets the value of the acquisitionTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setAcquisitionTime(Short value) {
        this.acquisitionTime = value;
    }

    /**
     * Gets the value of the altitude property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getAltitude() {
        return altitude;
    }

    /**
     * Sets the value of the altitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setAltitude(Float value) {
        this.altitude = value;
    }

    /**
     * Gets the value of the cps property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCps() {
        return cps;
    }

    /**
     * Sets the value of the cps property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCps(Integer value) {
        this.cps = value;
    }

    /**
     * Gets the value of the energyPerChannel property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getEnergyPerChannel() {
        return energyPerChannel;
    }

    /**
     * Sets the value of the energyPerChannel property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setEnergyPerChannel(Float value) {
        this.energyPerChannel = value;
    }
	
    /**
     * Gets the value of the energyPerChannel2 property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getEnergyPerChannel2() {
        return energyPerChannel2;
    }

    /**
     * Sets the value of the energyPerChannel2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setEnergyPerChannel2(Float value) {
        this.energyPerChannel2 = value;
    }
	
    /**
     * Gets the value of the entries property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getEntries() {
        return entries;
    }

    /**
     * Sets the value of the entries property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setEntries(Integer value) {
        this.entries = value;
    }

    /**
     * Gets the value of the latitude property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getLatitude() {
        return latitude;
    }

    /**
     * Sets the value of the latitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setLatitude(Float value) {
        this.latitude = value;
    }

    /**
     * Gets the value of the longitude property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getLongitude() {
        return longitude;
    }

    /**
     * Sets the value of the longitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setLongitude(Float value) {
        this.longitude = value;
    }

    /**
     * Gets the value of the measurementId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMeasurementId() {
        return measurementId;
    }

    /**
     * Sets the value of the measurementId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMeasurementId(Integer value) {
        this.measurementId = value;
    }

    /**
     * Gets the value of the measurementTimestamp property.
     * 
     *
     */
    public Date getMeasurementTimestamp() {
        return measurementTimestamp;
    }

    /**
     * Sets the value of the measurementTimestamp property.
     * 
     * @param value
     */
    public void setMeasurementTimestamp(Date value) {
        this.measurementTimestamp = value;
    }

    /**
     * Gets the value of the repetitionId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRepetitionId() {
        return repetitionId;
    }

    /**
     * Sets the value of the repetitionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRepetitionId(Integer value) {
        this.repetitionId = value;
    }

    /**
     * Gets the value of the sensorId property.
     * 
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getSensorId() {
        return sensorId;
    }

    /**
     * Sets the value of the sensorId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setSensorId(Short value) {
        this.sensorId = value;
    }

    /**
     * Gets the value of the sensorType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSensorType() {
        return sensorType;
    }

    /**
     * Sets the value of the sensorType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSensorType(String value) {
        this.sensorType = value;
    }

    /**
     * Gets the value of the sessionId property.
     * 
     * @return
     *     possible object is
     *     {@link MeasurementSession }
     *     
     */
    public MeasurementSession getSessionId() {
        return sessionId;
    }

    /**
     * Sets the value of the sessionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link MeasurementSession }
     *     
     */
    public void setSessionId(MeasurementSession value) {
        this.sessionId = value;
    }

    /**
     * Gets the value of the spectrum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpectrum() {
        return spectrum;
    }

    /**
     * Sets the value of the spectrum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpectrum(String value) {
        this.spectrum = value;
    }




    public JSONObject toJson(){
        JSONObject json = new JSONObject();
        try {
            json.put("acquisitionTime", acquisitionTime);
            json.put("altitude", altitude);
            json.put("cps", cps);
            json.put("energyPerChannel", energyPerChannel);
            json.put("energyPerChannel2", energyPerChannel2);
            json.put("entries", entries);
            json.put("latitude", latitude);
            json.put("longitude", longitude);
            json.put("measurementId", measurementId);
            if (measurementTimestamp != null) {
                json.put("measurementTimestamp", new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US).format(measurementTimestamp));
            }
            json.put("repetitionId", repetitionId);
            json.put("sensorId", sensorId);
            json.put("sensorType", sensorType);
            json.put("sessionId", sessionId.toJson());
            json.put("spectrum", spectrum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
