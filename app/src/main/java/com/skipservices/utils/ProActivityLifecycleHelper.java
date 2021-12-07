package com.skipservices.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.skipservices.Impl.Details_impl;
import com.skipservices.Model.TaskDetailCartModel;
import com.skipservices.parsers.SharedPrefrenceClass;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProActivityLifecycleHelper {
    static Context context2;
    String encodedImage;
    private static String mResult = "";
    String imagestring2, UserId;
    String namess, app_names, package_names;
    private int numberActivitiesStart = 0;
    private boolean isActivityChangingConfigurations = false;
    Details_impl details_impl;
    private ArrayList<TaskDetailCartModel> list_details = new ArrayList<TaskDetailCartModel>();
    private RequestQueue mRequestQueue;
    int f_pos = 0;
    MultipartEntity reqEntity;
    ConnectionDetector connectionDetector;
    MyPersonalApp globalVariable;

    public interface ActivityCallbacks {
        void OnSaved(Bundle savedInstance);
    }

    private final Application application;

    private ProActivityLifecycleHelper(Application application) {
        this.application = application;
    }

    public static ProActivityLifecycleHelper wrap(Context context) {
        context2 = context;
        return new ProActivityLifecycleHelper(assertApplication(context));
    }

    public Context with(final ActivityCallbacks callbacks) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity a, Bundle b) {
                Log.d("cchchch", "");
            }

            @Override
            public void onActivityStarted(final Activity a) {
//                ++activityReferences;
                globalVariable = (MyPersonalApp) context2;
                globalVariable.SetComplete("N");
                String namess = a.getClass().getSimpleName();
                if (numberActivitiesStart == 0) {
                    // The application come from background to foreground
                    connectionDetector = new ConnectionDetector(context2);
                    if (connectionDetector.isConnectingToInternet()) {
                        f_pos=0;
                        CallApiSequence();
//                        Toast.makeText(a, "Foreground", Toast.LENGTH_SHORT).show();

                    }
                }
                numberActivitiesStart++;

            }

            @Override
            public void onActivityResumed(Activity a) {
                callbacks.OnSaved(null);
            }

            @Override
            public void onActivityPaused(Activity a) {
//                takeScreenshot(a);
                namess = a.getClass().getSimpleName();

            }

            @Override
            public void onActivityStopped(Activity a) {
//                --activityReferences;
                String namess = a.getClass().getSimpleName();
                Log.d("ccheck", namess);
                numberActivitiesStart--;
                if (numberActivitiesStart == 0) {
                    // The application go from foreground to background
//                    Toast.makeText(a, "In Background", Toast.LENGTH_SHORT).show();
                    connectionDetector = new ConnectionDetector(context2);
                    if (connectionDetector.isConnectingToInternet()) {
                        f_pos=0;
                        CallApiSequence();
                    }
                }

            }

            @Override
            public void onActivitySaveInstanceState(Activity a, Bundle b) {
                callbacks.OnSaved(b);
            }

            @Override
            public void onActivityDestroyed(Activity a) {

//                application.unregisterActivityLifecycleCallbacks(this);
            }
        });
        return application.getBaseContext();
    }



    private static Application assertApplication(Context context) {
        final Context application = context.getApplicationContext();
        if (application instanceof Application) return (Application) application;
        throw new NullPointerException("Context must be instance of Application");
    }
    private void CallApiSequence() {
        details_impl = new Details_impl(context2);
        list_details = details_impl.getUser();

        if (list_details.size() > f_pos) {
            CallApiSequence2(f_pos);
        } else {
            globalVariable = (MyPersonalApp) context2;
            globalVariable.SetComplete("Y");
        }

    }

    private void CallApiSequence2(final int i) {
        details_impl = new Details_impl(context2);
        list_details = details_impl.getUser();


        if (list_details.get(i).getSs1().equalsIgnoreCase("1")) {
            mRequestQueue = Volley.newRequestQueue(context2);

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

                                details_impl = new Details_impl(context2);
                                SharedPrefrenceClass sharedPrefrenceClass = new SharedPrefrenceClass(context2);
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
                            Toast.makeText(context2, error.toString(), Toast.LENGTH_LONG).show();
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
            mRequestQueue = Volley.newRequestQueue(context2);
            StringRequest mStringRequest = new StringRequest(Request.Method.POST, WebService.SERVERURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("mainactivity_resonse", response);
                            RequestQueue mRequestQueue2 = Volley.newRequestQueue(context2);

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

                                                details_impl = new Details_impl(context2);
                                                SharedPrefrenceClass sharedPrefrenceClass = new SharedPrefrenceClass(context2);
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
                                            Toast.makeText(context2, error.toString(), Toast.LENGTH_LONG).show();
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
                            Toast.makeText(context2, error.toString(), Toast.LENGTH_LONG).show();
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
            mRequestQueue = Volley.newRequestQueue(context2);

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

                                SharedPrefrenceClass sharedPrefrenceClass = new SharedPrefrenceClass(context2);
                                details_impl = new Details_impl(context2);
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
                            Toast.makeText(context2, error.toString(), Toast.LENGTH_LONG).show();
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
                details_impl = new Details_impl(context2);
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
                    Toast.makeText(context2, message, 1).show();
                }
                details_impl = new Details_impl(context2);
                list_details = details_impl.getUser();

                //Picasso.with(context).load(str_signature).fit().into(image_signoffshow);


                mRequestQueue = Volley.newRequestQueue(context2);

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
                                    SharedPrefrenceClass sharedPrefrenceClass= new SharedPrefrenceClass(context2);

                                    details_impl = new Details_impl(context2);
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
                                Toast.makeText(context2, error.toString(), Toast.LENGTH_LONG).show();
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
