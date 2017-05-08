package com.dxy.phonefraud.listen;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.dxy.phonefraud.R;
import com.dxy.phonefraud.speechtotext.FucUtil;
import com.dxy.phonefraud.speechtotext.JsonParser;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by dongx on 2017/5/8.
 */
public class RecordToText {


    static int  ret = 0; // 函数调用返回值
    private static SpeechRecognizer mIat;
    private static String  mEngineType = SpeechConstant.TYPE_CLOUD;
    private static HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private static SharedPreferences mSharedPreferences;
    private static Context  context;

    private static String record_path;

    private static InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d("SpeechRecognizer", "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
            //    showTip("初始化失败，错误码：" + code);
                Log.d("SpeechRecognizer", "初始化失败，错误码：" + code);
            }
        }
    };
    public static void  getRecognizeResult(Context context,String path,String phonenumber,RecognizerListener mRecognizerListener){
        mIat = SpeechRecognizer.createRecognizer(context, mInitListener);
        mSharedPreferences = context.getSharedPreferences("com.iflytek.setting", Activity.MODE_PRIVATE);
        record_path = path;
        RecognizeClick(phonenumber,mRecognizerListener);

    }
    public static void RecognizeClick(String phonenumber,RecognizerListener mRecognizerListener){
        mIatResults.clear();
        // 设置参数
        //   BaseApplication appState = ((BaseApplication)getApplicationContext());
    //    String file_name = "/storage/emulated/0/CallRecorder/CallRecorder.pcm";
        String file_name = record_path;
        setParam(phonenumber);
        // 设置音频来源为外部文件
        mIat.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");
        // 也可以像以下这样直接设置音频文件路径识别（要求设置文件在sdcard上的全路径）：
        //mIat.setParameter(SpeechConstant.AUDIO_SOURCE, "-2");
        //mIat.setParameter(SpeechConstant.ASR_SOURCE_PATH, "sdcard/XXX/XXX.pcm");
        ret = mIat.startListening(mRecognizerListener);
        //   String file_name = "record-33704495.pcm";
        //  Log.i("filepath",path);
        if (ret != ErrorCode.SUCCESS || file_name == null) {
    //        showTip("识别失败,错误码：" + ret);
        } else {
            byte[] audioData = FucUtil.readAudioFile(file_name);

            if (null != audioData) {
         //       showTip(context.getString(R.string.text_begin_recognizer));
                // 一次（也可以分多次）写入音频文件数据，数据格式必须是采样率为8KHz或16KHz（本地识别只支持16K采样率，云端都支持），位长16bit，单声道的wav或者pcm
                // 写入8KHz采样的音频时，必须先调用setParameter(SpeechConstant.SAMPLE_RATE, "8000")设置正确的采样率
                // 注：当音频过长，静音部分时长超过VAD_EOS将导致静音后面部分不能识别。
                // 音频切分方法：FucUtil.splitBuffer(byte[] buffer,int length,int spsize);
                mIat.writeAudio(audioData, 0, audioData.length);
                mIat.stopListening();
            } else {
                mIat.cancel();
     //           showTip("读取音频流失败");
            }
        }
    }
    public static void setParam(String phonenumber) {
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

}
