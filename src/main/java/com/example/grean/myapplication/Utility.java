package com.example.grean.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by Administrator on 2016/11/4.
 */
public class Utility extends Application{

    private static Utility singleton;
    public static Utility getInstance(){
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }

    public Handler handler_count_time;
    public static Runnable runnable_count_time;



    private static long mLastClickTime;
    public static boolean dlg_cancel = false;
    public static long still_time = 60*1000; //initialize as 60 seconds

    /**
     * detect whether click too fast, in case of popping up multiple dialog.
     * @return returns whether the click is too fast
     */
    public static boolean isFastClick() {
        // current time
        long currentTime = System.currentTimeMillis();

        // gap between two clicks <500 ?
        if ( currentTime - mLastClickTime < 800) {
            mLastClickTime = currentTime;
            return true;
        }
        mLastClickTime = currentTime;
        return false;
    }

    public static AlertDialog AddCancelBtn(AlertDialog dialog){
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, Resources.getSystem().getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dlg_cancel = true;
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dlg_cancel = false;
            }
        });
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    /**
     * To hide keyboard when edittext lose focus
     * @param v
     * @param hasFocus
     */

    public static View.OnFocusChangeListener if_keyboard_losefocus = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                hideKeyboard(v, v.getContext().getApplicationContext());
            }
        }
    };

    public static void hideKeyboard(View view, Context context) {
        InputMethodManager inputMethodManager =(InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    /**
     * way to set all edittext listener to if_keyboard_losefocus
     */

    public static void set_all_edit_focus_listener(View view){
        if (view instanceof EditText) {
            view.setOnFocusChangeListener(if_keyboard_losefocus);
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                set_all_edit_focus_listener(innerView);
            }
        }
    }


    //instead I use onfocuschange listener to edittextview that's easy
    //hide the soft keyboard when click views that are not edittext
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
//                    hideSoftKeyboard(MyActivity.this);  //error occurs
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }
}
