package com.renyu.nj_tran.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.renyu.nj_tran.R;
import com.renyu.nj_tran.common.ParamUtils;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by renyu on 15/9/9.
 */
public class TipActivity extends Activity {

    @InjectView(R.id.custom_title)
    TextView custom_title;
    @InjectView(R.id.custom_content)
    TextView custom_content;
    @InjectView(R.id.custom_negativeButton)
    Button custom_negativeButton;
    @InjectView(R.id.custom_positiveButton)
    Button custom_positiveButton;

    String updateString="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_material_dialogs);
        ButterKnife.inject(this);

        updateString=getIntent().getExtras().getString("updateString");

        init();
    }

    private void init() {
        custom_title.setText("升级提示");
        custom_content.setText(updateString.split("-")[1]);
        custom_negativeButton.setText("取消");
        custom_negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        custom_positiveButton.setText("确定");
        custom_positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(TipActivity.this, "开始下载", Toast.LENGTH_SHORT).show();
                    }
                });
                OkHttpClient client=new OkHttpClient();
                Request request=new Request.Builder().url(updateString.split("-")[2]).build();
                Call call=client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(TipActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        InputStream is=response.body().byteStream();
                        final File file=new File(ParamUtils.appDir+File.separator+"Nj_tran.apk");
                        if (file.exists()) {
                            file.delete();
                        }
                        else {
                            file.createNewFile();
                        }
                        FileOutputStream fos=new FileOutputStream(file);
                        int count=0;
                        byte[] buffer=new byte[1024];
                        while ((count=is.read(buffer))!=-1) {
                            fos.write(buffer, 0, count);
                        }
                        is.close();
                        fos.close();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(TipActivity.this, "下载成功", Toast.LENGTH_SHORT).show();

                                //安装软件
                                Intent intent=new Intent();
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setAction(Intent.ACTION_VIEW);
                                String type="application/vnd.android.package-archive";
                                intent.setDataAndType(Uri.fromFile(file), type);
                                startActivity(intent);
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
