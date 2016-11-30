package com.example.grean.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;

import grean.custom.component.ExtendedHorizontalScrollView;

/**
 * Created by Administrator on 2016/11/9.
 */
public class SettingSubTab_Control extends BaseFragment {

    private Context mContext;
    private RadioGroup rg1, rg2;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.setting_control,container,false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();
        initializeScroll(view);
        initializeEightValve(view);
        Utility.set_all_edit_focus_listener(view);
    }

    private void initializeScroll(View view){

        final ImageView l = (ImageView) view.findViewById(R.id.left_arrow), r = (ImageView) view.findViewById(R.id.right_arrow);
        ExtendedHorizontalScrollView scrollView = (ExtendedHorizontalScrollView) view.findViewById(R.id.control_scroll);
        scrollView.setScrollStateListener(new ExtendedHorizontalScrollView.IScrollStateListener() {
            @Override
            public void onScrollMostLeft() {
                l.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onScrollFromMostLeft() {
                l.setVisibility(View.VISIBLE);
            }

            @Override
            public void onScrollMostRight() {
                r.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onScrollFromMostRight() {
                r.setVisibility(View.VISIBLE);
            }
        });

    }

    /**
     *  To mutex eight valves, only one can be checked at a time.
     * @param view
     */
    private void initializeEightValve(View view){

        rg1 = (RadioGroup) view.findViewById(R.id.eight_radio1);
        rg2 = (RadioGroup) view.findViewById(R.id.eight_radio2);
        rg1.clearCheck();
        rg2.clearCheck();
        rg1.setOnCheckedChangeListener(RadioGroupListener);
        rg2.setOnCheckedChangeListener(RadioGroupListener);

//        to get the checkid
//        int chkId1 = rg1.getCheckedRadioButtonId();
//        int chkId2 = rg2.getCheckedRadioButtonId();
//        int realCheck = chkId1 == -1 ? chkId2 : chkId1;
    }

    private RadioGroup.OnCheckedChangeListener RadioGroupListener = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            RadioGroup thegroup = group == rg1? rg2:rg1;
            if (checkedId != -1) {
                thegroup.setOnCheckedChangeListener(null); // remove the listener before clearing so we don't throw that stackoverflow exception//and infact it prevents the situation that both clear cuz the checkedid doesnot change in one click but group changed by listener.
                thegroup.clearCheck(); // clear the second RadioGroup!
                thegroup.setOnCheckedChangeListener(RadioGroupListener); //reset the listener
            }
            Log.e("former",""+checkedId);

            switch (checkedId){
                case R.id.valve_1:
                    break;
                case R.id.valve_2:
                    break;
                case R.id.valve_3:
                    break;
                case R.id.valve_4:
                    break;
                case R.id.valve_5:
                    break;
                case R.id.valve_6:
                    break;
                case R.id.valve_7:
                    break;
                case R.id.valve_8:
                    break;
                default:
                    break;
            }
        }
    };


}