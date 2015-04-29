package com.csci491.PartyCards.NetworkTasks;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;

import com.csci491.PartyCards.Globals;
import com.csci491.PartyCards.R;

import org.ksoap2.serialization.SoapObject;



public class GetGameNameSoapTask extends SoapTask {
    int gameId;
    Handler mainThreadHandler;

    // constructor:
    // mainThreadHandler - a handler so that the changes can be reflected in the UI thread
    // gameId - the game id that we're trying to find the name of
    public GetGameNameSoapTask(Handler mainThreadHandler, int _gameId) {
        this.mainThreadHandler = mainThreadHandler;
        gameId = _gameId;
    }

    @Override
    protected void onPreExecute() {
        //getting ready to make the soap call, setting method name and arguments
        request = new SoapObject(SoapTask.NAMESPACE, METHOD_GET_GAME_NAME);
        request.addProperty("arg0", gameId);

    }

    @Override
    protected void onPostExecute(SoapObject result) {
        // attempt to process the result of the soap call
        try {

            Message messageToUIThread = new Message();
            messageToUIThread.arg1 = 1; // signal the handler to add a new value
            messageToUIThread.arg2 = gameId;
            messageToUIThread.obj = result.getProperty(0).toString();
            mainThreadHandler.handleMessage(messageToUIThread);
        }
        catch (Exception e) {
            Log.e("GetGameNameSoapTaskErr", e.getMessage());
        }
    }



}
