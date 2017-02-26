package com.example.dingchen.mobilesafe;

import java.security.MessageDigest;

/**
 * Created by DIngChen on 2/19/2017.
 */

public class Md5Util {
    public static String encoder(String psd) {
        // TODO Auto-generated method stub
        try{
            //加盐
            psd = psd+"mobilesafe";
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bs = digest.digest(psd.getBytes());
            System.out.println(bs.length);
            StringBuffer stringBuffer = new StringBuffer();
            for(byte b:bs){
                int i = b & 0xff;
                String hexString = Integer.toHexString(i);
                if(hexString.length()<2){
                    hexString = "0"+hexString;
                }
                stringBuffer.append(hexString);
            }
            return stringBuffer.toString();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

}
