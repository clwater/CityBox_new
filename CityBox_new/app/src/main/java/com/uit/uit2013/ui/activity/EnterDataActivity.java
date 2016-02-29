package com.uit.uit2013.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.uit.uit2013.R;

/**
 * Created by yszsyf on 16/2/16.
 * 确认订单的时候 设置相关数据
 */
public class EnterDataActivity extends Activity implements View.OnClickListener {

    private TextView change_back , change_title ,change_finish ;
    private TextView change_remark;
    private EditText change_text;
    private Intent intent;
    private String title , text ,remark;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.enterdataactivity);

        create();
        setstatu();
    }

    private void setstatu() {

        intent = getIntent();
        //通过intent穿值 判断什么类型的数据

        title  = intent.getStringExtra("name");
        text = intent.getStringExtra("text");
        remark = intent.getStringExtra("remark");

        change_title.setText(title);
        change_text.setText(text);
        change_remark.setText(remark);

        if(title.equals("打赏") || title.equals("联系方式")){
            change_text.setKeyListener(new
                    DigitsKeyListener(false,true));
            //如果是打赏或者联系方式的话只能输入数字
        }


    }

    private void create() {
        change_back = (TextView)findViewById(R.id.change_back);
        change_back.setOnClickListener(this);
        change_title = (TextView)findViewById(R.id.change_title);
        change_finish = (TextView)findViewById(R.id.change_finish);
        change_finish.setOnClickListener(this);
        change_text = (EditText)findViewById(R.id.change_text);
        change_remark = (TextView)findViewById(R.id.change_remark);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.change_back:
                this.finish();
                break;
            case R.id.change_finish:
                changefinish();
                break;
        }
    }

    private void changefinish() {
            intent.putExtra("returnname" ,title);
            intent.putExtra("returntext" ,change_text.getText().toString());
            this.setResult(1,intent);           //
            this.finish();


    }
}
