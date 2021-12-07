package com.skipservices;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.skipservices.App.Config;
import com.skipservices.Impl.User_impl;
import com.skipservices.Model.UserModel;
import com.skipservices.Service.NotificationUtils;
import com.skipservices.parsers.LoginParser;
import com.skipservices.parsers.SharedPrefrenceClass;
import com.skipservices.thread.AsyncTaskClass;
import com.skipservices.thread.AsyncTaskCompleteListener;
import com.skipservices.utils.ConnectionDetector;
import com.skipservices.utils.EncryptData;
import com.skipservices.utils.MyPersonalApp;
import com.skipservices.utils.RotateAnimation;
import com.skipservices.utils.WebService;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

@SuppressWarnings("deprecation")
public class LoginActivity extends AppCompatActivity implements AsyncTaskCompleteListener<String> {

    public String DEBUG_TAG = "LoginActivity";
    Context context;
    Button btn_login;
    EditText editText_username;
    EditText editText_password;
    EncryptData encryptData;
    List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
    ConnectionDetector connectionDetector;
    AsyncTaskClass asyncTaskClass;
    String key = "";
    ProgressDialog progressDoalog;
    SharedPrefrenceClass sharedPrefrenceClass;
    private ArrayList<UserModel> list = new ArrayList<UserModel>();
    String token;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    String regid = "";
    private RequestQueue mRequestQueue;
    RotateAnimation rotateAnimation;
    TextView tx_version, tx_os;
    String version;
    int versionCode;
    FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_login);
        context = LoginActivity.this;
        editText_username = (EditText) findViewById(R.id.edt_username);
        editText_password = (EditText) findViewById(R.id.edt_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        tx_os = findViewById(R.id.tx_os);
        tx_version = findViewById(R.id.tx_version);
        encryptData = new EncryptData();
        connectionDetector = new ConnectionDetector(context);
        sharedPrefrenceClass = new SharedPrefrenceClass(context);

        rotateAnimation = new RotateAnimation(context);
        final MyPersonalApp globalVariable = (MyPersonalApp) getApplicationContext();
        token = globalVariable.GetToken();
        versionCode = BuildConfig.VERSION_CODE;
        Log.d("vsions", String.valueOf(versionCode) + "");

        tx_version.setText("V - " + String.valueOf(BuildConfig.VERSION_CODE));
        tx_os.setText("OS - " + String.valueOf(android.os.Build.VERSION.RELEASE));


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");
                    ShowDilagBonusMessage(LoginActivity.this, message);
//                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                }
            }
        };

        displayFirebaseRegId();
        FirebaseApp.initializeApp(this);
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();

        db = FirebaseFirestore.getInstance();
        db.setFirestoreSettings(settings);

        db.collection("ProjectVersion")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documents : task.getResult()) {

                        Log.d("hello", documents.getId() + " => " + documents.getData());
                        version = documents.getData().get("Version").toString();
                    }

                    if (versionCode < Integer.parseInt(version)) {
                        startActivity(new Intent(LoginActivity.this, EmergencyUpdate.class));
                        finish();
                    } else {
                        if (sharedPrefrenceClass.getKEY_LOGIN_USERID().length() > 1) {
                            Intent intent = new Intent(LoginActivity.this, TaskListActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                } else {
                    Toast.makeText(context, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });




//        rotateAnimation.show();

//        new CountDownTimer(2000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                //this will be done every 1000 milliseconds ( 1 seconds )
//                int progress = (int) ((60000 - millisUntilFinished) / 30000);
//            }
//
//            @Override
//            public void onFinish() {
//                //the progressBar will be invisible after 60 000 miliseconds ( 1 minute)
////                takeScreenshot();
//                if(rotateAnimation!=null)
//                {
//                    if(rotateAnimation.isShowing())
//                    {
//                        rotateAnimation.dismiss();
//                        rotateAnimation = null;
//                    }
//                }
//
//            }
//
//        }.start();

//		key="pull";
//		progressDoalog = new ProgressDialog(LoginActivity.this);
//		progressDoalog.setMessage("Updating Joblist. Please wait...!");
//		progressDoalog.setCancelable(false);
//		// show it
//		progressDoalog.show();
//		if(nameValuePair.size()>0)
//		{
//			nameValuePair.clear();
//		}
//
//		nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, WebService.KEY_PULL_TASK));
//
//		if(connectionDetector.isConnectingToInternet())
//		{
//			asyncTaskClass = new AsyncTaskClass(context, LoginActivity.this, nameValuePair, WebService.SERVERURL,false);
//			asyncTaskClass.execute();
//		}
//		else
//		{
//			Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
//			return;
//		}


        btn_login.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    if (editText_username.getText().toString().trim().length() > 0) {
                        if (editText_password.getText().toString().trim().length() > 0) {
                            User_impl register_impl = new User_impl(LoginActivity.this);
                            list = register_impl.getUser();
                            if (list.size() > 0) {
//								if (register_impl.isExist(editText_username.getText().toString().trim())) {
//									list = register_impl.getUserDetails1(editText_username.getText().toString().trim());
//									sharedPrefrenceClass.setKEY_LOGIN_NAME(list.get(0).getName());
//									sharedPrefrenceClass.setKEY_LOGIN_USERNAME(list.get(0).getUsername());
//									sharedPrefrenceClass.setKEY_LOGIN_USERID(list.get(0).getUserId());
//									sharedPrefrenceClass.setKEY_LOGIN_EMAIL(list.get(0).getEmail());
//									Intent intent = new Intent(LoginActivity.this, TaskListActivity.class);
//									startActivity(intent);
//									finish();
//									return;
//								} else {
//									mRequestQueue = Volley.newRequestQueue(LoginActivity.this);
//									Map<String, String> params = new HashMap<String, String>();
//									params.put(WebService.KEY_TASK, WebService.KEY_LOGIN_TASK);
//									params.put(WebService.KEY_LOGIN_USERNAMEKEY, editText_username.getText().toString().trim());
//									params.put(WebService.KEY_LOGIN_PASSWORDKEY, editText_password.getText().toString().trim());
//
//									StringRequest mStringRequest = new StringRequest(Request.Method.POST, WebService.SERVERURL,
//											new Response.Listener<String>() {
//												@Override
//												public void onResponse(String response) {
//													rotateAnimation.dismiss();
//													Log.d("mainactivity_resonse", response);
//													try {
//														LoginParser loginParser = new LoginParser(context);
//														loginParser.JsonStringToArrayList(response);
//
//														String status = loginParser.Getstatus();
//
//														if (status.equalsIgnoreCase("y")) {
//															User_impl impl = new User_impl(LoginActivity.this);
//															long det = impl.insert(new UserModel(0, sharedPrefrenceClass.getKEY_LOGIN_USERID(), editText_username.getText().toString(), sharedPrefrenceClass.getKEY_LOGIN_NAME(), sharedPrefrenceClass.getKEY_LOGIN_EMAIL(), editText_password.getText().toString().trim()));
//															Toast.makeText(context, loginParser.Getmessage(), 1).show();
//															Intent intent = new Intent(LoginActivity.this, TaskListActivity.class);
//															startActivity(intent);
//															finish();
//														} else {
//															Toast.makeText(context, loginParser.Getmessage(), 1).show();
//														}
//
//													} catch (Exception e) {
//														e.printStackTrace();
//													}
//												}
//											},
//											new Response.ErrorListener() {
//												@Override
//												public void onErrorResponse(VolleyError error) {
//													rotateAnimation.dismiss();
//													Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
//												}
//											}) {
//										@Override
//										protected Map<String, String> getParams() {
//											Map<String, String> params = new HashMap<String, String>();
//											params.put(WebService.KEY_TASK, WebService.KEY_LOGIN_TASK);
//											params.put(WebService.KEY_LOGIN_USERNAMEKEY, editText_username.getText().toString().trim());
//											params.put(WebService.KEY_LOGIN_PASSWORDKEY, editText_password.getText().toString().trim());
//											if (regid != null && !regid.isEmpty() && !regid.equals("null")) {
//												params.put("devicetoken", regid);
//											} else {
//												params.put("devicetoken", "");
//											}
//
//
//											return params;
//										}
//									};
//
//									mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
//											50000,
//											DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//											DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//									mRequestQueue.add(mStringRequest);
                                if(connectionDetector.isConnectingToInternet()) {
                                    mRequestQueue = Volley.newRequestQueue(LoginActivity.this);

                                    StringRequest mStringRequest = new StringRequest(Request.Method.POST, WebService.SERVERURL,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
//												rotateAnimation.dismiss();
                                                    sharedPrefrenceClass.setKEY_LOGIN_PASSWORD(editText_password.getText().toString());
                                                    Log.d("mainactivity_resonse", response);
                                                    try {
                                                        LoginParser loginParser = new LoginParser(context);
                                                        loginParser.JsonStringToArrayList(response);

                                                        String status = loginParser.Getstatus();

                                                        if (status.equalsIgnoreCase("y")) {
                                                            User_impl impl = new User_impl(LoginActivity.this);
                                                            long det = impl.insert(new UserModel(0, sharedPrefrenceClass.getKEY_LOGIN_USERID(), editText_username.getText().toString(), sharedPrefrenceClass.getKEY_LOGIN_NAME(), sharedPrefrenceClass.getKEY_LOGIN_EMAIL(), editText_password.getText().toString().trim()));
                                                            Toast.makeText(context, loginParser.Getmessage(), 1).show();
                                                            Intent intent = new Intent(LoginActivity.this, TaskListActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        } else {
                                                            Toast.makeText(context, loginParser.Getmessage(), 1).show();
                                                        }

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
//												rotateAnimation.dismiss();
                                                    Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                                                }
                                            }) {
                                        @Override
                                        protected Map<String, String> getParams() {
                                            Map<String, String> params = new HashMap<String, String>();
                                            params.put(WebService.KEY_TASK, WebService.KEY_LOGIN_TASK_OFFLINE);
                                            params.put(WebService.KEY_LOGIN_USERNAMEKEY, editText_username.getText().toString().trim());
                                            params.put(WebService.KEY_LOGIN_PASSWORDKEY, editText_password.getText().toString().trim());
                                            if (regid != null && !regid.isEmpty() && !regid.equals("null")) {
                                                params.put("devicetoken", regid);
                                            } else {
                                                params.put("devicetoken", "");
                                            }


                                            return params;
                                        }
                                    };

                                    mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                            50000,
                                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                                    mRequestQueue.add(mStringRequest);
                                }else{
                                    if (register_impl.isExist(editText_username.getText().toString().trim())) {
                                        list = register_impl.getUserDetails1(editText_username.getText().toString().trim());
                                        sharedPrefrenceClass.setKEY_LOGIN_NAME(list.get(0).getName());
                                        sharedPrefrenceClass.setKEY_LOGIN_USERNAME(list.get(0).getUsername());
                                        sharedPrefrenceClass.setKEY_LOGIN_USERID(list.get(0).getUserId());
                                        sharedPrefrenceClass.setKEY_LOGIN_EMAIL(list.get(0).getEmail());
                                        sharedPrefrenceClass.setKEY_LOGIN_PASSWORD(list.get(0).getPassword());
                                        Intent intent = new Intent(LoginActivity.this, TaskListActivity.class);
                                        startActivity(intent);
                                        finish();
                                        return;
                                    } else {
//                                    key = "pull2";
//                                    String encrusrname = encryptData.doEncrypt(WebService.ENCRYPTIONKEY,
//                                            editText_username.getText().toString().trim());
//                                    Log.i(DEBUG_TAG, "encrypted usernamme=" + encrusrname);
//                                    String encrpassword = encryptData.doEncrypt(WebService.ENCRYPTIONKEY,
//                                            editText_password.getText().toString().trim());
//                                    Log.i(DEBUG_TAG, "EncryptedPassword=" + encrpassword);
//
//                                    if (nameValuePair.size() > 0) {
//                                        nameValuePair.clear();
//                                    }
//
//                                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, WebService.KEY_LOGIN_TASK_OFFLINE));
//                                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_LOGIN_USERNAMEKEY, editText_username.getText().toString().trim()));
//                                    nameValuePair.add(new BasicNameValuePair(WebService.KEY_LOGIN_PASSWORDKEY, editText_password.getText().toString().trim()));
//                                    if (regid != null && !regid.isEmpty() && !regid.equals("null")) {
//                                        nameValuePair.add(new BasicNameValuePair("devicetoken", regid));
//                                    } else {
//                                        nameValuePair.add(new BasicNameValuePair("devicetoken", ""));
//                                    }
//
//                                    if (connectionDetector.isConnectingToInternet()) {
//                                        asyncTaskClass = new AsyncTaskClass(context, LoginActivity.this, nameValuePair, WebService.SERVERURL, false);
//                                        asyncTaskClass.execute();
//                                    } else {
//                                        Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
//                                        return;
//                                    }

                                        mRequestQueue = Volley.newRequestQueue(LoginActivity.this);

                                        StringRequest mStringRequest = new StringRequest(Request.Method.POST, WebService.SERVERURL,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
//												rotateAnimation.dismiss();
                                                        sharedPrefrenceClass.setKEY_LOGIN_PASSWORD(editText_password.getText().toString());
                                                        Log.d("mainactivity_resonse", response);
                                                        try {
                                                            LoginParser loginParser = new LoginParser(context);
                                                            loginParser.JsonStringToArrayList(response);

                                                            String status = loginParser.Getstatus();

                                                            if (status.equalsIgnoreCase("y")) {
                                                                User_impl impl = new User_impl(LoginActivity.this);
                                                                long det = impl.insert(new UserModel(0, sharedPrefrenceClass.getKEY_LOGIN_USERID(), editText_username.getText().toString(), sharedPrefrenceClass.getKEY_LOGIN_NAME(), sharedPrefrenceClass.getKEY_LOGIN_EMAIL(), editText_password.getText().toString().trim()));
                                                                Toast.makeText(context, loginParser.Getmessage(), 1).show();
                                                                Intent intent = new Intent(LoginActivity.this, TaskListActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            } else {
                                                                Toast.makeText(context, loginParser.Getmessage(), 1).show();
                                                            }

                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
//												rotateAnimation.dismiss();
                                                        Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                                                    }
                                                }) {
                                            @Override
                                            protected Map<String, String> getParams() {
                                                Map<String, String> params = new HashMap<String, String>();
                                                params.put(WebService.KEY_TASK, WebService.KEY_LOGIN_TASK_OFFLINE);
                                                params.put(WebService.KEY_LOGIN_USERNAMEKEY, editText_username.getText().toString().trim());
                                                params.put(WebService.KEY_LOGIN_PASSWORDKEY, editText_password.getText().toString().trim());
                                                if (regid != null && !regid.isEmpty() && !regid.equals("null")) {
                                                    params.put("devicetoken", regid);
                                                } else {
                                                    params.put("devicetoken", "");
                                                }


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

//								}
//								}
                            } else {
                                mRequestQueue = Volley.newRequestQueue(LoginActivity.this);

                                StringRequest mStringRequest = new StringRequest(Request.Method.POST, WebService.SERVERURL,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
//												rotateAnimation.dismiss();
                                                sharedPrefrenceClass.setKEY_LOGIN_PASSWORD(editText_password.getText().toString());
                                                Log.d("mainactivity_resonse", response);
                                                try {
                                                    LoginParser loginParser = new LoginParser(context);
                                                    loginParser.JsonStringToArrayList(response);

                                                    String status = loginParser.Getstatus();

                                                    if (status.equalsIgnoreCase("y")) {
                                                        User_impl impl = new User_impl(LoginActivity.this);
                                                        long det = impl.insert(new UserModel(0, sharedPrefrenceClass.getKEY_LOGIN_USERID(), editText_username.getText().toString(), sharedPrefrenceClass.getKEY_LOGIN_NAME(), sharedPrefrenceClass.getKEY_LOGIN_EMAIL(), editText_password.getText().toString().trim()));
                                                        Toast.makeText(context, loginParser.Getmessage(), 1).show();
                                                        Intent intent = new Intent(LoginActivity.this, TaskListActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        Toast.makeText(context, loginParser.Getmessage(), 1).show();
                                                    }

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
//												rotateAnimation.dismiss();
                                                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                                            }
                                        }) {
                                    @Override
                                    protected Map<String, String> getParams() {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put(WebService.KEY_TASK, WebService.KEY_LOGIN_TASK_OFFLINE);
                                        params.put(WebService.KEY_LOGIN_USERNAMEKEY, editText_username.getText().toString().trim());
                                        params.put(WebService.KEY_LOGIN_PASSWORDKEY, editText_password.getText().toString().trim());
                                        if (regid != null && !regid.isEmpty() && !regid.equals("null")) {
                                            params.put("devicetoken", regid);
                                        } else {
                                            params.put("devicetoken", "");
                                        }


                                        return params;
                                    }
                                };

                                mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                        50000,
                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                                mRequestQueue.add(mStringRequest);
//								key = "pull2";
//								String encrusrname = encryptData.doEncrypt(WebService.ENCRYPTIONKEY,
//										editText_username.getText().toString().trim());
//								Log.i(DEBUG_TAG, "encrypted usernamme=" + encrusrname);
//								String encrpassword = encryptData.doEncrypt(WebService.ENCRYPTIONKEY,
//										editText_password.getText().toString().trim());
//								Log.i(DEBUG_TAG, "EncryptedPassword=" + encrpassword);
//
//								if (nameValuePair.size() > 0) {
//									nameValuePair.clear();
//								}
//
//								nameValuePair.add(new BasicNameValuePair(WebService.KEY_TASK, WebService.KEY_LOGIN_TASK_OFFLINE));
//								nameValuePair.add(new BasicNameValuePair(WebService.KEY_LOGIN_USERNAMEKEY, editText_username.getText().toString().trim()));
//								nameValuePair.add(new BasicNameValuePair(WebService.KEY_LOGIN_PASSWORDKEY, editText_password.getText().toString().trim()));
//
//								if (regid != null && !regid.isEmpty() && !regid.equals("null")) {
//									nameValuePair.add(new BasicNameValuePair("devicetoken", regid));
//								}else{
//									nameValuePair.add(new BasicNameValuePair("devicetoken", ""));
//								}
//
//								if (connectionDetector.isConnectingToInternet()) {
//									asyncTaskClass = new AsyncTaskClass(context, LoginActivity.this, nameValuePair, WebService.SERVERURL, false);
//									asyncTaskClass.execute();
//								} else {
//									Toast.makeText(context, "No internet Connection", Toast.LENGTH_LONG).show();
//									return;
//								}
                            }
                        } else {
                            editText_password.setError("please enter password");
                        }
                    } else {
                        editText_username.setError("please enter username");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onTaskComplete(String result) {
        // TODO Auto-generated method stub
        Log.i(DEBUG_TAG, "response from server=" + result);
//        if(rotateAnimation!=null)
//        {
//            if(rotateAnimation.isShowing())
//            {
//                rotateAnimation.dismiss();
//                rotateAnimation = null;
//            }
//        }
        if (key.equalsIgnoreCase("pull")) {
            if (!result.equalsIgnoreCase("")) {
                progressDoalog.dismiss();
            }
        } else {

            try {
                LoginParser loginParser = new LoginParser(context);
                loginParser.JsonStringToArrayList(result);

                String status = loginParser.Getstatus();

                if (status.equalsIgnoreCase("y")) {
                    User_impl impl = new User_impl(LoginActivity.this);
                    long det = impl.insert(new UserModel(0, sharedPrefrenceClass.getKEY_LOGIN_USERID(), editText_username.getText().toString(), sharedPrefrenceClass.getKEY_LOGIN_NAME(), sharedPrefrenceClass.getKEY_LOGIN_EMAIL(), editText_password.getText().toString().trim()));
                    Toast.makeText(context, loginParser.Getmessage(), 1).show();
                    Intent intent = new Intent(LoginActivity.this, TaskListActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(context, loginParser.Getmessage(), 1).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        regid = pref.getString("regId", null);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    public void ShowDilagBonusMessage(final Context context, String message) {
        final Dialog dialog = new Dialog(context);
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
                        dialog.dismiss();
                    }
                }
            }
        });

        dialog.show();

    }
}
