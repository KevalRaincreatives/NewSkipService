package com.skipservices.Db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by DELL on 03/24/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Login.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "Details";
    public static final String TABLE_NAME2 = "Details2";
    public static final String TABLE_NAME3 = "Details3";
    public static final String TABLE_NAME4 = "SecondMan";
    private Context context;
    private static DatabaseHelper databaseHelper;


    public static final String DATABASE_CREATE = "Create table "
            + TABLE_NAME
            + "(id integer primary key autoincrement, "
            + "userId text,task_id text,task_number text,client_name text,address text,short_description text,job_type text,task_process text, "
            + " UNIQUE (id) ON CONFLICT REPLACE)";

    public static final String DATABASE_CREATE2 = "Create table "
            + TABLE_NAME2
            + "(id integer primary key autoincrement, "
            + "userId text,task_id text,task_number text,client_name text,date text,time text,address text,latitude text,longitude text,short_description text, "
            + "description text,acknowledged_by text,acknowledged_date text,acknowledged_time text,image_path text,image_bitmap text,task_process text,disposal_site_name text,disposal_site_id text, "
            + "disposal_date text,disposal_time text,job_type text,second_man text,job_started text,sing_off_status text,job_start_datetime text,task_sync_status text,ss1 text,ss2 text,ss2_A text,ss3 text,final_sync text,new_rental text,final_removal text,instructions text, "
            + " UNIQUE (id) ON CONFLICT REPLACE)";

    public static final String DATABASE_CREATE3 = "Create table "
            + TABLE_NAME3
            + "(id integer primary key autoincrement, "
            + "userId text,name text,username text,email text,password text, "
            + " UNIQUE (id) ON CONFLICT REPLACE)";

    public static final String DATABASE_CREATE4 = "Create table "
            + TABLE_NAME4
            + "(id integer primary key autoincrement, "
            + "manId text,name text,email text, "
            + " UNIQUE (id) ON CONFLICT REPLACE)";


    public static DatabaseHelper getInstance(Context context) {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context);
        }
        return databaseHelper;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {

            db.execSQL(DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE2);
            db.execSQL(DATABASE_CREATE3);
            db.execSQL(DATABASE_CREATE4);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
