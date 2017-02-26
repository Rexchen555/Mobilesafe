package com.example.dingchen.mobilesafe;

/**
 * Created by DIngChen on 2/24/2017.
 */

public class BlackNumberInfo {
    public String mode;
    public String phone;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "BlackNumberInfo{" +
                "mode='" + mode + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
