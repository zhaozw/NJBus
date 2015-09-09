package com.renyu.nj_tran.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.baidu.mobstat.StatService;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.renyu.nj_tran.R;
import com.renyu.nj_tran.common.ParamUtils;

import net.sqlcipher.database.SQLiteDatabase;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * Created by renyu on 15/8/27.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public abstract int initContentView();

    public abstract String initTitle();

    public abstract boolean showArror();

    @InjectView(R.id.toolbar_view)
    Toolbar toolbar_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initContentView());
        ButterKnife.inject(this);

        SQLiteDatabase.loadLibs(this);

        SwipeBackHelper.onCreate(this);

        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(true)
                .setSwipeEdgePercent(0.5f)
                .setSwipeSensitivity(0.5f)
                .setClosePercent(0.5f)
                .setSwipeRelateEnable(true).setSwipeSensitivity(1);

        initViews();
    }

    private void initViews() {
        if (toolbar_view!=null) {
            toolbar_view.setTitle(initTitle());
            if (showArror()) {
                toolbar_view.setNavigationIcon(R.mipmap.ic_back);
            }
            toolbar_view.setTitleTextColor(Color.WHITE);
            setSupportActionBar(toolbar_view);
            if (showArror()) {
                getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        StatService.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }

    public void onEventMainThread(final String updateString) {
        ParamUtils.isShowUpdate=false;

        Intent intent=new Intent(BaseActivity.this, TipActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("updateString", updateString);
        intent.putExtras(bundle);
        startActivity(intent);

    }
}
