package com.example.dingchen.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class QueryAddressActivity extends Activity {
    private EditText et_phone;
    private Button bt_query;
    private TextView tv_query_result;
    private String mAddress;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            tv_query_result.setText(mAddress);
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_address);
        initUI();
    }

    public void initUI() {
        et_phone = (EditText)findViewById(R.id.et_phone);
        bt_query = (Button)findViewById(R.id.bt_query);
        tv_query_result = (TextView)findViewById(R.id.tv_query_result);
        bt_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = et_phone.getText().toString();
                if(!TextUtils.isEmpty(phone)) {
                    query(phone);
                }else{
                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    et_phone.startAnimation(shake);
                }
            }
        });
        //实时查询，监听输入框中文本的变化
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    String phone = et_phone.getText().toString();
                    query(phone);
            }
        });
    }

    private void query(final String phone) {
        new Thread(){

            public void run(){
                 mAddress = AddressDao.getAddress(phone);
                mHandler.sendEmptyMessage(0);
            }

        }.start();

    }
}
