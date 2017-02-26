package com.example.dingchen.mobilesafe;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by DIngChen on 2/23/2017.
 */

public class AddressDao {
    private static String mAddress = "unknow number";
    //指定访问数据库的类型
    public static String path = "data/data/com.example.dingchen.mobilesafe/files/address.db";
    //开启数据库的链接，行进访问

    public static String getAddress(String phone){
        String regularException = "^1[3-8]\\d{9}";
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READONLY);
        if(phone.matches(regularException)){
            phone = phone.substring(0,7);

            Cursor cursor = db.query("data1", new String[]{"outkey"}, "id = ?", new String[]{phone}, null, null, null);

            if(cursor.moveToNext()){
                String outkey = cursor.getString(0);
                //通过表一查询到的结果作为外键，查询表二
                Cursor indexCursor = db.query("data2",new String[]{"location"},"id=?",new String[]{outkey},null,null,null);
                if(indexCursor.moveToNext()){
                    //获取查询到的电话归属地
                     mAddress = indexCursor.getString(0);
                }
        }else{
                mAddress = "unknow number";
            }

        }else{
            int length = phone.length();
            switch (length){
                case 3:
                    mAddress = "No";
                    break;
                case 4:
                    mAddress = "No";
                    break;
                case  5:
                    mAddress = "No";
                    break;
                case 7:
                    mAddress = "No";
                    break;
                case 8:
                    mAddress = "No";
                    break;
                case 11:
                    String area = phone.substring(1,3);
                    Cursor cursor = db.query("data2",new String[]{"location"},"area = ?",new String[]{area},null,null,null);
                    if (cursor.moveToNext()){
                        mAddress = cursor.getString(0);
                    }else {
                        mAddress = "unknow";
                    }
                    break;
                case 12:
                    String area1 = phone.substring(1,4);
                    Cursor cursor1 = db.query("data2",new String[]{"location"},"area = ?",new String[]{area1},null,null,null);
                    if (cursor1.moveToNext()){
                        mAddress = cursor1.getString(0);
                    }else {
                        mAddress = "unknow";
                    }
                    break;
            }
        }
        return mAddress;
    }

}
