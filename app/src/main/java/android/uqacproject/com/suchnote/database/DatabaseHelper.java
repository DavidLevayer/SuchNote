package android.uqacproject.com.suchnote.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by David Levayer on 26/03/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_NOTE_DATA = "notedata";

    // TODO Ajouter la date
    public static final String NOTEDATA_ID = "_id";
    public static final String NOTEDATA_FILENAME = "filename";
    public static final String NOTEDATA_NOTETYPE = "notetype";
    public static final String NOTEDATA_SSID_ASSOCIATED_NAME = "ssidassociatedname";
    public static final String NOTEDATA_DATE = "notedatadate";

    public static final String TABLE_WIFI_DATA = "wifidata";

    public static final String WIFIDATA_ID = "_id";
    public static final String WIFIDATA_SSID = "wifidatassid";
    public static final String WIFIDATA_SSID_ASSOCIATED_NAME = "wifidataassociatedname";

    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 1;

    private Context mContext;

    // Création de la table Note dans la base de données
    private static final String DATABASE_CREATE_NOTE_TABLE = "create table "
            + TABLE_NOTE_DATA + "(" +
            NOTEDATA_ID + " integer primary key autoincrement, " +
            NOTEDATA_FILENAME + " text not null, " +
            NOTEDATA_NOTETYPE + " integer, " +
            NOTEDATA_SSID_ASSOCIATED_NAME + " text, " +
            NOTEDATA_DATE + " text not null);";

    // Création de la table Wifi dans la base de données
    private static final String DATABASE_CREATE_WIFI_TABLE = "create table "
            + TABLE_WIFI_DATA + "(" +
            WIFIDATA_ID + " integer primary key autoincrement, " +
            WIFIDATA_SSID + " text not null, " +
            WIFIDATA_SSID_ASSOCIATED_NAME + " text not null);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE_NOTE_TABLE);
        database.execSQL(DATABASE_CREATE_WIFI_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WIFI_DATA);
        onCreate(db);
    }
}
