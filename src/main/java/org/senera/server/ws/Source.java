
package org.senera.server.ws;


import org.json.JSONException;
import org.json.JSONObject;

public class Source {

    protected Integer sourceId;
    protected String sourceName;

    /**
     * Gets the value of the sourceId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSourceId() {
        return sourceId;
    }

    /**
     * Sets the value of the sourceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSourceId(Integer value) {
        this.sourceId = value;
    }

    /**
     * Gets the value of the sourceName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceName() {
        return sourceName;
    }

    /**
     * Sets the value of the sourceName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceName(String value) {
        this.sourceName = value;
    }


    public JSONObject toJson(){
        JSONObject json = new JSONObject();
        try {
            json.put("sourceId", sourceId);
            json.put("sourceName", sourceName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
