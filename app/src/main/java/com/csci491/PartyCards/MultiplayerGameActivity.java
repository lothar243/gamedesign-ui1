package com.csci491.PartyCards;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.csci491.PartyCards.NetworkTasks.PartyCardsInterface;

// ====================================================================================================================
// MultiplayerGameActivity.java
// --------------------------------------------------------------------------------------------------------------------
// Party Cards: Android Networking Project
// CSCI-466: Networks
// Jeff Arends, Lee Curran, Angela Gross, Andrew Meissner
// Spring 2015
// --------------------------------------------------------------------------------------------------------------------
// Handles the UI listeners and other logic associated with the MultiplayerGameActivity
// ====================================================================================================================

public class MultiplayerGameActivity extends Activity
{
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // MULTIPLAYERGAMEACTIVITY ATTRIBUTES

    public static int numberOfPlayersSelecting;
    TextView blackCardTextView;
    Button visibleWhiteCardButton;
    Button submitButton;
    TextView textViewAditionalInfo;
    TextView textViewStatus;

    static InGameData thisGame;
    static int handIndex;
    static boolean previewPhase;

    public static Handler uiHandler;

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // ===============================================================================================================
    // ONCREATE()
    // ---------------------------------------------------------------------------------------------------------------
    // Prepares layout of the MultiplayerGameActivity
    // ===============================================================================================================
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingame);


        // set up references to ui elements
        Button buttonLeft = (Button) findViewById(R.id.buttonLeft);
        Button buttonRight = (Button) findViewById(R.id.buttonRight);
        blackCardTextView = (TextView) findViewById(R.id.textViewQuestion);
        textViewAditionalInfo = (TextView) findViewById(R.id.textViewAditionalInfo);
        textViewStatus = (TextView) findViewById(R.id.textViewStatus);
        visibleWhiteCardButton = (Button) findViewById(R.id.buttonCard);
        submitButton = (Button) findViewById(R.id.buttonSubmit);


        // set listeners on navigation
        buttonLeft.setOnClickListener(leftListener);
        buttonRight.setOnClickListener(rightListener);
        submitButton.setOnClickListener(submitListener);


        // this allows the background thread to call for ui updates
        uiHandler = new Handler() {
            // this will handle the notification gets from background thread
            @Override
            public void handleMessage(Message msg) {
                // make necessary changes to UI
                updateUI();
            }
        };

        // create a separate thread to handle network communication
        Globals.setUpBackgroundThread(uiHandler);
        // start the refreshing of data
        thisGame = null;
        previewPhase = true;
        scheduleDataUpdate();

    }

    // ===============================================================================================================
    // SCHEDULEDATAUPDATE()
    // ===============================================================================================================
    public void scheduleDataUpdate()
    {
        Globals.backgroundTaskThread.getHandlerToMsgQueue().sendEmptyMessage(PartyCardsInterface.GET_GAME_DATA);
    }

    // ===============================================================================================================
    // UPDATEUI()
    // ---------------------------------------------------------------------------------------------------------------
    // Updates UI of Activity
    // ===============================================================================================================
    public void updateUI()
    {
        // first check to make sure all variables have been set at least once
        if(thisGame == null)
        {
            //
            textViewStatus.setText("game data hasn't been fetched yet");
            return;
        }
        // else no longer initializing, set black and white card text
        blackCardTextView.setText(thisGame.blackCard);
        if(handIndex >= thisGame.hand.length)
        {
            handIndex = 0;

        }
        visibleWhiteCardButton.setText(thisGame.hand[handIndex]);
        textViewAditionalInfo.setText(handIndex + 1 + " / " + thisGame.hand.length);

        // now we're setting up the additional ui, like the buttons and instructions
        if(previewPhase)
        {
            numberOfPlayersSelecting = -1;
            textViewStatus.setText("Review game stats");
            submitButton.setText("Proceed");
            submitButton.setVisibility(View.VISIBLE);
        }
        else
        {
            switch (thisGame.turnPhase)
            {
                case 1: // normal players are choosing cards, card czar is waiting
                    submitButton.setVisibility(View.VISIBLE);
                    if (thisGame.playerIsCardCzar == 0)
                    { // normal players
                        if(numberOfPlayersSelecting == -1)
                        {
                            textViewStatus.setText("Choose your card");
                            submitButton.setText("Submit");
                        }
                        else
                        {
                            textViewStatus.setText("Your card has been submitted");
                            submitButton.setText("Change selection");
                        }
                    } else
                    { // card czar
                        submitButton.setVisibility(View.INVISIBLE);
                    }
                    break;
                case 2: // card czar is choosing a card, other players can view selection
                    if (thisGame.playerIsCardCzar == 0)
                    { // normal players
                        textViewStatus.setText("The card czar is choosing a card, have a look at their choices...");
                        submitButton.setVisibility(View.INVISIBLE);
                    }
                    else
                    { // card czar
                        submitButton.setText("Select winner");
                        submitButton.setVisibility(View.VISIBLE);
                    }
                    break;
            }
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
        // start the background thread
        Globals.windowIsInFocus = true;
        Globals.defaultMessage = PartyCardsInterface.GET_GAME_DATA;
        scheduleDataUpdate();
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

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    // BUTTON LISTENERS
    // \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    // ===============================================================================================================
    // CLICK -> LEFTLISTENER
    // ---------------------------------------------------------------------------------------------------------------
    // What happens if we click the "left arrow button" on the navigation
    // ===============================================================================================================
    private View.OnClickListener leftListener = new View.OnClickListener() {
        public void onClick(View v) {

            textViewStatus.setVisibility(View.VISIBLE);

            // decrease index by one, or increase by 6 if it's already 0
            handIndex = (handIndex + thisGame.hand.length - 1) % thisGame.hand.length;

            updateUI();
        }
    };

    // ===============================================================================================================
    // CLICK -> RIGHTLISTENER
    // ---------------------------------------------------------------------------------------------------------------
    // What happens if we click the "right arrow button" on the navigation (get card if available)
    // ===============================================================================================================
    private View.OnClickListener rightListener = new View.OnClickListener() {
        public void onClick(View v) {
            textViewStatus.setVisibility(View.VISIBLE);

            // increase index by one, or decrease by 6 if it's already 6
            handIndex = (handIndex + 1) % thisGame.hand.length;

            updateUI();
        }
    };

    // ===============================================================================================================
    // CLICK -> SUBMITLISTENER
    // ---------------------------------------------------------------------------------------------------------------
    // What happens if we click on the submit button for a card.
    // ===============================================================================================================
    private View.OnClickListener submitListener = new View.OnClickListener() {
        public void onClick(View arg0) {
            // communicate with server that a card has been selected
            if(previewPhase) {
                // prepare for next round
                previewPhase = false;
                scheduleDataUpdate();
            }
            else {
                Globals.backgroundTaskThread.getHandlerToMsgQueue().sendEmptyMessage(PartyCardsInterface.CHOOSE_CARD);
                scheduleDataUpdate();
            }
        }
    };

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    // MENU
    // \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    // ===============================================================================================================
    // ONCREATEOPTIONSMENU
    // ---------------------------------------------------------------------------------------------------------------
    // Inflate the menu; this adds items to the action bar if it is present.
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
        switch (item.getItemId()) {
            case R.id.action_leavegame:
                builder = new AlertDialog.Builder(MultiplayerGameActivity.this);
                builder.setTitle(R.string.leave_game_message);

                // Add the buttons
                builder.setPositiveButton(R.string.leave_game, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Leave Game
                        Intent intent = new Intent(MultiplayerGameActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

                // Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.playerNumber:

                builder = new AlertDialog.Builder(MultiplayerGameActivity.this);
                builder.setTitle(R.string.changePlayerNumber);

                final EditText input = new EditText(this);
                // Specify the type of input expected;
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Globals.multiplayerGamePlayerId = Integer.parseInt(input.getText().toString());
                        }
                        catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error parsing the number", Toast.LENGTH_SHORT).show();
                        }

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