package com.csci491.PartyCards.NetworkTasks;

import android.widget.TextView;

import com.csci491.PartyCards.Globals;

import org.ksoap2.serialization.SoapObject;

public class ListPlayersSoapTask extends SoapTask {
    TextView playerNamesTextView;
    int gameId;

    public ListPlayersSoapTask(TextView playerNamesTextView, int gameId) {
        super();
        this.playerNamesTextView = playerNamesTextView;
        this.gameId = gameId;
    }

    @Override
    protected void onPreExecute() {
        soapAction = formProperSoapAction(METHOD_LIST_PLAYERS);
        request = new SoapObject(SoapTask.NAMESPACE, METHOD_LIST_PLAYERS);
        request.addProperty("arg0", gameId);

    }

    @Override
    protected void onPostExecute(SoapObject result) {

        // attempt to process the result of the soap call
        try {
            // property[0] of result contains an array of the player names
            //Log.d("listSoapObject", result.toString());
            SoapObject arrayContainer = (SoapObject)result.getProperty(0);

            int numberOfPlayers = arrayContainer.getPropertyCount();

            //create a string listing each of the player names
            String playerNames = "Players: ";
            Globals.multiplayerFetchingPlayerNames = false;
            if(numberOfPlayers > 0) {
                //Log.d("listplayers", playerNames);
                playerNames += arrayContainer.getProperty(0).toString();
                for(int i = 1; i < numberOfPlayers; i++) {
                    //Log.d("listplayers", playerNames);
                    playerNames += ", " + arrayContainer.getProperty(i).toString();
                }
            }
            else {
                playerNames = "Nobody has joined this game yet";
            }
            playerNamesTextView.setText(playerNames);

        }
        catch (Exception e) {
            playerNamesTextView.setText("Players: Nobody");
        }
    }


}
