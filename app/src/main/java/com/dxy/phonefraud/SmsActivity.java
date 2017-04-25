package com.dxy.phonefraud;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dxy.phonefraud.adapter.myFragmentPagerAdapter;
import com.dxy.phonefraud.fragment.FraudPhoneFragment;
import com.dxy.phonefraud.fragment.FraudSmsFragment;
import com.dxy.phonefraud.fragment.NormalPhoneFragment;
import com.dxy.phonefraud.fragment.NormalSmsFragment;
import com.dxy.phonefraud.fragment.VpFragmentLv;

import java.util.ArrayList;

public class SmsActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener{
    private ViewPager viewPager;
    private RadioGroup radioGroup;
    private RadioButton fraud_sms,normal_sms;
    private ArrayList<Fragment> alFragment;
    private ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        //初始化界面组件
        initView();
        //初始化ViewPager
        initViewPager();
    }
    private void initView(){
        iv_back = (ImageView)findViewById(R.id.iv_left_image);
        viewPager=(ViewPager) findViewById(R.id.viewpager);
        radioGroup=(RadioGroup) findViewById(R.id.radiogroup);
        fraud_sms=(RadioButton) findViewById(R.id.fraud_sms);
        normal_sms=(RadioButton) findViewById(R.id.normal_sms);

        radioGroup.setOnCheckedChangeListener(this);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmsActivity.this.finish();
            }
        });
    }
    private void initViewPager(){
        FraudSmsFragment fraudPhoneFragment=new FraudSmsFragment();
        NormalSmsFragment normalPhoneFragment=new NormalSmsFragment();
     //   VpFragmentLv normalPhoneFragment = new VpFragmentLv();
        alFragment=new ArrayList<>();
        alFragment.add(fraudPhoneFragment);
        alFragment.add(normalPhoneFragment);
        //ViewPager设置适配器
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
                        radioGroup.check(R.id.fraud_sms);
                        break;
                    case 1:
                        radioGroup.check(R.id.normal_sms);
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
            case R.id.fraud_sms:
                viewPager.setCurrentItem(0,false);
                break;
            case R.id.normal_sms:
                viewPager.setCurrentItem(1,false);
                break;

        }
    }
}
