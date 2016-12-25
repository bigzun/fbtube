package com.bigzun.video.database.business;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.bigzun.video.app.ApplicationController;
import com.bigzun.video.database.model.ActionHistoryModel;
import com.bigzun.video.database.table.ActionHistoryTable;
import com.bigzun.video.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ActionHistoryBusiness extends ActionHistoryTable {
    // Database fields
    private static String TAG = ActionHistoryBusiness.class.getSimpleName();
    private SQLiteDatabase databaseRead;
    private SQLiteDatabase databaseWrite;
    private static ActionHistoryBusiness mInstance;

    public static ActionHistoryBusiness getInstance() {
        if (mInstance == null) {
            mInstance = new ActionHistoryBusiness(ApplicationController.getInstance());
            mInstance.getListItem();
        }

        return mInstance;
    }

    private ActionHistoryBusiness(ApplicationController application) {
        databaseRead = application.getDatabaseHelper().getMyReadableDatabase();
        databaseWrite = application.getDatabaseHelper().getMyWritableDatabase();
    }

    /**
     * closeCursor
     *
     * @param cursor
     */
    private void closeCursor(Cursor cursor) {
        if (cursor != null)
            cursor.close();
    }

    /**
     * setValues
     *
     * @param item
     */
    private ContentValues setValues(ActionHistoryModel item) {
        ContentValues values = new ContentValues();
        if (item != null) {
            if (-1 != item.getId())
                values.put(ID, item.getId());
            values.put(USER_ID, item.getUserId());
            values.put(DEVICE_ID, item.getDeviceId());
            values.put(ITEM_ID, item.getItemId());
            values.put(ITEM_TYPE, item.getItemType());
            values.put(ITEM_INFO, item.getItemInfo());
            values.put(ACTION_CONTENT, item.getActionContent());
            values.put(ACTION_TYPE, item.getActionType());
            values.put(DATETIME, item.getDatetime());
        }
        return values;
    }

    /**
     * getItemFromCursor
     *
     * @param cursor
     */
    private ActionHistoryModel getItemFromCursor(Cursor cursor) {
        ActionHistoryModel item = new ActionHistoryModel();
        try {
            item.setId(cursor.getLong(0));
            item.setUserId(cursor.getLong(1));
            item.setDeviceId(cursor.getString(2));
            item.setItemId(cursor.getLong(3));
            item.setItemType(cursor.getString(4));
            item.setItemInfo(cursor.getString(5));
            item.setActionType(cursor.getString(6));
            item.setActionContent(cursor.getString(7));
            item.setDatetime(cursor.getLong(8));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }

    /**
     * insertItem
     *
     * @param item
     */
    public boolean insertItem(ActionHistoryModel item) {
        if (databaseWrite == null || item == null)
            return false;

        long rowId = 0;
        try {
            databaseWrite.beginTransaction();
            try {
                ContentValues values = setValues(item);
                rowId = databaseWrite.insert(TABLE, null, values);
                Log.d(TAG, "insertItem rowId: " + rowId);
                databaseWrite.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                databaseWrite.endTransaction();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (rowId > 0) {
            item.setId(rowId);
            return true;
        }
        return false;
    }

    /**
     * insertListItem
     *
     * @param list
     */
    public void insertListItem(ArrayList<ActionHistoryModel> list) {
        if (databaseWrite == null || list == null || list.isEmpty())
            return;
        try {
            databaseWrite.beginTransaction();
            try {
                for (ActionHistoryModel item : list) {
                    ContentValues values = setValues(item);
                    long rowId = databaseWrite.insert(TABLE, null, values);
                    item.setId(rowId);
                    Log.d(TAG, "insertItem rowId: " + rowId);
                }
                databaseWrite.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                databaseWrite.endTransaction();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * updateItem
     *
     * @param item
     */
    public boolean updateItem(ActionHistoryModel item) {
        if (databaseWrite == null || item == null)
            return false;

        int rowsNo = 0;
        try {
            databaseWrite.beginTransaction();
            try {
                String whereClause = ID + " = " + item.getId();
                rowsNo = databaseWrite.update(TABLE, setValues(item), whereClause, null);
                Log.d(TAG, "updateItem rowsNo: " + rowsNo);
                databaseWrite.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                databaseWrite.endTransaction();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowsNo > 0 ? true : false;
    }

    /**
     * getItemById
     *
     * @param id
     */
    public ActionHistoryModel getItemById(String id) {
        if (databaseRead == null)
            return null;

        ActionHistoryModel item = null;
        Cursor cursor = null;
        try {
            cursor = databaseRead.rawQuery(SELECT_BY_ID_STATEMENT + id, null);
            if (cursor != null && cursor.moveToFirst()) {
                item = getItemFromCursor(cursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeCursor(cursor);
        }
        return item;
    }

    /**
     * getListItem
     */
    public List<ActionHistoryModel> getListItem() {
        if (databaseRead == null)
            return new ArrayList<>();

        List<ActionHistoryModel> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = databaseRead.rawQuery(SELECT_ALL_STATEMENT, null);
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        list.add(getItemFromCursor(cursor));
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeCursor(cursor);
        }
        return list;
    }

    /**
     * deleteItemByID
     *
     * @param id
     */
    public boolean deleteItemById(String id) {
        if (databaseWrite == null || TextUtils.isEmpty(id))
            return false;

        int rowsNo = 0;
        try {
            databaseWrite.beginTransaction();
            try {
                rowsNo = databaseWrite.delete(TABLE, WHERE_ID, new String[]{id});
                Log.d(TAG, "deleteItemById: " + id + "; rowsNo: " + rowsNo);
                databaseWrite.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                databaseWrite.endTransaction();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowsNo > 0 ? true : false;
    }

    /**
     * deleteListItem
     *
     * @param list
     */
    public void deleteListItem(ArrayList<ActionHistoryModel> list) {
        if (databaseWrite == null || list == null || list.isEmpty())
            return;

        try {
            databaseWrite.beginTransaction();
            try {
                int rowsNo = 0;
                for (ActionHistoryModel item : list) {
                    if (item != null)
                        rowsNo += databaseWrite.delete(TABLE, WHERE_ID, new String[]{"" + item.getId()});
                }
                Log.d(TAG, "deleteListItem rowsNo:" + rowsNo);
                databaseWrite.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                databaseWrite.endTransaction();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * deleteAllTable
     */
    public void deleteAllTable() {
        if (databaseWrite == null)
            return;

        try {
            databaseWrite.execSQL(DELETE_ALL_STATEMENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * dropTable
     */
    public void dropTable() {
        if (databaseWrite == null)
            return;

        try {
            databaseWrite.execSQL(DROP_STATEMENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * getListByUserId
     *
     * @param userId
     */
    public List<ActionHistoryModel> getListByUserId(long userId) {
        if (databaseRead == null)
            return new ArrayList<>();

        List<ActionHistoryModel> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = databaseRead.rawQuery(SELECT_BY_USER_ID_STATEMENT + userId, null);
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        list.add(getItemFromCursor(cursor));
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeCursor(cursor);
        }
        return list;
    }

    /**
     * getListByActionType
     *
     * @param actionType
     */
    public List<ActionHistoryModel> getListByActionType(String actionType) {
        if (databaseRead == null)
            return new ArrayList<>();

        List<ActionHistoryModel> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = databaseRead.rawQuery(SELECT_BY_ACTION_TYPE_STATEMENT + actionType, null);
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        list.add(getItemFromCursor(cursor));
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeCursor(cursor);
        }
        return list;
    }

    /**
     * getListByItemId
     *
     * @param itemId
     */
    public List<ActionHistoryModel> getListByItemId(long itemId) {
        if (databaseRead == null)
            return new ArrayList<>();

        List<ActionHistoryModel> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = databaseRead.rawQuery(SELECT_BY_ITEM_ID_STATEMENT + itemId, null);
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        list.add(getItemFromCursor(cursor));
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeCursor(cursor);
        }
        return list;
    }

    /**
     * getListByItemType
     *
     * @param itemType
     */
    public List<ActionHistoryModel> getListByItemType(String itemType) {
        if (databaseRead == null)
            return new ArrayList<>();

        List<ActionHistoryModel> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = databaseRead.rawQuery(SELECT_BY_ITEM_TYPE_STATEMENT + itemType, null);
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        list.add(getItemFromCursor(cursor));
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeCursor(cursor);
        }
        return list;
    }
}