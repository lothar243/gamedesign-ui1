package com.csci491.PartyCards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.csci491.PartyCards.NetworkTasks.IsGameFormingSoapTask;
import com.csci491.PartyCards.NetworkTasks.JoinGameSoapTask;
import com.csci491.PartyCards.NetworkTasks.ListPlayersSoapTask;
import com.csci491.PartyCards.NetworkTasks.NetworkMethods;
import com.csci491.PartyCards.NetworkTasks.PartyCardsInterface;
import com.csci491.PartyCards.NetworkTasks.StartGameSoapTask;


public class JoinGameActivity extends Activity {
    TextView playersNames;
    boolean active = true;
    TextView statusTextView;
    Button startGameButton;
    Button joinGameButton;

    Handler UIHandler = new Handler() {
        @Override
        public void handleMessage(Message incomingMessage) {
            if(active) {
                switch (incomingMessage.arg1) {
                    case 0:
                        Message refreshMessage = Message.obtain();
                        refreshMessage.arg1 = 0;
                        UIHandler.sendMessageDelayed(refreshMessage, 5000);
                        refreshGameInfo();
                        break;
                    default:
                        //System.out.println("default");
                        break;
                }
            }

        }
    };








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        Intent input = getIntent();

        Globals.multiplayerGameId = input.getIntExtra("gameId", -1);
        Globals.multiplayerGameName = input.getStringExtra("gameName");
        TextView titleText = (TextView) findViewById(R.id.textGameName);
        titleText.setText("Game Name: " + Globals.multiplayerGameName);
        Globals.multiplayerFetchingGameStatus = true;
        Globals.multiplayerFetchingPlayerNames = true;
        Globals.multiplayerGamePlayerId = -1;
        playersNames = (TextView) findViewById(R.id.textPlayerNames);
        playersNames.setText("Fetching current players...");
        UIHandler.sendEmptyMessage(0);
        Globals.multiplayerGameIsNew = true;


        statusTextView = (TextView)findViewById(R.id.textViewStatus);

        joinGameButton = (Button) findViewById(R.id.buttonJoinGame);
        joinGameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(Globals.userName.equals("")) {
                    statusTextView.setText("Error, you have no username");
                }
                else {
                    statusTextView.setText("Joining");


                    JoinGameSoapTask joinGame = new JoinGameSoapTask(Globals.multiplayerGameId, Globals.userName);
                    joinGame.execute();
                }
            }
        });
        joinGameButton.setVisibility(View.INVISIBLE);
        startGameButton = (Button) findViewById(R.id.buttonStartGame);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(Globals.userName.equals("")) {
                    statusTextView.setText("Error, you have no username");
                }
                else {
                    statusTextView.setText("Starting...");

                    StartGameSoapTask startGame = new StartGameSoapTask(Globals.multiplayerGameId, Globals.userName);
                    startGame.execute();
                }
            }
        });
        startGameButton.setVisibility(View.INVISIBLE);

    }


    public void refreshGameInfo() {

        // refresh player names
        ListPlayersSoapTask listPlayers = new ListPlayersSoapTask(playersNames, Globals.multiplayerGameId);
        listPlayers.execute();

        // check to see if the game has started
        IsGameFormingSoapTask checkForming = new IsGameFormingSoapTask(Globals.multiplayerGameId);
        checkForming.execute();

        if((!Globals.multiplayerGameIsNew) && (Globals.multiplayerGamePlayerId >= 0)) {
            // game has started, progressing to the next screen
            Intent intent = new Intent(JoinGameActivity.this, MultiplayerGameActivity.class);
            startActivity(intent);
        }
        if(Globals.multiplayerFetchingGameStatus) {
            // the first soap call has yet to return
            statusTextView.setText("Fetching game information");
        }
        else {
            joinGameButton.setVisibility(View.VISIBLE);
            if(Globals.multiplayerGameIsNew) {
                if (Globals.multiplayerGamePlayerId == -1) {
                    statusTextView.setText("This game is still forming, you can join!");
                }
                else {
                    statusTextView.setText("You have joined, waiting for the game to start");
                    joinGameButton.setVisibility(View.INVISIBLE);
                    startGameButton.setVisibility(View.VISIBLE);
                }
            }
            else {
                if(Globals.multiplayerGamePlayerId == -1) {
                    statusTextView.setText("This game is already underway");
                    joinGameButton.setText("Re-join");
                }
                else {
                    statusTextView.setText("Starting game...");
                }
            }
        }


    }

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
        active = true;
        if(!UIHandler.hasMessages(0)) {
            // handler doesn't have the message, so start periodic refreshes again
            UIHandler.sendEmptyMessage(0);
        }
        // else the message still exists in the message queue, no need to double up on refreshing
    }

    @Override
    protected void onPause() {
        super.onPause();
        active = false;
    }
}


