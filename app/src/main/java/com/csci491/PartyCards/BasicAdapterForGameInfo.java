package com.csci491.PartyCards;

import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;



public class BasicAdapterForGameInfo extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<BasicGameData> games;

    public BasicAdapterForGameInfo(Context context) {
        mInflater = LayoutInflater.from(context);
        clear();
    }
    public BasicAdapterForGameInfo(Context context, ArrayList<BasicGameData> games) {
        mInflater = LayoutInflater.from(context);
        this.games = games;
    }

    @Override
    public int getCount() {
        return games.size();
    }

    @Override
    public Object getItem(int position) {
        return games.get(position);
    }

    @Override
    public long getItemId(int position) {
        return games.get(position).gameId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView == null) {
            view = mInflater.inflate(R.layout.list_item, parent, false);
        } else {
            view = convertView;
        }

        view.setTag(games.get(position));
        TextView listItemTextView = (TextView)view.findViewById(R.id.listItemText);
        listItemTextView.setText(games.get(position).gameName);
        Message viewInfo = new Message();
        viewInfo.obj = games.get(position);
        view.setTag(viewInfo);

        return view;
    }

    public void add(BasicGameData additionalGame) {
        games.add(additionalGame);
    }

    public void clear() {
        games = new ArrayList<BasicGameData>();
    }

}