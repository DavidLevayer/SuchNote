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

    private String[] noteTable_allColumns = {
            DatabaseHelper.NOTEDATA_ID,
            DatabaseHelper.NOTEDATA_FILENAME,
            DatabaseHelper.NOTEDATA_NOTETYPE,
            DatabaseHelper.NOTEDATA_SSID_ASSOCIATED_NAME,
            DatabaseHelper.NOTEDATA_PREFERENCE};

    private String[] wifiTable_allColumns = {
            DatabaseHelper.WIFIDATA_SSID,
            DatabaseHelper.WIFIDATA_SSID_ASSOCIATED_NAME};

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

    public void addNoteInfo(NoteInformation noteInfo) {

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.NOTEDATA_FILENAME, noteInfo.getFilename());
        values.put(DatabaseHelper.NOTEDATA_NOTETYPE, noteInfo.getNotetype());
        values.put(DatabaseHelper.NOTEDATA_SSID_ASSOCIATED_NAME, noteInfo.getAssociatedName());
        mDatabase.insert(DatabaseHelper.TABLE_NOTE_DATA, null, values);
    }

    public void addWifiInfo(String ssid, String associatedName){

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.WIFIDATA_SSID, ssid);
        values.put(DatabaseHelper.WIFIDATA_SSID_ASSOCIATED_NAME, associatedName);
        mDatabase.insert(DatabaseHelper.TABLE_WIFI_DATA, null, values);
    }

    private NoteInformation cursorToNoteInfo(Cursor cursor) {
        NoteInformation info = new NoteInformation();
        info.setId(cursor.getLong(0));
        info.setFilename(cursor.getString(1));
        info.setNotetype(cursor.getString(2));
        info.setAssociatedName(cursor.getString(3));
        return info;
    }

    public void deleteNoteInfo(NoteInformation noteInfo) {
        long id = noteInfo.getId();
        mDatabase.delete(DatabaseHelper.TABLE_NOTE_DATA, DatabaseHelper.NOTEDATA_ID
                + " = " + id, null);
    }

    public void deleteWifiInfo(String ssid) {
        mDatabase.delete(DatabaseHelper.TABLE_WIFI_DATA, DatabaseHelper.WIFIDATA_SSID
                + " = " + ssid, null);
    }

    public void deleteAllNoteInformation(){
        mDatabase.delete(DatabaseHelper.TABLE_NOTE_DATA, null, null);
    }

    public void deleteAllWifiInformation(){
        mDatabase.delete(DatabaseHelper.TABLE_WIFI_DATA, null, null);
    }

    public String getWifiAssociatedName(String ssid){

        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_WIFI_DATA +
                " WHERE " + DatabaseHelper.WIFIDATA_SSID + " = ?";

        Cursor cursor = mDatabase.rawQuery(selectQuery, new String[]{ssid}, null);

        if(cursor != null){
            cursor.moveToFirst();
            return cursor.getString(2);
        }

        return null;
    }

    public List<NoteInformation> getAllNoteInformation() {
        List<NoteInformation> infos = new ArrayList<NoteInformation>();

        Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_NOTE_DATA,
                noteTable_allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            NoteInformation info = cursorToNoteInfo(cursor);
            infos.add(info);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return infos;
    }
}