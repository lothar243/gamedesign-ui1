package com.csci491.PartyCards;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.csci491.PartyCards.NetworkTasks.NetworkMethods;
import com.csci491.PartyCards.NetworkTasks.PartyCardsInterface;


public class MultiplayerGameActivity extends Activity {


    TextView blackCardTextView;
    Button visibleWhiteCardButton;
    Button submitButton;
    TextView textViewAditionalInfo;
    TextView textViewStatus;
    boolean submitButtonCanBeToggled;

    public static Handler uiHandler;
    WorkerThread backgroundTaskThread;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingame);

        // store some initial data in the various cards until the actual values are retrieved
        Globals.multiplayerTurnNumber = -1;
        initGameVariables();




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
        visibleWhiteCardButton.setOnClickListener(cardListener);


        // this allows the background thread to call for ui updates
        uiHandler = new Handler() {
            // this will handle the notification gets from background thread
            @Override
            public void handleMessage(Message msg) {
                // make necessary changes to UI
                updateUI(msg);
            }
        };

        // create a seperate thread to handle network communication
        backgroundTaskThread = new WorkerThread(uiHandler);
        backgroundTaskThread.start();
        while(backgroundTaskThread.getHandlerToMsgQueue() == null) {
                    // delay until the backgroundThread gets set up
        }

        // fetch current game information
        backgroundTaskThread.getHandlerToMsgQueue().sendEmptyMessage(PartyCardsInterface.GET_BLACK_CARD);
        backgroundTaskThread.getHandlerToMsgQueue().sendEmptyMessage(PartyCardsInterface.GET_HAND);
        backgroundTaskThread.getHandlerToMsgQueue().sendEmptyMessage(PartyCardsInterface.PLAYER_IS_CARD_CZAR);
        backgroundTaskThread.getHandlerToMsgQueue().sendEmptyMessage(PartyCardsInterface.GET_TURN_STATUS);
    }

    public void updateUI(Message msg) {
        // first check to make sure all variables have been set at least once
        if(Globals.multiplayerCurrentBlackCard.equals(defaultCardText)
                || Globals.multiplayerCurrentHand[0].equals(defaultCardText)
                || Globals.multiplayerPlayerIsCardCzar == -1
                || Globals.multiplayerTurnPhase == -1
                || Globals.multiplayerTurnNumber == -1
                || Globals.multiplayerNumberOfPlayersChoosing == -1) {
            //
            textViewStatus.setText("debug info: czar:" + Globals.multiplayerPlayerIsCardCzar + "phase:" + Globals.multiplayerTurnPhase + "turn:" + Globals.multiplayerTurnNumber + "Choosing:" + Globals.multiplayerNumberOfPlayersChoosing);
        }
        else { // no longer initializing
            blackCardTextView.setText(Globals.multiplayerCurrentBlackCard);
            visibleWhiteCardButton.setText(Globals.multiplayerCurrentHand[Globals.multiplayerWhiteCardIndex]);

            if(Globals.previewPhase) {
                textViewStatus.setText("Review game stats");
                submitButton.setText("Proceed");
                submitButtonCanBeToggled = false;
                submitButton.setVisibility(View.VISIBLE);
            }
            else {
                switch (Globals.multiplayerTurnPhase) {
                    case 1: // normal players are choosing cards, card czar is waiting
                        if (Globals.multiplayerPlayerIsCardCzar == 0) { // normal players
                            Toast.makeText(getApplicationContext(), "normal player phase 1", Toast.LENGTH_SHORT).show();
                            submitButtonCanBeToggled = true;
                            switch (Globals.multiplayerSelectionAccepted) {
                                case -1:
                                    textViewStatus.setText("Error submitting, try again");
                                    submitButton.setText("Resubmit");
                                    break;
                                case 0:
                                    textViewStatus.setText("Choose your card");
                                    submitButton.setText("Submit");
                                    break;
                                case 1:
                                    textViewStatus.setText("Waiting on " + Globals.multiplayerNumberOfPlayersChoosing + " players");
                                    submitButton.setText("Change selection");
                                    backgroundTaskThread.periodicRefresh();
                                    break;
                            }
                        } else { // card czar
                            Toast.makeText(getApplicationContext(), "card czar phase 1", Toast.LENGTH_SHORT).show();
                            textViewStatus.setText("You're the card czar\nWaiting on " + Globals.multiplayerNumberOfPlayersChoosing + " players");
                            submitButton.setVisibility(View.INVISIBLE);
                            submitButtonCanBeToggled = false;
                            backgroundTaskThread.periodicRefresh();

                        }
                        break;
                    case 2: // card czar is choosing a card, other players can view selection
                        if (Globals.multiplayerPlayerIsCardCzar == 0) { // normal players
                            Toast.makeText(getApplicationContext(), "normal player phase 2", Toast.LENGTH_SHORT).show();
                            textViewStatus.setText("Waiting on card czar\nHere are the selections");
                            backgroundTaskThread.periodicRefresh();
                            submitButton.setVisibility(View.INVISIBLE);
                            submitButtonCanBeToggled = false;
                        } else { // card czar
                            Toast.makeText(getApplicationContext(), "card czard phase 2", Toast.LENGTH_SHORT).show();
                            textViewStatus.setText("You're the card czar\nSelect a card");
                            submitButtonCanBeToggled = true;
                            submitButton.setText("Select winner");
                        }
                        break;
                }
            }
        }

    }

    public static final String defaultCardText = "Fetching game information...";

    private void initGameVariables() {
        Globals.multiplayerCurrentBlackCard = defaultCardText;
        Globals.multiplayerCurrentHand = new String[]{defaultCardText};
        Globals.multiplayerPlayerIsCardCzar = -1;
        Globals.multiplayerWhiteCardIndex = 0;
        Globals.multiplayerTurnPhase = -1;
        Globals.multiplayerNumberOfPlayersChoosing = -1;
        Globals.multiplayerSelectionAccepted = 0;

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
            Globals.multiplayerWhiteCardIndex = (Globals.multiplayerWhiteCardIndex + Globals.multiplayerCurrentHand.length - 1) %Globals.multiplayerCurrentHand.length;

            //set the text of the visible white card
            visibleWhiteCardButton.setText(Globals.multiplayerCurrentHand[Globals.multiplayerWhiteCardIndex]);

            // update the index at the bottom of the screen
            textViewAditionalInfo.setText(Globals.multiplayerWhiteCardIndex + 1 + " / " + Globals.getHandSize());
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
            Globals.multiplayerWhiteCardIndex = (Globals.multiplayerWhiteCardIndex + 1) % Globals.multiplayerCurrentHand.length;

            //set the text of the visible white card
            visibleWhiteCardButton.setText(Globals.multiplayerCurrentHand[Globals.multiplayerWhiteCardIndex]);

            // update the index at the bottom of the screen
            textViewAditionalInfo.setText(Globals.multiplayerWhiteCardIndex + 1 + " / " + Globals.multiplayerCurrentHand.length);
        }
    };

    // ===============================================================================================================
    // CLICK -> CARDLISTENER
    // ---------------------------------------------------------------------------------------------------------------
    // What happens if we click on a card (make the submit button visible)
    // ===============================================================================================================
    private View.OnClickListener cardListener = new View.OnClickListener() {
        public void onClick(View v) {

            if(submitButtonCanBeToggled) { // not all screens allow this button to be toggled
                if (submitButton.getVisibility() == View.INVISIBLE)
                    submitButton.setVisibility(View.VISIBLE);
                else
                    submitButton.setVisibility(View.INVISIBLE);
            }
            // Call the listener of the button
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
            if(Globals.previewPhase) {
                // prepare for next round
                Globals.previewPhase = false;
                initGameVariables();
                backgroundTaskThread.getHandlerToMsgQueue().sendEmptyMessage(PartyCardsInterface.GET_BLACK_CARD);
                backgroundTaskThread.getHandlerToMsgQueue().sendEmptyMessage(PartyCardsInterface.GET_HAND);
                backgroundTaskThread.getHandlerToMsgQueue().sendEmptyMessage(PartyCardsInterface.PLAYER_IS_CARD_CZAR);
                backgroundTaskThread.getHandlerToMsgQueue().sendEmptyMessage(PartyCardsInterface.GET_TURN_STATUS);
            }
            else {
                backgroundTaskThread.getHandlerToMsgQueue().sendEmptyMessage(PartyCardsInterface.CHOOSE_CARD);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ingame, menu);
        return true;
    }

    // ===============================================================================================================
    // ONOPTIONSITEMSELECTED
    // ---------------------------------------------------------------------------------------------------------------
    // This method handles item selection
    // ===============================================================================================================
    public boolean onOptionsItemSelected(MenuItem item) {
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
                            Toast.makeText(getApplicationContext(), "Error parsing the number", Toast.LENGTH_SHORT);
                        }
                        initGameVariables();
                        backgroundTaskThread.getHandlerToMsgQueue().sendEmptyMessage(PartyCardsInterface.GET_BLACK_CARD);
                        backgroundTaskThread.getHandlerToMsgQueue().sendEmptyMessage(PartyCardsInterface.GET_HAND);
                        backgroundTaskThread.getHandlerToMsgQueue().sendEmptyMessage(PartyCardsInterface.PLAYER_IS_CARD_CZAR);
                        backgroundTaskThread.getHandlerToMsgQueue().sendEmptyMessage(PartyCardsInterface.GET_TURN_STATUS);

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

    @Override
    protected void onResume() {
        super.onResume();
        // start the background thread
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}

class WorkerThread extends Thread {
    private final static String TAG = WorkerThread.class.getSimpleName(); // for the debugger
    NetworkMethods networkMethods;

    // when we need to update the ui
    private static Handler uiHandler;

    // to pass message back to itself
    private static Handler workerHandler;

    public WorkerThread(Handler uiHandler) {
        this.uiHandler = uiHandler;
    }

    public void run() {
        // Thread by default doesn't have a msg queue, to attach a msg queue to this thread
        Looper.prepare();
        networkMethods = new NetworkMethods();
        // this will bind the Handler to the msg queue
        // notice that msg queue is FIFO, if u send 2 runable objects 1 after the other, second 1 will wait till first one finish
        workerHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case PartyCardsInterface.GET_HAND:
                        Globals.multiplayerCurrentHand = networkMethods.getHand(Globals.multiplayerGameId, Globals.multiplayerGamePlayerId);
                        if (Globals.multiplayerWhiteCardIndex >= Globals.multiplayerCurrentHand.length) {
                            Globals.multiplayerWhiteCardIndex = 0;
                        }
                        break;
                    case PartyCardsInterface.GET_BLACK_CARD:
                        Globals.multiplayerCurrentBlackCard = networkMethods.getBlackCard(Globals.multiplayerGameId);
                        break;
                    case PartyCardsInterface.CHOOSE_CARD:
                        Globals.multiplayerNumberOfPlayersChoosing = networkMethods.chooseCard(Globals.multiplayerGameId, Globals.multiplayerGamePlayerId, Globals.multiplayerWhiteCardIndex);
                        if(Globals.multiplayerNumberOfPlayersChoosing == 0) {
                            getHandlerToMsgQueue().sendEmptyMessage(PartyCardsInterface.GET_TURN_STATUS);
                        }
                        break;
                    case PartyCardsInterface.PLAYER_IS_CARD_CZAR:
                        Globals.multiplayerPlayerIsCardCzar = networkMethods.playerIsCardCzar(Globals.multiplayerGameId, Globals.multiplayerGamePlayerId);
                        break;
                    case PartyCardsInterface.GET_TURN_STATUS:
                        // // returns [turn number][phase number][numberOfPlayersChoosing]
                        Integer[] turnStatus = networkMethods.getTurnStatus(Globals.multiplayerGameId);
                        if(Globals.multiplayerTurnNumber < turnStatus[0]) {
                            // before we begin the round, let's review who's playing the game, points, etc
                            Globals.previewPhase = true;
                            Globals.multiplayerTurnNumber = turnStatus[0];
                            Globals.multiplayerTurnPhase = turnStatus[1];
                            Globals.multiplayerNumberOfPlayersChoosing = turnStatus[2];
                            Globals.multiplayerCurrentHand = networkMethods.roundSummary(Globals.multiplayerGameId);
                        }
                        else {
                            Globals.multiplayerTurnNumber = turnStatus[0];
                            Globals.multiplayerTurnPhase = turnStatus[1];
                            Globals.multiplayerNumberOfPlayersChoosing = turnStatus[2];
                        }
                        break;
                    default:

                        break;

                }
                uiHandler.sendEmptyMessage(0);

            }
        };
        // handles msgs/runnables receive to msgqueue, this will start a loop that listens msg receiving
        Looper.loop();
    }

    public void periodicRefresh() {
        if(!workerHandler.hasMessages(PartyCardsInterface.GET_TURN_STATUS)) {
            workerHandler.sendEmptyMessageDelayed(PartyCardsInterface.GET_TURN_STATUS, 5000);
        }
    }

    public Handler getHandlerToMsgQueue() {
        return workerHandler;
    }

}