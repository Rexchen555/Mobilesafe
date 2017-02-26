package com.example.dingchen.mobilesafe;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.event.OnItemClick;

public class HomeActivity extends AppCompatActivity {
    private  GridView gv_home;
    private  String[] mTitleString;
    private  int[]  drawableIds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //初始化UI
        initUI();
        //准备数据
        initData();
    }

    private void initData() {
        //文字和图片
        //手机防盗，通信卫士，软件管理，进程管理，流量统计，手机杀毒，缓存清理，高级工具，设置中心
        mTitleString = new String[]{
           "Guardian","Net","Software","Process","Traffic","Virus","Cleaner","Tools","Setting"
        };
        drawableIds = new int[]{
                R.drawable.homesafe,R.drawable.homecallmsgsaf,R.drawable.homeapps,R.drawable.hometaskmanager,
                R.drawable.homenetmanager,R.drawable.hometrojan,R.drawable.homesysoptimize,R.drawable.hometools,
                R.drawable.homesettings
        };
        //九宫格数据适配器
        gv_home.setAdapter(new MyAdapter());
        //注册九宫格单个条目的点击事件
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        //开启对话框
                        showDialog();
                        break;
                    case 1:
                        startActivity(new Intent(getApplicationContext(),blackNumberActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(getApplicationContext(),AppManagementActivity.class));
                        break;
                    case 3:

                        break;
                    case 4:

                        break;
                    case 5:

                        break;
                    case 6:

                        break;
                    case 7:
                        //跳转到高级功能列表
                        startActivity(new Intent(getApplicationContext(),AToolActivity.class));
                        break;
                    case 8:
                        Intent intent = new Intent(getApplicationContext(),SettingActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });

    }

    private void showDialog() {
        //判断本地是否有存储密码
        String psd = SpUtils.getString(this,ConstantValue.MOBILE_SAFE_PSD,"");
        //初始设置密码对话框
        if(TextUtils.isEmpty(psd)){
            showSetPsdDialog();
        }
        //确认密码对话框
        else {
            showConfirmPsdDialog();
        }

    }
    //初始设置密码对话框
    private void showSetPsdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        final View view = View.inflate(this,R.layout.dialog_set_psd,null);
        dialog.setView(view);
        dialog.show();
        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_set_psd = (EditText) view.findViewById(R.id.et_set_psd);
                EditText et_confirm_psd = (EditText)view.findViewById(R.id.et_confirm_psd);
                String psd = et_confirm_psd.getText().toString();
                String conpsd = et_set_psd.getText().toString();
                if(!TextUtils.isEmpty(psd) && !TextUtils.isEmpty(conpsd)){
                    if(psd.equals(conpsd)){
                        //Intent intent = new Intent(getApplicationContext(),TestActivity.class);
                        Intent intent = new Intent(getApplicationContext(),SetupOverActivity.class);
                        startActivity(intent);
                        //到了新的界面需要隐藏对话框
                        dialog.dismiss();
                        SpUtils.putString(getApplicationContext(),ConstantValue.MOBILE_SAFE_PSD,Md5Util.encoder(conpsd));
                    }else{
                        ToastUtil.show(getApplication(),"confirm password error");
                    }
                }else{
                    ToastUtil.show(getApplication(),"please enter password");
                }
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    //确认密码对话框
    private void showConfirmPsdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        final View view = View.inflate(this,R.layout.dialog_confirm_psd,null);
        dialog.setView(view);
        dialog.show();
        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_confirm_psd = (EditText)view.findViewById(R.id.et_confirm_psd);
                String conpsd = et_confirm_psd.getText().toString();
                if( !TextUtils.isEmpty(conpsd)){
                    //获得32位密码，让输入密码同样要Md5
                    String psd = SpUtils.getString(getApplicationContext(),ConstantValue.MOBILE_SAFE_PSD,"");
                    if(psd.equals(Md5Util.encoder(conpsd))){
                        //Intent intent = new Intent(getApplicationContext(),TestActivity.class);
                        Intent intent = new Intent(getApplicationContext(),SetupOverActivity.class);
                        startActivity(intent);
                        //到了新的界面需要隐藏对话框
                        dialog.dismiss();
                        SpUtils.putString(getApplicationContext(),ConstantValue.MOBILE_SAFE_PSD,psd);
                    }else{
                        ToastUtil.show(getApplication(),"confirm password error");
                    }
                }else{
                    ToastUtil.show(getApplication(),"please enter password");
                }
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void initUI() {
       gv_home = (GridView) findViewById(R.id.gv_home);
    }
    class MyAdapter extends BaseAdapter{
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(),R.layout.gridview_item,null);
            TextView tv_title= (TextView)view.findViewById(R.id.tv_title);
            ImageView iv_icon = (ImageView)view.findViewById(R.id.iv_icon);
            tv_title.setText(mTitleString[position]);
            iv_icon.setBackgroundResource(drawableIds[position]);
            return view;
        }

        @Override
        public Object getItem(int position) {
            return mTitleString[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getCount() {
            //条目总数文字组数==图片张数
            return mTitleString.length;
        }
    }

}
