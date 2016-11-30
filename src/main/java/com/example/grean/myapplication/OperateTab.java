package com.example.grean.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import grean.custom.component.GreanToast;

/**
 * Created by Administrator on 2016/9/12.
 */
public class OperateTab extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener,View.OnFocusChangeListener {

    public static String mStatus;
    private static final int UPDATE_TIME = 0, UPDATE_DATE = 1;
    private Context mContext;
    private String mDate ,mNextDate;
    private int sum_of_btns = 8, sum_of_tvs = 6;
    private int mGapH, mGapM, mScdH, mScdM;
//    private boolean dlg_cancel = false;
    private Calendar c ;
    private SimpleDateFormat timeformat = new SimpleDateFormat("yyyy/MM/dd/HH:mm");
    private TimePickerDialog gap_tpd, scd_tpd;
    private static TimePickerDialog tpd;
    private DatePickerDialog dpd;
    private Switch switch_mode, switch_loop, switch_schedule;
    private EditText edit_low,edit_high;

    //just to simplify findviewbyid
    private Button[] btns = new Button[sum_of_btns];
    private Integer[] btId ={ R.id.btn_start_test, R.id.btn_both_cal, R.id.btn_low_cal, R.id.btn_high_cal, R.id.btn_standard_test, R.id.btn_initialize, R.id.btn_clean, R.id.btn_stop };

    //just to simplify findviewbyid
    private TextView[] tvs = new TextView[sum_of_tvs];
    private Integer[] tvId ={ R.id.text_status, R.id.text_next_time, R.id.text_gap_time, R.id.text_scd_time, R.id.text_low, R.id.text_high };







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.operate, container, false);
        mContext = getActivity();


        //if testing now, set all buttons disable
        //before initialView


        InitialView(view);

        return view;
    }

    /**
     * To initialize the view of operate tab.
     * @param view the view to be initialized.
     */
    private void InitialView(View view) {


        //bind textviews with id
        for(int i = 0; i<tvs.length; i++){
            tvs[i] = (TextView) view.findViewById(tvId[i]);
            tvs[i].setTag("tv"+i);
            //to set tv2&tv3 onclicklistener
            if(i == 2 || i == 3)
                tvs[i].setOnClickListener(this);
            Log.d("xjj", "tvs["+i+"]" +"ID:"+ tvId[i]+" TAG:"+tvs[i].getTag());
        }
        //bind buttons with id & listener
        for(int i = 0; i<btns.length; i++){
            btns[i] = (Button) view.findViewById(btId[i]);
            btns[i].setTag("btn"+i);
            btns[i].setOnClickListener(this);
            Log.d("xjj", "btns["+i+"] ID:"+ btId[i]+" TAG:"+btns[i].getTag());
        }
        //bind switchs with id & onclicklistener // and initialize with defaults
        switch_mode = (Switch) view.findViewById(R.id.switch_mode);
        switch_mode.setOnCheckedChangeListener(this);
        switch_mode.setTag("switch_mode");
        switch_mode.setChecked(getResources().getBoolean(R.bool.if_mode_online));
        switch_loop = (Switch) view.findViewById(R.id.switch_loop);
        switch_loop.setOnCheckedChangeListener(this);
        switch_loop.setTag("switch_loop");
        switch_loop.setChecked(getResources().getBoolean(R.bool.if_loop));
        switch_schedule =  (Switch) view.findViewById(R.id.switch_schedule);
        switch_schedule.setOnCheckedChangeListener(this);
        switch_schedule.setTag("switch_schedule");
        switch_schedule.setChecked(getResources().getBoolean(R.bool.if_schedule));


        //initialize status of machine
        mStatus = getResources().getString(R.string.status_1);
        tvs[0].setText(String.format(getResources().getString(R.string.status),mStatus)); // tv_status

        //initialize the next test time.
        mDate = "--";
        String str_mdate = String.format(getResources().getString(R.string.next_time),mDate);
        tvs[1].setText(str_mdate);//tv_next_time

        //initialize de gap time
        mGapH = getResources().getInteger(R.integer.gap_hour);
        mGapM = getResources().getInteger(R.integer.gap_min);
        tvs[2].setText(String.format(getResources().getString(R.string.gap_time),mGapH,mGapM));//tv_gap_time

        //initialize the scheduled time
        tvs[3].setText(mDate);
        //initialize msch&m
        c = Calendar.getInstance();
        mScdH = c.get(Calendar.HOUR_OF_DAY);
        mScdM = c.get(Calendar.MINUTE);
        //InitialPickerDialog();
        dpd = new DatePickerDialog(mContext, dateSetListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        edit_low = (EditText) view.findViewById(R.id.edit_low);
        edit_high = (EditText) view.findViewById(R.id.edit_high);
        edit_low.setOnFocusChangeListener(this);
        edit_high.setOnFocusChangeListener(this);



    }




    /**
     * OnTimeSetListener of gap_time & scheduled_time
     * OnDateSetListener of date
     * used by ontimeset listener
     */
    private TimePickerDialog.OnTimeSetListener gap_listener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            if (Utility.dlg_cancel) return;
            mGapH = hourOfDay;
            mGapM = minute;
            Log.d("xjj", "settime dialog:" + this);
            //set tvs[2]/gap_time_tv
            tvs[2].setText(String.format(getResources().getString(R.string.gap_time), mGapH, mGapM));
        }
    };

    private TimePickerDialog.OnTimeSetListener scd_listener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            if (Utility.dlg_cancel) return;
            if(view.isShown()) {
                mScdH = hourOfDay;
                mScdM = minute;
                //set tvs[3]/scheduled time
                tvs[3].setText(mDate + "/" + mScdH + ":" + String.format("%02d", mScdM));
                //start scheduled
                scheduled_test();
                Log.d("xjj", "why twice? after");
                Log.d("xjj","hour:"+hourOfDay+" min:"+minute);
            }
        }
    };

    private DatePickerDialog.OnDateSetListener dateSetListener =  new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            if(Utility.dlg_cancel) return;
            mDate = date_int2str(year,monthOfYear+1,dayOfMonth);
        }
    };



    /**
     * the confirm dialog before execute
     * @param str the notification tips
     * @param listener the listener after press yes
     * @return returns the dialog
     */
    private AlertDialog.Builder confirmDialog(String str, DialogInterface.OnClickListener listener){
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle("提示")
                .setMessage(str)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", listener);
        return dialog;
    }


    /** start
     * confirm dialog for immediate test button.
     */

    //confirm button for [immediate] [loop] test
    private DialogInterface.OnClickListener confirm_listener_im_loop = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //start im & loop test
            new GreanToast(mContext).show(getString(R.string.toast_im_loop));
            //cancel next task.
            // do this.
            //calculate next time
            Calendar newc = Calendar.getInstance();
            SimpleDateFormat timeformat = new SimpleDateFormat("yyyy/MM/dd/HH:mm");
            newc.add(Calendar.HOUR_OF_DAY, mGapH);
            newc.add(Calendar.MINUTE, mGapM);
//            it's going to be Wed Oct 26 15:18:00 格林尼治标准时间+0800 2016
//            Date a = new Date(timeformat.format(newc.getTime()));
//            Log.d("xjj",""+a);
            Date next_loop = new Date(newc.getTimeInMillis());
            mNextDate = timeformat.format(next_loop); Log.d
                    ("xjj", "mNextDate:" + mNextDate);
            tvs[1].setText(String.format(getResources().getString(R.string.next_time),mNextDate));
        }
    };
    //confirm button for [immediate] [single] test
    private DialogInterface.OnClickListener confirm_listener_im_single = new DatePickerDialog.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //start im & single test
            new GreanToast(mContext).show(getString(R.string.toast_im_single));

            //cancel any next test
            tvs[1].setText(String.format(getResources().getString(R.string.next_time),"--"));
        }
    };
    // confirm dialog tips
    private void immediate_test(){
        // switch loop?
        if(switch_loop.isChecked()){
            //do immediate & loop
            confirmDialog("立即开始循环测量？ (测量间隔为"+mGapH+"小时"+mGapM+"分)", confirm_listener_im_loop).show();
        }
        else {
            //do immediate & single
            confirmDialog("立即开始单次测量？", confirm_listener_im_single).show();
        }
    }
    /** end -- confirm dialog for immediate test button.*/



    /** start
     * confirm dialog for scheduled test button.
     */
    //confirm listener of [schedule] [single] test
    private DialogInterface.OnClickListener confirm_listener_scd_single = new DatePickerDialog.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            //start im & single test
            new GreanToast(mContext).show(getString(R.string.toast_scd_single));

            //cancel any next test

            mNextDate = mDate + "/" + String.format("%02d",mScdH) + ":" + String.format("%02d",mScdM);
            tvs[1].setText(String.format(getString(R.string.next_time),mNextDate));
        }
    };
    //confirm listener of [schedule] [loop] test
    private DialogInterface.OnClickListener confirm_listener_scd_loop = new DatePickerDialog.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            //start im & single test
            new GreanToast(mContext).show(getString(R.string.toast_scd_loop));

            //cancel any next test
            mNextDate = mDate + "/" + String.format("%02d",mScdH) + ":" + String.format("%02d",mScdM);

            tvs[1].setText(String.format(getString(R.string.next_time),mNextDate));
        }
    };

    // the confirm dialog tips
    private void scheduled_test(){
        //exam if null when first click yes
        if(tvs[3].getText().equals("--")) {
            new GreanToast(mContext).show(getString(R.string.tips_null_time));
            return;
        }

        //if set a time before
        c = Calendar.getInstance();
        Date scd = new Date(c.getTimeInMillis());
        Date now = new Date(c.getTimeInMillis());

        try {
            scd = timeformat.parse(mDate+"/"+mScdH+":"+mScdM);
            Log.d("xjj", "try scd:"+scd);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("xjj", "try false");
        }
        Log.d("xjj", "now:"+now.getTime()+" scd:"+scd.getTime());
        if(scd.getTime() < now.getTime()){
            new GreanToast(mContext).show(getString(R.string.tips_outdate));
            tvs[1].setText(String.format(getResources().getString(R.string.next_time),"--"));
            tvs[3].setText("--");
            return;
        }

        //switch loop?
        if(switch_loop.isChecked()){
            //do schedule & loop
            confirmDialog(String.format(getString(R.string.confirm_scd_loop),mDate,mScdH,mScdM,mGapH,mGapM), confirm_listener_scd_loop).show();
        }
        else {
            //do schedule & single
            confirmDialog(String.format(getString(R.string.confirm_scd_single),mDate,mScdH,mScdM), confirm_listener_scd_single).show();
        }
    }
    /* confirm dialog for scheduled test button. end */
/**end -- confirm dialog for scheduled test button */

    /**
     * help to transfer multiple int to string
     * @param ints single int or ints
     * @return a string in format as 11/22/33
     */
    private String date_int2str(int ... ints){
        String str = "";
        for(int i:ints){
            str += Integer.toString(i)+"/";
        }
        str = str.substring(0,str.length()-1);
        return str;
    }


    /**
     * switch ontouch listener.
     * @param buttonView the button chosen
     * @param isChecked been checked or unchecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        String tag = (String) buttonView.getTag();
        switch ( tag ) {
            case "switch_mode":
                break;
            case "switch_loop":
                if (isChecked) {
                    tvs[2].setTextColor(Color.WHITE);
                    tvs[2].setBackgroundColor(Color.parseColor("#009cc2"));
                } else {
                //cancel what has been issued to execute.
                    tvs[2].setTextColor(Color.parseColor("#007cc2"));
                    tvs[2].setBackgroundColor(Color.parseColor("#cccccc"));
                    tvs[1].setText(String.format(getResources().getString(R.string.next_time),"--"));
                    //cancel the loop mission
                }
                break;
            case "switch_schedule":
                if (isChecked) {
                    tvs[3].setTextColor(Color.WHITE);
                    tvs[3].setBackgroundColor(Color.parseColor("#009cc2"));
                } else {
                    //cancel what has been issued to execute.
                    tvs[3].setTextColor(Color.parseColor("#007cc2"));
                    tvs[3].setBackgroundColor(Color.parseColor("#cccccc"));
                    tvs[3].setText("--");
                    tvs[1].setText(String.format(getResources().getString(R.string.next_time),"--"));
                    //canceel the next time mission

                }
                break;

            default:
                break;
        }

    }


    /**
     * onclick listener of operate tab.
     * @param v the one being clicked.
     */
    @Override
    public void onClick(View v) {
        String tag = (String)v.getTag();
        Log.d("xjj","v.tag:"+tag);
        switch (tag){
            //gap time
            case "tv2":
                if(Utility.isFastClick() || !switch_loop.isChecked()) return;
                Utility.AddCancelBtn(new TimePickerDialog(mContext,gap_listener,mGapH,mGapM,true)).show();
                break;
            //schedule time
            case "tv3":
                if(Utility.isFastClick() || !switch_schedule.isChecked()) return;  //add schedule text control by switch_schedule
                Utility.AddCancelBtn(new TimePickerDialog(mContext,scd_listener,mScdH,mScdM,true)).show();
                dpd.setCanceledOnTouchOutside(false);
                dpd.show();
                break;




            //immediate test
            case "btn0":
                if(switch_schedule.isChecked())
                    scheduled_test();
                else
                    immediate_test();
                break;

            //calibrate
            case "btn1":
                //high & low cal
                break;
            //low cal
            case "btn2":
                break;
            //high cal
            case "btn3":
                break;
            //standard test
            case "btn4":
                break;
            //initializ
            case "btn5":
                break;
            //clean
            case "btn6":
                break;
            //stop
            case "btn7":
                break;
            default:
                break;

        }

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


/*
private SQLiteDB sqLiteDB;
private Context mContext;
TextView tx;
String tmpname;
Integer tmpid;
private ProgressBar pb;
private int progressbarStatus;
private Button showbt;
private Button shows;
private DatePicker datePicker;
private TimePicker timePicker;
private TimePicker timegap;
*/
/*
Button createbt = (Button) view.findViewById(R.id.db_button);
createbt.setOnClickListener(this);
Button insertbt = (Button) view.findViewById(R.id.insert_button);
insertbt.setOnClickListener(this);
showbt = (Button) view.findViewById(R.id.show);
showbt.setTag("off");
showbt.setOnClickListener(this);


shows = (Button) view.findViewById(R.id.shows);
shows.setOnClickListener(this);
tx = (TextView) view.findViewById(R.id.txt);

pb = (ProgressBar) getActivity().findViewById(R.id.testprogess);
if (pb == null) Log.d("xjj", "pb null");


sqLiteDB = new SQLiteDB(mContext, "sqldata.db", null, 1); //version 1

SharedPreferences pref = getActivity().getSharedPreferences("switch_status", Context.MODE_PRIVATE);
SharedPreferences.Editor editor = pref.edit();
editor.putBoolean("hey", true);//
editor.commit();
Log.d("xjj", "sprf: hey " + pref.getBoolean("hey", false));
Log.d("xjj", "sprf: heyy " + pref.getBoolean("heyy", false));
*/

/*
//measure the screen width and height
Display display = getActivity().getWindowManager().getDefaultDisplay();
DisplayMetrics outMetrics = new DisplayMetrics ();
display.getMetrics(outMetrics);
float density  = getResources().getDisplayMetrics().density;
float dpHeight = outMetrics.heightPixels / density;
float dpWidth  = outMetrics.widthPixels / density;
Log.d("xjj",  "dp width="+ dpWidth+" height=`"+dpHeight);
*/


//        return view;
//    }


//    private void customSwitch(String textleft,String textright) {
//
//         RelativeLayout.LayoutParams params_left;
//         RelativeLayout.LayoutParams params_right;
//
//       final TextView tv = (TextView) layout.findViewById(R.id.slidetext);
//
//        tv.setText(textoff);
//        int postion = 0;
//
//        params_left = new RelativeLayout.LayoutParams(60,60);
//        params_right = new RelativeLayout.LayoutParams(60,60);
//        params_left.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//        params_right.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//
//        tx.setLayoutParams(params_left);
//
//        tx.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(postion==0) {
//                    tv.setText(texton);
//                    tv.setLayoutParams(params_right);
//                    postion = 1;
//                    Log.d("xjj","switch status: "+postion);
//                }
//                else {
//                    tv.setText(textoff);
//                    tv.setLayoutParams(params_left);
//                    postion = 0;
//                }
//            }
//        });
//    }

//    private void gaptime(){
//        timegap.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
//        timegap.setIs24HourView(true);
//    }
//    private void date_time_init() {
//        Calendar c = Calendar.getInstance();
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH);
//        int day = c.get(Calendar.DAY_OF_MONTH);
//        int hour = c.get(Calendar.HOUR);
//        int minute = c.get(Calendar.MINUTE);
//
//        datePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
//        timePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
//        // 初始化DatePicker组件，初始化时指定监听器
//        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener()
//        {
//
//            @Override
//            public void onDateChanged(DatePicker arg0, int year
//                    , int month, int day)
//            {
////                ChooseDate.this.year = year;
////                ChooseDate.this.month = month;
////                ChooseDate.this.day = day;
////                // 显示当前日期、时间
////                showDate(year, month, day, hour, minute);
//                Toast.makeText(mContext,"您选择的日期："+year+"年  "
//                        +month+"月  "+day+"日", Toast.LENGTH_SHORT).show();
//            }
//        });
//        // 为TimePicker指定监听器
//        timePicker.setIs24HourView(true);
//        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener()
//        {
//
//            @Override
//            public void onTimeChanged(TimePicker view
//                    , int hourOfDay, int minute)
//            {
////                ChooseDate.this.hour = hourOfDay;
////                ChooseDate.this.minute = minute;
////                // 显示当前日期、时间
////                showDate(year, month, day, hour, minute);
//                Toast.makeText(mContext,"您选择的时间："+hourOfDay+"时  "
//                        +minute+"分", Toast.LENGTH_SHORT).show();
////
//            }
//        });
//
////        if(customSwitch.getSwitchStatus()){
////            datePicker.setEnabled(false);
////            timePicker.setEnabled(false);
////        }
//    }
//
//    public  class settx extends AsyncTask<String, Integer, Drawable>
//    {
//
//        @Override
//        protected void onPreExecute() {
//            Log.d("xjj","preExecute");
//
//            tx.setText("start");
//            progressbarStatus = 0;
//            pb.setVisibility(View.VISIBLE);
//
//
//        }
//
//        @Override
//        protected Drawable doInBackground(String... params) {
//            try {
//
//                while (progressbarStatus < 100) {
//                    progressbarStatus += 1;
//                    publishProgress(progressbarStatus);
//                    Thread.sleep(100);
//                }
//                return Drawable.createFromStream(new URL(params[0]).openStream(), "image.jpg");
//            } catch (IOException e) {
//                Log.d("xjj","can not get drawble");
//                return null;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//                Log.d("xjj","interrupted");
//                return null;
//            }
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            pb.setProgress(values[0]);
//        }
//
//        @Override
//        protected void onPostExecute(Drawable bitmap) {
//            Toast.makeText(mContext,"Start",Toast.LENGTH_SHORT);
//            tx.setText(tmpid+" "+tmpname);
//            tx.setBackground(bitmap);
//            pb.setVisibility(View.GONE);
//            showbt.setTag("off");
//        }
//
//        @Override
//        protected void onCancelled() {
//            tx.setText("cancelled");
//            progressbarStatus=0;
//            pb.setVisibility(View.GONE);
//            showbt.setTag("off");
//
//        }
//    }
//
//    public synchronized void getdata(){
//        SQLiteDatabase db = sqLiteDB.getReadableDatabase();
////        Cursor cursor = db.query("data",);
//        Cursor cursor1 = db.rawQuery("select * from data where id >5 order by id desc", new String[]{});
//        List<Integer> ids = new ArrayList<>();
//        List<String> names = new ArrayList<>();
//
//        final Handler handler = new Handler();
//
//        while(cursor1.moveToNext())
//        {
//            ids.add(cursor1.getInt(0));
//            names.add(cursor1.getString(1));
//            tmpname = cursor1.getString(1);
//            tmpid = cursor1.getInt(0);
//
//           // final String finalNewdata = newdata;
////            tx.postDelayed(new Runnable() {
////                @Override
////                public void run() {
////                        tx.setText(tmpid+" "+tmpname);
////                    try {
////                        tx.setBackground(Drawable.createFromStream(new URL("http://example.com/image.png").openStream(), "image.jpg"));
////                    } catch (IOException e) {
////                        e.printStackTrace();
////                    }
////                }
////            }, 0);
//
//        }
//
//        Iterator<Integer> iterator = ids.iterator();
//        while (iterator.hasNext() ) Log.d("xjj","iterator: "+ iterator.next());
//
//        for (String name : names) {
//            Log.d("xjj ","foreach: "+ name);
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        Integer id = v.getId();
//        switch (id){
//            case R.id.db_button:
//                sqLiteDB.getWritableDatabase();
//                break;
//            case R.id.insert_button:
//
//                SQLiteDatabase db = sqLiteDB.getWritableDatabase();
//                ContentValues values = new ContentValues();
//                values.put("name", "firstone");
//                db.insert("data" , null, values);
//                values.clear();
////                Toast.makeText(mContext,"hey insert",Toast.LENGTH_SHORT).show();
//
//                values.put("name", "secondone");
//                db.insert("data", null, values);
//                break;
//            case R.id.show:
//                if(v.getTag().equals("off")) {
//                    mtask = new settx();
//                    getdata();
//                    mtask.execute("http://example.com/image.png");
//                    v.setTag("on");
//                }
//                else {
//                    mtask.cancel(true);
//
//                }
//
//                break;
//            case R.id.shows:
////                mtask = new settx();
////                mtask.execute("adfdfa");
//                new settx().execute("adf"); //another new task. press this then press show  it wont cancell this.
//
//        }
//    }
//}
