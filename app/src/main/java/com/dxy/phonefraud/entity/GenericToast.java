package com.dxy.phonefraud.entity;

/**
 * Created by dongx on 2017/4/11.
 */
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import java.util.Timer;
import java.util.TimerTask;

public class GenericToast{
    private static final String TAG = "GenericToast";

    private static final int TOAST_TEXTSIZE = 20;

    /** {@link Toast#LENGTH_SHORT} default time is 3500ms */
    private static final int LENGTH_SHORT_TIME = 2000;

    private static Context mContext = null;

    private static Toast mToast = null;
    private static TextView mTextView = null;
    private static int mDuration = 0;
    private static CharSequence mText = null;

    private Handler mHandler = new Handler();
    private static Toast toast = null;
    private static Timer timer = null;
    private GenericToast(Context context) {
        mContext = context;
    }
    public static void showToast(final Activity activity, final String word, final long time){
        activity.runOnUiThread(new Runnable() {
            public void run() {
                final Toast toast = Toast.makeText(activity, word, Toast.LENGTH_LONG);
                toast.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        toast.cancel();
                    }
                }, time);
            }
        });
    }

    public static void CustomTimeToast(Context context,String text,int t) {
         toast = Toast.makeText(context,text, Toast.LENGTH_LONG);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        }, 0, 10);// 3000表示点击按钮之后，Toast延迟3000ms后显示
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, t);// 5000表示Toast显示时间为5秒

    }
    public static void CencelToast() {
                toast.cancel();
                timer.cancel();
    }
    public static GenericToast makeText(Context context, CharSequence text, int duration){
        GenericToast instance = new GenericToast(context);
        mContext = context;
        mDuration = duration;
        mText = text;
        return instance;
    }

    private static void getToast(Context context, CharSequence text){
        mToast = Toast.makeText(context, null, Toast.LENGTH_LONG);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastView = (LinearLayout)mToast.getView();

        // Get the screen size with unit pixels.
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        mTextView = new TextView(context);
        LayoutParams vlp = new LayoutParams(outMetrics.widthPixels,
                outMetrics.heightPixels);
        vlp.setMargins(0, 0, 0, 0);
        mTextView.setLayoutParams(vlp);
        mTextView.setTextSize(TOAST_TEXTSIZE);
        mTextView.setText(text);
        mTextView.setGravity(Gravity.CENTER);
        toastView.addView(mTextView);
    }

    /**
     * Before call this method, you should call {@link makeText}.
     *
     * @return Toast display duration.
     */
    public int getDuration(){
        return mDuration;
    }

    public void show(){
        Log.d(TAG, "Show custom toast");
        mHandler.post(showRunnable);
    }

    public void hide(){
        Log.d(TAG, "Hide custom toast");
        mDuration = 0;
        if(mToast != null){
            mToast.cancel();
        }
    }

    private Runnable showRunnable = new Runnable(){
        @Override
        public void run() {
            if(mToast != null){
                mTextView.setText(mText);
            }else{
                getToast(mContext, mText);
            }
            if(mDuration != 0){
                mToast.show();
            }else{
                Log.d(TAG, "Hide custom toast in runnable");
                hide();
                return;
            }

            if(mDuration > LENGTH_SHORT_TIME){
                mHandler.postDelayed(showRunnable, LENGTH_SHORT_TIME);
                mDuration -= LENGTH_SHORT_TIME;
            }else{
                mHandler.postDelayed(showRunnable, mDuration);
                mDuration = 0;
            }
        }
    };
}
