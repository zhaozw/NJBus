package com.renyu.nj_tran;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.view.Menu;
import android.view.MenuItem;

import com.renyu.nj_tran.common.CommonUtils;
import com.renyu.nj_tran.common.DBUtils;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.Random;

import ItonLifecubeJni.MyAESAlgorithm;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String str = a("line_id=" + 1 + "&line_code=" + 260 + "&updown_type=" + 1 + "&timestamp=" + a());
        byte[] bArr = new byte[str.length()];
        String a = a(32);
        String b = a(a, a(64));
        MyAESAlgorithm.Encrypt(str.getBytes(), a.getBytes(), bArr, str.length());
        str = a(bArr);

        System.out.println("data:"+str);
        System.out.println("key:"+b);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent MessageActivity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
