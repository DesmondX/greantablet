package com.example.grean.myapplication;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import grean.custom.component.GreanToast;

/**
 * Created by Administrator on 2016/11/9.
 */
public class SettingSubTab_Com extends BaseFragment {
    private Context mContext;
    private EditText address,sendmgl, senddigit;
    private Switch switch_com;
    private Button btn_send;
    private Spinner spinner_baud_rate;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        return  inflater.inflate(R.layout.setting_com,container,false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("xjj","add textwatcher");
        initializeLeft(view);
        initializeRight(view);
        initializeSpinner(view);
    }

    private void initializeLeft(View view){
        address = (EditText) view.findViewById(R.id.address);
        address.setOnFocusChangeListener(Utility.if_keyboard_losefocus);
    }



    private void initializeRight(View view){
        sendmgl = (EditText) view.findViewById(R.id.send_mgl);
        sendmgl.setText("0.5");
        //hide keyboard when lose focus
        sendmgl.setOnFocusChangeListener(Utility.if_keyboard_losefocus);
        //first way: allow only 999 when input
        sendmgl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !s.equals("")) {
                    float aa = 0;
                    try {
                        aa = Float.parseFloat(s.toString());
                        Log.d("xjj","aa:"+aa);
                    } catch (NumberFormatException e) {
                        // TODO Auto-generated catch block
                        Log.d("xjj","number format exception");
                    }
                    if (aa > 999f)
                        sendmgl.setText("999");
                }
            }
        });

        //second way: allow only 999 after input
        sendmgl.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                float mgl = 0;
//                if(v.getText().toString().isEmpty()) return false;
                try{
                    mgl = Float.valueOf(v.getText().toString());
                }catch (NumberFormatException e){
                    Log.d("xjj","number format exception");
                }
                if (mgl > 999f)  sendmgl.setText("999");
                return false;
            }
        });

        final GreanToast gt = new GreanToast(mContext);
        btn_send = (Button) view.findViewById(R.id.com_button);
        btn_send.setEnabled(false);
        btn_send.setBackground(getResources().getDrawable(R.color.gray_light));
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sendmgl.getText().toString().isEmpty()) {
//                    new GreanToast(mContext).show("发送失败:数据不可为空");
                    gt.show("发送失败:数据不可为空");
                    return;
                }
                //send operate
                gt.show("successfully sent");
            }
        });

        senddigit = (EditText) view.findViewById(R.id.send_digit);
        senddigit.setText("01 03 04 3F 00 00 00 F6 27");
        senddigit.setOnFocusChangeListener(Utility.if_keyboard_losefocus);
        switch_com = (Switch) view.findViewById(R.id.switch_com);
        switch_com.setChecked(getResources().getBoolean(R.bool.if_comtest_on));
        switch_com.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btn_send.setEnabled(isChecked?true:false);
                btn_send.setBackground(isChecked?getResources().getDrawable(R.drawable.bt_selector):getResources().getDrawable(R.color.gray_light));
            }
        });
    }

    private void initializeSpinner(View view){
        spinner_baud_rate = (Spinner) view.findViewById(R.id.spinner_baud_rate);
        spinner_baud_rate.getBackground().setColorFilter(getResources().getColor(R.color.blue_dark), PorterDuff.Mode.SRC_ATOP);
        spinner_baud_rate.setSelection(1);
        spinner_baud_rate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new GreanToast(getActivity()).show("select:"+parent.getItemAtPosition(position).toString());
                ((TextView)parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.blue_dark));
                ((TextView)parent.getChildAt(0)).setTextSize(30);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }




}
