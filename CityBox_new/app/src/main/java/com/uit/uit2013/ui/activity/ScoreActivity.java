package com.uit.uit2013.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.uit.uit2013.R;

/**
 * Created by yszsyf on 16/2/23.
 */
public class ScoreActivity  extends Activity{
    private TextView titletv , updatatv;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.study_score);


        createtitle();
    }

    private void createtitle() {
        titletv = (TextView)findViewById(R.id.titleTv);
        titletv.setText("成绩查询");
        updatatv = (TextView)findViewById(R.id.updatatv);
        updatatv.setText("");

    }

}
