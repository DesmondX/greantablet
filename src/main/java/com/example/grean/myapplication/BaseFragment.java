package com.example.grean.myapplication;

import android.app.Fragment;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
/**
 * Created by Administrator on 2016/9/21.
 *
 * to prevent click event going down through to another fragment
 *
 */
public class BaseFragment extends Fragment implements View.OnTouchListener{

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // block ontouch event. in case it would go down through to another fragment below
        view.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }
}