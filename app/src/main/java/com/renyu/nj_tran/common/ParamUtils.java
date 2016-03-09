package com.renyu.nj_tran.common;

import android.os.Environment;

import java.io.File;

/**
 * Created by renyu on 15/8/28.
 */
public class ParamUtils {
    public static final String dbName="ibus_20160217.db";
    public static final String appDir= Environment.getExternalStorageDirectory().getPath()+ File.separator+"nj_tran";

    //是否显示升级提示框
    public static boolean isShowUpdate=false;
}
