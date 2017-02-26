
package com.example.dingchen.mobilesafe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //一旦监听到关机广播，就要发送短信到指定接收者
        //获得存储的sim卡
        String spSimNumber = SpUtils.getString(context,ConstantValue.SIM_NUMBER,"");
        //获得当前手机sim卡
        TelephonyManager manager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String newspSimNumber = manager.getSimSerialNumber();
        //两个sim卡对比
        if(!spSimNumber.equals(newspSimNumber)){
            //如果序列号不一致，则给联系人发送短信
            SmsManager sm = SmsManager.getDefault();
            String phone = SpUtils.getString(context,ConstantValue.CONTACT_PHONE,"");
            sm.sendTextMessage(phone,null,"sim change",null,null);
        }

    }
}
