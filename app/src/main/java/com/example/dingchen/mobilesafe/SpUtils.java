package com.example.dingchen.mobilesafe;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by DIngChen on 2/17/2017.
 */

public class SpUtils {
    private static SharedPreferences sp;
    //写
    public static void putBoolean(Context context,String key,boolean value){
        if(sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key,value).commit();
    }
    //读
    public static boolean getBoolean(Context context,String key,boolean dfValue){
        if(sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key,dfValue);
    }

    public static void putString(Context context,String key,String value){
        if(sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putString(key,value).commit();
    }
    //读
    public static String getString(Context context,String key,String dfValue){
        if(sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getString(key,dfValue);
    }
    //上下文环境，需要移除节点的名称
    public static void remove(Context context, String key) {
        if(sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().remove(key).commit();
    }

}
