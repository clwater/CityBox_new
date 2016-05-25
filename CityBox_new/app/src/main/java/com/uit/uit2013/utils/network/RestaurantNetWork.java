package com.uit.uit2013.utils.network;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;


import com.uit.uit2013.model.Restaurant;
import com.uit.uit2013.utils.analysis.RestaurantAnalysis;
import com.uit.uit2013.utils.db.ResDateCtrl;

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
import java.util.Vector;

/**
 * Created by yszsyf on 16/2/1.
 * 获取食堂食堂数据 不知道为什么这里用volley不好用 =-=  只能使用android自带的
 */
public class RestaurantNetWork {

    public  static String getRestaurant(Context context){
        String result = null;


        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet("http://cityuit.wuxiwei.cn/index.php/Home/Campus/appShitang");



        List<NameValuePair> params  =new ArrayList<NameValuePair>();

        try {
            HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            HttpResponse httpResp = httpClient.execute(httpget);
            if (httpResp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
               // ResDateCtrl.delete(context);

                Log.d("newshitang" , result);

                RestaurantAnalysis.AnalysisRes(result, context);


            }

        } catch (IOException e) {}


        return result;
    }
}
