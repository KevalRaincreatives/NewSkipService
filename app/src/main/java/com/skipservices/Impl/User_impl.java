package com.skipservices.Impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.skipservices.Db.DatabaseHelper;
import com.skipservices.Model.UserModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 03/24/2016.
 */
public class User_impl implements DBOperation {

    SQLiteDatabase db;

    public User_impl(Context context) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        try {
            db = databaseHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public long insert(Object object) {

        UserModel details = (UserModel) object;
        long result = db.insert(details.TableName(), null, this.getContentvalues(object));
        return result;
    }

    @Override
    public int update(Object object) {
        UserModel details = (UserModel) object;
        String[] whereArgs = {String.valueOf(details.getId())};
        return updateall(object, "id = ?", whereArgs);
    }

    private int updateall(Object object, String string, String[] whereArgs) {
        UserModel table = (UserModel) object;
        int result = db.update(table.TableName(),
                this.getContentvalues(object), string, whereArgs);

        return result;
        // TODO Auto-generated method stub

    }

    public boolean getSinlgeEntry(String EMAIL, String PASSWORD) {
        Cursor c = db.rawQuery(
                "SELECT name,password FROM Details3 WHERE name = "
                        + "'" + EMAIL + "' AND password = " + "'"
                        + PASSWORD + "'", null);
        boolean exist = (c.getCount() > 0);

        return exist;

    }

    @Override
    public int delete(Object object) {
        UserModel details = (UserModel) object;
        String[] whereArgs = {String.valueOf(details.getId())};
        int result = db.delete(details.TableName(), "id = ?", whereArgs);
        // Util.customLog("Delete Result - " + result);
        return result;
    }

    @Override
    public ContentValues getContentvalues(Object object) {
        UserModel details = (UserModel) object;

        ContentValues contentValues = new ContentValues();
        contentValues.put("userId", details.getUserId());
        contentValues.put("name", details.getName());
        contentValues.put("username", details.getUsername());
        contentValues.put("email", details.getEmail());
        contentValues.put("password", details.getPassword());

        return contentValues;
    }

    public boolean isExist(String EMAIL) {
        Cursor cursor = db.rawQuery("SELECT name FROM Details3 WHERE name = '" + EMAIL
                + "'", null);
        boolean exist = (cursor.getCount() > 0);
        cursor.close();
//        db.close();
        return exist;
    }

    public void delete_record(int id) {
        db.delete(DatabaseHelper.TABLE_NAME3, "id=" + "'" + id + "'", null);
    }

    public List<UserModel> getUserDetails(String EMAIL) {
        List<UserModel> studentList = new ArrayList<UserModel>();

        String[] columnArray = {"id,userId,name,username,email,password "};
        Cursor c;
        try {
            c = db.query("Details3", columnArray, "name=" + "'" + EMAIL
                    + "'", null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                do {
                    UserModel table = new UserModel();
                    table.setId(c.getInt(c.getColumnIndex("id")));
                    table.setUserId(c.getString(c.getColumnIndex("userId")));
                    table.setName(c.getString(c.getColumnIndex("name")));
                    table.setUsername(c.getString(c.getColumnIndex("username")));
                    table.setEmail(c.getString(c.getColumnIndex("email")));
                    table.setPassword(c.getString(c.getColumnIndex("password")));

                    Log.e("ke1", c.getString(c.getColumnIndex("name")) + "");
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

    public ArrayList<UserModel> getUserDetails1(String EMAIL) {
        ArrayList<UserModel> studentList = new ArrayList<UserModel>();

        String[] columnArray = {"id,userId,name,username,email,password"};
        Cursor c;
        try {
            c = db.query("Details3", columnArray, "name =" + "'" + EMAIL
                    + "'", null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                do {
                    UserModel table = new UserModel();
                    table.setId(c.getInt(c.getColumnIndex("id")));
                    table.setUserId(c.getString(c.getColumnIndex("userId")));
                    table.setName(c.getString(c.getColumnIndex("name")));
                    table.setUsername(c.getString(c.getColumnIndex("username")));
                    table.setEmail(c.getString(c.getColumnIndex("email")));
                    table.setPassword(c.getString(c.getColumnIndex("password")));

                    Log.e("ke1", c.getString(c.getColumnIndex("username")) + "");
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

    public ArrayList<UserModel> getUser() {
        ArrayList<UserModel> studentList = new ArrayList<UserModel>();

        String[] columnArray = {"id,userId,name,username,email,password "};
        Cursor c;
        try {
            c = db.query("Details3", columnArray, null, null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                do {
                    UserModel table = new UserModel();
                    table.setId(c.getInt(c.getColumnIndex("id")));
                    table.setUserId(c.getString(c.getColumnIndex("userId")));
                    table.setName(c.getString(c.getColumnIndex("name")));
                    table.setUsername(c.getString(c.getColumnIndex("username")));
                    table.setEmail(c.getString(c.getColumnIndex("email")));
                    table.setPassword(c.getString(c.getColumnIndex("password")));

                    Log.e("ke1", c.getString(c.getColumnIndex("username")) + "");
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

}
