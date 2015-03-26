package android.uqacproject.com.suchnote.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David Levayer on 26/03/15.
 */
public class DatabaseManager {

    private Context mContext;

    // Database fields
    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDatabaseHelper;

    private String[] wifiTable_allColumns = {
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_NAME,
            DatabaseHelper.COLUMN_SSID,
            DatabaseHelper.COLUMN_PREFERENCE};

    public DatabaseManager(Context context) {
        mDatabaseHelper = new DatabaseHelper(context);
        mContext = context;
    }

    public void open() {
        mDatabase = mDatabaseHelper.getWritableDatabase();
    }

    public void close() {
        mDatabaseHelper.close();
    }

    public void addWifiInfo(MyWifiInfo wifi) {

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, wifi.getAssociatedName());
        values.put(DatabaseHelper.COLUMN_SSID, wifi.getSsid());
        values.put(DatabaseHelper.COLUMN_PREFERENCE, wifi.getPreference());

        mDatabase.insert(DatabaseHelper.TABLE_WIFI_DATA, null, values);
    }

    private MyWifiInfo cursorToWifiInfo(Cursor cursor) {
        MyWifiInfo info = new MyWifiInfo();
        info.setId(cursor.getLong(0));
        info.setAssociatedName(cursor.getString(1));
        info.setSsid(cursor.getString(2));
        info.setPreference(cursor.getString(3));
        return info;
    }

    public void deleteWifiInfo(MyWifiInfo wifiInfo) {
        long id = wifiInfo.getId();
        mDatabase.delete(DatabaseHelper.TABLE_WIFI_DATA, DatabaseHelper.COLUMN_ID
                + " = " + id, null);
    }

    public void deleteAllWifiInformation(){
        mDatabase.delete(DatabaseHelper.TABLE_WIFI_DATA, null, null);
    }

    public List<MyWifiInfo> getAllWifiInformation() {
        List<MyWifiInfo> infos = new ArrayList<MyWifiInfo>();

        Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_WIFI_DATA,
                wifiTable_allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MyWifiInfo comment = cursorToWifiInfo(cursor);
            infos.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return infos;
    }

    public class MyWifiInfo {

        private long id;
        private String ssid, associatedName,preference;

        public MyWifiInfo(){}

        public MyWifiInfo(String ssid, String associatedName, String preference){
            this.ssid = ssid;
            this.associatedName = associatedName;
            this.preference = preference;
        }

        public void setId(long id){
            this.id = id;
        }

        public void setSsid(String ssid) {
            this.ssid = ssid;
        }

        public void setAssociatedName(String associatedName) {
            this.associatedName = associatedName;
        }

        public void setPreference(String preference) {
            this.preference = preference;
        }

        public long getId(){
            return id;
        }

        public String getSsid() {
            return ssid;
        }

        public String getAssociatedName() {
            return associatedName;
        }

        public String getPreference() {
            return preference;
        }
    }
}
