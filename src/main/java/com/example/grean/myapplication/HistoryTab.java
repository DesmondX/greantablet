package com.example.grean.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/9/12.
 */
public class HistoryTab extends BaseFragment implements View.OnClickListener {

    private Context mContext;
    private TextView history, diary;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history, container, false);
        mContext = getActivity();

        initialize(view);

        return  view;
    }

    /**
     * iniatialize the view of history tab;
     * @param view is the view been initialized
     */
    private void initialize(View  view){
        history = (TextView) view.findViewById(R.id.show_history);
        history.setTag("history");
        history.setOnClickListener(this);
        diary = (TextView) view.findViewById(R.id.show_diary);
        diary.setTag("diary");
        diary.setOnClickListener(this);
    }

    /**
     * onclick listener
     * @param v the view been clicked
     */
    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        switch(tag){
            case "history":case "diary":
                settv(tag);
                break;

            default:
                break;

        }
    }

    //set the textview background and textcolor.
    private void settv(String tag){
        boolean if_history = tag.equals("history");
        boolean if_diary = tag.equals("diary");

        LinearLayout.LayoutParams params_pressed = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.5f);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
        history.setBackgroundResource(if_history?R.drawable.button_pressed:R.drawable.button);
        if (if_history) history.setTextColor(ContextCompat.getColor(mContext, R.color.blue_dark));
        else history.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        history.setTextSize(if_history?40:25);
        history.setLayoutParams(if_history?params_pressed:params);

        diary.setBackgroundResource(if_diary?R.drawable.button_pressed:R.drawable.button);
        if (if_diary) diary.setTextColor(ContextCompat.getColor(mContext, R.color.blue_dark));
        else diary.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        diary.setTextSize(if_diary?40:25);
        diary.setLayoutParams(if_diary?params_pressed:params);
    }


}
