package com.example.dingchen.mobilesafe;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactListActivity extends Activity {
    private ListView lv_contact;
    private List<HashMap<String,String>> contactList = new ArrayList<HashMap<String,String>>();
    private  MAdapter myAdapter;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            myAdapter =  new MAdapter();
            lv_contact.setAdapter(myAdapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        initUI();
        initDate();
    }
    class MAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return contactList.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(),R.layout.listview_contact_item,null);
            TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
            TextView tv_phone = (TextView)view.findViewById(R.id.tv_phone);
            tv_name.setText(getItem(position).get("name"));
            tv_phone.setText(getItem(position).get("phone"));
            return view;
        }

        @Override
        public HashMap<String, String> getItem(int position) {
            return contactList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }


    private void initDate() {
        new Thread(){
            @Override
            public void run() {
                //读取系统联系人可能是个耗时操作
                //获得内容解析器对象
                ContentResolver contentResolver = getContentResolver();
                //查询系统联系人数据库过程
                Cursor cursor = contentResolver.query(Uri.parse("content://com.android.contacts/raw_contacts"),
                        new String[]{"contact_id"},
                        null,null,null);
                contactList.clear();
                //循环一个游标直到没有数据为止
                while(cursor.moveToNext()){
                    String id = cursor.getString(0);
                    Cursor indexCursor = contentResolver.query(Uri.parse("content://com.android.contacts/data"),new String[]{"data1","mimetype"},
                            "raw_contact_id = ?",new String[]{id},null);
                    //根据用户唯一性id，查询data表和minetype表生成视图
                    //循环获得每个联系人的电话号码
                    HashMap<String,String> hashMap = new HashMap<String,String>();
                    while(indexCursor.moveToNext()){
                        String data = indexCursor.getString(0);
                        String type = indexCursor.getString(1);
                        //区分类型，填充数据
                        if(type.equals("vnd.android.cursor.item/phone_v2")){
                            if(!TextUtils.isEmpty(data)){
                                hashMap.put("phone",data);
                            }

                        }else if(type.equals("vnd.android.cursor.item/name")){
                            if(!TextUtils.isEmpty(data)){
                                hashMap.put("name",data);
                            }
                        }

                    }
                    indexCursor.close();
                    contactList.add(hashMap);
                }
                cursor.close();
                //消息机制,发送一个空的消息
                mHandler.sendEmptyMessage(0);
            }
        }.start();

    }

    private void initUI() {
         lv_contact= (ListView)findViewById(R.id.lv_contact);
         lv_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 //获取点重条目的索引指向集合中的对象
                 if(myAdapter!=null){
                    HashMap<String,String> hashMap = myAdapter.getItem(position);
                     //获取当前条目指向的电话号码
                     String phone = hashMap.get("phone");
                     Intent intent = new Intent();
                     intent.putExtra("phone", phone);
                     setResult(1, intent);
                     finish();
                 }

             }
         });
    }
}
