package com.skipservices.Impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.skipservices.Db.DatabaseHelper;
import com.skipservices.Model.SecondMan2Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 03/24/2016.
 */
public class SecondMan_impl implements DBOperation {

    SQLiteDatabase db;

    public SecondMan_impl(Context context) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        try {
            db = databaseHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public long insert(Object object) {

        SecondMan2Model details = (SecondMan2Model) object;
        long result = db.insert(details.TableName(), null, this.getContentvalues(object));
        return result;
    }

    @Override
    public int update(Object object) {
        SecondMan2Model details = (SecondMan2Model) object;
        String[] whereArgs = {String.valueOf(details.getId())};
        return updateall(object, "id = ?", whereArgs);
    }

    private int updateall(Object object, String string, String[] whereArgs) {
        SecondMan2Model table = (SecondMan2Model) object;
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
        SecondMan2Model details = (SecondMan2Model) object;
        String[] whereArgs = {String.valueOf(details.getId())};
        int result = db.delete(details.TableName(), "id = ?", whereArgs);
        // Util.customLog("Delete Result - " + result);
        return result;
    }

    @Override
    public ContentValues getContentvalues(Object object) {
        SecondMan2Model details = (SecondMan2Model) object;

        ContentValues contentValues = new ContentValues();
        contentValues.put("manId", details.getManId());
        contentValues.put("name", details.getName());
        contentValues.put("email", details.getEmail());

        return contentValues;
    }

    public boolean isExist(String EMAIL) {
        Cursor cursor = db.rawQuery("SELECT name FROM SecondMan WHERE name = '" + EMAIL
                + "'", null);
        boolean exist = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exist;
    }

    public void delete_record(int id) {
        db.delete(DatabaseHelper.TABLE_NAME4, "id=" + "'" + id + "'", null);
    }

    public List<SecondMan2Model> getUserDetails(String EMAIL) {
        List<SecondMan2Model> studentList = new ArrayList<SecondMan2Model>();

        String[] columnArray = {"id,manId,name,emai,password"};
        Cursor c;
        try {
            c = db.query("SecondMan", columnArray, "name=" + "'" + EMAIL
                    + "'", null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                do {
                    SecondMan2Model table = new SecondMan2Model();
                    table.setId(c.getInt(c.getColumnIndex("id")));
                    table.setManId(c.getString(c.getColumnIndex("manId")));
                    table.setName(c.getString(c.getColumnIndex("name")));
                    table.setEmail(c.getString(c.getColumnIndex("email")));

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

    public ArrayList<SecondMan2Model> getUserDetails1(String EMAIL) {
        ArrayList<SecondMan2Model> studentList = new ArrayList<SecondMan2Model>();

        String[] columnArray = {"id,manId,name,email "};
        Cursor c;
        try {
            c = db.query("SecondMan", columnArray, "name=" + "'" + EMAIL
                    + "'", null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                do {
                    SecondMan2Model table = new SecondMan2Model();
                    table.setId(c.getInt(c.getColumnIndex("id")));
                    table.setManId(c.getString(c.getColumnIndex("manId")));
                    table.setName(c.getString(c.getColumnIndex("name")));
                    table.setEmail(c.getString(c.getColumnIndex("email")));

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

    public ArrayList<SecondMan2Model> getUser() {
        ArrayList<SecondMan2Model> studentList = new ArrayList<SecondMan2Model>();

        String[] columnArray = {"id,manId,name,email"};
        Cursor c;
        try {
            c = db.query("SecondMan", columnArray, null, null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                do {
                    SecondMan2Model table = new SecondMan2Model();
                    table.setId(c.getInt(c.getColumnIndex("id")));
                    table.setManId(c.getString(c.getColumnIndex("manId")));
                    table.setName(c.getString(c.getColumnIndex("name")));
                    table.setEmail(c.getString(c.getColumnIndex("email")));

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
