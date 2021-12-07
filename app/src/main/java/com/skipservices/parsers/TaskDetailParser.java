package com.skipservices.parsers;


import android.content.Context;
import android.util.Log;

import com.skipservices.utils.WebService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskDetailParser {

    /*{
  "id": "1",
  "task_number": "skip00042-kartik",
  "client_name": "abcxyz pvt ltd.",
  "date": "22nd September 2016",
  "time": "12:09 PM",
  "address": "abc road, xyz city, 00000",
  "latitude": "13.113222",
  "longitude": "-59.598808",
  "short_description": "short description 1",
  "description": "duane Kartik /Simone changed it now. Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
  "tipping_books": [
    {
      "book_id": "1",
      "book_numner": "skip0001",
      "tickets": [
        "7",
        "10"
      ],
      "whole_book": 0
    },
    {
      "book_id": "3",
      "book_numner": "skip0542",
      "tickets": [
        "0"
      ],
      "whole_book": 1
    }
  ],
  "acknowledged_by": "ram",
  "acknowledged_date": "22nd September 2016",
  "acknowledged_time": "10:09 AM",
  "used_tickets": [
    {
      "id": "19",
      "book": "skip0001",
      "ticket": "6"
    },
    {
      "id": "20",
      "book": "skip0001",
      "ticket": "5"
    },
    {
      "id": "21",
      "book": "skip0001",
      "ticket": "9"
    },
    {
      "id": "23",
      "book": "skip0001",
      "ticket": "8"
    }
  ],
  "image_path": "http://ivmstore.com/skip2/administrator/components/com_task_managment/images/1474521037.jpg",
  "task_process": "Collected",
  "disposal_site": "SBRC",
  "disposal_site_id": "202155",
  "disposal_date": "22nd September 2016",
  "disposal_time": "15:09 PM",
  "job_type": "Removel"
}
*/
    public String DEBUG_TAG = "TaskListParser";
    Context context;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> arrayList_tippingbooks = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> arrayList_usedtipping = new ArrayList<HashMap<String, String>>();
    ArrayList<String> arrayList_book = new ArrayList<String>();
    ArrayList<String> arrayList_ticket = new ArrayList<String>();

    public TaskDetailParser(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;

    }


    public void JsonStringToArrayList(String jsonstring) {
        // TODO Auto-generated method stub

        try {

            //JSONObject jsonobject = new JSONObject(jsonstring);


            JSONObject jsonObject = new JSONObject(jsonstring);
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put(WebService.KEY_TASKDETAIL_TASKID, jsonObject.optString(WebService.KEY_TASKDETAIL_TASKID));
            hashMap.put(WebService.KEY_TASKDETAIL_TASKNUMBER, jsonObject.optString(WebService.KEY_TASKDETAIL_TASKNUMBER));
            hashMap.put(WebService.KEY_TASKDETAIL_CLIENTNAME, jsonObject.optString(WebService.KEY_TASKDETAIL_CLIENTNAME));

            hashMap.put(WebService.KEY_TASKDETAIL_DATE, jsonObject.optString(WebService.KEY_TASKDETAIL_DATE));
            hashMap.put(WebService.KEY_TASKDETAIL_TIME, jsonObject.optString(WebService.KEY_TASKDETAIL_TIME));
            hashMap.put(WebService.KEY_TASKDETAIL_ADDRESS, jsonObject.optString(WebService.KEY_TASKDETAIL_ADDRESS));
            hashMap.put(WebService.KEY_TASKDETAIL_LATITUDE, jsonObject.optString(WebService.KEY_TASKDETAIL_LATITUDE));

            hashMap.put(WebService.KEY_TASKDETAIL_LONGITUDE, jsonObject.optString(WebService.KEY_TASKDETAIL_LONGITUDE));
            hashMap.put(WebService.KEY_TASKDETAIL_SHORTDESCRIPTION, jsonObject.optString(WebService.KEY_TASKDETAIL_SHORTDESCRIPTION));
            hashMap.put(WebService.KEY_TASKDETAIL_DESCRIPTION, jsonObject.optString(WebService.KEY_TASKDETAIL_DESCRIPTION));

            hashMap.put(WebService.KEY_TASKDETAIL_TASKPROCESS, jsonObject.optString(WebService.KEY_TASKDETAIL_TASKPROCESS));
            hashMap.put("second_man", jsonObject.optString("second_man"));
            hashMap.put("job_started", jsonObject.optString("job_started"));
            hashMap.put(WebService.KEY_TASKDETAIL_DISPOSALSITE, jsonObject.optString(WebService.KEY_TASKDETAIL_DISPOSALSITE));
            hashMap.put(WebService.KEY_TASKDETAIL_DISPOSALSITEID, jsonObject.optString(WebService.KEY_TASKDETAIL_DISPOSALSITEID));

            hashMap.put(WebService.KEY_TASKDETAIL_DISPOSALDATE, jsonObject.optString(WebService.KEY_TASKDETAIL_DISPOSALDATE));
            hashMap.put(WebService.KEY_TASKDETAIL_DISPOSALTIME, jsonObject.optString(WebService.KEY_TASKDETAIL_DISPOSALTIME));
            hashMap.put(WebService.KEY_TASKDETAIL_JOBTYPE, jsonObject.optString(WebService.KEY_TASKDETAIL_JOBTYPE));

            hashMap.put(WebService.KEY_TASKDETAIL_ACKNOWLEDGEDBY, jsonObject.optString(WebService.KEY_TASKDETAIL_ACKNOWLEDGEDBY));
            hashMap.put(WebService.KEY_TASKDETAIL_ACKNOWLEDGEDDATE, jsonObject.optString(WebService.KEY_TASKDETAIL_ACKNOWLEDGEDDATE));
            hashMap.put(WebService.KEY_TASKDETAIL_ACKNOWLEDGEDTIME, jsonObject.optString(WebService.KEY_TASKDETAIL_ACKNOWLEDGEDTIME));
            hashMap.put(WebService.KEY_TASKDETAIL_ACKNOWLEDGEDIMAGE, jsonObject.optString(WebService.KEY_TASKDETAIL_ACKNOWLEDGEDIMAGE));

            arrayList.add(hashMap);


            JSONArray jsonArray = jsonObject.getJSONArray(WebService.KEY_TASKDETAIL_TIPPINGBOOKS);
            HashMap<String, String> map1 = new HashMap<String, String>();
            map1.put(WebService.KEY_TASKDETAIL_BOOKID, "0");
            map1.put(WebService.KEY_TASKDETAIL_BOONUMBER, "please select book number");
            map1.put(WebService.KEY_TASKDETAIL_WHOLEBOOK, "0");
            map1.put("numbers", "0*0");
            arrayList_tippingbooks.add(map1);
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject2 = jsonArray.optJSONObject(i);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(WebService.KEY_TASKDETAIL_BOOKID, jsonObject2.getString(WebService.KEY_TASKDETAIL_BOOKID));
                map.put(WebService.KEY_TASKDETAIL_BOONUMBER, jsonObject2.getString(WebService.KEY_TASKDETAIL_BOONUMBER));
                map.put(WebService.KEY_TASKDETAIL_WHOLEBOOK, jsonObject2.getString(WebService.KEY_TASKDETAIL_WHOLEBOOK));
                Log.i(DEBUG_TAG, "data=" + jsonObject2.optJSONArray(WebService.KEY_TASKDETAIL_TICETS).toString());
                JSONArray arraytic = jsonObject2.optJSONArray(WebService.KEY_TASKDETAIL_TICETS);
                Log.i(DEBUG_TAG, "size=" + arraytic.length());
                StringBuffer output = new StringBuffer();
                for (int j = 0; j < arraytic.length(); j++) {

                    Log.i(DEBUG_TAG, "size=" + j);
                    if (j == 0) {
                        output.append(arraytic.getString(j));
                    } else {
                        output.append("*" + arraytic.getString(j));
                    }

                    map.put("numbers", output.toString());
                }

                arrayList_tippingbooks.add(map);
            }

            JSONArray jsonArrayusedtb = jsonObject.getJSONArray(WebService.KEY_TASKDETAIL_USEDTICKETARRAY);

            for (int i = 0; i < jsonArrayusedtb.length(); i++) {

                JSONObject object = jsonArrayusedtb.optJSONObject(i);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(WebService.KEY_TASKDETAIL_USEDTICKETID, object.optString(WebService.KEY_TASKDETAIL_USEDTICKETID));
                map.put(WebService.KEY_TASKDETAIL_USEDTICKETBOOK, object.optString(WebService.KEY_TASKDETAIL_USEDTICKETBOOK));
                map.put(WebService.KEY_TASKDETAIL_USEDTICKET, object.optString(WebService.KEY_TASKDETAIL_USEDTICKET));
                arrayList_book.add(object.optString(WebService.KEY_TASKDETAIL_USEDTICKETBOOK));
                arrayList_ticket.add(object.optString(WebService.KEY_TASKDETAIL_USEDTICKET));
                arrayList_usedtipping.add(map);
            }


            Log.i(DEBUG_TAG, "arraylistbeforelenth=" + arrayList.toString());
            Log.i(DEBUG_TAG, "arraylistbookbeforelenth=" + arrayList_tippingbooks.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ArrayList<HashMap<String, String>> GetTaskDetailList() {
        return arrayList;
    }

    public ArrayList<HashMap<String, String>> GetTippingBookList() {
        return arrayList_tippingbooks;
    }

    public ArrayList<HashMap<String, String>> GetUsedTippingBookList() {
        return arrayList_usedtipping;
    }

    public ArrayList<String> GetUsedBook() {
        return arrayList_book;
    }

    public ArrayList<String> GetUsedTicket() {
        return arrayList_ticket;
    }

}
