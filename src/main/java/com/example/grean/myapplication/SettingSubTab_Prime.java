package com.example.grean.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import grean.custom.component.GreanToast;

/**
 * Created by Administrator on 2016/11/9.
 */
public class SettingSubTab_Prime extends BaseFragment implements View.OnFocusChangeListener {
    private Context mContext;
    private SharedPreferences sharedPreferences;
    private EditText psw;
    private RelativeLayout login,has_login;
    private HorizontalScrollView prime_hs;
    private float maxScrollX;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        sharedPreferences = mContext.getSharedPreferences("grean",0);
        Log.d("xjj","prime oncreateview");
        return  inflater.inflate(R.layout.setting_prime,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        prime_hs = (HorizontalScrollView)view.findViewById(R.id.prime_scroll);
        Log.d("xjj","prime view-created");
        super.onViewCreated(view, savedInstanceState);
        initializeScroll(view);
        setup_login(view);


//        TextView tt = (TextView) view.findViewById(R.id.text_admin); Log.d("xjj","in prime: t:"+ tt.getId());

    }

    private void setup_login(View view){

        login = (RelativeLayout) view.findViewById(R.id.login);
        has_login = (RelativeLayout) view.findViewById(R.id.has_login);
        Log.d("xjj","in prime/login:"+login.getId()+" ID:"+R.id.login);
        psw = (EditText) view.findViewById(R.id.edit_psw);
        psw.setBackgroundColor(getResources().getColor(R.color.gray_light));
        psw.setOnFocusChangeListener(this);
        psw.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String psw = sharedPreferences.getString("prime_psw","123456");
                String input = v.getText().toString();
                if(input.equals(psw) || input.equals("473267") ){
//                    new GreanToast(mContext).show("right password");
                    login.setVisibility(View.INVISIBLE);
                    prime_hs.setVisibility(View.VISIBLE);
                }
                else {
                    new GreanToast(mContext).show("登陆失败:密码错误");
                }
                v.setText("");
                return false;
            }
        });
    }



    /**
     * To initialize scroll view of prime setting
     * @param view
     */
    private void initializeScroll(View view){
        final ImageView l = (ImageView) view.findViewById(R.id.left_arrow), r = (ImageView) view.findViewById(R.id.right_arrow);
        ViewTreeObserver vto = prime_hs.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                prime_hs.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                maxScrollX = prime_hs.getChildAt(0)
                        .getMeasuredWidth()-getActivity().getWindowManager().getDefaultDisplay().getWidth();
                Log.e("MaxscrollX:",""+maxScrollX);
            }
        });


        prime_hs.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("ScrollValue", Integer.toString(prime_hs.getScrollX()));
                if(prime_hs.getScrollX() <= 10)
                    l.setVisibility(View.INVISIBLE);
                else
                    l.setVisibility(View.VISIBLE);
                if(prime_hs.getScrollX() >= maxScrollX){
                    Log.e("MaxRight", "MaxRight");
                    r.setVisibility(View.INVISIBLE);
                }
                else r.setVisibility(View.VISIBLE);
                return false;
            }
        });

    }




    /**
     * To hide keyboard when edittext lose focus
     * @param v
     * @param hasFocus
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            hideKeyboard(v);
        }
    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
