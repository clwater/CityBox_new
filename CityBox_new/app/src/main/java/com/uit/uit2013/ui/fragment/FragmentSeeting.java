package com.uit.uit2013.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.uit.uit2013.R;
import com.uit.uit2013.ui.activity.LoginActivity;
import com.uit.uit2013.utils.PreferenceTool;
import com.uit.uit2013.utils.db.ScheduleDateCtrl;

/**
 * Created by soul on 2016/1/19.
 */
public class FragmentSeeting extends Fragment implements View.OnClickListener {
    private TextView tv;
    private View view;
    private Activity activity;
    private Context context;
    private Button setting_esc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        activity = getActivity();
        context = activity.getApplicationContext();

        create();

        return view;
    }

    private void create() {
        setting_esc = (Button) view.findViewById(R.id.setting_esc);
        setting_esc.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tv = (TextView) getView().findViewById(R.id.titleTv);
        tv.setText("设置");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_esc:
                esc();
                break;
        }
    }

    private void esc() {
        PreferenceTool pt = new PreferenceTool(activity);
        PreferenceTool.clear();
        ScheduleDateCtrl.delete(activity);


        activity.finish();
        startActivity(new Intent( activity , LoginActivity.class));
    }
}