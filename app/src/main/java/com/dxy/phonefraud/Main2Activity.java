package com.dxy.phonefraud;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.CallLog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.dxy.phonefraud.DataSource.CallLogObserver;
import com.dxy.phonefraud.DataSource.SmsObserver;
import com.dxy.phonefraud.callrecord.CallRecord;
import com.dxy.phonefraud.callrecord.MyCallRecordReceiver;
import com.dxy.phonefraud.entity.SmsData;
import com.dxy.phonefraud.greendao.DaoSession;
import com.dxy.phonefraud.greendao.FraudPhone;
import com.dxy.phonefraud.greendao.FraudPhoneDao;
import com.dxy.phonefraud.greendao.FraudSms;
import com.dxy.phonefraud.greendao.FraudSmsDao;
import com.dxy.phonefraud.listen.SMSReceiver;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechRecognizer;
import com.tuenti.smsradar.Sms;
import com.tuenti.smsradar.SmsListener;
import com.tuenti.smsradar.SmsRadar;

import org.litepal.LitePal;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    private DaoSession daoSession;
    private FraudPhoneDao phoneDao;
    private FraudSmsDao smsDao;

    private CallLogObserver callLogObserver;
    private SmsObserver smsContentObserver;

    private CallRecord callRecord;
    private Handler mHandler;
    private String smsbody;
    private String result;

    private Notification mNotification;
    private NotificationManager mNotificationManager;
    private PendingIntent mResultIntent;
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

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        /*
         * 取得PendingIntent， 并设置跳转到的Activity，
         */
        Intent intent = new Intent(this, SmsActivity.class);
        mResultIntent = PendingIntent.getActivity(this, 1, intent,
                0);
   /*     daoSession = BaseApplication.getInstances().getDaoSession();
        phoneDao = daoSession.getFraudPhoneDao();
        smsDao = daoSession.getFraudSmsDao();
        phoneDao.deleteAll();
        smsDao.deleteAll();

        FraudPhone phone = new FraudPhone();
    //    phone.setId(1);
        phone.setPhonenumber("12345678901");
        phone.setPhonename("李四");
        phone.setCalltime("2017-04-09 15:22:45");
        phone.setType(0);
        phone.setIsrecord(0);
        phoneDao.insert(phone);
        FraudSms sms = new FraudSms();
   //     sms.setId(1);
        sms.setType(0);
        sms.setSmsname("李四");
        sms.setSmscontent("通过谈话得知该生遇到的问题是对学习逐渐失去兴趣，并把注意力、精力转移到了电子游戏上。" +
                "该生以前学习成绩算不上优秀但也不差，曾经努力学习过一段时间但由于取得的效果不明显，" +
                "于是开始逐渐丧失对学习的兴趣和热情，甚至产生负面的消极的情绪，试图通过电子游戏来转移失落感。");
        sms.setSmsnumber("12345678901");
        sms.setSmstime("2017-04-09 15:22:45");
        smsDao.insert(sms);*/
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


        callRecord = new CallRecord.Builder(this)
                .setRecordFileName("PhoneCallRecorder")
                .setRecordDirName("CallRecorder")
                .setShowSeed(true)
                .build();

        callRecord.changeReceiver(new MyCallRecordReceiver(callRecord));
        callRecord.enableSaveFile();
        callRecord.startCallReceiver();
        SmsRadar.initializeSmsRadarService(this, new SmsListener() {
            @Override
            public void onSmsSent(Sms sms) {
            }

            @Override
            public void onSmsReceived(Sms sms) {
                Log.i("ListenSmsPhone", sms.getMsg() + "  " + sms.getDate() + "  " + sms.getAddress() + "  " + sms.getType());
                smsbody =  sms.getMsg();
                try{
                    Thread t = new Thread(new Runnable(){
                        @Override
                        public void run() {
                            result = getHttp(smsbody,"0");
                        }
                    });
                    t.start();
                    t.join();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                Date d = new Date(Long.parseLong(sms.getDate()));
                String date = dateFormat.format(d);
                SmsData sdata = new SmsData();
                sdata.setSmstime(date);
                sdata.setSmscontent(sms.getMsg());
                sdata.setSmsnumber(sms.getAddress());
                if(result.equals("ok"))
                {
                    BaseApplication.addNormalSms(sdata);
                    //Toast.makeText(Main2Activity.this, "观察者 接收到正常短信", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(Main2Activity.this, "您接收到诈骗短信，请注意防范短信诈骗", Toast.LENGTH_LONG).show();
                    BaseApplication.addFraudSms(sdata);
                    setNotification();
                }
       //         showSmsToast(sms);
            }
        });

   //     SMSReceiver receiver = new SMSReceiver();
   //     registerReceiver(receiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
    //    mHandler = new Handler();

     //   smsContentObserver = new SmsObserver(mHandler, this);
    //    getContentResolver().registerContentObserver(Uri.parse("content://sms"), true, smsContentObserver);
 /*        callLogObserver = new CallLogObserver(mHandler, this);
        getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI, true, callLogObserver);//等价于【Uri.parse("content://call_log/calls")】
*/
    }
    private void showSmsToast(Sms sms) {
        Toast.makeText(this, sms.toString(), Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onDestroy() {
        Log.i("MyService", "mainactivity onDestroy ");
        callRecord.stopCallReceiver();
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
    public String getHttp(String msgBody,String type){
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder fromBodyBuilder = new FormBody.Builder();
        RequestBody requestBody = fromBodyBuilder.add("sms", msgBody).add("type", type).build();
        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder.url("http://dxysun.com:8001/spark/testsms/").post(requestBody).build();

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
    public void setNotification(){
        Bitmap largeIcon = BitmapFactory.decodeResource(
                Main2Activity.this.getResources(), R.drawable.ic_launcher);

        mNotification = new NotificationCompat.Builder(getBaseContext())
                // 设置大图标
                .setLargeIcon(largeIcon)
                        // 设置小图标
                .setSmallIcon(R.drawable.ic_launcher)
                        // 设置状态栏文本标题
                .setTicker("接收到诈骗短信")
                        // 设置标题
                .setContentTitle("短信诈骗")
                        // 设置内容
                .setContentText("您接收到诈骗短信，请注意防范短信诈骗")
                        // 设置ContentIntent
                .setContentIntent(mResultIntent)
                        // 设置Notification提示铃声为系统默认铃声
                .setSound(
                        RingtoneManager.getActualDefaultRingtoneUri(
                                getBaseContext(),
                                RingtoneManager.TYPE_NOTIFICATION))
                        // 点击Notification的时候使它自动消失
                .setAutoCancel(true).build();
        mNotificationManager.notify(0, mNotification);
    }

}
