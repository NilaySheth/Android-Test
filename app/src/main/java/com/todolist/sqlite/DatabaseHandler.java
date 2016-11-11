package com.todolist.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.todolist.Config.CommonStrings;

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(Context context) {
        super(context, CommonStrings.DATABASE_NAME, null,
                CommonStrings.DATABASE_VERSION);
    }

    /**
     * Creates all the tables in offline database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CommonStrings.CREATE_TODO_TABLE);
    }

    /**
     * Upgrades all the tables of database.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        this.onCreate(db);
    }
}
