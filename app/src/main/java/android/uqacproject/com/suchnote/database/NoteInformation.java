package android.uqacproject.com.suchnote.database;

import java.util.Date;

/**
 * Created by David Levayer on 26/03/15.
 */
public class NoteInformation {

    private long id;
    private String filename, associatedName;
    private int noteType;
    private Date date;

    public NoteInformation(){}

    public NoteInformation(String filename, int noteType, String associatedName, Date date){

        this.filename = filename;
        this.noteType = noteType;
        this.associatedName = associatedName;
        this.date = date;
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
        return noteType;
    }

    public void setNotetype(int notetype) {
        this.noteType = notetype;
    }

    public String getAssociatedName() {
        return associatedName;
    }

    public void setAssociatedName(String associatedName) {
        this.associatedName = associatedName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
