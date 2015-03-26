package android.uqacproject.com.suchnote.database;

/**
 * Created by David Levayer on 26/03/15.
 */
public class NoteInformation {

    private long id;
    private String filename, notetype, ssid, associatedName;

    public NoteInformation(){}

    public NoteInformation(String filename, String noteType,
                           String ssid, String associatedName){
        this.filename = filename;
        this.notetype = notetype;
        this.ssid = ssid;
        this.associatedName = associatedName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getNotetype() {
        return notetype;
    }

    public void setNotetype(String notetype) {
        this.notetype = notetype;
    }

    public String getSsid() {
        return ssid;
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
}
