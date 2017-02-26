package com.example.dingchen.mobilesafe;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by DIngChen on 2/16/2017.
 */
//能够获取焦点的自定义的TextView
public class FocusTextView extends TextView {
    //通过java代码创建控件
    public FocusTextView(Context context){
        super(context);
    }
    //由系统调用（属性和上下文构建）
    public FocusTextView(Context context, AttributeSet attrs){
        super(context,attrs);
    }
    //由系统调用（属性和上下文构建和布局中自定义文件的构造方法）
    public FocusTextView(Context context, AttributeSet attrs, int defStyle){
        super(context,attrs,defStyle);
    }
    //重写获取焦点的方法


    @Override
    public boolean isFocused() {
        return true;

    }
}
