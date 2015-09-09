package com.renyu.nj_tran.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.renyu.nj_tran.R;
import com.renyu.nj_tran.activity.CurrentPositionActivtiy;
import com.renyu.nj_tran.common.CommonUtils;
import com.renyu.nj_tran.common.DBUtils;
import com.renyu.nj_tran.model.LineModel;
import com.renyu.nj_tran.model.StationModel;
import com.wefika.flowlayout.FlowLayout;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by renyu on 15/8/28.
 */
public class MyLocationAdapter extends RecyclerView.Adapter<MyLocationAdapter.MyLocationHolder> {

    Context context=null;
    ArrayList<StationModel> models=null;

    Handler handler=new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            String[] array=msg.getData().getStringArray("array");
            final ArrayList<LineModel> temps=msg.getData().getParcelableArrayList("temps");
            final int st_id=msg.getData().getInt("st_id");
            final String line_name=msg.getData().getString("line_name");
            new AlertDialog.Builder(context).setTitle("请选择车辆行驶方向").setItems(array, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent=new Intent(context, CurrentPositionActivtiy.class);
                    Bundle bundle=new Bundle();
                    bundle.putParcelable("value", temps.get(which));
                    bundle.putInt("st_id", st_id);
                    bundle.putString("line_name", line_name);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            }).show();
        }
    };

    public MyLocationAdapter(Context context, ArrayList<StationModel> models) {
        this.context=context;
        this.models=models;
    }

    @Override
    public MyLocationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_mylocation, parent, false);
        return new MyLocationHolder(view);
    }

    @Override
    public void onBindViewHolder(MyLocationHolder holder, int position) {
        final int p_=position;
        holder.adapter_mylocation_stationname.setText(models.get(position).getSt_name());
        if (models.get(position).getLineModels().size()==0) {
            holder.adapter_mylocation_nostation.setVisibility(View.VISIBLE);
            holder.adapter_mylocation_flowlayout.setVisibility(View.INVISIBLE);
        }
        else {
            holder.adapter_mylocation_nostation.setVisibility(View.GONE);
            holder.adapter_mylocation_flowlayout.setVisibility(View.VISIBLE);
            holder.adapter_mylocation_flowlayout.removeAllViews();
            for (int i=0;i<models.get(position).getLineModels().size();i++) {
                final LineModel model=models.get(position).getLineModels().get(i);
                TextView view_mylocation_linename= (TextView) LayoutInflater.from(context).inflate(R.layout.view_mylocationtextview, null);
                view_mylocation_linename.setText(model.getLine_name());
                Paint paint=new Paint();
                paint.setTextSize(16 * CommonUtils.getScaleDensity(context));
                //文字宽度+sharp数据
                FlowLayout.LayoutParams params = new FlowLayout.LayoutParams((int) paint.measureText(model.getLine_name())+(int) CommonUtils.getDensity(context)*20, (int) CommonUtils.getDensity(context)*40);
                params.rightMargin = (int) CommonUtils.getDensity(context)*10;
                params.bottomMargin = (int) CommonUtils.getDensity(context)*10;
                view_mylocation_linename.setLayoutParams(params);
                view_mylocation_linename.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CommonUtils.addThread(new Runnable() {
                            @Override
                            public void run() {
                                ArrayList<LineModel> temps = DBUtils.getLineModelDetail(model.getLine_name(), model.getLine_id(), model.getLine_code(), context);
                                String[] array = new String[temps.size()];
                                for (int j = 0; j < temps.size(); j++) {
                                    array[j]=DBUtils.getStationName(temps.get(j).getSt_start_id(), context)+"--"+DBUtils.getStationName(temps.get(j).getSt_end_id(), context);
                                }

                                Message m=new Message();
                                Bundle bundle=new Bundle();
                                bundle.putStringArray("array", array);
                                bundle.putParcelableArrayList("temps", temps);
                                bundle.putInt("st_id", models.get(p_).getSt_id());
                                bundle.putString("line_name", model.getLine_name());
                                m.setData(bundle);
                                handler.sendMessage(m);
                            }
                        });
                    }
                });
                holder.adapter_mylocation_flowlayout.addView(view_mylocation_linename);
            }
        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class MyLocationHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.adapter_mylocation_stationname)
        TextView adapter_mylocation_stationname=null;
        @InjectView(R.id.adapter_mylocation_flowlayout)
        FlowLayout adapter_mylocation_flowlayout=null;
        @InjectView(R.id.adapter_mylocation_nostation)
        TextView adapter_mylocation_nostation=null;

        public MyLocationHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
