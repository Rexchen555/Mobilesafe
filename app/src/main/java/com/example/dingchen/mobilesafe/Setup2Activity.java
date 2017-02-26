package com.example.dingchen.mobilesafe;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;

public class Setup2Activity extends SetUpBaseActivity {
    private SettingItemView siv_sim_bount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        InitUI();
    }

    private void InitUI() {
        siv_sim_bount = (SettingItemView)findViewById(R.id.siv_sim_bount);
        //读取已有的绑定状态（是否存储了sim的序列号）
        String sim_number = SpUtils.getString(this,ConstantValue.SIM_NUMBER,"");
        //判断序列卡号为空
        if(TextUtils.isEmpty(sim_number)){
            siv_sim_bount.setCheck(false);
        }else{
            siv_sim_bount.setCheck(true);
        }
        siv_sim_bount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //3.获取原有的状态
                boolean isCheck = siv_sim_bount.isCheck();
                //将原有状态取反
                //状态设置给当前条目
                siv_sim_bount.setCheck(!isCheck);
                if(!isCheck){
                    //存储
                    //获得sim序列号 TelephonyManager
                    TelephonyManager manager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                    String simSerialNumber = manager.getSimSerialNumber();
                    SpUtils.putString(getApplicationContext(),ConstantValue.SIM_NUMBER,simSerialNumber);
                }else{
                    //不存储
                    SpUtils.remove(getApplicationContext(),ConstantValue.SIM_NUMBER);
                }
            }
        });
    }




    @Override
    public void pre_activity() {
        Intent intent = new Intent(getApplicationContext(),Setup1Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }

    @Override
    public void next_activity() {
        String serialNumber = SpUtils.getString(this,ConstantValue.SIM_NUMBER,"");
        if(!TextUtils.isEmpty(serialNumber)){
            Intent intent = new Intent(getApplicationContext(),Setup3Activity.class);
            startActivity(intent);
            finish();
        }
        else{
            ToastUtil.show(this,"please bind SIM");
        }
        overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
    }
}
