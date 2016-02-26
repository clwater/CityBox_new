package com.uit.uit2013.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uit.uit2013.R;
import com.uit.uit2013.ui.activity.BookActivity;
import com.uit.uit2013.ui.activity.ScoreActivity;

/**
 * Created by soul on 2016/1/19.
 */
public class FragmentStudy extends Fragment implements View.OnClickListener {
    private TextView tv;
    private TextView book  , score;

    private View view;
    private Activity activity;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_study, container, false);
        activity = getActivity();
        context = activity.getApplicationContext();



        return view;
    }

    private void create() {
        book = (TextView)view.findViewById(R.id.study_book);
        book.setOnClickListener(this);
        score = (TextView)view.findViewById(R.id.study_score);
        score.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tv = (TextView) getView().findViewById(R.id.titleTv);
        tv.setText("学习");

        create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.study_book:
                startActivity(new Intent(activity , BookActivity.class));

                break;
            case  R.id.study_score:
                startActivity(new Intent(activity , ScoreActivity.class));

                break;
        }
    }
}