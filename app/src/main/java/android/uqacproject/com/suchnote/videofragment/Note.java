package android.uqacproject.com.suchnote.videofragment;

/**
 * Created by David Levayer on 25/03/15.
 */
public class Note {

    private String name;
    private String place;

    public Note(){}

    public Note(String name, String place){
        this.name = name;
        this.place = place;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
