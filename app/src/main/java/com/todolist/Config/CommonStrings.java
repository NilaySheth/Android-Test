package com.todolist.Config;

public class CommonStrings {

    //Database Strings
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DB_TODOList";

    //Table table_todo
    public static final String TABLE_TODO_LIST = "table_todo";

    // Column names for table_todo table
    public static final String KEY_COL_ID = "col_id";
    public static final String KEY_COL_NAME = "col_name";
    public static final String KEY_COL_STATE = "col_state";

    // Create table_todo table
    public static final String CREATE_TODO_TABLE = "CREATE TABLE "
            + TABLE_TODO_LIST + "(" + KEY_COL_ID + " INTEGER,"
            + KEY_COL_NAME + " TEXT," + KEY_COL_STATE
            + " INTEGER" + ")";
}
