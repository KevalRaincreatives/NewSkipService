package com.skipservices.Impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.skipservices.Db.DatabaseHelper;
import com.skipservices.Model.TaskListCartModel;

import java.util.ArrayList;


/**
 * Created by DELL on 03/24/2016.
 */

public class Main_impl implements DBOperation {

    SQLiteDatabase db;
    Context cx;

    public Main_impl(Context context) {
        cx =context;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        try {
            db = databaseHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Main_impl() {

    }


    @Override
    public long insert(Object object) {

        TaskListCartModel details = (TaskListCartModel) object;
        long result = db.insert(details.TableName(), null, this.getContentvalues(object));
        return result;
    }

    @Override
    public int update(Object object) {
        TaskListCartModel details = (TaskListCartModel) object;
        String[] whereArgs = {String.valueOf(details.getId())};
        return updateall(object, "id = ?", whereArgs);
    }

    private int updateall(Object object, String string, String[] whereArgs) {
        TaskListCartModel table = (TaskListCartModel) object;



        int result = db.update(table.TableName(),
                this.getContentvalues(object), string, whereArgs);

        return result;
        // TODO Auto-generated method stub

    }

    public ArrayList<TaskListCartModel> getSinlgeEntry() {

        ArrayList<TaskListCartModel> studentList = new ArrayList<TaskListCartModel>();


        try {
            Cursor c = db.rawQuery(
                    "SELECT varta_id,varta_title,varta_audio,varta_note,varta_favourite FROM Details", null);

            if (c.getCount() > 0) {
                c.moveToFirst();
                do {
                    TaskListCartModel table = new TaskListCartModel();
                    table.setUserId(c.getString(c.getColumnIndex("userId")));
                    table.setTask_id(c.getString(c.getColumnIndex("task_id")));
                    table.setTask_number(c.getString(c.getColumnIndex("task_number")));
                    table.setClient_name(c.getString(c.getColumnIndex("client_name")));
                    table.setAddress(c.getString(c.getColumnIndex("address")));
                    table.setShort_description(c.getString(c.getColumnIndex("short_description")));
                    table.setJob_type(c.getString(c.getColumnIndex("job_type")));
                    table.setTask_process(c.getString(c.getColumnIndex("task_process")));

//                    Log.e("ke1", c.getString(c.getColumnIndex("product_id")) + "");
                    studentList.add(table);
                } while (c.moveToNext());
                c.close();
            } else {
                // Util.customLog("getAll FavValue - No value found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return studentList;


    }

    @Override
    public int delete(Object object) {
        TaskListCartModel details = (TaskListCartModel) object;
        String[] whereArgs = {String.valueOf(details.getId())};
        int result = db.delete(details.TableName(), "id = ?", whereArgs);
        // Util.customLog("Delete Result - " + result);
        return result;

    }

    @Override
    public ContentValues getContentvalues(Object object) {
        TaskListCartModel details = (TaskListCartModel) object;

        ContentValues contentValues = new ContentValues();
        contentValues.put("userId", details.getUserId());
        contentValues.put("task_id", details.getTask_id());
        contentValues.put("task_number", details.getTask_number());
        contentValues.put("client_name", details.getClient_name());
        contentValues.put("address", details.getAddress());
        contentValues.put("short_description", details.getShort_description());
        contentValues.put("job_type", details.getJob_type());
        contentValues.put("task_process", details.getTask_process());

        return contentValues;
    }

    public boolean isExist(String product_id) {
        Cursor cursor = db.rawQuery("SELECT varta_history FROM Details WHERE product_id = '" + "y"
                + "'", null);
        boolean exist = (cursor.getCount() > 0);
        cursor.close();
//        db.close();
        return exist;
    }

    public boolean isVariantExist(String variant) {
        Cursor cursor = db.rawQuery("SELECT variant FROM Details WHERE variant = '" + variant
                + "'", null);
        boolean exist = (cursor.getCount() > 0);
        cursor.close();
//        db.close();
        return exist;
    }

    public void delete_record(int id) {
        db.delete(DatabaseHelper.TABLE_NAME, "id=" + "'" + id + "'", null);
    }

    public void delete_table() {
        db.delete(DatabaseHelper.TABLE_NAME, null, null);
    }

//    public List<CategoryData> getUserDetails(String EMAIL) {
//        List<CategoryData> studentList = new ArrayList<CategoryData>();
//
//        String[] columnArray = {"id,FIRST_NAME,LAST_NAME,EMAIL,NUMBER,PASSWORD "};
//        Cursor c;
//        try {
//            c = db.query("Details", columnArray, "EMAIL=" + "'" + EMAIL
//                    + "'", null, null, null, null);
//            if (c.getCount() > 0) {
//                c.moveToFirst();
//                do {
//                    CategoryData table = new CategoryData();
//                    table.setId(c.getInt(c.getColumnIndex("id")));
//                    table.setFirstname(c.getString(c.getColumnIndex("FIRST_NAME")));
//                    table.setLastname(c.getString(c.getColumnIndex("LAST_NAME")));
//                    table.setEmail(c.getString(c.getColumnIndex("EMAIL")));
//                    table.setNumber(c.getString(c.getColumnIndex("NUMBER")));
//                    table.setPassword(c.getString(c.getColumnIndex("PASSWORD")));
//
//                    Log.e("ke1", c.getString(c.getColumnIndex("EMAIL")) + "");
//                    studentList.add(table);
//                } while (c.moveToNext());
//                c.close();
//            } else {
//                // Util.customLog("getAll FavValue - No value found");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return studentList;
//    }
//
    public ArrayList<TaskListCartModel> getUserDetails1(String EMAIL) {
        ArrayList<TaskListCartModel> studentList = new ArrayList<TaskListCartModel>();

        String[] columnArray = {"id,userId,task_id,task_number,client_name,address,short_description,job_type,task_process"};
        Cursor c;
        try {
            c = db.query("Details", columnArray, "userId =" + "'" + EMAIL
                    + "'", null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                do {
                    TaskListCartModel table = new TaskListCartModel();
                    table.setId(c.getInt(c.getColumnIndex("id")));
                    table.setUserId(c.getString(c.getColumnIndex("userId")));
                    table.setTask_id(c.getString(c.getColumnIndex("task_id")));
                    table.setTask_number(c.getString(c.getColumnIndex("task_number")));
                    table.setClient_name(c.getString(c.getColumnIndex("client_name")));
                    table.setAddress(c.getString(c.getColumnIndex("address")));
                    table.setShort_description(c.getString(c.getColumnIndex("short_description")));
                    table.setJob_type(c.getString(c.getColumnIndex("job_type")));
                    table.setTask_process(c.getString(c.getColumnIndex("task_process")));

                    Log.e("ke1", c.getString(c.getColumnIndex("userId")) + "");
                    studentList.add(table);
                } while (c.moveToNext());
                c.close();
            } else {
                // Util.customLog("getAll FavValue - No value found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return studentList;
    }

    public ArrayList<TaskListCartModel> getUser() {
        ArrayList<TaskListCartModel> studentList = new ArrayList<TaskListCartModel>();

        String[] columnArray = {"id,userId,task_id,task_number,client_name,address,short_description,job_type,task_process "};
        Cursor c = null;
        try {
            c = db.query("Details", columnArray, null, null, null, null, null);

                if (c.getCount() > 0) {
                    c.moveToFirst();
                    do {
                        TaskListCartModel table = new TaskListCartModel();
                        table.setId(c.getInt(c.getColumnIndex("id")));
                        table.setUserId(c.getString(c.getColumnIndex("userId")));
                        table.setTask_id(c.getString(c.getColumnIndex("task_id")));
                        table.setTask_number(c.getString(c.getColumnIndex("task_number")));
                        table.setClient_name(c.getString(c.getColumnIndex("client_name")));
                        table.setAddress(c.getString(c.getColumnIndex("address")));
                        table.setShort_description(c.getString(c.getColumnIndex("short_description")));
                        table.setJob_type(c.getString(c.getColumnIndex("job_type")));
                        table.setTask_process(c.getString(c.getColumnIndex("task_process")));

//                    Log.e("ke1", c.getString(c.getColumnIndex("product_id")) + "");
                        studentList.add(table);
                    } while (c.moveToNext());
                    c.close();

                } else {
                    // Util.customLog("getAll FavValue - No value found");
                }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return studentList;
    }

//    public ArrayList<TaskListCartModel> getUserDetails1(String EMAIL) {
//        ArrayList<TaskListCartModel> studentList = new ArrayList<TaskListCartModel>();
//
//        String[] columnArray = {"id,varta_id,varta_title,varta_desc,varta_audio,varta_note,varta_favourite,varta_extra,varta_history"};
//        Cursor c;
//        try {
//
//            c = db.query("Details", columnArray, "varta_id =" + "'" + EMAIL
//                    + "'", null, null, null, null);
//            if (c.getCount() > 0) {
//
//                c.moveToFirst();
//                do {
//                    mycart table = new mycart();
//                    table.setId(c.getInt(c.getColumnIndex("id")));
//                    table.setVarta_id(c.getString(c.getColumnIndex("varta_id")));
//                    table.setVarta_title(c.getString(c.getColumnIndex("varta_title")));
//                    table.setVarta_desc(c.getString(c.getColumnIndex("varta_desc")));
//                    table.setVarta_audio(c.getString(c.getColumnIndex("varta_audio")));
//                    table.setVarta_note(c.getString(c.getColumnIndex("varta_note")));
//                    table.setVarta_favourite(c.getString(c.getColumnIndex("varta_favourite")));
//                    table.setVarta_extra(c.getString(c.getColumnIndex("varta_extra")));
//                    table.setVarta_history(c.getString(c.getColumnIndex("varta_history")));
//
//                    studentList.add(table);
//                } while (c.moveToNext());
////                c.close();
////                db.close();
//            } else {
//                // Util.customLog("getAll FavValue - No value found");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return studentList;
//    }



}
