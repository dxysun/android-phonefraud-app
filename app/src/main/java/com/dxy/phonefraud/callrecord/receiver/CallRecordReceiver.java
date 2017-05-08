package com.dxy.phonefraud.callrecord.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.dxy.phonefraud.BaseApplication;
import com.dxy.phonefraud.DataSource.GetCall;
import com.dxy.phonefraud.R;
import com.dxy.phonefraud.callrecord.CallRecord;
import com.dxy.phonefraud.callrecord.PhoneListener;
import com.dxy.phonefraud.callrecord.PhoneReceive;
import com.dxy.phonefraud.callrecord.helper.PrefsHelper;
import com.dxy.phonefraud.entity.Constants;
import com.dxy.phonefraud.entity.NetworkTest;
import com.dxy.phonefraud.listen.RecordToText;
import com.dxy.phonefraud.speechtotext.JsonParser;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by aykutasil on 19.10.2016.
 */
public class CallRecordReceiver extends PhoneCallReceiver {


    private static final String TAG = CallRecordReceiver.class.getSimpleName();

    public static final String ACTION_IN = "android.intent.action.PHONE_STATE";
    public static final String ACTION_OUT = "android.intent.action.NEW_OUTGOING_CALL";
    public static final String EXTRA_PHONE_NUMBER = "android.intent.extra.PHONE_NUMBER";

    protected CallRecord callRecord;
    private static MediaRecorder recorder;
    private static AudioRecord  audioRecord;
    private File audiofile;
    private boolean isRecordStarted = false;
    private String phonenumber;
    //是否在录制
    private boolean isRecording = false;

    private static String result;
    private  static Toast toast = null;
    private  static Timer timer = null;
    private  static Timer time1 = null;


    private Context context;
    private Notification mNotification;
    private NotificationManager mNotificationManager;
    private PendingIntent mResultIntent;


 //   private RecordTask recordertask;
  public CallRecordReceiver( ) {

  }
    public CallRecordReceiver(CallRecord callRecord) {
        this.callRecord = callRecord;
    }

    @Override
    protected void onIncomingCallReceived(Context context, String number, Date start) {
        //    startRecord(context, "incoming", number);
        this.context = context;
        PhoneReceive.phoneListener.onIncomingCallReceived(context, number, start);
        if(number == null)
        {
            Log.i(TAG,"onIncomingCallReceived  empty");
        }
        else
        {
            Log.i(TAG,"onIncomingCallReceived   " + number);
        }
     //   SpeechApp appState = ((SpeechApp)context.getApplicationContext());
     //   appState.setPhonenumber(number);
        phonenumber = number;
        String name = GetCall.queryNameFromContactsByNumber(context, number);

        if(name == null)
        {
            if(NetworkTest.getNetWorkStatus(context) == Constants.NETWORK_CLASS_2_G || NetworkTest.getNetWorkStatus(context) == Constants.NETWORK_CLASS_UNKNOWN)
            {
                Log.i(TAG,"network   off" );
                isRecordStarted = true;
            }
            else
            {
                Log.i(TAG, "network   ok");
                //    Toast.makeText(context.getApplicationContext(), "RINGING " +mIncomingNumber, Toast.LENGTH_LONG).show();
                try{
                    Thread t = new Thread(new Runnable(){
                        @Override
                        public void run() {
                            result = getHttp("http://dxysun.com:8001/spark/testphone/?phone="+phonenumber+"&type=1");
                        }
                    });
                    t.start();
                    t.join();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                if(result.equals("ok"))
                {
                    Log.i(TAG, "RINGING  :" + "result ok");
                    isRecordStarted = true;
                    Toast.makeText(context.getApplicationContext(), "未知状态的陌生电话，已开启通话录音", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Log.i(TAG, "RINGING :" + "result get not ok");
                    //  Toast.makeText(context.getApplicationContext(), "诈骗电话，请及时挂断", Toast.LENGTH_LONG).show();
                    CustomTimeToast(context.getApplicationContext(), "诈骗电话，请及时挂断", 30 * 1000);
                }
                Log.i(TAG, "RINGING :" + phonenumber);
            }
        }

    }

    @Override
    protected void onIncomingCallAnswered(Context context, String number, Date start) {
        //    startRecord(context, "incoming", number);
        cancelToast();

        if(number == null)
        {
            number = phonenumber;
            Log.i(TAG,"onIncomingCallAnswered:" +phonenumber );
        }
        else
        {
            Log.i(TAG,"onIncomingCallAnswered " + number);
        }
        PhoneReceive.phoneListener.onIncomingCallAnswered(context,number,start);
        Log.i(TAG, "onIncomingCallAnswered  isRecordStarted  " + isRecordStarted);
        SharedPreferences pref = context.getSharedPreferences("set", context.MODE_PRIVATE);
        Boolean b = pref.getBoolean("is_record", true);
        if(isRecordStarted && b)
        {
            Log.i(TAG,"Record   start" );
            CustomTimeToast(context.getApplicationContext(), "录音开始", 10 * 1000);
            RecordTask  recordertask = new RecordTask();
            recordertask.context = context;
            recordertask.phoneNumber = number;
            recordertask.seed = "incoming";
            recordertask.execute();
        }
    }

    @Override
    protected void onIncomingCallEnded(Context context, String number, Date start, Date end) {
        stopRecord(context);
        cancelToast();
        String path = audiofile.getAbsolutePath();
        if(number == null)
        {
            number = phonenumber;
            Log.i(TAG,"onIncomingCallAnswered:" +phonenumber );
        }
        else
        {
            Log.i(TAG,"onIncomingCallAnswered " + number);
        }
        PhoneReceive.phoneListener.onIncomingCallEnded(context,number,start,end,isRecordStarted,path);
    }

    @Override
    protected void onOutgoingCallStarted(Context context, String number, Date start) {
    //    startRecord(context, "outgoing", number);
        if(number == null)
        {
            Log.i(TAG, "onOutgoingCallStarted :" + "    null");
        }
        else
        {
            Log.i(TAG, "onOutgoingCallStarted :" + number);
        }

 /*       RecordTask   recordertask = new RecordTask();
        recordertask.context = context;
        recordertask.phoneNumber = number;
        recordertask.seed = "outgoing";
        recordertask.execute();*/
    }

    @Override
    protected void onOutgoingCallEnded(Context context, String number, Date start, Date end) {
        if(number == null)
        {
            Log.i(TAG, "onOutgoingCallEnded :" + "    null");
        }
        else
        {
            Log.i(TAG, "onOutgoingCallEnded :" + number);
        }
  //      stopRecord(context);
    }

    @Override
    protected void onMissedCall(Context context, String number, Date start) {
        cancelToast();
    }

    // Derived classes could override these to respond to specific events of interest
    protected void onRecordingStarted(Context context, CallRecord callRecord, File audioFile) {
    //    SpeechApp appState = ((SpeechApp)context.getApplicationContext());
     //   appState.setSavefilePath(audiofile.getAbsolutePath());
    }

    protected void onRecordingFinished(Context context, CallRecord callRecord, File audioFile) {
    }

    public static short LowToShort(short a) {
        return (short)(((a & 0xFF) << 8) | ((a >> 8) & 0xFF));
    }

    private void stopRecord(Context context) {
  /*      if (recorder != null && isRecordStarted) {

            recorder.stop();
            recorder.reset();
            recorder.release();

            isRecordStarted = false;
            onRecordingFinished(context, callRecord, audiofile);

            Log.i(TAG, "record stop");
        }*/
        if (audioRecord != null && isRecording) {
            isRecording = false;
            Log.i(TAG, "record stop");
        }

    }
    public String getHttp(String url){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, java.util.concurrent.TimeUnit.SECONDS)
                .build();
        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder.get().url(url).build();
        Call call = okHttpClient.newCall(request);
        String result = "no result";
        Log.i(TAG, "start  request");
        try {
            Response response = call.execute();     //同步
            result = response.body().string();
            Log.i(TAG, "result  :" + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
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
    public void cancelToast(){

        if(time1 != null)
        {
            Log.i(TAG, "toast  :" + "cancel");
            time1.cancel();
            toast.cancel();
            timer.cancel();
        }
    }
    class RecordTask extends AsyncTask<Void, Integer, Void> {
        Context context;
        String seed;
        String phoneNumber;
        @Override
        protected Void doInBackground(Void... arg0) {
            Log.i(TAG,"开始录音");
            //16K采集率
            int frequency = 16000;
            //格式
            int channelConfiguration = AudioFormat.CHANNEL_IN_MONO;

            //16Bit
            int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

            String file_name = PrefsHelper.readPrefString(context, CallRecord.PREF_FILE_NAME);
            String dir_path = PrefsHelper.readPrefString(context, CallRecord.PREF_DIR_PATH);
            String dir_name = PrefsHelper.readPrefString(context, CallRecord.PREF_DIR_NAME);
            boolean show_seed = PrefsHelper.readPrefBool(context, CallRecord.PREF_SHOW_SEED);
            boolean show_phone_number = PrefsHelper.readPrefBool(context, CallRecord.PREF_SHOW_PHONE_NUMBER);
        //    int output_format = PrefsHelper.readPrefInt(context, CallRecord.PREF_OUTPUT_FORMAT);
            int audio_source = PrefsHelper.readPrefInt(context, CallRecord.PREF_AUDIO_SOURCE);
         //   int audio_encoder = PrefsHelper.readPrefInt(context, CallRecord.PREF_AUDIO_ENCODER);
            Log.i(TAG,"file_name : "+file_name);
            Log.i(TAG,"dir_path : "+dir_path);
            Log.i(TAG,"dir_name : "+dir_name);
            Log.i(TAG,"audio_source : "+audio_source);

            File sampleDir = new File(dir_path + "/" + dir_name);

            if (!sampleDir.exists()) {
                sampleDir.mkdirs();
            }


            StringBuilder fileNameBuilder = new StringBuilder();
            fileNameBuilder.append(file_name);
            fileNameBuilder.append("_");

            if (show_seed) {
                fileNameBuilder.append(seed);
                fileNameBuilder.append("_");
            }

            if (show_phone_number) {
                fileNameBuilder.append(phoneNumber);
                fileNameBuilder.append("_");
            }
            if(phoneNumber == null)
            {
                Log.i("phoneNumber", "empty");
            }
            else
            {
                Log.i("phoneNumber", phoneNumber);
            }

            file_name = fileNameBuilder.toString();
            Log.i(TAG, file_name);
            String suffix = ".pcm";

            try {
                //输出流
                audiofile = File.createTempFile(file_name, suffix, sampleDir);
                Log.i(TAG, "audiofile     " + audiofile.getAbsolutePath());

                OutputStream os = new FileOutputStream(audiofile);
                BufferedOutputStream bos = new BufferedOutputStream(os);
                DataOutputStream dos = new DataOutputStream(bos);
                int bufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
                audioRecord = new AudioRecord(audio_source, frequency, channelConfiguration, audioEncoding, bufferSize);

                short[] buffer = new short[bufferSize];
                audioRecord.startRecording();
                onRecordingStarted(context, callRecord, audiofile);
                Log.i(TAG, "开始录音");
                isRecording = true;
                while (isRecording) {
                    int bufferReadResult = audioRecord.read(buffer, 0, bufferSize);
                    for (int i = 0; i < bufferReadResult; i++) {

                        short s = LowToShort(buffer[i]);
                        dos.writeShort(s);
                    }
                }
                audioRecord.stop();
                onRecordingFinished(context, callRecord, audiofile);
                //        audioRecord.release();
                dos.close();
            } catch (Throwable t) {
                Log.e(TAG, "录音失败");
            }
            return null;
        }

        //当在上面方法中调用publishProgress时，该方法触发,该方法在UI线程中被执行
        protected void onProgressUpdate(Integer...progress){

        }

        protected void onPostExecute(Void result){

        }

        protected void onPreExecute(){

        }

    }

}
