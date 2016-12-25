package com.bigzun.video.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bigzun.video.database.table.ActionHistoryTable;
import com.bigzun.video.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    /* TODO database info */
    private static final String DATABASE_NAME = "BeeMoney.db";
    /* TODO khong duoc tuy tien nang version database */
    private static final int DATABASE_VERSION = 1;

    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static SQLiteDatabase mWritableDb;
    private static SQLiteDatabase mReadableDb;
    private static DatabaseHelper mInstance;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        createAllTable(database);
    }

    private void createAllTable(SQLiteDatabase database) {
        database.execSQL(ActionHistoryTable.CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade oldVersion = " + oldVersion + " newVersion = " + newVersion);
    }

    public static DatabaseHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseHelper(context);
        }
        return mInstance;
    }

    public void closeDatabaseConnection(SQLiteDatabase database) {
        try {
            if (database != null) {
                database.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "closeDatabaseConnection", e);
        }
    }

    public SQLiteDatabase getMyWritableDatabase() {
        if ((mWritableDb == null) || (!mWritableDb.isOpen())) {
            mWritableDb = mInstance.getWritableDatabase();
        }
        return mWritableDb;
    }

    public SQLiteDatabase getMyReadableDatabase() {
        if ((mReadableDb == null) || (!mReadableDb.isOpen())) {
            mReadableDb = mInstance.getReadableDatabase();
        }
        return mReadableDb;
    }

    private boolean checkExistColumnInTable(SQLiteDatabase db, String tableName, String columnName) {
        Cursor cur = null;
        boolean isExist = false;
        try {
            cur = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    do {
                        String name = cur.getString(1);
                        if (columnName != null && columnName.equals(name)) {
                            isExist = true;
                            break;
                        }
                    } while (cur.moveToNext());
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
            isExist = false;
        } finally {
            if (cur != null) {
                cur.close();
            }
        }
        return isExist;
    }
}