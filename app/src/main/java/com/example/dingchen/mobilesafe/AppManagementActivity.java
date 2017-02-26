package com.example.dingchen.mobilesafe;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class AppManagementActivity extends Activity {
    private ListView lv_app_list;
    public List<AppInfo> mAppInfoList;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            MyAdapter mAdapter =   new MyAdapter();
           lv_app_list.setAdapter(mAdapter);
        }
    };
    class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return mAppInfoList.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null){
                convertView = View.inflate(getApplicationContext(),R.layout.listview_app_item,null);
                holder = new ViewHolder();
                holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_path = (TextView) convertView.findViewById(R.id.tv_path);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            holder.iv_icon.setBackgroundDrawable(mAppInfoList.get(position).icon);
            holder.tv_name.setText(mAppInfoList.get(position).name);
            if(mAppInfoList.get(position).isSdCard){
                holder.tv_path.setText("sd card");
            }else {
                holder.tv_path.setText("system");
            }
            return convertView;
        }


        @Override
        public Object getItem(int position) {
            return mAppInfoList.get(position);
        }
    }
    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_path;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_management);
        initTitle();

        initList();
    }

    private void initList() {
         lv_app_list = (ListView)findViewById(R.id.lv_app_list);
        new Thread(){
            @Override
            public void run() {
                 mAppInfoList = AppInfoprovider.getAppInfoList(getApplicationContext());
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    private void initTitle() {
        //获得磁盘
        String path = Environment.getDataDirectory().getAbsolutePath();

        //获得sd卡
        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();

        //获取以上两个文件夹可用的大小
        String memoryAvailSpace = Formatter.formatFileSize(this,getAvaibleSpace(path));
        String sdMemoryAvailSpace = Formatter.formatFileSize(this,getAvaibleSpace(sdPath));
        TextView tv_memory = (TextView) findViewById(R.id.tv_memory);
        TextView tv_sd_memory = (TextView) findViewById(R.id.tv_sd_memory);
        tv_memory.setText("disk:"+memoryAvailSpace);
        tv_sd_memory.setText("sd:"+sdMemoryAvailSpace);

    }

    private long getAvaibleSpace(String path) {
        StatFs statFs = new StatFs(path);
        long count = statFs.getAvailableBlocks();
        long size = statFs.getBlockSize();
        return count*size;
    }
}
