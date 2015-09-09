package com.renyu.nj_tran.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Environment;
import android.support.v4.view.MotionEventCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ItonLifecubeJni.MyAESAlgorithm;
import de.greenrobot.event.EventBus;

/**
 * Created by renyu on 15/8/25.
 */
public class CommonUtils {

    private static ExecutorService executorService= Executors.newFixedThreadPool(5);

    private static RequestQueue queue=null;

    /**
     * 拷贝本地数据库
     * @param context
     */
    public static void copyDbFile(Context context) {
        File file=new File("/data/data/"+context.getPackageName()+"/"+ParamUtils.dbName);
        if(!file.exists()) {
            CommonUtils.copyAssetsFile(ParamUtils.dbName, file.getPath(), context);
        }
    }

    public static void createTemp(Context context) {
        File file=new File(ParamUtils.appDir);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 通过assets复制文件
     * @param oldName
     * @param newPath
     * @param context
     */
    private static void copyAssetsFile(String oldName, String newPath, Context context) {
        AssetManager manager=context.getAssets();
        try {
            int byteread=0;
            InputStream inStream=manager.open(oldName);
            FileOutputStream fs=new FileOutputStream(newPath);
            byte[] buffer=new byte[1024];
            while ((byteread = inStream.read(buffer))!=-1) {
                fs.write(buffer, 0, byteread);
            }
            inStream.close();
            fs.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static RequestQueue getVolleyInstance(Context context) {
        synchronized (CommonUtils.class) {
            if (queue==null) {
                synchronized (CommonUtils.class) {
                    queue= Volley.newRequestQueue(context.getApplicationContext());
                }
            }
        }
        return queue;
    }

    public static float getDensity(Context context) {
        WindowManager manager= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm=new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        return dm.density;
    }

    public static float getScaleDensity(Context context) {
        WindowManager manager= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm=new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        return dm.scaledDensity;
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static void checkUpdate(final Context context) {
        Request<String> stringRequest=new StringRequest(Request.Method.GET, "http://nanjing.sinaapp.com/main.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String result=new String(response.toString().getBytes("iso-8859-1"), "utf-8");
                    int versionCode=Integer.parseInt(result.split("-")[0]);
                    if(versionCode>CommonUtils.getVersionCode(context)) {
                        ParamUtils.isShowUpdate=true;
                        EventBus.getDefault().post(result);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        getVolleyInstance(context).add(stringRequest);
    }

    /**
     * 获取版本号
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        try {
            PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 检测apk包状态
     * @param context
     * @param path
     * @return
     */
    public static boolean checkAPKState(Context context, String path) {
        PackageInfo pi=null;
        try {
            PackageManager pm=context.getPackageManager();
            pi=pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
            if(pi==null) {
                File file=new File(path);
                if(file.exists()) {
                    file.delete();
                }
            }
            return pi==null?false:true;
        } catch(Exception e) {
            if(pi==null) {
                File file=new File(path);
                if(file.exists()) {
                    file.delete();
                }
            }
            return false;
        }
    }

    public static void addThread(Runnable runnable) {
        executorService.submit(runnable);
    }

    /**
     * 公交车当前位置信息请求参数
     * @param line_id
     * @param line_code
     * @param updown_type
     * @return
     */
    public static HashMap<String, String> getKeyAndData(int line_id, int line_code, int updown_type) {
        String str = a("line_id=" + line_id + "&line_code=" + line_code + "&updown_type=" + updown_type + "&timestamp=" + a());
        byte[] bArr = new byte[str.length()];
        String a = a(32);
        String b = a(a, a(64));
        MyAESAlgorithm.Encrypt(str.getBytes(), a.getBytes(), bArr, str.length());
        str = a(bArr);

        System.out.println("data:"+str);
        System.out.println("key:"+b);

        HashMap<String, String> map=new HashMap<>();
        map.put("data", str);
        map.put("key", b);

        return map;
    }

    public static String a(String str) {
        if (str.length() % 16 == 0) {
            return str;
        }
        int length = (((str.length() / 16) + 1) * 16) - str.length();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            stringBuffer.append('*');
        }
        return new StringBuilder(String.valueOf(str)).append(stringBuffer.toString()).toString();
    }

    public static int a() {
        return ((int) (System.currentTimeMillis() / 1000)) - 3;
    }

    public static String a(int i) {
        String str = "";
        Random random = new Random();
        String str2 = str;
        for (int i2 = 0; i2 < i; i2++) {
            str = random.nextInt(2) % 2 == 0 ? "char" : "num";
            if ("char".equalsIgnoreCase(str)) {
                str2 = new StringBuilder(String.valueOf(str2)).append((char) (random.nextInt(26) + (random.nextInt(2) % 2 == 0 ? 65 : 97))).toString();
            } else if ("num".equalsIgnoreCase(str)) {
                str2 = new StringBuilder(String.valueOf(str2)).append(String.valueOf(random.nextInt(10))).toString();
            }
        }
        return str2;
    }

    public static String a(byte[] bArr) {
        char[] a = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
        StringBuffer stringBuffer = new StringBuffer();
        int length = bArr.length;
        int i = 0;
        while (i < length) {
            int i2 = i + 1;
            int i3 = bArr[i] & MotionEventCompat.ACTION_MASK;
            if (i2 == length) {
                stringBuffer.append(a[i3 >>> 2]);
                stringBuffer.append(a[(i3 & 3) << 4]);
                stringBuffer.append("==");
                break;
            }
            int i4 = i2 + 1;
            i2 = bArr[i2] & MotionEventCompat.ACTION_MASK;
            if (i4 == length) {
                stringBuffer.append(a[i3 >>> 2]);
                stringBuffer.append(a[((i3 & 3) << 4) | ((i2 & 240) >>> 4)]);
                stringBuffer.append(a[(i2 & 15) << 2]);
                stringBuffer.append("=");
                break;
            }
            i = i4 + 1;
            i4 = bArr[i4] & MotionEventCompat.ACTION_MASK;
            stringBuffer.append(a[i3 >>> 2]);
            stringBuffer.append(a[((i3 & 3) << 4) | ((i2 & 240) >>> 4)]);
            stringBuffer.append(a[((i2 & 15) << 2) | ((i4 & 192) >>> 6)]);
            stringBuffer.append(a[i4 & 63]);
        }
        return stringBuffer.toString();
    }

    public static String a(String str, String str2) {
        char[] toCharArray = str.toCharArray();
        char[] toCharArray2 = str2.toCharArray();
        for (int i = 0; i < toCharArray.length; i++) {
            if (i < 16) {
                toCharArray2[(i * 2) + 1] = toCharArray[i];
            } else {
                toCharArray2[i * 2] = toCharArray[i];
            }
        }
        return new String(toCharArray2);
    }
}
