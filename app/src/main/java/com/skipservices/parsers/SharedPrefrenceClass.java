package com.skipservices.parsers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPrefrenceClass {
	SharedPreferences sharedPreferences;
	Editor editor;

	Context context;
	public SharedPrefrenceClass(Context context) {
		super();
		// TODO Auto-generated constructor stub
		this.context = context;
		sharedPreferences = context.getSharedPreferences("ivm",
				Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();

	}
	
	
	// LOGIN DETAILS CONSTANT 
		public static String KEY_LOGIN_USERID = "userId";
		public static String KEY_LOGIN_NAME = "name";
		public static String KEY_LOGIN_USERNAME = "username";
		public static String KEY_LOGIN_EMAIL = "email";
	public static String KEY_LOGIN_PASSWORD= "password";
		
		
		
		
		public String getKEY_LOGIN_USERID() {
			return sharedPreferences
					.getString(KEY_LOGIN_USERID, "");
			
		}

		public void setKEY_LOGIN_USERID(String kEY_LOGIN_USERID) {
			editor.putString(KEY_LOGIN_USERID,
					kEY_LOGIN_USERID);
			editor.commit();
			
		}

		public String getKEY_LOGIN_NAME() {
			return sharedPreferences
					.getString(KEY_LOGIN_NAME, "");
			
		}

		public void setKEY_LOGIN_NAME(String kEY_LOGIN_NAME) {
			editor.putString(KEY_LOGIN_NAME,
					kEY_LOGIN_NAME);
			editor.commit();
			
		}

		public String getKEY_LOGIN_USERNAME() {
			return sharedPreferences
					.getString(KEY_LOGIN_USERNAME, "");
			
		}

		public void setKEY_LOGIN_USERNAME(String kEY_LOGIN_USERNAME) {
			editor.putString(KEY_LOGIN_USERNAME,
					kEY_LOGIN_USERNAME);
			editor.commit();
			
		}

		public String getKEY_LOGIN_EMAIL() {
			return sharedPreferences
					.getString(KEY_LOGIN_EMAIL, "");
			
		}

		public void setKEY_LOGIN_EMAIL(String kEY_LOGIN_EMAIL) {
			editor.putString(KEY_LOGIN_EMAIL,
					kEY_LOGIN_EMAIL);
			editor.commit();
			
		}

	public String getKEY_LOGIN_PASSWORD() {
		return sharedPreferences
				.getString(KEY_LOGIN_PASSWORD, "");

	}

	public void setKEY_LOGIN_PASSWORD(String kEY_LOGIN_PASSWORD) {
		editor.putString(KEY_LOGIN_PASSWORD,
				kEY_LOGIN_PASSWORD);
		editor.commit();

	}
}
