package com.example.dingchen.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public abstract class SetUpBaseActivity extends AppCompatActivity {
    private  GestureDetector gestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取手势识别器
        gestureDetector = new GestureDetector(this,new MyOnGuestureListener() );

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private class MyOnGuestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //按下的坐标
            float startX = e1.getRawX();
            //抬起的坐标
            float endX = e2.getRawX();
            float startY = e1.getRawY();
            float endY = e2.getRawY();
            if ((Math.abs(startY-endY)) > 50) {
                return false;
            }
            if(startX-endX>100){
                next_activity();
            }if(endX-startX>100){
                pre_activity();
            }

            return true;
        }
    }
    public void prePage(View view){
        pre_activity();
    }
    public void nextPage(View view){
        next_activity();
    }
    public abstract void next_activity();
    //上一步的操作
    public abstract void pre_activity();
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断keycode是否是返回键的标示
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //true:是可以屏蔽按键的事件
            //return true;
            pre_activity();
        }
        return super.onKeyDown(keyCode, event);
    }

}
