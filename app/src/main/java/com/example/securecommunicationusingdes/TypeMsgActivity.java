package com.example.securecommunicationusingdes;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;


public class TypeMsgActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener {
    public static TypeMsgActivity typeMsgActivity;
    public static boolean flag;
    public static int msgLen = 0;
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    //final private String FCM_API = "https://fcm.googleapis.com/v1/{parent=demofcm-311ab/*}";
    final private String serverKey = "key=" + "AAAAC5pLdw0:APA91bGJJW_hcx400dADQ-xHmAmYZHre4I60yYP_LTObSywis4U_ca5k7BwKSrv6yCiB67vbLkI7SpMLW2nlF25R_Ig0VLESsri4Vz5SrwfCAkcVwjR0BVotzeuhl_LVno1no33tGCSm";
    final private String contentType = "application/json";
    public String isEncypted = "", tokon;
    RelativeLayout relativeLayout;
    View actionBarView;
    RecyclerView recyclerView;
    TextView tv_title, home;
    ArrayList<ResponseMessage> responseMessages = new ArrayList<>();
    String privateKey, receiver, sender, date_time;
    UserSessionManager userSessionManager;
    LinearLayoutManager mLinearLayoutManager;
    EditText text;
    ImageView send;
    Bundle ab;
    LinearLayout lvSwitch;
    Calendar calander;
    SimpleDateFormat simpledateformat;
    String Date,TOKEN;
    String decryptmsg;
    boolean doubleBackToExitPressedOnce = false;
    Switch actionbar_view_switch;
    private MsgAdapter msgAdapter;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        typeMsgActivity = this;
        msgLen = 0;
        ab = getIntent().getExtras();
        sender = ab.getString("sender");
        //tokon = ab.getString("token");
        //isEncypted = ab.getString("isEncypted");
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                TOKEN = instanceIdResult.getToken();

                Log.d("DEVICE TOKEN:", TOKEN);
            }
        });

        setActionBarView();


        //date_time="2018-02-06 00:00:00.000000";

        //Toast.makeText(typeMsgActivity, ""+isEncypted, Toast.LENGTH_SHORT).show();

        try {
            Cipher c = Cipher.getInstance("DES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        findViewById();
        userSessionManager = new UserSessionManager(this);
        receiver = userSessionManager.getUserDetails().get("mob");
        mLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLinearLayoutManager);

        getMesgFromServer();
        msgAdapter = new MsgAdapter(getBaseContext(), responseMessages);
        recyclerView.setAdapter(msgAdapter);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getMesgFromServer();
                    }
                });

            }
        }, 5000, 4000);


        System.out.println("tokon  ::" + tokon);

    }


    private void getMesgFromServer() {

       /* final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing...");
        progressDialog.show();*/
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UserSessionManager.url + "Msg/getAll/receiver/" + receiver + "/sender/" + sender,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.e("response", response);
                            //progressDialog.dismiss();
                            try {

                                JSONObject obj = new JSONObject(response);
                                JSONArray jArray = obj.getJSONArray("msg");
                                //int len = jArray.length();
                                responseMessages.clear();
                                for (int i = 0; i < jArray.length(); i++) {
                                    JSONObject json_data = jArray.getJSONObject(i);


                                    responseMessages.add(new ResponseMessage(
                                            "" + json_data.getString("sender"),
                                            "" + json_data.getString("receiver"),
                                            "" + json_data.getString("messages"),
                                            "" + json_data.getString("private_key"),
                                            "" + json_data.getString("id"),
                                            "" + json_data.getString("isEncypted")
                                    ));

                                }

                                if (responseMessages.size() > msgLen) {

                                    msgLen = responseMessages.size();
                                    msgAdapter.notifyDataSetChanged();
                                    recyclerView.smoothScrollToPosition(responseMessages.size());
                                    //
                                    // recyclerView.scrollToPosition(2*responseMessages.size()+1);


                                }


//
//                                recyclerView.addOnItemTouchListener(
//                                        new RecyclerItemClickListener(getBaseContext(), new RecyclerItemClickListener.OnItemClickListener() {
//                                            @Override public void onItemClick(View view, int position) {
//                                                // TODO Handle item click
//
//                                                dialog(responseMessages.get(position).getMsg(),1,position);
//
//                                            }
//                                        })
//                                );


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("nitin3", e.getMessage());
                                //Toast.makeText(getApplicationContext(),"Data fail",Toast.LENGTH_SHORT).show();

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("error", error.toString());
                            // progressDialog.dismiss();

                            //username.setError(getString(R.string.error_incorrect_username));

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    /*params.put("username", user);
                    params.put("password", pass);*/
                    //params.put("app1", "2");
                    return params;
                }


            };

            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findViewById() {
        lvSwitch = (LinearLayout) findViewById(R.id.lvSwitch);
        lvSwitch.setVisibility(View.VISIBLE);


        calander = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        send = (ImageView) findViewById(R.id.iv_send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date = simpledateformat.format(calander.getTime());

                if (isEncypted != null) {
                    if (isEncypted.equalsIgnoreCase("1")) {
                        dialog(text.getText().toString().trim(), 2, 0);
                    } else {
                        try {
                            SendMsgToServer(sender, receiver, "1", "" + Des._encrypt(text.getText().toString().trim(), "1"), Date, "0");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    dialog(text.getText().toString().trim(), 2, 0);
                }

            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        text = (EditText) findViewById(R.id.textMsg);
        tv_title = (TextView) actionBarView.findViewById(R.id.actionbar_view_title);
        actionbar_view_switch = (Switch) actionBarView.findViewById(R.id.actionbar_view_switch);
        home = (TextView) actionBarView.findViewById(R.id.actionbar_view_tv_left);
        home.setVisibility(View.VISIBLE);
        home.setText("Back");
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
        if (isEncypted != null) {
            if (isEncypted.equalsIgnoreCase("1")) {
                actionbar_view_switch.setChecked(true);

            } else {
                actionbar_view_switch.setChecked(false);
            }
        }
        actionbar_view_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    isEncypted = "1";
                    //changeEncryptionMode(sender, receiver, "1");
                } else {
                    isEncypted = "0";
                    //changeEncryptionMode(sender, receiver, "0");
                }

            }
        });

        tv_title.setText("User Name");
    }

   /* public void setSwitch(Boolean b){
        actionbar_view_switch.setChecked(b);
        flag=true;
    }*/

    private void SendMsgToServer(final String sender, final String receiver, final String privateKey, final String msg, final String date_time, final String isEncypted1) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing...");
        progressDialog.show();
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UserSessionManager.url + "msg/post/",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("responsenew", response);
                            progressDialog.dismiss();
                            try {


                                // JSONArray jArray = obj.getJSONArray("login");
                                //int len = jArray.length();
                                //sendSMS(sender, privateKey,"1");
                                text.setText("");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        sendToken(receiver, msg);

                                    }
                                });


                                //getMesgFromServer();

                                //JSONObject obj = new JSONObject(response);
                                /*
                                JSONArray jArray = new JSONArray(response);
                                JSONObject jobj=jArray.getJSONObject(0);
                                String o=jobj.getString("sucessArrayName");
                                Log.e("nitinhello",o);
                                JSONArray jArray2=new JSONArray(o);
                                //int len = jArray.length();
                                String mid="";
                                for (int i = 0; i < jArray2.length(); i++) {
                                    JSONObject json_data = jArray.getJSONObject(i);
                                    mid=json_data.getString("mid");

                                    Log.i("log_tag",
                                            " user_id : " + json_data.getString("mid")
                                    );


                                }
                                */


                                //sendSms(user,msg);


                            } catch (Exception e) {
                                e.printStackTrace();
                                //Toast.makeText(getApplicationContext(),"Data fail",Toast.LENGTH_SHORT).show();

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("error", error.toString());
                            progressDialog.dismiss();

                            //username.setError(getString(R.string.error_incorrect_username));

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("sender", receiver);
                    params.put("receiver", sender);
                    params.put("private_key", privateKey);
                    params.put("messages", msg);
                    params.put("date_time", date_time);
                    params.put("active", "1");
                    params.put("isEncypted", "" + isEncypted1);

                    System.out.println("Map Data : " + params.toString());
                    return params;
                }


            };

            VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private void changeEncryptionMode(final String sender ,final String receiver,final String isEncypted) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing...");
        progressDialog.show();
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UserSessionManager.url+"Login/post/",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("responsenew",response);
                            progressDialog.dismiss();
                            try {


                                JSONObject obj = new JSONObject(response);
                                JSONArray jArray = obj.getJSONArray("result");
                                //int len = jArray.length();
                                responseMessages.clear();
                                for (int i = 0; i < jArray.length(); i++) {
                                    JSONObject json_data = jArray.getJSONObject(i);
                                    if (json_data.getString("status").equalsIgnoreCase("200")){
                                        responseMessages.clear();
                                        msgLen=0;
                                        getMesgFromServer();
                                    }
                                }



                                //sendSms(user,msg);


                            } catch (Exception e) {
                                e.printStackTrace();
                                //Toast.makeText(getApplicationContext(),"Data fail",Toast.LENGTH_SHORT).show();

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("error",error.toString());
                            progressDialog.dismiss();

                            //username.setError(getString(R.string.error_incorrect_username));

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("sender", receiver);
                    params.put("receiver", sender);
                    params.put("isEncypted", isEncypted);
                    return params;
                }


            };

            VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }*/
    private void sendSMS(String phoneNumber, String message, String mid) {
        Toast.makeText(getApplicationContext(), phoneNumber + message, Toast.LENGTH_LONG).show();
        PendingIntent pi = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, pi, null);
        Toast.makeText(this, "sms Send ", Toast.LENGTH_SHORT).show();
    }


    private void setActionBarView() {

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_view_custom);
        actionBarView = getSupportActionBar().getCustomView();
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void dialog(final String msg, final int type, final int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Private Key");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                privateKey = input.getText().toString().trim();
                String decrypt, encrypt;
                switch (type) {
                    case 1:
                        try {

                            decrypt = Des._decrypt(msg, privateKey);
                            responseMessages.get(index).setMsg(decrypt);
                            msgAdapter.notifyDataSetChanged();
                            new CountDownTimer(8000, 1000) {

                                public void onTick(long millisUntilFinished) {
                                    //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                                    //here you can have your logic to set text to edittext
                                }

                                public void onFinish() {
                                    responseMessages.get(index).setMsg(msg);
                                    msgAdapter.notifyDataSetChanged();
                                }

                            }.start();

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Wrong Key", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case 2:
                        try {
                            encrypt = Des._encrypt(msg, privateKey);
                            SendMsgToServer(sender, receiver, privateKey, encrypt, Date, "1");
                            //Toast.makeText(getApplicationContext(),"hit",Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        break;
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        builder.show();
    }

    public void sendToken(String title, String body) {
        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", title);
            notifcationBody.put("message", body);

//                    if (userToken.toString().isEmpty()) {
//                        MainToken = TOKEN;
//                    } else {
//                        MainToken = UserToken;
//                    }

            System.out.println("Map Token :: "+sender);

            notification.put("to","/topics/"+sender);
//            notification.put("to", "egLdu-_bP88:APA91bHao-8nXht2HVwNbKekXdVJes8OWpaHZLlSbCJAA3BCUb8xGSBV8TKJ73yXTyn1NP0YKbUQqAQKVJWN7YXS");
            notification.put("data", notifcationBody);
            sendNotification(notification);
        } catch (JSONException e) {
            e.getStackTrace();
            Log.e("notify", "onCreate: " + e.getMessage());
        }

    }


    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("nofity", "onResponse: " + response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("error notify", "onErrorResponse: Didn't work");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
}


