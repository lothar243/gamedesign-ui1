package com.csci491.PartyCards.NetworkTasks;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.ksoap2.serialization.SoapObject;


public class GetGamesSoapTask extends SoapTask {
    Handler mainThreadHandler;

    // constructor:
    // mainThreadHandler - a handler back to the UI thread that calls the aSyncTask so that changes can be reflected when we're done here
    public GetGamesSoapTask(Handler mainThreadHandler) {
        this.mainThreadHandler = mainThreadHandler;
    }

    @Override
    protected void onPreExecute() {
        // setup the soap call specific to this method
        soapAction = formProperSoapAction(METHOD_GET_NEW_GAMES);
        request = new SoapObject(SoapTask.NAMESPACE, METHOD_GET_NEW_GAMES);
    }

    @Override
    protected void onPostExecute(SoapObject result) {
        // first clear the list in case there are already items in it
        Message clearListMessage = new Message();
        clearListMessage.arg1 = 0;
        mainThreadHandler.handleMessage(clearListMessage);

        // attempt to process the result of the soap call
        try {
            // property[0] of result contains an array of the games that are both new and active
            SoapObject arrayContainer = (SoapObject)result.getProperty(0);

            // set up Globals to keep track of multiplayer games
            int numberOfGames = arrayContainer.getPropertyCount();

            // spawn another aSyncTask for each of the retrieved game ids, fetching each of their names
            for(int i = 0; i < numberOfGames; i++) {
                GetGameNameSoapTask fetchName = new GetGameNameSoapTask(mainThreadHandler, i);
                fetchName.execute();
            }


        }
        catch (Exception e) {
            Log.e("GetNewGameSoapTaskError", "Error retrieving game list");
        }
    }


}
