package com.uit.uit2013.ui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.twotoasters.jazzylistview.JazzyListView;
import com.twotoasters.jazzylistview.effects.FanEffect;
import com.uit.uit2013.R;
import com.uit.uit2013.ui.activity.LifeAcceptActivity;
import com.uit.uit2013.ui.activity.LifeRestauranActivity;
import com.uit.uit2013.utils.PreferenceTool;
import com.uit.uit2013.utils.network.CheckNetWork;
import com.uit.uit2013.utils.network.GetAllCanAcceptOrderNetWork;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by soul on 2016/1/19.
 */
public class FragmentLife extends Fragment implements View.OnClickListener {
    private TextView tv , life_restaurant , life_accept ,updatatv;
    private JazzyListView jazzylistview_life ;
    private View view;
    private Activity activity;
    private Context context;
    public static ProgressDialog pr;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_life, container, false);
        activity = getActivity();
        context = activity.getApplicationContext();

        create();

       // test();

        return view;
    }

    private void create() {
        life_restaurant = (TextView) view.findViewById(R.id.life_restaurant);
        life_accept = (TextView) view.findViewById(R.id.life_accept);
        updatatv = (TextView) view.findViewById(R.id.updatatv);
        updatatv.setText("");

        life_restaurant.setOnClickListener(this);
        life_accept.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.life_restaurant:
                startActivity(new Intent(activity , LifeRestauranActivity.class));
                break;
            case R.id.life_accept:
                check();

                break;
        }

    }

    private void check() {

        pr = ProgressDialog.show(activity, null, "正在查看送餐资格");

       CheckTask task=new CheckTask();
        task.execute();




    }




    private class CheckTask extends AsyncTask<Void, Void, Void> {
        String resultweb = "";
        PreferenceTool pt = new PreferenceTool(activity);

        protected Void doInBackground(Void... params) {
            try {
                resultweb = CheckNetWork.Check(PreferenceTool.getid());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onProgressUpdate(Void... progress){}
        protected void onPostExecute(Void result){
            analysiccheck(resultweb);
        }
    }

    private void analysiccheck(String resultweb) {
        pr.dismiss();
        if (resultweb.equals("{\"status\":\"ok\"}")){

            startActivity(new Intent(activity , LifeAcceptActivity.class));
        }else {
            Toast.makeText(activity , "您未申请送餐，请关注微心城院小助手（csxyxzs）申请送餐，感谢使用。" , Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tv = (TextView) getView().findViewById(R.id.titleTv);
        tv.setText("生活");
    }


}