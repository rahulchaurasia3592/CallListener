package com.rahul.calllistener.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.rahul.calllistener.model.CallEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * A class for making the database operations.
 *
 * Created by rahul on 3/3/16.
 */
public class CallEntryDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_INCOMING_NUMBER};

    public CallEntryDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void createCallEntry(String incomingNumber) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_INCOMING_NUMBER, incomingNumber);
        database.insert(MySQLiteHelper.TABLE_CALL_ENTRY, null, values);
    }

    public List<CallEntry> getAllCallEntries() {
        List<CallEntry> callEntries = new ArrayList<CallEntry>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_CALL_ENTRY,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            CallEntry callEntry = cursorToCallEntry(cursor);
            callEntries.add(callEntry);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return callEntries;
    }

    private CallEntry cursorToCallEntry(Cursor cursor) {
        CallEntry callEntry = new CallEntry();
        callEntry.setId(cursor.getLong(0));
        callEntry.setComment(cursor.getString(1));
        return callEntry;
    }
}