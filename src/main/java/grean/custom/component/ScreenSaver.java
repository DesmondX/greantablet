package grean.custom.component;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.grean.myapplication.R;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Administrator on 2016/11/4.
 */
public class ScreenSaver extends Activity{

    private TextView tv;
    private float light = 0f;
    private AtomicBoolean temrun = new AtomicBoolean(true);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);

        tv = new TextView(this);
        tv.setTextSize(60);
        tv.setTextColor(getResources().getColor(R.color.blue_dark));
        tv.setText("Breath effect");  // show text
        tv.setAlpha(0);
        tv.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        layout.addView(tv,params);



        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                temrun.set(false);
                return false;
            }
        });
        setContentView(layout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        breath();
    }

    private Handler handler = new Handler(){
        public void handleMessage(Message message){
            tv.setAlpha(light>1.1?2-light:light);
            light+=0.03;
            if(light>2) light = 0; //instead of light-2
    //        tv.setText(""+light);
        }

    };


    private void breath(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(temrun.get()) {
                    try {
                        Thread.sleep(100);
                        handler.sendMessage(new Message());
                        breath();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    handler.removeCallbacks(this);
                    finish();
                }
            }
        }).start();
    }

}
