package com.csci491.PartyCards;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

// ====================================================================================================================
// CzarActivity.java
// --------------------------------------------------------------------------------------------------------------------
// Party Cards: Android Networking Project
// CSCI-466: Networks
// Jeff Arends, Lee Curran, Angela Gross, Andrew Meissner
// Spring 2015
// --------------------------------------------------------------------------------------------------------------------
// Handles the UI listeners and other logic associated with the CzarActivity
// ====================================================================================================================

public class CzarActivity extends Activity
{
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // ===============================================================================================================
    // ONCREATE()
    // ---------------------------------------------------------------------------------------------------------------
    // Prepares layout of the CzarActivity (cards, navigation, etc.)
    // ===============================================================================================================
	public void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ingame);

        // PREPARE LAYOUT FOR REVIEW CARDS (END OF A ROUND)
		if (Globals.isRoundWinner())
        {
			// change submit button
			Button submit = (Button) findViewById(R.id.buttonSubmit);
			submit.setVisibility(View.VISIBLE);
			submit.setText("Continue");

			setOwnerNameOnCard();

			// display helper
			TextView textViewHelper = (TextView) findViewById(R.id.textViewHelper);
			textViewHelper.setText(R.string.helperInGameReview);
		}
        // PREPARE LAYOUT FOR CZAR VIEW
        else
        {
			Button buttonCard = (Button) findViewById(R.id.buttonCard);
			buttonCard.setOnClickListener(cardListener);

			// display helper
			TextView textViewHelper = (TextView) findViewById(R.id.textViewHelper);
			textViewHelper.setText(R.string.helperInGameCzar);
		}

		// PREPARE WHAT'S THE SAME FOR BOTH VIEWS
		// Set question (Black card)
		TextView question = (TextView) findViewById(R.id.textViewQuestion);
		question.setText(Globals.getBlackCards().get(0).getContent());

		// set white cards based on other players' submission and call the listener of the button
		Button buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
		buttonSubmit.setOnClickListener(submitListener);

		Button buttonCard = (Button) findViewById(R.id.buttonCard);
		buttonCard.setTextColor(getApplication().getResources().getColor(R.color.white));
		buttonCard.setBackgroundColor(getApplication().getResources().getColor(R.color.black));
		buttonCard.setText(Globals.getPlays().get(Globals.getIndexWhiteCard()).getContent());

		// navigation (white cards)
		Globals.setIndexWhiteCard(0);
		Button buttonLeft = (Button) findViewById(R.id.buttonLeft);
		Button buttonRight = (Button) findViewById(R.id.buttonRight);

		// set listeners on navigation
		buttonLeft.setOnClickListener(leftListener);
		buttonRight.setOnClickListener(rightListener);

		for (int i = 0; i < Globals.getPlays().size(); i++)
			System.out.println(Globals.getPlays().get(i).getOwner().getName() + " owns " + Globals.getPlays().get(i).getContent());

	}

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // ===============================================================================================================
    // SETOWNERNAMEONCARD()
    // ---------------------------------------------------------------------------------------------------------------
    // As the name suggests, get the winner's name of the card that was chosen
    // ===============================================================================================================
	private void setOwnerNameOnCard()
    {
		TextView textViewAditionalInfo = (TextView) findViewById(R.id.textViewAditionalInfo);
		textViewAditionalInfo.setVisibility(View.GONE);
		Player owner = Globals.getPlays().get(Globals.getIndexWhiteCard()).getOwner();
		textViewAditionalInfo.setText(owner.getName());
		if (owner.getName().equals(Globals.getWinnerName()))
			textViewAditionalInfo.setText(textViewAditionalInfo.getText() + " " + getResources().getString(R.string.winner));
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
	private OnClickListener leftListener = new OnClickListener()
    {
		public void onClick(View v)
        {
			Button card = (Button) findViewById(R.id.buttonCard);
			Button submit = (Button) findViewById(R.id.buttonSubmit);

			// verify if it's possible to go back
			if (Globals.getIndexWhiteCard() > 0)
				Globals.setIndexWhiteCard(Globals.getIndexWhiteCard() - 1);
			else
				Globals.setIndexWhiteCard(Globals.getNumPlayers() - 2);

			card.setText(Globals.getPlays().get(Globals.getIndexWhiteCard()).getContent());

			if (!Globals.isRoundWinner())
				submit.setVisibility(View.VISIBLE);
			else
				setOwnerNameOnCard();
		}
	};

    // ===============================================================================================================
    // CLICK -> RIGHTLISTENER
    // ---------------------------------------------------------------------------------------------------------------
    // What happens if we click the "right arrow button" on the navigation (get card if available)
    // ===============================================================================================================
	private OnClickListener rightListener = new OnClickListener()
    {
		public void onClick(View v)
        {
			Button card = (Button) findViewById(R.id.buttonCard);
			Button submit = (Button) findViewById(R.id.buttonSubmit);

			// verify if it's possible to go further
			if (Globals.getIndexWhiteCard() < Globals.getNumPlayers() - 2)
				Globals.setIndexWhiteCard(Globals.getIndexWhiteCard() + 1);
			else
				Globals.setIndexWhiteCard(0);

			card.setText(Globals.getPlays().get(Globals.getIndexWhiteCard()).getContent());

			if (!Globals.isRoundWinner())
				submit.setVisibility(View.GONE);
			else
				setOwnerNameOnCard();
		}
	};

    // ===============================================================================================================
    // CLICK -> CARDLISTENER
    // ---------------------------------------------------------------------------------------------------------------
    // What happens if we click on a card (make the submit button visible)
    // ===============================================================================================================
	private OnClickListener cardListener = new OnClickListener()
    {
		public void onClick(View v)
        {
			// turn the visibility of the "submit" button to TRUE
			Button buttonSubmit = (Button) findViewById(R.id.buttonSubmit);

			if (buttonSubmit.getVisibility() == View.GONE)
				buttonSubmit.setVisibility(View.VISIBLE);
			else
				buttonSubmit.setVisibility(View.GONE);
		}
	};

    // ===============================================================================================================
    // CLICK -> SUBMITLISTENER
    // ---------------------------------------------------------------------------------------------------------------
    // What happens if we click on the submit button for a card.
    // ===============================================================================================================
	private OnClickListener submitListener = new OnClickListener()
    {
		public void onClick(View arg0)
        {
            // just reviewing cards and game is over: Back to main screen
			if (Globals.isGameWinner())
            {
				Intent intent = new Intent(CzarActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
            // just reviewing cards and the game is not over: Go to next round
            else if (Globals.isRoundWinner())
            {
				Intent intent = new Intent(CzarActivity.this, NewRoundActivity.class);
				startActivity(intent);
				finish();
			}
            // card Czar screen: add point to the player who chose the card
            else
            {
				Player player = Globals.getPlays().get(Globals.getIndexWhiteCard()).getOwner();
				int newScore = player.getScore() + 1;
				Globals.getPlays().get(Globals.getIndexWhiteCard()).getOwner().setScore(newScore);
				Globals.setWinnerName(player.getName());

				// allows to set a new black card
				Globals.setChangeBlackCard(true);

				// Changes the player
				Globals.setIsRoundWinner(true);

				Globals.setIndexWhiteCard(0);

				// Redirect to PlayerTurnActivity, now with a message for the winner
				// Later in that class player will be redirected to NewRoundActivity
				Intent intent = new Intent(CzarActivity.this, PlayerTurnActivity.class);
				startActivity(intent);
				finish();
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
		switch (item.getItemId())
        {
            case R.id.action_leavegame:
                AlertDialog.Builder builder = new AlertDialog.Builder(CzarActivity.this);
                builder.setTitle(R.string.leave_game_message);

                // Add the buttons
                builder.setPositiveButton(R.string.leave_game, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        // Leave Game
                        Intent intent = new Intent(CzarActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
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

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
