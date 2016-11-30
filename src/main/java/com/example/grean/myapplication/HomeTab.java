package com.example.grean.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultYAxisValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/9/12.
 */
public class HomeTab extends BaseFragment implements View.OnClickListener, TextView.OnEditorActionListener {


    private Handler mHandler = new Handler();
    private int mProgressStatus = 0;
    private ProgressBar mProgressBar ;
    private EditText mEditText;

    private LineChart mLineChart;
    int DrawNumber =6;
    int cc =0;
//    Integer cc = 0;
    List<Float> resultbuff1 = new ArrayList<>();
    Random random = new Random();



    Float [] resultbuff = new Float[30];// ={0.5f, 0.8f, 1.1f, 2.0f,3f, 0.5f, 0.8f, 1.1f, 2.0f,3f } ;


    String [] timebuff = new String[30];// ={"2016-9-18-15:30","2016-9-18-15:31","2016-9-18-15:32","2016-9-18-15:33","2016-9-18-15:30","2016-9-18-15:31","2016-9-18-15:32","2016-9-18-15:33","2016-9-18-15:30","2016-9-18-15:31"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.home, container, false);

        mEditText =(EditText) view.findViewById(R.id.chart_num);
        mEditText.setOnEditorActionListener(this);

        for(int i =0; i<20; i++)
        {
            resultbuff[i] =  random.nextFloat()*3;
            Log.d("random", ""+resultbuff[i]);
            timebuff[i] = getRandomString(8);
            Log.d("random", ""+timebuff[i]);
        }



//        Iterator it= resultbuff1.iterator();
//        while (it.hasNext()) Log.d("result1 ", ""+Integer.parseInt(it.next().toString()));

        // linechart
        mLineChart = (LineChart) view.findViewById(R.id.spread_line_chart);


        LineData mLineData = getLineData(DrawNumber, resultbuff,timebuff);

        showChart(mLineChart, mLineData); // better try catch

//        setProgressBar(view);

        Button cln =(Button) view.findViewById(R.id.clean);
        cln.setOnClickListener(this);
        Button intl =(Button) view.findViewById(R.id.initest);
        intl.setOnClickListener(this);

        return view;
    }


    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }


    private void setProgressBar(View view){
        mProgressBar = (ProgressBar) view.findViewById(R.id.testprogess);
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setMax(1000);
//        mProgressBar.sette
         new Thread(new Runnable() {
            public void run() {

                while (mProgressStatus < mProgressBar.getMax()) {
//                    mProgressStatus= doWork();
                        mProgressStatus += 10;
                    // Update the progress bar
                    try {
                        mProgressBar.setProgress(mProgressStatus);
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mHandler.post(new Runnable() {
                    public void run() {
                        mProgressBar.setVisibility(View.GONE);
                        mProgressStatus=0;
                    }
                });
            }
        }).start();
    }





    private LineData getLineData (Integer count, Float[] results, String[] times) {
        ArrayList<String> xValues = new ArrayList<String>();

        //结束取数据
        Float value;
        ArrayList<Entry> yValues = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {
            // x轴显示的数据，这里默认使用数字下标显示
            xValues.add(times[i]);
        }
        // y轴的数据
        for (int i = 0; i < count; i++) {
            value =results[i];
            yValues.add(new Entry(value, i));
        }

        // create a dataset and give it a type
        // y轴的数据集合
        LineDataSet lineDataSet = new LineDataSet(yValues, "浓度走势图   mg/L" /*显示在比例图上*/);
        // mLineDataSet.setFillAlpha(110);
        //mLineDataSet.setFillColor(Color.RED);

        //用y轴的集合来设置参数
        lineDataSet.setLineWidth(2f); // 线宽
        lineDataSet.setCircleSize(3f);// 显示的圆形大小
        lineDataSet.setColor(Color.parseColor("#007cc2"));// 显示颜色
        lineDataSet.setCircleColor(Color.parseColor("#007cc2"));// 圆形的颜色
        lineDataSet.setHighLightColor(Color.YELLOW); // 高亮的线的颜色

        lineDataSet.setDrawCircles(true);//图标上的数据点不用小圆圈表示
        lineDataSet.setDrawCubic(true);//设置允许曲线平滑
        lineDataSet.setCubicIntensity(0.1f);//设置平滑度
        lineDataSet.setDrawFilled(true);//设置允许     填充曲线以下区域
        lineDataSet.setFillColor(Color.parseColor("#007cc2"));//设置填充的颜色

        lineDataSet.setValueTextSize(20f);  // dot font size
        lineDataSet.setValueTextColor(Color.BLACK);

        ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();
        lineDataSets.add(lineDataSet); // add the datasets

        // create a data object with the datasets
        LineData lineData = new LineData(xValues, lineDataSets);

        return lineData;
    }


    private void showChart(LineChart lineChart, LineData lineData) {
        lineChart.setDrawBorders(false);  //是否在折线图上添加边框

        // no description text
        lineChart.setDescription("");// chart description
        // 如果没有数据的时候，会显示这个，类似listview的emtpyview
        lineChart.setNoDataTextDescription("No data found.");

        // enable / disable grid background
//        lineChart.setDrawGridBackground(false); // 是否显示表格颜色
//        lineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度

        // enable touch gestures
        lineChart.setTouchEnabled(true); // 设置是否可以触摸

        // enable scaling and dragging
        lineChart.setDragEnabled(true);// 是否可以拖拽
        lineChart.setScaleEnabled(false);// 是否可以缩放

        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(true);//


        lineChart.setBackgroundColor(Color.parseColor("#ffffee"));// 设置背景

        // add data
        lineChart.setData(lineData); // 设置数据

        // get the legend (only possible after setting data)
        Legend mLegend = lineChart.getLegend(); // 设置比例图标示，就是那个一组y的value的

        // modify the legend ...
         mLegend.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
        mLegend.setForm(Legend.LegendForm.LINE);// 样式
        mLegend.setFormSize(20f);// chart
        mLegend.setTextColor(Color.BLACK);// 颜色
        mLegend.setTextSize(20f);

//      mLegend.setTypeface(mTf);// 字体

        YAxis axisLeft = lineChart.getAxisLeft(); //y轴左边标示
        YAxis axisRight = lineChart.getAxisRight(); //y轴右边标示
        YAxisValueFormatter f=new DefaultYAxisValueFormatter(2); // 小数精度
        axisLeft.setTextSize(12f);
        axisRight.setTextSize(12f);
        axisLeft.setValueFormatter(f);
        axisRight.setValueFormatter(f);

//        lineChart.animateX(1000); // animation does not work now. cuz need oncreate, which has been replaced by hide&show.
    }



    @Override
    public void onClick(View v) {
        int buttonid = v.getId();
        switch (buttonid) {

            case R.id.initest:

                mProgressStatus = 80;
                setProgressBar(this.getView());
//                getActivity().finish();
                break;
            case R.id.clean:
//                mProgressStatus = 500;
////                setProgressBar(this.getView());
//                 OperateTab op = new OperateTab();
//                OperateTab.settx x= op.new settx();
//              synchronized (x){x.execute("sss");}
////                getFragmentManager().findFragmentById(R.id.operatetab).settx();
                break;

        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {



        if(mEditText.getText().toString().isEmpty())
            return false;

        cc = Integer.parseInt(mEditText.getText().toString());
        Log.d("xjj","count= "+cc);

        if(cc > resultbuff.length)
            return false;

            LineData mLineData = getLineData(cc, resultbuff,timebuff);
            showChart(mLineChart, mLineData); // better try catch
            return  false;

    }
}
