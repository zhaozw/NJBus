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
import com.renyu.nj_tran.common.CommonUtils;
import com.renyu.nj_tran.common.OKHttpHelper;
import com.renyu.nj_tran.common.ParamUtils;

import java.io.File;

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

    int currentProcess=0;

    OKHttpHelper httpHelper=new OKHttpHelper();

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
                CommonUtils.showNotification(getApplicationContext());
                Toast.makeText(TipActivity.this, "开始下载", Toast.LENGTH_SHORT).show();
                httpHelper.downloadFile(updateString.split("-")[2], ParamUtils.appDir, new OKHttpHelper.RequestListener() {
                    @Override
                    public void onSuccess(String string) {
                        Toast.makeText(TipActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
                        //安装软件
                        Intent intent=new Intent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setAction(Intent.ACTION_VIEW);
                        String type="application/vnd.android.package-archive";
                        intent.setDataAndType(Uri.fromFile(new File(string)), type);
                        startActivity(intent);
                        CommonUtils.cancelNotification(TipActivity.this);
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(TipActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                        CommonUtils.cancelNotification(TipActivity.this);
                    }
                }, new OKHttpHelper.ProgressListener() {
                    @Override
                    public void updateprogress(int progress, long bytesRead, long contentLength) {
                        if ((int) ((100 * bytesRead) / contentLength)/10>currentProcess) {
                            currentProcess=(int) ((100 * bytesRead) / contentLength)/10;
                            CommonUtils.updateNotification(getApplicationContext(), (int) ((100 * bytesRead) / contentLength), 100);
                        }
                    }
                });
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
