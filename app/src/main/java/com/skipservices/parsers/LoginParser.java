package com.skipservices.parsers;


import org.json.JSONObject;

import com.skipservices.utils.WebService;

import android.content.Context;
import android.util.Log;

public class LoginParser {

	// response from server={"userId":"528","name":"Driver 1","username":"driver1","email":"ram.l@raincreatives.com","message":"Login successfully","status":"y"}

	public  String DEBUG_TAG = "LoginParser";
	Context context;
	String Jsonstring;
	SharedPrefrenceClass sharedPrefrenceClass;
	public String message = "";
	public String Status = "";
	
	public LoginParser(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		sharedPrefrenceClass = new SharedPrefrenceClass(context);
	}
	
	
	public  void JsonStringToArrayList(String jsonstring) {
		// TODO Auto-generated method stub
        
		try
		{
			message = "";
			JSONObject jsonobject = new JSONObject(jsonstring);
			Status = jsonobject.optString(WebService.KEY_LOGIN_STATUS);
			
			if(Status.equalsIgnoreCase("y"))
			{
				message = jsonobject.optString(WebService.KEY_LOGIN_MESSAGE);
				sharedPrefrenceClass.setKEY_LOGIN_NAME(jsonobject.optString(WebService.KEY_LOGIN_NAME));
				sharedPrefrenceClass.setKEY_LOGIN_USERNAME(jsonobject.optString(WebService.KEY_LOGIN_USERNAME));
				sharedPrefrenceClass.setKEY_LOGIN_USERID(jsonobject.optString(WebService.KEY_LOGIN_USERID));
				sharedPrefrenceClass.setKEY_LOGIN_EMAIL(jsonobject.optString(WebService.KEY_LOGIN_EMAIL));
			}
			else
			{
				message = jsonobject.optString(WebService.KEY_LOGIN_MESSAGE);
				
			}
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public String Getmessage()
	{
		return message;
	}
	public String Getstatus()
	{
		return Status;
	}
	
}
