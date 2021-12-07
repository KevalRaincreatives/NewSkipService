package com.skipservices.utils;

import android.app.Activity;
import android.content.SharedPreferences;

import java.util.Arrays;
import java.util.List;

public abstract class MyUtility {
	public static boolean addFavoriteItem(Activity activity,String favoriteItem){
        //Get previous favorite items
        String favoriteList = getStringFromPreferences(activity,null,"favorites");
        // Append new Favorite item
        if(favoriteList!=null){
            favoriteList = favoriteList+","+favoriteItem;
        }else{
            favoriteList = favoriteItem;
        }
        // Save in Shared Preferences
        return putStringInPreferences(activity,favoriteList,"favorites");
    }
    public static List<String> getFavoriteList(Activity activity){
        String favoriteList = getStringFromPreferences(activity,null,"favorites");
        return convertStringToArray(favoriteList);
    }
    private static boolean putStringInPreferences(Activity activity,String nick,String key){
        SharedPreferences sharedPreferences = activity.getPreferences(Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, nick);
        editor.commit();                        
        return true;        
    }
    private static String getStringFromPreferences(Activity activity,String defaultValue,String key){
        SharedPreferences sharedPreferences = activity.getPreferences(Activity.MODE_PRIVATE);
        String temp = sharedPreferences.getString(key, defaultValue);
        return temp;        
    }

    private static List<String> convertStringToArray(String str){
    	List<String> itemList = null;
    	if(str!=null)
    	{
    		if(str.length()>0)
        	{
              String [] arr = str.split(",");
               itemList = Arrays.asList(arr);
        	}
    	}

        return itemList;
    }

}
