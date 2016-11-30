package grean.custom.component;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.grean.myapplication.R.mipmap;

/**
 * Created by Administrator on 2016/10/10.
 */
public class GreanToast   {
    private  Toast toast;
    private  Context mContext;
    public GreanToast(Context context) {

         this.mContext = context;
         ImageView imageView= new ImageView(context);
         imageView.setImageResource(mipmap.logo);
         toast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);

         LinearLayout toastView = (LinearLayout) toast.getView();
         toastView.setOrientation(LinearLayout.HORIZONTAL);
         toastView.setBackgroundColor(Color.parseColor("#99CCFF"));
//        toastView.setBackgroundResource(R.drawable.progressbar); // style can be applied
         LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(90,30);
         params.setMargins(0,0,10,0);
         toastView.addView(imageView, 0, params);  //put image in child position 0

         TextView tv = (TextView) toastView.getChildAt(1);  //child position 1
         tv.setShadowLayer(0,0,0,0);
         tv.setHighlightColor(Color.WHITE);
         tv.setTextSize(25);

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.parseColor("#99CCFF")); // Changes this drawbale to use a single color instead of a gradient
        gd.setCornerRadius(5);
        gd.setStroke(0,0);

        toastView.setBackground(gd);

    }
    public void show(String string){
        //toast = Toast.makeText(mContext, string, Toast.LENGTH_SHORT);
        toast.setText(string);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
