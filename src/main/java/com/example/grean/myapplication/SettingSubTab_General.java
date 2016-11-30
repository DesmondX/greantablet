package com.example.grean.myapplication;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.DataOutputStream;
import java.util.Calendar;

import dwinapi.Dwin;

/**
 * Created by Administrator on 2016/11/9.
 */
public class SettingSubTab_General extends BaseFragment implements View.OnClickListener{

    private Context mContext ;

    private Switch aSwitch;
    private Button btn_set_time;
    private Calendar c;
    private TimePickerDialog tpd;
    private DatePickerDialog dpd;
    private int yyyy, mon, day, hour, min, second;
    private String date, time;
    private SharedPreferences sharedPreferences;
    private SeekBar seekBar;
    private TextView progresstv;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        sharedPreferences = getActivity().getSharedPreferences("grean",0);
//        sharedPreferences = getActivity().getPreferences(0);
        return  inflater.inflate(R.layout.setting_general,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeSystemTime(view);
        initializeBackLight(view);
        initializeScreensaver(view);

        /**
         * temporary use for close this application
         */
        Button close = (Button) view.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

    }


    /**
     * To initialize the method of set time.
     * @param view the view to be initialized.
     */
    public void initializeSystemTime(View view){
        btn_set_time = (Button) view.findViewById(R.id.btn_set_time);
        btn_set_time.setTag("set_time");
        btn_set_time.setOnClickListener(this);

        Utility.dlg_cancel = false;

        c = Calendar.getInstance();
        yyyy = c.get(Calendar.YEAR);
        mon = c.get(Calendar.MONTH)+1;
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);
        second = c.get(Calendar.SECOND);

        //initialize timepickerdialog and datepickerdialog
        tpd = new TimePickerDialog(mContext,new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if(Utility.dlg_cancel) return;
                if(view.isShown()) {
                    time = "" + String.format("%02d",hourOfDay) + String.format("%02d",minute);
                    Log.d("xjj", "" + hourOfDay + " " + minute);
                    SetSystemTime(date, time);
                }
            }
        },hour,min,true);

        Utility.AddCancelBtn(tpd);

        dpd = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date = ""+String.format("%04d",year) + String.format("%02d",monthOfYear+1) + String.format("%02d",dayOfMonth);
            }
        },yyyy,mon,day);
        dpd.setCanceledOnTouchOutside(false);
    }

    /**
     * To initialize the background light control bar
     */
    public void initializeBackLight(View view){
        progresstv = (TextView) view.findViewById(R.id.text_backlight_progress);
        int p = sharedPreferences.getInt("backlight",getResources().getInteger(R.integer.background_light));
        progresstv.setText(Integer.toString( 100*p/255)+"%");
        Dwin.setBrightness(getActivity(),p);
        seekBar = (SeekBar) view.findViewById(R.id.seekbar_light);
        seekBar.setProgress(p);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress < 20){
                    progress=20;
                }
                Dwin.setBrightness(getActivity(),progress);
                progresstv.setText(Integer.toString(100*progress/255)+"%");Log.d("xjj",""+progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sharedPreferences.edit().putInt("backlight",seekBar.getProgress()).apply();
            }
        });
    }



    /**
     * To initialize screensaver
     */
    public void initializeScreensaver(View view){
        aSwitch = (Switch) view.findViewById(R.id.switch_screensaver);
        aSwitch.setChecked(sharedPreferences.getBoolean("saver_on",getResources().getBoolean(R.bool.if_daydream)));
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedPreferences.edit().putBoolean("saver_on",isChecked).apply();
            }
        });


        EditText edt = (EditText) view.findViewById(R.id.still_time);
        edt.setText(Long.toString(sharedPreferences.getLong("still_time", getResources().getInteger(R.integer.still_time))));
        edt.setOnFocusChangeListener(Utility.if_keyboard_losefocus);
        edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(v.getText().toString().isEmpty())
                    return false;
                Long still_time = Long.parseLong(v.getText().toString());
                sharedPreferences.edit().putLong("still_time",still_time).apply();
                return false;
            }
        });
    }



    /**
     * change system time
     */
    public void SetSystemTime(String date, String time){
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(
                    process.getOutputStream());
//            os.writeBytes("setprop persist.sys.timezone GMT\n");
            String datetimes = date+"."+time+"00";
//           String datetimes = "20110101.111111";
            Log.d("xjj","datetimes: "+datetimes);
            os.writeBytes("/system/bin/date -s " + datetimes + "\n");
            os.writeBytes("clock -w\n");
            os.writeBytes("exit\n");
            os.flush();
            Log.d("xjj","1");
        } catch (Exception e) {
            Log.d("xjj", "set time exception");
        }
    }


    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        switch (tag){
            case "set_time":
                tpd.show();
                dpd.show();
                break;
            default:
                break;
        }
    }

}
