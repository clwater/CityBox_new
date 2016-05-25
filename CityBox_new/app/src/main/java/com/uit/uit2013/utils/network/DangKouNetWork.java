package com.uit.uit2013.utils.network;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.uit.uit2013.utils.analysis.DangKouAnalysis;
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
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yszsyf on 16/2/13.
 */
public class DangKouNetWork {
    public  static String getDangKou(Context context , String id ) throws JSONException {
        String result = null;


        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());

        HttpClient httpClient = new DefaultHttpClient();

        //Log.d("=-=" , "id:  " + id);


        HttpGet httpget = new HttpGet("http://cityuit.wuxiwei.cn/index.php/Home/Campus/appCaidan/id/" + id);
        List<NameValuePair> params  =new ArrayList<NameValuePair>();




        try {
            HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            HttpResponse httpResp = httpClient.execute(httpget);
            if (httpResp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
                Log.d("=-=" , result);

                DangKouAnalysis.AnalysisRes(result,id, context);
            }

        } catch (IOException e) {}


        return result;
    }



}
