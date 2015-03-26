package android.uqacproject.com.suchnote.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by David Levayer on 26/03/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_WIFI_DATA = "wifidata";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "filename";
    public static final String COLUMN_SSID = "networkssid";
    public static final String COLUMN_PREFERENCE = "notemodepreference";

    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 1;

    private Context mContext;

    // Création de la table Wifi dans la base de données
    private static final String DATABASE_CREATE_WIFI_TABLE = "create table "
            + TABLE_WIFI_DATA + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_NAME
            + " text not null, "+ COLUMN_SSID +" text not null, "+ COLUMN_PREFERENCE+" text);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE_WIFI_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WIFI_DATA);
        onCreate(db);
    }
}
