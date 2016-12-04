package com.example.mayur.socketclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by mayur on 12/3/2016.
 */

public class PathListAdapter extends BaseAdapter{

    public List<String> sources;
    public Context context;

    public PathListAdapter(Context context){
        this.context = context;
        sources = new ArrayList<>();
    }

    public void setSources(List<String> sources){
        this.sources = sources;
    }


    public String getPath(int position){
        return sources.get(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.list_item_path, null);


        TextView path = (TextView) convertView.findViewById(R.id.text);
        path.setText(sources.get(position));

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return sources.size();
    }
}
