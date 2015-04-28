package com.csci491.PartyCards.NetworkTasks;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

public class CreateNewGameSoapTask extends SoapTask {
    String gameName;

    public CreateNewGameSoapTask(String _gameName) {
        super();
        gameName = _gameName;
    }

    @Override
    protected void onPreExecute() {
        request = new SoapObject(SoapTask.NAMESPACE, METHOD_CREATE_GAME);
        request.addProperty("arg0", gameName);

    }

    @Override
    protected void onPostExecute(SoapObject result) {
        // attempt to process the result of the soap call
        try {

            Log.d("CreateNewGameSoapTask", "result of creating new game: " +  result.getProperty(0).toString());

        }
        catch (Exception e) {
            Log.d("CreateNewGameSoapTask", e.getMessage());
        }
    }
}
