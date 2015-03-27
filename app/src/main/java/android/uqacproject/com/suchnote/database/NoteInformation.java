package android.uqacproject.com.suchnote.database;

/**
 * Created by David Levayer on 26/03/15.
 */
public class NoteInformation {

    private long id;
    private String filename, associatedName;
    private int notetype;

    public NoteInformation(){}

    public NoteInformation(String filename, int noteType, String associatedName){

        this.filename = filename;
        this.notetype = notetype;
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

    public int getNotetype() {
        return notetype;
    }

    public void setNotetype(int notetype) {
        this.notetype = notetype;
    }

    public String getAssociatedName() {
        return associatedName;
    }

    public void setAssociatedName(String associatedName) {
        this.associatedName = associatedName;
    }
}
