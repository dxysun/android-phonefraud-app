package com.dxy.phonefraud;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dxy.phonefraud.adapter.myFragmentPagerAdapter;
import com.dxy.phonefraud.fragment.FraudPhoneFragment;
import com.dxy.phonefraud.fragment.NormalPhoneFragment;
import com.dxy.phonefraud.fragment.RecordPhoneFragment;
import android.support.v4.app.Fragment;
import java.util.ArrayList;

public class PhoneActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener,View.OnClickListener{
    private ViewPager viewPager;
    private RadioGroup radioGroup;
    private RadioButton fraud_phone,normal_phone,record_phone;
    private ArrayList<Fragment> alFragment;
    private ImageView iv_back;
    private ImageView iv_report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        //初始化界面组件
        initView();
        //初始化ViewPager
        initViewPager();
    }
    private void initView(){
        iv_back = (ImageView)findViewById(R.id.iv_left_image);
        viewPager=(ViewPager) findViewById(R.id.viewpager);
        radioGroup=(RadioGroup) findViewById(R.id.radiogroup);
        fraud_phone=(RadioButton) findViewById(R.id.fraud_phone);
        normal_phone=(RadioButton) findViewById(R.id.normal_phone);
        record_phone=(RadioButton) findViewById(R.id.record_phone);
        iv_report = (ImageView)findViewById(R.id.iv_right_image);

        iv_report.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(this);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneActivity.this.finish();
            }
        });
    }
    private void initViewPager(){
        FraudPhoneFragment fraudPhoneFragment=new FraudPhoneFragment();
        NormalPhoneFragment normalPhoneFragment=new NormalPhoneFragment();
        RecordPhoneFragment recordPhoneFragment=new RecordPhoneFragment();
        alFragment=new ArrayList<>();
        alFragment.add(fraudPhoneFragment);
        alFragment.add(normalPhoneFragment);
        alFragment.add(recordPhoneFragment);
        //ViewPager设置适配器
     //   getFragmentManager();
        viewPager.setAdapter(new myFragmentPagerAdapter(getSupportFragmentManager(), alFragment));
        //ViewPager显示第一个Fragment
        viewPager.setCurrentItem(0);
        //ViewPager页面切换监听
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //滑动ViewPager,RadioButton选中状态做相应变换
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        radioGroup.check(R.id.fraud_phone);
                        break;
                    case 1:
                        radioGroup.check(R.id.normal_phone);
                        break;
                    case 2:
                        radioGroup.check(R.id.record_phone);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    /**
     * 点击RadioButton切换ViewPager中相应的Fragment
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.fraud_phone:
                viewPager.setCurrentItem(0,false);
                break;
            case R.id.normal_phone:
                viewPager.setCurrentItem(1,false);
                break;
            case R.id.record_phone:
                viewPager.setCurrentItem(2,false);
                break;

        }
    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch(v.getId()){
            case R.id.iv_right_image:
                Intent intent = new Intent(this,ReportActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
        Log.i("用Activity实现", "点击事件");
    }

}
