package com.skipservices.utils;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by devloper on 28/07/2016.
 */

public class InternalStorage {
    Context context;
    public static InternalStorage customProgress = null;

    public static InternalStorage getInstance() {
        if (customProgress == null) {
            customProgress = new InternalStorage();
        }
        return customProgress;
    }

//    public InternalStorage(Context context) {
//        this.context = context;
//    }

    public static void writeObject(Context context, String key, Object object) throws IOException {
        FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(object);
        oos.close();
        fos.close();
    }

    public static Object readObject(Context context, String key) throws IOException,
            ClassNotFoundException {
        FileInputStream fis = context.openFileInput(key);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object object = ois.readObject();
        return object;
    }

    public static boolean fileExists(Context context, String key) {
        File file = context.getApplicationContext().getFileStreamPath(key);
        return file.exists();
    }

}
