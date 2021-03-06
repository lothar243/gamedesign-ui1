package com.csci491.PartyCards;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.csci491.PartyCards.NetworkTasks.PartyCardsInterface;

// ====================================================================================================================
// ListMultiplayerGamesActivity.java
// --------------------------------------------------------------------------------------------------------------------
// Party Cards: Android Networking Project
// CSCI-466: Networks
// Jeff Arends, Lee Curran, Angela Gross, Andrew Meissner
// Spring 2015
// --------------------------------------------------------------------------------------------------------------------
// Handles the UI listeners and other logic associated with the ListMultiplayerGamesActivity
// ====================================================================================================================

public class ListMultiplayerGamesActivity extends ListActivity
{
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // LISTMULTIPLAYGAMESACTIVITY ATTRIBUTES
    public static Handler uiHandler;
    public static BasicAdapterForGameInfo gameNameAdapter;
    public static BasicGameData [] listedGames;
    static  Context appContext;

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // ===============================================================================================================
    // ONCREATE()
    // ---------------------------------------------------------------------------------------------------------------
    // Prepares layout of the ListMultiplayerGamesActivity
    // ===============================================================================================================
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_multiplayer_game);
        gameNameAdapter = new BasicAdapterForGameInfo(this);

        // set up the handler to listen for when the game names have been retrieved
        uiHandler = new Handler() {
            // this will handle the notification gets from background thread
            @Override
            public void handleMessage(Message msg) {
                // make necessary changes to UI
                updateUI();
            }
        };


        Globals.setUpBackgroundThread(uiHandler);

        appContext = getApplicationContext();
        // finally, set up the adapter that takes care of generating the list
        setListAdapter(gameNameAdapter);

        final Button newGameButton = (Button)findViewById(R.id.ButtonNewMultiplayerGame);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createNewGameDialog();
            }
        });

        if(Globals.userName.equals(""))
        {
            promptForUserName();
        }


    }

    // ===============================================================================================================
    // UPDATEUI()
    // ---------------------------------------------------------------------------------------------------------------
    // Updates UI of Activity
    // ===============================================================================================================
    public static void updateUI()
    {
        if(listedGames != null)
        {
            gameNameAdapter.clear();
            for (BasicGameData game : listedGames)
            {
                gameNameAdapter.add(game);
            }
            gameNameAdapter.notifyDataSetChanged();
            Toast.makeText(appContext, "updating " + listedGames.length, Toast.LENGTH_SHORT).show();
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // ===============================================================================================================
    // ONRESUME
    // ---------------------------------------------------------------------------------------------------------------
    // Handles what happens when the game is focused on again
    // ===============================================================================================================
    protected void onResume()
    {
        super.onResume();
        Globals.windowIsInFocus = true;
        Globals.defaultMessage = PartyCardsInterface.GET_GAMES;
        Globals.backgroundTaskThread.getHandlerToMsgQueue().sendEmptyMessage(Globals.defaultMessage);
    }

    // ===============================================================================================================
    // ONPAUSE
    // ---------------------------------------------------------------------------------------------------------------
    // Handles what happens when the game is paused
    // ===============================================================================================================
    protected void onPause()
    {
        super.onPause();
        Globals.windowIsInFocus = false;
    }

    // ===============================================================================================================
    // ONLISTITEMCLICK
    // ---------------------------------------------------------------------------------------------------------------
    // Helps join the selected game
    // ===============================================================================================================
    protected void onListItemClick(ListView listView, View currentView, int position, long id)
    {
        Globals.multiplayerGameId = listedGames[position].gameId;

        Intent joinGameIntent = new Intent(ListMultiplayerGamesActivity.this, JoinGameActivity.class);

        startActivity(joinGameIntent);
        finish();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // ===============================================================================================================
    // CREATENEWGAMEDIALOG
    // ---------------------------------------------------------------------------------------------------------------
    // Sets up UI to create a new game
    // ===============================================================================================================
    private void createNewGameDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game Title");

        // Set up the input
        final EditText input = new EditText(this);

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Message createGameMessage = Message.obtain();
                createGameMessage.what = PartyCardsInterface.CREATE_NEW_GAME;
                createGameMessage.obj = input.getText().toString();
                Globals.backgroundTaskThread.getHandlerToMsgQueue().sendMessage(createGameMessage);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    // ===============================================================================================================
    // PROMPTFORUSERNAME
    // ---------------------------------------------------------------------------------------------------------------
    // This method sets up the UI to prompt for a user name before joining a game
    // ===============================================================================================================
    public void promptForUserName()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Before you can join, what is your username?");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected;
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Globals.userName = input.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    // ===============================================================================================================
    // ONCREATEOPTIONSMENU
    // ===============================================================================================================
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.ingame, menu);
        return true;
    }

    // ===============================================================================================================
    // ONOPTIONSITEMSELECTED
    // ---------------------------------------------------------------------------------------------------------------
    // This method handles item selection
    // ===============================================================================================================
    public boolean onOptionsItemSelected(MenuItem item)
    {
        AlertDialog.Builder builder;
        switch (item.getItemId())
        {
            case R.id.set_server_ip:

                builder = new AlertDialog.Builder(ListMultiplayerGamesActivity.this);
                builder.setTitle(R.string.changePlayerNumber);

                final EditText input = new EditText(this);
                // Specify the type of input expected;
                input.setInputType(InputType.TYPE_CLASS_PHONE);
                builder.setView(input);
                input.setText(Globals.multiplayerServerIPAddress);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            Globals.multiplayerServerIPAddress = input.getText().toString();
                        System.out.println("Server IP: " + Globals.multiplayerServerIPAddress);
                        Globals.backgroundTaskThread.getHandlerToMsgQueue().sendEmptyMessage(WorkerThread.CHANGE_IP_ADDRESS);

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
