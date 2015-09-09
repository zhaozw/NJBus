package com.renyu.nj_tran.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.renyu.nj_tran.R;
import com.renyu.nj_tran.model.LineModel;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by renyu on 15/9/8.
 */
public class SearchAdapter extends BaseAdapter {

    Context context=null;
    ArrayList<LineModel> models=null;

    public SearchAdapter(Context context, ArrayList<LineModel> models) {
        this.context=context;
        this.models=models;
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
        SearchHolder holder=null;
        if (convertView==null) {
            convertView= LayoutInflater.from(context).inflate(R.layout.adapter_search, parent, false);
            holder=new SearchHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder= (SearchHolder) convertView.getTag();
        }
        if (models.get(position).getBus_start().equals("")||models.get(position).getBus_end().equals("")) {
            holder.adapter_search_text.setText(models.get(position).getLine_name());
        }
        else {
            holder.adapter_search_text.setText(models.get(position).getLine_name()+"（"+models.get(position).getBus_start()+"-"+models.get(position).getBus_end()+"）");
        }
        return convertView;
    }

    class SearchHolder {

        @InjectView(R.id.adapter_search_text)
        TextView adapter_search_text=null;

        public SearchHolder(View convertView) {
            ButterKnife.inject(this, convertView);
        }
    }
}
