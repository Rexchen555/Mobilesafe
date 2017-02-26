package com.example.dingchen.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class Setup4Activity extends SetUpBaseActivity {
    private  CheckBox cb_box;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        initUI();
    }

    private void initUI() {
        cb_box = (CheckBox) findViewById(R.id.cb_box);
        //是否选中状态的回显
        boolean open_security = SpUtils.getBoolean(getApplicationContext(),ConstantValue.OPEN_SECURITY,false);
        cb_box.setChecked(open_security);
        if(open_security){
            cb_box.setText("protect open");
        }else{
            cb_box.setText("protect closed");
        }
        cb_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SpUtils.putBoolean(getApplicationContext(), ConstantValue.OPEN_SECURITY, isChecked);
                if(isChecked){
                    cb_box.setText("protect open");
                }else {
                    cb_box.setText("protect closed");
                }
            }
        });

    }




    @Override
    public void pre_activity() {
        Intent intent = new Intent(getApplicationContext(), Setup3Activity.class);
        startActivity(intent);

        finish();
        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }

    @Override
    public void next_activity() {
        boolean open_security = SpUtils.getBoolean(this, ConstantValue.OPEN_SECURITY, false);
        if(open_security){
            Intent intent = new Intent(getApplicationContext(), SetupOverActivity.class);
            startActivity(intent);

            finish();
            SpUtils.putBoolean(this, ConstantValue.SETUP_OVER, true);


        }else{
            ToastUtil.show(getApplicationContext(), "open protect");
        }
        overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
    }
}
