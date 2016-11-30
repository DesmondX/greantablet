package com.example.grean.myapplication;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

import dwinapi.Dwin;
import grean.custom.component.ScreenSaver;

/**
 * Created by Administrator on 2016/9/12.
 */
public class SettingTab extends BaseFragment  {
    private Context mContext;


    private Integer[] rbId = {R.id.setting_general, R.id.setting_com,R.id.setting_control,R.id.setting_prime};
    private StateListDrawable[] states = new StateListDrawable[rbId.length];
    private RadioButton[] rbs =new RadioButton[rbId.length];
    private Fragment[] setfrgs ={new SettingSubTab_General(), new SettingSubTab_Com(), new SettingSubTab_Control(), new SettingSubTab_Prime()};

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.setting, container, false);
        mContext = getActivity();
        /**
         * set for radio buttons
         */
        for (int i=0; i< rbId.length; i++){
            rbs[i] = (RadioButton) view.findViewById(rbId[i]);
            StateListDrawable temp = new StateListDrawable();
            states[i] = temp;
            states[i].addState(new int[] {android.R.attr.state_checked},getResources().getDrawable(R.mipmap.rbck)); //android.R.color.transparent;
            states[i].addState(new int[] {}, getResources().getDrawable(R.mipmap.buttonbg));
            rbs[i].setBackground(states[i]);
        }


        final View layout = inflater.inflate(R.layout.setting_prime, null);
        TextView t = (TextView) layout.findViewById(R.id.text_admin);Log.d("xjj","t:"+t.getId());
        t.setText("heyman");

        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i =0; i<rbId.length; i++){
                    if(checkedId == rbId[i]){
                        Log.d("xjj","setfgs["+i+"]: "+setfrgs[i] );
                        updatefrag(setfrgs[i]);
                        break;
                    }
                }
            }
        });
        updatefrag(setfrgs[0]);

/**
 * temporary use for reboot/ or shut down(reboot -p)
 */
        //shut down button.
        //temporary use
        Button shut = (Button) view.findViewById(R.id.shutdown);
        shut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dwin dwin = Dwin.getInstance();
                dwin.execCommand("reboot");
            }
        });



        /**
         * temporary for screensaver
         */
        Button b = (Button) view.findViewById(R.id.effect);
        b.setText("breath");
        b.setTextSize(30);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),ScreenSaver.class);
                startActivity(i);
                Utility.still_time = 10000;
            }
        });
        View general_view = inflater.inflate(R.layout.setting_general,null);
        EditText edt = (EditText) general_view.findViewById(R.id.still_time);Log.d("xjj","nullpoint:"+ R.id.still_time+" edt"+edt);
        edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(v.getText().toString().isEmpty())
                    return false;
                Utility.still_time =(long)Integer.parseInt(v.getText().toString()) * 1000;
                Log.d("xjj", "gettext= "+ v.getText()+" still_time= "+Utility.still_time);
                return false;
            }
        });

//        initialize(view);
        Date date = new Date(System.currentTimeMillis());
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(mContext);
        TextView tv = (TextView) view.findViewById(R.id.tv);
        tv.setText("Time: " + dateFormat.format(date));

        return  view;
    }

    private void updatefrag (Fragment fragment){
//        try {
        FragmentManager fma = getFragmentManager();
        FragmentTransaction ftr = fma.beginTransaction();
//        ftr.replace(R.id.setframe, fragment);
        if(fragment.isAdded())
        {
            for(Fragment fg: setfrgs)
            {
                if(fg == fragment)
                    ftr.show(fg);
                else
                    ftr.hide(fg);
            }
        }

        else  ftr.add(R.id.setframe, fragment).show(fragment);
        ftr.commit();
    }


}
