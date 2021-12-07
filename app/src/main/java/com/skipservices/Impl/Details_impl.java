package com.skipservices.Impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.skipservices.Db.DatabaseHelper;
import com.skipservices.Model.TaskDetailCartModel;

import java.util.ArrayList;


/**
 * Created by DELL on 03/24/2016.
 */

public class Details_impl implements DBOperation {

    SQLiteDatabase db;
    Context cx;

    public Details_impl(Context context) {
        cx = context;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        try {
            db = databaseHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Details_impl() {

    }


    @Override
    public long insert(Object object) {

        TaskDetailCartModel details = (TaskDetailCartModel) object;
        long result = db.insert(details.TableName(), null, this.getContentvalues(object));
        return result;
    }

    @Override
    public int update(Object object) {
        TaskDetailCartModel details = (TaskDetailCartModel) object;
        String[] whereArgs = {String.valueOf(details.getId())};
        return updateall(object, "id = ?", whereArgs);
    }

    private int updateall(Object object, String string, String[] whereArgs) {
        TaskDetailCartModel table = (TaskDetailCartModel) object;


        int result = db.update(table.TableName(),
                this.getContentvalues(object), string, whereArgs);

        return result;
        // TODO Auto-generated method stub

    }

    public ArrayList<TaskDetailCartModel> getSinlgeEntry() {

        ArrayList<TaskDetailCartModel> studentList = new ArrayList<TaskDetailCartModel>();


        try {
            Cursor c = db.rawQuery(
                    "SELECT task_id,varta_title,varta_audio,varta_note,varta_favourite FROM Details", null);

            if (c.getCount() > 0) {
                c.moveToFirst();
                do {
                    TaskDetailCartModel table = new TaskDetailCartModel();
                    table.setTask_id(c.getString(c.getColumnIndex("task_id")));
                    table.setTask_number(c.getString(c.getColumnIndex("task_number")));
                    table.setClient_name(c.getString(c.getColumnIndex("client_name")));
                    table.setDate(c.getString(c.getColumnIndex("date")));
                    table.setTime(c.getString(c.getColumnIndex("time")));
                    table.setAddress(c.getString(c.getColumnIndex("address")));
                    table.setLatitude(c.getString(c.getColumnIndex("latitude")));
                    table.setLongitude(c.getString(c.getColumnIndex("longitude")));
                    table.setShort_description(c.getString(c.getColumnIndex("short_description")));
                    table.setDescription(c.getString(c.getColumnIndex("description")));
                    table.setAcknowledged_by(c.getString(c.getColumnIndex("acknowledged_by")));
                    table.setAcknowledged_date(c.getString(c.getColumnIndex("acknowledged_date")));
                    table.setAcknowledged_time(c.getString(c.getColumnIndex("acknowledged_time")));
                    table.setImage_path(c.getString(c.getColumnIndex("image_path")));
                    table.setTask_process(c.getString(c.getColumnIndex("task_process")));
                    table.setDisposal_site_name(c.getString(c.getColumnIndex("disposal_site_name")));
                    table.setDisposal_site_id(c.getString(c.getColumnIndex("disposal_site_id")));
                    table.setDisposal_date(c.getString(c.getColumnIndex("disposal_date")));
                    table.setDisposal_time(c.getString(c.getColumnIndex("disposal_time")));
                    table.setJob_type(c.getString(c.getColumnIndex("job_type")));
                    table.setSecond_man(c.getString(c.getColumnIndex("second_man")));
                    table.setJob_started(c.getString(c.getColumnIndex("job_started")));
                    table.setSing_off_status(c.getString(c.getColumnIndex("sing_off_status")));

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
        TaskDetailCartModel details = (TaskDetailCartModel) object;
        String[] whereArgs = {String.valueOf(details.getId())};
        int result = db.delete(details.TableName(), "id = ?", whereArgs);
        // Util.customLog("Delete Result - " + result);
        return result;

    }

    @Override
    public ContentValues getContentvalues(Object object) {
        TaskDetailCartModel details = (TaskDetailCartModel) object;

        ContentValues contentValues = new ContentValues();
        contentValues.put("userId", details.getUserId());
        contentValues.put("task_id", details.getTask_id());
        contentValues.put("task_number", details.getTask_number());
        contentValues.put("client_name", details.getClient_name());
        contentValues.put("date", details.getDate());
        contentValues.put("time", details.getTime());
        contentValues.put("address", details.getAddress());
        contentValues.put("latitude", details.getLatitude());
        contentValues.put("longitude", details.getLongitude());
        contentValues.put("short_description", details.getShort_description());
        contentValues.put("description", details.getDescription());
        contentValues.put("acknowledged_by", details.getAcknowledged_by());
        contentValues.put("acknowledged_date", details.getAcknowledged_date());
        contentValues.put("acknowledged_time", details.getAcknowledged_time());
        contentValues.put("image_path", details.getImage_path());
        contentValues.put("image_bitmap", details.getImage_bitmap());
        contentValues.put("task_process", details.getTask_process());
        contentValues.put("disposal_site_name", details.getDisposal_site_name());
        contentValues.put("disposal_site_id", details.getDisposal_site_id());
        contentValues.put("disposal_date", details.getDisposal_date());
        contentValues.put("disposal_time", details.getDisposal_time());
        contentValues.put("job_type", details.getJob_type());
        contentValues.put("second_man", details.getSecond_man());
        contentValues.put("job_started", details.getJob_started());
        contentValues.put("sing_off_status", details.getSing_off_status());
        contentValues.put("job_start_datetime", details.getJob_start_datetime());
        contentValues.put("task_sync_status", details.getTask_sync_status());
        contentValues.put("ss1", details.getSs1());
        contentValues.put("ss2", details.getSs2());
        contentValues.put("ss2_A", details.getSs2_A());
        contentValues.put("ss3", details.getSs3());
        contentValues.put("final_sync", details.getFinal_sync());
        contentValues.put("new_rental", details.getNew_rental());
        contentValues.put("final_removal", details.getFinal_removal());
        contentValues.put("instructions", details.getInstructions());

        return contentValues;
    }

    public boolean isExist(String product_id) {
        Cursor cursor = db.rawQuery("SELECT task_id FROM Details2 WHERE task_id = '" + product_id
                + "'", null);
        boolean exist = (cursor.getCount() > 0);
        cursor.close();
//        db.close();
        return exist;
    }

    public boolean isVariantExist(String variant) {
        Cursor cursor = db.rawQuery("SELECT variant FROM Details2 WHERE variant = '" + variant
                + "'", null);
        boolean exist = (cursor.getCount() > 0);
        cursor.close();
//        db.close();
        return exist;
    }

    public void delete_record(int id) {
        db.delete(DatabaseHelper.TABLE_NAME2, "id=" + "'" + id + "'", null);
    }

    public void delete_table() {
        db.delete(DatabaseHelper.TABLE_NAME2, null, null);
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
//    public List<CategoryData> getUserDetails1(String EMAIL) {
//        List<CategoryData> studentList = new ArrayList<CategoryData>();
//
//        String[] columnArray = {"id,FIRST_NAME,LAST_NAME,EMAIL,NUMBER,PASSWORD "};
//        Cursor c;
//        try {
//            c = db.query("Details", columnArray, "EMAIL =" + "'" + EMAIL
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

    public ArrayList<TaskDetailCartModel> getUser() {
        ArrayList<TaskDetailCartModel> studentList = new ArrayList<TaskDetailCartModel>();

        String[] columnArray = {"id,userId,task_id, task_number, client_name, date,time,address, latitude,longitude,short_description\n" +
                "            ,description,acknowledged_by,acknowledged_date,acknowledged_time,image_path,image_bitmap,task_process,disposal_site_name\n" +
                "            ,disposal_site_id,disposal_date,disposal_time, job_type,second_man,job_started, sing_off_status,job_start_datetime\n" +
                "            ,task_sync_status,ss1,ss2,ss2_A,ss3,final_sync,new_rental,final_removal,instructions"};
        Cursor c = null;
        try {
            c = db.query("Details2", columnArray, null, null, null, null, null);

            if (c.getCount() > 0) {
                c.moveToFirst();
                do {
                    TaskDetailCartModel table = new TaskDetailCartModel();
                    table.setId(c.getInt(c.getColumnIndex("id")));
                    table.setUserId(c.getString(c.getColumnIndex("userId")));
                    table.setTask_id(c.getString(c.getColumnIndex("task_id")));
                    table.setTask_number(c.getString(c.getColumnIndex("task_number")));
                    table.setClient_name(c.getString(c.getColumnIndex("client_name")));
                    table.setDate(c.getString(c.getColumnIndex("date")));
                    table.setTime(c.getString(c.getColumnIndex("time")));
                    table.setAddress(c.getString(c.getColumnIndex("address")));
                    table.setLatitude(c.getString(c.getColumnIndex("latitude")));
                    table.setLongitude(c.getString(c.getColumnIndex("longitude")));
                    table.setShort_description(c.getString(c.getColumnIndex("short_description")));
                    table.setDescription(c.getString(c.getColumnIndex("description")));
                    table.setAcknowledged_by(c.getString(c.getColumnIndex("acknowledged_by")));
                    table.setAcknowledged_date(c.getString(c.getColumnIndex("acknowledged_date")));
                    table.setAcknowledged_time(c.getString(c.getColumnIndex("acknowledged_time")));
                    table.setImage_path(c.getString(c.getColumnIndex("image_path")));
                    table.setImage_bitmap(c.getString(c.getColumnIndex("image_bitmap")));
                    table.setTask_process(c.getString(c.getColumnIndex("task_process")));
                    table.setDisposal_site_name(c.getString(c.getColumnIndex("disposal_site_name")));
                    table.setDisposal_site_id(c.getString(c.getColumnIndex("disposal_site_id")));
                    table.setDisposal_date(c.getString(c.getColumnIndex("disposal_date")));
                    table.setDisposal_time(c.getString(c.getColumnIndex("disposal_time")));
                    table.setJob_type(c.getString(c.getColumnIndex("job_type")));
                    table.setSecond_man(c.getString(c.getColumnIndex("second_man")));
                    table.setJob_started(c.getString(c.getColumnIndex("job_started")));
                    table.setSing_off_status(c.getString(c.getColumnIndex("sing_off_status")));
                    table.setJob_start_datetime(c.getString(c.getColumnIndex("job_start_datetime")));
                    table.setTask_sync_status(c.getString(c.getColumnIndex("task_sync_status")));
                    table.setSs1(c.getString(c.getColumnIndex("ss1")));
                    table.setSs2(c.getString(c.getColumnIndex("ss2")));
                    table.setSs2_A(c.getString(c.getColumnIndex("ss2_A")));
                    table.setSs3(c.getString(c.getColumnIndex("ss3")));
                    table.setFinal_sync(c.getString(c.getColumnIndex("final_sync")));
                    table.setNew_rental(c.getString(c.getColumnIndex("new_rental")));
                    table.setFinal_removal(c.getString(c.getColumnIndex("final_removal")));
                    table.setInstructions(c.getString(c.getColumnIndex("instructions")));

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

    public ArrayList<TaskDetailCartModel> getUserDetails1(String EMAIL) {
        ArrayList<TaskDetailCartModel> studentList = new ArrayList<TaskDetailCartModel>();

        String[] columnArray = {"id,userId,task_id,task_number,client_name,date,time,address,latitude,longitude,short_description,description,acknowledged_by,acknowledged_date,acknowledged_time,image_path,image_bitmap,task_process,disposal_site_name,disposal_site_id,disposal_date,disposal_time,job_type,second_man,job_started,sing_off_status,job_start_datetime,task_sync_status,ss1,ss2,ss2_A,ss3,final_sync,new_rental,final_removal,instructions"};
        Cursor c;
        try {

            c = db.query("Details2", columnArray, "task_id =" + "'" + EMAIL
                    + "'", null, null, null, null);
            if (c.getCount() > 0) {

                c.moveToFirst();
                do {
                    TaskDetailCartModel table = new TaskDetailCartModel();
                    table.setId(c.getInt(c.getColumnIndex("id")));
                    table.setUserId(c.getString(c.getColumnIndex("userId")));
                    table.setTask_id(c.getString(c.getColumnIndex("task_id")));
                    table.setTask_number(c.getString(c.getColumnIndex("task_number")));
                    table.setClient_name(c.getString(c.getColumnIndex("client_name")));
                    table.setDate(c.getString(c.getColumnIndex("date")));
                    table.setTime(c.getString(c.getColumnIndex("time")));
                    table.setAddress(c.getString(c.getColumnIndex("address")));
                    table.setLatitude(c.getString(c.getColumnIndex("latitude")));
                    table.setLongitude(c.getString(c.getColumnIndex("longitude")));
                    table.setShort_description(c.getString(c.getColumnIndex("short_description")));
                    table.setDescription(c.getString(c.getColumnIndex("description")));
                    table.setAcknowledged_by(c.getString(c.getColumnIndex("acknowledged_by")));
                    table.setAcknowledged_date(c.getString(c.getColumnIndex("acknowledged_date")));
                    table.setAcknowledged_time(c.getString(c.getColumnIndex("acknowledged_time")));
                    table.setImage_path(c.getString(c.getColumnIndex("image_path")));
                    table.setImage_bitmap(c.getString(c.getColumnIndex("image_bitmap")));
                    table.setTask_process(c.getString(c.getColumnIndex("task_process")));
                    table.setDisposal_site_name(c.getString(c.getColumnIndex("disposal_site_name")));
                    table.setDisposal_site_id(c.getString(c.getColumnIndex("disposal_site_id")));
                    table.setDisposal_date(c.getString(c.getColumnIndex("disposal_date")));
                    table.setDisposal_time(c.getString(c.getColumnIndex("disposal_time")));
                    table.setJob_type(c.getString(c.getColumnIndex("job_type")));
                    table.setSecond_man(c.getString(c.getColumnIndex("second_man")));
                    table.setJob_started(c.getString(c.getColumnIndex("job_started")));
                    table.setSing_off_status(c.getString(c.getColumnIndex("sing_off_status")));
                    table.setJob_start_datetime(c.getString(c.getColumnIndex("job_start_datetime")));
                    table.setTask_sync_status(c.getString(c.getColumnIndex("task_sync_status")));
                    table.setSs1(c.getString(c.getColumnIndex("ss1")));
                    table.setSs2(c.getString(c.getColumnIndex("ss2")));
                    table.setSs2_A(c.getString(c.getColumnIndex("ss2_A")));
                    table.setSs3(c.getString(c.getColumnIndex("ss3")));
                    table.setFinal_sync(c.getString(c.getColumnIndex("final_sync")));
                    table.setNew_rental(c.getString(c.getColumnIndex("new_rental")));
                    table.setFinal_removal(c.getString(c.getColumnIndex("final_removal")));
                    table.setInstructions(c.getString(c.getColumnIndex("instructions")));

                    studentList.add(table);
                } while (c.moveToNext());
//                c.close();
//                db.close();
            } else {
                // Util.customLog("getAll FavValue - No value found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return studentList;
    }

    public ArrayList<TaskDetailCartModel> getTotalListUser(String EMAIL) {
        ArrayList<TaskDetailCartModel> studentList = new ArrayList<TaskDetailCartModel>();

        String[] columnArray = {"id,userId,task_id,task_number,client_name,date,time,address,latitude,longitude,short_description,description,acknowledged_by,acknowledged_date,acknowledged_time,image_path,image_bitmap,task_process,disposal_site_name,disposal_site_id,disposal_date,disposal_time,job_type,second_man,job_started,sing_off_status,job_start_datetime,task_sync_status,ss1,ss2,ss2_A,ss3,final_sync,new_rental,final_removal,instructions"};
        Cursor c;
        try {

            c = db.query("Details2", columnArray, "userId =" + "'" + EMAIL
                    + "'", null, null, null, null);
            if (c.getCount() > 0) {

                c.moveToFirst();
                do {
                    TaskDetailCartModel table = new TaskDetailCartModel();
                    table.setId(c.getInt(c.getColumnIndex("id")));
                    table.setUserId(c.getString(c.getColumnIndex("userId")));
                    table.setTask_id(c.getString(c.getColumnIndex("task_id")));
                    table.setTask_number(c.getString(c.getColumnIndex("task_number")));
                    table.setClient_name(c.getString(c.getColumnIndex("client_name")));
                    table.setDate(c.getString(c.getColumnIndex("date")));
                    table.setTime(c.getString(c.getColumnIndex("time")));
                    table.setAddress(c.getString(c.getColumnIndex("address")));
                    table.setLatitude(c.getString(c.getColumnIndex("latitude")));
                    table.setLongitude(c.getString(c.getColumnIndex("longitude")));
                    table.setShort_description(c.getString(c.getColumnIndex("short_description")));
                    table.setDescription(c.getString(c.getColumnIndex("description")));
                    table.setAcknowledged_by(c.getString(c.getColumnIndex("acknowledged_by")));
                    table.setAcknowledged_date(c.getString(c.getColumnIndex("acknowledged_date")));
                    table.setAcknowledged_time(c.getString(c.getColumnIndex("acknowledged_time")));
                    table.setImage_path(c.getString(c.getColumnIndex("image_path")));
                    table.setImage_bitmap(c.getString(c.getColumnIndex("image_bitmap")));
                    table.setTask_process(c.getString(c.getColumnIndex("task_process")));
                    table.setDisposal_site_name(c.getString(c.getColumnIndex("disposal_site_name")));
                    table.setDisposal_site_id(c.getString(c.getColumnIndex("disposal_site_id")));
                    table.setDisposal_date(c.getString(c.getColumnIndex("disposal_date")));
                    table.setDisposal_time(c.getString(c.getColumnIndex("disposal_time")));
                    table.setJob_type(c.getString(c.getColumnIndex("job_type")));
                    table.setSecond_man(c.getString(c.getColumnIndex("second_man")));
                    table.setJob_started(c.getString(c.getColumnIndex("job_started")));
                    table.setSing_off_status(c.getString(c.getColumnIndex("sing_off_status")));
                    table.setJob_start_datetime(c.getString(c.getColumnIndex("job_start_datetime")));
                    table.setTask_sync_status(c.getString(c.getColumnIndex("task_sync_status")));
                    table.setSs1(c.getString(c.getColumnIndex("ss1")));
                    table.setSs2(c.getString(c.getColumnIndex("ss2")));
                    table.setSs2_A(c.getString(c.getColumnIndex("ss2_A")));
                    table.setSs3(c.getString(c.getColumnIndex("ss3")));
                    table.setFinal_sync(c.getString(c.getColumnIndex("final_sync")));
                    table.setNew_rental(c.getString(c.getColumnIndex("new_rental")));
                    table.setFinal_removal(c.getString(c.getColumnIndex("final_removal")));
                    table.setInstructions(c.getString(c.getColumnIndex("instructions")));

                    studentList.add(table);
                } while (c.moveToNext());
//                c.close();
//                db.close();
            } else {
                // Util.customLog("getAll FavValue - No value found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return studentList;
    }


}
