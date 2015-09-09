package com.renyu.nj_tran.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.PolylineOptions;
import com.renyu.nj_tran.R;
import com.renyu.nj_tran.common.DBUtils;
import com.renyu.nj_tran.model.CurrentPositionModel;
import com.renyu.nj_tran.model.LineModel;
import com.renyu.nj_tran.model.StationModel;

import java.util.ArrayList;

import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * Created by renyu on 15/9/6.
 */
public class CurrentPositionMapActivity extends BaseActivity implements AMap.OnCameraChangeListener, AMap.OnMarkerClickListener, AMap.OnMarkerDragListener, AMap.OnMapLoadedListener {

    @InjectView(R.id.station_mapview)
    MapView station_mapview;
    AMap aMap=null;

    ArrayList<StationModel> stationModels=null;

    boolean isDrawComp=false;

    ArrayList<Marker> markers=null;

    @Override
    public int initContentView() {
        return R.layout.activity_stationmap;
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

        LineModel model=getIntent().getExtras().getParcelable("value");
        stationModels= DBUtils.getLineInfo(model.getLine_id(), model.getUpdown_type(), CurrentPositionMapActivity.this);
        markers=new ArrayList<>();

        initView(savedInstanceState);

        EventBus.getDefault().register(CurrentPositionMapActivity.this);
    }

    private void initView(Bundle savedInstanceState) {
        station_mapview.onCreate(savedInstanceState);
        aMap=station_mapview.getMap();
        aMap.setOnMarkerClickListener(this);
        aMap.setOnMarkerDragListener(this);
        aMap.setOnMapLoadedListener(this);
        aMap.setOnCameraChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        station_mapview.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        station_mapview.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        station_mapview.onDestroy();

        EventBus.getDefault().unregister(CurrentPositionMapActivity.this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        station_mapview.onSaveInstanceState(outState);
    }

    private void changeCamera(boolean animate, LatLng latLng) {
        CameraUpdate cameraUpdate= CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 14, 0, 30));
        if (animate) {
            aMap.animateCamera(cameraUpdate);
        }
        else {
            aMap.moveCamera(cameraUpdate);
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {

    }

    private void addStationMarker(double lat, double lng, String title, String snippet, int zIndex) {
        MarkerOptions options = new MarkerOptions().anchor(0.5f, 0.5f).position(new LatLng(lat, lng)).title(title).snippet(snippet).draggable(true).icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_station_2)).zIndex(zIndex);
        aMap.addMarker(options);
    }

    private void addBusMarker(double lat, double lng, String title, String snippet, int zIndex) {
        MarkerOptions options = new MarkerOptions().anchor(0.5f, 0.5f).position(new LatLng(lat, lng)).title(title).snippet(snippet).draggable(true).icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_bus)).zIndex(zIndex);
        Marker marker=aMap.addMarker(options);
        markers.add(marker);
    }

    @Override
    public void onMapLoaded() {
        double start_lat=stationModels.get(0).getSt_real_lat();
        double start_lng=stationModels.get(0).getSt_real_lon();

        double end_lat=stationModels.get(stationModels.size() - 1).getSt_real_lat();
        double end_lng=stationModels.get(stationModels.size()-1).getSt_real_lon();

        //设置中心点
        changeCamera(false, new LatLng((start_lat + end_lat) / 2, (end_lng + start_lng) / 2));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                PolylineOptions options=new PolylineOptions();
                for (int i = 0; i < stationModels.size(); i++) {
                    options.add(new LatLng(stationModels.get(i).getSt_real_lat(), stationModels.get(i).getSt_real_lon()));
                    addStationMarker(stationModels.get(i).getSt_real_lat(), stationModels.get(i).getSt_real_lon(), stationModels.get(i).getSt_name(), stationModels.get(i).getSt_name(), i);
                }
                aMap.addPolyline(options.color(Color.BLACK)).setGeodesic(true);
                isDrawComp=true;
            }
        }, 300);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }

    public void onEventMainThread(ArrayList<CurrentPositionModel.BusesEntity> busesEntityList) {
        if (isDrawComp) {
            for (int i=0;i<markers.size();i++) {
                markers.get(i).remove();
            }
            for (int i=0;i<busesEntityList.size();i++) {
                addBusMarker(busesEntityList.get(i).getSt_real_lat(), busesEntityList.get(i).getSt_real_lon(), "", "", 100+i);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
