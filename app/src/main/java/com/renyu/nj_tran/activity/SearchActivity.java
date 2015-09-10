package com.renyu.nj_tran.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.renyu.nj_tran.R;
import com.renyu.nj_tran.adapter.SearchAdapter;
import com.renyu.nj_tran.common.CommonUtils;
import com.renyu.nj_tran.common.DBUtils;
import com.renyu.nj_tran.model.LineModel;
import com.renyu.nj_tran.model.StationModel;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.InjectView;

/**
 * Created by renyu on 15/9/8.
 */
public class SearchActivity extends BaseActivity {

    @InjectView(R.id.search_lv)
    SwipeMenuListView search_lv;
    @InjectView(R.id.search_result_lv)
    ListView search_result_lv=null;
    @InjectView(R.id.search_edit)
    EditText search_edit;

    SearchAdapter adapter=null;
    SearchAdapter adapter2=null;

    ArrayList<LineModel> models=null;
    ArrayList<LineModel> models_search=null;

    @Override
    public int initContentView() {
        return R.layout.activity_search;
    }

    @Override
    public String initTitle() {
        return "搜索";
    }

    @Override
    public boolean showArror() {
        return true;
    }

    Handler handler=new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            String[] array=msg.getData().getStringArray("array");
            final ArrayList<LineModel> temps=msg.getData().getParcelableArrayList("temps");
            final int st_id=msg.getData().getInt("st_id");
            final String line_name=msg.getData().getString("line_name");
            new AlertDialog.Builder(SearchActivity.this).setTitle("请选择车辆行驶方向").setItems(array, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent=new Intent(SearchActivity.this, CurrentPositionActivtiy.class);
                    Bundle bundle=new Bundle();
                    bundle.putParcelable("value", temps.get(which));
                    bundle.putInt("st_id", st_id);
                    bundle.putString("line_name", line_name);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        models=new ArrayList<>();
        models_search=new ArrayList<>();

        init();

    }

    private void init() {

        SwipeMenuCreator creator=new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu swipeMenu) {
                SwipeMenuItem item=new SwipeMenuItem(SearchActivity.this);
                item.setBackground(new ColorDrawable(Color.RED));
                item.setWidth(CommonUtils.dip2px(SearchActivity.this, 90));
                item.setTitle("删除");
                item.setTitleColor(Color.WHITE);
                item.setTitleSize(CommonUtils.sp2px(SearchActivity.this, 5));
                swipeMenu.addMenuItem(item);
            }
        };
        search_lv.setMenuCreator(creator);
        search_lv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int i, SwipeMenu swipeMenu, int i1) {
                return false;
            }
        });
        search_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchActivity.this, CurrentPositionActivtiy.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("value", models.get(position));
                bundle.putInt("st_id", -1);
                bundle.putString("line_name", models.get(position).getLine_name());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        adapter=new SearchAdapter(this, models);
        search_lv.setAdapter(adapter);

        search_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    search_result_lv.setVisibility(View.GONE);
                    search_lv.setVisibility(View.VISIBLE);
                } else {
                    search_result_lv.setVisibility(View.VISIBLE);
                    search_lv.setVisibility(View.GONE);

                    boolean isOK=true;
                    Pattern pattern=Pattern.compile("^[a-zA-Z0-9\\u4E00-\\u9fa5]+$");
                    for (int i=0;i<s.toString().length();i++) {
                        Matcher matcher=pattern.matcher(s.toString());
                        if (!matcher.find()) {
                            isOK=false;
                            break;
                        }
                    }
                    if (!isOK) {
                        return;
                    }
                    models_search.clear();
                    models_search.addAll(DBUtils.getSearchResult(SearchActivity.this, s.toString()));
                    adapter2.notifyDataSetChanged();
                }
            }
        });

        adapter2=new SearchAdapter(this, models_search);
        search_result_lv.setAdapter(adapter2);
        search_result_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                CommonUtils.addThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<LineModel> temps = DBUtils.getLineModelDetail(models_search.get(position).getLine_name(), models_search.get(position).getLine_id(), models_search.get(position).getLine_code(), SearchActivity.this);
                        String[] array = new String[temps.size()];
                        for (int j = 0; j < temps.size(); j++) {
                            array[j] = DBUtils.getStationName(temps.get(j).getSt_start_id(), SearchActivity.this) + "--" + DBUtils.getStationName(temps.get(j).getSt_end_id(), SearchActivity.this);
                        }

                        Message m = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putStringArray("array", array);
                        bundle.putParcelableArrayList("temps", temps);
                        bundle.putInt("st_id", -1);
                        bundle.putString("line_name", models_search.get(position).getLine_name());
                        m.setData(bundle);
                        handler.sendMessage(m);
                    }
                });
            }
        });

    }

    @NonNull
    private ArrayList<LineModel> getLineModels() {
        ArrayList<LineModel> lineModels= DBUtils.getFav(this);
        for (int i=0;i<lineModels.size();i++) {
            ArrayList<StationModel> getLineInfo=DBUtils.getLineInfo(lineModels.get(i).getLine_id(), lineModels.get(i).getUpdown_type(), this);
            lineModels.get(i).setBus_start(getLineInfo.get(0).getSt_name());
            lineModels.get(i).setBus_end(getLineInfo.get(getLineInfo.size()-1).getSt_name());
        }
        return lineModels;
    }

    @Override
    protected void onResume() {
        super.onResume();

        ArrayList<LineModel> lineModels = getLineModels();
        models.clear();
        models.addAll(lineModels);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
