package com.csci491.PartyCards;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MultiplayerGameActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingame);

        // TODO fetch current black card
        // TODO fetch the current hand
        // TODO fetch the current player status (Czar or normal player)


        // set question (Black card)
        TextView question = (TextView) findViewById(R.id.textViewQuestion);
        question.setText("TextViewQuestion");

        // set white cards based on player's hand
        Button buttonCard = (Button) findViewById(R.id.buttonCard);
        buttonCard.setText("buttonCard");
        buttonCard.setOnClickListener(cardListener);

        // navigation (white cards)
        Globals.setIndexWhiteCard(0);
        Button buttonLeft = (Button) findViewById(R.id.buttonLeft);
        Button buttonRight = (Button) findViewById(R.id.buttonRight);

        // display card #
        TextView textViewAditionalInfo = (TextView) findViewById(R.id.textViewAditionalInfo);
        textViewAditionalInfo.setVisibility(View.VISIBLE);
        textViewAditionalInfo.setText(Globals.getIndexWhiteCard() + 1 + " / 7");

        // set listeners on navigation
        buttonLeft.setOnClickListener(leftListener);
        buttonRight.setOnClickListener(rightListener);

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
            Button card = (Button) findViewById(R.id.buttonCard);

            Button submit = (Button) findViewById(R.id.buttonSubmit);
            submit.setVisibility(View.GONE);

            // verify if it's possible to go back
            // yes, so go back
            if (Globals.getIndexWhiteCard() > 0)
                Globals.setIndexWhiteCard(Globals.getIndexWhiteCard() - 1);
                // no, so go to last card
            else
                Globals.setIndexWhiteCard(Globals.getPlayers().get(Globals.getIndexHumanPlayer()).getMyHand().size() - 1);

            card.setText("cardText");

            System.out.println("PLAYER CARD: " + Globals.getIndexWhiteCard() + " --- CARD #: " + (Globals.getIndexWhiteCard() + 1));

            TextView textViewAditionalInfo = (TextView) findViewById(R.id.textViewAditionalInfo);
            textViewAditionalInfo.setText(Globals.getIndexWhiteCard() + 1 + " / " + Globals.getHandSize());
        }
    };

    // ===============================================================================================================
    // CLICK -> RIGHTLISTENER
    // ---------------------------------------------------------------------------------------------------------------
    // What happens if we click the "right arrow button" on the navigation (get card if available)
    // ===============================================================================================================
    private View.OnClickListener rightListener = new View.OnClickListener() {
        public void onClick(View v) {
            Button card = (Button) findViewById(R.id.buttonCard);
            Button submit = (Button) findViewById(R.id.buttonSubmit);
            submit.setVisibility(View.GONE);

            // verify if it's possible to go further
            // yes, so go further
            if (Globals.getIndexWhiteCard() < Globals.getPlayers().get(Globals.getIndexHumanPlayer()).getMyHand().size() - 1)
                Globals.setIndexWhiteCard(Globals.getIndexWhiteCard() + 1);
                // no, so go back to fisrt card
            else
                Globals.setIndexWhiteCard(0);

            card.setText("Card text2");

            System.out.println("PLAYER CARD: " + Globals.getIndexWhiteCard() + " --- CARD #: " + (Globals.getIndexWhiteCard() + 1));

            TextView textViewAditionalInfo = (TextView) findViewById(R.id.textViewAditionalInfo);
            textViewAditionalInfo.setText(Globals.getIndexWhiteCard() + 1 + " / " + Globals.getHandSize());
        }
    };

    // ===============================================================================================================
    // CLICK -> CARDLISTENER
    // ---------------------------------------------------------------------------------------------------------------
    // What happens if we click on a card (make the submit button visible)
    // ===============================================================================================================
    private View.OnClickListener cardListener = new View.OnClickListener() {
        public void onClick(View v) {
            // turn the visibility of the "submit" button to TRUE
            Button buttonSubmit = (Button) findViewById(R.id.buttonSubmit);

            if (buttonSubmit.getVisibility() == View.GONE)
                buttonSubmit.setVisibility(View.VISIBLE);
            else
                buttonSubmit.setVisibility(View.GONE);

            // Call the listener of the button
            buttonSubmit.setOnClickListener(submitListener);
        }
    };

    // ===============================================================================================================
    // CLICK -> SUBMITLISTENER
    // ---------------------------------------------------------------------------------------------------------------
    // What happens if we click on the submit button for a card.
    // ===============================================================================================================
    private View.OnClickListener submitListener = new View.OnClickListener() {
        public void onClick(View arg0) {
            //TODO communicate with server that a card has been selected
//            // stores who submitted this card in the property "owner"
//            Globals.getPlayers().get(Globals.getIndexHumanPlayer()).getMyHand().get(Globals.getIndexWhiteCard()).setOwner(Globals.getPlayers().get(Globals.getIndexHumanPlayer()));
//
//            // Calling this method will remove the card from the players hand and make the player draw a new card from the white pile
//            // additionally, if the white pile is empty, the player will re-shuffle the white deck and draw from it
//            Globals.getPlays().add(Globals.getPlayers().get(Globals.getIndexHumanPlayer()).playWhiteCard(Globals.getIndexWhiteCard()));
//
//            Globals.setIndexWhiteCard(0);
//
//            // Redirect to Player Turn screen
//            Intent intent = new Intent(InGameActivity.this, PlayerTurnActivity.class);
//            startActivity(intent);
//            finish();
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
        switch (item.getItemId()) {
            case R.id.action_leavegame:
                AlertDialog.Builder builder = new AlertDialog.Builder(MultiplayerGameActivity.this);
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
