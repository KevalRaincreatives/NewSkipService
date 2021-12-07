package com.skipservices;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.skipservices.App.Config;
import com.skipservices.Impl.Details_impl;
import com.skipservices.Impl.Main_impl;
import com.skipservices.Impl.SecondMan_impl;
import com.skipservices.Model.JobTypeModel;
import com.skipservices.Model.SecondMan2Model;
import com.skipservices.Model.TaskDetailCartModel;
import com.skipservices.Model.TaskListCartModel;
import com.skipservices.parsers.DisposalModel;
import com.skipservices.parsers.LoginParser;
import com.skipservices.parsers.SecondManModel;
import com.skipservices.parsers.SharedPrefrenceClass;
import com.skipservices.parsers.TaskDetailParser;
import com.skipservices.parsers.TaskListModel;
import com.skipservices.parsers.TaskListParser;
import com.skipservices.thread.AsyncTaskClass;
import com.skipservices.thread.AsyncTaskCompleteListener;
import com.skipservices.utils.ConnectionDetector;
import com.skipservices.utils.InternalStorage;
import com.skipservices.utils.MyPersonalApp;
import com.skipservices.utils.RotateAnimation;
import com.skipservices.utils.WebService;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TaskListActivity extends AppCompatActivity implements AsyncTaskCompleteListener<String> {

    public String DEBUG_TAG = "TaskListActivity";
    Context context;
    ListView listView;
    TextView txt_username, txt_date;
    List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
    EditText editText_search;
    ImageView btn_edtsearch;
    ConnectionDetector connectionDetector;
    AsyncTaskClass asyncTaskClass;
    SharedPrefrenceClass sharedPrefrenceClass;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
    TaskAdpater taskAdpater;
    ImageView img_refresh;
    ArrayList<TaskListModel> arrLike = new ArrayList<>();
    ArrayList<DisposalModel> arrDisposal = new ArrayList<>();
    ProgressDialog progressDoalog;
    String key = "";
    Button btn_show, btn_logout;
    ArrayList<String> SampleArrayList = new ArrayList<String>();
    String allIds = "";
    DisposalAdapter2 disposalAdapter2;
    TextView tx_disposal, txt_disposalsiteid;
    int regionidex = 0;
    String namedisposal = "";
    String strsignoffdate = "";
    String strsignofftime = "";
    TaskDetailParser taskDetailParser;
    private ArrayList<TaskListCartModel> list = new ArrayList<TaskListCartModel>();
    Main_impl impl;
    private ArrayList<TaskDetailCartModel> list_details = new ArrayList<TaskDetailCartModel>();
    Details_impl details_impl;
    MultipartEntity reqEntity;
    int position_status = 0;
    MyPersonalApp globalVariable;
    private RequestQueue mRequestQueue;
    int f_pos = 0;
    InternalStorage internalStorage;
    ArrayList<SecondMan2Model> arrSecond2 = new ArrayList<>();
    SecondMan_impl secondMan_impl;
    ArrayList<SecondManModel> arrSecond = new ArrayList<>();
    List<String> ids_service = new ArrayList<String>();
    String allAmeIds = "";
    String logout_status = "";
    RotateAnimation rotateAnimation;
    ArrayList<JobTypeModel> arrjobStatus = new ArrayList<>();
    Spinner spn_location;
    JobTypeAdapter jobTypeAdapter;
    private boolean userIsInteracting;
    int job_status_type = 0;
    StringBuilder log=new StringBuilder();
    String line;

    private List<String> permissionsNeeded;
    private List<String> permissionsNeededRationale;
    private List<String> permissionsList;

    String[] permissions = new String[]{
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    String[] permissionsTitle = new String[]{
            "Storage",
            "Memory",
    };
    private static final int Premission_Grant_requestCode = 20;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasklist);
        context = TaskListActivity.this;
        globalVariable = (MyPersonalApp) getApplicationContext();
        rotateAnimation = new RotateAnimation(context);

        listView = (ListView) findViewById(R.id.listitems);
        txt_username = (TextView) findViewById(R.id.txt_username);
        txt_date = (TextView) findViewById(R.id.txt_date);
        editText_search = (EditText) findViewById(R.id.edt_tasksearch);
        btn_edtsearch = findViewById(R.id.btn_edtsearch);
        img_refresh = findViewById(R.id.img_refresh);
        btn_show = findViewById(R.id.btn_show);
        btn_logout = findViewById(R.id.btn_logout);
        spn_location = findViewById(R.id.spn_location);
        internalStorage = InternalStorage.getInstance();
        sharedPrefrenceClass = new SharedPrefrenceClass(context);
        connectionDetector = new ConnectionDetector(context);
        secondMan_impl = new SecondMan_impl(this);

        txt_username.setText(sharedPrefrenceClass.getKEY_LOGIN_USERNAME());

        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            log=new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                log.append(line);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                permissionsNeeded = new ArrayList<>();
                permissionsNeededRationale = new ArrayList<>();
                permissionsList = new ArrayList<>();

                boolean isGranted = multiplePermissions(permissions);
                if (isGranted) {
//                    File file = new File(Environment.getExternalStorageDirectory().getPath(), "Skip");
//                    if (!file.exists()) {
//                        Toast.makeText(TaskListActivity.this, "error.toString()", Toast.LENGTH_LONG).show();
//
//                        file.mkdirs();
//                    }
                    File filename = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/Skip");

                    if (!filename.exists())
                    {
                        // Make it, if it doesn't exit
                        boolean success = filename.mkdirs();
                        if (!success)
                        {
                            filename = null;
                        }
                    }

//                    filename.createNewFile();
                    String cmd = "logcat -d -f" + filename.getAbsolutePath()+"/skip.log";
                    Runtime.getRuntime().exec(cmd);
                }
            }else{
                File file = new File(Environment.getExternalStorageDirectory().getPath(), "Skip");
                if (!file.exists()) {
                    file.mkdirs();
                }
                File filename = new File(file.getAbsolutePath() + "/skip.log");
                filename.createNewFile();
                String cmd = "logcat -d -f" + filename.getAbsolutePath();
                Runtime.getRuntime().exec(cmd);
            }

        } catch (IOException e) {
        }


        String datetime = "";
        SimpleDateFormat myFormat = new SimpleDateFormat("EEEE', 'MMMM dd', ' yyyy");//Y-m-d H:i:s  2016-09-19 15:09:00
        try {
            Date date = new Date();
            datetime = myFormat.format(date);
            Log.i(DEBUG_TAG, "Current Date Time in give format: " + datetime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        arrjobStatus.add(new JobTypeModel("ALL"));
        arrjobStatus.add(new JobTypeModel("Pending"));
        arrjobStatus.add(new JobTypeModel("In-progress"));

        jobTypeAdapter = new JobTypeAdapter(context, arrjobStatus);
        spn_location.setAdapter(jobTypeAdapter);


        txt_date.setText(datetime);

        img_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDoalog = new ProgressDialog(TaskListActivity.this);
                progressDoalog.setMessage("Updating Joblist. Please wait...!");
                progressDoalog.setCancelable(false);
                // show it
                progressDoalog.show();
                mRequestQueue = Volley.newRequestQueue(TaskListActivity.this);

                StringRequest mStringRequest = new StringRequest(Request.Method.POST, WebService.SERVERURL_PULL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("refresh_resonse", response);
                                if (!response.equalsIgnoreCase("")) {
                                    progressDoalog.dismiss();
                                } else {
                                    progressDoalog.dismiss();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(TaskListActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(WebService.KEY_TASK, WebService.KEY_PULL_TASK2);

                        return params;
                    }
                };

                mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        50000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                mRequestQueue.add(mStringRequest);
//                key = "pull";
//                progressDoalog = new ProgressDialog(TaskListActivity.this);
//                progressDoalog.setMessage("Updating Joblist. Please wait...!");
//                progressDoalog.setCancelable(false);
//                // show it
//                progressDoalog.show();
//                if (nameValuePair.size() > 0) {
//                    nameValuePair.clear();
//                }
//
//                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, WebService.KEY_PULL_TASK2));
//
//                if (connectionDetector.isConnectingToInternet()) {
//                    asyncTaskClass = new AsyncTaskClass(context, TaskListActivity.this, nameValuePair, WebService.SERVERURL_PULL, false);
//                    asyncTaskClass.execute();
//                } else {
//                    Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
//                    return;
//                }
            }
        });

        details_impl = new Details_impl(TaskListActivity.this);
        list_details = details_impl.getUser();
        Log.d("sampledistance", "ss");

        if (connectionDetector.isConnectingToInternet()) {

            if (globalVariable.GetComplete().equalsIgnoreCase("Y")) {
                rotateAnimation = new RotateAnimation(context);
                arrLike.clear();
                rotateAnimation.show();
                RequestQueue mRequestQueue2 = Volley.newRequestQueue(TaskListActivity.this);

                StringRequest mStringRequest2 = new StringRequest(Request.Method.POST, WebService.SERVERURL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {


                                Log.d("mainactivity_resonse", response);
                                try {
                                    LoginParser loginParser = new LoginParser(context);
                                    loginParser.JsonStringToArrayList(response);

                                    String status = loginParser.Getstatus();

                                    if (status.equalsIgnoreCase("y")) {

                                        mRequestQueue = Volley.newRequestQueue(TaskListActivity.this);
                                        StringRequest mStringRequest = new StringRequest(Request.Method.POST, WebService.SERVERURL,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        if (rotateAnimation != null) {
                                                            if (rotateAnimation.isShowing()) {
                                                                rotateAnimation.dismiss();
                                                                rotateAnimation = null;
                                                            }
                                                        }
                                                        Log.d("logout_resonse", response);
                                                        try {
                                                            details_impl = new Details_impl(TaskListActivity.this);
                                                            details_impl.delete_table();
                                                            list_details.clear();

                                                            JSONArray jsonArray = new JSONArray(response);
                                                            Gson gson2 = new Gson();
                                                            Log.i(DEBUG_TAG, "jsonarraylengeth=" + jsonArray.length());
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
                                                                    String instructions=arrayElement_0.getString("instructions");

                                                                    details_impl = new Details_impl(TaskListActivity.this);
                                                                    long det = details_impl.insert(new TaskDetailCartModel(0, sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(task_id), task_number, client_name, date, time, address,
                                                                            latitude, longitude, short_description, description, acknowledged_by, acknowledged_date, acknowledged_time, image_path, "",
                                                                            task_process, disposal_site_name, disposal_site_id, disposal_date, disposal_time, job_type, second_man, job_started, sing_off_status
                                                                            , job_start_datetime, task_sync_status, ss1, ss2, ss2_A, ss3, final_sync, new_rental, final_removal,instructions
                                                                    ));
                                                                }
                                                            }
                                                            arrDisposal.clear();
                                                            for (int i = 0; i < arrLike.get(0).getDisposal_site().size(); i++) {
                                                                arrDisposal.add(new DisposalModel(arrLike.get(0).getDisposal_site().get(i).getId(), arrLike.get(0).getDisposal_site().get(i).getName()));
                                                                try {
                                                                    internalStorage.writeObject(TaskListActivity.this, "EmenitiesList", arrDisposal);
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }


                                                            list_details = details_impl.getUser();

                                                            if (list_details.size() < 1) {
                                                                Toast.makeText(context, "No Data", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                ArrayList<TaskDetailCartModel> list2 = new ArrayList<TaskDetailCartModel>();
                                                                list2.clear();
                                                                for (int i = 0; i < list_details.size(); i++) {
                                                                    if (job_status_type == 0) {
                                                                        if (list_details.get(i).getTask_process().equalsIgnoreCase("Completed")) {

                                                                        } else {
                                                                            list2.add(new TaskDetailCartModel(list_details.get(i).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list_details.get(i).getTask_id()), list_details.get(i).getTask_number(), list_details.get(i).getClient_name(), list_details.get(i).getDate(), list_details.get(i).getTime(), list_details.get(i).getAddress(),
                                                                                    list_details.get(i).getLatitude(), list_details.get(i).getLongitude(), list_details.get(i).getShort_description(), list_details.get(i).getDescription(), list_details.get(i).getAcknowledged_by(), list_details.get(i).getAcknowledged_date(), list_details.get(i).getAcknowledged_time(), list_details.get(i).getImage_path(), list_details.get(i).getImage_bitmap(),
                                                                                    list_details.get(i).getTask_process(), list_details.get(i).getDisposal_site_name(), list_details.get(i).getDisposal_site_id(), list_details.get(i).getDisposal_date(), list_details.get(i).getDisposal_time(), list_details.get(i).getJob_type(), list_details.get(i).getSecond_man(), list_details.get(i).getJob_started(), list_details.get(i).getSing_off_status(), list_details.get(i).getJob_start_datetime(),
                                                                                    list_details.get(i).getTask_sync_status(), list_details.get(i).getSs1(), list_details.get(i).getSs2(), list_details.get(i).getSs2_A(), list_details.get(i).getSs3(), list_details.get(i).getFinal_sync(), list_details.get(i).getNew_rental(), list_details.get(i).getFinal_removal(),list_details.get(i).getInstructions()
                                                                            ));
                                                                        }
                                                                    } else if (job_status_type == 1) {
                                                                        if (list_details.get(i).getTask_process().equalsIgnoreCase("Completed") || list_details.get(i).getTask_process().equalsIgnoreCase("In-progress")) {

                                                                        } else {
                                                                            list2.add(new TaskDetailCartModel(list_details.get(i).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list_details.get(i).getTask_id()), list_details.get(i).getTask_number(), list_details.get(i).getClient_name(), list_details.get(i).getDate(), list_details.get(i).getTime(), list_details.get(i).getAddress(),
                                                                                    list_details.get(i).getLatitude(), list_details.get(i).getLongitude(), list_details.get(i).getShort_description(), list_details.get(i).getDescription(), list_details.get(i).getAcknowledged_by(), list_details.get(i).getAcknowledged_date(), list_details.get(i).getAcknowledged_time(), list_details.get(i).getImage_path(), list_details.get(i).getImage_bitmap(),
                                                                                    list_details.get(i).getTask_process(), list_details.get(i).getDisposal_site_name(), list_details.get(i).getDisposal_site_id(), list_details.get(i).getDisposal_date(), list_details.get(i).getDisposal_time(), list_details.get(i).getJob_type(), list_details.get(i).getSecond_man(), list_details.get(i).getJob_started(), list_details.get(i).getSing_off_status(), list_details.get(i).getJob_start_datetime(),
                                                                                    list_details.get(i).getTask_sync_status(), list_details.get(i).getSs1(), list_details.get(i).getSs2(), list_details.get(i).getSs2_A(), list_details.get(i).getSs3(), list_details.get(i).getFinal_sync(), list_details.get(i).getNew_rental(), list_details.get(i).getFinal_removal(),list_details.get(i).getInstructions()
                                                                            ));
                                                                        }
                                                                    } else if (job_status_type == 2) {
                                                                        if (list_details.get(i).getTask_process().equalsIgnoreCase("Completed") || list_details.get(i).getTask_process().equalsIgnoreCase("Pending")) {

                                                                        } else {
                                                                            list2.add(new TaskDetailCartModel(list_details.get(i).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list_details.get(i).getTask_id()), list_details.get(i).getTask_number(), list_details.get(i).getClient_name(), list_details.get(i).getDate(), list_details.get(i).getTime(), list_details.get(i).getAddress(),
                                                                                    list_details.get(i).getLatitude(), list_details.get(i).getLongitude(), list_details.get(i).getShort_description(), list_details.get(i).getDescription(), list_details.get(i).getAcknowledged_by(), list_details.get(i).getAcknowledged_date(), list_details.get(i).getAcknowledged_time(), list_details.get(i).getImage_path(), list_details.get(i).getImage_bitmap(),
                                                                                    list_details.get(i).getTask_process(), list_details.get(i).getDisposal_site_name(), list_details.get(i).getDisposal_site_id(), list_details.get(i).getDisposal_date(), list_details.get(i).getDisposal_time(), list_details.get(i).getJob_type(), list_details.get(i).getSecond_man(), list_details.get(i).getJob_started(), list_details.get(i).getSing_off_status(), list_details.get(i).getJob_start_datetime(),
                                                                                    list_details.get(i).getTask_sync_status(), list_details.get(i).getSs1(), list_details.get(i).getSs2(), list_details.get(i).getSs2_A(), list_details.get(i).getSs3(), list_details.get(i).getFinal_sync(), list_details.get(i).getNew_rental(), list_details.get(i).getFinal_removal(),list_details.get(i).getInstructions()
                                                                            ));
                                                                        }
                                                                    }
                                                                }

                                                                taskAdpater = new TaskAdpater(TaskListActivity.this, list2);
                                                                listView.setAdapter(taskAdpater);
                                                                taskAdpater.notifyDataSetChanged();


                                                                Comparator<TaskDetailCartModel> comp = new Comparator<TaskDetailCartModel>() {
                                                                    @Override
                                                                    public int compare(TaskDetailCartModel c1, TaskDetailCartModel c2) {
                                                                        return c2.getTask_process().compareTo(c1.getTask_process());
                                                                    }
                                                                };

                                                                Collections.sort(list2, comp);
                                                            }

//                                taskAdpater = new TaskAdpater(TaskListActivity.this, list_details);
//                                listView.setAdapter(taskAdpater);

                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Toast.makeText(TaskListActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                                                        if (rotateAnimation != null) {
                                                            if (rotateAnimation.isShowing()) {
                                                                rotateAnimation.dismiss();
                                                                rotateAnimation = null;
                                                            }
                                                        }
                                                    }
                                                }) {
                                            @Override
                                            protected Map<String, String> getParams() {
                                                Map<String, String> params = new HashMap<String, String>();
                                                String datetime = "";
                                                SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//Y-m-d H:i:s  2016-09-19 15:09:00
                                                try {
                                                    Date date = new Date();
                                                    datetime = myFormat.format(date);
                                                    Log.i(DEBUG_TAG, "Current Date Time in give format: " + datetime);
                                                } catch (ParseException e) {
                                                    // TODO Auto-generated catch block
                                                    e.printStackTrace();
                                                }
                                                params.put(WebService.KEY_TASK, WebService.KEY_TASKLIST_TASK_OFFLINE);
                                                params.put(WebService.KEY_TASKLIST_DRIVERIDKEY, sharedPrefrenceClass.getKEY_LOGIN_USERID());
                                                params.put(WebService.KEY_TASKLIST_DATEKEY, datetime);

                                                return params;
                                            }
                                        };

                                        mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                                50000,
                                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                                        mRequestQueue.add(mStringRequest);
                                    } else {
                                        Toast.makeText(context, loginParser.Getmessage(), 1).show();
                                        if (rotateAnimation != null) {
                                            if (rotateAnimation.isShowing()) {
                                                rotateAnimation.dismiss();
                                                rotateAnimation = null;
                                            }
                                        }
                                        finish();
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    if (rotateAnimation != null) {
                                        if (rotateAnimation.isShowing()) {
                                            rotateAnimation.dismiss();
                                            rotateAnimation = null;
                                        }
                                    }
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
//												rotateAnimation.dismiss();
                                Toast.makeText(TaskListActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                                if (rotateAnimation != null) {
                                    if (rotateAnimation.isShowing()) {
                                        rotateAnimation.dismiss();
                                        rotateAnimation = null;
                                    }
                                }
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(WebService.KEY_TASK, WebService.KEY_LOGIN_TASK_OFFLINE);
                        params.put(WebService.KEY_LOGIN_USERNAMEKEY, sharedPrefrenceClass.getKEY_LOGIN_USERNAME());
                        params.put(WebService.KEY_LOGIN_PASSWORDKEY, sharedPrefrenceClass.getKEY_LOGIN_PASSWORD());
                        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                        if (pref.getString("regId", null) != null && !pref.getString("regId", null).isEmpty() && !pref.getString("regId", null).equals("null")) {
                            params.put("devicetoken", pref.getString("regId", null));
                        } else {
                            params.put("devicetoken", "");
                        }


                        return params;
                    }
                };

                mStringRequest2.setRetryPolicy(new DefaultRetryPolicy(
                        50000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                mRequestQueue2.add(mStringRequest2);

//            if (nameValuePair.size() > 0) {
//                nameValuePair.clear();
//            }
//            key = "List";
//            String datetime2 = "";
//            SimpleDateFormat myFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//Y-m-d H:i:s  2016-09-19 15:09:00
//            try {
//                Date date = new Date();
//                datetime2 = myFormat2.format(date);
//                Log.i(DEBUG_TAG, "Current Date Time in give format: " + datetime2);
//            } catch (ParseException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//            nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, WebService.KEY_TASKLIST_TASK_OFFLINE));
//            nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASKLIST_DRIVERIDKEY, sharedPrefrenceClass.getKEY_LOGIN_USERID()));
//            nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASKLIST_DATEKEY, datetime2));
//            arrLike.clear();
//            if (connectionDetector.isConnectingToInternet()) {
//
//                asyncTaskClass = new AsyncTaskClass(context, TaskListActivity.this, nameValuePair, WebService.SERVERURL, true);
//                asyncTaskClass.execute();
//            } else {
//                Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
//                return;
//            }
            } else {
                f_pos = 0;
                rotateAnimation = new RotateAnimation(context);
                if (rotateAnimation != null) {
                    if (rotateAnimation.isShowing()) {
                        rotateAnimation.dismiss();
                        rotateAnimation = null;
                    }
                }
                rotateAnimation.show();
                CallApiSequence();
            }
        } else {
            details_impl = new Details_impl(TaskListActivity.this);
            list_details = details_impl.getUser();
            if (list_details.size() < 1) {
                Toast.makeText(context, "No Data", Toast.LENGTH_SHORT).show();
            } else {
                ArrayList<TaskDetailCartModel> list2 = new ArrayList<TaskDetailCartModel>();
                list2.clear();
                for (int i = 0; i < list_details.size(); i++) {
                    if (job_status_type == 0) {
                        if (list_details.get(i).getTask_process().equalsIgnoreCase("Completed")) {

                        } else {
                            list2.add(new TaskDetailCartModel(list_details.get(i).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list_details.get(i).getTask_id()), list_details.get(i).getTask_number(), list_details.get(i).getClient_name(), list_details.get(i).getDate(), list_details.get(i).getTime(), list_details.get(i).getAddress(),
                                    list_details.get(i).getLatitude(), list_details.get(i).getLongitude(), list_details.get(i).getShort_description(), list_details.get(i).getDescription(), list_details.get(i).getAcknowledged_by(), list_details.get(i).getAcknowledged_date(), list_details.get(i).getAcknowledged_time(), list_details.get(i).getImage_path(), list_details.get(i).getImage_bitmap(),
                                    list_details.get(i).getTask_process(), list_details.get(i).getDisposal_site_name(), list_details.get(i).getDisposal_site_id(), list_details.get(i).getDisposal_date(), list_details.get(i).getDisposal_time(), list_details.get(i).getJob_type(), list_details.get(i).getSecond_man(), list_details.get(i).getJob_started(), list_details.get(i).getSing_off_status(), list_details.get(i).getJob_start_datetime(),
                                    list_details.get(i).getTask_sync_status(), list_details.get(i).getSs1(), list_details.get(i).getSs2(), list_details.get(i).getSs2_A(), list_details.get(i).getSs3(), list_details.get(i).getFinal_sync(), list_details.get(i).getNew_rental(), list_details.get(i).getFinal_removal(),list_details.get(i).getInstructions()
                            ));
                        }
                    } else if (job_status_type == 1) {
                        if (list_details.get(i).getTask_process().equalsIgnoreCase("Completed") || list_details.get(i).getTask_process().equalsIgnoreCase("In-progress")) {

                        } else {
                            list2.add(new TaskDetailCartModel(list_details.get(i).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list_details.get(i).getTask_id()), list_details.get(i).getTask_number(), list_details.get(i).getClient_name(), list_details.get(i).getDate(), list_details.get(i).getTime(), list_details.get(i).getAddress(),
                                    list_details.get(i).getLatitude(), list_details.get(i).getLongitude(), list_details.get(i).getShort_description(), list_details.get(i).getDescription(), list_details.get(i).getAcknowledged_by(), list_details.get(i).getAcknowledged_date(), list_details.get(i).getAcknowledged_time(), list_details.get(i).getImage_path(), list_details.get(i).getImage_bitmap(),
                                    list_details.get(i).getTask_process(), list_details.get(i).getDisposal_site_name(), list_details.get(i).getDisposal_site_id(), list_details.get(i).getDisposal_date(), list_details.get(i).getDisposal_time(), list_details.get(i).getJob_type(), list_details.get(i).getSecond_man(), list_details.get(i).getJob_started(), list_details.get(i).getSing_off_status(), list_details.get(i).getJob_start_datetime(),
                                    list_details.get(i).getTask_sync_status(), list_details.get(i).getSs1(), list_details.get(i).getSs2(), list_details.get(i).getSs2_A(), list_details.get(i).getSs3(), list_details.get(i).getFinal_sync(), list_details.get(i).getNew_rental(), list_details.get(i).getFinal_removal(),list_details.get(i).getInstructions()
                            ));
                        }
                    } else if (job_status_type == 2) {
                        if (list_details.get(i).getTask_process().equalsIgnoreCase("Completed") || list_details.get(i).getTask_process().equalsIgnoreCase("Pending")) {

                        } else {
                            list2.add(new TaskDetailCartModel(list_details.get(i).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list_details.get(i).getTask_id()), list_details.get(i).getTask_number(), list_details.get(i).getClient_name(), list_details.get(i).getDate(), list_details.get(i).getTime(), list_details.get(i).getAddress(),
                                    list_details.get(i).getLatitude(), list_details.get(i).getLongitude(), list_details.get(i).getShort_description(), list_details.get(i).getDescription(), list_details.get(i).getAcknowledged_by(), list_details.get(i).getAcknowledged_date(), list_details.get(i).getAcknowledged_time(), list_details.get(i).getImage_path(), list_details.get(i).getImage_bitmap(),
                                    list_details.get(i).getTask_process(), list_details.get(i).getDisposal_site_name(), list_details.get(i).getDisposal_site_id(), list_details.get(i).getDisposal_date(), list_details.get(i).getDisposal_time(), list_details.get(i).getJob_type(), list_details.get(i).getSecond_man(), list_details.get(i).getJob_started(), list_details.get(i).getSing_off_status(), list_details.get(i).getJob_start_datetime(),
                                    list_details.get(i).getTask_sync_status(), list_details.get(i).getSs1(), list_details.get(i).getSs2(), list_details.get(i).getSs2_A(), list_details.get(i).getSs3(), list_details.get(i).getFinal_sync(), list_details.get(i).getNew_rental(), list_details.get(i).getFinal_removal(),list_details.get(i).getInstructions()
                            ));
                        }
                    }
                }

                taskAdpater = new TaskAdpater(TaskListActivity.this, list2);
                listView.setAdapter(taskAdpater);
                taskAdpater.notifyDataSetChanged();


                Comparator<TaskDetailCartModel> comp = new Comparator<TaskDetailCartModel>() {
                    @Override
                    public int compare(TaskDetailCartModel c1, TaskDetailCartModel c2) {
                        return c2.getTask_process().compareTo(c1.getTask_process());
                    }
                };

                Collections.sort(list2, comp);
            }
        }

        spn_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (userIsInteracting) {
                    if (position == 0) {
                        job_status_type = 0;
                        details_impl = new Details_impl(TaskListActivity.this);
                        list_details = details_impl.getUser();
                        if (list_details.size() < 1) {
                            Toast.makeText(context, "No Data", Toast.LENGTH_SHORT).show();
                        } else {
                            ArrayList<TaskDetailCartModel> list2 = new ArrayList<TaskDetailCartModel>();
                            list2.clear();
                            for (int i = 0; i < list_details.size(); i++) {
                                if (list_details.get(i).getTask_process().equalsIgnoreCase("Completed")) {

                                } else {
                                    list2.add(new TaskDetailCartModel(list_details.get(i).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list_details.get(i).getTask_id()), list_details.get(i).getTask_number(), list_details.get(i).getClient_name(), list_details.get(i).getDate(), list_details.get(i).getTime(), list_details.get(i).getAddress(),
                                            list_details.get(i).getLatitude(), list_details.get(i).getLongitude(), list_details.get(i).getShort_description(), list_details.get(i).getDescription(), list_details.get(i).getAcknowledged_by(), list_details.get(i).getAcknowledged_date(), list_details.get(i).getAcknowledged_time(), list_details.get(i).getImage_path(), list_details.get(i).getImage_bitmap(),
                                            list_details.get(i).getTask_process(), list_details.get(i).getDisposal_site_name(), list_details.get(i).getDisposal_site_id(), list_details.get(i).getDisposal_date(), list_details.get(i).getDisposal_time(), list_details.get(i).getJob_type(), list_details.get(i).getSecond_man(), list_details.get(i).getJob_started(), list_details.get(i).getSing_off_status(), list_details.get(i).getJob_start_datetime(),
                                            list_details.get(i).getTask_sync_status(), list_details.get(i).getSs1(), list_details.get(i).getSs2(), list_details.get(i).getSs2_A(), list_details.get(i).getSs3(), list_details.get(i).getFinal_sync(), list_details.get(i).getNew_rental(), list_details.get(i).getFinal_removal(),list_details.get(i).getInstructions()
                                    ));
                                }
                            }

                            taskAdpater = new TaskAdpater(TaskListActivity.this, list2);
                            listView.setAdapter(taskAdpater);
                            taskAdpater.notifyDataSetChanged();

                            Comparator<TaskDetailCartModel> comp = new Comparator<TaskDetailCartModel>() {
                                @Override
                                public int compare(TaskDetailCartModel c1, TaskDetailCartModel c2) {
                                    return c2.getTask_process().compareTo(c1.getTask_process());
                                }
                            };

                            Collections.sort(list2, comp);
                        }
                    } else if (position == 1) {
                        job_status_type = 1;
                        details_impl = new Details_impl(TaskListActivity.this);
                        list_details = details_impl.getUser();
                        if (list_details.size() < 1) {
                            Toast.makeText(context, "No Data", Toast.LENGTH_SHORT).show();
                        } else {
                            ArrayList<TaskDetailCartModel> list2 = new ArrayList<TaskDetailCartModel>();
                            list2.clear();
                            for (int i = 0; i < list_details.size(); i++) {
                                if (list_details.get(i).getTask_process().equalsIgnoreCase("Completed") || list_details.get(i).getTask_process().equalsIgnoreCase("In-progress")) {

                                } else {
                                    list2.add(new TaskDetailCartModel(list_details.get(i).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list_details.get(i).getTask_id()), list_details.get(i).getTask_number(), list_details.get(i).getClient_name(), list_details.get(i).getDate(), list_details.get(i).getTime(), list_details.get(i).getAddress(),
                                            list_details.get(i).getLatitude(), list_details.get(i).getLongitude(), list_details.get(i).getShort_description(), list_details.get(i).getDescription(), list_details.get(i).getAcknowledged_by(), list_details.get(i).getAcknowledged_date(), list_details.get(i).getAcknowledged_time(), list_details.get(i).getImage_path(), list_details.get(i).getImage_bitmap(),
                                            list_details.get(i).getTask_process(), list_details.get(i).getDisposal_site_name(), list_details.get(i).getDisposal_site_id(), list_details.get(i).getDisposal_date(), list_details.get(i).getDisposal_time(), list_details.get(i).getJob_type(), list_details.get(i).getSecond_man(), list_details.get(i).getJob_started(), list_details.get(i).getSing_off_status(), list_details.get(i).getJob_start_datetime(),
                                            list_details.get(i).getTask_sync_status(), list_details.get(i).getSs1(), list_details.get(i).getSs2(), list_details.get(i).getSs2_A(), list_details.get(i).getSs3(), list_details.get(i).getFinal_sync(), list_details.get(i).getNew_rental(), list_details.get(i).getFinal_removal(),list_details.get(i).getInstructions()
                                    ));
                                }
                            }

                            taskAdpater = new TaskAdpater(TaskListActivity.this, list2);
                            listView.setAdapter(taskAdpater);
                            taskAdpater.notifyDataSetChanged();


                            Comparator<TaskDetailCartModel> comp = new Comparator<TaskDetailCartModel>() {
                                @Override
                                public int compare(TaskDetailCartModel c1, TaskDetailCartModel c2) {
                                    return c2.getTask_process().compareTo(c1.getTask_process());
                                }
                            };

                            Collections.sort(list2, comp);
                        }
                    } else if (position == 2) {
                        job_status_type = 2;
                        details_impl = new Details_impl(TaskListActivity.this);
                        list_details = details_impl.getUser();
                        if (list_details.size() < 1) {
                            Toast.makeText(context, "No Data", Toast.LENGTH_SHORT).show();
                        } else {
                            ArrayList<TaskDetailCartModel> list2 = new ArrayList<TaskDetailCartModel>();
                            list2.clear();
                            for (int i = 0; i < list_details.size(); i++) {
                                if (list_details.get(i).getTask_process().equalsIgnoreCase("Completed") || list_details.get(i).getTask_process().equalsIgnoreCase("Pending")) {

                                } else {
                                    list2.add(new TaskDetailCartModel(list_details.get(i).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list_details.get(i).getTask_id()), list_details.get(i).getTask_number(), list_details.get(i).getClient_name(), list_details.get(i).getDate(), list_details.get(i).getTime(), list_details.get(i).getAddress(),
                                            list_details.get(i).getLatitude(), list_details.get(i).getLongitude(), list_details.get(i).getShort_description(), list_details.get(i).getDescription(), list_details.get(i).getAcknowledged_by(), list_details.get(i).getAcknowledged_date(), list_details.get(i).getAcknowledged_time(), list_details.get(i).getImage_path(), list_details.get(i).getImage_bitmap(),
                                            list_details.get(i).getTask_process(), list_details.get(i).getDisposal_site_name(), list_details.get(i).getDisposal_site_id(), list_details.get(i).getDisposal_date(), list_details.get(i).getDisposal_time(), list_details.get(i).getJob_type(), list_details.get(i).getSecond_man(), list_details.get(i).getJob_started(), list_details.get(i).getSing_off_status(), list_details.get(i).getJob_start_datetime(),
                                            list_details.get(i).getTask_sync_status(), list_details.get(i).getSs1(), list_details.get(i).getSs2(), list_details.get(i).getSs2_A(), list_details.get(i).getSs3(), list_details.get(i).getFinal_sync(), list_details.get(i).getNew_rental(), list_details.get(i).getFinal_removal(),list_details.get(i).getInstructions()
                                    ));
                                }
                            }

                            taskAdpater = new TaskAdpater(TaskListActivity.this, list2);
                            listView.setAdapter(taskAdpater);
                            taskAdpater.notifyDataSetChanged();

                            Comparator<TaskDetailCartModel> comp = new Comparator<TaskDetailCartModel>() {
                                @Override
                                public int compare(TaskDetailCartModel c1, TaskDetailCartModel c2) {
                                    return c2.getTask_process().compareTo(c1.getTask_process());
                                }
                            };

                            Collections.sort(list2, comp);
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ids_service.size() > 0) {
                    int rental = 0;
                    for (int i = 0; i < ids_service.size(); i++) {
                        Details_impl main_impl = new Details_impl(TaskListActivity.this);
                        list_details = main_impl.getUser();


                        for (int j = 0; j < list_details.size(); j++) {
                            if (list_details.get(j).getTask_id().equalsIgnoreCase(ids_service.get(i))) {
                                if (list_details.get(j).getNew_rental().equalsIgnoreCase("Y")) {
                                    rental++;
                                }
                            }
                        }
                    }


                    if (rental == ids_service.size()) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(TaskListActivity.this);

                        builder.setTitle("Confirm");
                        builder.setMessage("Are you sure you want to Complete?");

                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog3, int which) {

                                SimpleDateFormat myFormatdate = new SimpleDateFormat("yyyy-MM-dd");//Y-m-d H:i:s  2016-09-19 15:09:00
                                SimpleDateFormat myFormattime = new SimpleDateFormat("HH:mm:ss");
                                try {
                                    Date date = new Date();
                                    strsignoffdate = myFormatdate.format(date);
                                    strsignofftime = myFormattime.format(date);

                                    Log.i(DEBUG_TAG, "Current Date Time in give format: " + strsignoffdate + " " + strsignofftime);
                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                for (int i = 0; i < ids_service.size(); i++) {
                                    Details_impl main_impl = new Details_impl(TaskListActivity.this);
                                    list_details = main_impl.getUser();


                                    for (int j = 0; j < list_details.size(); j++) {
                                        if (list_details.get(j).getTask_id().equalsIgnoreCase(ids_service.get(i))) {
                                            long det3 = main_impl.update(new TaskDetailCartModel(list_details.get(j).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list_details.get(j).getTask_id()), list_details.get(j).getTask_number(), list_details.get(j).getClient_name(), list_details.get(j).getDate(), list_details.get(j).getTime(), list_details.get(j).getAddress(),
                                                    list_details.get(j).getLatitude(), list_details.get(j).getLongitude(), list_details.get(j).getShort_description(), list_details.get(j).getDescription(), list_details.get(j).getAcknowledged_by(), list_details.get(j).getAcknowledged_date(), list_details.get(j).getAcknowledged_time(), list_details.get(j).getImage_path(), list_details.get(j).getImage_bitmap(),
                                                    "Completed", "SBRC", "SBR100", strsignoffdate, strsignofftime, list_details.get(j).getJob_type(), list_details.get(j).getSecond_man(), list_details.get(j).getJob_started(), list_details.get(j).getSing_off_status(), list_details.get(j).getJob_start_datetime(),
                                                    "4", list_details.get(j).getSs1(), list_details.get(j).getSs2(), list_details.get(j).getSs2_A(), "1", "0", list_details.get(j).getNew_rental(), list_details.get(j).getFinal_removal(),list_details.get(j).getInstructions()
                                            ));
                                        }
                                    }
                                }

                                Details_impl main_impl2 = new Details_impl(TaskListActivity.this);
                                list_details = main_impl2.getUser();
                                ArrayList<TaskDetailCartModel> list2 = new ArrayList<>();
                                list2.clear();
                                for (int i = 0; i < list_details.size(); i++) {
                                    if (job_status_type == 0) {
                                        if (list_details.get(i).getTask_process().equalsIgnoreCase("Completed")) {

                                        } else {
                                            list2.add(new TaskDetailCartModel(list_details.get(i).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list_details.get(i).getTask_id()), list_details.get(i).getTask_number(), list_details.get(i).getClient_name(), list_details.get(i).getDate(), list_details.get(i).getTime(), list_details.get(i).getAddress(),
                                                    list_details.get(i).getLatitude(), list_details.get(i).getLongitude(), list_details.get(i).getShort_description(), list_details.get(i).getDescription(), list_details.get(i).getAcknowledged_by(), list_details.get(i).getAcknowledged_date(), list_details.get(i).getAcknowledged_time(), list_details.get(i).getImage_path(), list_details.get(i).getImage_bitmap(),
                                                    list_details.get(i).getTask_process(), list_details.get(i).getDisposal_site_name(), list_details.get(i).getDisposal_site_id(), list_details.get(i).getDisposal_date(), list_details.get(i).getDisposal_time(), list_details.get(i).getJob_type(), list_details.get(i).getSecond_man(), list_details.get(i).getJob_started(), list_details.get(i).getSing_off_status(), list_details.get(i).getJob_start_datetime(),
                                                    list_details.get(i).getTask_sync_status(), list_details.get(i).getSs1(), list_details.get(i).getSs2(), list_details.get(i).getSs2_A(), list_details.get(i).getSs3(), list_details.get(i).getFinal_sync(), list_details.get(i).getNew_rental(), list_details.get(i).getFinal_removal(),list_details.get(i).getInstructions()
                                            ));
                                        }
                                    } else if (job_status_type == 1) {
                                        if (list_details.get(i).getTask_process().equalsIgnoreCase("Completed") || list_details.get(i).getTask_process().equalsIgnoreCase("In-progress")) {

                                        } else {
                                            list2.add(new TaskDetailCartModel(list_details.get(i).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list_details.get(i).getTask_id()), list_details.get(i).getTask_number(), list_details.get(i).getClient_name(), list_details.get(i).getDate(), list_details.get(i).getTime(), list_details.get(i).getAddress(),
                                                    list_details.get(i).getLatitude(), list_details.get(i).getLongitude(), list_details.get(i).getShort_description(), list_details.get(i).getDescription(), list_details.get(i).getAcknowledged_by(), list_details.get(i).getAcknowledged_date(), list_details.get(i).getAcknowledged_time(), list_details.get(i).getImage_path(), list_details.get(i).getImage_bitmap(),
                                                    list_details.get(i).getTask_process(), list_details.get(i).getDisposal_site_name(), list_details.get(i).getDisposal_site_id(), list_details.get(i).getDisposal_date(), list_details.get(i).getDisposal_time(), list_details.get(i).getJob_type(), list_details.get(i).getSecond_man(), list_details.get(i).getJob_started(), list_details.get(i).getSing_off_status(), list_details.get(i).getJob_start_datetime(),
                                                    list_details.get(i).getTask_sync_status(), list_details.get(i).getSs1(), list_details.get(i).getSs2(), list_details.get(i).getSs2_A(), list_details.get(i).getSs3(), list_details.get(i).getFinal_sync(), list_details.get(i).getNew_rental(), list_details.get(i).getFinal_removal(),list_details.get(i).getInstructions()
                                            ));
                                        }
                                    } else if (job_status_type == 2) {
                                        if (list_details.get(i).getTask_process().equalsIgnoreCase("Completed") || list_details.get(i).getTask_process().equalsIgnoreCase("Pending")) {

                                        } else {
                                            list2.add(new TaskDetailCartModel(list_details.get(i).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list_details.get(i).getTask_id()), list_details.get(i).getTask_number(), list_details.get(i).getClient_name(), list_details.get(i).getDate(), list_details.get(i).getTime(), list_details.get(i).getAddress(),
                                                    list_details.get(i).getLatitude(), list_details.get(i).getLongitude(), list_details.get(i).getShort_description(), list_details.get(i).getDescription(), list_details.get(i).getAcknowledged_by(), list_details.get(i).getAcknowledged_date(), list_details.get(i).getAcknowledged_time(), list_details.get(i).getImage_path(), list_details.get(i).getImage_bitmap(),
                                                    list_details.get(i).getTask_process(), list_details.get(i).getDisposal_site_name(), list_details.get(i).getDisposal_site_id(), list_details.get(i).getDisposal_date(), list_details.get(i).getDisposal_time(), list_details.get(i).getJob_type(), list_details.get(i).getSecond_man(), list_details.get(i).getJob_started(), list_details.get(i).getSing_off_status(), list_details.get(i).getJob_start_datetime(),
                                                    list_details.get(i).getTask_sync_status(), list_details.get(i).getSs1(), list_details.get(i).getSs2(), list_details.get(i).getSs2_A(), list_details.get(i).getSs3(), list_details.get(i).getFinal_sync(), list_details.get(i).getNew_rental(), list_details.get(i).getFinal_removal(),list_details.get(i).getInstructions()
                                            ));
                                        }
                                    }
                                }


                                taskAdpater = new TaskAdpater(TaskListActivity.this, list2);
                                listView.setAdapter(taskAdpater);
                                taskAdpater.notifyDataSetChanged();

                                Comparator<TaskDetailCartModel> comp = new Comparator<TaskDetailCartModel>() {
                                    @Override
                                    public int compare(TaskDetailCartModel c1, TaskDetailCartModel c2) {
                                        return c2.getTask_process().compareTo(c1.getTask_process());
                                    }
                                };

                                Collections.sort(list2, comp);


                                if (connectionDetector.isConnectingToInternet()) {
                                    f_pos = 0;
                                    rotateAnimation = new RotateAnimation(context);
                                    rotateAnimation.show();
                                    CallApiSequence();
                                } else {
                                    Toast.makeText(TaskListActivity.this, "Completed", Toast.LENGTH_SHORT).show();
                                }


                                dialog3.dismiss();
                            }
                        });

                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog3, int which) {

                                // Do nothing
                                dialog3.dismiss();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();


                    } else {
                        ShowAddCard(context);
                    }


                    SampleArrayList.clear();
                    SparseBooleanArray selectedRows = taskAdpater.getSelectedIds();
                    if (selectedRows.size() > 0) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < selectedRows.size(); i++) {
                            if (selectedRows.valueAt(i)) {
//                            String selectedRowLabel = arrLike.get(selectedRows.keyAt(i)).getId();
                                String selectedRowLabel = list_details.get(selectedRows.keyAt(i)).getTask_id();
                                stringBuilder.append(selectedRowLabel + "\n");
//                            SampleArrayList.add(arrLike.get(selectedRows.keyAt(i)).getId());
//                            if (list.get(selectedRows.keyAt(i)).getTask_process().equalsIgnoreCase("Completed")){
//                                SampleArrayList.add(list.get(selectedRows.keyAt(i+1)).getTask_id());
//                            }else {
                                SampleArrayList.add(list_details.get(selectedRows.keyAt(i)).getTask_id());
//                            }
                            }
                        }
                        allIds = TextUtils.join(",", SampleArrayList);
                        Log.d("sampledistance", allIds);
//                    ShowAddCard(context);
//                    Toast.makeText(context, "Selected Rows\n" + stringBuilder.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TaskListActivity.this);

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure you want to Logout?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog3, int which) {
                        if (connectionDetector.isConnectingToInternet()) {
                            dialog3.dismiss();
                            logout_status = "yes";
                            rotateAnimation = new RotateAnimation(context);
                            rotateAnimation.show();
                            CallApiSequence();

//                            final Dialog dialog2 = new Dialog(TaskListActivity.this);
//                            dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                            dialog2.setContentView(R.layout.single_log);
//                            TextView tx_log = (TextView) dialog2.findViewById(R.id.tx_log);
//                            Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
//                            Button btn_cancel = dialog2.findViewById(R.id.btn_cancel);
//
//                            dialog2.show();
//                            tx_log.setText(log.toString());
//                            btn_cancel.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    dialog2.dismiss();
//                                }
//                            });
                        } else {
                            Toast.makeText(context, "Please Check your internet connection", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog3, int which) {

                        // Do nothing
                        dialog3.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });
        btn_edtsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText_search.setText("");
                taskAdpater.filter("");
            }
        });

    }

    public void ShowAddCard(final Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custome_card_add);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCanceledOnTouchOutside(false);

//        Button btn_send = dialog.findViewById(R.id.btn_send);
        tx_disposal = dialog.findViewById(R.id.tx_disposal);
        txt_disposalsiteid = dialog.findViewById(R.id.txt_disposalsiteid);
        ImageView img_close = dialog.findViewById(R.id.img_close);
        if (internalStorage.fileExists(getApplicationContext(), "EmenitiesList")) {
            try {
                arrDisposal = (ArrayList<DisposalModel>) internalStorage.readObject(getApplicationContext(), "EmenitiesList");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
//            if (nameValuePair.size() > 0) {
//                nameValuePair.clear();
//            }
//
//            nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, WebService.KEY_TASKDETAIL_TASK));
////        nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASKDETAIL_TASKIDDKEY, arrLike.get(0).getId()));
//            nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASKDETAIL_TASKIDDKEY, list_details.get(0).getTask_id()));
//
//            if (connectionDetector.isConnectingToInternet()) {
//                key = "detail";
//                asyncTaskClass = new AsyncTaskClass(context, TaskListActivity.this, nameValuePair, WebService.SERVERURL, true);
//                asyncTaskClass.execute();
//            } else {
//                Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
//                return;
//            }
        }

        tx_disposal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog2 = new Dialog(TaskListActivity.this);
                dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog2.setContentView(R.layout.single_child_list);
                ListView lst_chld = (ListView) dialog2.findViewById(R.id.lst_chld);
                Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
                Button btn_cancel = dialog2.findViewById(R.id.btn_cancel);
                regionidex=0;
                disposalAdapter2 = new DisposalAdapter2(context, arrDisposal);
                lst_chld.setAdapter(disposalAdapter2);

                dialog2.show();

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog2.dismiss();
                    }
                });

                lst_chld.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        DisposalModel data = (DisposalModel) disposalAdapter2.getItem(position);
//                        tx_company.setText(data.getCompany_name());
//                        regionname = data.getAlbumName();
                        regionidex = position;

                        if (position == 0) {
                            tx_disposal.setText("Please Select disposal site");
                            txt_disposalsiteid.setText("");
                            namedisposal = "";
                        } else {
                            tx_disposal.setText(data.getName());
                            txt_disposalsiteid.setText(data.getId());
                            namedisposal = data.getName();
                            disposalAdapter2.notifyDataSetChanged();
                        }
                    }
                });
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (regionidex == 0) {
                            Toast.makeText(getApplicationContext(), "Select Disposal site", Toast.LENGTH_SHORT).show();
                        } else {

                            AlertDialog.Builder builder = new AlertDialog.Builder(TaskListActivity.this);

                            builder.setTitle("Confirm");
                            builder.setMessage("Are you sure you want to Complete?");

                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog3, int which) {
                                    // Do nothing but close the dialog
//                                        if (nameValuePair.size() > 0) {
//                                            nameValuePair.clear();
//                                        }

//                                        SimpleDateFormat myFormatdate = new SimpleDateFormat("yyyy-MM-dd");//Y-m-d H:i:s  2016-09-19 15:09:00
//                                        SimpleDateFormat myFormattime = new SimpleDateFormat("HH:mm:ss");
//                                        try {
//                                            Date date = new Date();
//                                            strsignoffdate = myFormatdate.format(date);
//                                            strsignofftime = myFormattime.format(date);
//
//                                            Log.i(DEBUG_TAG, "Current Date Time in give format: " + strsignoffdate + " " + strsignofftime);
//                                        } catch (ParseException e) {
//                                            // TODO Auto-generated catch block
//                                            e.printStackTrace();
//                                        }
//                                        Log.d("allids",allIds);
//                                        nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, "save_ticket_combined"));
//                                        nameValuePair.add(new BasicNameValuePair(WebService.KEY_TIPPINGBOOTASKID, allIds));
//                                        nameValuePair.add(new BasicNameValuePair(WebService.KEY_TIPPINGBOOKID, "0"));
//                                        nameValuePair.add(new BasicNameValuePair(WebService.KEY_TIPPINGBOOKTICKET, "0000"));
//                                        nameValuePair.add(new BasicNameValuePair(WebService.KEY_TIPPINGBOOKDATETIME, strsignoffdate + " " + strsignofftime));
//
//
//                                        if (connectionDetector.isConnectingToInternet()) {
//                                            key = "savetippingbook";
//                                            asyncTaskClass = new AsyncTaskClass(context, TaskListActivity.this, nameValuePair, WebService.SERVERURL, true);
//                                            asyncTaskClass.execute();
//                                            if (dialog != null) {
//                                                if (dialog.isShowing()) {
//                                                    dialog.dismiss();
//                                                }
//                                            }
//                                        } else {
//                                            Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
//                                            return;
//                                        }

                                    SimpleDateFormat myFormatdate = new SimpleDateFormat("yyyy-MM-dd");//Y-m-d H:i:s  2016-09-19 15:09:00
                                    SimpleDateFormat myFormattime = new SimpleDateFormat("HH:mm:ss");
                                    try {
                                        Date date = new Date();
                                        strsignoffdate = myFormatdate.format(date);
                                        strsignofftime = myFormattime.format(date);

                                        Log.i(DEBUG_TAG, "Current Date Time in give format: " + strsignoffdate + " " + strsignofftime);
                                    } catch (ParseException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

                                    details_impl = new Details_impl(TaskListActivity.this);

                                    allAmeIds = TextUtils.join(",", ids_service);

                                    for (int i = 0; i < ids_service.size(); i++) {
//                                        list_details = details_impl.getUserDetails1(SampleArrayList.get(i));
//
//
//                                        Details_impl details_impl2 = new Details_impl(TaskListActivity.this);
//                                        list_details = details_impl2.getUserDetails1(SampleArrayList.get(i));
//                                        long det2 = details_impl2.update(new TaskDetailCartModel(list_details.get(0).getId(),sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list_details.get(0).getTask_id()), list_details.get(0).getTask_number(), list_details.get(0).getClient_name(), list_details.get(0).getDate(), list_details.get(0).getTime(), list_details.get(0).getAddress(),
//                                                list_details.get(0).getLatitude(), list_details.get(0).getLongitude(), list_details.get(0).getShort_description(), list_details.get(0).getDescription(), list_details.get(0).getAcknowledged_by(), list_details.get(0).getAcknowledged_date(), list_details.get(0).getAcknowledged_time(), list_details.get(0).getImage_path(),list_details.get(0).getImage_bitmap(),
//                                                "In-progress", namedisposal, txt_disposalsiteid.getText().toString(), strsignoffdate, strsignofftime, list_details.get(0).getJob_type(), list_details.get(0).getSecond_man(), list_details.get(0).getJob_started(), list_details.get(0).getSing_off_status(), list_details.get(0).getJob_start_datetime(),
//                                                "4", list_details.get(0).getSs1(), list_details.get(0).getSs2(), list_details.get(0).getSs2_A(), "1", "0"
//                                        ));
                                        Details_impl main_impl = new Details_impl(TaskListActivity.this);
                                        list_details = main_impl.getUser();

                                        for (int j = 0; j < list_details.size(); j++) {
                                            if (list_details.get(j).getTask_id().equalsIgnoreCase(ids_service.get(i))) {

//                                                long det3 = main_impl.update(new TaskListCartModel(list.get(j).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), list.get(j).getTask_id(), list.get(j).getTask_number(), list.get(j).getClient_name(), list.get(j).getAddress(),
//                                                        list.get(j).getShort_description(), list.get(j).getJob_type(), "Completed"));
                                                long det3 = main_impl.update(new TaskDetailCartModel(list_details.get(j).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list_details.get(j).getTask_id()), list_details.get(j).getTask_number(), list_details.get(j).getClient_name(), list_details.get(j).getDate(), list_details.get(j).getTime(), list_details.get(j).getAddress(),
                                                        list_details.get(j).getLatitude(), list_details.get(j).getLongitude(), list_details.get(j).getShort_description(), list_details.get(j).getDescription(), list_details.get(j).getAcknowledged_by(), list_details.get(j).getAcknowledged_date(), list_details.get(j).getAcknowledged_time(), list_details.get(j).getImage_path(), list_details.get(j).getImage_bitmap(),
                                                        "Completed", namedisposal, txt_disposalsiteid.getText().toString(), strsignoffdate, strsignofftime, list_details.get(j).getJob_type(), list_details.get(j).getSecond_man(), list_details.get(j).getJob_started(), list_details.get(j).getSing_off_status(), list_details.get(j).getJob_start_datetime(),
                                                        "4", list_details.get(j).getSs1(), list_details.get(j).getSs2(), list_details.get(j).getSs2_A(), "1", "0", list_details.get(j).getNew_rental(), list_details.get(j).getFinal_removal(), list_details.get(j).getInstructions()
                                                ));

                                            }
                                        }
                                    }
                                    Details_impl main_impl2 = new Details_impl(TaskListActivity.this);
                                    list_details = main_impl2.getUser();
                                    ArrayList<TaskDetailCartModel> list2 = new ArrayList<>();
                                    for (int i = 0; i < list_details.size(); i++) {
                                        if (job_status_type == 0) {
                                            if (list_details.get(i).getTask_process().equalsIgnoreCase("Completed")) {

                                            } else {
                                                list2.add(new TaskDetailCartModel(list_details.get(i).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list_details.get(i).getTask_id()), list_details.get(i).getTask_number(), list_details.get(i).getClient_name(), list_details.get(i).getDate(), list_details.get(i).getTime(), list_details.get(i).getAddress(),
                                                        list_details.get(i).getLatitude(), list_details.get(i).getLongitude(), list_details.get(i).getShort_description(), list_details.get(i).getDescription(), list_details.get(i).getAcknowledged_by(), list_details.get(i).getAcknowledged_date(), list_details.get(i).getAcknowledged_time(), list_details.get(i).getImage_path(), list_details.get(i).getImage_bitmap(),
                                                        list_details.get(i).getTask_process(), list_details.get(i).getDisposal_site_name(), list_details.get(i).getDisposal_site_id(), list_details.get(i).getDisposal_date(), list_details.get(i).getDisposal_time(), list_details.get(i).getJob_type(), list_details.get(i).getSecond_man(), list_details.get(i).getJob_started(), list_details.get(i).getSing_off_status(), list_details.get(i).getJob_start_datetime(),
                                                        list_details.get(i).getTask_sync_status(), list_details.get(i).getSs1(), list_details.get(i).getSs2(), list_details.get(i).getSs2_A(), list_details.get(i).getSs3(), list_details.get(i).getFinal_sync(), list_details.get(i).getNew_rental(), list_details.get(i).getFinal_removal(), list_details.get(i).getInstructions()
                                                ));
                                            }
                                        } else if (job_status_type == 1) {
                                            if (list_details.get(i).getTask_process().equalsIgnoreCase("Completed") || list_details.get(i).getTask_process().equalsIgnoreCase("In-progress")) {

                                            } else {
                                                list2.add(new TaskDetailCartModel(list_details.get(i).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list_details.get(i).getTask_id()), list_details.get(i).getTask_number(), list_details.get(i).getClient_name(), list_details.get(i).getDate(), list_details.get(i).getTime(), list_details.get(i).getAddress(),
                                                        list_details.get(i).getLatitude(), list_details.get(i).getLongitude(), list_details.get(i).getShort_description(), list_details.get(i).getDescription(), list_details.get(i).getAcknowledged_by(), list_details.get(i).getAcknowledged_date(), list_details.get(i).getAcknowledged_time(), list_details.get(i).getImage_path(), list_details.get(i).getImage_bitmap(),
                                                        list_details.get(i).getTask_process(), list_details.get(i).getDisposal_site_name(), list_details.get(i).getDisposal_site_id(), list_details.get(i).getDisposal_date(), list_details.get(i).getDisposal_time(), list_details.get(i).getJob_type(), list_details.get(i).getSecond_man(), list_details.get(i).getJob_started(), list_details.get(i).getSing_off_status(), list_details.get(i).getJob_start_datetime(),
                                                        list_details.get(i).getTask_sync_status(), list_details.get(i).getSs1(), list_details.get(i).getSs2(), list_details.get(i).getSs2_A(), list_details.get(i).getSs3(), list_details.get(i).getFinal_sync(), list_details.get(i).getNew_rental(), list_details.get(i).getFinal_removal(), list_details.get(i).getInstructions()
                                                ));
                                            }
                                        } else if (job_status_type == 2) {
                                            if (list_details.get(i).getTask_process().equalsIgnoreCase("Completed") || list_details.get(i).getTask_process().equalsIgnoreCase("Pending")) {

                                            } else {
                                                list2.add(new TaskDetailCartModel(list_details.get(i).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list_details.get(i).getTask_id()), list_details.get(i).getTask_number(), list_details.get(i).getClient_name(), list_details.get(i).getDate(), list_details.get(i).getTime(), list_details.get(i).getAddress(),
                                                        list_details.get(i).getLatitude(), list_details.get(i).getLongitude(), list_details.get(i).getShort_description(), list_details.get(i).getDescription(), list_details.get(i).getAcknowledged_by(), list_details.get(i).getAcknowledged_date(), list_details.get(i).getAcknowledged_time(), list_details.get(i).getImage_path(), list_details.get(i).getImage_bitmap(),
                                                        list_details.get(i).getTask_process(), list_details.get(i).getDisposal_site_name(), list_details.get(i).getDisposal_site_id(), list_details.get(i).getDisposal_date(), list_details.get(i).getDisposal_time(), list_details.get(i).getJob_type(), list_details.get(i).getSecond_man(), list_details.get(i).getJob_started(), list_details.get(i).getSing_off_status(), list_details.get(i).getJob_start_datetime(),
                                                        list_details.get(i).getTask_sync_status(), list_details.get(i).getSs1(), list_details.get(i).getSs2(), list_details.get(i).getSs2_A(), list_details.get(i).getSs3(), list_details.get(i).getFinal_sync(), list_details.get(i).getNew_rental(), list_details.get(i).getFinal_removal(), list_details.get(i).getInstructions()
                                                ));
                                            }
                                        }
                                    }


                                    taskAdpater = new TaskAdpater(TaskListActivity.this, list2);
                                    listView.setAdapter(taskAdpater);
                                    taskAdpater.notifyDataSetChanged();

                                    Comparator<TaskDetailCartModel> comp = new Comparator<TaskDetailCartModel>() {
                                        @Override
                                        public int compare(TaskDetailCartModel c1, TaskDetailCartModel c2) {
                                            return c2.getTask_process().compareTo(c1.getTask_process());
                                        }
                                    };

                                    Collections.sort(list2, comp);


//                                        if (nameValuePair.size() > 0) {
//                                            nameValuePair.clear();
//                                        }
//
//                                        nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, "update_status_combined_offline"));
//                                        nameValuePair.add(new BasicNameValuePair(WebService.KEY_UPDATETASKID, allIds));
//                                        nameValuePair.add(new BasicNameValuePair(WebService.KEY_UPDATETASKPROCESS, "Completed"));
//                                        nameValuePair.add(new BasicNameValuePair(WebService.KEY_SIGNOFFDATETIME, strsignoffdate + " " + strsignofftime));
//                                        nameValuePair.add(new BasicNameValuePair("task_sync_status", list_details.get(0).getTask_sync_status()));
//                                        nameValuePair.add(new BasicNameValuePair("ss1", list_details.get(0).getSs1()));
//                                        nameValuePair.add(new BasicNameValuePair("ss2", list_details.get(0).getSs2()));
//                                        nameValuePair.add(new BasicNameValuePair("ss3", list_details.get(0).getSs3()));
//                                        nameValuePair.add(new BasicNameValuePair("final_sync", list_details.get(0).getFinal_sync()));
//                                        if (!txt_disposalsiteid.getText().toString().equals("")) {
//                                            nameValuePair.add(new BasicNameValuePair(WebService.KEY_DISPOSAL_NAME, namedisposal));
//                                            nameValuePair.add(new BasicNameValuePair(WebService.KEY_DISPOSAL_ID, txt_disposalsiteid.getText().toString()));
//                                            nameValuePair.add(new BasicNameValuePair(WebService.KEY_TIPPINGBOOKDATETIME, strsignoffdate + " " + strsignofftime));
//
//                                        } else {
//                                            nameValuePair.add(new BasicNameValuePair(WebService.KEY_DISPOSAL_NAME, ""));
//                                            nameValuePair.add(new BasicNameValuePair(WebService.KEY_DISPOSAL_ID, ""));
//                                            nameValuePair.add(new BasicNameValuePair(WebService.KEY_TIPPINGBOOKDATETIME, strsignoffdate + " " + strsignofftime));
//                                        }
//
//                                        if (connectionDetector.isConnectingToInternet()) {
//                                            key = "statusupdate";
//                                            asyncTaskClass = new AsyncTaskClass(context, TaskListActivity.this, nameValuePair, WebService.SERVERURL, true);
//                                            asyncTaskClass.execute();
//                                        } else {
//                                            Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
//                                            return;
//                                        }
                                    if (connectionDetector.isConnectingToInternet()) {
                                        rotateAnimation = new RotateAnimation(context);
                                        rotateAnimation.show();
                                        CallApiSequence();
                                    } else {
                                        Toast.makeText(TaskListActivity.this, "Completed", Toast.LENGTH_SHORT).show();
                                    }

                                    dialog2.dismiss();
                                    dialog.dismiss();

                                    dialog3.dismiss();
                                }
                            });

                            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog3, int which) {

                                    // Do nothing
                                    dialog3.dismiss();
                                }
                            });

                            AlertDialog alert = builder.create();
                            alert.show();


//                                final com.rey.material.app.SimpleDialog mDialog = new com.rey.material.app.SimpleDialog(context);
//                                mDialog.title("Are you sure you want to Complete?");
//                                mDialog.positiveAction("YES");
//                                mDialog.negativeAction("NO");
//                                mDialog.cancelable(false);
//
//                                mDialog.positiveActionClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        if (nameValuePair.size() > 0) {
//                                            nameValuePair.clear();
//                                        }
//                                        SimpleDateFormat myFormatdate = new SimpleDateFormat("yyyy-MM-dd");//Y-m-d H:i:s  2016-09-19 15:09:00
//                                        SimpleDateFormat myFormattime = new SimpleDateFormat("HH:mm:ss");
//                                        try {
//                                            Date date = new Date();
//                                            strsignoffdate = myFormatdate.format(date);
//                                            strsignofftime = myFormattime.format(date);
//
//                                            Log.i(DEBUG_TAG, "Current Date Time in give format: " + strsignoffdate + " " + strsignofftime);
//                                        } catch (ParseException e) {
//                                            // TODO Auto-generated catch block
//                                            e.printStackTrace();
//                                        }
//                                        Log.d("allids",allIds);
//                                        nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, "save_ticket_combined"));
//                                        nameValuePair.add(new BasicNameValuePair(WebService.KEY_TIPPINGBOOTASKID, allIds));
//                                        nameValuePair.add(new BasicNameValuePair(WebService.KEY_TIPPINGBOOKID, "0"));
//                                        nameValuePair.add(new BasicNameValuePair(WebService.KEY_TIPPINGBOOKTICKET, "0000"));
//                                        nameValuePair.add(new BasicNameValuePair(WebService.KEY_TIPPINGBOOKDATETIME, strsignoffdate + " " + strsignofftime));
//
//
//                                        if (connectionDetector.isConnectingToInternet()) {
//                                            key = "savetippingbook";
//                                            asyncTaskClass = new AsyncTaskClass(context, TaskListActivity.this, nameValuePair, WebService.SERVERURL, true);
//                                            asyncTaskClass.execute();
//                                            if (dialog != null) {
//                                                if (dialog.isShowing()) {
//                                                    dialog.dismiss();
//                                                }
//                                            }
//                                        } else {
//                                            Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
//                                            return;
//                                        }
////                            tx_album.setText(regionname);
//
//                                        mDialog.dismiss();
//                                        dialog2.dismiss();
//                                        dialog.dismiss();
//
//                                    }
//                                });
//                                mDialog.negativeActionClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        mDialog.dismiss();
//                                    }
//                                });
//                                mDialog.show();


                        }
                    }
                });
            }
        });
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public class DisposalAdapter2 extends BaseAdapter {
        Context context;
        ArrayList<DisposalModel> rowItems;

        public DisposalAdapter2(Context context, ArrayList<DisposalModel> rowItems) {
            this.context = context;
            this.rowItems = rowItems;
        }

        /*private view holder class*/
        private class ViewHolder {
            TextView tx_name;
            RadioButton radio;
        }

        @Override
        public int getCount() {
            return rowItems.size();
        }

        @Override
        public Object getItem(int position) {
            return rowItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return rowItems.indexOf(getItem(position));
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.single_pass_company, null);
                holder = new ViewHolder();
                holder.tx_name = (TextView) convertView.findViewById(R.id.tx_name);

                holder.radio = (RadioButton) convertView.findViewById(R.id.radio);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            final DisposalModel rowItem = (DisposalModel) getItem(position);

            holder.tx_name.setText(rowItem.getName());
            if (regionidex == position) {
                holder.radio.setChecked(true);
            } else {
                holder.radio.setChecked(false);
            }

            return convertView;
        }
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
//        rotateAnimation = new RotateAnimation(context);

//		btn_edtsearch.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if(editText_search.getText().toString().length()>0)
//				{
//					if(nameValuePair.size()>0)
//					{
//						nameValuePair.clear();
//					}
//
//					nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, WebService.KEY_SEARCHCOMPANYTASK));
//					nameValuePair.add(new BasicNameValuePair(WebService.KEY_SEARCHCOMPANYDRIVER, sharedPrefrenceClass.getKEY_LOGIN_USERID()));
//					nameValuePair.add(new BasicNameValuePair(WebService.KEY_SEARCHCOMPANYSEARCH, editText_search.getText().toString().trim()));
//
//					if(connectionDetector.isConnectingToInternet())
//					{
//
//						asyncTaskClass = new AsyncTaskClass(context, TaskListActivity.this, nameValuePair, WebService.SERVERURL,false);
//						asyncTaskClass.execute();
//					}
//					else
//					{
//						Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
//						return;
//					}
//				}
//				else
//				{
//					editText_search.setError("please enter text");
//				}
//			}
//		});

        arrSecond2 = secondMan_impl.getUser();
        if (arrSecond2.size() < 1) {
            ListDrivers();
        }

        editText_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = editText_search.getText().toString().toLowerCase(Locale.getDefault());
                taskAdpater.filter(text);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                details_impl = new Details_impl(TaskListActivity.this);
                list_details = details_impl.getUser();


                TaskDetailCartModel model = (TaskDetailCartModel) taskAdpater.getItem(arg2);
                Intent intent = new Intent(TaskListActivity.this, TaskDetailActivity.class);
//                intent.putExtra("id", model.getTask_id());
                intent.putExtra("id", model.getTask_id());
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();


        details_impl = new Details_impl(this);
        list_details = details_impl.getTotalListUser(sharedPrefrenceClass.getKEY_LOGIN_USERID());
//        Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show();
        ids_service.clear();

//        if (list_details.size() < 1) {
//            if (nameValuePair.size() > 0) {
//                nameValuePair.clear();
//            }
//            key = "List";
//            String datetime2 = "";
//            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//Y-m-d H:i:s  2016-09-19 15:09:00
//            try {
//                Date date = new Date();
//                datetime2 = myFormat.format(date);
//                Log.i(DEBUG_TAG, "Current Date Time in give format: " + datetime2);
//            } catch (ParseException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//            nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, WebService.KEY_TASKLIST_TASK_OFFLINE));
//            nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASKLIST_DRIVERIDKEY, sharedPrefrenceClass.getKEY_LOGIN_USERID()));
//            nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASKLIST_DATEKEY, "2019-12-12 15:09:00"));
//            arrLike.clear();
//            if (connectionDetector.isConnectingToInternet()) {
//
//                asyncTaskClass = new AsyncTaskClass(context, TaskListActivity.this, nameValuePair, WebService.SERVERURL, true);
//                asyncTaskClass.execute();
//            } else {
//                Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
//                return;
//            }
//        } else {
//            if (connectionDetector.isConnectingToInternet()) {
//                if (nameValuePair.size() > 0) {
//                    nameValuePair.clear();
//                }
//                key = "List";
//                String datetime = "";
//                SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//Y-m-d H:i:s  2016-09-19 15:09:00
//                try {
//                    Date date = new Date();
//                    datetime = myFormat.format(date);
//                    Log.i(DEBUG_TAG, "Current Date Time in give format: " + datetime);
//                } catch (ParseException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, WebService.KEY_TASKLIST_TASK));
//                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASKLIST_DRIVERIDKEY, sharedPrefrenceClass.getKEY_LOGIN_USERID()));
//                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASKLIST_DATEKEY, datetime));
//                arrLike.clear();
//                if (connectionDetector.isConnectingToInternet()) {
//
//                    asyncTaskClass = new AsyncTaskClass(context, TaskListActivity.this, nameValuePair, WebService.SERVERURL, true);
//                    asyncTaskClass.execute();
//                } else {
//                    Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
//                    return;
//                }
//            }else {
        Details_impl main_impl = new Details_impl(this);
        list_details = main_impl.getUser();
        if (list_details.size() < 1) {
            Toast.makeText(context, "No Data", Toast.LENGTH_SHORT).show();
        } else {
            ArrayList<TaskDetailCartModel> list2 = new ArrayList<TaskDetailCartModel>();
            list2.clear();
            for (int i = 0; i < list_details.size(); i++) {
                if (spn_location.getSelectedItemPosition() == 0) {
                    if (list_details.get(i).getTask_process().equalsIgnoreCase("Completed")) {

                    } else {
                        list2.add(new TaskDetailCartModel(list_details.get(i).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list_details.get(i).getTask_id()), list_details.get(i).getTask_number(), list_details.get(i).getClient_name(), list_details.get(i).getDate(), list_details.get(i).getTime(), list_details.get(i).getAddress(),
                                list_details.get(i).getLatitude(), list_details.get(i).getLongitude(), list_details.get(i).getShort_description(), list_details.get(i).getDescription(), list_details.get(i).getAcknowledged_by(), list_details.get(i).getAcknowledged_date(), list_details.get(i).getAcknowledged_time(), list_details.get(i).getImage_path(), list_details.get(i).getImage_bitmap(),
                                list_details.get(i).getTask_process(), list_details.get(i).getDisposal_site_name(), list_details.get(i).getDisposal_site_id(), list_details.get(i).getDisposal_date(), list_details.get(i).getDisposal_time(), list_details.get(i).getJob_type(), list_details.get(i).getSecond_man(), list_details.get(i).getJob_started(), list_details.get(i).getSing_off_status(), list_details.get(i).getJob_start_datetime(),
                                list_details.get(i).getTask_sync_status(), list_details.get(i).getSs1(), list_details.get(i).getSs2(), list_details.get(i).getSs2_A(), list_details.get(i).getSs3(), list_details.get(i).getFinal_sync(), list_details.get(i).getNew_rental(), list_details.get(i).getFinal_removal(), list_details.get(i).getInstructions()
                        ));
                    }
                } else if (spn_location.getSelectedItemPosition() == 1) {
                    if (list_details.get(i).getTask_process().equalsIgnoreCase("Completed") || list_details.get(i).getTask_process().equalsIgnoreCase("In-progress")) {

                    } else {
                        list2.add(new TaskDetailCartModel(list_details.get(i).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list_details.get(i).getTask_id()), list_details.get(i).getTask_number(), list_details.get(i).getClient_name(), list_details.get(i).getDate(), list_details.get(i).getTime(), list_details.get(i).getAddress(),
                                list_details.get(i).getLatitude(), list_details.get(i).getLongitude(), list_details.get(i).getShort_description(), list_details.get(i).getDescription(), list_details.get(i).getAcknowledged_by(), list_details.get(i).getAcknowledged_date(), list_details.get(i).getAcknowledged_time(), list_details.get(i).getImage_path(), list_details.get(i).getImage_bitmap(),
                                list_details.get(i).getTask_process(), list_details.get(i).getDisposal_site_name(), list_details.get(i).getDisposal_site_id(), list_details.get(i).getDisposal_date(), list_details.get(i).getDisposal_time(), list_details.get(i).getJob_type(), list_details.get(i).getSecond_man(), list_details.get(i).getJob_started(), list_details.get(i).getSing_off_status(), list_details.get(i).getJob_start_datetime(),
                                list_details.get(i).getTask_sync_status(), list_details.get(i).getSs1(), list_details.get(i).getSs2(), list_details.get(i).getSs2_A(), list_details.get(i).getSs3(), list_details.get(i).getFinal_sync(), list_details.get(i).getNew_rental(), list_details.get(i).getFinal_removal(), list_details.get(i).getInstructions()
                        ));
                    }
                } else if (spn_location.getSelectedItemPosition() == 2) {
                    if (list_details.get(i).getTask_process().equalsIgnoreCase("Completed") || list_details.get(i).getTask_process().equalsIgnoreCase("Pending")) {

                    } else {
                        list2.add(new TaskDetailCartModel(list_details.get(i).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list_details.get(i).getTask_id()), list_details.get(i).getTask_number(), list_details.get(i).getClient_name(), list_details.get(i).getDate(), list_details.get(i).getTime(), list_details.get(i).getAddress(),
                                list_details.get(i).getLatitude(), list_details.get(i).getLongitude(), list_details.get(i).getShort_description(), list_details.get(i).getDescription(), list_details.get(i).getAcknowledged_by(), list_details.get(i).getAcknowledged_date(), list_details.get(i).getAcknowledged_time(), list_details.get(i).getImage_path(), list_details.get(i).getImage_bitmap(),
                                list_details.get(i).getTask_process(), list_details.get(i).getDisposal_site_name(), list_details.get(i).getDisposal_site_id(), list_details.get(i).getDisposal_date(), list_details.get(i).getDisposal_time(), list_details.get(i).getJob_type(), list_details.get(i).getSecond_man(), list_details.get(i).getJob_started(), list_details.get(i).getSing_off_status(), list_details.get(i).getJob_start_datetime(),
                                list_details.get(i).getTask_sync_status(), list_details.get(i).getSs1(), list_details.get(i).getSs2(), list_details.get(i).getSs2_A(), list_details.get(i).getSs3(), list_details.get(i).getFinal_sync(), list_details.get(i).getNew_rental(), list_details.get(i).getFinal_removal(), list_details.get(i).getInstructions()
                        ));
                    }
                }
            }



            taskAdpater = new TaskAdpater(TaskListActivity.this, list2);
            listView.setAdapter(taskAdpater);
            taskAdpater.notifyDataSetChanged();

            Comparator<TaskDetailCartModel> comp = new Comparator<TaskDetailCartModel>() {
                @Override
                public int compare(TaskDetailCartModel c1, TaskDetailCartModel c2) {
                    return c2.getTask_process().compareTo(c1.getTask_process());
                }
            };

            Collections.sort(list2, comp);
//            if (rotateAnimation != null) {
//                if (rotateAnimation.isShowing()) {
//                    rotateAnimation.dismiss();
//                    rotateAnimation = null;
//                }
//            }

        }
//        }
//        }
    }

    @Override
    public void onTaskComplete(String result) {
        // TODO Auto-generated method stub
        Log.i(DEBUG_TAG, "response from server=" + result);
        if (key.equalsIgnoreCase("pull")) {
            if (!result.equalsIgnoreCase("")) {
                progressDoalog.dismiss();
            }
        } else if (key.equalsIgnoreCase("detail")) {
            taskDetailParser = new TaskDetailParser(context);
            taskDetailParser.JsonStringToArrayList(result);

            Gson gson2 = new Gson();
            arrDisposal.clear();
            JSONArray jsonArrayLike = null;
            try {
                jsonArrayLike = new JSONArray(taskDetailParser.GetTaskDetailList().get(0).get(WebService.KEY_TASKDETAIL_DISPOSALSITE));
                for (int i = 0; i < jsonArrayLike.length(); i++) {
                    JSONObject arrayElement_0 = jsonArrayLike.getJSONObject(i);
                    arrDisposal.add(gson2.fromJson(String.valueOf(arrayElement_0), DisposalModel.class));
                    try {
                        internalStorage.writeObject(TaskListActivity.this, "EmenitiesList", arrDisposal);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else if (key.equalsIgnoreCase("savetippingbook")) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("allids", allIds);
            //String status = jsonObject.optString(WebService.KEY_LOGIN_STATUS);
            String message = jsonObject.optString(WebService.KEY_LOGIN_MESSAGE);
            SimpleDateFormat myFormatdate = new SimpleDateFormat("yyyy-MM-dd");//Y-m-d H:i:s  2016-09-19 15:09:00
            SimpleDateFormat myFormattime = new SimpleDateFormat("HH:mm:ss");
            try {
                Date date = new Date();
                strsignoffdate = myFormatdate.format(date);
                strsignofftime = myFormattime.format(date);

                Log.i(DEBUG_TAG, "Current Date Time in give format: " + strsignoffdate + " " + strsignofftime);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (message.toString().length() > 0) {
                Toast.makeText(context, message, 1).show();
            }

            if (nameValuePair.size() > 0) {
                nameValuePair.clear();
            }

            nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, "update_status_combined"));
            nameValuePair.add(new BasicNameValuePair(WebService.KEY_UPDATETASKID, allIds));
            nameValuePair.add(new BasicNameValuePair(WebService.KEY_UPDATETASKPROCESS, "Completed"));
            nameValuePair.add(new BasicNameValuePair(WebService.KEY_SIGNOFFDATETIME, strsignoffdate + " " + strsignofftime));
            if (!txt_disposalsiteid.getText().toString().equals("")) {
                nameValuePair.add(new BasicNameValuePair(WebService.KEY_DISPOSAL_NAME, namedisposal));
                nameValuePair.add(new BasicNameValuePair(WebService.KEY_DISPOSAL_ID, txt_disposalsiteid.getText().toString()));
                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TIPPINGBOOKDATETIME, strsignoffdate + " " + strsignofftime));

            } else {
                nameValuePair.add(new BasicNameValuePair(WebService.KEY_DISPOSAL_NAME, ""));
                nameValuePair.add(new BasicNameValuePair(WebService.KEY_DISPOSAL_ID, ""));
                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TIPPINGBOOKDATETIME, strsignoffdate + " " + strsignofftime));
            }

            if (connectionDetector.isConnectingToInternet()) {
                key = "statusupdate";
                asyncTaskClass = new AsyncTaskClass(context, TaskListActivity.this, nameValuePair, WebService.SERVERURL, true);
                asyncTaskClass.execute();
            } else {
                Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
                return;
            }
        } else if (key.equalsIgnoreCase("statusupdate2")) {

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);

                String status = jsonObject.getString("status");
                String message = jsonObject.getString("message");
                String task_sync_status = jsonObject.getString("task_sync_status");
                String ss1 = jsonObject.getString("ss1");
                String ss2 = jsonObject.getString("ss2");
                String ss2_A = jsonObject.getString("ss2_A");
                String ss3 = jsonObject.getString("ss3");
                String final_sync = jsonObject.getString("final_sync");


                details_impl = new Details_impl(TaskListActivity.this);
                list_details = details_impl.getUserDetails1(list_details.get(position_status).getTask_id());
                long det = details_impl.update(new TaskDetailCartModel(list_details.get(0).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list_details.get(0).getTask_id()), list_details.get(0).getTask_number(), list_details.get(0).getClient_name(), list_details.get(0).getDate(), list_details.get(0).getTime(), list_details.get(0).getAddress(),
                        list_details.get(0).getLatitude(), list_details.get(0).getLongitude(), list_details.get(0).getShort_description(), list_details.get(0).getDescription(), list_details.get(0).getAcknowledged_by(), list_details.get(0).getAcknowledged_date(), list_details.get(0).getAcknowledged_time(), list_details.get(0).getImage_path(), list_details.get(0).getImage_bitmap(),
                        list_details.get(0).getTask_process(), list_details.get(0).getDisposal_site_name(), list_details.get(0).getDisposal_site_id(), list_details.get(0).getDisposal_date(), list_details.get(0).getDisposal_time(), list_details.get(0).getJob_type(), list_details.get(0).getSecond_man(), list_details.get(0).getJob_started(), list_details.get(0).getSing_off_status(), list_details.get(0).getJob_start_datetime(),
                        task_sync_status, ss1, ss2, ss2_A, ss3, final_sync, list_details.get(0).getNew_rental(), list_details.get(0).getFinal_removal(), list_details.get(0).getInstructions()
                ));
                globalVariable.SetTaskFinished("true");
                rotateAnimation = new RotateAnimation(context);
                rotateAnimation.show();
                CallApiSequence();


            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (key.equalsIgnoreCase("statusupdate")) {

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);

                String status = jsonObject.getString("status");
                String message = jsonObject.getString("message");
                String task_sync_status = jsonObject.getString("task_sync_status");
                String ss1 = jsonObject.getString("ss1");
                String ss2 = jsonObject.getString("ss2");
                String ss2_A = jsonObject.getString("ss2_A");
                String ss3 = jsonObject.getString("ss3");
                String final_sync = jsonObject.getString("final_sync");


                details_impl = new Details_impl(TaskListActivity.this);
                list_details = details_impl.getUserDetails1(list_details.get(position_status).getTask_id());
                long det = details_impl.update(new TaskDetailCartModel(list_details.get(position_status).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list_details.get(position_status).getTask_id()), list_details.get(position_status).getTask_number(), list_details.get(position_status).getClient_name(), list_details.get(position_status).getDate(), list_details.get(position_status).getTime(), list_details.get(position_status).getAddress(),
                        list_details.get(position_status).getLatitude(), list_details.get(position_status).getLongitude(), list_details.get(position_status).getShort_description(), list_details.get(position_status).getDescription(), list_details.get(position_status).getAcknowledged_by(), list_details.get(position_status).getAcknowledged_date(), list_details.get(position_status).getAcknowledged_time(), list_details.get(position_status).getImage_path(), list_details.get(position_status).getImage_bitmap(),
                        list_details.get(position_status).getTask_process(), list_details.get(position_status).getDisposal_site_name(), list_details.get(position_status).getDisposal_site_id(), list_details.get(position_status).getDisposal_date(), list_details.get(position_status).getDisposal_time(), list_details.get(position_status).getJob_type(), list_details.get(position_status).getSecond_man(), list_details.get(position_status).getJob_started(), list_details.get(position_status).getSing_off_status(), list_details.get(position_status).getJob_start_datetime(),
                        task_sync_status, ss1, ss2, ss2_A, ss3, final_sync, list_details.get(position_status).getNew_rental(), list_details.get(position_status).getFinal_removal(), list_details.get(position_status).getInstructions()
                ));
                globalVariable.SetTaskFinished("true");
                rotateAnimation = new RotateAnimation(context);
                rotateAnimation.show();
                CallApiSequence();


            } catch (JSONException e) {
                e.printStackTrace();
            }


            if (nameValuePair.size() > 0) {
                nameValuePair.clear();
            }
            key = "List2";
            String datetime = "";
            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//Y-m-d H:i:s  2016-09-19 15:09:00
            try {
                Date date = new Date();
                datetime = myFormat.format(date);
                Log.i(DEBUG_TAG, "Current Date Time in give format: " + datetime);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, WebService.KEY_TASKLIST_TASK_OFFLINE));
            nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASKLIST_DRIVERIDKEY, sharedPrefrenceClass.getKEY_LOGIN_USERID()));
            nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASKLIST_DATEKEY, datetime));
            arrLike.clear();
            if (connectionDetector.isConnectingToInternet()) {
                asyncTaskClass = new AsyncTaskClass(context, TaskListActivity.this, nameValuePair, WebService.SERVERURL, true);
                asyncTaskClass.execute();
            } else {
                Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
                return;
            }
        } else if (key.equalsIgnoreCase("List")) {
            try {
                TaskListParser taskListParser = new TaskListParser(context);
                taskListParser.JsonStringToArrayList(result);
                arrayList = taskListParser.GetTaskList();
                Log.i(DEBUG_TAG, "response from arraylist=" + arrayList.toString());
                if (arrayList.size() > 0) {
//				TaskListAdpater taskListAdpater = new TaskListAdpater(TaskListActivity.this, arrayList);
//				listView.setAdapter(taskListAdpater);
                } else {

                }
                details_impl = new Details_impl(TaskListActivity.this);
                details_impl.delete_table();
                list_details.clear();

                JSONArray jsonArray = new JSONArray(result);
                Gson gson2 = new Gson();
                Log.i(DEBUG_TAG, "jsonarraylengeth=" + jsonArray.length());
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
                        String instructions= arrayElement_0.getString("instructions");

                        details_impl = new Details_impl(TaskListActivity.this);
                        long det = details_impl.insert(new TaskDetailCartModel(0, sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(task_id), task_number, client_name, date, time, address,
                                latitude, longitude, short_description, description, acknowledged_by, acknowledged_date, acknowledged_time, image_path, "",
                                task_process, disposal_site_name, disposal_site_id, disposal_date, disposal_time, job_type, second_man, job_started, sing_off_status
                                , job_start_datetime, task_sync_status, ss1, ss2, ss2_A, ss3, final_sync, new_rental, final_removal,instructions
                        ));
                    }
                }
                arrDisposal.clear();
//                JSONArray jsonArrayLike = null;
//                try {
//                    jsonArrayLike = new JSONArray(arrLike.get(0).getDisposal_date());
                for (int i = 0; i < arrLike.get(0).getDisposal_site().size(); i++) {
//                        JSONObject arrayElement_0 = jsonArrayLike.getJSONObject(i);
                    arrDisposal.add(new DisposalModel(arrLike.get(0).getDisposal_site().get(i).getId(), arrLike.get(0).getDisposal_site().get(i).getName()));
//                        arrDisposal.add(gson2.fromJson(String.valueOf(arrayElement_0), DisposalModel.class));
                    try {
                        internalStorage.writeObject(TaskListActivity.this, "EmenitiesList", arrDisposal);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

                list_details = details_impl.getUser();




                taskAdpater = new TaskAdpater(TaskListActivity.this, list_details);
                listView.setAdapter(taskAdpater);


                Comparator<TaskDetailCartModel> comp = new Comparator<TaskDetailCartModel>() {
                    @Override
                    public int compare(TaskDetailCartModel c1, TaskDetailCartModel c2) {
                        return c2.getTask_process().compareTo(c1.getTask_process());
                    }
                };

                Collections.sort(list_details, comp);

//                if (internalStorage.fileExists(getApplicationContext(), "EmenitiesList")) {
//
//                } else {
//                    if (nameValuePair.size() > 0) {
//                        nameValuePair.clear();
//                    }
//
//                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, WebService.KEY_TASKDETAIL_TASK_OFFLINE));
//                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASKDETAIL_TASKIDDKEY, list_details.get(0).getTask_id()));
//
//                    if (connectionDetector.isConnectingToInternet()) {
//                        key = "detail";
//                        asyncTaskClass = new AsyncTaskClass(context, TaskListActivity.this, nameValuePair, WebService.SERVERURL, true);
//                        asyncTaskClass.execute();
//                    } else {
//                        Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
//                        return;
//                    }
//                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (key.equalsIgnoreCase("List2")) {
            try {
                TaskListParser taskListParser = new TaskListParser(context);
                taskListParser.JsonStringToArrayList(result);
                arrayList = taskListParser.GetTaskList();
                Log.i(DEBUG_TAG, "response from arraylist=" + arrayList.toString());
                if (arrayList.size() > 0) {
//				TaskListAdpater taskListAdpater = new TaskListAdpater(TaskListActivity.this, arrayList);
//				listView.setAdapter(taskListAdpater);
                } else {

                }
                details_impl = new Details_impl(TaskListActivity.this);
                details_impl.delete_table();
                list_details.clear();

                JSONArray jsonArray = new JSONArray(result);
                Gson gson2 = new Gson();
                Log.i(DEBUG_TAG, "jsonarraylengeth=" + jsonArray.length());
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
                        String instructions=arrayElement_0.getString("instructions");

                        details_impl = new Details_impl(TaskListActivity.this);
                        long det = details_impl.insert(new TaskDetailCartModel(0, sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(task_id), task_number, client_name, date, time, address,
                                latitude, longitude, short_description, description, acknowledged_by, acknowledged_date, acknowledged_time, image_path, "",
                                task_process, disposal_site_name, disposal_site_id, disposal_date, disposal_time, job_type, second_man, job_started, sing_off_status
                                , job_start_datetime, task_sync_status, ss1, ss2, ss2_A, ss3, final_sync, new_rental, final_removal,instructions
                        ));
                    }
                }


                list_details = details_impl.getUser();
                taskAdpater = new TaskAdpater(TaskListActivity.this, list_details);
                listView.setAdapter(taskAdpater);


                Comparator<TaskDetailCartModel> comp = new Comparator<TaskDetailCartModel>() {
                    @Override
                    public int compare(TaskDetailCartModel c1, TaskDetailCartModel c2) {
                        return c2.getTask_process().compareTo(c1.getTask_process());
                    }
                };

                Collections.sort(list_details, comp);

                rotateAnimation = new RotateAnimation(context);
                rotateAnimation.show();
                CallApiSequence();


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (key.equalsIgnoreCase("job_started")) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);

                String status = jsonObject.getString("status");
                String message = jsonObject.getString("message");
                String task_sync_status = jsonObject.getString("task_sync_status");
                String ss1 = jsonObject.getString("ss1");
                String ss2 = jsonObject.getString("ss2");
                String ss2_A = jsonObject.getString("ss2_A");
                String ss3 = jsonObject.getString("ss3");
                String final_sync = jsonObject.getString("final_sync");

                details_impl = new Details_impl(TaskListActivity.this);
                list_details = details_impl.getUserDetails1(String.valueOf(list_details.get(position_status).getTask_id()));
                long det = details_impl.update(new TaskDetailCartModel(list_details.get(position_status).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list_details.get(position_status).getTask_id()), list_details.get(position_status).getTask_number(), list_details.get(position_status).getClient_name(), list_details.get(position_status).getDate(), list_details.get(position_status).getTime(), list_details.get(position_status).getAddress(),
                        list_details.get(position_status).getLatitude(), list_details.get(position_status).getLongitude(), list_details.get(position_status).getShort_description(), list_details.get(position_status).getDescription(), list_details.get(position_status).getAcknowledged_by(), list_details.get(position_status).getAcknowledged_date(), list_details.get(position_status).getAcknowledged_time(), list_details.get(position_status).getImage_path(), list_details.get(position_status).getImage_bitmap(),
                        list_details.get(position_status).getTask_process(), list_details.get(position_status).getDisposal_site_name(), list_details.get(position_status).getDisposal_site_id(), list_details.get(position_status).getDisposal_date(), list_details.get(position_status).getDisposal_time(), list_details.get(position_status).getJob_type(), list_details.get(position_status).getSecond_man(), list_details.get(position_status).getJob_started(), list_details.get(position_status).getSing_off_status(), list_details.get(position_status).getJob_start_datetime(),
                        task_sync_status, ss1, ss2, ss2_A, ss3, final_sync, list_details.get(position_status).getNew_rental(), list_details.get(position_status).getFinal_removal(), list_details.get(position_status).getInstructions()
                ));
                globalVariable.SetTaskFinished("true");
                rotateAnimation = new RotateAnimation(context);
                rotateAnimation.show();
                CallApiSequence();

            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else if (key.equalsIgnoreCase("signoffstatus")) {

            details_impl = new Details_impl(TaskListActivity.this);
            list_details = details_impl.getUserDetails1(String.valueOf(list_details.get(position_status).getTask_id()));


            if (nameValuePair.size() > 0) {
                nameValuePair.clear();
            }

            nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, WebService.KEY_UPDATETASK_OFFLINE));
            nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASKDETAIL_TASKIDDKEY, list_details.get(position_status).getTask_id()));
            nameValuePair.add(new BasicNameValuePair("second_man", list_details.get(position_status).getSecond_man()));
            nameValuePair.add(new BasicNameValuePair(WebService.KEY_UPDATETASKPROCESS, list_details.get(position_status).getTask_process()));
            nameValuePair.add(new BasicNameValuePair(WebService.KEY_SIGNOFFDATETIME, list_details.get(position_status).getAcknowledged_date() + " " + list_details.get(position_status).getAcknowledged_time()));
            nameValuePair.add(new BasicNameValuePair("task_sync_status", list_details.get(position_status).getTask_sync_status()));
            nameValuePair.add(new BasicNameValuePair("ss1", list_details.get(position_status).getSs1()));
            nameValuePair.add(new BasicNameValuePair("ss2", list_details.get(position_status).getSs2()));
            nameValuePair.add(new BasicNameValuePair("ss2_A", list_details.get(position_status).getSs2_A()));
            nameValuePair.add(new BasicNameValuePair("ss3", list_details.get(position_status).getSs3()));
            nameValuePair.add(new BasicNameValuePair("final_sync", list_details.get(position_status).getFinal_sync()));
            if (!txt_disposalsiteid.getText().toString().equals("")) {
                nameValuePair.add(new BasicNameValuePair(WebService.KEY_DISPOSAL_NAME, ""));
                nameValuePair.add(new BasicNameValuePair(WebService.KEY_DISPOSAL_ID, ""));
                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TIPPINGBOOKDATETIME, list_details.get(position_status).getAcknowledged_date() + " " + list_details.get(position_status).getAcknowledged_time()));
            } else {
                nameValuePair.add(new BasicNameValuePair(WebService.KEY_DISPOSAL_NAME, ""));
                nameValuePair.add(new BasicNameValuePair(WebService.KEY_DISPOSAL_ID, ""));
                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TIPPINGBOOKDATETIME, list_details.get(position_status).getAcknowledged_date() + " " + list_details.get(position_status).getAcknowledged_time()));
            }

            if (connectionDetector.isConnectingToInternet()) {
                key = "notavailablesign";
                asyncTaskClass = new AsyncTaskClass(context, TaskListActivity.this, nameValuePair, WebService.SERVERURL, true);
                asyncTaskClass.execute();
            } else {
                Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
                return;
            }
        } else if (key.equalsIgnoreCase("notavailablesign")) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);

                String status = jsonObject.getString("status");
                String message = jsonObject.getString("message");
                String task_sync_status = jsonObject.getString("task_sync_status");
                String ss1 = jsonObject.getString("ss1");
                String ss2 = jsonObject.getString("ss2");
                String ss2_A = jsonObject.getString("ss2_A");
                String ss3 = jsonObject.getString("ss3");
                String final_sync = jsonObject.getString("final_sync");

                details_impl = new Details_impl(TaskListActivity.this);
                list_details = details_impl.getUserDetails1(String.valueOf(list_details.get(position_status).getTask_id()));
                long det = details_impl.update(new TaskDetailCartModel(list_details.get(position_status).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list_details.get(position_status).getTask_id()), list_details.get(position_status).getTask_number(), list_details.get(position_status).getClient_name(), list_details.get(position_status).getDate(), list_details.get(position_status).getTime(), list_details.get(position_status).getAddress(),
                        list_details.get(position_status).getLatitude(), list_details.get(position_status).getLongitude(), list_details.get(position_status).getShort_description(), list_details.get(position_status).getDescription(), list_details.get(position_status).getAcknowledged_by(), list_details.get(position_status).getAcknowledged_date(), list_details.get(position_status).getAcknowledged_time(), list_details.get(position_status).getImage_path(), list_details.get(position_status).getImage_bitmap(),
                        list_details.get(position_status).getTask_process(), list_details.get(position_status).getDisposal_site_name(), list_details.get(position_status).getDisposal_site_id(), list_details.get(position_status).getDisposal_date(), list_details.get(position_status).getDisposal_time(), list_details.get(position_status).getJob_type(), list_details.get(position_status).getSecond_man(), list_details.get(position_status).getJob_started(), list_details.get(position_status).getSing_off_status(), list_details.get(position_status).getJob_start_datetime(),
                        task_sync_status, ss1, ss2, ss2_A, ss3, final_sync, list_details.get(position_status).getNew_rental(), list_details.get(position_status).getFinal_removal(), list_details.get(position_status).getInstructions()
                ));

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            ArrayList<TaskListCartModel> ticket_list = new ArrayList<TaskListCartModel>();
//            Main_impl main_impl = new Main_impl(this);
//            ticket_list = main_impl.getUser();
//
//            for (int i = 0; i < ticket_list.size(); i++) {
//                if (ticket_list.get(i).getTask_id().equalsIgnoreCase(list_details.get(position_status).getTask_id())) {
//                    Main_impl main_impl2 = new Main_impl(this);
//                    ticket_list = main_impl.getUser();
//
//                    long det = main_impl2.update(new TaskListCartModel(ticket_list.get(i).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), ticket_list.get(i).getTask_id(), ticket_list.get(i).getTask_number(), ticket_list.get(i).getClient_name(), ticket_list.get(i).getAddress(),
//                            ticket_list.get(i).getShort_description(), ticket_list.get(i).getJob_type(), "In-progress"));
//
//                }
//            }
            globalVariable.SetTaskFinished("true");
            rotateAnimation = new RotateAnimation(context);
            rotateAnimation.show();
            CallApiSequence();

        }

    }

    public class TaskAdpater extends BaseAdapter {
        private Context context;
        private ArrayList<TaskDetailCartModel> rowlist;
        private ArrayList<TaskDetailCartModel> rowcountry = null;
        private SparseBooleanArray mSelectedItemsIds;
        boolean[] checkBoxState;

        public TaskAdpater(Context context, ArrayList<TaskDetailCartModel> rowlist) {
            this.context = context;
            this.rowlist = rowlist;
            this.rowcountry = new ArrayList<>();
            this.rowcountry.addAll(rowlist);
            mSelectedItemsIds = new SparseBooleanArray();
            checkBoxState = new boolean[rowlist.size()];
        }

        private class viewHolder {
            TextView txt_number, txt_number1;
            TextView txt_clientname;
            TextView txt_address;
            TextView txt_discription;
            TextView txt_jobtype;
            TextView txt_status;
            TextView txt_rental_status;
            LinearLayout layout;
            CheckBox checkBox;
            RelativeLayout rel_instrctn;

        }

        @Override
        public int getCount() {
            return rowlist.size();
        }

        @Override
        public Object getItem(int position) {
            return rowlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return rowlist.indexOf(getItem(position));
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            viewHolder holder = null;
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        String name=holder.txt_name.getText().toString();
//        String price=holder.txt_price.getText().toString();
            if (convertView == null) {

                convertView = inflater.inflate(R.layout.adapter_tasklist, null);
                holder = new viewHolder();
                holder.txt_rental_status = convertView.findViewById(R.id.txt_rental_status);
                holder.txt_number = (TextView) convertView.findViewById(R.id.txt_snumber);
                holder.txt_number1 = (TextView) convertView.findViewById(R.id.txt_snumber1);
                holder.txt_clientname = (TextView) convertView.findViewById(R.id.txt_clientname);
                holder.txt_address = (TextView) convertView.findViewById(R.id.txt_address);
                holder.txt_discription = (TextView) convertView.findViewById(R.id.txt_description);
                holder.txt_jobtype = (TextView) convertView.findViewById(R.id.txt_jobtype);
                holder.txt_status = (TextView) convertView.findViewById(R.id.txt_status);
                holder.layout = (LinearLayout) convertView.findViewById(R.id.layout_tasklist);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.check);
                holder.rel_instrctn=(RelativeLayout)convertView.findViewById(R.id.rel_instrctn);
                holder.txt_number.setText(String.valueOf(position + 1));


                convertView.setTag(holder);

            } else
                holder = (viewHolder) convertView.getTag();



            final TaskDetailCartModel list = (TaskDetailCartModel) getItem(position);
//            holder.checkBox.setTag(position);

            if (list.getNew_rental().equalsIgnoreCase("Y")) {
                holder.txt_rental_status.setText("New Rental");
            } else if (list.getFinal_removal().equalsIgnoreCase("Y")) {
                holder.txt_rental_status.setText("Final Removal");
            } else {
                holder.txt_rental_status.setVisibility(View.GONE);
            }


            if (!list.getInstructions().equalsIgnoreCase("")){
                holder.rel_instrctn.setVisibility(View.VISIBLE);
            }else{
                holder.rel_instrctn.setVisibility(View.GONE);
            }

            holder.checkBox.setChecked(checkBoxState[position]);
//            holder.checkBox.setChecked(mSelectedItemsIds.get(position));

            holder.txt_clientname.setText(list.getClient_name());
            holder.txt_address.setText(list.getAddress());
            holder.txt_discription.setText(list.getShort_description());
            holder.txt_jobtype.setText(list.getJob_type());
            holder.txt_status.setText(list.getTask_process());

            if (list.getTask_process().equalsIgnoreCase("In-progress")) {
                holder.checkBox.setVisibility(View.VISIBLE);
            } else {
                holder.checkBox.setVisibility(View.GONE);
            }

            holder.txt_number1.setText(list.getTask_number());
            if (position % 2 == 1) {
                holder.layout.setBackgroundColor(Color.WHITE);
                holder.txt_number.setBackgroundResource(R.drawable.txtshapecolor_green);
            } else {
                holder.layout.setBackgroundColor(Color.parseColor("#c1c1c1"));
                holder.txt_number.setBackgroundResource(R.drawable.txtshapecolor_blue);
            }

            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    checkCheckBox(position, !mSelectedItemsIds.get(position));
                    if (((CheckBox) v).isChecked()) {
                        checkBoxState[position] = true;
                        ids_service.add(String.valueOf(list.getTask_id()));
                    } else {
                        checkBoxState[position] = false;
                        ids_service.remove(String.valueOf(list.getTask_id()));
                    }
                }
            });

            holder.rel_instrctn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowDilagBonusInstructionMessage(context,list.getInstructions());
                }
            });

//            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if (isChecked) {
//                        ids_service.add(String.valueOf(list.getTask_id()));
//                    } else {
//                        ids_service.remove(String.valueOf(list.getTask_id()));
//                    }
//                }
//            });


            return convertView;
        }

        public void checkCheckBox(int position, boolean value) {
            if (value)
                mSelectedItemsIds.put(position, true);
            else
                mSelectedItemsIds.delete(position);

            notifyDataSetChanged();
        }

        public SparseBooleanArray getSelectedIds() {
            return mSelectedItemsIds;
        }

        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            rowlist.clear();
            if (charText.length() == 0) {
                rowlist.addAll(rowcountry);
            } else {
                for (TaskDetailCartModel wp : rowcountry) {
                    if (wp.getClient_name().toLowerCase(Locale.getDefault()).contains(charText)) {
                        rowlist.add(wp);
                    }
                }
            }
            taskAdpater.notifyDataSetChanged();
        }
    }

    private void CallApiSequence() {
        details_impl = new Details_impl(TaskListActivity.this);
        list_details = details_impl.getUser();
//

//        rotateAnimation.show();

        if (list_details.size() > f_pos) {
            CallApiSequence2(f_pos);
        } else {
            Toast.makeText(context, "All Jobs pushed", Toast.LENGTH_SHORT).show();
            if (logout_status.equalsIgnoreCase("yes")) {
                mRequestQueue = Volley.newRequestQueue(TaskListActivity.this);

                StringRequest mStringRequest = new StringRequest(Request.Method.POST, WebService.SERVERURL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (rotateAnimation != null) {
                                    if (rotateAnimation.isShowing()) {
                                        rotateAnimation.dismiss();
                                        rotateAnimation = null;
                                    }
                                }
                                Log.d("logout_resonse", response);
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response);

                                    String status = jsonObject.getString("status");
                                    if (status.equalsIgnoreCase("y")) {
                                        Intent intent = new Intent(context, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        sharedPrefrenceClass.setKEY_LOGIN_USERID("");
                                        sharedPrefrenceClass.setKEY_LOGIN_USERNAME("");
                                        sharedPrefrenceClass.setKEY_LOGIN_PASSWORD("");
                                        details_impl = new Details_impl(TaskListActivity.this);
                                        details_impl.delete_table();
                                        finish();
                                    } else {
                                        Toast.makeText(context, "Something Went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(TaskListActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                                if (rotateAnimation != null) {
                                    if (rotateAnimation.isShowing()) {
                                        rotateAnimation.dismiss();
                                        rotateAnimation = null;
                                    }
                                }
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(WebService.KEY_TASK, "logout");
                        params.put("userid", sharedPrefrenceClass.getKEY_LOGIN_USERID());

                        return params;
                    }
                };

                mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        50000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                mRequestQueue.add(mStringRequest);
            } else if (logout_status.equalsIgnoreCase("no")) {
                if (rotateAnimation != null) {
                    if (rotateAnimation.isShowing()) {
                        rotateAnimation.dismiss();
                        rotateAnimation = null;
                    }
                }
                finish();

            } else {
                RequestQueue mRequestQueue2 = Volley.newRequestQueue(TaskListActivity.this);

                StringRequest mStringRequest2 = new StringRequest(Request.Method.POST, WebService.SERVERURL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {


                                Log.d("mainactivity_resonse", response);
                                try {
                                    LoginParser loginParser = new LoginParser(context);
                                    loginParser.JsonStringToArrayList(response);

                                    String status = loginParser.Getstatus();

                                    if (status.equalsIgnoreCase("y")) {

                                        f_pos = 0;

                                        arrLike.clear();
                                        mRequestQueue = Volley.newRequestQueue(TaskListActivity.this);

                                        StringRequest mStringRequest = new StringRequest(Request.Method.POST, WebService.SERVERURL,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        if (rotateAnimation != null) {
                                                            if (rotateAnimation.isShowing()) {
                                                                rotateAnimation.dismiss();
                                                                rotateAnimation = null;
                                                            }
                                                        }
                                                        globalVariable.SetComplete("Y");
                                                        Log.d("logout_resonse", response);
                                                        try {
                                                            details_impl = new Details_impl(TaskListActivity.this);
                                                            details_impl.delete_table();
                                                            list_details.clear();

                                                            JSONArray jsonArray = new JSONArray(response);
                                                            Gson gson2 = new Gson();
                                                            Log.i(DEBUG_TAG, "jsonarraylengeth=" + jsonArray.length());
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
                                                                    String instructions=arrayElement_0.getString("instructions");

                                                                    details_impl = new Details_impl(TaskListActivity.this);
                                                                    long det = details_impl.insert(new TaskDetailCartModel(0, sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(task_id), task_number, client_name, date, time, address,
                                                                            latitude, longitude, short_description, description, acknowledged_by, acknowledged_date, acknowledged_time, image_path, "",
                                                                            task_process, disposal_site_name, disposal_site_id, disposal_date, disposal_time, job_type, second_man, job_started, sing_off_status
                                                                            , job_start_datetime, task_sync_status, ss1, ss2, ss2_A, ss3, final_sync, new_rental, final_removal,instructions
                                                                    ));
                                                                }
                                                            }
                                                            arrDisposal.clear();
                                                            for (int i = 0; i < arrLike.get(0).getDisposal_site().size(); i++) {
                                                                arrDisposal.add(new DisposalModel(arrLike.get(0).getDisposal_site().get(i).getId(), arrLike.get(0).getDisposal_site().get(i).getName()));
                                                                try {
                                                                    internalStorage.writeObject(TaskListActivity.this, "EmenitiesList", arrDisposal);
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }

                                                            list_details = details_impl.getUser();

                                                            if (list_details.size() < 1) {
                                                                Toast.makeText(context, "No Data", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                ArrayList<TaskDetailCartModel> list2 = new ArrayList<TaskDetailCartModel>();
                                                                list2.clear();
                                                                for (int i = 0; i < list_details.size(); i++) {
                                                                    if (job_status_type == 0) {
                                                                        if (list_details.get(i).getTask_process().equalsIgnoreCase("Completed")) {

                                                                        } else {
                                                                            list2.add(new TaskDetailCartModel(list_details.get(i).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list_details.get(i).getTask_id()), list_details.get(i).getTask_number(), list_details.get(i).getClient_name(), list_details.get(i).getDate(), list_details.get(i).getTime(), list_details.get(i).getAddress(),
                                                                                    list_details.get(i).getLatitude(), list_details.get(i).getLongitude(), list_details.get(i).getShort_description(), list_details.get(i).getDescription(), list_details.get(i).getAcknowledged_by(), list_details.get(i).getAcknowledged_date(), list_details.get(i).getAcknowledged_time(), list_details.get(i).getImage_path(), list_details.get(i).getImage_bitmap(),
                                                                                    list_details.get(i).getTask_process(), list_details.get(i).getDisposal_site_name(), list_details.get(i).getDisposal_site_id(), list_details.get(i).getDisposal_date(), list_details.get(i).getDisposal_time(), list_details.get(i).getJob_type(), list_details.get(i).getSecond_man(), list_details.get(i).getJob_started(), list_details.get(i).getSing_off_status(), list_details.get(i).getJob_start_datetime(),
                                                                                    list_details.get(i).getTask_sync_status(), list_details.get(i).getSs1(), list_details.get(i).getSs2(), list_details.get(i).getSs2_A(), list_details.get(i).getSs3(), list_details.get(i).getFinal_sync(), list_details.get(i).getNew_rental(), list_details.get(i).getFinal_removal(), list_details.get(i).getInstructions()
                                                                            ));
                                                                        }
                                                                    } else if (job_status_type == 1) {
                                                                        if (list_details.get(i).getTask_process().equalsIgnoreCase("Completed") || list_details.get(i).getTask_process().equalsIgnoreCase("In-progress")) {

                                                                        } else {
                                                                            list2.add(new TaskDetailCartModel(list_details.get(i).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list_details.get(i).getTask_id()), list_details.get(i).getTask_number(), list_details.get(i).getClient_name(), list_details.get(i).getDate(), list_details.get(i).getTime(), list_details.get(i).getAddress(),
                                                                                    list_details.get(i).getLatitude(), list_details.get(i).getLongitude(), list_details.get(i).getShort_description(), list_details.get(i).getDescription(), list_details.get(i).getAcknowledged_by(), list_details.get(i).getAcknowledged_date(), list_details.get(i).getAcknowledged_time(), list_details.get(i).getImage_path(), list_details.get(i).getImage_bitmap(),
                                                                                    list_details.get(i).getTask_process(), list_details.get(i).getDisposal_site_name(), list_details.get(i).getDisposal_site_id(), list_details.get(i).getDisposal_date(), list_details.get(i).getDisposal_time(), list_details.get(i).getJob_type(), list_details.get(i).getSecond_man(), list_details.get(i).getJob_started(), list_details.get(i).getSing_off_status(), list_details.get(i).getJob_start_datetime(),
                                                                                    list_details.get(i).getTask_sync_status(), list_details.get(i).getSs1(), list_details.get(i).getSs2(), list_details.get(i).getSs2_A(), list_details.get(i).getSs3(), list_details.get(i).getFinal_sync(), list_details.get(i).getNew_rental(), list_details.get(i).getFinal_removal(), list_details.get(i).getInstructions()
                                                                            ));
                                                                        }
                                                                    } else if (job_status_type == 2) {
                                                                        if (list_details.get(i).getTask_process().equalsIgnoreCase("Completed") || list_details.get(i).getTask_process().equalsIgnoreCase("Pending")) {

                                                                        } else {
                                                                            list2.add(new TaskDetailCartModel(list_details.get(i).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list_details.get(i).getTask_id()), list_details.get(i).getTask_number(), list_details.get(i).getClient_name(), list_details.get(i).getDate(), list_details.get(i).getTime(), list_details.get(i).getAddress(),
                                                                                    list_details.get(i).getLatitude(), list_details.get(i).getLongitude(), list_details.get(i).getShort_description(), list_details.get(i).getDescription(), list_details.get(i).getAcknowledged_by(), list_details.get(i).getAcknowledged_date(), list_details.get(i).getAcknowledged_time(), list_details.get(i).getImage_path(), list_details.get(i).getImage_bitmap(),
                                                                                    list_details.get(i).getTask_process(), list_details.get(i).getDisposal_site_name(), list_details.get(i).getDisposal_site_id(), list_details.get(i).getDisposal_date(), list_details.get(i).getDisposal_time(), list_details.get(i).getJob_type(), list_details.get(i).getSecond_man(), list_details.get(i).getJob_started(), list_details.get(i).getSing_off_status(), list_details.get(i).getJob_start_datetime(),
                                                                                    list_details.get(i).getTask_sync_status(), list_details.get(i).getSs1(), list_details.get(i).getSs2(), list_details.get(i).getSs2_A(), list_details.get(i).getSs3(), list_details.get(i).getFinal_sync(), list_details.get(i).getNew_rental(), list_details.get(i).getFinal_removal(), list_details.get(i).getInstructions()
                                                                            ));
                                                                        }
                                                                    }
                                                                }

                                                                taskAdpater = new TaskAdpater(TaskListActivity.this, list2);
                                                                listView.setAdapter(taskAdpater);
                                                                taskAdpater.notifyDataSetChanged();


                                                                Comparator<TaskDetailCartModel> comp = new Comparator<TaskDetailCartModel>() {
                                                                    @Override
                                                                    public int compare(TaskDetailCartModel c1, TaskDetailCartModel c2) {
                                                                        return c2.getTask_process().compareTo(c1.getTask_process());
                                                                    }
                                                                };

                                                                Collections.sort(list2, comp);
                                                            }

//                                    taskAdpater = new TaskAdpater(TaskListActivity.this, list_details);
//                                    listView.setAdapter(taskAdpater);

                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Toast.makeText(TaskListActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                                                        if (rotateAnimation != null) {
                                                            if (rotateAnimation.isShowing()) {
                                                                rotateAnimation.dismiss();
                                                                rotateAnimation = null;
                                                            }
                                                        }
                                                    }
                                                }) {
                                            @Override
                                            protected Map<String, String> getParams() {
                                                Map<String, String> params = new HashMap<String, String>();
                                                String datetime = "";
                                                SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//Y-m-d H:i:s  2016-09-19 15:09:00
                                                try {
                                                    Date date = new Date();
                                                    datetime = myFormat.format(date);
                                                    Log.i(DEBUG_TAG, "Current Date Time in give format: " + datetime);
                                                } catch (ParseException e) {
                                                    // TODO Auto-generated catch block
                                                    e.printStackTrace();
                                                }
                                                params.put(WebService.KEY_TASK, WebService.KEY_TASKLIST_TASK_OFFLINE);
                                                params.put(WebService.KEY_TASKLIST_DRIVERIDKEY, sharedPrefrenceClass.getKEY_LOGIN_USERID());
                                                params.put(WebService.KEY_TASKLIST_DATEKEY, datetime);
//                        params.put(WebService.KEY_TASKLIST_DATEKEY, "2019-12-23 15:09:00");

                                                return params;
                                            }
                                        };

                                        mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                                50000,
                                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                                        mRequestQueue.add(mStringRequest);
                                    } else {
                                        Toast.makeText(context, loginParser.Getmessage(), 1).show();
                                        if (rotateAnimation != null) {
                                            if (rotateAnimation.isShowing()) {
                                                rotateAnimation.dismiss();
                                                rotateAnimation = null;
                                            }
                                        }
                                        finish();
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    if (rotateAnimation != null) {
                                        if (rotateAnimation.isShowing()) {
                                            rotateAnimation.dismiss();
                                            rotateAnimation = null;
                                        }
                                    }
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
//												rotateAnimation.dismiss();
                                Toast.makeText(TaskListActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                                if (rotateAnimation != null) {
                                    if (rotateAnimation.isShowing()) {
                                        rotateAnimation.dismiss();
                                        rotateAnimation = null;
                                    }
                                }
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(WebService.KEY_TASK, WebService.KEY_LOGIN_TASK_OFFLINE);
                        params.put(WebService.KEY_LOGIN_USERNAMEKEY, sharedPrefrenceClass.getKEY_LOGIN_USERNAME());
                        params.put(WebService.KEY_LOGIN_PASSWORDKEY, sharedPrefrenceClass.getKEY_LOGIN_PASSWORD());
                        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                        if (pref.getString("regId", null) != null && !pref.getString("regId", null).isEmpty() && !pref.getString("regId", null).equals("null")) {
                            params.put("devicetoken", pref.getString("regId", null));
                        } else {
                            params.put("devicetoken", "");
                        }


                        return params;
                    }
                };

                mStringRequest2.setRetryPolicy(new DefaultRetryPolicy(
                        50000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                mRequestQueue2.add(mStringRequest2);

            }
        }


//        for (int i = 0; i < list_details.size(); i++) {
//            details_impl = new Details_impl(TaskListActivity.this);
//            list_details = details_impl.getUser();
//            globalVariable.SetTaskFinished("false");
//            if (list_details.get(i).getSs1().equalsIgnoreCase("1")) {
//                mRequestQueue = Volley.newRequestQueue(TaskListActivity.this);
//
//                final int finalI = i;
//                final int position_status=i;
//                StringRequest mStringRequest = new StringRequest(Request.Method.POST, WebService.SERVERURL,
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                Log.d("mainactivity_resonse", response);
//                                JSONObject jsonObject = null;
//                                try {
//                                    jsonObject = new JSONObject(response);
//
//                                    String status = jsonObject.getString("status");
//                                    String message = jsonObject.getString("message");
//                                    String task_sync_status = jsonObject.getString("task_sync_status");
//                                    String ss1 = jsonObject.getString("ss1");
//                                    String ss2 = jsonObject.getString("ss2");
//                                    String ss2_A = jsonObject.getString("ss2_A");
//                                    String ss3 = jsonObject.getString("ss3");
//                                    String final_sync = jsonObject.getString("final_sync");
//
//                                    details_impl = new Details_impl(TaskListActivity.this);
//                                    list_details = details_impl.getUserDetails1(String.valueOf(list_details.get(position_status).getTask_id()));
//                                    long det = details_impl.update(new TaskDetailCartModel(list_details.get(position_status).getId(), String.valueOf(list_details.get(position_status).getTask_id()), list_details.get(position_status).getTask_number(), list_details.get(position_status).getClient_name(), list_details.get(position_status).getDate(), list_details.get(position_status).getTime(), list_details.get(position_status).getAddress(),
//                                            list_details.get(position_status).getLatitude(), list_details.get(position_status).getLongitude(), list_details.get(position_status).getShort_description(), list_details.get(position_status).getDescription(), list_details.get(position_status).getAcknowledged_by(), list_details.get(position_status).getAcknowledged_date(), list_details.get(position_status).getAcknowledged_time(), list_details.get(position_status).getImage_path(),
//                                            list_details.get(position_status).getTask_process(), list_details.get(position_status).getDisposal_site_name(), list_details.get(position_status).getDisposal_site_id(), list_details.get(position_status).getDisposal_date(), list_details.get(position_status).getDisposal_time(), list_details.get(position_status).getJob_type(), list_details.get(position_status).getSecond_man(), list_details.get(position_status).getJob_started(), list_details.get(position_status).getSing_off_status(), list_details.get(position_status).getJob_start_datetime(),
//                                            task_sync_status, ss1, ss2,ss2_A, ss3, final_sync
//                                    ));
//                                    globalVariable.SetTaskFinished("true");
//                                    CallApiSequence();
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                Toast.makeText(TaskListActivity.this, error.toString(), Toast.LENGTH_LONG).show();
//                            }
//                        }) {
//                    @Override
//                    protected Map<String, String> getParams() {
//                        Map<String, String> params = new HashMap<String, String>();
//                        params.put(WebService.KEY_TASK, "job_started_offline");
//                        params.put(WebService.KEY_TIPPINGBOOTASKID, list_details.get(finalI).getTask_id());
//                        params.put("second_man", list_details.get(finalI).getSecond_man());
//                        params.put("job_start_datetime", list_details.get(finalI).getJob_start_datetime());
//                        params.put("task_sync_status",list_details.get(finalI).getTask_sync_status());
//                        params.put("ss1", list_details.get(finalI).getSs1());
//                        params.put("ss2", list_details.get(finalI).getSs2());
//                        params.put("ss2_A", list_details.get(finalI).getSs2_A());
//                        params.put("ss3", list_details.get(finalI).getSs3());
//                        params.put("final_sync", list_details.get(finalI).getFinal_sync());
//
//
//                        return params;
//                    }
//                };
//
//                mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                        10000,
//                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//                mRequestQueue.add(mStringRequest);
//
////                if (nameValuePair.size() > 0) {
////                    nameValuePair.clear();
////                }
////
////                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, "job_started_offline"));
////                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TIPPINGBOOTASKID, list_details.get(i).getTask_id()));
////                nameValuePair.add(new BasicNameValuePair("second_man", list_details.get(i).getSecond_man()));
////                nameValuePair.add(new BasicNameValuePair("job_start_datetime", list_details.get(i).getJob_start_datetime()));
////                nameValuePair.add(new BasicNameValuePair("task_sync_status",list_details.get(i).getTask_sync_status()));
////                nameValuePair.add(new BasicNameValuePair("ss1", list_details.get(i).getSs1()));
////                nameValuePair.add(new BasicNameValuePair("ss2", list_details.get(i).getSs2()));
////                nameValuePair.add(new BasicNameValuePair("ss2_A", list_details.get(i).getSs2_A()));
////                nameValuePair.add(new BasicNameValuePair("ss3", list_details.get(i).getSs3()));
////                nameValuePair.add(new BasicNameValuePair("final_sync", list_details.get(i).getFinal_sync()));
////
////                if (connectionDetector.isConnectingToInternet()) {
////                    key = "job_started";
////                    position_status=i;
////                    asyncTaskClass = new AsyncTaskClass(context, TaskListActivity.this, nameValuePair, WebService.SERVERURL, true);
////                    asyncTaskClass.execute();
////                } else {
////                    Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
////                    return;
////                }
//            } else if (list_details.get(i).getSs2().equalsIgnoreCase("1")) {
//                    new SubmitRoadSideAssistDataAsyTask(i).execute();
//            }else if (list_details.get(i).getSs2_A().equalsIgnoreCase("1")) {
//                position_status=i;
//                final int finalI1 = i;
//                final int position_status= i;
//                StringRequest mStringRequest = new StringRequest(Request.Method.POST, WebService.SERVERURL,
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                Log.d("mainactivity_resonse", response);
//                                details_impl = new Details_impl(TaskListActivity.this);
//                                list_details = details_impl.getUserDetails1(String.valueOf(list_details.get(position_status).getTask_id()));
//                                mRequestQueue = Volley.newRequestQueue(TaskListActivity.this);
//
//                                StringRequest mStringRequest = new StringRequest(Request.Method.POST, WebService.SERVERURL,
//                                        new Response.Listener<String>() {
//                                            @Override
//                                            public void onResponse(String response) {
//                                                Log.d("mainactivity_resonse", response);
//                                                JSONObject jsonObject = null;
//                                                try {
//                                                    jsonObject = new JSONObject(response);
//
//                                                    String status = jsonObject.getString("status");
//                                                    String message = jsonObject.getString("message");
//                                                    String task_sync_status = jsonObject.getString("task_sync_status");
//                                                    String ss1 = jsonObject.getString("ss1");
//                                                    String ss2 = jsonObject.getString("ss2");
//                                                    String ss2_A = jsonObject.getString("ss2_A");
//                                                    String ss3 = jsonObject.getString("ss3");
//                                                    String final_sync = jsonObject.getString("final_sync");
//
//                                                    details_impl = new Details_impl(TaskListActivity.this);
//                                                    list_details = details_impl.getUserDetails1(String.valueOf(list_details.get(position_status).getTask_id()));
//                                                    long det = details_impl.update(new TaskDetailCartModel(list_details.get(position_status).getId(), String.valueOf(list_details.get(position_status).getTask_id()), list_details.get(position_status).getTask_number(), list_details.get(position_status).getClient_name(), list_details.get(position_status).getDate(), list_details.get(position_status).getTime(), list_details.get(position_status).getAddress(),
//                                                            list_details.get(position_status).getLatitude(), list_details.get(position_status).getLongitude(), list_details.get(position_status).getShort_description(), list_details.get(position_status).getDescription(), list_details.get(position_status).getAcknowledged_by(), list_details.get(position_status).getAcknowledged_date(), list_details.get(position_status).getAcknowledged_time(), list_details.get(position_status).getImage_path(),
//                                                            list_details.get(position_status).getTask_process(), list_details.get(position_status).getDisposal_site_name(), list_details.get(position_status).getDisposal_site_id(), list_details.get(position_status).getDisposal_date(), list_details.get(position_status).getDisposal_time(), list_details.get(position_status).getJob_type(), list_details.get(position_status).getSecond_man(), list_details.get(position_status).getJob_started(), list_details.get(position_status).getSing_off_status(), list_details.get(position_status).getJob_start_datetime(),
//                                                            task_sync_status,ss1, ss2, ss2_A, ss3, final_sync
//                                                    ));
//
//                                                } catch (JSONException e) {
//                                                    e.printStackTrace();
//                                                }
//
//                                                ArrayList<TaskListCartModel> ticket_list = new ArrayList<TaskListCartModel>();
//                                                Main_impl main_impl = new Main_impl(TaskListActivity.this);
//                                                ticket_list = main_impl.getUser();
//
//                                                for (int i = 0; i < ticket_list.size(); i++) {
//                                                    if (ticket_list.get(i).getTask_id().equalsIgnoreCase(list_details.get(position_status).getTask_id())) {
//                                                        Main_impl main_impl2 = new Main_impl(TaskListActivity.this);
//                                                        ticket_list = main_impl.getUser();
//
//                                                        long det = main_impl2.update(new TaskListCartModel(ticket_list.get(i).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), ticket_list.get(i).getTask_id(), ticket_list.get(i).getTask_number(), ticket_list.get(i).getClient_name(), ticket_list.get(i).getAddress(),
//                                                                ticket_list.get(i).getShort_description(), ticket_list.get(i).getJob_type(), "In-progress"));
//
//                                                    }
//                                                }
//                                                globalVariable.SetTaskFinished("true");
////                                                CallApiSequence();
//                                            }
//                                        },
//                                        new Response.ErrorListener() {
//                                            @Override
//                                            public void onErrorResponse(VolleyError error) {
//                                                Toast.makeText(TaskListActivity.this, error.toString(), Toast.LENGTH_LONG).show();
//                                            }
//                                        }) {
//                                    @Override
//                                    protected Map<String, String> getParams() {
//                                        Map<String, String> params = new HashMap<String, String>();
//                                        params.put(WebService.KEY_TASK, WebService.KEY_UPDATETASK_OFFLINE);
//                                        params.put(WebService.KEY_TASKDETAIL_TASKIDDKEY, list_details.get(position_status).getTask_id());
//                                        params.put("second_man", list_details.get(position_status).getSecond_man());
//                                        params.put(WebService.KEY_UPDATETASKPROCESS, list_details.get(position_status).getTask_process());
//                                        params.put(WebService.KEY_SIGNOFFDATETIME, list_details.get(position_status).getAcknowledged_date() + " " + list_details.get(position_status).getAcknowledged_time());
//                                        params.put("task_sync_status", list_details.get(position_status).getTask_sync_status());
//                                        params.put("ss1", list_details.get(position_status).getSs1());
//                                        params.put("ss2", list_details.get(position_status).getSs2());
//                                        params.put("ss2_A", list_details.get(position_status).getSs2_A());
//                                        params.put("ss3", list_details.get(position_status).getSs3());
//                                        params.put("final_sync", list_details.get(position_status).getFinal_sync());
//                                        if (!txt_disposalsiteid.getText().toString().equals("")) {
//                                            params.put(WebService.KEY_DISPOSAL_NAME, "");
//                                            params.put(WebService.KEY_DISPOSAL_ID, "");
//                                            params.put(WebService.KEY_TIPPINGBOOKDATETIME, list_details.get(position_status).getAcknowledged_date() + " " + list_details.get(position_status).getAcknowledged_time());
//                                        } else {
//                                            params.put(WebService.KEY_DISPOSAL_NAME, "");
//                                            params.put(WebService.KEY_DISPOSAL_ID, "");
//                                            params.put(WebService.KEY_TIPPINGBOOKDATETIME, list_details.get(position_status).getAcknowledged_date() + " " + list_details.get(position_status).getAcknowledged_time());
//                                        }
//
//
//                                        return params;
//                                    }
//                                };
//
//                                mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                                        10000,
//                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//                                mRequestQueue.add(mStringRequest);
//
//
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                Toast.makeText(TaskListActivity.this, error.toString(), Toast.LENGTH_LONG).show();
//                            }
//                        }) {
//                    @Override
//                    protected Map<String, String> getParams() {
//                        Map<String, String> params = new HashMap<String, String>();
//                        params.put(WebService.KEY_TASK, WebService.KEY_SIGN_OFFTASK_OFFLINE);
//                        params.put(WebService.KEY_UPDATETASKID, list_details.get(finalI1).getTask_id());
//                        params.put(WebService.KEY_SIGN_OFF_STATUS, list_details.get(finalI1).getSing_off_status());
//                        params.put("second_man", list_details.get(finalI1).getSecond_man());
//
//                        return params;
//                    }
//                };
//
//                mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                        10000,
//                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//                mRequestQueue.add(mStringRequest);
////                    if (nameValuePair.size() > 0) {
////                        nameValuePair.clear();
////                    }
////                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, WebService.KEY_SIGN_OFFTASK_OFFLINE));
////                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_UPDATETASKID, list_details.get(i).getTask_id()));
////                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_SIGN_OFF_STATUS, list_details.get(i).getSing_off_status()));
////                    nameValuePair.add(new BasicNameValuePair("second_man", list_details.get(i).getSecond_man()));
////
////                    if (connectionDetector.isConnectingToInternet()) {
////                        key = "signoffstatus";
////                        position_status=i;
////                        asyncTaskClass = new AsyncTaskClass(context, TaskListActivity.this, nameValuePair, WebService.SERVERURL, true);
////                        asyncTaskClass.execute();
////                    } else {
////                        Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
////                        return;
////                    }
//            }else if (list_details.get(i).getSs3().equalsIgnoreCase("1")) {
//                mRequestQueue = Volley.newRequestQueue(TaskListActivity.this);
//
//                final int finalI = i;
//                position_status=i;
//                final int finalI2 = i;
//                final int position_status = i;
//                StringRequest mStringRequest = new StringRequest(Request.Method.POST, WebService.SERVERURL,
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                Log.d("mainactivity_resonse", response);
//                                JSONObject jsonObject = null;
//                                try {
//                                    jsonObject = new JSONObject(response);
//
//                                    String status = jsonObject.getString("status");
//                                    String message = jsonObject.getString("message");
//                                    String task_sync_status = jsonObject.getString("task_sync_status");
//                                    String ss1 = jsonObject.getString("ss1");
//                                    String ss2 = jsonObject.getString("ss2");
//                                    String ss2_A = jsonObject.getString("ss2_A");
//                                    String ss3 = jsonObject.getString("ss3");
//                                    String final_sync = jsonObject.getString("final_sync");
//
//
//                                    details_impl = new Details_impl(TaskListActivity.this);
//                                    list_details = details_impl.getUserDetails1(list_details.get(finalI2).getTask_id());
//                                    long det = details_impl.update(new TaskDetailCartModel(list_details.get(position_status).getId(), String.valueOf(list_details.get(position_status).getTask_id()), list_details.get(position_status).getTask_number(), list_details.get(position_status).getClient_name(), list_details.get(position_status).getDate(), list_details.get(position_status).getTime(), list_details.get(position_status).getAddress(),
//                                            list_details.get(position_status).getLatitude(), list_details.get(position_status).getLongitude(), list_details.get(position_status).getShort_description(), list_details.get(position_status).getDescription(), list_details.get(position_status).getAcknowledged_by(), list_details.get(position_status).getAcknowledged_date(), list_details.get(position_status).getAcknowledged_time(), list_details.get(position_status).getImage_path(),
//                                            list_details.get(position_status).getTask_process(), list_details.get(position_status).getDisposal_site_name(), list_details.get(position_status).getDisposal_site_id(), list_details.get(position_status).getDisposal_date(), list_details.get(position_status).getDisposal_time(), list_details.get(position_status).getJob_type(), list_details.get(position_status).getSecond_man(), list_details.get(position_status).getJob_started(), list_details.get(position_status).getSing_off_status(), list_details.get(position_status).getJob_start_datetime(),
//                                            task_sync_status, ss1, ss2,ss2_A, ss3, final_sync
//                                    ));
//                                    globalVariable.SetTaskFinished("true");
////                                    CallApiSequence();
//
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                Toast.makeText(TaskListActivity.this, error.toString(), Toast.LENGTH_LONG).show();
//                            }
//                        }) {
//                    @Override
//                    protected Map<String, String> getParams() {
//                        Map<String, String> params = new HashMap<String, String>();
//                        params.put(WebService.KEY_TASK, "update_status_combined_offline");
//                        params.put(WebService.KEY_UPDATETASKID, list_details.get(finalI2).getTask_id());
//                        params.put(WebService.KEY_UPDATETASKPROCESS, "Completed");
//                        params.put(WebService.KEY_SIGNOFFDATETIME, list_details.get(finalI2).getDisposal_date() + " " + list_details.get(finalI2).getDisposal_time());
//                        params.put("task_sync_status", list_details.get(finalI2).getTask_sync_status());
//                        params.put("ss1", list_details.get(finalI2).getSs1());
//                        params.put("ss2", list_details.get(finalI2).getSs2());
//                        params.put("ss2_A", list_details.get(finalI2).getSs2_A());
//                        params.put("ss3", list_details.get(finalI2).getSs3());
//                        params.put("final_sync", list_details.get(finalI2).getFinal_sync());
//                        if (!txt_disposalsiteid.getText().toString().equals("")) {
//                            params.put(WebService.KEY_DISPOSAL_NAME, list_details.get(finalI2).getDisposal_site_name());
//                            params.put(WebService.KEY_DISPOSAL_ID, list_details.get(finalI2).getDisposal_site_id());
//                            params.put(WebService.KEY_TIPPINGBOOKDATETIME, list_details.get(finalI2).getDisposal_date() + " " + list_details.get(finalI2).getDisposal_time());
//
//                        } else {
//                            params.put(WebService.KEY_DISPOSAL_NAME, "");
//                            params.put(WebService.KEY_DISPOSAL_ID, "");
//                            params.put(WebService.KEY_TIPPINGBOOKDATETIME, list_details.get(finalI2).getDisposal_date() + " " + list_details.get(finalI2).getDisposal_time());
//                        }
//
//
//                        return params;
//                    }
//                };
//
//                mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                        10000,
//                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//                mRequestQueue.add(mStringRequest);
//
////                if (nameValuePair.size() > 0) {
////                    nameValuePair.clear();
////                }
////
////                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, "update_status_combined_offline"));
////                nameValuePair.add(new BasicNameValuePair(WebService.KEY_UPDATETASKID, list_details.get(i).getTask_id()));
////                nameValuePair.add(new BasicNameValuePair(WebService.KEY_UPDATETASKPROCESS, "Completed"));
////                nameValuePair.add(new BasicNameValuePair(WebService.KEY_SIGNOFFDATETIME, list_details.get(i).getDisposal_date() + " " + list_details.get(i).getDisposal_time()));
////                nameValuePair.add(new BasicNameValuePair("task_sync_status", list_details.get(0).getTask_sync_status()));
////                nameValuePair.add(new BasicNameValuePair("ss1", list_details.get(0).getSs1()));
////                nameValuePair.add(new BasicNameValuePair("ss2", list_details.get(0).getSs2()));
////                nameValuePair.add(new BasicNameValuePair("ss2_A", list_details.get(0).getSs2_A()));
////                nameValuePair.add(new BasicNameValuePair("ss3", list_details.get(0).getSs3()));
////                nameValuePair.add(new BasicNameValuePair("final_sync", list_details.get(0).getFinal_sync()));
////                if (!txt_disposalsiteid.getText().toString().equals("")) {
////                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_DISPOSAL_NAME, list_details.get(i).getDisposal_site_name()));
////                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_DISPOSAL_ID, list_details.get(i).getDisposal_site_id()));
////                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_TIPPINGBOOKDATETIME, list_details.get(i).getDisposal_date() + " " + list_details.get(i).getDisposal_time()));
////
////                } else {
////                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_DISPOSAL_NAME, ""));
////                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_DISPOSAL_ID, ""));
////                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_TIPPINGBOOKDATETIME, list_details.get(i).getDisposal_date() + " " + list_details.get(i).getDisposal_time()));
////                }
////
////                if (connectionDetector.isConnectingToInternet()) {
////                    key = "statusupdate";
////                    position_status=i;
////                    asyncTaskClass = new AsyncTaskClass(context, TaskListActivity.this, nameValuePair, WebService.SERVERURL, true);
////                    asyncTaskClass.execute();
////                } else {
////                    Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
////                    return;
////                }
//
//            }
////            while(!globalVariable.GetTaskFinished().equalsIgnoreCase("true")){ }
//        }


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
                details_impl = new Details_impl(TaskListActivity.this);
                list_details = details_impl.getUser();

                byte[] decodedBytes = Base64.decode(list_details.get(ii).getImage_bitmap(), 0);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);

                Log.i(DEBUG_TAG, "bytearraylenghth = " + bos.toByteArray().length);
                ContentBody contentPart = new ByteArrayBody(bos.toByteArray(), "image/jpeg", "files" + 1 + ".jpg");
                reqEntity.addPart("attachment", contentPart);
                Log.i(DEBUG_TAG, "image path:" + contentPart.toString());


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
                Log.i(DEBUG_TAG, "Response: " + s.toString());
                str_responsefromserver = s.toString();
                //struservalidateserverres = s.toString();

            }
            catch (Exception e) {
                e.printStackTrace();
                Log.e(e.getClass().getName(), e.getMessage(), e.getCause());
                details_impl = new Details_impl(TaskListActivity.this);
                list_details = details_impl.getUser();

                RequestQueue mRequestQueue2 = Volley.newRequestQueue(TaskListActivity.this);

                StringRequest mStringRequest2 = new StringRequest(Request.Method.POST, WebService.SERVERURL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {

                                    //09-21 18:49:22.643: I/TaskDetailActivity(2161): Response: {"status":"1","message":"Saved successfully"}
//                                    JSONObject jsonObject = new JSONObject(str_responsefromserver);
                                    JSONObject jsonObject = new JSONObject(response);
                                    //String status = jsonObject.optString(WebService.KEY_LOGIN_STATUS);
                                    String message = jsonObject.optString(WebService.KEY_LOGIN_MESSAGE);
//                                    if (message.toString().length() > 0) {
//                                        Toast.makeText(context, message, 1).show();
//                                    }
                                    details_impl = new Details_impl(TaskListActivity.this);
                                    list_details = details_impl.getUser();

                                    //Picasso.with(context).load(str_signature).fit().into(image_signoffshow);

                                    mRequestQueue = Volley.newRequestQueue(context);

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

                                                        details_impl = new Details_impl(context);
                                                        list_details = details_impl.getUserDetails1(list_details.get(ii).getTask_id());
                                                        long det = details_impl.update(new TaskDetailCartModel(list_details.get(0).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list_details.get(0).getTask_id()), list_details.get(0).getTask_number(), list_details.get(0).getClient_name(), list_details.get(0).getDate(), list_details.get(0).getTime(), list_details.get(0).getAddress(),
                                                                list_details.get(0).getLatitude(), list_details.get(0).getLongitude(), list_details.get(0).getShort_description(), list_details.get(0).getDescription(), list_details.get(0).getAcknowledged_by(), list_details.get(0).getAcknowledged_date(), list_details.get(0).getAcknowledged_time(), list_details.get(0).getImage_path(), list_details.get(0).getImage_bitmap(),
                                                                list_details.get(0).getTask_process(), list_details.get(0).getDisposal_site_name(), list_details.get(0).getDisposal_site_id(), list_details.get(0).getDisposal_date(), list_details.get(0).getDisposal_time(), list_details.get(0).getJob_type(), list_details.get(0).getSecond_man(), list_details.get(0).getJob_started(), list_details.get(0).getSing_off_status(), list_details.get(0).getJob_start_datetime(),
                                                                task_sync_status, ss1, ss2, ss2_A, ss3, final_sync, list_details.get(0).getNew_rental(), list_details.get(0).getFinal_removal(), list_details.get(0).getInstructions()
                                                        ));


                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    CallApiSequence();

                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                                                    CallApiSequence();
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
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(TaskListActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {


                        Map<String, String> params = new HashMap<String, String>();
                        params.put(WebService.KEY_TASK, WebService.KEY_SIGNOFFTASK_OFFLINE);
                        params.put(WebService.KEY_SIGNOFFDATETIME, list_details.get(ii).getAcknowledged_date() + " " + list_details.get(ii).getAcknowledged_time());
                        params.put(WebService.KEY_SIGNOFFACOWLEDGEDBY, list_details.get(ii).getAcknowledged_by());
                        params.put(WebService.KEY_SIGNOFFTASKID, list_details.get(ii).getTask_id());
                        params.put("second_man", list_details.get(ii).getSecond_man());
                        params.put("attachment", "");

                        return params;
                    }
                };

                mStringRequest2.setRetryPolicy(new DefaultRetryPolicy(
                        50000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                mRequestQueue2.add(mStringRequest2);



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
                    Toast.makeText(context, message, 1).show();
                }
                details_impl = new Details_impl(TaskListActivity.this);
                list_details = details_impl.getUser();

                //Picasso.with(context).load(str_signature).fit().into(image_signoffshow);

                mRequestQueue = Volley.newRequestQueue(context);

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

                                    details_impl = new Details_impl(context);
                                    list_details = details_impl.getUserDetails1(list_details.get(ii).getTask_id());
                                    long det = details_impl.update(new TaskDetailCartModel(list_details.get(0).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list_details.get(0).getTask_id()), list_details.get(0).getTask_number(), list_details.get(0).getClient_name(), list_details.get(0).getDate(), list_details.get(0).getTime(), list_details.get(0).getAddress(),
                                            list_details.get(0).getLatitude(), list_details.get(0).getLongitude(), list_details.get(0).getShort_description(), list_details.get(0).getDescription(), list_details.get(0).getAcknowledged_by(), list_details.get(0).getAcknowledged_date(), list_details.get(0).getAcknowledged_time(), list_details.get(0).getImage_path(), list_details.get(0).getImage_bitmap(),
                                            list_details.get(0).getTask_process(), list_details.get(0).getDisposal_site_name(), list_details.get(0).getDisposal_site_id(), list_details.get(0).getDisposal_date(), list_details.get(0).getDisposal_time(), list_details.get(0).getJob_type(), list_details.get(0).getSecond_man(), list_details.get(0).getJob_started(), list_details.get(0).getSing_off_status(), list_details.get(0).getJob_start_datetime(),
                                            task_sync_status, ss1, ss2, ss2_A, ss3, final_sync, list_details.get(0).getNew_rental(), list_details.get(0).getFinal_removal(), list_details.get(0).getInstructions()
                                    ));


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                CallApiSequence();

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                                CallApiSequence();
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


//                if (nameValuePair.size() > 0) {
//                    nameValuePair.clear();
//                }
//
//
//                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, WebService.KEY_UPDATETASK_OFFLINE));
//                nameValuePair.add(new BasicNameValuePair(WebService.KEY_UPDATETASKID, list_details.get(ii).getTask_id()));
//                nameValuePair.add(new BasicNameValuePair("second_man", list_details.get(ii).getSecond_man()));
//                nameValuePair.add(new BasicNameValuePair(WebService.KEY_UPDATETASKPROCESS, "In-progress"));
//                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TIPPINGBOOKDATETIME, list_details.get(ii).getAcknowledged_date() + " " + list_details.get(ii).getAcknowledged_time()));
//                nameValuePair.add(new BasicNameValuePair(WebService.KEY_SIGNOFFDATETIME, list_details.get(ii).getAcknowledged_date() + " " + list_details.get(ii).getAcknowledged_time()));
//                nameValuePair.add(new BasicNameValuePair("task_sync_status", list_details.get(ii).getTask_sync_status()));
//                nameValuePair.add(new BasicNameValuePair("ss1", list_details.get(ii).getSs1()));
//                nameValuePair.add(new BasicNameValuePair("ss2", list_details.get(ii).getSs2()));
//                nameValuePair.add(new BasicNameValuePair("ss2_A", list_details.get(ii).getSs2_A()));
//                nameValuePair.add(new BasicNameValuePair("ss3", list_details.get(ii).getSs3()));
//                nameValuePair.add(new BasicNameValuePair("final_sync", list_details.get(ii).getFinal_sync()));
//                if (connectionDetector.isConnectingToInternet()) {
//                    key = "statusupdate2";
//                    position_status = ii;
//                    asyncTaskClass = new AsyncTaskClass(context, TaskListActivity.this, nameValuePair, WebService.SERVERURL, true);
//                    asyncTaskClass.execute();
//                } else {
//                    Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
//                    return;
//                }


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    private void ListDrivers() {
        mRequestQueue = Volley.newRequestQueue(TaskListActivity.this);

        StringRequest mStringRequest = new StringRequest(Request.Method.POST, WebService.SERVERURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("mainactivity_resonse", response);
                        arrSecond.clear();
                        arrSecond.add(0, new SecondManModel("0", "Please select", ""));
                        arrSecond2.clear();
                        arrSecond2.add(0, new SecondMan2Model(0, "0", "Please select", ""));
                        try {
                            Gson gson2 = new Gson();
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject arrayElement_0 = jsonArray.getJSONObject(i);
                                arrSecond.add(gson2.fromJson(String.valueOf(arrayElement_0), SecondManModel.class));

                                String man_id = arrayElement_0.getString("id");
                                String names = arrayElement_0.getString("name");
                                String emails = arrayElement_0.getString("email");

                                secondMan_impl = new SecondMan_impl(TaskListActivity.this);
                                long det = secondMan_impl.insert(new SecondMan2Model(0, man_id, names, emails));
                            }

                            arrSecond2 = secondMan_impl.getUser();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(TaskListActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(WebService.KEY_TASK, "ListDrivers");

                return params;
            }
        };

        mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(mStringRequest);
    }

    private void CallApiSequence2(final int i) {
        details_impl = new Details_impl(TaskListActivity.this);
        list_details = details_impl.getUser();

//        Toast.makeText(context, "Job in Sync " + list_details.get(i).getTask_number(), Toast.LENGTH_SHORT).show();
        if (list_details.get(i).getSs1().equalsIgnoreCase("1")) {
            mRequestQueue = Volley.newRequestQueue(TaskListActivity.this);

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

                                details_impl = new Details_impl(TaskListActivity.this);
                                list_details = details_impl.getUserDetails1(String.valueOf(list_details.get(i).getTask_id()));
                                long det = details_impl.update(new TaskDetailCartModel(list_details.get(0).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list_details.get(0).getTask_id()), list_details.get(0).getTask_number(), list_details.get(0).getClient_name(), list_details.get(0).getDate(), list_details.get(0).getTime(), list_details.get(0).getAddress(),
                                        list_details.get(0).getLatitude(), list_details.get(0).getLongitude(), list_details.get(0).getShort_description(), list_details.get(0).getDescription(), list_details.get(0).getAcknowledged_by(), list_details.get(0).getAcknowledged_date(), list_details.get(0).getAcknowledged_time(), list_details.get(0).getImage_path(), list_details.get(0).getImage_bitmap(),
                                        list_details.get(0).getTask_process(), list_details.get(0).getDisposal_site_name(), list_details.get(0).getDisposal_site_id(), list_details.get(0).getDisposal_date(), list_details.get(0).getDisposal_time(), list_details.get(0).getJob_type(), list_details.get(0).getSecond_man(), list_details.get(0).getJob_started(), list_details.get(0).getSing_off_status(), list_details.get(0).getJob_start_datetime(),
                                        task_sync_status, ss1, ss2, ss2_A, ss3, final_sync, list_details.get(0).getNew_rental(), list_details.get(0).getFinal_removal(), list_details.get(0).getInstructions()
                                ));

                            } catch (JSONException e) {
                                e.printStackTrace();
//                                CallApiSequence();
                            }
                            globalVariable.SetTaskFinished("true");
                            CallApiSequence();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(TaskListActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                            CallApiSequence();
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

        }
        else if (list_details.get(i).getSs2().equalsIgnoreCase("1")) {
            new SubmitRoadSideAssistDataAsyTask(i).execute();
        }
        else if (list_details.get(i).getSs2_A().equalsIgnoreCase("1")) {
            mRequestQueue = Volley.newRequestQueue(TaskListActivity.this);
            StringRequest mStringRequest = new StringRequest(Request.Method.POST, WebService.SERVERURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("mainactivity_resonse", response);
                            RequestQueue mRequestQueue2 = Volley.newRequestQueue(TaskListActivity.this);

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

                                                details_impl = new Details_impl(TaskListActivity.this);
                                                list_details = details_impl.getUserDetails1(String.valueOf(list_details.get(i).getTask_id()));
                                                long det = details_impl.update(new TaskDetailCartModel(list_details.get(0).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list_details.get(0).getTask_id()), list_details.get(0).getTask_number(), list_details.get(0).getClient_name(), list_details.get(0).getDate(), list_details.get(0).getTime(), list_details.get(0).getAddress(),
                                                        list_details.get(0).getLatitude(), list_details.get(0).getLongitude(), list_details.get(0).getShort_description(), list_details.get(0).getDescription(), list_details.get(0).getAcknowledged_by(), list_details.get(0).getAcknowledged_date(), list_details.get(0).getAcknowledged_time(), list_details.get(0).getImage_path(), list_details.get(0).getImage_bitmap(),
                                                        list_details.get(0).getTask_process(), list_details.get(0).getDisposal_site_name(), list_details.get(0).getDisposal_site_id(), list_details.get(0).getDisposal_date(), list_details.get(0).getDisposal_time(), list_details.get(0).getJob_type(), list_details.get(0).getSecond_man(), list_details.get(0).getJob_started(), list_details.get(0).getSing_off_status(), list_details.get(0).getJob_start_datetime(),
                                                        task_sync_status, ss1, ss2, ss2_A, ss3, final_sync, list_details.get(0).getNew_rental(), list_details.get(0).getFinal_removal(), list_details.get(0).getInstructions()
                                                ));

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                CallApiSequence();
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
                                            globalVariable.SetTaskFinished("true");
                                            CallApiSequence();
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(TaskListActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                                            CallApiSequence();
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
                            Toast.makeText(TaskListActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                            CallApiSequence();
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
//                    if (nameValuePair.size() > 0) {
//                        nameValuePair.clear();
//                    }
//                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, WebService.KEY_SIGN_OFFTASK_OFFLINE));
//                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_UPDATETASKID, list_details.get(i).getTask_id()));
//                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_SIGN_OFF_STATUS, list_details.get(i).getSing_off_status()));
//                    nameValuePair.add(new BasicNameValuePair("second_man", list_details.get(i).getSecond_man()));
//
//                    if (connectionDetector.isConnectingToInternet()) {
//                        key = "signoffstatus";
//                        position_status=i;
//                        asyncTaskClass = new AsyncTaskClass(context, TaskListActivity.this, nameValuePair, WebService.SERVERURL, true);
//                        asyncTaskClass.execute();
//                    } else {
//                        Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
//                        return;
//                    }
        }
        else if (list_details.get(i).getSs3().equalsIgnoreCase("1")) {
            mRequestQueue = Volley.newRequestQueue(TaskListActivity.this);

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


                                details_impl = new Details_impl(TaskListActivity.this);
                                list_details = details_impl.getUserDetails1(list_details.get(i).getTask_id());
                                long det = details_impl.update(new TaskDetailCartModel(list_details.get(0).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list_details.get(0).getTask_id()), list_details.get(0).getTask_number(), list_details.get(0).getClient_name(), list_details.get(0).getDate(), list_details.get(0).getTime(), list_details.get(0).getAddress(),
                                        list_details.get(0).getLatitude(), list_details.get(0).getLongitude(), list_details.get(0).getShort_description(), list_details.get(0).getDescription(), list_details.get(0).getAcknowledged_by(), list_details.get(0).getAcknowledged_date(), list_details.get(0).getAcknowledged_time(), list_details.get(0).getImage_path(), list_details.get(0).getImage_bitmap(),
                                        list_details.get(0).getTask_process(), list_details.get(0).getDisposal_site_name(), list_details.get(0).getDisposal_site_id(), list_details.get(0).getDisposal_date(), list_details.get(0).getDisposal_time(), list_details.get(0).getJob_type(), list_details.get(0).getSecond_man(), list_details.get(0).getJob_started(), list_details.get(0).getSing_off_status(), list_details.get(0).getJob_start_datetime(),
                                        task_sync_status, ss1, ss2, ss2_A, ss3, final_sync, list_details.get(0).getNew_rental(), list_details.get(0).getFinal_removal(), list_details.get(0).getInstructions()
                                ));
//                                    globalVariable.SetTaskFinished("true");


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            CallApiSequence();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(TaskListActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                            CallApiSequence();
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
//                    if (!txt_disposalsiteid.getText().toString().equals("")) {
                    params.put(WebService.KEY_DISPOSAL_NAME, list_details.get(i).getDisposal_site_name());
                    params.put(WebService.KEY_DISPOSAL_ID, list_details.get(i).getDisposal_site_id());
                    params.put(WebService.KEY_TIPPINGBOOKDATETIME, list_details.get(i).getDisposal_date() + " " + list_details.get(i).getDisposal_time());

//                    } else {
//                        params.put(WebService.KEY_DISPOSAL_NAME, "");
//                        params.put(WebService.KEY_DISPOSAL_ID, "");
//                        params.put(WebService.KEY_TIPPINGBOOKDATETIME, list_details.get(i).getDisposal_date() + " " + list_details.get(i).getDisposal_time());
//                    }


                    return params;
                }
            };

            mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    50000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            mRequestQueue.add(mStringRequest);

//                if (nameValuePair.size() > 0) {
//                    nameValuePair.clear();
//                }
//
//                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, "update_status_combined_offline"));
//                nameValuePair.add(new BasicNameValuePair(WebService.KEY_UPDATETASKID, list_details.get(i).getTask_id()));
//                nameValuePair.add(new BasicNameValuePair(WebService.KEY_UPDATETASKPROCESS, "Completed"));
//                nameValuePair.add(new BasicNameValuePair(WebService.KEY_SIGNOFFDATETIME, list_details.get(i).getDisposal_date() + " " + list_details.get(i).getDisposal_time()));
//                nameValuePair.add(new BasicNameValuePair("task_sync_status", list_details.get(0).getTask_sync_status()));
//                nameValuePair.add(new BasicNameValuePair("ss1", list_details.get(0).getSs1()));
//                nameValuePair.add(new BasicNameValuePair("ss2", list_details.get(0).getSs2()));
//                nameValuePair.add(new BasicNameValuePair("ss2_A", list_details.get(0).getSs2_A()));
//                nameValuePair.add(new BasicNameValuePair("ss3", list_details.get(0).getSs3()));
//                nameValuePair.add(new BasicNameValuePair("final_sync", list_details.get(0).getFinal_sync()));
//                if (!txt_disposalsiteid.getText().toString().equals("")) {
//                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_DISPOSAL_NAME, list_details.get(i).getDisposal_site_name()));
//                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_DISPOSAL_ID, list_details.get(i).getDisposal_site_id()));
//                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_TIPPINGBOOKDATETIME, list_details.get(i).getDisposal_date() + " " + list_details.get(i).getDisposal_time()));
//
//                } else {
//                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_DISPOSAL_NAME, ""));
//                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_DISPOSAL_ID, ""));
//                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_TIPPINGBOOKDATETIME, list_details.get(i).getDisposal_date() + " " + list_details.get(i).getDisposal_time()));
//                }
//
//                if (connectionDetector.isConnectingToInternet()) {
//                    key = "statusupdate";
//                    position_status=i;
//                    asyncTaskClass = new AsyncTaskClass(context, TaskListActivity.this, nameValuePair, WebService.SERVERURL, true);
//                    asyncTaskClass.execute();
//                } else {
//                    Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
//                    return;
//                }

        } else {
            f_pos++;
            CallApiSequence();
        }
//            while(!globalVariable.GetTaskFinished().equalsIgnoreCase("true")){ }


    }

    @Override
    public void onBackPressed() {
        finish();
//        f_pos = 0;
//        logout_status = "no";
//        CallApiSequence();

    }

    public class JobTypeAdapter extends BaseAdapter {
        Context context;
        private List<JobTypeModel> arrayList;
        LayoutInflater layoutInflater;

        public JobTypeAdapter(Context context,
                              List<JobTypeModel> arrayList) {
            super();
            this.context = context;
            this.arrayList = arrayList;
            layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return arrayList.size();
        }


        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        class ViewHolder {
            TextView txt_title;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            ViewHolder holder = new ViewHolder();
            View v = layoutInflater.inflate(R.layout.adapter_spinnerrow2, null);
            holder.txt_title = v.findViewById(R.id.txt_spn_row);

            holder.txt_title.setText(arrayList.get(position).getJob_type_status());

            return v;
        }
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }
    public void ShowDilagBonusInstructionMessage(final Context context, String message) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_instruction);
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
                        dialog.dismiss();
                    }
                }
            }
        });

        dialog.show();

    }

    private boolean multiplePermissions(String[] permissionsData) {

        boolean isGranted = true;
        for (int i = 0; i < permissionsData.length; i++) {

            if (!checkPermission(permissionsData[i])) {
                isGranted = false;
                if (checkPermissionRationale(permissionsData[i])) {
                    permissionsNeeded.add(getPermissionTitle(permissionsData[i]));
                    permissionsNeededRationale.add(permissionsData[i]);
                } else {
                    permissionsList.add(permissionsData[i]);
                }
            }
        }

        if (permissionsList.size() > 0) {
            gotoRequestPermissions();
        }

        if (permissionsNeededRationale.size() > 0) {
            gotoRequestPermissionsRationale(permissionsNeededRationale.toArray(new String[permissionsNeededRationale.size()]));
        }

        return isGranted;
    }


    private void gotoRequestPermissions() {
        ActivityCompat.requestPermissions(this, permissionsList.toArray(new String[permissionsList.size()]), Premission_Grant_requestCode);
    }

    private void gotoRequestPermissionsRationale(String[] ratPermission) {
        ActivityCompat.requestPermissions(this, ratPermission, Premission_Grant_requestCode);
    }


    private boolean checkPermission(String permission) {

        if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private boolean checkPermissionRationale(String permission) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            return true;
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case Premission_Grant_requestCode: {

                boolean isgrantedall = true;

                List<String> perList = new ArrayList<>();


                for (int i = 0; i < permissions.length; i++) {

                    Log.e("PGranted", "  permissions.length   =" + permissions[i]);

                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {

                        Log.e("PGranted", "  permissions. false   =" + i);
                        isgrantedall = false;
                        perList.add(permissions[i]);
                    }
                }

                Log.e("PGranted", "  permissions.finish    =" + perList.size() + "   result  =" + isgrantedall);

                if (perList.size() <= 0 && isgrantedall) {
//                    gotoActivity();
//                    this.finish();
//                    OpenCamera();

                } else {
//                    gotoActivity();
//                    this.finish();
                }


                return;
            }
        }
    }

    public String getPermissionTitle(String userSTring) {
        int idx = Arrays.asList(permissions).indexOf(userSTring);
        return permissionsTitle[idx];
    }
}
