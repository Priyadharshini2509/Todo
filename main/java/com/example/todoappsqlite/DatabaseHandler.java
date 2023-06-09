package com.example.todoappsqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String NAME= "todoListDatabase";
    private static final String TODO_TABLE="todo";
    private static final String ID="id";
    private static final String TASK="task";
    private static final String STATUS="status";
    private static final String CREATE_TODO_TABLE="CREATE TABLE "+TODO_TABLE+"("+ ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
            +TASK+"TEXT, "+STATUS+"INTEGER)";

    private SQLiteDatabase sqLiteDatabase;

    DatabaseHandler(Context context){
        super(context,NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TODO_TABLE);
        onCreate(sqLiteDatabase);
    }
    public void openDatabase(){
        sqLiteDatabase=this.getWritableDatabase();
    }
    public void insertTask(ToDoModel task){
        ContentValues cv = new ContentValues();
        cv.put(TASK,task.getTask());
        cv.put(STATUS,0);
        sqLiteDatabase.insert(TODO_TABLE,null,cv);
    }
    @SuppressLint("Range")
    public List<ToDoModel> getAllTasks(){
        List<ToDoModel> taskList = new ArrayList<>();
        Cursor cursor = null;
        sqLiteDatabase.beginTransaction();
        try{
            cursor = sqLiteDatabase.query(TODO_TABLE,null,null,null,null,null,null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    do {
                        ToDoModel task = new ToDoModel();
                        task.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                        task.setId(cursor.getInt(cursor.getColumnIndex(TASK)));
                        task.setId(cursor.getInt(cursor.getColumnIndex(STATUS)));
                        taskList.add(task);
                    }while (cursor.moveToNext());
                }
            }
        }
        finally {
            sqLiteDatabase.endTransaction();
            cursor.close();
        }
        return taskList;
    }
    public void updateStatus(int id,int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS,status);
        sqLiteDatabase.update(TODO_TABLE,cv,ID+"=?",new String[]{String.valueOf(id)});
    }
    public void updateTask(int id,String task){
        ContentValues cv = new ContentValues();
        cv.put(TASK,task);
        sqLiteDatabase.update(TODO_TABLE,cv,ID+"+?",new String[]{String.valueOf(id)});
    }
    public void deleteTask(int id){
        sqLiteDatabase.delete(TODO_TABLE,ID+"=?",new String[]{String.valueOf(id)});
    }
}
