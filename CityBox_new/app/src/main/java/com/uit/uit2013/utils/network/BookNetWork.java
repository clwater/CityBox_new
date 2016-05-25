package com.uit.uit2013.utils.network;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.uit.uit2013.utils.analysis.RestaurantAnalysis;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yszsyf on 16/2/26.
 */
public class BookNetWork {
    public  static String Book(String bookname){
        String result = null;


        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());

        HttpClient httpClient = new DefaultHttpClient();
        //http://cityuit.wuxiwei.cn/index.php/Home/Campus/appLibrary/title/时间简史
        String url = "http://cityuit.wuxiwei.cn/index.php/Home/Campus/appLibrary/title/" + bookname;
       // Log.d("bookbn" , "url" + url);
        HttpGet httpget = new HttpGet(url);
        List<NameValuePair> params  =new ArrayList<NameValuePair>();

        try {
            HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            HttpResponse httpResp = httpClient.execute(httpget);
            if (httpResp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
                Log.d("bookbn" , result);
            }

        } catch (IOException e) {}


        //return "";
        return result;
    }
}
