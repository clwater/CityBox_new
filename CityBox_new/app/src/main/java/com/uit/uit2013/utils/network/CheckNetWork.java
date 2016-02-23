package com.uit.uit2013.utils.network;

import android.os.StrictMode;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yszsyf on 16/2/23.
 */
public class CheckNetWork {
    public static String Check(String username) throws IOException {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        HttpClient httpClient = new DefaultHttpClient(); //
        String result = "";

        HttpPost httpPost = new HttpPost("http://cityuit.sinaapp.com/sendable.php");
        List<NameValuePair> params  =new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("username", username ));



        try {
            HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            httpPost.setEntity(entity);
            HttpResponse httpResp = httpClient.execute(httpPost);
            if(httpResp.getStatusLine().getStatusCode()== HttpStatus.SC_OK){
                result= EntityUtils.toString(httpResp.getEntity(), "UTF-8");
                return result;
            }
        } catch (IOException e) {}

        return result;
    }
}
