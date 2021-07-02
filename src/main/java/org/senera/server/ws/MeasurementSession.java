
package org.senera.server.ws;


import org.json.JSONException;
import org.json.JSONObject;

import javax.xml.datatype.XMLGregorianCalendar;


public class MeasurementSession {

    protected String comments;
    protected Integer duration;
    protected Integer repetitions;
    protected XMLGregorianCalendar sessionDtg;
    protected Integer sessionId;
    protected Float sourceAlt;
    protected Source sourceId;
    protected Float sourceLat;
    protected Float sourceLong;
    protected String userId;

    /**
     * Gets the value of the comments property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComments() {
        return comments;
    }

    /**
     * Sets the value of the comments property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComments(String value) {
        this.comments = value;
    }

    /**
     * Gets the value of the duration property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * Sets the value of the duration property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDuration(Integer value) {
        this.duration = value;
    }

    /**
     * Gets the value of the repetitions property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRepetitions() {
        return repetitions;
    }

    /**
     * Sets the value of the repetitions property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRepetitions(Integer value) {
        this.repetitions = value;
    }

    /**
     * Gets the value of the sessionDtg property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getSessionDtg() {
        return sessionDtg;
    }

    /**
     * Sets the value of the sessionDtg property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSessionDtg(XMLGregorianCalendar value) {
        this.sessionDtg = value;
    }

    /**
     * Gets the value of the sessionId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSessionId() {
        return sessionId;
    }

    /**
     * Sets the value of the sessionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSessionId(Integer value) {
        this.sessionId = value;
    }

    /**
     * Gets the value of the sourceAlt property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getSourceAlt() {
        return sourceAlt;
    }

    /**
     * Sets the value of the sourceAlt property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setSourceAlt(Float value) {
        this.sourceAlt = value;
    }

    /**
     * Gets the value of the sourceId property.
     * 
     * @return
     *     possible object is
     *     {@link Source }
     *     
     */
    public Source getSourceId() {
        return sourceId;
    }

    /**
     * Sets the value of the sourceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Source }
     *     
     */
    public void setSourceId(Source value) {
        this.sourceId = value;
    }

    /**
     * Gets the value of the sourceLat property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getSourceLat() {
        return sourceLat;
    }

    /**
     * Sets the value of the sourceLat property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setSourceLat(Float value) {
        this.sourceLat = value;
    }

    /**
     * Gets the value of the sourceLong property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getSourceLong() {
        return sourceLong;
    }

    /**
     * Sets the value of the sourceLong property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setSourceLong(Float value) {
        this.sourceLong = value;
    }

    /**
     * Gets the value of the userId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the value of the userId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserId(String value) {
        this.userId = value;
    }



    public JSONObject toJson(){
        JSONObject json = new JSONObject();
        try {
            json.put("sessionId", sessionId);
            json.put("duration", duration);
            json.put("repetitions", repetitions);
            json.put("sessionDtg", sessionDtg);
            if (sourceId != null) {
                json.put("sourceId", sourceId.toJson());
            }
            json.put("comments", comments);
            json.put("sourceAlt", sourceAlt);
            json.put("sourceLat", sourceLat);
            json.put("sourceLong", sourceLong);
            json.put("userId", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
