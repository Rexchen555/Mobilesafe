package com.example.dingchen.mobilesafe;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by DIngChen on 2/17/2017.
 */

public class SettingItemView extends RelativeLayout {
    private CheckBox cb_box;
    private  TextView tv_des;
    private static final String NAMESPACE = "http://schemas.android.com/apk/res/com.example.dingchen.mobilesafe";
    private String mDestitle;
    private String mDesoff;
    private String mDeson;
    public SettingItemView(Context context){
        this(context,null);
    }
    //由系统调用（属性和上下文构建）
    public SettingItemView(Context context, AttributeSet attrs){

        this(context,attrs,0);
    }
    //由系统调用（属性和上下文构建和布局中自定义文件的构造方法）
    public SettingItemView(Context context, AttributeSet attrs, int defStyle){
        super(context,attrs,defStyle);
        //xml->view
        View.inflate(context,R.layout.setting_item_voiew,this);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_des = (TextView) findViewById(R.id.tv_des);
        cb_box = (CheckBox) findViewById(R.id.cb_box);

        initAttrs(attrs);
        tv_title.setText(mDestitle);
    }
    //返回自定义属性里的属性值
    private void initAttrs(AttributeSet attrs) {
        mDestitle = attrs.getAttributeValue(NAMESPACE ,"destitle");
        mDesoff = attrs.getAttributeValue(NAMESPACE ,"desoff");
        mDeson = attrs.getAttributeValue(NAMESPACE ,"deson");
    }

    //返回当前SettingItemView是否是选中状态 true是开启，false是关闭
        public boolean isCheck(){
            return cb_box.isChecked();
        }
    //由点击过程中传递
    public void setCheck(boolean isCheck){
        cb_box.setChecked(isCheck);
        if(isCheck){
            //开启
            tv_des.setText(mDeson);
        }else{
            //关闭
            tv_des.setText(mDesoff);
    }
    }


}
