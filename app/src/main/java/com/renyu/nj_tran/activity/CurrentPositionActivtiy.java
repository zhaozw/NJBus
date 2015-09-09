package com.renyu.nj_tran.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.renyu.nj_tran.R;
import com.renyu.nj_tran.adapter.CurrentPositionAdapter;
import com.renyu.nj_tran.common.CommonUtils;
import com.renyu.nj_tran.common.DBUtils;
import com.renyu.nj_tran.common.GsonRequest;
import com.renyu.nj_tran.model.CurrentPositionModel;
import com.renyu.nj_tran.model.LineModel;
import com.renyu.nj_tran.model.StationModel;

import java.util.ArrayList;
import java.util.Map;

import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * Created by renyu on 15/8/28.
 */
public class CurrentPositionActivtiy extends BaseActivity {

    LineModel model=null;
    int st_id=0;

    @InjectView(R.id.currentposition_srl)
    SwipeRefreshLayout currentposition_srl=null;
    @InjectView(R.id.currentposition_lv)
    ListView currentposition_lv=null;
    CurrentPositionAdapter adapter=null;

    ArrayList<Object> models=null;
    ArrayList<StationModel> stationModels=null;
    ArrayList<Integer> hasPassArraylists=null;

    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1) {
                stationModels=msg.getData().getParcelableArrayList("value");

                //找到相似之前的都添加进去
                for (int i=0;i<stationModels.size();i++) {
                    if (stationModels.get(i).getSt_id()!=st_id) {
                        hasPassArraylists.add(stationModels.get(i).getSt_id());
                    }
                    else if (stationModels.get(i).getSt_id()==st_id) {
                        break;
                    }
                }
                adapter.setHasPassArraylists(hasPassArraylists);
                models.addAll(stationModels);
                adapter.notifyDataSetChanged();

                post(runnable);
            }
            else if (msg.what==2) {
                ArrayList<CurrentPositionModel.BusesEntity> busesEntityList=msg.getData().getParcelableArrayList("value");
                EventBus.getDefault().post(busesEntityList);
                models.clear();
                for (int i=0;i<stationModels.size();i++) {
                    for (int j=0;j<busesEntityList.size();j++) {
                        if (busesEntityList.get(j).getSt_level()==(i+1)) {
                            models.add(busesEntityList.get(j));
                        }
                    }
                    models.add(stationModels.get(i));
                }
                adapter.notifyDataSetChanged();

                postDelayed(runnable, 10000);

                currentposition_srl.setRefreshing(false);
            }
        }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            currentposition_srl.setRefreshing(true);
            GsonRequest<CurrentPositionModel> request = new GsonRequest<CurrentPositionModel>(Request.Method.POST, "http://testbk.jstv.com/rest4/BusData/getBuses/?line_code=", CurrentPositionModel.class, new Response.Listener<CurrentPositionModel>() {

                @Override
                public void onResponse(final CurrentPositionModel response) {
                    CommonUtils.addThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<CurrentPositionModel.BusesEntity> busesEntityList = (ArrayList<CurrentPositionModel.BusesEntity>) response.getBuses();
//                            for (int i = 0; i < busesEntityList.size(); i++) {
//                                CurrentPositionModel.BusesEntity entity=busesEntityList.get(i);
//                                System.out.println(DBUtils.getStationName(entity.getSt_id(), CurrentPositionActivtiy.this) + " "
//                                        + entity.getOffline() + " " + entity.getSpeed() + " " + entity.getSt_dis() + " " + entity.getSt_level() + " "
//                                        + entity.getSt_real_lat() + " " + entity.getSt_real_lon() + " " + entity.getUpdated());
//                            }
                            Message m=new Message();
                            Bundle bundle=new Bundle();
                            bundle.putParcelableArrayList("value", busesEntityList);
                            m.setData(bundle);
                            m.what=2;
                            handler.sendMessage(m);
                        }
                    });

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    currentposition_srl.setRefreshing(false);
                    System.out.println(error.getMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map=CommonUtils.getKeyAndData(model.getLine_id(), model.getLine_code(), model.getUpdown_type());
                    map.put("client", "android");
                    return map;
                }
            };
            request.setTag("current_position");
            CommonUtils.getVolleyInstance(CurrentPositionActivtiy.this).add(request);
        }
    };

    @Override
    public int initContentView() {
        return R.layout.activtiy_currentposition;
    }

    @Override
    public String initTitle() {
        return getIntent().getExtras().getString("line_name");
    }

    @Override
    public boolean showArror() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model=getIntent().getExtras().getParcelable("value");
        models=new ArrayList<>();
        st_id=getIntent().getExtras().getInt("st_id");
        hasPassArraylists=new ArrayList<>();

        init();
    }

    public void init() {
        currentposition_srl.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        currentposition_srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().post(runnable);
            }
        });
        adapter=new CurrentPositionAdapter(this, models, st_id);
        currentposition_lv.setAdapter(adapter);
        CommonUtils.addThread(new Runnable() {
            @Override
            public void run() {
                Message m = new Message();
                m.what = 1;
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("value", DBUtils.getLineInfo(model.getLine_id(), model.getUpdown_type(), CurrentPositionActivtiy.this));
                m.setData(bundle);
                handler.sendMessage(m);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_currentposition, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.action_cp) {
            Intent intent=new Intent(CurrentPositionActivtiy.this, CurrentPositionMapActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("line_name", getIntent().getExtras().getString("line_name"));
            bundle.putParcelable("value", getIntent().getExtras().getParcelable("value"));
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else if (item.getItemId()==R.id.action_fav) {
            if (DBUtils.isFav(CurrentPositionActivtiy.this, model.getLine_id(), model.getUpdown_type())) {
                item.setIcon(R.mipmap.icon_fav_nor);
                DBUtils.removeFav(this, model.getLine_id(), model.getUpdown_type());
            }
            else {
                item.setIcon(R.mipmap.icon_fav_click);
                DBUtils.addFav(this, model.getLine_id(), model.getUpdown_type(), model.getLine_name());
            }
        }
        else if (item.getItemId()==android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.action_fav);
        if (DBUtils.isFav(CurrentPositionActivtiy.this, model.getLine_id(), model.getUpdown_type())) {
            item.setIcon(R.mipmap.icon_fav_click);
        }
        return super.onPrepareOptionsMenu(menu);
    }
}
