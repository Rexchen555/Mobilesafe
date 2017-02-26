package com.example.dingchen.mobilesafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Setup3Activity extends SetUpBaseActivity {
private EditText et_phone_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        initUI();
    }

    private void initUI() {
        //显示电话号码的输入框
       et_phone_number = (EditText)findViewById(R.id.et_phone_number);

        //获得回显
        String phone = SpUtils.getString(this,ConstantValue.CONTACT_PHONE,"");
        et_phone_number.setText(phone);
        //点击选择联系人的按钮
        Button bt_select_number = (Button)findViewById(R.id.bt_select_number);
        bt_select_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ContactListActivity.class);
                startActivityForResult(intent,0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data!=null){
            //1,返回到当前界面的时候,接受结果的方法
            String phone = data.getStringExtra("phone");
            //2,将特殊字符过滤(中划线转换成空字符串)
            phone = phone.replace("-", "").replace(" ", "").trim();
            et_phone_number.setText(phone);
            SpUtils.putString(getApplicationContext(),ConstantValue.CONTACT_PHONE,phone);

        }

        super.onActivityResult(requestCode, resultCode, data);
    }




    @Override
    public void pre_activity() {
        //String contact_phone = SpUtils.getString(getApplicationContext(),ConstantValue.CONTACT_PHONE,"");

        Intent intent = new Intent(getApplicationContext(), Setup2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }

    @Override
    public void next_activity() {
        String phone = et_phone_number.getText().toString();
        if(!TextUtils.isEmpty(phone)) {
            Intent intent = new Intent(getApplicationContext(), Setup4Activity.class);
            startActivity(intent);
            finish();
            //如果是输入的电话号码，需要保存
            SpUtils.getString(getApplicationContext(), ConstantValue.CONTACT_PHONE, "");
        }else{
            ToastUtil.show(getApplication(),"please enter number");
        }
        overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
    }
}
