package com.dxy.phonefraud;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dxy.phonefraud.DataSource.GetCall;
import com.dxy.phonefraud.DataSource.GetSms;
import com.dxy.phonefraud.DataSource.SmsInfo;
import com.dxy.phonefraud.DataSource.SmsReadDao;
import com.dxy.phonefraud.adapter.FraudPhoneAdapter;
import com.dxy.phonefraud.adapter.FraudSmsAdapter;
import com.dxy.phonefraud.adapter.NormalPhoneAdapter;
import com.dxy.phonefraud.adapter.NormalSmsAdapter;
import com.dxy.phonefraud.adapter.RecordPhoneAdapter;
import com.dxy.phonefraud.entity.PhoneData;
import com.dxy.phonefraud.entity.SmsData;
import com.iflytek.cloud.SpeechUtility;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by dongx on 2017/4/9.
 */
public class BaseApplication extends Application {

    private SQLiteDatabase db;


    private static NormalPhoneAdapter normalphoneAdapter;
    private static ArrayList<PhoneData> normalphonelist;
    private static HashMap<String,ArrayList<PhoneData>> normalphonemap;

    private static FraudPhoneAdapter fraudphoneAdapter;
    private static ArrayList<PhoneData> fraudphonelist;

    private static RecordPhoneAdapter recordhoneAdapter;
    private static ArrayList<PhoneData> recoredphonelist;

    private static FraudSmsAdapter fraudsmsAdapter;
    private static List<SmsData> fraudsmslist;

    private static NormalSmsAdapter normalsmsAdapter;
    private static List<SmsData> normalsmslist;
    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        SpeechUtility.createUtility(BaseApplication.this, "appid=" + getString(R.string.app_id));
    }
    public static List<SmsData> getNormalsmslist() {
        return normalsmslist;
    }

    public static void setNormalsmslist(Context context) {

        normalsmslist = GetSms.getSmsInPhone(context);

    }

    public static NormalSmsAdapter getNormalsmsAdapter() {
        return normalsmsAdapter;
    }

    public static void setNormalsmsAdapter(NormalSmsAdapter normalsmsAdapter) {
        BaseApplication.normalsmsAdapter = normalsmsAdapter;
    }

    public static List<SmsData> getFraudsmslist() {
        return fraudsmslist;
    }

    public static void setFraudsmslist() {
        fraudsmslist = new ArrayList<>();
  /*      String content = "恭喜您！浙江卫视《中国好声音》第三季栏目开播以来受到全国观众的观看和热爱，" +
                "为了答谢全国观众的大力支持特举办全国手机用户抽奖活动。您的手机号码很幸运被场外抽奖抽中，" +
                "将获得赞助商苹果笔记本电脑一台及奖金11800元人民币！请您登陆http://hsxzuk.com确认领奖，" +
                "领奖码2384.工作人员将在确认领奖起24小时内联系您并将奖品邮寄给您。请您尽快确认领奖 谢谢！";
        String content1 = "尊敬的手机用户您好！您已被湖南卫视《爸爸去哪儿》栏目组真情回馈活动抽取为场外幸运号，" +
                "将获得由999感冒灵与小儿感冒药联合赞助的梦想基金98000元人民币（RMB）" +
                "与苹果MacBook Pro笔记本电脑一台，您的验证码为【5898】，详情请登陆官方活动" +
                "网站:www.babaac.com填写邮寄地址及时领取。注：如将个人领奖信息泄露给他人" +
                "造成冒名领取本台概不负责。【湖南卫视】";

        fraudsmslist.add(new SmsData("111", "18205147449", "2017-05-08 20:58:31", content1, 0));
        fraudsmslist.add(new SmsData("110", "1069674936730", "2017-04-14 13:34:45", content, 0));*/
        List<SmsData> pl1 = DataSupport.findAll(SmsData.class);
        for(int i = 0;i < pl1.size();i ++)
        {
            SmsData s1 = pl1.get(i);
            if(s1.getType() == 0)
            {
                fraudsmslist.add(s1);
            }
        }


    }

    public static FraudSmsAdapter getFraudsmsAdapter() {
        return fraudsmsAdapter;
    }

    public static void setFraudsmsAdapter(FraudSmsAdapter fraudsmsAdapter) {
        BaseApplication.fraudsmsAdapter = fraudsmsAdapter;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public static RecordPhoneAdapter getRecordhoneAdapter() {
        return recordhoneAdapter;
    }

    public static void setRecordhoneAdapter(RecordPhoneAdapter recordhoneAdapter) {
        BaseApplication.recordhoneAdapter = recordhoneAdapter;
    }

    public static ArrayList<PhoneData> getRecoredphonelist() {
        return recoredphonelist;
    }

    public static void setRecoredphonelist() {
        recoredphonelist = new ArrayList<>();
        List<PhoneData> plist = DataSupport.where("phonenumber = ?","67443").find(PhoneData.class);
        if(plist.size() == 0)
        {
            PhoneData  p =   new PhoneData("110","67443", "2017-05-08 22:32:34",0);
            p.setIsrecord(1);
            p.setType(2);
            p.setListtype(3);
            p.setRecordpath("/storage/emulated/0/CallRecorder/CallRecorder.pcm");
            p.save();
        }

        List<PhoneData> pl1 = DataSupport.findAll(PhoneData.class);
        for(int i = 0;i < pl1.size();i ++)
        {
            PhoneData p1 = pl1.get(i);
            if(p1.getIsrecord() == 1)
            {
                recoredphonelist.add(p1);
            }
        }
    }

    public static FraudPhoneAdapter getFraudphoneAdapter() {
        return fraudphoneAdapter;
    }

    public static void setFraudphoneAdapter(FraudPhoneAdapter fraudphoneAdapter) {
        BaseApplication.fraudphoneAdapter = fraudphoneAdapter;
    }

    public static NormalPhoneAdapter getNormalphoneAdapter() {
        return normalphoneAdapter;
    }

    public static void setNormalphoneAdapter(NormalPhoneAdapter normalphoneAdapter) {
        BaseApplication.normalphoneAdapter = normalphoneAdapter;
    }

    public static ArrayList<PhoneData> getFraudphonelist() {

        return fraudphonelist;
    }

    public static void  setFraudphonelist() {

        fraudphonelist = new ArrayList<>();
    /*    PhoneData phone = new PhoneData();
        phone.setPhonenumber("18631266315");
        phone.setCalltime("2017-04-09 15:22:45");
        phone.setType(0);
        phone.setIsrecord(0);
        fraudphonelist.add(phone);*/
        List<PhoneData> pl1 = DataSupport.findAll(PhoneData.class);
        for(int i = 0;i < pl1.size();i ++)
        {
            PhoneData p = pl1.get(i);
            if(p.getType() == 0)
            {
                fraudphonelist.add(p);
            }
        }

    }

    public static ArrayList<PhoneData> getNormalphonelist() {
        return normalphonelist;
    }

    public static HashMap<String, ArrayList<PhoneData>> getNormalphonemap() {
        return normalphonemap;
    }

    public static void setNormalphonemap(HashMap<String, ArrayList<PhoneData>> phonemap) {
        normalphonemap = phonemap;
    }
    public static void deleteNormalphone(int position,PhoneData phone,Context context){
        if(normalphoneAdapter != null)
        {
        //    DataSupport.delete(PhoneData.class, phone.getId());
            GetCall.DeleteCallByNumber(context,phone.getPhonenumber());
            normalphoneAdapter.remove(position);
        }

    }

    public static void deleteFraudlphone(int position,PhoneData phone,Context context){
   //     PhoneData p = fraudphonelist.get(position);

        if(fraudphoneAdapter != null && position != -1)
        {
            if(phone.getIsrecord() == 1)
            {
                for(int i = 0;i < recoredphonelist.size();i ++)
                {
                    if(recoredphonelist.get(i).getPhonenumber().equals(phone.getPhonenumber()))
                    {
                        recordhoneAdapter.remove(i);
                        break;
                        //    recoredphonelist.remove(i);
                    }
                }
            }

            DataSupport.delete(PhoneData.class, phone.getId());
            fraudphoneAdapter.remove(position);

        }
        else
        {
            int pos = 0;
            for(int i = 0;i < fraudphonelist.size();i ++)
            {
                if(fraudphonelist.get(i).getPhonenumber().equals(phone.getPhonenumber()))
                {
                    pos = i;
                    break;
                }
            }
            fraudphonelist.remove(pos);
        }

    }

    public static void deleteRecordphone(int position,PhoneData phone,Context context){
    //    PhoneData p = DataSupport.find(PhoneData.class,phone.getId());
     //   phone.setIsrecord(0);
        if(phone.getType() != 0)
        {
            DataSupport.delete(PhoneData.class, phone.getId());
        }
        else
        {
            ContentValues values = new ContentValues();
            values.put("isrecord", "0");
            DataSupport.update(PhoneData.class, values, phone.getId());
        }
        if(recordhoneAdapter != null)
        recordhoneAdapter.remove(position);

    }
    public static void deleteNormalSms(int position,SmsData sms,Context context){
        if(normalsmsAdapter != null)
        {
        //    DataSupport.delete(SmsData.class, sms.getId());
            SmsReadDao.deleteOneReceivedSms(context, sms.getSmsid());
            normalsmsAdapter.remove(position);
        }

    }
    public static void deleteFraudSms(int position,SmsData sms,Context context){
        if(fraudsmsAdapter != null)
        {
            DataSupport.delete(SmsData.class, sms.getId());
            fraudsmsAdapter.remove(position);
        }
    }

    public static void setNormalphonelist(Context context) {
        Log.i("NormalPhoneFragment", "set  list phonemap ");
        normalphonelist = new ArrayList<>();
        normalphonemap = GetCall.GetCallsInPhoneBymap(context);

        List<Map.Entry<String,ArrayList<PhoneData>>> listtemp = new ArrayList<>(normalphonemap.entrySet());
        Collections.sort(listtemp, new Comparator<Map.Entry<String, ArrayList<PhoneData>>>() {
            //降序排序
            @Override
            public int compare(Map.Entry<String, ArrayList<PhoneData>> o1, Map.Entry<String, ArrayList<PhoneData>> o2) {
                String time1 = o1.getValue().get(0).getCalltime();
                String time2 = o2.getValue().get(0).getCalltime();
                return time2.compareTo(time1);
            }
        });
        normalphonemap.clear();
        for(Map.Entry<String, ArrayList<PhoneData>> entry : listtemp){
            normalphonemap.put(entry.getKey(), entry.getValue());
            PhoneData p = entry.getValue().get(0);
            normalphonelist.add(p);
        }

    }

   public static void addRecordPhone(int position,PhoneData fphone,Context context)
   {
       Log.i("ListenSmsPhone", "addRecordPhone ");
         if(recordhoneAdapter != null)
         {
             Log.i("ListenSmsPhone", "addRecordPhone not null");
             fphone.save();
             recordhoneAdapter.add(fphone);
         }
   }

    public static void addFraudPhone(int position,PhoneData fphone,Context context){
    //    fraudphonelist.add(p);
        fphone.setListtype(0);
        fphone.setType(0);
        Log.i("BaseApplication", "getIsrecord " + fphone.getIsrecord());
        if(position != -1)
        {
            if (fphone.getIsrecord() == 1 )
            {
                Log.i("BaseApplication", "id " + fphone.getId());
                recoredphonelist.get(position).setType(0);
                recordhoneAdapter.notifyDataSetChanged();
                Log.i("BaseApplication", "getType " + fphone.getType());
                ContentValues values = new ContentValues();
                values.put("type", "0");
                DataSupport.update(PhoneData.class, values, fphone.getId());
                //  normalphoneAdapter

                int pos = -1;
                for(int i = 0;i < normalphonelist.size();i ++)
                {
                    if(fphone.getPhonenumber() != null && normalphonelist.get(i).getPhonenumber().equals(fphone.getPhonenumber()))
                    {
                        pos = i;
                        break;
                    }
                }
                if(pos != -1)
                {
                    normalphonelist.remove(pos);
                    normalphoneAdapter.notifyDataSetChanged();
                }
                GetCall.DeleteCallByNumber(context, fphone.getPhonenumber());
                //    PhoneData p1 = DataSupport.find(PhoneData.class, fphone.getId());
                //    Log.i("BaseApplication", "after getType " + p1.getType());
            }
            else
            {
                fphone.save();
            }
        }
        else
        {
            fphone.save();
        }
        if(fraudphoneAdapter != null)
        {
          //  fraudphoneAdapter.notifyDataSetChanged();
            fraudphoneAdapter.add(fphone);
        }

    }
    public static void addNormalPhone(int position,PhoneData phone,Context context){
        //    fraudphonelist.add(p);
      //  phone.setType(1);
        if(position != -1)
        {
            GetCall.insertCallLog(context, phone);
            if(phone.getIsrecord() == 0)
            {
                DataSupport.delete(PhoneData.class, phone.getId());
            }
            else
            {
                recoredphonelist.get(position).setType(1);
                recordhoneAdapter.notifyDataSetChanged();
                ContentValues values = new ContentValues();
                values.put("type", "1");
                DataSupport.update(PhoneData.class, values, phone.getId());
            }

        }

        if(normalphoneAdapter != null)
        {
            //  fraudphoneAdapter.notifyDataSetChanged();
            normalphoneAdapter.add(phone);
        }
    }


    public static void addFraudSms(int position,SmsData sms,Context context){
        sms.setType(0);
        sms.save();
        if(position != -1)
        {
            SmsReadDao.deleteOneReceivedSms(context,sms.getSmsid());
        }
        else
        {
            SmsInfo smsinfo = SmsReadDao.getLastReceivedSmsInfo(context);
            Log.i("ListenSmsPhone", "body  "+smsinfo.getBody());
            SmsReadDao.deleteOneReceivedSms(context, smsinfo.getId());
        }

        Log.i("ListenSmsPhone", "addFraudSms fraudsmsAdapter");
        if(fraudsmsAdapter != null)
        {
            //  fraudphoneAdapter.notifyDataSetChanged();
            Log.i("ListenSmsPhone", "addFraudSms update");
            fraudsmsAdapter.add(sms);
        }
    }
    public static void addNormalSms(int position,SmsData sms,Context context){
        if(position != -1)
        {
            SmsReadDao.insertSms(context, sms);
        }
       // sms.setType(1);
    //    SmsReadDao.deleteLastSms(context, sms.getSmsid());
        if(normalsmsAdapter != null)
        {
            //  fraudphoneAdapter.notifyDataSetChanged();
            normalsmsAdapter.add(sms);
        }
    }
    public static String postHttp(String msgBody,String type){
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder fromBodyBuilder = new FormBody.Builder();
        RequestBody requestBody = fromBodyBuilder.add("sms", msgBody).add("type", type).build();
        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder.url("http://dxysun.com:8001/spark/testsms/").post(requestBody).build();

        Call call = okHttpClient.newCall(request);
        Log.i("ListenSmsPhone", "start  request");
        String result = "nonetwork";
        try {
            Response response = call.execute();     //同步
            result = response.body().string();
            Log.i("ListenSmsPhone", "result  :" + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
