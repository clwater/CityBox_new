package com.uit.uit2013.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.uit.uit2013.R;
import com.uit.uit2013.model.User;
import com.uit.uit2013.utils.PreferenceTool;
import com.uit.uit2013.utils.analysis.LoginAnalysis;


import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;



/**
 * Created by soul on 2016/1/16.
 * 登录页面
 */
public class LoginActivity  extends Activity implements View.OnClickListener {
    public static String TAG = "LoginActivity" ;

    private EditText userid , userpw ;
    private Button login;
    private String id , pw ,request ;
    public static Context context;
    public static ProgressDialog pr;
    RequestQueue mQueue;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loginactivity);
        oncreate();
    }

    //初始化
    private void oncreate() {
        userid = (EditText) findViewById(R.id.userid);
        userpw = (EditText) findViewById(R.id.passwd);
        login = (Button) findViewById(R.id.log_button);
        login.setOnClickListener(this);
    }

    public void onClick(View view) {
        id = userid.getText().toString();
        pw = userpw.getText().toString();

        pr = ProgressDialog.show(LoginActivity.this, null, "登陆中......");
        mQueue = Volley.newRequestQueue(this);
        CountingTask task=new CountingTask();
        task.execute();
    }

private class CountingTask extends AsyncTask<Void, Void, Void> {
    boolean pd = false;
    protected Void doInBackground(Void... params) {
        StringRequest postRequest = new StringRequest(Request.Method.POST ,  "http://120.27.53.146:5000/api/login",
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        request = response;
                        LoginAnalysis la  = new LoginAnalysis();
                        try {
                            pd = la.getstatu(response);
                            if (pd){
                                loginsuccess();
                            }else {
                                pr.dismiss();
                                Toast.makeText(LoginActivity.this , "账号或密码错误" , Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                        }
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {}
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", id);
                params.put("password", pw);
                return params;
            }
        };
        mQueue.add(postRequest);
        return null;
    }

    protected void onProgressUpdate(Void... progress){}
    protected void onPostExecute(Void result){}

}
    private void loginsuccess() throws JSONException {
        Toast.makeText(this,"登陆成功" , Toast.LENGTH_SHORT).show();

        savelogin();
        pr.dismiss();
        startActivity(new Intent( this , MainActivity.class));
    }

    private void savelogin() throws JSONException {
        LoginAnalysis la = new LoginAnalysis();
        User user = new User();
        user = la.getUser(request,id,pw);

        PreferenceTool pt = new PreferenceTool(this);
        pt.setPreferenceTool(user);
        this.finish();
    }
}
