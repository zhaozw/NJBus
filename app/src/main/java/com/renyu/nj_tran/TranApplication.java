package com.renyu.nj_tran;

import android.app.Application;

import com.renyu.nj_tran.common.CommonUtils;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by renyu on 15/8/27.
 */
public class TranApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        JPushInterface.setDebugMode(false); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush

        CommonUtils.copyDbFile(this);
        CommonUtils.createTemp(this);
    }
}
