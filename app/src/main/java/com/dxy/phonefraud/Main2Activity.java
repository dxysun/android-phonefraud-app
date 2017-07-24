package com.dxy.phonefraud;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.dxy.phonefraud.DataSource.CallLogObserver;
import com.dxy.phonefraud.DataSource.GetCall;
import com.dxy.phonefraud.DataSource.SmsInfo;
import com.dxy.phonefraud.DataSource.SmsObserver;
import com.dxy.phonefraud.DataSource.SmsReadDao;
import com.dxy.phonefraud.callrecord.CallRecord;
import com.dxy.phonefraud.callrecord.MyCallRecordReceiver;
import com.dxy.phonefraud.callrecord.PhoneListener;
import com.dxy.phonefraud.callrecord.PhoneReceive;
import com.dxy.phonefraud.entity.PhoneData;
import com.dxy.phonefraud.entity.SmsData;
import com.dxy.phonefraud.listen.RecordToText;
import com.dxy.phonefraud.listen.SMSReceiver;
import com.dxy.phonefraud.speechtotext.JsonParser;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.tuenti.smsradar.Sms;
import com.tuenti.smsradar.SmsListener;
import com.tuenti.smsradar.SmsRadar;


import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Main2Activity extends Activity implements View.OnClickListener {
    private ImageView iv_setting;
    private ImageButton phone_fraud;
    private ImageButton sms_fraud;

    private CallRecord callRecord;
    private Handler mHandler;
    private String smsbody;
    private String result;

    private Notification mNotification;
    private NotificationManager mNotificationManager;
    private PendingIntent smsResultIntent;
    private PendingIntent phoneResultIntent;

    private Dialog alertDialog;
    private static HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private String record_result;
    private String phone_result;
    private String call_time;
    private String record_number;
    private String record_path;
    private  static String[] PERMISSIONS = new String[]{Manifest.permission.READ_CALL_LOG,Manifest.permission.READ_CONTACTS,Manifest.permission.READ_SMS,Manifest.permission.WRITE_CALL_LOG,Manifest.permission.RECORD_AUDIO};
    private  static Toast toast = null;
    private  static Timer timer = null;
    private  static Timer time1 = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        iv_setting = (ImageView)findViewById(R.id.iv_right_image);
        phone_fraud = (ImageButton)findViewById(R.id.phoneButton);
        sms_fraud = (ImageButton)findViewById(R.id.smsButton);
        iv_setting.setOnClickListener(this);
        phone_fraud.setOnClickListener(this);
        sms_fraud.setOnClickListener(this);
        initPermission();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        /*
         * 取得PendingIntent， 并设置跳转到的Activity，
         */
        Intent intent = new Intent(this, SmsActivity.class);
        smsResultIntent = PendingIntent.getActivity(this, 1, intent, 0);

        Intent intentphone = new Intent(this, PhoneActivity.class);
        phoneResultIntent = PendingIntent.getActivity(this, 1, intentphone, 0);
        BaseApplication.setContext(Main2Activity.this);
        Thread t = new Thread(new Runnable(){
            @Override
            public void run() {
                BaseApplication.setNormalphonelist(Main2Activity.this);
                BaseApplication.setFraudphonelist();
                BaseApplication.setRecoredphonelist();
                BaseApplication.setNormalsmslist(Main2Activity.this);
                BaseApplication.setFraudsmslist();
            }
        });
        t.start();

        PhoneReceive.startPhoneListen(this, new PhoneListener() {
            @Override
            public void onIncomingCallEnded(Context context, String number, Date start, Date end,boolean isRecordStarted,String path,String result) {
                Log.i("ListenSmsPhone", "onIncomingCallEnded，号码" + number + "时间为： " + call_time);
                SharedPreferences pref = context.getSharedPreferences("set", context.MODE_PRIVATE);
                Boolean b = pref.getBoolean("is_record", true);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

                call_time = dateFormat.format(end);
                record_number = number;
                record_path = path;

                Log.i("ListenSmsPhone", "onIncomingCallEnded    result    " + result);
                if(isRecordStarted && b && number != null && path != null)
                {
                    //        path = "/storage/emulated/0/CallRecorder/CallRecorder.pcm";
                    Log.i("ListenSmsPhone", "开始识别录音 ");
                    RecordToText.getRecognizeResult(context, path, number, mRecognizerListener);
                }
                else
                {
                    PhoneData p = new PhoneData();
                    p.setPhonenumber(number);
                    p.setCalltime(call_time);
                    if(result != null)
                    {
                        p.setIsrecord(0);
                        if( !result.equals("fraud"))
                        {
                            p.setType(1);
                            p.setPhonename(result);
                            BaseApplication.addNormalPhone(-1, p, getApplicationContext());
                        }
                        else
                        {
                            p.setType(0);
                            BaseApplication.addFraudPhone(-1, p, getApplicationContext());
                        }
                    }
                }
            }

            @Override
            public void onIncomingCallAnswered(Context context, String number, Date start) {
                Log.i("ListenSmsPhone", "onIncomingCallAnswered，号码" + number );
            }

            @Override
            public void onIncomingCallReceived(Context context, String number, Date start) {
                Log.i("ListenSmsPhone", "onIncomingCallReceived，号码" + number );
            }
            @Override
            public void onMissedCall(Context context, String number, Date start,String result)
            {
                Log.i("ListenSmsPhone", "onMissedCall，号码" + number +"result  "+result);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                call_time = dateFormat.format(start);
                PhoneData p = new PhoneData();
                p.setPhonenumber(number);
                p.setCalltime(call_time);
                if(result != null)
                {
                    p.setIsrecord(0);
                    if( result.equals("fraud"))
                    {
                        p.setType(0);
                        BaseApplication.addFraudPhone(-1, p, getApplicationContext());
                    }
                    if(result.equals("notknown"))
                    {
                        p.setType(1);
                        BaseApplication.addNormalPhone(-1, p, getApplicationContext());
                    }
                }
            }
        });
        SmsRadar.initializeSmsRadarService(this, new SmsListener() {
            @Override
            public void onSmsSent(Sms sms) {
            }

            @Override
            public void onSmsReceived(Sms sms) {    //监听接收到短信
                SmsInfo smsinfo = SmsReadDao.getLastReceivedSmsInfo(Main2Activity.this);
                smsbody = sms.getMsg();
                Log.i("ListenSmsPhone", "smsbody    "+smsbody);
                try {
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            result = smspostHttp(smsbody);
                        }
                    });
                    t.start();
                    t.join();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("ListenSmsPhone", "after   smsbody");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                Date d = new Date(Long.parseLong(sms.getDate()));
                String date = dateFormat.format(d);

                SmsData sdata = new SmsData();
                sdata.setSmsid(smsinfo.getId());
                sdata.setSmstime(date);
                sdata.setSmscontent(sms.getMsg());
                String number = sms.getAddress();
                if(number.startsWith("+86"))
                {
                    number = number.substring(3);
                }

                sdata.setSmsnumber(number);
                if (result.equals("normal")) {
                    Log.i("ListenSmsPhone", "addNormalSms ");
                    String name = GetCall.queryNameFromContactsByNumber(getApplicationContext(), number);
                    if(name != null)
                    {
                        sdata.setSmsname(name);
                    }
                    BaseApplication.addNormalSms(-1,sdata, getApplicationContext());
                    //Toast.makeText(Main2Activity.this, "观察者 接收到正常短信", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(Main2Activity.this, "您接收到诈骗短信，请注意防范短信诈骗", Toast.LENGTH_LONG).show();
                    Log.i("ListenSmsPhone", "addFraudSms ");
                    BaseApplication.addFraudSms(-1, sdata, getApplicationContext());
                    String Ticker = "接收到诈骗短信";
                    String title = "短信诈骗";
                    String contentTitle = "您接收到诈骗短信，请注意防范短信诈骗";
                    int id = 0;
                    setNotification(Ticker, title, contentTitle, smsResultIntent, id);
                }
                //         showSmsToast(sms);
            }
        });

   //     SMSReceiver receiver = new SMSReceiver();
   //     registerReceiver(receiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
    //    mHandler = new Handler();

     //   SmsObserver smsContentObserver = new SmsObserver(mHandler, this);
     //   getContentResolver().registerContentObserver(Uri.parse("content://sms"), true, smsContentObserver);
 /*        callLogObserver = new CallLogObserver(mHandler, this);
        getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI, true, callLogObserver);//等价于【Uri.parse("content://call_log/calls")】
*/
    }
    private void initPermission() {
        int permission = ContextCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.READ_CALL_LOG);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            //需不需要解释的dialog
            if (shouldRequest()) return;
            //请求权限
            ActivityCompat.requestPermissions(Main2Activity.this,PERMISSIONS, 1);
        }
    }
    private boolean shouldRequest() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CALL_LOG)) {
            //显示一个对话框，给用户解释
            explainDialog();
            return true;
        }
        return false;
    }
    private void explainDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("应用需要获取您的权限,是否授权？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //请求权限
                        ActivityCompat.requestPermissions(Main2Activity.this, PERMISSIONS, 1);
                    }
                }).setNegativeButton("取消", null)
                .create().show();
    }
    private void showSmsToast(Sms sms) {
        Toast.makeText(this, sms.toString(), Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onDestroy() {
        Log.i("MyService", "mainactivity onDestroy ");
    //    callRecord.stopCallReceiver();
        PhoneReceive.stopPhoneListen(this);
        super.onDestroy();
    }
    //用Activity实现OnClickListener接口
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch(v.getId()){
            case R.id.iv_right_image:
                LitePal.getDatabase();
                Intent intent = new Intent(this,SetActivity.class);
                startActivity(intent);
                break;
            case R.id.phoneButton:
                Intent intent1 = new Intent(this,PhoneActivity.class);
                startActivity(intent1);
         //       overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.smsButton:
                Intent intent2 = new Intent(this, SmsActivity.class);
                startActivity(intent2);
           //     overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                break;
            default:
                break;
        }
        Log.i("用Activity实现", "点击事件");
    }
    public String smspostHttp(String msgBody){
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder fromBodyBuilder = new FormBody.Builder();
        RequestBody requestBody = fromBodyBuilder.add("sms", msgBody).build();
        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder.url("https://lucfzy.com/spark/sms/").post(requestBody).build();

        Call call = okHttpClient.newCall(request);

        String s = "nonetwork";
        Log.i("ListenSmsPhone", "start  request");
        try {
            Response response = call.execute();     //同步
            s = response.body().string();
            Log.i("ListenSmsPhone", "result  :" + s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }
    public void setNotification(String Ticker,String Title,String  ContentText,PendingIntent resultIntent,int id){
        Bitmap largeIcon = BitmapFactory.decodeResource(
                Main2Activity.this.getResources(), R.drawable.anitfraud);

        mNotification = new NotificationCompat.Builder(getBaseContext())
                // 设置大图标
                .setLargeIcon(largeIcon)
                        // 设置小图标
                .setSmallIcon(R.drawable.ic_launcher)
                        // 设置状态栏文本标题
                .setTicker(Ticker)
                        // 设置标题
                .setContentTitle(Title)
                        // 设置内容
                .setContentText(ContentText)
                        // 设置ContentIntent
                .setContentIntent(resultIntent)
                        // 设置Notification提示铃声为系统默认铃声
                .setSound(
                        RingtoneManager.getActualDefaultRingtoneUri(
                                getBaseContext(),
                                RingtoneManager.TYPE_NOTIFICATION))
                        // 点击Notification的时候使它自动消失
                .setAutoCancel(true).build();
        mNotificationManager.notify(id, mNotification);
    }
    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            //    showTip("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
         //   showTip(error.getPlainDescription(true));
            Toast.makeText(getApplicationContext(), "刚才的通话中您没有说话", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            //       showTip("结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            //         Log.d(TAG, results.getResultString());

            record_result = printResult(results);
            PhoneData pdata = new PhoneData();
            pdata.setRecordpath(record_path);
            pdata.setIsrecord(1);
            pdata.setPhonenumber(record_number);
            pdata.setCalltime(call_time);

            if (isLast) {
                // TODO 最后的结果
                Log.i("ListenSmsPhone", "识别结束 结果为 "+ record_result );
                try{
                    Thread t = new Thread(new Runnable(){
                        @Override
                        public void run() {
                            phone_result = phonepostHttp(record_result);
                        }
                    });
                    t.start();
                    t.join();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                if(phone_result.equals("normal"))
                {
                    //   BaseApplication.addNormalSms(sdata);
                    Log.i("ListenSmsPhone", "识别结束 结果为正常 ");
//                    Toast.makeText(Main2Activity.this, "观察者 接收到正常短信", Toast.LENGTH_LONG).show();
                    pdata.setType(1);
                    BaseApplication.addNormalPhone(-1,pdata,getApplicationContext());
                }
                else
                {
                    Log.i("ListenSmsPhone", "识别结束 结果为不正常 ");
               //     Toast.makeText(Main2Activity.this, "您刚刚接听的电话含有诈骗内容，请注意防范电话诈骗", Toast.LENGTH_LONG).show();
                    CustomTimeToast(getApplicationContext(),  "您刚刚接听的电话含有诈骗内容，请注意防范电话诈骗", 10 * 1000);
                    //     BaseApplication.addFraudSms(sdata);
                    String Ticker = "接收到诈骗电话";
                    String title = "电话诈骗";
                    String contentTitle = "您刚刚接听的电话含有诈骗内容，请注意防范电话诈骗";
                    int id = 1;
                    pdata.setType(0);
                    GetCall.DeleteCallByNumber(getApplicationContext(), pdata.getPhonenumber());
                    BaseApplication.addFraudPhone(-1,pdata,getApplicationContext());
                    setNotification(Ticker, title, contentTitle, phoneResultIntent, id);
                }
                Log.i("ListenSmsPhone", "添加录音列表 ");
                BaseApplication.addRecordPhone(-1,pdata,getApplicationContext());
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            //    showTip("当前正在说话，音量大小：" + volume);
            ///     Log.d(TAG, "返回音频数据："+data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };
    private String printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }
        Log.i("result", resultBuffer.toString());
        return resultBuffer.toString();
        //      record_text.setText(resultBuffer.toString());
        //    mResultText.setSelection(mResultText.length());
    }
    public String phonepostHttp(String phone){
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder fromBodyBuilder = new FormBody.Builder();
        RequestBody requestBody = fromBodyBuilder.add("phonecontent", phone).build();
        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder.url("https://lucfzy.com/spark/phonecontent/").post(requestBody).build();

        Call call = okHttpClient.newCall(request);

        String s = "nonetwork";
        Log.i("ListenSmsPhone", "start  request");
        try {
            Response response = call.execute();     //同步
            s = response.body().string();
            Log.i("ListenSmsPhone", "result  :" + s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    public  void CustomTimeToast(Context context,String text,int t) {
        toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        }, 0, 10);             // 3000表示点击按钮之后，Toast延迟3000ms后显示
        time1 = new Timer();
        time1.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, t);            // 5000表示Toast显示时间为5秒
    }
}
