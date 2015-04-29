package com.csci491.PartyCards.NetworkTasks;

import org.ksoap2.serialization.SoapObject;

public class StartGameSoapTask extends SoapTask {
    //@WebMethod int joinGame(int gameId, String userName); // returns your player number in the game

    int gameId;


    public StartGameSoapTask(int _gameId, String _userName) {
        gameId = _gameId;
    }

    @Override
    protected void onPreExecute() {
        request = new SoapObject(SoapTask.NAMESPACE, METHOD_START_GAME);
        request.addProperty("arg0", gameId);
    }
}
