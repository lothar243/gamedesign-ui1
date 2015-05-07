package com.csci491.PartyCards;

import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

// ====================================================================================================================
// BasicAdapterForGameInfo.java
// --------------------------------------------------------------------------------------------------------------------
// Party Cards: Android Networking Project
// CSCI-466: Networks
// Jeff Arends, Lee Curran, Angela Gross, Andrew Meissner
// Spring 2015
// ====================================================================================================================

public class BasicAdapterForGameInfo extends BaseAdapter
{
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // BASICADAPTERFORGAMEINFO ATTRIBUTES
    private LayoutInflater mInflater;
    private ArrayList<BasicGameData> games;

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // BASICADAPTERFORGAMEINFO CONSTRUCTORS
    public BasicAdapterForGameInfo(Context context)
    {
        mInflater = LayoutInflater.from(context);
        clear();
    }
    public BasicAdapterForGameInfo(Context context, ArrayList<BasicGameData> games)
    {
        mInflater = LayoutInflater.from(context);
        this.games = games;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    // METHODS ADD AND CLEAR GAMES
    // \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public void add(BasicGameData additionalGame) {
        games.add(additionalGame);
    }

    public void clear() {
        games = new ArrayList<BasicGameData>();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    // GETTERS AND SETTERS FOR BASICADAPTERFORGAMEINFO
    // \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public int getCount() {
        return games.size();
    }

    public Object getItem(int position) {
        return games.get(position);
    }

    public long getItemId(int position) {
        return games.get(position).gameId;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view;
        if(convertView == null)
        {
            view = mInflater.inflate(R.layout.list_item, parent, false);
        }
        else
        {
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

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}