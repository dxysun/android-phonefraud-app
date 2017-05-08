package com.dxy.phonefraud.entity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.SyncStateContract;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by dongx on 2017/4/12.
 */
public class NetworkTest {
    /**
     * Unknown network class
     */
    public static final int NETWORK_CLASS_UNKNOWN = 0;

    /**
     * wifi net work
     */
    public static final int NETWORK_WIFI = 1;

    /**
     * "2G" networks
     */
    public static final int NETWORK_CLASS_2_G = 2;

    /**
     * "3G" networks
     */
    public static final int NETWORK_CLASS_3_G = 3;

    /**
     * "4G" networks
     */
    public static final int NETWORK_CLASS_4_G = 4;

    /**
     * 检测当的网络（WLAN、3G/2G）状态
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean networktest() {

        try {

            String connectionurl = "http://dxysun.com:8001/spark/testphone/?phone=123456";
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(2, java.util.concurrent.TimeUnit.SECONDS)
                    .build();
            Request request = new Request.Builder()
                    .url(connectionurl)
                    .build();
            Response response = client.newCall(request).execute();
            Log.d("Network test", String.valueOf(response.code()));
            if (response.code() == 200) {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    public static  boolean ping() {

        String result = null;
        try {
            String ip = "www.dxysun.com";// ping 的地址，可以换成任何一种可靠的外网
            Process p = Runtime.getRuntime().exec("ping -c 2 -w 100 " + ip);// ping网址3次
            // 读取ping的内容，可以不加
        /*    InputStream input = p.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuffer stringBuffer = new StringBuffer();
            String content = "";
            while ((content = in.readLine()) != null) {
                stringBuffer.append(content);
            }
            Log.d("------ping-----", "result content : " + stringBuffer.toString());*/
            // ping的状态
            int status = p.waitFor();
            if (status == 0) {
                result = "success";
                return true;
            } else {
                result = "failed";
            }
        } catch (IOException e) {
            result = "IOException";
        } catch (InterruptedException e) {
            result = "InterruptedException";
        } finally {
            Log.i("CallRecord", "----result---" + "result = " + result);
        }
        return false;
    }
    public static int getNetWorkClass(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return Constants.NETWORK_CLASS_2_G;

            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return Constants.NETWORK_CLASS_3_G;

            case TelephonyManager.NETWORK_TYPE_LTE:
                return Constants.NETWORK_CLASS_4_G;

            default:
                return Constants.NETWORK_CLASS_UNKNOWN;
        }
    }
    public static int getNetWorkStatus(Context context) {
        int netWorkType = Constants.NETWORK_CLASS_UNKNOWN;

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            int type = networkInfo.getType();

            if (type == ConnectivityManager.TYPE_WIFI) {
                netWorkType = Constants.NETWORK_WIFI;
            } else if (type == ConnectivityManager.TYPE_MOBILE) {
                netWorkType = getNetWorkClass(context);
            }
        }

        return netWorkType;
    }
}
