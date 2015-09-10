package com.renyu.nj_tran.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.renyu.nj_tran.R;
import com.renyu.nj_tran.model.StationModel;

import java.util.ArrayList;

import butterknife.InjectView;

/**
 * Created by renyu on 15/8/27.
 */
public class StationMapActivity extends BaseActivity implements AMap.OnCameraChangeListener, AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener, AMap.OnMarkerDragListener, AMap.OnMapLoadedListener, AMap.InfoWindowAdapter {

    @InjectView(R.id.station_mapview)
    MapView station_mapview=null;
    AMap aMap=null;

    ArrayList<StationModel> models=null;

    @Override
    public int initContentView() {
        return R.layout.activity_stationmap;
    }

    @Override
    public String initTitle() {
        return "当前位置";
    }

    @Override
    public boolean showArror() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);

        models=getIntent().getExtras().getParcelableArrayList("values");

        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        station_mapview.onCreate(savedInstanceState);
        aMap=station_mapview.getMap();
        aMap.setOnMarkerClickListener(this);
        aMap.setOnInfoWindowClickListener(this);
        aMap.setOnMarkerDragListener(this);
        aMap.setOnMapLoadedListener(this);
        aMap.setInfoWindowAdapter(this);
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
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        station_mapview.onSaveInstanceState(outState);
    }

    private void changeCamera(boolean animate, LatLng latLng) {
        CameraUpdate cameraUpdate=CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 16, 0, 30));
        if (animate) {
            aMap.animateCamera(cameraUpdate);
        }
        else {
            aMap.moveCamera(cameraUpdate);
        }
    }

    private void addMarker(double lat, double lng, String title, String snippet, int zIndex) {
        View view_station_layout= LayoutInflater.from(this).inflate(R.layout.view_station, null);
        TextView view_station_textview = (TextView) view_station_layout.findViewById(R.id.view_station_textview);
        view_station_textview.setText(title);

        MarkerOptions options = new MarkerOptions().anchor(0.5f, 0.5f).position(new LatLng(lat, lng)).title(title).snippet(snippet).draggable(true).icon(BitmapDescriptorFactory.fromView(view_station_layout)).zIndex(zIndex);
        aMap.addMarker(options);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

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

    @Override
    public void onMapLoaded() {
        //设置中心点
        changeCamera(false, new LatLng(getIntent().getExtras().getDouble("lat"), getIntent().getExtras().getDouble("lng")));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < models.size(); i++) {
                    addMarker(models.get(i).getSt_real_lat(), models.get(i).getSt_real_lon(), models.get(i).getSt_name(), models.get(i).getSt_name(), i);
                }
            }
        }, 1000);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view=getLayoutInflater().inflate(R.layout.view_infowindow, null);
        render(marker, view);
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    private void render(Marker marker, View view) {
        String snippet=marker.getSnippet();
        TextView infowindow_text= (TextView) view.findViewById(R.id.infowindow_text);
        infowindow_text.setText(snippet);
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
//        System.out.println(cameraPosition.target.latitude+" "+cameraPosition.target.longitude);
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
//        System.out.println(cameraPosition.target.latitude+" "+cameraPosition.target.longitude);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
