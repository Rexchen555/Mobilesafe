package com.example.dingchen.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SetupOverActivity extends Activity {
private TextView tv_safe_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean setup_over = SpUtils.getBoolean(this,ConstantValue.SETUP_OVER,false);
        if(setup_over){
            //密码输入成功并且全部设置完成跳转到这里
            setContentView(R.layout.activity_setup_over);
            initUI();
        }else{
        //如果没有设置完成，跳转到导航界面的第一个
            Intent intent = new Intent(this,Setup1Activity.class);
            startActivity(intent);
            //开启了一个新的界面，就关闭功能列表
            finish();
            }
    }

    private void initUI() {
        tv_safe_number = (TextView) findViewById(R.id.tv_safe_number);
        String phone = SpUtils.getString(getApplicationContext(),ConstantValue.CONTACT_PHONE,"");
        tv_safe_number.setText(phone);
        TextView tv_reset_setup = (TextView) findViewById(R.id.tv_reset_setup);
        tv_reset_setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Setup1Activity.class));
                finish();
            }
        });
    }
}
