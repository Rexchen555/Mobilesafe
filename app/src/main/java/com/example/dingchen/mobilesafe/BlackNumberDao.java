package com.example.dingchen.mobilesafe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DIngChen on 2/24/2017.
 */

public class BlackNumberDao {
    //单例模式
    private BlackNumberOpenHelper blackNumberOpenHelper;
    //私有构造方法
    private BlackNumberDao(Context context){
        //创建数据库以及其表结构
         blackNumberOpenHelper = new BlackNumberOpenHelper(context);
    }

    //申明一个当前类的对象
    private static BlackNumberDao blackNumberDao = null;
    //如果当前类的对象为空，创建一个新的
    public static BlackNumberDao getInstance(Context context){
        if(blackNumberDao == null){
            blackNumberDao = new BlackNumberDao(context);
        }
        return blackNumberDao;
    }
    //增加一个条目
    //拦截的电话号码和类型
    public void insert(String phone, String mode){
        //开启数据库，准备写入操作
        SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("phone",phone);
        db.insert("blacknumber",null,values);
        db.close();
    }
    //从数据库中删除数据
    public void delete(String phone){
        SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
        db.delete("blacknumber","phone = ?",new String[]{phone});
        db.close();
    }
    //改数据
    public void update(String phone, String mode){
        SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("mode",mode);
        db.update("blacknumber",contentValues,"phone = ?",new String[]{});
        db.close();
    }
    //查询所有的号码以及类型的集合
    public List<BlackNumberInfo> findAll(){
        SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("blacknumber",new String[]{"phone","mode"},null,null,null,null,"_id desc");
        List<BlackNumberInfo> blackNumberList = new ArrayList<BlackNumberInfo>();
        while(cursor.moveToNext()){
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
            blackNumberInfo.phone = cursor.getString(0);
            blackNumberInfo.mode = cursor.getString(1);
            blackNumberList.add(blackNumberInfo);
        }
        cursor.close();
        db.close();
        return blackNumberList;
    }



}
