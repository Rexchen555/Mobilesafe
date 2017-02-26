package com.example.dingchen.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

public class blackNumberActivity extends Activity {
    private  BlackNumberDao  mDao;
    private  List<BlackNumberInfo> mNumberInfoList;
    public MyAdapter mAdapter;
    private ListView lv_blacknumber;
    public int mode = 1;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //告知listView
            mAdapter = new MyAdapter();
            lv_blacknumber.setAdapter(mAdapter);
            super.handleMessage(msg);
        }
    };
    class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return mNumberInfoList.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return mNumberInfoList.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(),R.layout.listview_blacknumber_item,null);
            TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);
            TextView tv_mode = (TextView)view.findViewById(R.id.tv_mode);
            tv_phone.setText(mNumberInfoList.get(position).phone);

            switch (mode){
                case 1:
                    tv_mode.setText("message");
                    break;
                case 2:
                    tv_mode.setText("phone");
                    break;
                case 3:
                    tv_mode.setText("all");
                    break;
            }
            return view;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_number);
        initUI();
        initData();
    }

    private void initData() {
        //获取数据库中所有的电话号码
        new Thread(){
            public void run(){
                //获取操作数据库的对象
                mDao = BlackNumberDao.getInstance(getApplicationContext());
                //查询所有数据的操作
                mNumberInfoList = mDao.findAll();
                //通过消息机制告知主线程去使用消息机制
                mHandler.sendEmptyMessage(0);

            }
        }.start();
    }

    private void initUI() {
        Button bt_add = (Button)findViewById(R.id.bt_add);
        lv_blacknumber = (ListView) findViewById(R.id.lv_blacknumber);
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }
    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(getApplicationContext(),R.layout.dialog_add_blacknumber,null);
        dialog.setView(view,0,0,0,0);
        final EditText et_phone = (EditText) view.findViewById(R.id.et_phone);
        RadioGroup rg_group = (RadioGroup)view.findViewById(R.id.rg_group);
        Button bt_submit= (Button)view.findViewById(R.id.bt_submit);
        Button bt_cancel= (Button)view.findViewById(R.id.bt_cancel);
        //监听选中条目的切换
        rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_sms:
                        mode = 1;
                        break;
                    case R.id.rb_phone:
                        mode = 2;
                        break;
                    case R.id.rb_all:
                        mode = 3;
                        break;
                }
            }
        });
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取输入框中的电话号码
                String phone = et_phone.getText().toString();
                if(!TextUtils.isEmpty(phone)){
                    //插入数据库
                    mDao.insert(phone,mode+"");
                    //让数据库和集合保持同步
                    BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
                    blackNumberInfo.phone = phone;
                    blackNumberInfo.mode = mode+"";
                    //将对象插入到集合的最顶部
                    mNumberInfoList.add(0,blackNumberInfo);
                    //通知数据适配器
                    if(mAdapter!=null){
                        mAdapter.notifyDataSetChanged();
                    }
                    dialog.dismiss();
                }else{
                    ToastUtil.show(getApplicationContext(),"please enter number");
                }
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
