package com.uit.uit2013.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.uit.uit2013.R;
import com.uit.uit2013.ui.activity.LoginActivity;
import com.uit.uit2013.utils.PreferenceTool;
import com.uit.uit2013.utils.db.ScheduleDateCtrl;
import com.umeng.update.UmengDownloadListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

/**
 * Created by soul on 2016/1/19.
 * 设置页面
 */
public class FragmentSeeting extends Fragment implements View.OnClickListener {
    private TextView tv;
    private View view;
    private Activity activity;
    private Context context;
    private TextView setting_esc , setting_update , setting_version , setting_about , setting_back;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        activity = getActivity();
        context = activity.getApplicationContext();

        try {
            create();
        } catch (PackageManager.NameNotFoundException e) {}

        return view;
    }

    private void create() throws PackageManager.NameNotFoundException {
        setting_esc = (TextView) view.findViewById(R.id.setting_esc);
        setting_esc.setOnClickListener(this);
        setting_update = (TextView) view.findViewById(R.id.setting_update);
        setting_update.setOnClickListener(this);
        setting_version = (TextView)  view.findViewById(R.id.setting_version);
        setting_version.setText(setversion());
        setting_about = (TextView) view.findViewById(R.id.setting_about);
        setting_about.setOnClickListener(this);
        setting_back = (TextView) view.findViewById(R.id.setting_back);
        setting_back.setOnClickListener(this);
    }

    private String setversion() throws PackageManager.NameNotFoundException {
        PackageManager packageManager = activity.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(activity.getPackageName(),0);
        String version = packInfo.versionName;
        return version;
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
            case R.id.setting_update:
                umupdate();
                break;
            case R.id.setting_about:
                break;
            case R.id.setting_back:
                back();
                break;


        }
    }

    private void back() {

        AlertDialog.Builder builder=new AlertDialog.Builder(activity);  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage("详细反馈请关注我们的微信平台(csxyxzs)或者我们的用户反馈群(308407868)"); //设置内容
        builder.setPositiveButton("复制群号", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ClipboardManager cmb = (ClipboardManager) activity.getSystemService(activity.CLIPBOARD_SERVICE);
                cmb.setText("308407868");
                Toast.makeText(activity , "已经复制群号 308407868,请长按以粘贴" , Toast.LENGTH_SHORT).show();

            }
        });
        builder.setNegativeButton("复制微信号", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ClipboardManager cmb = (ClipboardManager) activity.getSystemService(activity.CLIPBOARD_SERVICE);
                cmb.setText("csxyxzs");
                Toast.makeText(activity , "已经复制微信公共号 csxyxzs ,请长按以粘贴" , Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }

    private void umupdate() {
        final Context mContext = activity.getApplicationContext();
        Toast.makeText(activity,"检测更新中" ,Toast.LENGTH_SHORT).show();
        UmengUpdateAgent.setUpdateAutoPopup(false);
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus,
                                         UpdateResponse updateInfo) {
                switch (updateStatus) {
                    case UpdateStatus.Yes: // has update
                        UmengUpdateAgent.showUpdateDialog(activity, updateInfo);
                        break;
                    case UpdateStatus.No: // has no update
                        Toast.makeText(activity,"未检测到新版本" ,Toast.LENGTH_SHORT).show();
                        break;
                    case UpdateStatus.Timeout: // time out
                        Toast.makeText(activity,"请检查网络连接" ,Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        UmengUpdateAgent.forceUpdate(mContext);
    }


    private void esc() {


        AlertDialog.Builder builder=new AlertDialog.Builder(activity);  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage("是否确认退出?"); //设置内容
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                PreferenceTool pt = new PreferenceTool(activity);
                PreferenceTool.clear();
                ScheduleDateCtrl.delete(activity);


                activity.finish();
                startActivity(new Intent( activity , LoginActivity.class));
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }
}