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
import com.android.volley.toolbox.StringRequest;
import com.renyu.nj_tran.R;
import com.renyu.nj_tran.adapter.CurrentPositionAdapter;
import com.renyu.nj_tran.common.CommonUtils;
import com.renyu.nj_tran.common.DBUtils;
import com.renyu.nj_tran.common.OKHttpHelper;
import com.renyu.nj_tran.model.CurrentPositionModel;
import com.renyu.nj_tran.model.JsonParse;
import com.renyu.nj_tran.model.LineModel;
import com.renyu.nj_tran.model.StationModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    ArrayList<CurrentPositionModel> busLists=null;

    OKHttpHelper httpHelper=new OKHttpHelper();

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
                busLists= (ArrayList<CurrentPositionModel>) msg.getData().getSerializable("value");
                EventBus.getDefault().post(busLists);
                models.clear();
                for (int i=0;i<stationModels.size();i++) {
                    for (int j=0;j<busLists.size();j++) {
                        if (busLists.get(j).getLevel()==(i+1)) {
                            models.add(busLists.get(j));
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
            StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://bus.inj100.jstv.com/real_a", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    ArrayList<CurrentPositionModel> models=new ArrayList<>();
                    try {
                        JSONArray array=new JSONArray(response);
                        for (int i=0;i<array.length();i++) {
                            CurrentPositionModel model=new CurrentPositionModel();
                            CurrentPositionModel.GpsEntity entity=new CurrentPositionModel.GpsEntity();
                            JSONObject object=array.getJSONObject(i);
                            model.setDis(object.getDouble("Dis"));
                            model.setId(object.getString("Id"));
                            model.setLevel(object.getInt("Level"));
                            model.setOff(object.getInt("Off"));
                            model.setSpd(object.getString("Spd"));
                            model.setT(object.getInt("T"));
                            entity.setLat(object.getJSONObject("Gps").getDouble("lat"));
                            entity.setLon(object.getJSONObject("Gps").getDouble("lon"));
                            model.setGps(entity);
                            models.add(model);
                        }
                        Message m=new Message();
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("value", models);
                        m.setData(bundle);
                        m.what=2;
                        handler.sendMessage(m);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        currentposition_srl.setRefreshing(false);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    currentposition_srl.setRefreshing(false);
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = CommonUtils.getKeyAndData(model.getLine_id(), model.getLine_code(), model.getUpdown_type());
                    map.put("client", "android");
                    return map;
                }
            };
            stringRequest.setTag("current_position");
            CommonUtils.getVolleyInstance(CurrentPositionActivtiy.this).add(stringRequest);
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
            if (busLists!=null&&busLists.size()>0) {
                bundle.putSerializable("busLists", busLists);
            }
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
