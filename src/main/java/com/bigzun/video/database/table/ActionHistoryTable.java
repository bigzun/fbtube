package com.bigzun.video.database.table;

public class ActionHistoryTable {
    protected static final String TABLE = "ActionHistoryTable";
    protected static final String ID = "id";
    protected static final String USER_ID = "user_id";
    protected static final String DEVICE_ID = "device_id";
    protected static final String ITEM_ID = "item_id";
    protected static final String ITEM_TYPE = "item_type";
    protected static final String ITEM_INFO = "item_info";
    protected static final String ACTION_TYPE = "action_type";
    protected static final String ACTION_CONTENT = "action_content";
    protected static final String DATETIME = "datetime";

    public static final String CREATE_STATEMENT = "CREATE TABLE IF NOT EXISTS " + TABLE + " ("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + USER_ID + " INTEGER, "
            + DEVICE_ID + " TEXT, "
            + ITEM_ID + " INTEGER, "
            + ITEM_TYPE + " TEXT, "
            + ITEM_INFO + " TEXT, "
            + ACTION_TYPE + " TEXT, "
            + ACTION_CONTENT + " TEXT, "
            + DATETIME + " INTEGER "
            + ")";

    protected static final String DROP_STATEMENT = "DROP TABLE IF EXISTS " + TABLE;
    protected static final String SELECT_ALL_STATEMENT = "SELECT * FROM " + TABLE + " ORDER BY " + ID + " DESC";
    protected static final String SELECT_BY_ID_STATEMENT = "SELECT * FROM " + TABLE + " WHERE " + ID + " = ";
    protected static final String SELECT_BY_ITEM_ID_STATEMENT = "SELECT * FROM " + TABLE + " WHERE " + ITEM_ID + " = ";
    protected static final String SELECT_BY_ITEM_TYPE_STATEMENT = "SELECT * FROM " + TABLE + " WHERE " + ITEM_TYPE + " = ";
    protected static final String SELECT_BY_USER_ID_STATEMENT = "SELECT * FROM " + TABLE + " WHERE " + USER_ID + " = ";
    protected static final String SELECT_BY_ACTION_TYPE_STATEMENT = "SELECT * FROM " + TABLE + " WHERE " + ACTION_TYPE + " = ";
    protected static final String DELETE_ALL_STATEMENT = "DELETE FROM " + TABLE;
    protected static final String WHERE_ID = ID + " = ?";

}