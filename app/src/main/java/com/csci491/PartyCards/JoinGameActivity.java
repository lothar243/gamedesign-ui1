package com.csci491.PartyCards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.csci491.PartyCards.NetworkTasks.PartyCardsInterface;


public class JoinGameActivity extends Activity {
    public static BasicGameData gameInfo;

    static TextView gameNameTextView;
    static TextView playersNamesTextView;
    static TextView statusTextView;

    static Button startGameButton;
    static Button joinGameButton;

    static Handler uiHandler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        // finding inflated textview and buttons
        gameNameTextView = (TextView) findViewById(R.id.textGameName);
        playersNamesTextView = (TextView) findViewById(R.id.textPlayerNames);
        statusTextView = (TextView)findViewById(R.id.textViewStatus);
        joinGameButton = (Button) findViewById(R.id.buttonJoinGame);
        startGameButton = (Button) findViewById(R.id.buttonStartGame);

        // setting up the on click listeners
        joinGameButton.setOnClickListener(joinButtonListener);
        startGameButton.setOnClickListener(startGameListener);


        // this allows the background thread to call for ui updates
        uiHandler = new Handler() {
            // this will handle the notification gets from background thread
            @Override
            public void handleMessage(Message msg) {
                // make necessary changes to UI
                updateUI();
            }
        };


        // getting the background thread set up
        Globals.setUpBackgroundThread(uiHandler);

                                                                                                                                                                                                                                                                                                                                                                                                                      }

    public void updateUI() {
        if(gameInfo == null) {
            return;
        }
        else {
            Toast.makeText(getApplicationContext(), "updating game " + gameInfo.gameId, Toast.LENGTH_SHORT).show();

            // game name
            gameNameTextView.setText("Game name: " + gameInfo.gameName);

            //update current players textView
            if(gameInfo.playerNames.length == 0) {
                playersNamesTextView.setText("Players: None");
            }
            else {
                String playerNamesString = "Players: " + gameInfo.playerNames[0];
                for (int i = 1; i < gameInfo.playerNames.length; i++) {
                    playerNamesString += ", " + gameInfo.playerNames[i];
                }
                playersNamesTextView.setText(playerNamesString);
            }


            //buttons
            if(Globals.userName != null || Globals.userName.equals("")) {
                if((Globals.multiplayerGamePlayerId = gameInfo.playerHasJoinedGame(Globals.userName)) >= 0) {

                    if(gameInfo.gameIsNew) {
                        // the player has joined a game that is still forming, giving them the option to start the game
                        statusTextView.setText("You may start the game");
                        joinGameButton.setVisibility(View.INVISIBLE);
                        startGameButton.setVisibility(View.VISIBLE);
                    }
                    else {
                        // the player has joined a game that has already started... starting the game
                        Intent myIntent = new Intent(getApplicationContext(), MultiplayerGameActivity.class);
                        startActivity(myIntent);
                        finish();
                    }
                }
                else {
                    if(gameInfo.gameIsNew) {
                        // the game is new and the player hasn't joined, giving them the option to join
                        statusTextView.setText("The game is forming");
                        joinGameButton.setVisibility(View.VISIBLE);
                        startGameButton.setVisibility(View.INVISIBLE);
                    }
                    else {
                        // the game has started and the player hadn't joined
                        statusTextView.setText("The game has already started without you!");
                        joinGameButton.setVisibility(View.INVISIBLE);
                        startGameButton.setVisibility(View.INVISIBLE);
                    }

                }
            }
            else {
                statusTextView.setText("You have to have a username to interact with the games");
                joinGameButton.setVisibility(View.INVISIBLE);
                startGameButton.setVisibility(View.INVISIBLE);
            }
        }

    }


    private View.OnClickListener joinButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(Globals.userName.equals("")) {
                statusTextView.setText("Error, you have no username");
            }
            else {
                statusTextView.setText("Joining");


                Globals.backgroundTaskThread.getHandlerToMsgQueue().sendEmptyMessage(PartyCardsInterface.JOIN_GAME);
            }
        }
    };

    private View.OnClickListener startGameListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(Globals.userName.equals("")) {
                statusTextView.setText("Error, you have no username");
            }
            else {
                statusTextView.setText("Starting...");

                Globals.backgroundTaskThread.getHandlerToMsgQueue().sendEmptyMessage(PartyCardsInterface.STAR_NEW_GAME);
            }
        }
    };



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_join_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Globals.windowIsInFocus = true;
        Globals.defaultMessage = PartyCardsInterface.GET_BASIC_GAME_DATA_SINGLE_GAME;
        Globals.backgroundTaskThread.getHandlerToMsgQueue().sendEmptyMessage(Globals.defaultMessage);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Globals.windowIsInFocus = false;
    }
}




