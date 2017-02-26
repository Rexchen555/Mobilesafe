package com.example.dingchen.mobilesafe;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DIngChen on 2/25/2017.
 */

public class AppInfoprovider {
    //返回当前手机所有应用相关信息
    public static List<AppInfo> getAppInfoList(Context ctx){
        //包的管理者对象
        PackageManager pm = ctx.getPackageManager();
        //获得安装在手机上的应用信息相关集合
        List<PackageInfo> packageList= pm.getInstalledPackages(0);
        ArrayList<AppInfo> appInfoList = new ArrayList<AppInfo>();
        //循环遍历应用信息相关集合
        for(PackageInfo packageInfo : packageList){
            AppInfo appInfo = new AppInfo();
            //获得应用包名
            appInfo.packageName = packageInfo.packageName;
            //获得应用名称
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            applicationInfo.loadLabel(pm).toString();
            //获得图标
            appInfo.icon = applicationInfo.loadIcon(pm);
            //判断是否为系统应用
            if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)==ApplicationInfo.FLAG_SYSTEM){
              //系统应用
                appInfo.isSystem = true;
            }else{
                //非系统应用
                appInfo.isSystem = false;
            }if((applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE)==ApplicationInfo.FLAG_EXTERNAL_STORAGE){
                //系统应用
                appInfo.isSdCard = true;
            }else{
                //非系统应用
                appInfo.isSdCard = false;
            }
            appInfoList.add(appInfo);
        }
        return appInfoList;
    }


}
