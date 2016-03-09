package com.renyu.nj_tran.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.renyu.nj_tran.R;
import com.renyu.nj_tran.model.CurrentPositionModel;
import com.renyu.nj_tran.model.StationModel;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by renyu on 15/8/29.
 */
public class CurrentPositionAdapter extends BaseAdapter {

    final static int STATION_TYPE=0;
    final static int BUS_TYPE=1;

    int st_id=0;
    ArrayList<Object> models=null;
    ArrayList<Integer> hasPassArraylists=null;
    Context context=null;

    public CurrentPositionAdapter(Context context, ArrayList<Object> models, int st_id) {
        this.context=context;
        this.models=models;
        this.st_id=st_id;
    }

    public void setHasPassArraylists(ArrayList<Integer> hasPassArraylists) {
        this.hasPassArraylists=hasPassArraylists;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int position) {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (getItemViewType(position)==STATION_TYPE) {
            CPStationHolder cpStationHolder=null;
            if (convertView==null) {
                convertView=LayoutInflater.from(context).inflate(R.layout.adapter_currentpositionstation, parent, false);
                cpStationHolder=new CPStationHolder(convertView);
                convertView.setTag(cpStationHolder);
            }
            else {
                cpStationHolder= (CPStationHolder) convertView.getTag();
            }
            cpStationHolder.adapter_currentposition_stationnum.setText(""+(((StationModel) models.get(position)).getSt_pos()+1));
            cpStationHolder.adapter_currentposition_stationname.setText(((StationModel) models.get(position)).getSt_name());
            if (((StationModel) models.get(position)).getSt_id()==this.st_id) {
                cpStationHolder.adapter_currentposition_stationname.setTextColor(context.getResources().getColor(android.R.color.holo_blue_dark));
                convertView.setBackgroundColor(Color.WHITE);
            }
            else {
                if (hasPassArraylists.contains(((StationModel) models.get(position)).getSt_id())) {
                    cpStationHolder.adapter_currentposition_stationname.setTextColor(Color.BLACK);
                    convertView.setBackgroundColor(Color.WHITE);
                }
                else {
                    cpStationHolder.adapter_currentposition_stationname.setTextColor(Color.GRAY);
                    convertView.setBackgroundColor(Color.parseColor("#f4f4f4"));
                }
            }
        }
        else if (getItemViewType(position)==BUS_TYPE) {
            CPBusHolder cpBusHolder= null;
            if (convertView==null) {
                convertView=LayoutInflater.from(context).inflate(R.layout.adapter_currentpositionline, parent, false);
                cpBusHolder=new CPBusHolder(convertView);
                convertView.setTag(cpBusHolder);
            }
            else {
                cpBusHolder= (CPBusHolder) convertView.getTag();
            }
            CurrentPositionModel model=((CurrentPositionModel) models.get(position));
            cpBusHolder.adapter_currentposition_line.setText("约" + model.getDis()+"米");
            convertView.setBackgroundColor(Color.WHITE);
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        if (models.get(position) instanceof StationModel) {
            return STATION_TYPE;
        }
        else if (models.get(position) instanceof CurrentPositionModel) {
            return BUS_TYPE;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    public class CPStationHolder {

        @InjectView(R.id.adapter_currentposition_stationname)
        TextView adapter_currentposition_stationname=null;
        @InjectView(R.id.adapter_currentposition_stationnum)
        TextView adapter_currentposition_stationnum;

        public CPStationHolder(View itemView) {
            ButterKnife.inject(this, itemView);
        }
    }

    public class CPBusHolder {

        @InjectView(R.id.adapter_currentposition_line)
        TextView adapter_currentposition_line=null;

        public CPBusHolder(View itemView) {
            ButterKnife.inject(this, itemView);
        }
    }
}
