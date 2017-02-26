package com.example.dingchen.mobilesafe;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by DIngChen on 2/14/2017.
 */
public class StreamUtil {
    public static String streamToString(InputStream is) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //读到没有为止
        byte[] buffer = new byte[1024];
        int temp=-1;
        try {
            while ((temp = is.read(buffer)) != -1) {
                bos.write(buffer,0,temp);
            }
            return bos.toString();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try {
                bos.close();
                is.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }
}
