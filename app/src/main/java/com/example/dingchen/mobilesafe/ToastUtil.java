package com.example.dingchen.mobilesafe;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by DIngChen on 2/15/2017.
 */

public class ToastUtil {
    //打印toast
    public static void show(Context ctx,String msg){
        /*
        上下文环境，文本内容
         */
        Toast.makeText(ctx,msg,Toast.LENGTH_SHORT).show();
    }
}
