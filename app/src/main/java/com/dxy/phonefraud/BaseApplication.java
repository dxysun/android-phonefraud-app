package com.dxy.phonefraud;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dxy.phonefraud.DataSource.GetCall;
import com.dxy.phonefraud.DataSource.GetSms;
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
        String content = "恭喜您！浙江卫视《中国好声音》第三季栏目开播以来受到全国观众的观看和热爱，" +
                "为了答谢全国观众的大力支持特举办全国手机用户抽奖活动。您的手机号码很幸运被场外抽奖抽中，" +
                "将获得赞助商苹果笔记本电脑一台及奖金11800元人民币！请您登陆http://hsxzuk.com确认领奖，" +
                "领奖码2384.工作人员将在确认领奖起24小时内联系您并将奖品邮寄给您。请您尽快确认领奖 谢谢！";
        String content1 = "尊敬的手机用户您好！您已被湖南卫视《爸爸去哪儿》栏目组真情回馈活动抽取为场外幸运号，" +
                "将获得由999感冒灵与小儿感冒药联合赞助的梦想基金98000元人民币（RMB）" +
                "与苹果MacBook Pro笔记本电脑一台，您的验证码为【5898】，详情请登陆官方活动" +
                "网站:www.babaac.com填写邮寄地址及时领取。注：如将个人领奖信息泄露给他人" +
                "造成冒名领取本台概不负责。【湖南卫视】";
        fraudsmslist = new ArrayList<>();
        fraudsmslist.add(new SmsData("111", "18205147449", "2017-05-08 20:58:31", content1, 0));
        fraudsmslist.add(new SmsData("110", "1069674936730", "2017-04-14 13:34:45", content, 0));

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
        PhoneData  p =   new PhoneData("110","67443", "2017-05-08 22:32:34",0);
        p.setIsrecord(1);
        p.setListtype(3);
        p.setRecordpath("/storage/emulated/0/CallRecorder/CallRecorder.pcm");
        recoredphonelist.add(p);
     /*   for (int i = 0; i < 5; i++) {
            PhoneData  p =   new PhoneData("110","1346775885" + i, "张三", "2017-04-"+ i,0);
            p.setIsrecord(1);
            p.setListtype(3);
            p.setRecordpath("/storage/emulated/0/CallRecorder/CallRecorder.pcm");
            recoredphonelist.add(p);
        }*/
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

  /*      DaoSession ds = instances.getDaoSession();
        FraudPhoneDao fphonedao = ds.getFraudPhoneDao();
        List<FraudPhone> fphonelist =  fphonedao.loadAll();
        Log.i("NormalPhoneFragment", "setFraudphonelist  " + fphonelist.size());
        fraudphonelist = new ArrayList<>();
        for(FraudPhone phone : fphonelist) {
            PhoneData p = new PhoneData();
            p.setListtype(0);
            p.setPhonename(phone.getPhonename());
            p.setType(0);
            p.setCalltime(phone.getCalltime());
            p.setPhonenumber(phone.getPhonenumber());
            fraudphonelist.add(p);
        }*/
   /*     PhoneData phone1 = new PhoneData();
        phone1.setPhonenumber("1234567890145");
        phone1.setPhonename("王五");
        phone1.setCalltime("2017-04-15 15:22:45");
        phone1.setType(0);
        phone1.setIsrecord(0);
        phone1.save();*/

        fraudphonelist = new ArrayList<>();
        PhoneData phone = new PhoneData();
        phone.setPhonenumber("18631266315");
    //    phone.setPhonename("李四");
        phone.setCalltime("2017-04-09 15:22:45");
        phone.setType(0);
        phone.setIsrecord(0);
        fraudphonelist.add(phone);
        List<PhoneData> pl1 = DataSupport.findAll(PhoneData.class);
        if(pl1.size() > 0)
        {
            PhoneData pl = DataSupport.find(PhoneData.class,pl1.get(0).getId());
            fraudphonelist.add(pl);
        }

     //   fraudphonelist =
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
    public static void deleteNormalphone(int position,String phonenumber,Context context){
        if(normalphoneAdapter != null)
        normalphoneAdapter.remove(position);
/*        normalphonelist.remove(position);
    //    GetCall.DeleteCallByNumber(context, phonenumber);
        if(normalphoneAdapter != null)
        {
            normalphoneAdapter.notifyDataSetChanged();
        }*/
    }

    public static void deleteFraudlphone(int position,String phonenumber,Context context){
   //     PhoneData p = fraudphonelist.get(position);
        if(fraudphoneAdapter != null)
        fraudphoneAdapter.remove(position);
   //     GetCall.DeleteCallByNumber(context, phonenumber);
    //    DaoSession ds = instances.getDaoSession();
     //   FraudPhoneDao fphonedao = ds.getFraudPhoneDao();
  /*      fraudphonelist.remove(position);
        if(fraudphoneAdapter != null)
        {
            fraudphoneAdapter.notifyDataSetChanged();
        }*/
    }

    public static void deleteRecordphone(int position,String phonenumber,Context context){
        //     PhoneData p = fraudphonelist.get(position);
        if(recordhoneAdapter != null)
        recordhoneAdapter.remove(position);
        //     GetCall.DeleteCallByNumber(context, phonenumber);
        //    DaoSession ds = instances.getDaoSession();
        //   FraudPhoneDao fphonedao = ds.getFraudPhoneDao();
  /*      fraudphonelist.remove(position);
        if(fraudphoneAdapter != null)
        {
            fraudphoneAdapter.notifyDataSetChanged();
        }*/
    }
    public static void deleteNormalSms(int position,String id,Context context){
        if(normalsmsAdapter != null)
            normalsmsAdapter.remove(position);
    }
    public static void deleteFraudSms(int position,String id,Context context){
        if(fraudsmsAdapter != null)
            fraudsmsAdapter.remove(position);

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



    public static void addFraudPhone(PhoneData fphone){
    //    fraudphonelist.add(p);
        fphone.setType(0);
        if(fraudphoneAdapter != null)
        {
          //  fraudphoneAdapter.notifyDataSetChanged();
            fraudphoneAdapter.add(fphone);
        }

    }
    public static void addNormalPhone(PhoneData phone){
        //    fraudphonelist.add(p);
        phone.setType(1);
        if(normalphoneAdapter != null)
        {
            //  fraudphoneAdapter.notifyDataSetChanged();
            normalphoneAdapter.add(phone);
        }
    }


    public static void addFraudSms(SmsData sms){
        sms.setType(0);
        Log.i("ListenSmsPhone", "addFraudSms fraudsmsAdapter");
        if(fraudsmsAdapter != null)
        {
            //  fraudphoneAdapter.notifyDataSetChanged();
            Log.i("ListenSmsPhone", "addFraudSms update");
            fraudsmsAdapter.add(sms);
        }
    }
    public static void addNormalSms(SmsData sms){
        sms.setType(1);
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
