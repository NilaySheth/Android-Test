package com.todolist.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.todolist.Config.CommonStrings;
import com.todolist.model.DataDescription;

import java.util.ArrayList;
import java.util.List;

public class DbTODOList {
    private DatabaseHandler dbHelper;
    private Context context;

    public DbTODOList(Context context) {
        this.context = context;
    }


    public void addTODO(DataDescription dataTodo) {
        dbHelper = new DatabaseHandler(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CommonStrings.KEY_COL_ID, dataTodo.getId());
        values.put(CommonStrings.KEY_COL_NAME, dataTodo.getName());
        values.put(CommonStrings.KEY_COL_STATE, dataTodo.getState());
        // Inserting Row
        db.insert(CommonStrings.TABLE_TODO_LIST, null, values);
        db.close(); // Closing database connection
    }

    public void addAllTODO(List<DataDescription> dataTodolist) {
        dbHelper = new DatabaseHandler(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for(int i=0;i<dataTodolist.size(); i++){
            ContentValues values = new ContentValues();
            values.put(CommonStrings.KEY_COL_ID, dataTodolist.get(i).getId());
            values.put(CommonStrings.KEY_COL_NAME, dataTodolist.get(i).getName());
            values.put(CommonStrings.KEY_COL_STATE, dataTodolist.get(i).getState());
            // Inserting Row
            db.insert(CommonStrings.TABLE_TODO_LIST, null, values);
        }
        db.close(); // Closing database connection
    }

    public ArrayList<DataDescription> getAllTODO() {
        dbHelper = new DatabaseHandler(context);
        ArrayList<DataDescription> todoList = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(CommonStrings.TABLE_TODO_LIST, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                DataDescription todoItem = new DataDescription(
                        cursor.getInt(0), cursor.getString(1), cursor.getInt(2), false);
                todoList.add(todoItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return todoList;
    }

    public ArrayList<DataDescription> getFilteredTODO(int state) {
        dbHelper = new DatabaseHandler(context);
        ArrayList<DataDescription> todoList = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(CommonStrings.TABLE_TODO_LIST, null, CommonStrings.KEY_COL_STATE + " = ?", new String[]{String.valueOf(state)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                DataDescription todoItem = new DataDescription(
                        cursor.getInt(0), cursor.getString(1), cursor.getInt(2), false);
                todoList.add(todoItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return todoList;
    }

    public void deleteTODO(int id) {
        dbHelper = new DatabaseHandler(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(CommonStrings.TABLE_TODO_LIST, CommonStrings.KEY_COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteAllTODO() {
        dbHelper = new DatabaseHandler(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(CommonStrings.TABLE_TODO_LIST, null, null);
        db.close();
    }
}
