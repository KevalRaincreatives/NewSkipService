package com.skipservices.parsers;


import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.skipservices.utils.WebService;

import android.content.Context;
import android.util.Log;

public class TaskListParser {

	/*[
  {
    "id": "1",
    "task_number": "skip00042",
    "client_name": "abcxyz pvt ltd.",
    "address": "abc road, xyz city, 00000",
    "short_description": "short description 1",
    "job_type": "Removel",
    "task_peocess": "Collected"
  },
  {
    "id": "2",
    "task_number": "task01244",
    "client_name": "xyz Corp.",
    "address": "Company name, Street name, City",
    "short_description": "short description 2",
    "job_type": "Removel",
    "task_peocess": "Pending"
  }
]
*/
	public  String DEBUG_TAG = "TaskListParser";
	Context context;
	ArrayList<HashMap<String, String>>arrayList = new ArrayList<HashMap<String,String>>();
	
	
	
	public TaskListParser(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		
	}
	
	
	public  void JsonStringToArrayList(String jsonstring) {
		// TODO Auto-generated method stub
        
		try
		{
			
			//JSONObject jsonobject = new JSONObject(jsonstring);
		    JSONArray jsonArray = new JSONArray(jsonstring);
		    Log.i(DEBUG_TAG, "jsonarraylengeth="+jsonArray.length());
		    if(jsonArray.length()>0)
		    {
		    	for(int i=0;i<jsonArray.length();i++)
			    {
		    		 Log.i(DEBUG_TAG, "jsonarraylengeth="+jsonArray.length());
			    	JSONObject jsonObject = jsonArray.optJSONObject(i);
			    	HashMap<String, String>hashMap = new  HashMap<String, String>();
			    	hashMap.put(WebService.KEY_TASKLIST_TASKID, jsonObject.optString(WebService.KEY_TASKLIST_TASKID));
			    	hashMap.put(WebService.KEY_TASKLIST_TASKNUMBER, jsonObject.optString(WebService.KEY_TASKLIST_TASKNUMBER));
			    	hashMap.put(WebService.KEY_TASKLIST_CLIENTNAME, jsonObject.optString(WebService.KEY_TASKLIST_CLIENTNAME));
			    	hashMap.put(WebService.KEY_TASKLIST_ADDRESS, jsonObject.optString(WebService.KEY_TASKLIST_ADDRESS));
			    	hashMap.put(WebService.KEY_TASKLIST_SHORTDESCRIPTION, jsonObject.optString(WebService.KEY_TASKLIST_SHORTDESCRIPTION));
			    	hashMap.put(WebService.KEY_TASKLIST_JOBTYPE, jsonObject.optString(WebService.KEY_TASKLIST_JOBTYPE));
			    	hashMap.put(WebService.KEY_TASKLIST_TASKPROCESS, jsonObject.optString(WebService.KEY_TASKLIST_TASKPROCESS));
			    	
			    	arrayList.add(hashMap);
			    	
			    }
		    }                                                                                                                                                                                                                                    
		    else
		    {
		    	//no data 
		    }
		    
			
		    Log.i(DEBUG_TAG, "arraylistbeforelenth="+arrayList.toString());
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public ArrayList<HashMap<String, String>>GetTaskList()
	{
		Log.i(DEBUG_TAG, "arraylistafterlenth="+arrayList.toString());
		return arrayList;
	}
	
}
