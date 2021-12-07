package com.skipservices.Impl;

import android.content.ContentValues;

/**
 * Created by DELL on 03/24/2016.
 */
public interface DBOperation {

    public long insert(Object object);

    public int update(Object object);

    public int delete(Object object);

    ContentValues getContentvalues(Object object);

}
