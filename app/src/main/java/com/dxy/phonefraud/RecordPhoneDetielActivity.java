package com.dxy.phonefraud;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dxy.phonefraud.entity.PhoneData;
import com.dxy.phonefraud.listen.RecordToText;
import com.dxy.phonefraud.speechtotext.FucUtil;
import com.dxy.phonefraud.speechtotext.JsonParser;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class RecordPhoneDetielActivity extends Activity implements View.OnClickListener{
    private String TAG = "RecordPhoneDetielActivityListenText";
    private ImageView iv_back;
    private TextView record_info;
    private TextView record_time;
    private Button delete_record;
    private Button sign_record_fraud;
    private Button record_to_text;
    private ImageButton playButton;
    private TextView record_file;
    private TextView record_text;
    private PlayTask player;
    private Dialog alertDialog;
    private File file;
    private boolean isPlaying;
    private boolean isPlay;
    private Toast mToast;
    private PhoneData phone;
    private  int position;

    int ret = 0; // 函数调用返回值
    private SpeechRecognizer mIat;
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private RecognizerDialog mIatDialog;
    private SharedPreferences mSharedPreferences;

    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d("SpeechRecognizer", "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code);
                Log.d("SpeechRecognizer", "初始化失败，错误码：" + code);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_phone_detiel);
 //       SpeechUtility.createUtility(this, "appid=" + getString(R.string.app_id));
        iv_back = (ImageView)findViewById(R.id.iv_left_image);
        record_info = (TextView)findViewById(R.id.record_info);
        record_time = (TextView)findViewById(R.id.record_time);
        delete_record = (Button)findViewById(R.id.delete_record);
        sign_record_fraud = (Button)findViewById(R.id.sign_record_fraud);

        record_text = (TextView)findViewById(R.id.record_text);
        record_to_text = (Button)findViewById(R.id.record_to_text);
        playButton = (ImageButton)findViewById(R.id.playButton);
        record_file = (TextView)findViewById(R.id.record_file);

        isPlaying = false;
        isPlay = true;
        setdata();
        if(phone.getType() == 0)
        {
            record_info.setTextColor(Color.RED);
            sign_record_fraud.setText("标记为正常电话");
        }

        if(mInitListener == null)
        {
            Log.i("MscSpeechLog","mInitListener null");
        }
        else
        {
            Log.i("MscSpeechLog","mInitListener not null");
        }
        mIat = SpeechRecognizer.createRecognizer(this, mInitListener);
        if(mIat == null)
        {
            Log.i("MscSpeechLog","mIat null");
        }
        else
        {
            Log.i("MscSpeechLog","mIat not null");
        }
        mSharedPreferences = getSharedPreferences("com.iflytek.setting",Activity.MODE_PRIVATE);
        mIatDialog = new RecognizerDialog(this, mInitListener);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        delete_record.setOnClickListener(this);
        sign_record_fraud.setOnClickListener(this);
        record_to_text.setOnClickListener(this);
        playButton.setOnClickListener(this);
        record_to_text.setOnClickListener(this);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordPhoneDetielActivity.this.finish();
            }
        });


    }
    public void setdata(){
        //PhoneData sms= (SmsData) intent.getSerializableExtra("obj");
        Bundle bundle = getIntent().getExtras();
        phone = bundle.getParcelable("phone");

        position = bundle.getInt("position");

        if(phone.getPhonename() != null)
        {
            record_info.setText(phone.getPhonename()+"   "+ phone.getPhonenumber());
        }
        else
        {
            record_info.setText(phone.getPhonenumber());
        }
        record_time.setText(phone.getCalltime());
        file = new File(phone.getRecordpath());
    }
    @Override
    public void onClick(View v){
        // TODO Auto-generated method stub

        switch (v.getId()) {
            case R.id.sign_record_fraud:
                String str = "您确定要把此电话标记为诈骗电话？";
                if(phone.getType() == 0)
                {
                    str = "您确定要把此电话标记为正常电话？";
                }
                alertDialog = new AlertDialog.Builder(this)
                        .setTitle("确定标记？")
                        .setMessage(str)
                                // .setIcon(R.drawable.lianxiren)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface arg0,
                                                        int arg1) {
                                        if(phone.getType() == 0)
                                        {
                                            BaseApplication.addNormalPhone(position,phone, RecordPhoneDetielActivity.this);
                                            BaseApplication.deleteFraudlphone(-1,phone, RecordPhoneDetielActivity.this);
                                        }
                                        else
                                        {
                                            BaseApplication.addFraudPhone(position,phone,RecordPhoneDetielActivity.this);
                                        }

                         //               BaseApplication.deleteRecordphone(position,phone,RecordPhoneDetielActivity.this);
                                        RecordPhoneDetielActivity.this.finish();
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface arg0,
                                                        int arg1) {
                                        alertDialog.cancel();
                                    }
                                }).create();
                alertDialog.show();

                break;
            case R.id.delete_record:
                alertDialog = new AlertDialog.Builder(this)
                        .setTitle("确定删除？")
                        .setMessage("您确定删除该号码的通话录音信息？")
                                // .setIcon(R.drawable.lianxiren)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface arg0,
                                                        int arg1) {
                                        BaseApplication.deleteRecordphone(position, phone, RecordPhoneDetielActivity.this);
                                        RecordPhoneDetielActivity.this.finish();
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface arg0,
                                                        int arg1) {
                                        alertDialog.cancel();
                                    }
                                }).create();
                alertDialog.show();
                break;
            case R.id.record_to_text:
                Log.i("MscSpeechLog", "RecognizeClick() ");
                RecognizeClick();
            //    record_text.setText("为，一会去哪吃饭，去三号门吧，好的，一会三号门见，行");
            //    RecordToText.getRecognizeResult(this,"/storage/emulated/0/CallRecorder/CallRecorder.pcm",phone.getPhonenumber(),mRecognizerListener);
                break;
            case R.id.playButton:
                if(isPlay){
                    Log.i(TAG,"开始播放 task");
                    player = new PlayTask();
                    player.execute();
                    isPlay = false;
                }
                else
                {
                    Log.i(TAG,"结束播放 task");
                    isPlaying = false;
                    isPlay = true;
                }

                break;
            default:
                break;
        }
    }
    public short LowToShort(short a) {
        return (short)(((a & 0xFF) << 8) | ((a >> 8) & 0xFF));
    }
    class PlayTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            Log.i(TAG,"开始录音 task");
            isPlaying = true;
            int frequency = 16000;
            //格式
            int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
            //   int channelConfiguration = AudioFormat.CHANNEL_IN_MONO;
            //16Bit
            int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
            int bufferSize = AudioTrack.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
            short[] buffer = new short[bufferSize/4];
            try {
                //定义输入流，将音频写入到AudioTrack类中，实现播放
                DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
                //实例AudioTrack
                AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC, frequency, channelConfiguration, audioEncoding, bufferSize, AudioTrack.MODE_STREAM);
                //开始播放
                track.play();
                //由于AudioTrack播放的是流，所以，我们需要一边播放一边读取
                while(isPlaying && dis.available()>0){
                    int i = 0;
                    while(dis.available()>0 && i<buffer.length){
                        short s = LowToShort(dis.readShort());
                        buffer[i] = s;
                        //   buffer[i] = dis.readShort();
                        i++;
                    }
                    //然后将数据写入到AudioTrack中
                    track.write(buffer, 0, buffer.length);
                    if(dis.available() <= 0)
                    {
                        Log.i(TAG,"播放  not available()");
                    }

                }
                Log.i(TAG, "播放结束 task");

                //播放结束
                track.stop();
                dis.close();
            } catch (Exception e) {
                // TODO: handle exception
            }
            return null;
        }

        protected void onPostExecute(Void result){
            Log.i(TAG,"播放之后 ");
            playButton.setBackgroundResource(R.color.white);
            playButton.setBackgroundResource(R.drawable.play);
            record_file.setText("录音文件");
        }

        protected void onPreExecute(){
            Log.i(TAG, "播放之前 ");
            playButton.setBackgroundResource(R.drawable.pause);
            record_file.setText("录音文件  正在播放");
        }

    }

    public void RecognizeClick(){
        record_text.setText(null);// 清空显示内容
        mIatResults.clear();
        // 设置参数
     //   BaseApplication appState = ((BaseApplication)getApplicationContext());
        String file_name = "/storage/emulated/0/CallRecorder/CallRecorder.pcm";
        if(phone != null)
        {
            file_name = phone.getRecordpath();
            setParam(phone.getPhonenumber());
        }

        // 设置音频来源为外部文件
        mIat.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");
        // 也可以像以下这样直接设置音频文件路径识别（要求设置文件在sdcard上的全路径）：
        //mIat.setParameter(SpeechConstant.AUDIO_SOURCE, "-2");
        //mIat.setParameter(SpeechConstant.ASR_SOURCE_PATH, "sdcard/XXX/XXX.pcm");
        ret = mIat.startListening(mRecognizerListener);
        //   String file_name = "record-33704495.pcm";
        //  Log.i("filepath",path);
        if (ret != ErrorCode.SUCCESS || file_name == null) {
            showTip("识别失败,错误码：" + ret);
        } else {
            byte[] audioData = FucUtil.readAudioFile(file_name);

            if (null != audioData) {
                showTip(getString(R.string.text_begin_recognizer));
                // 一次（也可以分多次）写入音频文件数据，数据格式必须是采样率为8KHz或16KHz（本地识别只支持16K采样率，云端都支持），位长16bit，单声道的wav或者pcm
                // 写入8KHz采样的音频时，必须先调用setParameter(SpeechConstant.SAMPLE_RATE, "8000")设置正确的采样率
                // 注：当音频过长，静音部分时长超过VAD_EOS将导致静音后面部分不能识别。
                // 音频切分方法：FucUtil.splitBuffer(byte[] buffer,int length,int spsize);
                mIat.writeAudio(audioData, 0, audioData.length);
                mIat.stopListening();
            } else {
                mIat.cancel();
                showTip("读取音频流失败");
            }
        }
    }
    public void setParam(String phonenumber) {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        //  String lag = mSharedPreferences.getString("iat_language_preference","mandarin");
        String lag = "zh_cn";
        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);
        }

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "10000"));

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "10000"));

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/call/iat_"+phonenumber+".wav");
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
            showTip(error.getPlainDescription(true));
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
        //    showTip("结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d(TAG, results.getResultString());
            printResult(results);

            if (isLast) {
                // TODO 最后的结果
            //    record_text.setText(s);
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
        //    showTip("当前正在说话，音量大小：" + volume);
            Log.d(TAG, "返回音频数据："+data.length);
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

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }
    private void printResult(RecognizerResult results) {
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

        record_text.setText(resultBuffer.toString());
    //    mResultText.setSelection(mResultText.length());
    }

}
