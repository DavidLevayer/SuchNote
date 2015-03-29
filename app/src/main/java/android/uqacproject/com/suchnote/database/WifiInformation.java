package android.uqacproject.com.suchnote.database;

import java.io.Serializable;

/**
 * Created by David Levayer on 29/03/15.
 */
public class WifiInformation implements Serializable {

    private long id;
    private String ssid;
    private String associatedName;
    private String associatedColor;

    public WifiInformation() { }

    public WifiInformation(String ssid, String associatedName, String associatedColor) {
        this.ssid = ssid;
        this.associatedName = associatedName;
        this.associatedColor = associatedColor;
    }

    public String getSsid() {
        return ssid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getAssociatedName() {
        return associatedName;
    }

    public void setAssociatedName(String associatedName) {
        this.associatedName = associatedName;
    }

    public String getAssociatedColor() {
        return associatedColor;
    }

    public void setAssociatedColor(String associatedColor) {
        this.associatedColor = associatedColor;
    }
}
