package com.csci491.PartyCards.NetworkTasks;

import android.util.Log;

import com.csci491.PartyCards.Globals;

import org.ksoap2.serialization.SoapObject;

public class IsGameFormingSoapTask extends SoapTask {
    //@WebMethod int joinGame(int gameId, String userName); // returns your player number in the game

    int gameId;


    public IsGameFormingSoapTask(int _gameId) {
        gameId = _gameId;
    }

    @Override
    protected void onPreExecute() {
        request = new SoapObject(SoapTask.NAMESPACE, METHOD_IS_GAME_FORMING);
        request.addProperty("arg0", gameId);
    }

    @Override
    protected void onPostExecute(SoapObject result) {
        // attempt to process the result of the soap call
        try {
            // property[0] of result contains an array of the games that are both new and active

            Globals.multiplayerFetchingGameStatus = false;
            Globals.multiplayerGameIsNew = Boolean.valueOf((result.getProperty(0).toString()));
        }
        catch (Exception e) {
            Log.e("IsGameFormingError", e.getMessage());
        }
    }

}
