package com.csci491.PartyCards.NetworkTasks;

import android.util.Log;

import com.csci491.PartyCards.Globals;

import org.ksoap2.serialization.SoapObject;


public class JoinGameSoapTask extends SoapTask {
    //@WebMethod int joinGame(int gameId, String userName); // returns your player number in the game

    int gameId;
    String userName;

    public JoinGameSoapTask(int _gameId, String _userName) {
        gameId = _gameId;
        userName = _userName;
    }

    @Override
    protected void onPreExecute() {
        request = new SoapObject(SoapTask.NAMESPACE, METHOD_JOIN_GAME);
        request.addProperty("arg0", gameId);
        request.addProperty("arg1", userName);
    }

    @Override
    protected void onPostExecute(SoapObject result) {
        // attempt to process the result of the soap call
        try {
            // property[0] of result contains an array of the games that are both new and active
            int playerNumber = Integer.parseInt(result.getProperty(0).toString());
            if(playerNumber >= 0) {
                Globals.multiplayerGamePlayerId = playerNumber;
            }
        }
        catch (Exception e) {
            Log.e("JoinGameError", e.getMessage());
        }
    }
}
