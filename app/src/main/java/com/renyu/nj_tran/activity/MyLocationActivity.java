package com.renyu.nj_tran.activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.amap.api.location.AMapLocalDayWeatherForecast;
import com.amap.api.location.AMapLocalWeatherForecast;
import com.amap.api.location.AMapLocalWeatherListener;
import com.amap.api.location.AMapLocalWeatherLive;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.model.LatLng;
import com.renyu.nj_tran.R;
import com.renyu.nj_tran.adapter.MyLocationAdapter;
import com.renyu.nj_tran.common.CommonUtils;
import com.renyu.nj_tran.common.DBUtils;
import com.renyu.nj_tran.model.StationModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;

/**
 * Created by renyu on 15/8/27.
 */
public class MyLocationActivity extends BaseActivity implements AMapLocationListener, AMapLocalWeatherListener {

    @InjectView(R.id.mylocation_srl)
    SwipeRefreshLayout mylocation_srl=null;
    @InjectView(R.id.mylocation_rv)
    RecyclerView mylocation_rv=null;
    @InjectView(R.id.mylocation_address)
    TextView mylocation_address=null;
    MyLocationAdapter adapter=null;

    LocationManagerProxy proxy=null;

    ArrayList<StationModel> models=null;

    boolean isLoading=false;
    //当前定位到的坐标
    LatLng c_latlng=null;

    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            ArrayList<StationModel> tempList=msg.getData().<StationModel>getParcelableArrayList("value");
            LinkedHashMap<String, StationModel> map=new LinkedHashMap<>();
            for (int i=0;i<tempList.size();i++) {
                if (!map.containsKey(tempList.get(i).getSt_name())) {
                    map.put(tempList.get(i).getSt_name(), tempList.get(i));
                }
            }
            ArrayList<StationModel> temp=new ArrayList<>();
            Iterator<Map.Entry<String, StationModel>> it=map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, StationModel> entry=it.next();
                temp.add(entry.getValue());
            }

            models.clear();
            models.addAll(temp);
            adapter.notifyDataSetChanged();

            mylocation_srl.setRefreshing(false);

            isLoading=false;
        }
    };

    @Override
    public int initContentView() {
        return R.layout.activity_mylocation;
    }

    @Override
    public String initTitle() {
        return "当前位置";
    }

    @Override
    public boolean showArror() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        models=new ArrayList<>();

        proxy=LocationManagerProxy.getInstance(this);
        proxy.setGpsEnable(false);

        CommonUtils.checkUpdate(this);

        init();
    }

    private void init() {
        mylocation_srl.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        mylocation_srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isLoading) {
                    proxy.requestLocationData(LocationProviderProxy.AMapNetwork, 60 * 1000, 15, MyLocationActivity.this);
                }
            }
        });
        mylocation_rv.setLayoutManager(new LinearLayoutManager(this));
        adapter=new MyLocationAdapter(this, models);
        mylocation_rv.setAdapter(adapter);

        mylocation_srl.postDelayed(new Runnable() {
            @Override
            public void run() {
                mylocation_srl.setRefreshing(true);
                proxy.requestLocationData(LocationProviderProxy.AMapNetwork, 60 * 1000, 15, MyLocationActivity.this);
                isLoading = true;
            }
        }, 300);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        proxy.destroy();
    }

    @Override
    public void onLocationChanged(final AMapLocation aMapLocation) {
        if (aMapLocation!=null&&aMapLocation.getAMapException().getErrorCode()==0) {
            System.out.println(aMapLocation.getLatitude()+" "+ aMapLocation.getLongitude());
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String locationTime=format.format(new Date(aMapLocation.getTime()));
            System.out.println(locationTime);
            System.out.println(aMapLocation.getAddress());
            System.out.println(aMapLocation.getProvince()==null?"未知":aMapLocation.getProvince());
            System.out.println(aMapLocation.getCity());
            System.out.println(aMapLocation.getCityCode());
            System.out.println(aMapLocation.getDistrict());
            System.out.println(aMapLocation.getAdCode());
            proxy.requestWeatherUpdates(LocationManagerProxy.WEATHER_TYPE_LIVE, this);
            mylocation_address.setText(aMapLocation.getAddress());
            c_latlng=new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            CommonUtils.addThread(new Runnable() {
                @Override
                public void run() {
                    Message m=new Message();
                    Bundle bundle=new Bundle();
//                    bundle.putParcelableArrayList("value", DBUtils.getNearByStation(MyLocationActivity.this, new LatLng(32.068574, 118.773129)));
                    bundle.putParcelableArrayList("value", DBUtils.getNearByStation(MyLocationActivity.this, new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                    m.setData(bundle);
                    handler.sendMessage(m);
                }
            });

            proxy.removeUpdates(MyLocationActivity.this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onWeatherLiveSearched(AMapLocalWeatherLive aMapLocalWeatherLive) {
        if (aMapLocalWeatherLive!=null&&aMapLocalWeatherLive.getAMapException().getErrorCode()==0) {
            System.out.println(aMapLocalWeatherLive.getWeather());
            System.out.println(aMapLocalWeatherLive.getTemperature());
            System.out.println(aMapLocalWeatherLive.getWindDir());
            System.out.println(aMapLocalWeatherLive.getWindPower());
            System.out.println(aMapLocalWeatherLive.getHumidity());
            System.out.println(aMapLocalWeatherLive.getReportTime());
        }
    }

    @Override
    public void onWeatherForecaseSearched(AMapLocalWeatherForecast aMapLocalWeatherForecast) {
        if (aMapLocalWeatherForecast!=null&&aMapLocalWeatherForecast.getAMapException().getErrorCode()==0) {
            List<AMapLocalDayWeatherForecast> forcasts = aMapLocalWeatherForecast.getWeatherForecast();
            for (int i=0;i<forcasts.size();i++) {
                AMapLocalDayWeatherForecast forecast=forcasts.get(i);

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mylocation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.action_mylocation) {
            if (models!=null&&models.size()>0&&c_latlng!=null) {
                Intent intent=new Intent(MyLocationActivity.this, StationMapActivity.class);
                Bundle bundle=new Bundle();
                bundle.putDouble("lat", c_latlng.latitude);
                bundle.putDouble("lng", c_latlng.longitude);
                bundle.putParcelableArrayList("values", models);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
        else if (item.getItemId()==R.id.action_search) {
            Intent intent=new Intent(MyLocationActivity.this, SearchActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
