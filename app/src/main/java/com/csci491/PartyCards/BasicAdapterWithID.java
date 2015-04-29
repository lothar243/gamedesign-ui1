package com.csci491.PartyCards;

import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;



public class BasicAdapterWithID extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<String> data;
    private List<Integer> IDs;

    public BasicAdapterWithID(Context context) {
        mInflater = LayoutInflater.from(context);
        clear();
    }
    public BasicAdapterWithID(Context context, List<String> data, List<Integer> ID) {
        mInflater = LayoutInflater.from(context);
        this.data = data;
        this.IDs = ID;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return IDs.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView == null) {
            view = mInflater.inflate(R.layout.list_item, parent, false);
        } else {
            view = convertView;
        }

        view.setTag(data.get(position));
        TextView listItemTextView = (TextView)view.findViewById(R.id.listItemText);
        listItemTextView.setText(data.get(position));
        Message viewInfo = new Message();
        viewInfo.arg1 = IDs.get(position);
        viewInfo.obj = data.get(position);
        view.setTag(viewInfo);

        return view;
    }

    public void add(String dataString, Integer ID) {
        data.add(dataString);
        IDs.add(ID);
    }

    public void clear() {
        data = new ArrayList<String>();
        IDs = new ArrayList<Integer>();
    }

}