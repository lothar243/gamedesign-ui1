package com.csci491.PartyCards.NetworkTasks;

import android.os.Handler;
import android.util.Log;

import org.ksoap2.serialization.SoapObject;

public class CreateNewGameSoapTask extends SoapTask {
    Handler mainThreadHandler;
    String gameName;

    public CreateNewGameSoapTask(Handler mainThreadHandler, String _gameName) {
        super();
        this.mainThreadHandler = mainThreadHandler;
        gameName = _gameName;
    }

    @Override
    protected void onPreExecute() {
        soapAction = formProperSoapAction(METHOD_CREATE_GAME);
        request = new SoapObject(SoapTask.NAMESPACE, METHOD_CREATE_GAME);
        request.addProperty("arg0", gameName);

    }

    @Override
    protected void onPostExecute(SoapObject result) {
        // the soap call is finished, time to update the list
        GetGamesSoapTask refreshTask = new GetGamesSoapTask(mainThreadHandler);
        refreshTask.execute();
        try {
            Log.d("createnewGameTask", result.getProperty(0).toString());

        }
        catch (Exception e) {
            Log.d("CreateNewGameSoapTask", e.getMessage());
        }
    }
}
