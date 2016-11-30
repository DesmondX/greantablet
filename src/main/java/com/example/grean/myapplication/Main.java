package com.example.grean.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import dwinapi.Dwin;
import grean.custom.component.ScreenSaver;


/**
 * Created by Administrator on 2016/9/7.
 */
public class Main extends Activity   {

    //the number of tabs
   private final int tabnum =5;
   private StateListDrawable[] states = new StateListDrawable[tabnum];
   private RadioButton[] rbs =new RadioButton[tabnum];
   private Fragment [] frgs ={ new  HomeTab(), new OperateTab(), new SettingTab(), new HistoryTab(), new InfoTab() };

    private long gap_time=0;
    private Date last_time;
    private AtomicBoolean runSaver = new AtomicBoolean(false);

    private SharedPreferences prf ;
    private Long still_time =0l;
    private boolean saver_on = true;
    public static  Dwin dwin = Dwin.getInstance();
    /**
     * a handler and a runnable to count user still time. used by screensaver
     */
    public  Handler handler_count_time = new Handler();
    public  Runnable runnable_count_time = new Runnable() {
        @Override
        public void run() {
            Date time_now = new Date(System.currentTimeMillis());
            gap_time = time_now.getTime() - last_time.getTime();
            still_time = prf.getLong("still_time",60);
            saver_on = prf.getBoolean("saver_on", true);
            if(gap_time > still_time*1000 && saver_on){
                if(!runSaver.get()){
                    runSaver.set(true);
                    Intent i = new Intent(getApplicationContext(),ScreenSaver.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
//                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }else {
                runSaver.set(false);
            }
            handler_count_time.postDelayed(runnable_count_time,1000);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.tab);

        prf = getSharedPreferences("grean",0);
//        prf = this.getPreferences(0);
        //create dwin and hide navigationbar
            dwin.hideNavigation();


        RadioGroup radio = (RadioGroup) findViewById(R.id.radio);
        //to set every tab's background
        final Integer[] rbId ={R.id.hometab, R.id.operatetab, R.id.settingtab, R.id.historytab, R.id.infotab };
        Integer[] drId ={R.mipmap.home, R.mipmap.opt, R.mipmap.set, R.mipmap.history, R.mipmap.info};
        Integer[] ckId ={R.mipmap.homeck, R.mipmap.optck, R.mipmap.setck, R.mipmap.historyck, R.mipmap.infock};
        for(int i=0; i<rbId.length; i++) {

            rbs[i]=(RadioButton)findViewById(rbId[i]);

            StateListDrawable temp= new StateListDrawable();
            states[i]=temp;
            states[i].addState(new int[] {android.R.attr.state_checked},
                    getResources().getDrawable(ckId[i]));
            states[i].addState(new int[] { },
                    getResources().getDrawable(drId[i]));

            rbs[i].setBackground(states[i]);

        }


        //bound 5 radio buttons with its fragment.
        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                for(int i=0; i<rbId.length; i++)
                {
                    if(checkedId == rbId[i])
                    {
                        Log.d("xjj","fgs["+i+"]: "+frgs[i] );
                        updatefrag(frgs[i]);
                        break;
                    }
                }
            }
        });
        updatefrag(frgs[0]);
    }


    /**
     * update fragments
     *@param  fragment ,the upper fragment which should be shown
     */
    private void updatefrag (Fragment fragment){
//        try {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
//        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        if(fragment.isAdded())
        {
            for(Fragment fg: frgs)
            {
                if(fg == fragment)
                    ft.show(fg);
                else
                    ft.hide(fg);
            }
        }
        else  ft.add(R.id.frame, fragment).show(fragment);

        ft.commit();Log.d("xjj","main commit");


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        updateUserActionTime();
        return super.dispatchTouchEvent(ev);
    }

    public void updateUserActionTime(){
        Date now = new Date(System.currentTimeMillis());
        last_time.setTime(now.getTime());
    }

    @Override
    protected void onResume() {
        super.onResume();
        last_time = new Date(System.currentTimeMillis());
        handler_count_time.post(runnable_count_time);
        Log.d("saver","start post");

        //set brightness
        int p = prf.getInt("backlight",getResources().getInteger(R.integer.background_light));
        Dwin.setBrightness(this,p);
    }

    @Override
    protected void onDestroy() {

        dwin.showNavigation();
        handler_count_time.removeCallbacks(runnable_count_time);
        Log.d("xjj","main destroyed");
        super.onDestroy();
    }




}
