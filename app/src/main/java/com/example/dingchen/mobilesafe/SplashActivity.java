package com.example.dingchen.mobilesafe;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class SplashActivity extends AppCompatActivity {
    protected static final String tag = "SplashActivity";
    //更新新版本的状态吗
    protected static final int UPDATE_VERSION = 100;
    //进入应用程序的状态码
    protected static final int ENTER_HOME = 101;
    protected static final int URL_ERROR = 102;
    protected static final int IO_ERROR = 103;
    protected static final int JSON_ERROR = 104;
    private int mLocalVersionCode;
    private String mVersionDes;
    private String downloadUrl;
    private  RelativeLayout rl_root;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_VERSION:
                    //弹出对话框,提示用户更新
                    showUpdateDialog();
                    break;
                case ENTER_HOME:
                    //进入应用程序主界面,activity跳转过程
                    enterHome();
                    break;
                case URL_ERROR:
                    ToastUtil.show(SplashActivity.this,"url error");
                    enterHome();
                    break;
                case IO_ERROR:
                    ToastUtil.show(SplashActivity.this,"read error");
                    enterHome();
                    break;
                case JSON_ERROR:
                    ToastUtil.show(SplashActivity.this,"JSON error");
                    enterHome();
                    break;
            }
        }
    };
    //弹出对话框，提示用户更新
    private void showUpdateDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置左上角图标
        builder.setIcon(R.drawable.ic);
        builder.setTitle("new version");
        builder.setMessage(mVersionDes);
        builder.setPositiveButton("now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //下载apk,downladUrl
                downloadApk();
            }
        });
        builder.setNegativeButton("later",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //取消对话框
                enterHome();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enterHome();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void downloadApk() {
        //apk链接地址，放置apk的所在路径

        //判断sdk可用
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
        //获取sdk的路径
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"mobilesafe.apk";
            //发送请求，获得apk，并放置到指定位置
            HttpUtils httpUtils = new HttpUtils();
            httpUtils.download(downloadUrl, path, new RequestCallBack<File>() {
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    File file = responseInfo.result;
                    installApk(file);
                }

                @Override
                public void onFailure(HttpException error, String msg) {

                }

                @Override
                public void onStart() {
                    super.onStart();
                }

                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                }
            });
        }
    }
    //安装对应apk
    private void installApk(File file) {
        //系统应用界面
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULF");
        intent.setData(Uri.fromFile(file));
        intent.setType("application/vnd.android.package-archive");
        startActivityForResult(intent,0);
    }
    //开启一个activity后，返回结果

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        enterHome();
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //初始化UI
        initUI();
        //初始化数据
        initData();
        //initAnimation();
        //初始化数据库
        initDB("address.db");
        }

    private void initDB(String name) {
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            inputStream = getAssets().open(name);
            File file = getFilesDir();
            File dbFile = new File(file,name);
            if(!dbFile.exists()){
                fileOutputStream = new FileOutputStream(dbFile);
                byte[] bs = new byte[1024];
                int temp = -1;
                while((temp = inputStream.read(bs))!=-1){
                    fileOutputStream.write(bs, 0, temp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if(inputStream!=null && fileOutputStream!=null){
                    inputStream.close();
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //添加淡入动画效果
    //private void initAnimation() {
       // AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
       // alphaAnimation.setDuration(3000);
       // rl_root.startAnimation(alphaAnimation);
    //}

    private  TextView tv_Version_name;
    private void initUI() {
        //应用名称版本号
       tv_Version_name = (TextView)findViewById(R.id.tv_version_name);
        //检测是否有更新，提示用户下载
        rl_root = (RelativeLayout)findViewById(R.id.rl_root);

    }
    private void initData(){
        //得到版本民称
        tv_Version_name.setText("Version:"+getVersionName());
        mLocalVersionCode = getVersionCode();
        //获得服务器版本号
        //获得json中的内容
       if(SpUtils.getBoolean(this,ConstantValue.OPEN_UPDATE,false)){
            checkVersion();
        }else{
            mHandler.sendEmptyMessageDelayed(ENTER_HOME,4000);

        }

    }
    //检测版本号
    public void checkVersion(){
        new Thread(){
            public void run(){
                Message msg = Message.obtain();
                long startTime = System.currentTimeMillis();
                try {
                    URL url = new URL("http://10.0.2.2:8080/update.json");
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    //设置常见参数（请求头）
                    connection.setConnectTimeout(2000);
                    //读取超时
                    connection.setReadTimeout(2000);

                    if(connection.getResponseCode() == 200){
                        //以流的形式
                        InputStream is = connection.getInputStream();
                        String json = StreamUtil.streamToString(is);
                        Log.i(tag,json);
                        //json的解析
                        JSONObject jsonObject = new JSONObject(json);
                        String versionName = jsonObject.getString("versionName");
                        mVersionDes = jsonObject.getString("versionDes");
                        String versionCode = jsonObject.getString("versionCode");
                        downloadUrl = jsonObject.getString("downloadUrl");
                        Log.i(tag,versionName);
                        Log.i(tag,mVersionDes);
                        Log.i(tag,versionCode);
                        Log.i(tag,downloadUrl);

                        //比对版本号
                        if(mLocalVersionCode < Integer.parseInt(versionCode)){
                            //提示用户更新
                            msg.what = UPDATE_VERSION;
                        }else{
                            //进入主界面
                            msg.what = ENTER_HOME;
                        }
                    }
                }catch(MalformedURLException e){
                    e.printStackTrace();
                    msg.what = URL_ERROR;
                }catch(IOException e){
                    e.printStackTrace();
                    msg.what = IO_ERROR;
                }catch(JSONException e){
                    e.printStackTrace();
                    msg.what = JSON_ERROR;
                }finally {
                    //指定睡眠时间，超过四秒不做处理
                    //小于四秒要
                    long endTime = System.currentTimeMillis();
                    if(endTime-startTime<4000){
                        try {
                            Thread.sleep(4000-(endTime-startTime));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    mHandler.sendMessage(msg);
                }
            };
        }.start();


    }

    private int getVersionCode() {
        //1,包管理者对象packageManager
        PackageManager pm = getPackageManager();
        //2,从包的管理者对象中,获取指定包名的基本信息(版本名称,版本号),传0代表获取基本信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            //3,获取版本名称
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    private String getVersionName(){
        //包管理者对象packageManager
        PackageManager pm = getPackageManager();
        //从包的管理者对象中。获得指定包名的基本信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(this.getPackageName(), 0);
            //获取对应的版本名称
           return packageInfo.versionName;

        }catch(Exception e ){
            e.printStackTrace();
        }
        return null;
    }
   public void enterHome(){
       Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
       //关闭导航界面
       finish();
   }

}
