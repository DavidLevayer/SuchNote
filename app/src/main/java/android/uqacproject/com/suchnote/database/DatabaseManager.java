package android.uqacproject.com.suchnote.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;

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
            DatabaseHelper.NOTEDATA_DATE};

    private String[] wifiTable_allColumns = {
            DatabaseHelper.WIFIDATA_SSID,
            DatabaseHelper.WIFIDATA_ASSOCIATED_NAME};

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
        values.put(DatabaseHelper.NOTEDATA_DATE, new SimpleDateFormat("dd MM yyyy HH:mm:ss").format(noteInfo.getDate()));
        mDatabase.insert(DatabaseHelper.TABLE_NOTE_DATA, null, values);
    }

    public void addWifiInfo(WifiInformation info){

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.WIFIDATA_SSID, info.getSsid());
        values.put(DatabaseHelper.WIFIDATA_ASSOCIATED_NAME, info.getAssociatedName());
        values.put(DatabaseHelper.WIFIDATA_ASSOCIATED_COLOR, info.getAssociatedColor());
        mDatabase.insert(DatabaseHelper.TABLE_WIFI_DATA, null, values);
    }

    private NoteInformation cursorToNoteInfo(Cursor cursor) {
        NoteInformation info = new NoteInformation();
        info.setId(cursor.getLong(0));
        info.setFilename(cursor.getString(1));
        info.setNotetype(cursor.getInt(2));
        info.setAssociatedName(cursor.getString(3));
        try {
            info.setDate(new SimpleDateFormat("dd MM yyyy HH:mm:ss").parse(cursor.getString(4)));
        } catch (ParseException e) {
            Toast.makeText(mContext,"Invalid date format",Toast.LENGTH_LONG).show();
        }
        return info;
    }

    private WifiInformation cursorToWifiInfo(Cursor cursor) {
        WifiInformation info = new WifiInformation();
        info.setId(cursor.getLong(0));
        info.setSsid(cursor.getString(1));
        info.setAssociatedName(cursor.getString(2));
        info.setAssociatedColor(cursor.getString(3));
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

    @Deprecated
    public String getWifiAssociatedName(String ssid){

        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_WIFI_DATA +
                " WHERE " + DatabaseHelper.WIFIDATA_SSID + " = ?";

        Cursor cursor = mDatabase.rawQuery(selectQuery, new String[]{ssid}, null);

        if(cursor != null && cursor.getCount() != 0){
            cursor.moveToFirst();
            String res = cursor.getString(2);
            cursor.close();
            return res;

        }

        return null;
    }

    public WifiInformation getWifiInformation(String ssid){
        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_WIFI_DATA +
                " WHERE " + DatabaseHelper.WIFIDATA_SSID + " = ?";

        Cursor cursor = mDatabase.rawQuery(selectQuery, new String[]{ssid}, null);

        if(cursor != null && cursor.getCount() != 0){
            cursor.moveToFirst();
            WifiInformation res = cursorToWifiInfo(cursor);
            cursor.close();
            return res;
        }

        return null;
    }

    public String getColorFromAssociatedName(String associatedName){
        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_WIFI_DATA +
                " WHERE " + DatabaseHelper.WIFIDATA_ASSOCIATED_NAME + " = ?";

        Cursor cursor = mDatabase.rawQuery(selectQuery, new String[]{associatedName}, null);

        if(cursor != null && cursor.getCount() != 0){
            cursor.moveToFirst();
            String res = cursor.getString(3);
            cursor.close();
            return res;

        }

        return null;
    }

    public NoteInformation[] getAllNoteInformation() {

        Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_NOTE_DATA,
                noteTable_allColumns, null, null, null, null, null);

        NoteInformation[] infos = new NoteInformation[cursor.getCount()];
        int i = 0;

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            NoteInformation info = cursorToNoteInfo(cursor);
            infos[i] = info;
            cursor.moveToNext();
        }

        cursor.close();
        return infos;
    }

    public NoteInformation[] getNoteInformation(final int noteType){

        Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_NOTE_DATA,
                noteTable_allColumns, DatabaseHelper.NOTEDATA_NOTETYPE + " = ?",
                new String[]{String.valueOf(noteType)}, null, null, null);

        NoteInformation[] infos = new NoteInformation[cursor.getCount()];
        int i = 0;

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            NoteInformation info = cursorToNoteInfo(cursor);
            infos[i] = info;
            cursor.moveToNext();
            i++;
        }

        cursor.close();
        return infos;
    }

    public void removeNoteInformation(NoteInformation note){
        String filename = note.getFilename();
        int res = mDatabase.delete(
                DatabaseHelper.TABLE_NOTE_DATA,
                DatabaseHelper.NOTEDATA_FILENAME+" = ?",
                new String[]{String.valueOf(filename)});
    }
}
