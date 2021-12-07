package com.skipservices;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.skipservices.Impl.Details_impl;
import com.skipservices.Impl.Main_impl;
import com.skipservices.Impl.SecondMan_impl;
import com.skipservices.Model.SecondMan2Model;
import com.skipservices.Model.TaskDetailCartModel;
import com.skipservices.Model.TaskListCartModel;
import com.skipservices.adapter.BookAddAdapter;
import com.skipservices.adapter.BookTicketSpinnerAdapter;
import com.skipservices.adapter.TicketAddAdapter;
import com.skipservices.adapter.TicketSpinnerAdapter;
import com.skipservices.interfaces.OnBtnRemoveBookClickListner;
import com.skipservices.interfaces.OnBtnRemoveClickListner;
import com.skipservices.parsers.DisposalModel;
import com.skipservices.parsers.SecondManModel;
import com.skipservices.parsers.SharedPrefrenceClass;
import com.skipservices.parsers.TaskDetailParser;
import com.skipservices.thread.AsyncTaskClass;
import com.skipservices.thread.AsyncTaskCompleteListener;
import com.skipservices.utils.ConnectionDetector;
import com.skipservices.utils.MyPersonalApp;
import com.skipservices.utils.MyUtility;
import com.skipservices.utils.RotateAnimation;
import com.skipservices.utils.WebService;
import com.squareup.picasso.Picasso;

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
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
public class TaskDetailActivity extends AppCompatActivity implements OnBtnRemoveClickListner, OnBtnRemoveBookClickListner, AsyncTaskCompleteListener<String> {

    public String DEBUG_TAG = "TaskDetailActivity";
    Context context;
    Button btn_assigntt, btn_acknowledge, btn_start;
    TextView txt_username, txt_date, txt_tasknumber, txt_jobtype, txt_clientname, txt_time, txt_description,txt_description2, txt_signature, txt_signofftime,
            txt_disposalsite, txt_disposalsiteid, txt_disposaldate, txt_disposaltime,txt_instruction;
    ImageButton imgbtn_direction;
    LinearLayout lin_direction,lin_instruction;
    //Spinner spn_status;
    double slat = 13.103000;
    double slon = -59.614456;
    double dlat = 13.099907;
    double dlon = -59.596946;
    ListView listView_tippingticket;
    ListView listView_tippingbook;

    ArrayList<HashMap<String, String>> arrbook = new ArrayList<HashMap<String, String>>();
    ArrayList<String> arrticket = new ArrayList<String>();
    TicketAddAdapter ticketAddAdapter;
    BookAddAdapter bookAddAdapter;
    List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
    ConnectionDetector connectionDetector;
    AsyncTaskClass asyncTaskClass;
    SharedPrefrenceClass sharedPrefrenceClass;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
    String strtaskid = "";
    TaskDetailParser taskDetailParser;
    int spinnerbookposition = 0;
    ArrayList<String> mylist;
    TicketSpinnerAdapter ticketSpinnerAdapter = null;
    String str_boonumbervalue = "";
    String str_ticketvalue = "";
    String taskkey = "";
    ArrayList<String> list_status = new ArrayList<String>();
    String strsignoffdate = "";
    String strsignofftime = "";
    Button btn_email, btn_status;
    public static final int SIGNATURE_ACTIVITY = 1;
    String str_signature = "";
    MultipartEntity reqEntity;
    private String str_signaturename = "";
    Dialog dialogsignoff;
    Bitmap bitmap_sign;
    ImageView image_signoffshow;
    Spinner spn_disposal, spn_second_man;
    TextView tx_disposal;
    ArrayList<DisposalModel> arrLike = new ArrayList<>();
    ArrayList<SecondManModel> arrSecond = new ArrayList<>();

    DisposalAdapter disposalAdapter;
    DisposalAdapter2 disposalAdapter2;
    SecondManAdapter secondManAdapter;
    String namedisposal = "", final_Status = "", str_cat_id = "";
    private boolean userIsInteracting;
    int exist = 0, regionidex = 0;
    int jobstart = 0;
    RotateAnimation rotateAnimation;
    private ArrayList<TaskDetailCartModel> list = new ArrayList<TaskDetailCartModel>();
    Details_impl impl;
    ArrayList<SecondMan2Model> arrSecond2 = new ArrayList<>();
    SecondMan_impl secondMan_impl;
    String imagepath;
    MyPersonalApp globalVariable;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskdetail);
        context = TaskDetailActivity.this;
        globalVariable = (MyPersonalApp) getApplicationContext();
        btn_assigntt = (Button) findViewById(R.id.btn_assigntt);
        btn_acknowledge = (Button) findViewById(R.id.btn_acnowledge);
        btn_email = (Button) findViewById(R.id.btn_emaltoadmin);
        btn_status = (Button) findViewById(R.id.btn_status);
        btn_start = (Button) findViewById(R.id.btn_start);
        imgbtn_direction = (ImageButton) findViewById(R.id.imgbtn_direction);
        lin_direction=findViewById(R.id.lin_direction);
        txt_username = (TextView) findViewById(R.id.txt_username);
        txt_date = (TextView) findViewById(R.id.txt_date);

        txt_tasknumber = (TextView) findViewById(R.id.txt_tasknumber);
        txt_jobtype = (TextView) findViewById(R.id.txt_jobtype);
        txt_clientname = (TextView) findViewById(R.id.txt_clientname);
        txt_time = (TextView) findViewById(R.id.txt_time);
        txt_description = (TextView) findViewById(R.id.txt_description);
        txt_description2 = (TextView) findViewById(R.id.txt_description2);
        txt_signature = (TextView) findViewById(R.id.txt_signature);
        image_signoffshow = (ImageView) findViewById(R.id.imgv_signoffshow);
        txt_signofftime = (TextView) findViewById(R.id.txt_signofftime);
        txt_disposalsite = (TextView) findViewById(R.id.txt_disposalsite);
        txt_disposalsiteid = (TextView) findViewById(R.id.txt_disposalsiteid);
        txt_disposaldate = (TextView) findViewById(R.id.txt_disposaldate);
        txt_disposaltime = (TextView) findViewById(R.id.disposaltime);
        txt_instruction=findViewById(R.id.txt_instruction);
        lin_instruction=findViewById(R.id.lin_instruction);
//        Toast.makeText(this, String.valueOf(context.getDatabasePath("Login.db")), Toast.LENGTH_SHORT).show();
        Log.d("happys",String.valueOf(context.getDatabasePath("Login.db")));

        listView_tippingticket = (ListView) findViewById(R.id.list_tippingticket);
        listView_tippingbook = (ListView) findViewById(R.id.list_tippingbook);

        tx_disposal = findViewById(R.id.tx_disposal);
        spn_disposal = findViewById(R.id.spn_disposal);
        spn_second_man = findViewById(R.id.spn_second_man);

        setListViewHeightBasedOnChildren(listView_tippingticket);
        setListViewHeightBasedOnChildren(listView_tippingbook);

        sharedPrefrenceClass = new SharedPrefrenceClass(context);
        connectionDetector = new ConnectionDetector(context);

        txt_username.setText(sharedPrefrenceClass.getKEY_LOGIN_USERNAME());

        //txt_date.setText("ABC Godown #751");

        Intent intent = getIntent();
        if (intent != null) {
            strtaskid = intent.getExtras().getString("id");
        }

        impl = new Details_impl(this);
        list = impl.getUser();

        secondMan_impl = new SecondMan_impl(this);

        Log.d("ssss", String.valueOf(list.size()));
//        Toast.makeText(context, String.valueOf(list.size()), Toast.LENGTH_SHORT).show();


        if (list.size() < 1) {
            if (nameValuePair.size() > 0) {
                nameValuePair.clear();
            }

            nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, WebService.KEY_TASKDETAIL_TASK_OFFLINE));
            nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASKDETAIL_TASKIDDKEY, strtaskid));

            if (connectionDetector.isConnectingToInternet()) {
                taskkey = "detail";
                asyncTaskClass = new AsyncTaskClass(context, TaskDetailActivity.this, nameValuePair, WebService.SERVERURL, true);
                asyncTaskClass.execute();
            } else {
                return;
            }
        } else {
//            if (impl.isExist(strtaskid)) {
                ShowTaskDetailDB();
//            } else {
//                if (nameValuePair.size() > 0) {
//                    nameValuePair.clear();
//                }
//
//                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, WebService.KEY_TASKDETAIL_TASK_OFFLINE));
//                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASKDETAIL_TASKIDDKEY, strtaskid));
//
//                if (connectionDetector.isConnectingToInternet()) {
//                    taskkey = "detail";
//                    asyncTaskClass = new AsyncTaskClass(context, TaskDetailActivity.this, nameValuePair, WebService.SERVERURL, true);
//                    asyncTaskClass.execute();
//                } else {
////                    Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
//                    return;
//                }
//            }

        }


        btn_assigntt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DialogTicketDetails();
            }
        });

        btn_acknowledge.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

//                if (jobstart == 1) {
                    // TODO Auto-generated method stub
                    Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_sign);
                    List<String> favorites = MyUtility.getFavoriteList(TaskDetailActivity.this);
                    if (favorites != null) {
                        if (favorites.size() > 0) {
                            if (favorites.contains(strtaskid)) {
                                Toast.makeText(context, "Not Available to sign", 1).show();

                            } else {
                                Log.i(DEBUG_TAG, "num=" + favorites.toString());
                                DialogSignOff(largeIcon);
                            }
                        }
                    } else {
                        impl = new Details_impl(TaskDetailActivity.this);
                        list = impl.getUserDetails1(strtaskid);
                        if (list.get(0).getSing_off_status().equalsIgnoreCase("NATS")){
                            Toast.makeText(context, "Not Available to sign", 1).show();
                        }else if (list.get(0).getSs2().equalsIgnoreCase("2")){
//                            Toast.makeText(context, "Not Available to sign", 1).show();
                            Toast.makeText(context, "Signature already Taken", 1).show();
                        }else {
//                        Toast.makeText(context, "Null", 1).show();
                            DialogSignOff(largeIcon);
                        }
                    }
//                }
            }
        });

        lin_direction.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (connectionDetector.isConnectingToInternet()) {
                    impl = new Details_impl(TaskDetailActivity.this);
                    list = impl.getUserDetails1(strtaskid);
                    if (list.size() > 0) {
                        try {
                            if (list.get(0).getLatitude().length() > 2) {
                                dlat = Double.parseDouble(list.get(0).getLatitude());
                                dlon = Double.parseDouble(list.get(0).getLongitude());

                                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                        Uri.parse("http://maps.google.com/maps?saddr=" + slat + "," + slon + "&daddr=" + dlat + "," + dlon));
                                startActivity(intent);
                            } else {
                                Toast.makeText(context, "Lat/Long not available for the address", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    Toast.makeText(context, "App is working offline. Can't load map.", Toast.LENGTH_SHORT).show();
                }

//                try {
//                    if (taskDetailParser != null) {
//                        if (!taskDetailParser.GetTaskDetailList().isEmpty()) {
//                            dlat = Double.parseDouble(taskDetailParser.GetTaskDetailList().get(0).get(WebService.KEY_TASKDETAIL_LATITUDE));
//                            dlon = Double.parseDouble(taskDetailParser.GetTaskDetailList().get(0).get(WebService.KEY_TASKDETAIL_LONGITUDE));
//
//                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                                    Uri.parse("http://maps.google.com/maps?saddr=" + slat + "," + slon + "&daddr=" + dlat + "," + dlon));
//                            startActivity(intent);
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        });
		
		
		/*spn_status.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(position>0)
				{
					if(nameValuePair.size()>0)
					{
						nameValuePair.clear();
					}
					
					nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, WebService.KEY_UPDATETASK));
					nameValuePair.add(new BasicNameValuePair(WebService.KEY_UPDATETASKID, strtaskid));
					nameValuePair.add(new BasicNameValuePair(WebService.KEY_UPDATETASKPROCESS, list_status.get(position)));
					
					if(connectionDetector.isConnectingToInternet())
					{
						taskkey = "statusupdate";
						asyncTaskClass = new AsyncTaskClass(context, TaskDetailActivity.this, nameValuePair, WebService.SERVERURL,false);
						asyncTaskClass.execute();
					}
					else
					{
						Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
						return;
					}
				}
				
				
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
*/
        spn_disposal.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                // Toast.makeText(context,
                // "item= "+parent.getItemAtPosition(position), 1).show();
                if (position > 0) {
                    DisposalModel data = (DisposalModel) disposalAdapter.getItem(position);
                    txt_disposalsiteid.setText(data.getId());
                    namedisposal = data.getName();

                } else {
                    txt_disposalsiteid.setText("");
                    namedisposal = "";
//					rel_shiping.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        tx_disposal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (final_Status.equals("completed")) {
                } else {
                    final Dialog dialog = new Dialog(TaskDetailActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.single_child_list);
                    ListView lst_chld = (ListView) dialog.findViewById(R.id.lst_chld);
                    Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
                    Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
                    disposalAdapter2 = new DisposalAdapter2(context, arrLike);
                    lst_chld.setAdapter(disposalAdapter2);

                    dialog.show();

                    btn_cancel.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
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
                                if (nameValuePair.size() > 0) {
                                    nameValuePair.clear();
                                }
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
                                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, WebService.KEY_TIPPINGBOOKTASK));
                                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TIPPINGBOOTASKID, strtaskid));
                                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TIPPINGBOOKID, "0"));
                                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TIPPINGBOOKTICKET, "0000"));
                                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TIPPINGBOOKDATETIME, strsignoffdate + " " + strsignofftime));


                                if (connectionDetector.isConnectingToInternet()) {
                                    taskkey = "savetippingbook";
                                    asyncTaskClass = new AsyncTaskClass(context, TaskDetailActivity.this, nameValuePair, WebService.SERVERURL, true);
                                    asyncTaskClass.execute();
                                    if (dialog != null) {
                                        if (dialog.isShowing()) {
                                            dialog.dismiss();
                                        }
                                    }
                                } else {
//                                    Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
                                    return;
                                }
//                            tx_album.setText(regionname);
                                dialog.dismiss();
                            }
                        }
                    });
                }
            }
        });

        spn_second_man.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (userIsInteracting) {
//                    str_cat_id = arrSecond.get(position).getId();
//                    Toast.makeText(TaskDetailActivity.this, str_cat_id, Toast.LENGTH_SHORT).show();
//                    str_cat_name = arrCategory.get(position).getCategory_name();

//                    if (str_cat_id.equalsIgnoreCase("0")) {
//
//                    } else {
//                        attemptSubCategory(str_cat_id);
//                    }
                }
                // TODO Auto-generated method stub
                // Toast.makeText(context,
                // "item= "+parent.getItemAtPosition(position), 1).show();
                if (position > 0) {
                    SecondMan2Model data = (SecondMan2Model) secondManAdapter.getItem(position);
                    txt_disposalsiteid.setText(data.getManId());
                    namedisposal = data.getName();
                    str_cat_id = data.getManId();

                } else {
                    txt_disposalsiteid.setText("");
                    namedisposal = "";
                    str_cat_id = arrSecond.get(position).getId();
//					rel_shiping.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        btn_start.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (jobstart == 0) {
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

                    impl = new Details_impl(TaskDetailActivity.this);
                    list = impl.getUserDetails1(strtaskid);
                    long det = impl.update(new TaskDetailCartModel(list.get(0).getId(),sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list.get(0).getTask_id()), list.get(0).getTask_number(), list.get(0).getClient_name(), list.get(0).getDate(), list.get(0).getTime(), list.get(0).getAddress(),
                            list.get(0).getLatitude(), list.get(0).getLongitude(), list.get(0).getShort_description(), list.get(0).getDescription(), list.get(0).getAcknowledged_by(), list.get(0).getAcknowledged_date(), list.get(0).getAcknowledged_time(), list.get(0).getImage_path(),list.get(0).getImage_bitmap(),
                            list.get(0).getTask_process(), list.get(0).getDisposal_site_name(), list.get(0).getDisposal_site_id(), list.get(0).getDisposal_date(), list.get(0).getDisposal_time(), list.get(0).getJob_type(), str_cat_id, "1", list.get(0).getSing_off_status(), strsignoffdate + " " + strsignofftime,
                            "1", "1", "0", "0", "0","0",list.get(0).getNew_rental(),list.get(0).getFinal_removal(),list.get(0).getInstructions()
                    ));

                    ShowTaskDetailDB();

//                    if (nameValuePair.size() > 0) {
//                        nameValuePair.clear();
//                    }
//                    SimpleDateFormat myFormatdate = new SimpleDateFormat("yyyy-MM-dd");//Y-m-d H:i:s  2016-09-19 15:09:00
//                    SimpleDateFormat myFormattime = new SimpleDateFormat("HH:mm:ss");
//                    try {
//                        Date date = new Date();
//                        strsignoffdate = myFormatdate.format(date);
//                        strsignofftime = myFormattime.format(date);
//
//                        Log.i(DEBUG_TAG, "Current Date Time in give format: " + strsignoffdate + " " + strsignofftime);
//                    } catch (ParseException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, "job_started"));
//                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_TIPPINGBOOTASKID, strtaskid));
//                    nameValuePair.add(new BasicNameValuePair("second_man", str_cat_id));
//                    nameValuePair.add(new BasicNameValuePair("job_start_datetime", strsignoffdate + " " + strsignofftime));


//                    impl = new Details_impl(TaskDetailActivity.this);
//                    list = impl.getUser();
//
//                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, "job_started_offline"));
//                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_TIPPINGBOOTASKID, list.get(0).getTask_id()));
//                    nameValuePair.add(new BasicNameValuePair("second_man", list.get(0).getSecond_man()));
//                    nameValuePair.add(new BasicNameValuePair("job_start_datetime", list.get(0).getJob_start_datetime()));
//                    nameValuePair.add(new BasicNameValuePair("task_sync_status", list.get(0).getTask_sync_status()));
//                    nameValuePair.add(new BasicNameValuePair("ss1", list.get(0).getSs1()));
//                    nameValuePair.add(new BasicNameValuePair("ss2", list.get(0).getSs2()));
//                    nameValuePair.add(new BasicNameValuePair("ss3", list.get(0).getSs3()));
//                    nameValuePair.add(new BasicNameValuePair("final_sync", list.get(0).getFinal_sync()));
//
//                    if (connectionDetector.isConnectingToInternet()) {
//                        taskkey = "job_started";
//                        asyncTaskClass = new AsyncTaskClass(context, TaskDetailActivity.this, nameValuePair, WebService.SERVERURL, true);
//                        asyncTaskClass.execute();
//                    } else {
//                        Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
//                        return;
//                    }

//                    if (connectionDetector.isConnectingToInternet()) {
//                        CallApiSequence();
//                    }
                }
            }
        });

    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();


//        if (nameValuePair.size() > 0) {
//            nameValuePair.clear();
//        }
//
//        nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, WebService.KEY_TASKDETAIL_TASK));
//        nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASKDETAIL_TASKIDDKEY, strtaskid));
//
//        if (connectionDetector.isConnectingToInternet()) {
//            taskkey = "detail";
//            asyncTaskClass = new AsyncTaskClass(context, TaskDetailActivity.this, nameValuePair, WebService.SERVERURL, true);
//            asyncTaskClass.execute();
//        } else {
//            Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
//            return;
//        }

        listView_tippingbook.setOnTouchListener(new OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        listView_tippingticket.setOnTouchListener(new OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        btn_email.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (nameValuePair.size() > 0) {
                    nameValuePair.clear();
                }

                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, "email_admin"));
                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASKDETAIL_TASKIDDKEY, strtaskid));

                if (connectionDetector.isConnectingToInternet()) {
                    taskkey = "email";
                    asyncTaskClass = new AsyncTaskClass(context, TaskDetailActivity.this, nameValuePair, WebService.SERVERURL, false);
                    asyncTaskClass.execute();
                } else {
//                    Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
    }
	
/*
	public void AddEdittext()
	{
		         android.widget.LinearLayout.LayoutParams lParamsMW = new LinearLayout.LayoutParams(
				 LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				 
				 LinearLayout linearLayout = new LinearLayout(this);
				 linearLayout.setOrientation(LinearLayout.VERTICAL);
				 
				 EditText edtView = new EditText(this);
				 edtView.setHint("Please enter your name");
				 edtView.setLayoutParams(lParamsMW);
				 edtView.setPadding(2, 2, 2, 2);
				 lParamsMW.setMargins(50, 300, 30, 100);
				 
				 linearLayout.addView(edtView);
				 
				 
				 this.setContentView(linearLayout, new LinearLayout.LayoutParams(
				 LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				 
	}*/

    public void DialogSignOff(Bitmap bitmap) {
        dialogsignoff = new Dialog(context);
        dialogsignoff.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogsignoff.setContentView(R.layout.dialog_anowledgement);
        dialogsignoff.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        Button btn_cancel = (Button) dialogsignoff.findViewById(R.id.btn_cancel);
        Button btn_notavailabletosign = (Button) dialogsignoff.findViewById(R.id.btn_notavailtosign);
        Button btn_hold = (Button) dialogsignoff.findViewById(R.id.btn_hold);
        ImageView imageView = (ImageView) dialogsignoff.findViewById(R.id.img_signature);
        Button btn_save = (Button) dialogsignoff.findViewById(R.id.btn_save);
        TextView textView_date = (TextView) dialogsignoff.findViewById(R.id.txt_signoffdate);
        TextView textView_time = (TextView) dialogsignoff.findViewById(R.id.txt_signofftime);
        //Button btn_notavailabletosign = (Button)dialog.findViewById(R.id.btn_notavailtosign);

        dialogsignoff.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        SimpleDateFormat myFormatdate = new SimpleDateFormat("yyyy-MM-dd");//Y-m-d H:i:s  2016-09-19 15:09:00
        SimpleDateFormat myFormattime = new SimpleDateFormat("HH:mm:ss");
        try {
            Date date = new Date();
            strsignoffdate = myFormatdate.format(date);
            strsignofftime = myFormattime.format(date);
            textView_date.setText(strsignoffdate);
            textView_time.setText(strsignofftime);
            Log.i(DEBUG_TAG, "Current Date Time in give format: " + strsignoffdate + " " + strsignofftime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), draw);
        imageView.setImageBitmap(bitmap);
        imageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(TaskDetailActivity.this, CaptureSignature.class);
                startActivityForResult(intent, SIGNATURE_ACTIVITY);
                if (dialogsignoff != null) {
                    if (dialogsignoff.isShowing()) {
                        dialogsignoff.dismiss();
                    }
                }

            }
        });

        btn_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (dialogsignoff != null) {
                    if (dialogsignoff.isShowing()) {
                        dialogsignoff.dismiss();
                    }
                }
            }
        });

        btn_notavailabletosign.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                if (str_cat_id.equalsIgnoreCase("0")) {
//                    Toast.makeText(TaskDetailActivity.this, "Please Select Second Man", Toast.LENGTH_SHORT).show();
//                } else {

                impl = new Details_impl(TaskDetailActivity.this);
                list = impl.getUserDetails1(strtaskid);
                if (dialogsignoff != null) {
                    if (dialogsignoff.isShowing()) {
                        dialogsignoff.dismiss();
//                        MyUtility.addFavoriteItem(TaskDetailActivity.this, strtaskid);
                        MyUtility.addFavoriteItem(TaskDetailActivity.this, list.get(0).getTask_id());
                        spn_second_man.setEnabled(false);
                        spn_second_man.setClickable(false);
                        if (nameValuePair.size() > 0) {
                            nameValuePair.clear();
                        }

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


                        impl = new Details_impl(TaskDetailActivity.this);
                        list = impl.getUserDetails1(strtaskid);
                        long det = impl.update(new TaskDetailCartModel(list.get(0).getId(),sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list.get(0).getTask_id()), list.get(0).getTask_number(), list.get(0).getClient_name(), list.get(0).getDate(), list.get(0).getTime(), list.get(0).getAddress(),
                                list.get(0).getLatitude(), list.get(0).getLongitude(), list.get(0).getShort_description(), list.get(0).getDescription(), list.get(0).getAcknowledged_by(), strsignoffdate, strsignofftime, list.get(0).getImage_path(),list.get(0).getImage_bitmap(),
                                "In-progress", list.get(0).getDisposal_site_name(), list.get(0).getDisposal_site_id(), strsignoffdate, strsignofftime, list.get(0).getJob_type(),str_cat_id, list.get(0).getJob_started(), "NATS", strsignoffdate + " " + strsignofftime,
                                "3", "1", "0","1", "0", "0",list.get(0).getNew_rental(),list.get(0).getFinal_removal(),list.get(0).getInstructions()
                        ));

                        ShowTaskDetailDB();

                        impl = new Details_impl(TaskDetailActivity.this);
                        list = impl.getUserDetails1(strtaskid);
                        if (connectionDetector.isConnectingToInternet()) {
                            rotateAnimation = new RotateAnimation(context);
                            rotateAnimation.show();
                            mRequestQueue = Volley.newRequestQueue(TaskDetailActivity.this);

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

                                                impl = new Details_impl(TaskDetailActivity.this);
                                                list= impl.getUserDetails1(strtaskid);
                                                long det = impl.update(new TaskDetailCartModel(list.get(0).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list.get(0).getTask_id()), list.get(0).getTask_number(), list.get(0).getClient_name(), list.get(0).getDate(), list.get(0).getTime(), list.get(0).getAddress(),
                                                        list.get(0).getLatitude(), list.get(0).getLongitude(), list.get(0).getShort_description(), list.get(0).getDescription(), list.get(0).getAcknowledged_by(), list.get(0).getAcknowledged_date(), list.get(0).getAcknowledged_time(), list.get(0).getImage_path(), list.get(0).getImage_bitmap(),
                                                        list.get(0).getTask_process(), list.get(0).getDisposal_site_name(), list.get(0).getDisposal_site_id(), list.get(0).getDisposal_date(), list.get(0).getDisposal_time(), list.get(0).getJob_type(), list.get(0).getSecond_man(), list.get(0).getJob_started(), list.get(0).getSing_off_status(), list.get(0).getJob_start_datetime(),
                                                        task_sync_status, ss1, ss2, ss2_A, ss3, final_sync, list.get(0).getNew_rental(), list.get(0).getFinal_removal(),list.get(0).getInstructions()
                                                ));
                                                RequestQueue mRequestQueue3 = Volley.newRequestQueue(TaskDetailActivity.this);
                                                StringRequest mStringRequest3 = new StringRequest(Request.Method.POST, WebService.SERVERURL,
                                                        new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
                                                                Log.d("mainactivity_resonse", response);
                                                                RequestQueue mRequestQueue2 = Volley.newRequestQueue(TaskDetailActivity.this);

                                                                StringRequest mStringRequest2 = new StringRequest(Request.Method.POST, WebService.SERVERURL,
                                                                        new Response.Listener<String>() {
                                                                            @Override
                                                                            public void onResponse(String response) {
                                                                                if(rotateAnimation!=null)
                                                                                {
                                                                                    if(rotateAnimation.isShowing())
                                                                                    {
                                                                                        rotateAnimation.dismiss();
                                                                                        rotateAnimation = null;
                                                                                    }
                                                                                }
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

                                                                                    impl = new Details_impl(TaskDetailActivity.this);
                                                                                    list= impl.getUserDetails1(strtaskid);
                                                                                    long det = impl.update(new TaskDetailCartModel(list.get(0).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list.get(0).getTask_id()), list.get(0).getTask_number(), list.get(0).getClient_name(), list.get(0).getDate(), list.get(0).getTime(), list.get(0).getAddress(),
                                                                                            list.get(0).getLatitude(), list.get(0).getLongitude(), list.get(0).getShort_description(), list.get(0).getDescription(), list.get(0).getAcknowledged_by(), list.get(0).getAcknowledged_date(), list.get(0).getAcknowledged_time(), list.get(0).getImage_path(), list.get(0).getImage_bitmap(),
                                                                                            list.get(0).getTask_process(), list.get(0).getDisposal_site_name(), list.get(0).getDisposal_site_id(), list.get(0).getDisposal_date(), list.get(0).getDisposal_time(), list.get(0).getJob_type(), list.get(0).getSecond_man(), list.get(0).getJob_started(), list.get(0).getSing_off_status(), list.get(0).getJob_start_datetime(),
                                                                                            task_sync_status, ss1, ss2, ss2_A, ss3, final_sync, list.get(0).getNew_rental(), list.get(0).getFinal_removal(),list.get(0).getInstructions()
                                                                                    ));

                                                                                } catch (JSONException e) {
                                                                                    e.printStackTrace();
                                                                                }

                                                                            }
                                                                        },
                                                                        new Response.ErrorListener() {
                                                                            @Override
                                                                            public void onErrorResponse(VolleyError error) {
                                                                                Toast.makeText(TaskDetailActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                                                                            }
                                                                        }) {
                                                                    @Override
                                                                    protected Map<String, String> getParams() {
                                                                        Map<String, String> params = new HashMap<String, String>();
                                                                        params.put(WebService.KEY_TASK, WebService.KEY_UPDATETASK_OFFLINE);
                                                                        params.put(WebService.KEY_TASKDETAIL_TASKIDDKEY, list.get(0).getTask_id());
                                                                        params.put("second_man", list.get(0).getSecond_man());
                                                                        params.put(WebService.KEY_UPDATETASKPROCESS, "In-progress");
                                                                        params.put(WebService.KEY_SIGNOFFDATETIME, list.get(0).getAcknowledged_date() + " " + list.get(0).getAcknowledged_time());
                                                                        params.put("task_sync_status", list.get(0).getTask_sync_status());
                                                                        params.put("ss1", list.get(0).getSs1());
                                                                        params.put("ss2", list.get(0).getSs2());
                                                                        params.put("ss2_A", list.get(0).getSs2_A());
                                                                        params.put("ss3", list.get(0).getSs3());
                                                                        params.put("final_sync", list.get(0).getFinal_sync());
                                                                            params.put(WebService.KEY_DISPOSAL_NAME, "");
                                                                            params.put(WebService.KEY_DISPOSAL_ID, "");
                                                                            params.put(WebService.KEY_TIPPINGBOOKDATETIME, list.get(0).getAcknowledged_date() + " " + list.get(0).getAcknowledged_time());



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
                                                                Toast.makeText(TaskDetailActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                                                            }
                                                        }) {
                                                    @Override
                                                    protected Map<String, String> getParams() {
                                                        Map<String, String> params = new HashMap<String, String>();
                                                        params.put(WebService.KEY_TASK, WebService.KEY_SIGN_OFFTASK_OFFLINE);
                                                        params.put(WebService.KEY_UPDATETASKID, list.get(0).getTask_id());
                                                        params.put(WebService.KEY_SIGN_OFF_STATUS, list.get(0).getSing_off_status());
                                                        params.put("second_man", list.get(0).getSecond_man());

                                                        return params;
                                                    }
                                                };

                                                mStringRequest3.setRetryPolicy(new DefaultRetryPolicy(
                                                        50000,
                                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                                                mRequestQueue3.add(mStringRequest3);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(TaskDetailActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                                        }
                                    }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put(WebService.KEY_TASK, "job_started_offline");
                                    params.put(WebService.KEY_TIPPINGBOOTASKID, list.get(0).getTask_id());
                                    params.put("second_man",list.get(0).getSecond_man());
                                    params.put("job_start_datetime", list.get(0).getJob_start_datetime());
                                    params.put("task_sync_status", list.get(0).getTask_sync_status());
                                    params.put("ss1", list.get(0).getSs1());
                                    params.put("ss2", list.get(0).getSs2());
                                    params.put("ss2_A", list.get(0).getSs2_A());
                                    params.put("ss3", list.get(0).getSs3());
                                    params.put("final_sync", list.get(0).getFinal_sync());


                                    return params;
                                }
                            };

                            mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                    50000,
                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                            mRequestQueue.add(mStringRequest);
                        }


//                        ArrayList<TaskListCartModel> ticket_list = new ArrayList<TaskListCartModel>();
//                        Main_impl main_impl = new Main_impl(TaskDetailActivity.this);
//                        ticket_list = main_impl.getUser();
//
//                        for (int i = 0; i < ticket_list.size(); i++) {
//                            if (ticket_list.get(i).getTask_id().equalsIgnoreCase(strtaskid)) {
//                                Main_impl main_impl2 = new Main_impl(TaskDetailActivity.this);
//                                ticket_list = main_impl.getUser();
//
//                                long det2 = main_impl2.update(new TaskListCartModel(ticket_list.get(i).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), ticket_list.get(i).getTask_id(), ticket_list.get(i).getTask_number(), ticket_list.get(i).getClient_name(), ticket_list.get(i).getAddress(),
//                                        ticket_list.get(i).getShort_description(), ticket_list.get(i).getJob_type(), "In-progress"));
//
//                            }
//                        }


//                        nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, WebService.KEY_SIGN_OFFTASK));
//                        nameValuePair.add(new BasicNameValuePair(WebService.KEY_UPDATETASKID, strtaskid));
//                        nameValuePair.add(new BasicNameValuePair(WebService.KEY_SIGN_OFF_STATUS, "NATS"));
//                        nameValuePair.add(new BasicNameValuePair("second_man", str_cat_id));


//                        impl = new Details_impl(TaskDetailActivity.this);
//                        list=impl.getUser();
//
//                        nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, WebService.KEY_SIGN_OFFTASK_OFFLINE));
//                        nameValuePair.add(new BasicNameValuePair(WebService.KEY_UPDATETASKID, list.get(0).getTask_id()));
//                        nameValuePair.add(new BasicNameValuePair(WebService.KEY_SIGN_OFF_STATUS, list.get(0).getSing_off_status()));
//                        nameValuePair.add(new BasicNameValuePair("second_man", list.get(0).getSecond_man()));
//
//                        if (connectionDetector.isConnectingToInternet()) {
//                            taskkey = "signoffstatus";
//                            asyncTaskClass = new AsyncTaskClass(context, TaskDetailActivity.this, nameValuePair, WebService.SERVERURL, true);
//                            asyncTaskClass.execute();
//                        } else {
//                            Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
//                            return;
//                        }

//                        if (connectionDetector.isConnectingToInternet()) {
//                            CallApiSequence();
//                        }
                    }

                }
            }
        });

        btn_hold.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (dialogsignoff != null) {
                    if (dialogsignoff.isShowing()) {
                        dialogsignoff.dismiss();
                    }
                }
//                if (dialogsignoff != null) {
//                    if (dialogsignoff.isShowing()) {
//                        dialogsignoff.dismiss();
//                        if (nameValuePair.size() > 0) {
//                            nameValuePair.clear();
//                        }
//
//                        nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, WebService.KEY_SIGN_OFFTASK));
//                        nameValuePair.add(new BasicNameValuePair(WebService.KEY_UPDATETASKID, strtaskid));
//                        nameValuePair.add(new BasicNameValuePair(WebService.KEY_SIGN_OFF_STATUS, "hold"));
//                        nameValuePair.add(new BasicNameValuePair("second_man", str_cat_id));
//
//                        if (connectionDetector.isConnectingToInternet()) {
//                            taskkey = "hold_status";
//                            asyncTaskClass = new AsyncTaskClass(context, TaskDetailActivity.this, nameValuePair, WebService.SERVERURL, true);
//                            asyncTaskClass.execute();
//                        } else {
//                            Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
//                            return;
//                        }
//                    }
//                }

//                }
            }
        });


        btn_save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (bitmap_sign!= null && !bitmap_sign.equals("null")) {

                    MyUtility.addFavoriteItem(TaskDetailActivity.this, strtaskid);

//                image_signoffshow.setImageBitmap(bitmap_sign);
//                txt_signature.setText(str_signaturename);
//                txt_signofftime.setText(strsignoffdate + " " + strsignofftime);

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

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap_sign.compress(Bitmap.CompressFormat.JPEG, 1, baos);
                    byte[] imageBytes = baos.toByteArray();
                    String encodedImage = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
                    globalVariable.SetImageBitmap(encodedImage);

//
                    impl = new Details_impl(TaskDetailActivity.this);
                    list = impl.getUserDetails1(strtaskid);
                    long det = impl.update(new TaskDetailCartModel(list.get(0).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list.get(0).getTask_id()), list.get(0).getTask_number(), list.get(0).getClient_name(), list.get(0).getDate(), list.get(0).getTime(), list.get(0).getAddress(),
                            list.get(0).getLatitude(), list.get(0).getLongitude(), list.get(0).getShort_description(), list.get(0).getDescription(), str_signaturename, strsignoffdate, strsignofftime, imagepath, encodedImage,
                            "In-progress", list.get(0).getDisposal_site_name(), list.get(0).getDisposal_site_id(), list.get(0).getDisposal_date(), list.get(0).getDisposal_time(), list.get(0).getJob_type(), str_cat_id, "1", list.get(0).getSing_off_status(), strsignoffdate + " " + strsignofftime,
                            "2", "1", "1", "0", "0", "0", list.get(0).getNew_rental(), list.get(0).getFinal_removal(),list.get(0).getInstructions()
                    ));
//
                    ShowTaskDetailDB();
                    if (connectionDetector.isConnectingToInternet()) {
                        new SubmitRoadSideAssistDataAsyTask().execute();


                    }

//                ArrayList<TaskListCartModel> ticket_list = new ArrayList<TaskListCartModel>();
//                Main_impl main_impl = new Main_impl(TaskDetailActivity.this);
//                ticket_list = main_impl.getUser();
//
//                for (int i = 0; i < ticket_list.size(); i++) {
//                    if (ticket_list.get(i).getTask_id().equalsIgnoreCase(strtaskid)) {
//                        Main_impl main_impl2 = new Main_impl(TaskDetailActivity.this);
//                        ticket_list = main_impl.getUser();
//
//                        long det2 = main_impl2.update(new TaskListCartModel(ticket_list.get(i).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), ticket_list.get(i).getTask_id(), ticket_list.get(i).getTask_number(), ticket_list.get(i).getClient_name(), ticket_list.get(i).getAddress(),
//                                ticket_list.get(i).getShort_description(), ticket_list.get(i).getJob_type(), "In-progress"));
//
//                    }
//                }

//
//
                    if (dialogsignoff != null) {
                        if (dialogsignoff.isShowing()) {
                            dialogsignoff.dismiss();
                        }
                    }

//                if (connectionDetector.isConnectingToInternet()) {
//                    CallApiSequence();
//                }
//                new SubmitRoadSideAssistDataAsyTask().execute();
                }else{
                    Toast.makeText(TaskDetailActivity.this, "Please record the signature first", Toast.LENGTH_SHORT).show();
                }
            }
        });


        dialogsignoff.show();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SIGNATURE_ACTIVITY:
                if (resultCode == RESULT_OK) {

                    Bundle bundle = data.getExtras();
                    String status = bundle.getString("status");
                    imagepath = bundle.getString("path");
                    str_signaturename = bundle.getString("name");
                    if (status.equalsIgnoreCase("done")) {
                        Toast toast = Toast.makeText(this, "Signature capture successful!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 105, 50);
                        toast.show();
                        //convert the string to byte array
                        //byte[] imageAsBytes = Base64.decode(bundle.getBytes());
                        //get reference to the image view where you want to display the image
                        //ImageView image = (ImageView)this.findViewById(R.id.ImageView);
                        //set the image by decoding the byte array to bitmap
                        //image.setImageBitmap(
                        // ..BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)
                        //);
                        //Uri u = data.getData();
                        str_signature = imagepath;
                        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                        Bitmap bitmap = BitmapFactory.decodeFile(imagepath, bmOptions);
                        bitmap = Bitmap.createScaledBitmap(bitmap, 381, 250, true);
                        bitmap_sign = bitmap;
                        DialogSignOff(bitmap);
                    }
                }
                break;
        }

    }

    public void DialogTicketDetails() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_ticketdetails);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        final Spinner spn_ticet = (Spinner) dialog.findViewById(R.id.spn_ticket);
        Spinner spn_booknumber = (Spinner) dialog.findViewById(R.id.spn_booknumber);
        Button btn_save = (Button) dialog.findViewById(R.id.btn_save);
        //int po =0;

        BookTicketSpinnerAdapter sipnnerAdapter = new BookTicketSpinnerAdapter(context, taskDetailParser.GetTippingBookList());
        spn_booknumber.setAdapter(sipnnerAdapter);
        spn_booknumber.setSelection(0, false);

        spn_booknumber.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                spinnerbookposition = position;
                //String name = taskDetailParser.GetTippingBookList().get(position).get(WebService.KEY_TASKDETAIL_BOONUMBER);
                //Toast.makeText(context, "item="+name, 1).show();
                HashMap<String, String> map1 = new HashMap<String, String>();
                map1.put(WebService.KEY_TASKDETAIL_BOOKID, taskDetailParser.GetTippingBookList().get(position).get(WebService.KEY_TASKDETAIL_BOOKID));
                map1.put(WebService.KEY_TASKDETAIL_BOONUMBER, taskDetailParser.GetTippingBookList().get(position).get(WebService.KEY_TASKDETAIL_BOONUMBER));
                map1.put(WebService.KEY_TASKDETAIL_WHOLEBOOK, taskDetailParser.GetTippingBookList().get(position).get(WebService.KEY_TASKDETAIL_WHOLEBOOK));
                arrbook.add(map1);

                str_boonumbervalue = taskDetailParser.GetTippingBookList().get(position).get(WebService.KEY_TASKDETAIL_BOOKID);

                if (taskDetailParser.GetTippingBookList().get(position).get(WebService.KEY_TASKDETAIL_WHOLEBOOK).equals("0")) {

                    String numbers = taskDetailParser.GetTippingBookList().get(position).get("numbers");
                    //Toast.makeText(context, "item="+numbers, 1).show();
                    mylist = new ArrayList<String>();
                    mylist.add("please choose ticket");
                    //ArrayList aList= (ArrayList) Arrays.asList(numbers.split("\\*"));
                    //String arr[] = null;
                    //arr[0] = "please choose ticket";
                    String arr[] = numbers.split("\\*");
                    for (int i = 0; i < arr.length; i++) {
                        mylist.add(arr[i]);
                    }
                    ticketSpinnerAdapter = new TicketSpinnerAdapter(context, mylist);
                    spn_ticet.setAdapter(ticketSpinnerAdapter);
                    spn_ticet.setSelection(0, false);
                    //Log.i(DEBUG_TAG, "ticet numbers="+arr[0]);
                    //Log.i(DEBUG_TAG, "ticet numbers="+arr[1]);
                    //Log.i(DEBUG_TAG, "ticet numbers="+arr[2]);


                } else {
                    mylist = new ArrayList<String>();
                    mylist.add("please choose ticket");
                    mylist.add("0");
                    ticketSpinnerAdapter = new TicketSpinnerAdapter(context, mylist);
                    spn_ticet.setAdapter(ticketSpinnerAdapter);
                    spn_ticet.setSelection(0, false);
                }


                //bookAddAdapter = new BookAddAdapter(context, arrbook, TaskDetailActivity.this);
                //listView_tippingbook.setAdapter(bookAddAdapter);

                //DialogAddTicket(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });


        spn_ticet.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                str_ticketvalue = mylist.get(position);
                //ticketAddAdapter = new TicketAddAdapter(context, mylist, TaskDetailActivity.this);
                //listView_tippingticket.setAdapter(ticketAddAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        btn_cancel.setOnClickListener(new OnClickListener() {

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

        btn_save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (str_boonumbervalue.equals("")) {
                    Toast.makeText(TaskDetailActivity.this, "Please Select Book Number", Toast.LENGTH_SHORT).show();
                } else if (str_ticketvalue.equals("please choose ticket")) {
                    Toast.makeText(TaskDetailActivity.this, "Please Select Ticket", Toast.LENGTH_SHORT).show();
                } else {
                    if (nameValuePair.size() > 0) {
                        nameValuePair.clear();
                    }
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
                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, WebService.KEY_TIPPINGBOOKTASK));
                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_TIPPINGBOOTASKID, strtaskid));
                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_TIPPINGBOOKID, str_boonumbervalue));
                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_TIPPINGBOOKTICKET, str_ticketvalue));
                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_TIPPINGBOOKDATETIME, strsignoffdate + " " + strsignofftime));


                    if (connectionDetector.isConnectingToInternet()) {
                        taskkey = "savetippingbook";
                        asyncTaskClass = new AsyncTaskClass(context, TaskDetailActivity.this, nameValuePair, WebService.SERVERURL, true);
                        asyncTaskClass.execute();
                        if (dialog != null) {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }
                    } else {
//                        Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
                        return;
                    }


                }
            }
        });

        dialog.show();
    }


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
		/*if(nameValuePair.size()>0)
		{
			nameValuePair.clear();
		}
		
		nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, WebService.KEY_TASKDETAIL_TASK));
		nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASKDETAIL_TASKIDDKEY, strtaskid));
		
		if(connectionDetector.isConnectingToInternet())
		{
			taskkey = "detail";
			asyncTaskClass = new AsyncTaskClass(context, TaskDetailActivity.this, nameValuePair, WebService.SERVERURL,false);
			asyncTaskClass.execute();
		}
		else
		{
			Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
			return;
		}*/
    }
	
	
	/*public void DialogAddBook()
	{
		final Dialog dialog = new Dialog(context);
    	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    	dialog.setContentView(R.layout.dialog_addticket);
    	dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
    	TextView textView = (TextView)dialog.findViewById(R.id.txt_dialogtitle);
    	Spinner spinner = (Spinner)dialog.findViewById(R.id.spn_tiket);
    	
    	BookTicketSpinnerAdapter sipnnerAdapter = new BookTicketSpinnerAdapter(context, taskDetailParser.GetTippingBookList());
    	spinner.setAdapter(sipnnerAdapter);
    	spinner.setSelection(0, false);
    	textView.setText("Please Choose Tipping Book");
    	
    	
    	spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				String name = taskDetailParser.GetTippingBookList().get(position).get(WebService.KEY_TASKDETAIL_BOONUMBER);
				Toast.makeText(context, "item="+name, 1).show();
				HashMap<String, String>map1 = new HashMap<String, String>();
				map1.put(WebService.KEY_TASKDETAIL_BOOKID, taskDetailParser.GetTippingBookList().get(position).get(WebService.KEY_TASKDETAIL_BOOKID));
	    		map1.put(WebService.KEY_TASKDETAIL_BOONUMBER, taskDetailParser.GetTippingBookList().get(position).get(WebService.KEY_TASKDETAIL_BOONUMBER));
	    		map1.put(WebService.KEY_TASKDETAIL_WHOLEBOOK, taskDetailParser.GetTippingBookList().get(position).get(WebService.KEY_TASKDETAIL_WHOLEBOOK));
				arrbook.add(map1);
				bookAddAdapter = new BookAddAdapter(context, arrbook, TaskDetailActivity.this);
				listView_tippingbook.setAdapter(bookAddAdapter);
				if(dialog!=null)
				{
					if(dialog.isShowing())
					{
						dialog.dismiss();
					}
				}
				DialogAddTicket(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
    	
    	
    	
    	buttonadd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(editText.getText().toString().length()>0)
				{
					Toast.makeText(context, "itemsbefore="+arrticket.toString(), 1).show();
					arrbook.add(editText.getText().toString().trim());
					bookAddAdapter = new BookAddAdapter(context, arrbook, TaskDetailActivity.this);
					listView_tippingbook.setAdapter(bookAddAdapter);
					Toast.makeText(context, "itemsafter="+arrbook.toString(), 1).show();
					if(dialog!=null)
					{
						if(dialog.isShowing())
						{
							dialog.dismiss();
						}
					}
				}
				else
				{
					editText.setError("Please enter Book");
				}
			}
		});
    	
    	
    	dialog.show();
	}
	*/
	/*public void DialogAddTicket(int position)
	{
		final Dialog dialog = new Dialog(context);
    	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    	dialog.setContentView(R.layout.dialog_addticket);
    	dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
    	TextView textView = (TextView)dialog.findViewById(R.id.txt_dialogtitle);
    	
    	Spinner spinner = (Spinner)dialog.findViewById(R.id.spn_tiket);
    	textView.setText("Please Choose Tipping Ticket");
    	
    	if(taskDetailParser.GetTippingBookList().get(position).get(WebService.KEY_TASKDETAIL_WHOLEBOOK).equals("0"))
    	{
    		
    		String numbers = taskDetailParser.GetTippingBookList().get(position).get("numbers");
    		
    		mylist.add("please choose ticket");
    		//ArrayList aList= (ArrayList) Arrays.asList(numbers.split("\\*"));
    		 //String arr[] = null;
    		//arr[0] = "please choose ticket";
    		String arr[]  = numbers.split("\\*");
    		for(int i =0;i<arr.length;i++)
    		{
    			mylist.add(arr[i]);
    		}
    		//Log.i(DEBUG_TAG, "ticet numbers="+arr[0]);
    		//Log.i(DEBUG_TAG, "ticet numbers="+arr[1]);
    		//Log.i(DEBUG_TAG, "ticet numbers="+arr[2]);
    		
    		TicketSpinnerAdapter ticketSpinnerAdapter = new TicketSpinnerAdapter(context, mylist);
    		spinner.setAdapter(ticketSpinnerAdapter);
    		spinner.setSelection(0, false);
    		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					ticketAddAdapter = new TicketAddAdapter(context, mylist, TaskDetailActivity.this);
					listView_tippingticket.setAdapter(ticketAddAdapter);
					
					if(dialog!=null)
					{
						if(dialog.isShowing())
						{
							dialog.dismiss();
						}
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					
				}
			});
    		
    	}
    	else
    	{
    		
    	}
    	
    	buttonadd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(editText.getText().toString().length()>0)
				{
					Toast.makeText(context, "itemsbefore="+arrticket.toString(), 1).show();
					arrticket.add(editText.getText().toString().trim());
					ticketAddAdapter = new TicketAddAdapter(context, arrticket, TaskDetailActivity.this);
					listView_tippingticket.setAdapter(ticketAddAdapter);
					Toast.makeText(context, "itemsafter="+arrticket.toString(), 1).show();
					if(dialog!=null)
					{
						if(dialog.isShowing())
						{
							dialog.dismiss();
						}
					}
				}
				else
				{
					editText.setError("Please enter ticket");
				}
			}
		});
    	
    	
    	dialog.show();
	}*/

    @Override
    public void OnBtnRemoveTicketClick(int position) {
        // TODO Auto-generated method stub
        if (nameValuePair.size() > 0) {
            nameValuePair.clear();
        }

        nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, WebService.KEY_REMOVETIPPINGBOOKTASK));
        nameValuePair.add(new BasicNameValuePair(WebService.KEY_REMOVETIPPINGBOOKTASKID, strtaskid));
        nameValuePair.add(new BasicNameValuePair("used_id", taskDetailParser.GetUsedTippingBookList().get(position).get(WebService.KEY_TASKDETAIL_USEDTICKETID)));
        //nameValuePair.add(new BasicNameValuePair(WebService.KEY_REMOVETIPPINGBOOKTICKET, str_ticketvalue));


        if (connectionDetector.isConnectingToInternet()) {
            taskkey = "detail";
            asyncTaskClass = new AsyncTaskClass(context, TaskDetailActivity.this, nameValuePair, WebService.SERVERURL, false);
            asyncTaskClass.execute();

        } else {
//            Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
            return;
        }
    }

    @Override
    public void OnBtnRemoveBookClick(int position) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onTaskComplete(String result) {
        // TODO Auto-generated method stub
        Log.i(DEBUG_TAG, "response from Details=" + result);
        try {
            if (taskkey.equalsIgnoreCase("detail")) {
                taskDetailParser = new TaskDetailParser(context);
                taskDetailParser.JsonStringToArrayList(result);

                JSONObject jsonObject = new JSONObject(result);

                String task_id = jsonObject.getString("id");
                String task_number = jsonObject.getString("task_number");
                String client_name = jsonObject.getString("client_name");
                String date = jsonObject.getString("date");
                String time = jsonObject.getString("time");
                String address = jsonObject.getString("address");
                String latitude = jsonObject.getString("latitude");
                String longitude = jsonObject.getString("longitude");
                String short_description = jsonObject.getString("short_description");
                String description = jsonObject.getString("description");
                String acknowledged_by = jsonObject.getString("acknowledged_by");
                String acknowledged_date = jsonObject.getString("acknowledged_date");
                String acknowledged_time = jsonObject.getString("acknowledged_time");
                String image_path = jsonObject.getString("image_path");
                String task_process = jsonObject.getString("task_process");
                String disposal_site_name = jsonObject.getString("disposal_site_name");
                String disposal_site_id = jsonObject.getString("disposal_site_id");
                String disposal_date = jsonObject.getString("disposal_date");
                String disposal_time = jsonObject.getString("disposal_time");
                String job_type = jsonObject.getString("job_type");
                String second_man = jsonObject.getString("second_man");
                String job_started = jsonObject.getString("job_started");
                String sing_off_status = jsonObject.getString("sing_off_status");
                String job_start_datetime = jsonObject.getString("job_start_datetime");
                String task_sync_status = jsonObject.getString("task_sync_status");
                String ss1 = jsonObject.getString("ss1");
                String ss2 = jsonObject.getString("ss2");
                String ss2_A = jsonObject.getString("ss2_A");
                String ss3 = jsonObject.getString("ss3");
                String final_sync = jsonObject.getString("final_sync");
                String instructions = jsonObject.getString("instructions");

                impl = new Details_impl(TaskDetailActivity.this);
                long det = impl.insert(new TaskDetailCartModel(0,sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(task_id), task_number, client_name, date, time, address,
                        latitude, longitude, short_description, description, acknowledged_by, acknowledged_date, acknowledged_time, image_path,"",
                        task_process, disposal_site_name, disposal_site_id, disposal_date, disposal_time, job_type, second_man, job_started, sing_off_status
                        , job_start_datetime, task_sync_status, ss1, ss2,ss2_A ,ss3, final_sync,"","",instructions
                ));

                if (instructions.equalsIgnoreCase("")){
                    lin_instruction.setVisibility(View.VISIBLE);
                }else{
                    lin_instruction.setVisibility(View.GONE);
                }

                txt_instruction.setText(instructions);

                txt_tasknumber.setText(taskDetailParser.GetTaskDetailList().get(0).get(WebService.KEY_TASKDETAIL_TASKNUMBER));
                txt_jobtype.setText(taskDetailParser.GetTaskDetailList().get(0).get(WebService.KEY_TASKDETAIL_JOBTYPE));
                txt_clientname.setText(taskDetailParser.GetTaskDetailList().get(0).get(WebService.KEY_TASKDETAIL_CLIENTNAME));
                txt_date.setText(taskDetailParser.GetTaskDetailList().get(0).get(WebService.KEY_TASKDETAIL_DATE));
                txt_time.setText(taskDetailParser.GetTaskDetailList().get(0).get(WebService.KEY_TASKDETAIL_TIME));
                txt_description.setText(taskDetailParser.GetTaskDetailList().get(0).get(WebService.KEY_TASKDETAIL_DESCRIPTION));
                txt_signature.setText(taskDetailParser.GetTaskDetailList().get(0).get(WebService.KEY_TASKDETAIL_ACKNOWLEDGEDBY));
                txt_signofftime.setText(taskDetailParser.GetTaskDetailList().get(0).get(WebService.KEY_TASKDETAIL_ACKNOWLEDGEDDATE) + " " +
                        taskDetailParser.GetTaskDetailList().get(0).get(WebService.KEY_TASKDETAIL_ACKNOWLEDGEDTIME));
                Picasso.with(context).load(taskDetailParser.GetTaskDetailList().get(0).get(WebService.KEY_TASKDETAIL_ACKNOWLEDGEDIMAGE)).into(image_signoffshow);
                txt_disposalsite.setText(taskDetailParser.GetTaskDetailList().get(0).get(WebService.KEY_TASKDETAIL_DISPOSALSITE));
                txt_disposalsiteid.setText(taskDetailParser.GetTaskDetailList().get(0).get(WebService.KEY_TASKDETAIL_DISPOSALSITEID));

                txt_disposaldate.setText(taskDetailParser.GetTaskDetailList().get(0).get(WebService.KEY_TASKDETAIL_DISPOSALDATE));
                txt_disposaltime.setText(taskDetailParser.GetTaskDetailList().get(0).get(WebService.KEY_TASKDETAIL_DISPOSALTIME));
                //GetUpdateStatusArray(taskDetailParser.GetTaskDetailList().get(0).get(WebService.KEY_TASKDETAIL_TASKPROCESS));
                btn_status.setText(taskDetailParser.GetTaskDetailList().get(0).get(WebService.KEY_TASKDETAIL_TASKPROCESS));
                if (taskDetailParser.GetTaskDetailList().get(0).get(WebService.KEY_TASKDETAIL_TASKPROCESS).equalsIgnoreCase("In-progress")) {
                    spn_second_man.setEnabled(false);
                    spn_second_man.setClickable(false);
                }

                if (taskDetailParser.GetTaskDetailList().get(0).get("job_started").equalsIgnoreCase("0")) {
                    jobstart = 0;
                    btn_start.setBackgroundResource(R.color.app_loginbtn);
                    btn_acknowledge.setBackgroundResource(R.color.app_loginbtn_disable);
                } else {
                    jobstart = 1;
                    btn_start.setBackgroundResource(R.color.app_loginbtn_disable);
                    btn_start.setText("STARTED");
                    btn_acknowledge.setBackgroundResource(R.color.app_loginbtn);

//                    spn_second_man.setEnabled(false);
//                    spn_second_man.setClickable(false);
                }


                BookAddAdapter bookAddAdapter = new BookAddAdapter(context, taskDetailParser.GetUsedTippingBookList(), TaskDetailActivity.this);
                listView_tippingbook.setAdapter(bookAddAdapter);
                TicketAddAdapter ticketAddAdapter = new TicketAddAdapter(context, taskDetailParser.GetUsedTippingBookList(), TaskDetailActivity.this);
                listView_tippingticket.setAdapter(ticketAddAdapter);
                Gson gson2 = new Gson();
                arrLike.clear();
                JSONArray jsonArrayLike = new JSONArray(taskDetailParser.GetTaskDetailList().get(0).get(WebService.KEY_TASKDETAIL_DISPOSALSITE));
                for (int i = 0; i < jsonArrayLike.length(); i++) {
                    JSONObject arrayElement_0 = jsonArrayLike.getJSONObject(i);
                    arrLike.add(gson2.fromJson(String.valueOf(arrayElement_0), DisposalModel.class));
                }

                if (final_Status.equals("completed")) {
                    spn_disposal.setEnabled(false);
                } else {
                    disposalAdapter = new DisposalAdapter(context, arrLike);
                    spn_disposal.setAdapter(disposalAdapter);
                }
//                attemptSecondMan();
                if (arrSecond2.size() < 1) {
                    attemptSecondMan();
                } else {

                    secondManAdapter = new SecondManAdapter(context, arrSecond2);
                    spn_second_man.setAdapter(secondManAdapter);

                    for (int i = 0; i < arrSecond2.size(); i++) {
                        if (impl.isExist(strtaskid)) {
                            list = impl.getUserDetails1(strtaskid);
                            if (list.get(0).getSecond_man().equalsIgnoreCase(arrSecond2.get(i).getManId())) {
                                spn_second_man.setSelection(i);
                                exist = 1;
                                str_cat_id = arrSecond2.get(i).getManId();
                            } else {

                            }
                        }

                    }
                }
            } else if (taskkey.equalsIgnoreCase("job_started")) {
                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString("status");
                String message = jsonObject.getString("message");
                String task_sync_status = jsonObject.getString("task_sync_status");
                String ss1 = jsonObject.getString("ss1");
                String ss2 = jsonObject.getString("ss2");
                String ss2_A = jsonObject.getString("ss2_A");
                String ss3 = jsonObject.getString("ss3");
                String final_sync = jsonObject.getString("final_sync");

                impl = new Details_impl(TaskDetailActivity.this);
                list = impl.getUserDetails1(strtaskid);
                long det = impl.update(new TaskDetailCartModel(list.get(0).getId(),sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list.get(0).getTask_id()), list.get(0).getTask_number(), list.get(0).getClient_name(), list.get(0).getDate(), list.get(0).getTime(), list.get(0).getAddress(),
                        list.get(0).getLatitude(), list.get(0).getLongitude(), list.get(0).getShort_description(), list.get(0).getDescription(), list.get(0).getAcknowledged_by(), list.get(0).getAcknowledged_date(), list.get(0).getAcknowledged_time(), list.get(0).getImage_path(),list.get(0).getImage_bitmap(),
                        list.get(0).getTask_process(), list.get(0).getDisposal_site_name(), list.get(0).getDisposal_site_id(), list.get(0).getDisposal_date(), list.get(0).getDisposal_time(), list.get(0).getJob_type(), str_cat_id, list.get(0).getJob_started(), list.get(0).getSing_off_status(), strsignoffdate + " " + strsignofftime,
                        task_sync_status, ss1, ss2,ss2_A, ss3, final_sync,list.get(0).getNew_rental(),list.get(0).getFinal_removal(),list.get(0).getInstructions()
                ));

                ShowTaskDetailDB();
//                if (nameValuePair.size() > 0) {
//                    nameValuePair.clear();
//                }
//
//                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, WebService.KEY_TASKDETAIL_TASK));
//                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASKDETAIL_TASKIDDKEY, strtaskid));
//
//                if (connectionDetector.isConnectingToInternet()) {
//                    taskkey = "detail";
//                    asyncTaskClass = new AsyncTaskClass(context, TaskDetailActivity.this, nameValuePair, WebService.SERVERURL, false);
//                    asyncTaskClass.execute();
//                } else {
//                    Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
//                    return;
//                }
            } else if (taskkey.equalsIgnoreCase("second")) {
                arrSecond.clear();
                arrSecond.add(0, new SecondManModel("0", "Please select", ""));
                arrSecond2.clear();
                arrSecond2.add(0, new SecondMan2Model(0, "0", "Please select", ""));

                Gson gson2 = new Gson();
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject arrayElement_0 = jsonArray.getJSONObject(i);
                    arrSecond.add(gson2.fromJson(String.valueOf(arrayElement_0), SecondManModel.class));

                    String man_id = arrayElement_0.getString("id");
                    String names = arrayElement_0.getString("name");
                    String emails = arrayElement_0.getString("email");

                    secondMan_impl = new SecondMan_impl(TaskDetailActivity.this);
                    long det = secondMan_impl.insert(new SecondMan2Model(0, man_id, names, emails));
                }

                arrSecond2 = secondMan_impl.getUser();

                secondManAdapter = new SecondManAdapter(context, arrSecond2);
                spn_second_man.setAdapter(secondManAdapter);

                for (int i = 0; i < arrSecond2.size(); i++) {
                    JSONObject arrayElement_0 = jsonArray.getJSONObject(i);
                    if (list.size() < 1) {
                        if (taskDetailParser.GetTaskDetailList().get(0).get("second_man").equalsIgnoreCase(arrayElement_0.getString("id"))) {
                            spn_second_man.setSelection(i);
                            exist = 1;
                            str_cat_id = arrSecond2.get(i).getManId();
                        } else {

                        }
                    } else {
                        if (impl.isExist(strtaskid)) {
                            list = impl.getUserDetails1(strtaskid);
                            if (list.get(0).getSecond_man().equalsIgnoreCase(arrSecond2.get(i).getManId())) {
                                spn_second_man.setSelection(i);
                                exist = 1;
                                str_cat_id = arrSecond2.get(i).getManId();
                            } else {

                            }
                        } else {
                            if (taskDetailParser.GetTaskDetailList().get(0).get("second_man").equalsIgnoreCase(arrSecond2.get(i).getManId())) {
                                spn_second_man.setSelection(i);
                                exist = 1;
                                str_cat_id = arrSecond2.get(i).getManId();
                            } else {

                            }
                        }

                    }
                }

            } else if (taskkey.equalsIgnoreCase("signoff")) {
                JSONObject jsonObject = new JSONObject(result);
                //String status = jsonObject.optString(WebService.KEY_LOGIN_STATUS);
                String message = jsonObject.optString(WebService.KEY_LOGIN_MESSAGE);
                if (message.toString().length() > 0) {
                    Toast.makeText(context, message, 1).show();
                }


                if (nameValuePair.size() > 0) {
                    nameValuePair.clear();
                }

                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, WebService.KEY_TASKDETAIL_TASK));
                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASKDETAIL_TASKIDDKEY, strtaskid));

                if (connectionDetector.isConnectingToInternet()) {
                    taskkey = "detail";
                    asyncTaskClass = new AsyncTaskClass(context, TaskDetailActivity.this, nameValuePair, WebService.SERVERURL, false);
                    asyncTaskClass.execute();
                } else {
                    Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
                    return;
                }
            } else if (taskkey.equalsIgnoreCase("savetippingbook")) {
                JSONObject jsonObject = new JSONObject(result);
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

                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, WebService.KEY_UPDATETASK));
                nameValuePair.add(new BasicNameValuePair(WebService.KEY_UPDATETASKID, strtaskid));
                nameValuePair.add(new BasicNameValuePair("second_man", str_cat_id));
                nameValuePair.add(new BasicNameValuePair(WebService.KEY_UPDATETASKPROCESS, "Completed"));
                nameValuePair.add(new BasicNameValuePair(WebService.KEY_SIGNOFFDATETIME, strsignoffdate + " " + strsignofftime));
                final_Status = "completed";
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
                    taskkey = "statusupdate";
                    asyncTaskClass = new AsyncTaskClass(context, TaskDetailActivity.this, nameValuePair, WebService.SERVERURL, false);
                    asyncTaskClass.execute();
                } else {
                    Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
                    return;
                }
            } else if (taskkey.equalsIgnoreCase("statusupdate")) {
                //String status = jsonObject.optString(WebService.KEY_LOGIN_STATUS);
                //String message = jsonObject.optString(WebService.KEY_LOGIN_MESSAGE);
				/*if(message.toString().length()>0)
				{
					Toast.makeText(context, message, 1).show();	
				}*/
//				Intent intent = new Intent(TaskDetailActivity.this, TaskDetailActivity.class);
//				intent.putExtra("id", strtaskid);
//				startActivity(intent);
//				finish();


                spn_second_man.setEnabled(false);
                spn_second_man.setClickable(false);

                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString("status");
                String message = jsonObject.getString("message");
                String task_sync_status = jsonObject.getString("task_sync_status");
                String ss1 = jsonObject.getString("ss1");
                String ss2 = jsonObject.getString("ss2");
                String ss2_A = jsonObject.getString("ss2_A");
                String ss3 = jsonObject.getString("ss3");
                String final_sync = jsonObject.getString("final_sync");

                ArrayList<TaskListCartModel> ticket_list = new ArrayList<TaskListCartModel>();
                Main_impl main_impl = new Main_impl(this);
                ticket_list = main_impl.getUser();

                for (int i = 0; i < ticket_list.size(); i++) {
                    if (ticket_list.get(i).getTask_id().equalsIgnoreCase(strtaskid)) {
                        Main_impl main_impl2 = new Main_impl(this);
                        ticket_list = main_impl.getUser();

                        long det = main_impl2.update(new TaskListCartModel(ticket_list.get(i).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), ticket_list.get(i).getTask_id(), ticket_list.get(i).getTask_number(), ticket_list.get(i).getClient_name(), ticket_list.get(i).getAddress(),
                                ticket_list.get(i).getShort_description(), ticket_list.get(i).getJob_type(), "In-progress"));

                    }
                }

                impl = new Details_impl(TaskDetailActivity.this);
                list = impl.getUserDetails1(strtaskid);
                long det = impl.update(new TaskDetailCartModel(list.get(0).getId(),sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list.get(0).getTask_id()), list.get(0).getTask_number(), list.get(0).getClient_name(), list.get(0).getDate(), list.get(0).getTime(), list.get(0).getAddress(),
                        list.get(0).getLatitude(), list.get(0).getLongitude(), list.get(0).getShort_description(), list.get(0).getDescription(), list.get(0).getAcknowledged_by(), list.get(0).getAcknowledged_date(), list.get(0).getAcknowledged_time(), list.get(0).getImage_path(),list.get(0).getImage_bitmap(),
                        list.get(0).getTask_process(), list.get(0).getDisposal_site_name(), list.get(0).getDisposal_site_id(), list.get(0).getDisposal_date(), list.get(0).getDisposal_time(), list.get(0).getJob_type(), str_cat_id, "1", list.get(0).getSing_off_status(), strsignoffdate + " " + strsignofftime,
                        task_sync_status, ss1, ss2,ss2_A, ss3, final_sync,list.get(0).getNew_rental(),list.get(0).getFinal_removal(),list.get(0).getInstructions()
                ));

                ShowTaskDetailDB();

//                spn_second_man.setEnabled(false);
//                spn_second_man.setClickable(false);
//
//                if (final_Status.equals("completed")) {
//                    spn_disposal.setEnabled(false);
//                    btn_status.setText("Completed");
//                }
//                if (nameValuePair.size() > 0) {
//                    nameValuePair.clear();
//                }
//
//                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, WebService.KEY_TASKDETAIL_TASK));
//                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASKDETAIL_TASKIDDKEY, strtaskid));
//
//                if (connectionDetector.isConnectingToInternet()) {
//                    taskkey = "detail";
//                    asyncTaskClass = new AsyncTaskClass(context, TaskDetailActivity.this, nameValuePair, WebService.SERVERURL, true);
//                    asyncTaskClass.execute();
//                } else {
//                    Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
//                    return;
//                }
            } else if (taskkey.equalsIgnoreCase("email")) {
                JSONObject jsonObject = new JSONObject(result);
                //String status = jsonObject.optString(WebService.KEY_LOGIN_STATUS);
                String message = jsonObject.optString(WebService.KEY_LOGIN_MESSAGE);
                if (message.toString().length() > 0) {
                    Toast.makeText(context, message, 1).show();
                }
            } else if (taskkey.equalsIgnoreCase("signoffstatus")) {
                //JSONObject jsonObject = new JSONObject(result);
                //String status = jsonObject.optString(WebService.KEY_LOGIN_STATUS);
                //String message = jsonObject.optString(WebService.KEY_LOGIN_MESSAGE);
				/*if(message.toString().length()>0)
				{
					Toast.makeText(context, message, 1).show();
				}*/
//                SimpleDateFormat myFormatdate = new SimpleDateFormat("yyyy-MM-dd");//Y-m-d H:i:s  2016-09-19 15:09:00
//                SimpleDateFormat myFormattime = new SimpleDateFormat("HH:mm:ss");
//                try {
//                    Date date = new Date();
//                    strsignoffdate = myFormatdate.format(date);
//                    strsignofftime = myFormattime.format(date);
//
//                    Log.i(DEBUG_TAG, "Current Date Time in give format: " + strsignoffdate + " " + strsignofftime);
//                } catch (ParseException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }

                impl = new Details_impl(TaskDetailActivity.this);
                list = impl.getUserDetails1(strtaskid);


                if (nameValuePair.size() > 0) {
                    nameValuePair.clear();
                }

//                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, WebService.KEY_UPDATETASK));
//                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASKDETAIL_TASKIDDKEY, strtaskid));
//                nameValuePair.add(new BasicNameValuePair("second_man", str_cat_id));
//                nameValuePair.add(new BasicNameValuePair(WebService.KEY_UPDATETASKPROCESS, "In-progress"));
//                nameValuePair.add(new BasicNameValuePair(WebService.KEY_SIGNOFFDATETIME, strsignoffdate + " " + strsignofftime));
//                if (!txt_disposalsiteid.getText().toString().equals("")) {
//                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_DISPOSAL_NAME, tx_disposal.getText().toString()));
//                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_DISPOSAL_ID, txt_disposalsiteid.getText().toString()));
//                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_TIPPINGBOOKDATETIME, strsignoffdate + " " + strsignofftime));
//
//                } else {
//                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_DISPOSAL_NAME, ""));
//                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_DISPOSAL_ID, ""));
//                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_TIPPINGBOOKDATETIME, strsignoffdate + " " + strsignofftime));
//
//                }

                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, WebService.KEY_UPDATETASK_OFFLINE));
                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASKDETAIL_TASKIDDKEY, list.get(0).getTask_id()));
                nameValuePair.add(new BasicNameValuePair("second_man", list.get(0).getSecond_man()));
                nameValuePair.add(new BasicNameValuePair(WebService.KEY_UPDATETASKPROCESS, list.get(0).getTask_process()));
                nameValuePair.add(new BasicNameValuePair(WebService.KEY_SIGNOFFDATETIME, list.get(0).getAcknowledged_date() + " " + list.get(0).getAcknowledged_time()));
                nameValuePair.add(new BasicNameValuePair("task_sync_status", list.get(0).getTask_sync_status()));
                nameValuePair.add(new BasicNameValuePair("ss1", list.get(0).getSs1()));
                nameValuePair.add(new BasicNameValuePair("ss2", list.get(0).getSs2()));
                nameValuePair.add(new BasicNameValuePair("ss3", list.get(0).getSs3()));
                nameValuePair.add(new BasicNameValuePair("final_sync", list.get(0).getFinal_sync()));
                if (!txt_disposalsiteid.getText().toString().equals("")) {
                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_DISPOSAL_NAME, ""));
                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_DISPOSAL_ID, ""));
                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_TIPPINGBOOKDATETIME, list.get(0).getAcknowledged_date() + " " + list.get(0).getAcknowledged_time()));
                } else {
                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_DISPOSAL_NAME, ""));
                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_DISPOSAL_ID, ""));
                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_TIPPINGBOOKDATETIME, list.get(0).getAcknowledged_date() + " " + list.get(0).getAcknowledged_time()));
                }

                if (connectionDetector.isConnectingToInternet()) {
                    taskkey = "notavailablesign";
                    asyncTaskClass = new AsyncTaskClass(context, TaskDetailActivity.this, nameValuePair, WebService.SERVERURL, true);
                    asyncTaskClass.execute();
                } else {
                    Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
                    return;
                }
            } else if (taskkey.equalsIgnoreCase("notavailablesign")) {
//				Toast.makeText(this, "Not Availble Sign Successfully", Toast.LENGTH_SHORT).show();

                spn_second_man.setEnabled(false);
                spn_second_man.setClickable(false);

                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString("status");
                String message = jsonObject.getString("message");
                String task_sync_status = jsonObject.getString("task_sync_status");
                String ss1 = jsonObject.getString("ss1");
                String ss2 = jsonObject.getString("ss2");
                String ss2_A = jsonObject.getString("ss2_A");
                String ss3 = jsonObject.getString("ss3");
                String final_sync = jsonObject.getString("final_sync");

                ArrayList<TaskListCartModel> ticket_list = new ArrayList<TaskListCartModel>();
                Main_impl main_impl = new Main_impl(this);
                ticket_list = main_impl.getUser();

                for (int i = 0; i < ticket_list.size(); i++) {
                    if (ticket_list.get(i).getTask_id().equalsIgnoreCase(strtaskid)) {
                        Main_impl main_impl2 = new Main_impl(this);
                        ticket_list = main_impl.getUser();

                        long det = main_impl2.update(new TaskListCartModel(ticket_list.get(i).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), ticket_list.get(i).getTask_id(), ticket_list.get(i).getTask_number(), ticket_list.get(i).getClient_name(), ticket_list.get(i).getAddress(),
                                ticket_list.get(i).getShort_description(), ticket_list.get(i).getJob_type(), "In-progress"));

                    }
                }

                impl = new Details_impl(TaskDetailActivity.this);
                list = impl.getUserDetails1(strtaskid);
                long det = impl.update(new TaskDetailCartModel(list.get(0).getId(),sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list.get(0).getTask_id()), list.get(0).getTask_number(), list.get(0).getClient_name(), list.get(0).getDate(), list.get(0).getTime(), list.get(0).getAddress(),
                        list.get(0).getLatitude(), list.get(0).getLongitude(), list.get(0).getShort_description(), list.get(0).getDescription(), list.get(0).getAcknowledged_by(), list.get(0).getAcknowledged_date(), list.get(0).getAcknowledged_time(), list.get(0).getImage_path(),list.get(0).getImage_bitmap(),
                        list.get(0).getTask_process(), list.get(0).getDisposal_site_name(), list.get(0).getDisposal_site_id(), list.get(0).getDisposal_date(), list.get(0).getDisposal_time(), list.get(0).getJob_type(), str_cat_id, "1", list.get(0).getSing_off_status(), strsignoffdate + " " + strsignofftime,
                        task_sync_status, ss1, ss2,ss2_A, ss3, final_sync,list.get(0).getNew_rental(),list.get(0).getFinal_removal(),list.get(0).getInstructions()
                ));

                ShowTaskDetailDB();


//                if (nameValuePair.size() > 0) {
//                    nameValuePair.clear();
//                }
//
//                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, WebService.KEY_TASKDETAIL_TASK));
//                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASKDETAIL_TASKIDDKEY, strtaskid));
//
//                if (connectionDetector.isConnectingToInternet()) {
//                    taskkey = "detail";
//                    asyncTaskClass = new AsyncTaskClass(context, TaskDetailActivity.this, nameValuePair, WebService.SERVERURL, true);
//                    asyncTaskClass.execute();
//                } else {
//                    Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
//                    return;
//                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
	
	
	/*public ArrayList<String>GetUpdateStatusArray(String status)
	{
		if(list_status.size()>0)
		{
			list_status.clear();
		}
		list_status.add("Please select status");
		list_status.add("Pending");
		list_status.add("In-progress");
		list_status.add("Completed"); //Collected
		//list_status.add("Done");
		StatusSpinnerAdapter statusSpinnerAdapter = new StatusSpinnerAdapter(context, list_status);
		spn_status.setAdapter(statusSpinnerAdapter);
		int position = list_status.indexOf(status);
		Toast.makeText(context, status, 1).show();
		spn_status.setSelection(position);
		
		return list_status;
		
	}*/

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/

    private void attemptSecondMan() {
        if (nameValuePair.size() > 0) {
            nameValuePair.clear();
        }

        nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, "ListDrivers"));

        if (connectionDetector.isConnectingToInternet()) {
            taskkey = "second";
            asyncTaskClass = new AsyncTaskClass(context, TaskDetailActivity.this, nameValuePair, WebService.SERVERURL, true);
            asyncTaskClass.execute();
        } else {
            Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
            return;
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LinearLayout.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public class SubmitRoadSideAssistDataAsyTask extends AsyncTask<Void, Void, Void> {
        private String str_responsefromserver;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            rotateAnimation = new RotateAnimation(context);
            rotateAnimation.show();
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
                Log.i(DEBUG_TAG, "image path:" + str_signature);
                bitmap_sign.compress(CompressFormat.JPEG, 90, bos);
                Log.i(DEBUG_TAG, "bytearraylenghth = " + bos.toByteArray().length);
                ContentBody contentPart = new ByteArrayBody(bos.toByteArray(), "image/jpeg", "files" + 1 + ".jpg");
                reqEntity.addPart("attachment", contentPart);
                Log.i(DEBUG_TAG, "image path:" + contentPart.toString());
//                SimpleDateFormat myFormatdate = new SimpleDateFormat("yyyy-MM-dd");//Y-m-d H:i:s  2016-09-19 15:09:00
//                SimpleDateFormat myFormattime = new SimpleDateFormat("HH:mm:ss");
//                try {
//                    Date date = new Date();
//                    strsignoffdate = myFormatdate.format(date);
//                    strsignofftime = myFormattime.format(date);
//
//                    Log.i(DEBUG_TAG, "Current Date Time in give format: " + strsignoffdate + " " + strsignofftime);
//                } catch (ParseException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }

                impl = new Details_impl(TaskDetailActivity.this);
                list = impl.getUserDetails1(strtaskid);


                reqEntity.addPart(WebService.KEY_TASK, new StringBody(WebService.KEY_SIGNOFFTASK_OFFLINE));
                reqEntity.addPart(WebService.KEY_SIGNOFFDATETIME, new StringBody(list.get(0).getAcknowledged_date() + " " + list.get(0).getAcknowledged_time()));
                reqEntity.addPart(WebService.KEY_SIGNOFFACOWLEDGEDBY, new StringBody(list.get(0).getAcknowledged_by()));
                reqEntity.addPart(WebService.KEY_SIGNOFFTASKID, new StringBody(strtaskid));
                reqEntity.addPart("second_man", new StringBody(list.get(0).getSecond_man()));


                //https://www.google.com/maps/dir///@23.0454818,72.5737186,15z/data=!3m1!4b1


                //Log.i(DEBUG_TAG, "parameter lenght="+reqEntity.getContentLength());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                reqEntity.writeTo(bytes);
                //String content = bytes.toString();
                //Log.i("Inquiry Activity", "parameter="+content);
                // File file= new File("/mnt/sdcard/forest.png");
                // FileBody bin = new FileBody(file);

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
                RequestQueue mRequestQueue2 = Volley.newRequestQueue(TaskDetailActivity.this);

                StringRequest mStringRequest2 = new StringRequest(Request.Method.POST, WebService.SERVERURL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                rotateAnimation.dismiss();
                                rotateAnimation = null;

                                try {
                                    //09-21 18:49:22.643: I/TaskDetailActivity(2161): Response: {"status":"1","message":"Saved successfully"}
                                    JSONObject jsonObject = new JSONObject(str_responsefromserver);
                                    //String status = jsonObject.optString(WebService.KEY_LOGIN_STATUS);
                                    String message = jsonObject.optString(WebService.KEY_LOGIN_MESSAGE);
                                    if (message.toString().length() > 0) {
                                        Toast.makeText(context, message, 1).show();
                                    }
                                    impl = new Details_impl(TaskDetailActivity.this);
                                    list = impl.getUserDetails1(strtaskid);


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

                                                        impl = new Details_impl(context);
                                                        list = impl.getUserDetails1(strtaskid);
                                                        long det = impl.update(new TaskDetailCartModel(list.get(0).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list.get(0).getTask_id()), list.get(0).getTask_number(), list.get(0).getClient_name(), list.get(0).getDate(), list.get(0).getTime(), list.get(0).getAddress(),
                                                                list.get(0).getLatitude(), list.get(0).getLongitude(), list.get(0).getShort_description(), list.get(0).getDescription(), list.get(0).getAcknowledged_by(), list.get(0).getAcknowledged_date(), list.get(0).getAcknowledged_time(), list.get(0).getImage_path(), list.get(0).getImage_bitmap(),
                                                                list.get(0).getTask_process(), list.get(0).getDisposal_site_name(), list.get(0).getDisposal_site_id(), list.get(0).getDisposal_date(), list.get(0).getDisposal_time(), list.get(0).getJob_type(), list.get(0).getSecond_man(), list.get(0).getJob_started(), list.get(0).getSing_off_status(), list.get(0).getJob_start_datetime(),
                                                                task_sync_status, ss1, ss2, ss2_A, ss3, final_sync, list.get(0).getNew_rental(), list.get(0).getFinal_removal(),list.get(0).getInstructions()
                                                        ));
//                                    CallApiSequence();


                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                                                }
                                            }) {
                                        @Override
                                        protected Map<String, String> getParams() {
                                            Map<String, String> params = new HashMap<String, String>();
                                            params.put(WebService.KEY_TASK, WebService.KEY_UPDATETASK_OFFLINE);
                                            params.put(WebService.KEY_UPDATETASKID, strtaskid);
                                            params.put("second_man", list.get(0).getSecond_man());
                                            params.put(WebService.KEY_UPDATETASKPROCESS, "In-progress");
                                            params.put(WebService.KEY_TIPPINGBOOKDATETIME, list.get(0).getAcknowledged_date() + " " + list.get(0).getAcknowledged_time());
                                            params.put(WebService.KEY_SIGNOFFDATETIME, list.get(0).getAcknowledged_date() + " " + list.get(0).getAcknowledged_time());
                                            params.put("task_sync_status", list.get(0).getTask_sync_status());
                                            params.put("ss1", list.get(0).getSs1());
                                            params.put("ss2", list.get(0).getSs2());
                                            params.put("ss2_A", list.get(0).getSs2_A());
                                            params.put("ss3", list.get(0).getSs3());
                                            params.put("final_sync", list.get(0).getFinal_sync());


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
                                Toast.makeText(TaskDetailActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        impl = new Details_impl(TaskDetailActivity.this);
                        list = impl.getUserDetails1(strtaskid);

                        Map<String, String> params = new HashMap<String, String>();
                        params.put(WebService.KEY_TASK, WebService.KEY_SIGNOFFTASK_OFFLINE);
                        params.put(WebService.KEY_SIGNOFFDATETIME, list.get(0).getAcknowledged_date() + " " + list.get(0).getAcknowledged_time());
                        params.put(WebService.KEY_SIGNOFFACOWLEDGEDBY, list.get(0).getAcknowledged_by());
                        params.put(WebService.KEY_SIGNOFFTASKID, strtaskid);
                        params.put("second_man", list.get(0).getSecond_man());
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
            rotateAnimation.dismiss();
            rotateAnimation = null;

            try {
                //09-21 18:49:22.643: I/TaskDetailActivity(2161): Response: {"status":"1","message":"Saved successfully"}
                JSONObject jsonObject = new JSONObject(str_responsefromserver);
                //String status = jsonObject.optString(WebService.KEY_LOGIN_STATUS);
                String message = jsonObject.optString(WebService.KEY_LOGIN_MESSAGE);
                if (message.toString().length() > 0) {
                    Toast.makeText(context, message, 1).show();
                }
                impl = new Details_impl(TaskDetailActivity.this);
                list = impl.getUserDetails1(strtaskid);

                //Picasso.with(context).load(str_signature).fit().into(image_signoffshow);
//                image_signoffshow.setImageBitmap(bitmap_sign);
//                txt_signature.setText(list.get(0).getAcknowledged_by());
//                txt_signofftime.setText(list.get(0).getAcknowledged_date()+ " " + list.get(0).getAcknowledged_time());
//                if (dialogsignoff != null) {
//                    if (dialogsignoff.isShowing()) {
//                        dialogsignoff.dismiss();
//                    }
//                }

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

                                    impl = new Details_impl(context);
                                    list = impl.getUserDetails1(strtaskid);
                                    long det = impl.update(new TaskDetailCartModel(list.get(0).getId(), sharedPrefrenceClass.getKEY_LOGIN_USERID(), String.valueOf(list.get(0).getTask_id()), list.get(0).getTask_number(), list.get(0).getClient_name(), list.get(0).getDate(), list.get(0).getTime(), list.get(0).getAddress(),
                                            list.get(0).getLatitude(), list.get(0).getLongitude(), list.get(0).getShort_description(), list.get(0).getDescription(), list.get(0).getAcknowledged_by(), list.get(0).getAcknowledged_date(), list.get(0).getAcknowledged_time(), list.get(0).getImage_path(), list.get(0).getImage_bitmap(),
                                            list.get(0).getTask_process(), list.get(0).getDisposal_site_name(), list.get(0).getDisposal_site_id(), list.get(0).getDisposal_date(), list.get(0).getDisposal_time(), list.get(0).getJob_type(), list.get(0).getSecond_man(), list.get(0).getJob_started(), list.get(0).getSing_off_status(), list.get(0).getJob_start_datetime(),
                                            task_sync_status, ss1, ss2, ss2_A, ss3, final_sync, list.get(0).getNew_rental(), list.get(0).getFinal_removal(),list.get(0).getInstructions()
                                    ));
//                                    CallApiSequence();


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(WebService.KEY_TASK, WebService.KEY_UPDATETASK_OFFLINE);
                        params.put(WebService.KEY_UPDATETASKID, strtaskid);
                        params.put("second_man", list.get(0).getSecond_man());
                        params.put(WebService.KEY_UPDATETASKPROCESS, "In-progress");
                        params.put(WebService.KEY_TIPPINGBOOKDATETIME, list.get(0).getAcknowledged_date() + " " + list.get(0).getAcknowledged_time());
                        params.put(WebService.KEY_SIGNOFFDATETIME, list.get(0).getAcknowledged_date() + " " + list.get(0).getAcknowledged_time());
                        params.put("task_sync_status", list.get(0).getTask_sync_status());
                        params.put("ss1", list.get(0).getSs1());
                        params.put("ss2", list.get(0).getSs2());
                        params.put("ss2_A", list.get(0).getSs2_A());
                        params.put("ss3", list.get(0).getSs3());
                        params.put("final_sync", list.get(0).getFinal_sync());


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
//                nameValuePair.add(new BasicNameValuePair(WebService.KEY_UPDATETASKID, list.get(0).getTask_id()));
//                nameValuePair.add(new BasicNameValuePair("second_man", list.get(0).getSecond_man()));
//                nameValuePair.add(new BasicNameValuePair(WebService.KEY_UPDATETASKPROCESS, list.get(0).getTask_process()));
//                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TIPPINGBOOKDATETIME, list.get(0).getAcknowledged_date()+ " " + list.get(0).getAcknowledged_time()));
//                nameValuePair.add(new BasicNameValuePair(WebService.KEY_SIGNOFFDATETIME, list.get(0).getAcknowledged_date() + " " + list.get(0).getAcknowledged_time()));
//                nameValuePair.add(new BasicNameValuePair("task_sync_status", list.get(0).getTask_sync_status()));
//                nameValuePair.add(new BasicNameValuePair("ss1", list.get(0).getSs1()));
//                nameValuePair.add(new BasicNameValuePair("ss2", list.get(0).getSs2()));
//                nameValuePair.add(new BasicNameValuePair("ss3", list.get(0).getSs3()));
//                nameValuePair.add(new BasicNameValuePair("final_sync", list.get(0).getFinal_sync()));
//                if (connectionDetector.isConnectingToInternet()) {
//                    taskkey = "statusupdate";
//                    asyncTaskClass = new AsyncTaskClass(context, TaskDetailActivity.this, nameValuePair, WebService.SERVERURL, true);
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


        //Log.i(DEBUG_TAG, "get data="+str_responsefromserver);


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

    public class DisposalAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<DisposalModel> rowlist;

        String pathName;

        public DisposalAdapter(Context context, ArrayList<DisposalModel> rowlist) {
            this.context = context;
            this.rowlist = rowlist;
        }

        private class ViewHolder {
            TextView txt_spn_row;
            TextView tx_total_price;
            TextView tx_payment;
            TextView tx_ordr_on, tx_status, tx_pro_name;
            ImageView img;

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
            return rowlist.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.adapter_spinnerrow, null);
                holder = new ViewHolder();
                holder.txt_spn_row = convertView.findViewById(R.id.txt_spn_row);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final DisposalModel list = rowlist.get(position);
            holder.txt_spn_row.setText(list.getName());

            return convertView;
        }

    }

    public class SecondManAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<SecondMan2Model> rowlist;

        String pathName;

        public SecondManAdapter(Context context, ArrayList<SecondMan2Model> rowlist) {
            this.context = context;
            this.rowlist = rowlist;
        }

        private class ViewHolder {
            TextView txt_spn_row;
            TextView tx_total_price;
            TextView tx_payment;
            TextView tx_ordr_on, tx_status, tx_pro_name;
            ImageView img;

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
            return rowlist.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.adapter_spinnerrow, null);
                holder = new ViewHolder();
                holder.txt_spn_row = convertView.findViewById(R.id.txt_spn_row);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final SecondMan2Model list = rowlist.get(position);
            holder.txt_spn_row.setText(list.getName());

            return convertView;
        }

    }

    private void ShowTaskDetailDB() {
        impl = new Details_impl(TaskDetailActivity.this);
        list = impl.getUserDetails1(strtaskid);
//        Toast.makeText(context, list.get(0).getDate(), Toast.LENGTH_SHORT).show();

        if (!list.get(0).getInstructions().equalsIgnoreCase("")){
            lin_instruction.setVisibility(View.VISIBLE);
        }else{
            lin_instruction.setVisibility(View.GONE);
        }

        txt_instruction.setText(list.get(0).getInstructions());
        txt_tasknumber.setText(list.get(0).getTask_number());
        txt_jobtype.setText(list.get(0).getJob_type());
        txt_clientname.setText(list.get(0).getClient_name());
        txt_date.setText(list.get(0).getDate());
        txt_time.setText(list.get(0).getTime());
        txt_description.setText(list.get(0).getShort_description());
        txt_description2.setText(" - "+list.get(0).getDescription());
        txt_signature.setText(list.get(0).getAcknowledged_by());
        txt_signofftime.setText(list.get(0).getAcknowledged_date() + " " +
                list.get(0).getAcknowledged_time());

        if (list.get(0).getImage_path().contains("http")) {
            Picasso.with(context).load(list.get(0).getImage_path()).into(image_signoffshow);
        }else if (list.get(0).getImage_path().equalsIgnoreCase("")) {
//            Picasso.with(context).load(list.get(0).getImage_path()).into(image_signoffshow);
        } else {
//            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//            Bitmap bitmap = BitmapFactory.decodeFile(list.get(0).getImage_bitmap(), bmOptions);
//            bitmap = Bitmap.createScaledBitmap(bitmap, 381, 250, true);

            byte[] decodedBytes = Base64.decode(list.get(0).getImage_bitmap(), 0);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            image_signoffshow.setImageBitmap(bitmap);
        }
        txt_disposalsite.setText(list.get(0).getDisposal_site_name());
        txt_disposalsiteid.setText(list.get(0).getDisposal_site_id());

        txt_disposaldate.setText(list.get(0).getDisposal_date());
        txt_disposaltime.setText(list.get(0).getDisposal_time());
        //GetUpdateStatusArray(taskDetailParser.GetTaskDetailList().get(0).get(WebService.KEY_TASKDETAIL_TASKPROCESS));
        btn_status.setText(list.get(0).getTask_process());
        if (list.get(0).getTask_process().equalsIgnoreCase("In-progress")) {
            spn_second_man.setEnabled(false);
            spn_second_man.setClickable(false);
        } else if (list.get(0).getTask_process().equalsIgnoreCase("completed")) {
            spn_second_man.setEnabled(false);
            spn_second_man.setClickable(false);

            spn_disposal.setEnabled(false);
        }

        if (list.get(0).getJob_started().equalsIgnoreCase("0")) {
            jobstart = 1;
            btn_start.setBackgroundResource(R.color.app_loginbtn);
//            btn_acknowledge.setBackgroundResource(R.color.app_loginbtn_disable);
            btn_acknowledge.setBackgroundResource(R.color.app_loginbtn);
        } else {
            jobstart = 1;
            btn_start.setBackgroundResource(R.color.app_loginbtn_disable);
            btn_start.setText("STARTED");
            btn_acknowledge.setBackgroundResource(R.color.app_loginbtn);
        }

        arrSecond2 = secondMan_impl.getUser();
        if (arrSecond2.size() < 1) {
            attemptSecondMan();
        } else {
            secondManAdapter = new SecondManAdapter(context, arrSecond2);
            spn_second_man.setAdapter(secondManAdapter);

            for (int i = 0; i < arrSecond2.size(); i++) {
                if (impl.isExist(strtaskid)) {
                    list = impl.getUserDetails1(strtaskid);
                    if (list.get(0).getSecond_man().equalsIgnoreCase(arrSecond2.get(i).getManId())) {
                        spn_second_man.setSelection(i);
                        exist = 1;
                        str_cat_id = arrSecond2.get(i).getManId();
                    } else {
//                        spn_second_man.setSelection(0);
//                        str_cat_id = arrSecond2.get(0).getManId();
                    }
                }
            }
        }
    }

    private void CallApiSequence() {
        impl = new Details_impl(TaskDetailActivity.this);
        list = impl.getUserDetails1(strtaskid);

        if (list.size() > 0) {
            if (list.get(0).getSs1().equalsIgnoreCase("1")) {
                if (nameValuePair.size() > 0) {
                    nameValuePair.clear();
                }

                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, "job_started_offline"));
                nameValuePair.add(new BasicNameValuePair(WebService.KEY_TIPPINGBOOTASKID, list.get(0).getTask_id()));
                nameValuePair.add(new BasicNameValuePair("second_man", list.get(0).getSecond_man()));
                nameValuePair.add(new BasicNameValuePair("job_start_datetime", list.get(0).getJob_start_datetime()));
                nameValuePair.add(new BasicNameValuePair("task_sync_status", list.get(0).getTask_sync_status()));
                nameValuePair.add(new BasicNameValuePair("ss1", list.get(0).getSs1()));
                nameValuePair.add(new BasicNameValuePair("ss2", list.get(0).getSs2()));
                nameValuePair.add(new BasicNameValuePair("ss2_A", list.get(0).getSs2_A()));
                nameValuePair.add(new BasicNameValuePair("ss3", list.get(0).getSs3()));
                nameValuePair.add(new BasicNameValuePair("final_sync", list.get(0).getFinal_sync()));

                if (connectionDetector.isConnectingToInternet()) {
                    taskkey = "job_started";
                    asyncTaskClass = new AsyncTaskClass(context, TaskDetailActivity.this, nameValuePair, WebService.SERVERURL, true);
                    asyncTaskClass.execute();
                } else {
//                    Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
                    return;
                }
            } else if (list.get(0).getSs2().equalsIgnoreCase("1")) {
                if (list.get(0).getTask_sync_status().equalsIgnoreCase("3")) {
                    if (nameValuePair.size() > 0) {
                        nameValuePair.clear();
                    }
                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, WebService.KEY_SIGN_OFFTASK_OFFLINE));
                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_UPDATETASKID, list.get(0).getTask_id()));
                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_SIGN_OFF_STATUS, list.get(0).getSing_off_status()));
                    nameValuePair.add(new BasicNameValuePair("second_man", list.get(0).getSecond_man()));

                    if (connectionDetector.isConnectingToInternet()) {
                        taskkey = "signoffstatus";
                        asyncTaskClass = new AsyncTaskClass(context, TaskDetailActivity.this, nameValuePair, WebService.SERVERURL, true);
                        asyncTaskClass.execute();
                    } else {
//                        Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
                        return;
                    }
                } else {
                    new SubmitRoadSideAssistDataAsyTask().execute();
                }
            }
        }


    }


}
