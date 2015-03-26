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
            DatabaseHelper.COLUMN_FILENAME,
            DatabaseHelper.COLUMN_NOTETYPE,
            DatabaseHelper.COLUMN_SSID,
            DatabaseHelper.COLUMN_SSID_ASSOCIATED_NAME,
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

    public void addWifiInfo(NoteInformation noteInfo) {

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_FILENAME, noteInfo.getFilename());
        values.put(DatabaseHelper.COLUMN_NOTETYPE, noteInfo.getNotetype());
        values.put(DatabaseHelper.COLUMN_SSID, noteInfo.getSsid());
        values.put(DatabaseHelper.COLUMN_SSID_ASSOCIATED_NAME, noteInfo.getAssociatedName());
        mDatabase.insert(DatabaseHelper.TABLE_NOTE_DATA, null, values);
    }

    private NoteInformation cursorToWifiInfo(Cursor cursor) {
        NoteInformation info = new NoteInformation();
        info.setId(cursor.getLong(0));
        info.setFilename(cursor.getString(1));
        info.setNotetype(cursor.getString(2));
        info.setSsid(cursor.getString(3));
        info.setAssociatedName(cursor.getString(4));
        return info;
    }

    public void deleteWifiInfo(NoteInformation noteInfo) {
        long id = noteInfo.getId();
        mDatabase.delete(DatabaseHelper.TABLE_NOTE_DATA, DatabaseHelper.COLUMN_ID
                + " = " + id, null);
    }

    public void deleteAllWifiInformation(){
        mDatabase.delete(DatabaseHelper.TABLE_NOTE_DATA, null, null);
    }

    public List<NoteInformation> getAllWifiInformation() {
        List<NoteInformation> infos = new ArrayList<NoteInformation>();

        Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_NOTE_DATA,
                wifiTable_allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            NoteInformation info = cursorToWifiInfo(cursor);
            infos.add(info);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return infos;
    }
}
