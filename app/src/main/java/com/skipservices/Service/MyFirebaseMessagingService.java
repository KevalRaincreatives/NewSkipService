package com.skipservices.Service;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.skipservices.App.Config;
import com.skipservices.Impl.Details_impl;
import com.skipservices.Model.TaskDetailCartModel;
import com.skipservices.R;
import com.skipservices.TaskListActivity;
import com.skipservices.parsers.SharedPrefrenceClass;
import com.skipservices.parsers.TaskListModel;
import com.skipservices.utils.WebService;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Ravi Tamada on 08/08/16.
 * www.androidhive.info
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;
    Details_impl details_impl;
    private ArrayList<TaskDetailCartModel> list_details = new ArrayList<TaskDetailCartModel>();
    private RequestQueue mRequestQueue;
    int f_pos = 0;
    MultipartEntity reqEntity;
    ArrayList<TaskListModel> arrLike = new ArrayList<>();

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN", s);
        String refreshedToken = s;
        Log.e("tokenfcm", refreshedToken);
        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(final String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                public void run() {


                    Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
//                    ShowDilagBonusMessage(getBaseContext(), message);
                    f_pos = 0;
                    CallApiSequence();

                }
            });


//            AlertDialog alertDialog = new AlertDialog.Builder(this)
//                    .setTitle("Title")
//                    .setMessage(message)
//                    .create();
//
//            alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//            alertDialog.show();

            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);


            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), TaskListActivity.class);
                resultIntent.putExtra("message", message);

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);
    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();
    }

    public void ShowDilagBonusMessage(final Context context, String message) {
        final Dialog dialog = new Dialog(getBaseContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_productdetailbonusmessage);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCanceledOnTouchOutside(false);

        Button button = (Button) dialog.findViewById(R.id.btn_ok);
        TextView textView = (TextView) dialog.findViewById(R.id.txt_message);
        textView.setText(message);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        f_pos = 0;
                        CallApiSequence();
                        dialog.dismiss();
                    }
                }
            }
        });

        dialog.show();

    }

    private void CallApiSequence() {
        details_impl = new Details_impl(getBaseContext());
        list_details = details_impl.getUser();

        if (list_details.size() > f_pos) {
            CallApiSequence2(f_pos);
        } else {
            mRequestQueue = Volley.newRequestQueue(getBaseContext());

            StringRequest mStringRequest = new StringRequest(Request.Method.POST, WebService.SERVERURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("mainactivity_resonse", response);

                            details_impl = new Details_impl(getBaseContext());
                            details_impl.delete_table();
                            list_details.clear();

                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                Gson gson2 = new Gson();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject arrayElement_0 = jsonArray.getJSONObject(i);
                                    if (!arrayElement_0.getString("task_process").equals("Completed")) {
                                        arrLike.add(gson2.fromJson(String.valueOf(arrayElement_0), TaskListModel.class));

                                        String task_id = arrayElement_0.getString("id");
                                        String task_number = arrayElement_0.getString("task_number");
                                        String client_name = arrayElement_0.getString("client_name");
                                        String date = arrayElement_0.getString("date");
                                        String time = arrayElement_0.getString("time");
                                        String address = arrayElement_0.getString("address");
                                        String latitude = arrayElement_0.getString("latitude");
                                        String longitude = arrayElement_0.getString("longitude");
                                        String short_description = arrayElement_0.getString("short_description");
                                        String description = arrayElement_0.getString("description");
                                        String acknowledged_by = arrayElement_0.getString("acknowledged_by");
                                        String acknowledged_date = arrayElement_0.getString("acknowledged_date");
                                        String acknowledged_time = arrayElement_0.getString("acknowledged_time");
                                        String image_path = arrayElement_0.getString("image_path");
                                        String task_process = arrayElement_0.getString("task_process");
                                        String disposal_site_name = arrayElement_0.getString("disposal_site_name");
                                        String disposal_site_id = arrayElement_0.getString("disposal_site_id");
                                        String disposal_date = arrayElement_0.getString("disposal_date");
                                        String disposal_time = arrayElement_0.getString("disposal_time");
                                        String job_type = arrayElement_0.getString("job_type");
                                        String second_man = arrayElement_0.getString("second_man");
                                        String job_started = arrayElement_0.getString("job_started");
                                        String sing_off_status = arrayElement_0.getString("sing_off_status");
                                        String job_start_datetime = arrayElement_0.getString("job_start_datetime");
                                        String task_sync_status = arrayElement_0.getString("task_sync_status");
                                        String ss1 = arrayElement_0.getString("ss1");
                                        String ss2 = arrayElement_0.getString("ss2");
                                        String ss2_A = arrayElement_0.getString("ss2_A");
                                        String ss3 = arrayElement_0.getString("ss3");
                                        String final_sync = arrayElement_0.getString("final_sync");
                                        String new_rental = arrayElement_0.getString("new_rental");
                                        String final_removal = arrayElement_0.getString("final_removal");
                                        String instructions = arrayElement_0.getString("instructions");

                                        details_impl = new Details_impl(getBaseContext());
                                        SharedPrefrenceClass sharedPrefrenceClass = new SharedPrefrenceClass(getBaseContext());
                                        long det = details_impl.insert(new TaskDetailCartModel(0, sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(task_id), task_number, client_name, date, time, address,
                                                latitude, longitude, short_description, description, acknowledged_by, acknowledged_date, acknowledged_time, image_path, "",
                                                task_process, disposal_site_name, disposal_site_id, disposal_date, disposal_time, job_type, second_man, job_started, sing_off_status
                                                , job_start_datetime, task_sync_status, ss1, ss2, ss2_A, ss3, final_sync, new_rental, final_removal,instructions
                                        ));
                                    }
                                }


                                ActivityManager am = (ActivityManager) getBaseContext().getSystemService(Context.ACTIVITY_SERVICE);
                                ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
                                if (String.valueOf(cn).contains("TaskListActivity")) {
//                                    startActivity(new Intent(getApplicationContext(), TaskListActivity.class));

                                    Intent i = new Intent(getApplicationContext(), TaskListActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                }
                                Log.d("hellosss", String.valueOf(cn));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(WebService.KEY_TASK, "job_started_offline");
                    String datetime2 = "";
                    SimpleDateFormat myFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//Y-m-d H:i:s  2016-09-19 15:09:00
                    try {
                        Date date = new Date();
                        datetime2 = myFormat2.format(date);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    SharedPrefrenceClass sharedPrefrenceClass = new SharedPrefrenceClass(getBaseContext());
                    params.put(WebService.KEY_TASK, WebService.KEY_TASKLIST_TASK_OFFLINE);
                    params.put(WebService.KEY_TASKLIST_DRIVERIDKEY, sharedPrefrenceClass.getKEY_LOGIN_USERID());
                    params.put(WebService.KEY_TASKLIST_DATEKEY, datetime2);
                    arrLike.clear();
                    return params;
                }
            };

            mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    50000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            mRequestQueue.add(mStringRequest);
        }

    }

    private void CallApiSequence2(final int i) {
        details_impl = new Details_impl(getBaseContext());
        list_details = details_impl.getUser();


        if (list_details.get(i).getSs1().equalsIgnoreCase("1")) {
            mRequestQueue = Volley.newRequestQueue(getBaseContext());

            StringRequest mStringRequest = new StringRequest(Request.Method.POST, WebService.SERVERURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("mainactivity_resonse", response);
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(response);

                                String status = jsonObject.getString("status");
                                String message = jsonObject.getString("message");
                                String task_sync_status = jsonObject.getString("task_sync_status");
                                String ss1 = jsonObject.getString("ss1");
                                String ss2 = jsonObject.getString("ss2");
                                String ss2_A = jsonObject.getString("ss2_A");
                                String ss3 = jsonObject.getString("ss3");
                                String final_sync = jsonObject.getString("final_sync");

                                details_impl = new Details_impl(getBaseContext());
                                SharedPrefrenceClass sharedPrefrenceClass = new SharedPrefrenceClass(getBaseContext());
                                list_details = details_impl.getUserDetails1(String.valueOf(list_details.get(i).getTask_id()));
                                long det = details_impl.update(new TaskDetailCartModel(list_details.get(0).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list_details.get(0).getTask_id()), list_details.get(0).getTask_number(), list_details.get(0).getClient_name(), list_details.get(0).getDate(), list_details.get(0).getTime(), list_details.get(0).getAddress(),
                                        list_details.get(0).getLatitude(), list_details.get(0).getLongitude(), list_details.get(0).getShort_description(), list_details.get(0).getDescription(), list_details.get(0).getAcknowledged_by(), list_details.get(0).getAcknowledged_date(), list_details.get(0).getAcknowledged_time(), list_details.get(0).getImage_path(), list_details.get(0).getImage_bitmap(),
                                        list_details.get(0).getTask_process(), list_details.get(0).getDisposal_site_name(), list_details.get(0).getDisposal_site_id(), list_details.get(0).getDisposal_date(), list_details.get(0).getDisposal_time(), list_details.get(0).getJob_type(), list_details.get(0).getSecond_man(), list_details.get(0).getJob_started(), list_details.get(0).getSing_off_status(), list_details.get(0).getJob_start_datetime(),
                                        task_sync_status, ss1, ss2, ss2_A, ss3, final_sync, list_details.get(0).getNew_rental(), list_details.get(0).getFinal_removal(), list_details.get(0).getInstructions()
                                ));
                                CallApiSequence();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(WebService.KEY_TASK, "job_started_offline");
                    params.put(WebService.KEY_TIPPINGBOOTASKID, list_details.get(i).getTask_id());
                    params.put("second_man", list_details.get(i).getSecond_man());
                    params.put("job_start_datetime", list_details.get(i).getJob_start_datetime());
                    params.put("task_sync_status", list_details.get(i).getTask_sync_status());
                    params.put("ss1", list_details.get(i).getSs1());
                    params.put("ss2", list_details.get(i).getSs2());
                    params.put("ss2_A", list_details.get(i).getSs2_A());
                    params.put("ss3", list_details.get(i).getSs3());
                    params.put("final_sync", list_details.get(i).getFinal_sync());


                    return params;
                }
            };

            mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    50000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            mRequestQueue.add(mStringRequest);

        } else if (list_details.get(i).getSs2().equalsIgnoreCase("1")) {
            new SubmitRoadSideAssistDataAsyTask(i).execute();
        } else if (list_details.get(i).getSs2_A().equalsIgnoreCase("1")) {
            mRequestQueue = Volley.newRequestQueue(getBaseContext());
            StringRequest mStringRequest = new StringRequest(Request.Method.POST, WebService.SERVERURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("mainactivity_resonse", response);
                            RequestQueue mRequestQueue2 = Volley.newRequestQueue(getBaseContext());

                            StringRequest mStringRequest2 = new StringRequest(Request.Method.POST, WebService.SERVERURL,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.d("mainactivity_resonse", response);
                                            JSONObject jsonObject = null;
                                            try {
                                                jsonObject = new JSONObject(response);

                                                String status = jsonObject.getString("status");
                                                String message = jsonObject.getString("message");
                                                String task_sync_status = jsonObject.getString("task_sync_status");
                                                String ss1 = jsonObject.getString("ss1");
                                                String ss2 = jsonObject.getString("ss2");
                                                String ss2_A = jsonObject.getString("ss2_A");
                                                String ss3 = jsonObject.getString("ss3");
                                                String final_sync = jsonObject.getString("final_sync");

                                                details_impl = new Details_impl(getBaseContext());
                                                SharedPrefrenceClass sharedPrefrenceClass = new SharedPrefrenceClass(getBaseContext());
                                                list_details = details_impl.getUserDetails1(String.valueOf(list_details.get(i).getTask_id()));
                                                long det = details_impl.update(new TaskDetailCartModel(list_details.get(0).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list_details.get(0).getTask_id()), list_details.get(0).getTask_number(), list_details.get(0).getClient_name(), list_details.get(0).getDate(), list_details.get(0).getTime(), list_details.get(0).getAddress(),
                                                        list_details.get(0).getLatitude(), list_details.get(0).getLongitude(), list_details.get(0).getShort_description(), list_details.get(0).getDescription(), list_details.get(0).getAcknowledged_by(), list_details.get(0).getAcknowledged_date(), list_details.get(0).getAcknowledged_time(), list_details.get(0).getImage_path(), list_details.get(0).getImage_bitmap(),
                                                        list_details.get(0).getTask_process(), list_details.get(0).getDisposal_site_name(), list_details.get(0).getDisposal_site_id(), list_details.get(0).getDisposal_date(), list_details.get(0).getDisposal_time(), list_details.get(0).getJob_type(), list_details.get(0).getSecond_man(), list_details.get(0).getJob_started(), list_details.get(0).getSing_off_status(), list_details.get(0).getJob_start_datetime(),
                                                        task_sync_status, ss1, ss2, ss2_A, ss3, final_sync, list_details.get(0).getNew_rental(), list_details.get(0).getFinal_removal(), list_details.get(0).getInstructions()
                                                ));

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

//                                            ArrayList<TaskListCartModel> ticket_list = new ArrayList<TaskListCartModel>();
//                                            Main_impl main_impl = new Main_impl(TaskListActivity.this);
//                                            ticket_list = main_impl.getUser();
//
//                                            for (int j = 0; j < ticket_list.size(); j++) {
//                                                if (ticket_list.get(j).getTask_id().equalsIgnoreCase(list_details.get(0).getTask_id())) {
//                                                    Main_impl main_impl2 = new Main_impl(TaskListActivity.this);
//                                                    ticket_list = main_impl.getUser();
//
//                                                    long det = main_impl2.update(new TaskListCartModel(ticket_list.get(j).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), ticket_list.get(j).getTask_id(), ticket_list.get(j).getTask_number(), ticket_list.get(j).getClient_name(), ticket_list.get(j).getAddress(),
//                                                            ticket_list.get(j).getShort_description(), ticket_list.get(j).getJob_type(), "In-progress"));
//
//                                                }
//                                            }
                                            CallApiSequence();
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_LONG).show();
                                        }
                                    }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put(WebService.KEY_TASK, WebService.KEY_UPDATETASK_OFFLINE);
                                    params.put(WebService.KEY_TASKDETAIL_TASKIDDKEY, list_details.get(i).getTask_id());
                                    params.put("second_man", list_details.get(i).getSecond_man());
                                    params.put(WebService.KEY_UPDATETASKPROCESS, "In-progress");
                                    params.put(WebService.KEY_SIGNOFFDATETIME, list_details.get(i).getAcknowledged_date() + " " + list_details.get(i).getAcknowledged_time());
                                    params.put("task_sync_status", list_details.get(i).getTask_sync_status());
                                    params.put("ss1", list_details.get(i).getSs1());
                                    params.put("ss2", list_details.get(i).getSs2());
                                    params.put("ss2_A", list_details.get(i).getSs2_A());
                                    params.put("ss3", list_details.get(i).getSs3());
                                    params.put("final_sync", list_details.get(i).getFinal_sync());
                                    params.put(WebService.KEY_DISPOSAL_NAME, "");
                                    params.put(WebService.KEY_DISPOSAL_ID, "");
                                    params.put(WebService.KEY_TIPPINGBOOKDATETIME, list_details.get(i).getAcknowledged_date() + " " + list_details.get(i).getAcknowledged_time());


                                    return params;
                                }
                            };

                            mStringRequest2.setRetryPolicy(new DefaultRetryPolicy(
                                    50000,
                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                            mRequestQueue2.add(mStringRequest2);


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(WebService.KEY_TASK, WebService.KEY_SIGN_OFFTASK_OFFLINE);
                    params.put(WebService.KEY_UPDATETASKID, list_details.get(i).getTask_id());
                    params.put(WebService.KEY_SIGN_OFF_STATUS, list_details.get(i).getSing_off_status());
                    params.put("second_man", list_details.get(i).getSecond_man());

                    return params;
                }
            };

            mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    50000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            mRequestQueue.add(mStringRequest);
//
        } else if (list_details.get(i).getSs3().equalsIgnoreCase("1")) {
            mRequestQueue = Volley.newRequestQueue(getBaseContext());

            StringRequest mStringRequest = new StringRequest(Request.Method.POST, WebService.SERVERURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("mainactivity_resonse", response);
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(response);

                                String status = jsonObject.getString("status");
                                String message = jsonObject.getString("message");
                                String task_sync_status = jsonObject.getString("task_sync_status");
                                String ss1 = jsonObject.getString("ss1");
                                String ss2 = jsonObject.getString("ss2");
                                String ss2_A = jsonObject.getString("ss2_A");
                                String ss3 = jsonObject.getString("ss3");
                                String final_sync = jsonObject.getString("final_sync");

                                SharedPrefrenceClass sharedPrefrenceClass = new SharedPrefrenceClass(getBaseContext());
                                details_impl = new Details_impl(getBaseContext());
                                list_details = details_impl.getUserDetails1(list_details.get(i).getTask_id());
                                long det = details_impl.update(new TaskDetailCartModel(list_details.get(0).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list_details.get(0).getTask_id()), list_details.get(0).getTask_number(), list_details.get(0).getClient_name(), list_details.get(0).getDate(), list_details.get(0).getTime(), list_details.get(0).getAddress(),
                                        list_details.get(0).getLatitude(), list_details.get(0).getLongitude(), list_details.get(0).getShort_description(), list_details.get(0).getDescription(), list_details.get(0).getAcknowledged_by(), list_details.get(0).getAcknowledged_date(), list_details.get(0).getAcknowledged_time(), list_details.get(0).getImage_path(), list_details.get(0).getImage_bitmap(),
                                        list_details.get(0).getTask_process(), list_details.get(0).getDisposal_site_name(), list_details.get(0).getDisposal_site_id(), list_details.get(0).getDisposal_date(), list_details.get(0).getDisposal_time(), list_details.get(0).getJob_type(), list_details.get(0).getSecond_man(), list_details.get(0).getJob_started(), list_details.get(0).getSing_off_status(), list_details.get(0).getJob_start_datetime(),
                                        task_sync_status, ss1, ss2, ss2_A, ss3, final_sync, list_details.get(0).getNew_rental(), list_details.get(0).getFinal_removal(), list_details.get(0).getInstructions()
                                ));
//                                    globalVariable.SetTaskFinished("true");
                                CallApiSequence();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(WebService.KEY_TASK, "update_status_combined_offline");
                    params.put(WebService.KEY_UPDATETASKID, list_details.get(i).getTask_id());
                    params.put(WebService.KEY_UPDATETASKPROCESS, "Completed");
                    params.put(WebService.KEY_SIGNOFFDATETIME, list_details.get(i).getDisposal_date() + " " + list_details.get(i).getDisposal_time());
                    params.put("task_sync_status", list_details.get(i).getTask_sync_status());
                    params.put("ss1", list_details.get(i).getSs1());
                    params.put("ss2", list_details.get(i).getSs2());
                    params.put("ss2_A", list_details.get(i).getSs2_A());
                    params.put("ss3", list_details.get(i).getSs3());
                    params.put("final_sync", list_details.get(i).getFinal_sync());
                    params.put(WebService.KEY_DISPOSAL_NAME, list_details.get(i).getDisposal_site_name());
                    params.put(WebService.KEY_DISPOSAL_ID, list_details.get(i).getDisposal_site_id());
                    params.put(WebService.KEY_TIPPINGBOOKDATETIME, list_details.get(i).getDisposal_date() + " " + list_details.get(i).getDisposal_time());


                    return params;
                }
            };

            mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    50000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            mRequestQueue.add(mStringRequest);

//

        } else {
            f_pos++;
            CallApiSequence();
        }


    }

    public class SubmitRoadSideAssistDataAsyTask extends AsyncTask<Void, Void, Void> {


        private String str_responsefromserver;
        int ii = 0;

        public SubmitRoadSideAssistDataAsyTask(int i) {
            ii = i;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            //InputStream is = null;
            //StringBuffer result = null;
            try {


                HttpClient httpClient = new DefaultHttpClient();
                HttpPost postRequest = new HttpPost(WebService.SERVERURL);


                reqEntity = new MultipartEntity(
                        HttpMultipartMode.BROWSER_COMPATIBLE);


                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                // byte[] data = bos.toByteArray();
                details_impl = new Details_impl(getBaseContext());
                list_details = details_impl.getUser();

                byte[] decodedBytes = Base64.decode(list_details.get(ii).getImage_bitmap(), 0);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);

                ContentBody contentPart = new ByteArrayBody(bos.toByteArray(), "image/jpeg", "files" + 1 + ".jpg");
                reqEntity.addPart("attachment", contentPart);


                reqEntity.addPart(WebService.KEY_TASK, new StringBody(WebService.KEY_SIGNOFFTASK_OFFLINE));
                reqEntity.addPart(WebService.KEY_SIGNOFFDATETIME, new StringBody(list_details.get(ii).getAcknowledged_date() + " " + list_details.get(ii).getAcknowledged_time()));
                reqEntity.addPart(WebService.KEY_SIGNOFFACOWLEDGEDBY, new StringBody(list_details.get(ii).getAcknowledged_by()));
                reqEntity.addPart(WebService.KEY_SIGNOFFTASKID, new StringBody(list_details.get(ii).getTask_id()));
                reqEntity.addPart("second_man", new StringBody(list_details.get(ii).getSecond_man()));

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                reqEntity.writeTo(bytes);

                postRequest.setEntity(reqEntity);
                HttpResponse response = httpClient.execute(postRequest);
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        response.getEntity().getContent(), "UTF-8"));
                String sResponse;
                StringBuilder s = new StringBuilder();

                while ((sResponse = reader.readLine()) != null) {
                    s = s.append(sResponse);
                }
                str_responsefromserver = s.toString();
                //struservalidateserverres = s.toString();

            } catch (Exception e) {
                e.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //Response: {"data":{"message":"Mail sent successfully"}}

            try {

                //09-21 18:49:22.643: I/TaskDetailActivity(2161): Response: {"status":"1","message":"Saved successfully"}
                JSONObject jsonObject = new JSONObject(str_responsefromserver);
                //String status = jsonObject.optString(WebService.KEY_LOGIN_STATUS);
                String message = jsonObject.optString(WebService.KEY_LOGIN_MESSAGE);
                if (message.toString().length() > 0) {
                    Toast.makeText(getBaseContext(), message, 1).show();
                }
                details_impl = new Details_impl(getBaseContext());
                list_details = details_impl.getUser();

                //Picasso.with(context).load(str_signature).fit().into(image_signoffshow);


                mRequestQueue = Volley.newRequestQueue(getBaseContext());

                StringRequest mStringRequest = new StringRequest(Request.Method.POST, WebService.SERVERURL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("mainactivity_resonse", response);
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response);

                                    String status = jsonObject.getString("status");
                                    String message = jsonObject.getString("message");
                                    String task_sync_status = jsonObject.getString("task_sync_status");
                                    String ss1 = jsonObject.getString("ss1");
                                    String ss2 = jsonObject.getString("ss2");
                                    String ss2_A = jsonObject.getString("ss2_A");
                                    String ss3 = jsonObject.getString("ss3");
                                    String final_sync = jsonObject.getString("final_sync");
                                    SharedPrefrenceClass sharedPrefrenceClass = new SharedPrefrenceClass(getBaseContext());

                                    details_impl = new Details_impl(getBaseContext());
                                    list_details = details_impl.getUserDetails1(list_details.get(ii).getTask_id());
                                    long det = details_impl.update(new TaskDetailCartModel(list_details.get(0).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list_details.get(0).getTask_id()), list_details.get(0).getTask_number(), list_details.get(0).getClient_name(), list_details.get(0).getDate(), list_details.get(0).getTime(), list_details.get(0).getAddress(),
                                            list_details.get(0).getLatitude(), list_details.get(0).getLongitude(), list_details.get(0).getShort_description(), list_details.get(0).getDescription(), list_details.get(0).getAcknowledged_by(), list_details.get(0).getAcknowledged_date(), list_details.get(0).getAcknowledged_time(), list_details.get(0).getImage_path(), list_details.get(0).getImage_bitmap(),
                                            list_details.get(0).getTask_process(), list_details.get(0).getDisposal_site_name(), list_details.get(0).getDisposal_site_id(), list_details.get(0).getDisposal_date(), list_details.get(0).getDisposal_time(), list_details.get(0).getJob_type(), list_details.get(0).getSecond_man(), list_details.get(0).getJob_started(), list_details.get(0).getSing_off_status(), list_details.get(0).getJob_start_datetime(),
                                            task_sync_status, ss1, ss2, ss2_A, ss3, final_sync, list_details.get(0).getNew_rental(), list_details.get(0).getFinal_removal(), list_details.get(0).getInstructions()
                                    ));
                                    CallApiSequence();


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(WebService.KEY_TASK, WebService.KEY_UPDATETASK_OFFLINE);
                        params.put(WebService.KEY_UPDATETASKID, list_details.get(ii).getTask_id());
                        params.put("second_man", list_details.get(ii).getSecond_man());
                        params.put(WebService.KEY_UPDATETASKPROCESS, "In-progress");
                        params.put(WebService.KEY_TIPPINGBOOKDATETIME, list_details.get(ii).getAcknowledged_date() + " " + list_details.get(ii).getAcknowledged_time());
                        params.put(WebService.KEY_SIGNOFFDATETIME, list_details.get(ii).getAcknowledged_date() + " " + list_details.get(ii).getAcknowledged_time());
                        params.put("task_sync_status", list_details.get(ii).getTask_sync_status());
                        params.put("ss1", list_details.get(ii).getSs1());
                        params.put("ss2", list_details.get(ii).getSs2());
                        params.put("ss2_A", list_details.get(ii).getSs2_A());
                        params.put("ss3", list_details.get(ii).getSs3());
                        params.put("final_sync", list_details.get(ii).getFinal_sync());


                        return params;
                    }
                };


                mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        50000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                mRequestQueue.add(mStringRequest);


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
}
