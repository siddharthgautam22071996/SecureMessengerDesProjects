package com.example.securecommunicationusingdes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;



public class ForgetPassword extends AppCompatActivity{
    EditText number;
    Button bt;
    String user;
    LinearLayout getpass;
    String getway;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);
        getpass = (LinearLayout)findViewById(R.id.getpass);
        number = (EditText)findViewById(R.id.tv_getPassword);
        bt = (Button)findViewById(R.id.bt);


        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = number.getText().toString().trim();
                if(user.length()==10){
                    GetUserFromServer();
                }else{
                    Toast.makeText(getApplicationContext(),"Number incorrect",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void GetUserFromServer() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing...");
        progressDialog.show();
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UserSessionManager.url+"Login/get/mobile/"+user,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("response",response);
                            progressDialog.dismiss();
                            try {

                                JSONObject obj = new JSONObject(response);
                                JSONArray jArray = obj.getJSONArray("login");
                                //int len = jArray.length();
                                for (int i = 0; i < jArray.length(); i++) {
                                    JSONObject json_data = jArray.getJSONObject(i);
                                   /* Log.i("log_tag",
                                            " user_id : " + json_data.getString("user_id")
                                    );*/
                                   String senderId ="TECHFI";
                                   String serverUrl = "msg.msgclub.net";
                                   String authKey = "c35b6d3e2bb6769b86536f1ac249a69";
                                   String  routeId="1";

                                   String message="Your Password is "+json_data.getString("password") +"";

                                    String getData = "mobileNos="+json_data.getString("mobile")+"&message="+ URLEncoder.encode(message, "utf-8")+"&senderId="+senderId+"&routeId="+routeId;

                                    //API URL
                                    getway="http://"+serverUrl+"/rest/services/sendSMS/sendGroupSms?AUTH_KEY="+authKey+"&"+getData;
                                    sendUserpassword();




                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(),"Wrong fail",Toast.LENGTH_SHORT).show();

                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
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
                    /*params.put("username", user);
                    params.put("password", pass);*/
                    //params.put("app1", "2");
                    return params;
                }


            };

            VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void sendUserpassword() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing...");
        progressDialog.show();
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, getway,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("response",response);
                            progressDialog.dismiss();
                            try {


                                Toast.makeText(getApplicationContext(),"Your Password sent in your mobile No.",Toast.LENGTH_LONG).show();

                                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                finish();
                                JSONObject obj = new JSONObject(response);
                                //JSONArray jArray = obj.getJSONArray("login");
                                //int len = jArray.length();


                                }
                             catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext()," fail",Toast.LENGTH_SHORT).show();

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
                    /*params.put("username", user);
                    params.put("password", pass);*/
                    //params.put("app1", "2");
                    return params;
                }


            };

            VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


}
