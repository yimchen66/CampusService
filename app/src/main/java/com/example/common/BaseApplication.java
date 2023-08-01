package com.example.common;

import android.app.Application;

import org.litepal.LitePalApplication;

import com.xuexiang.xui.XUI;

public class BaseApplication extends LitePalApplication {

    @Override
    public void onCreate() {


        super.onCreate();
        XUI.init(this); //初始化UI框架
        XUI.debug(true);  //开启UI框架调试日志
    }
}
