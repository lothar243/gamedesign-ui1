package com.csci491.PartyCards;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.NumberPicker;

import java.util.Collections;

// ====================================================================================================================
// NewGameActivity.java
// --------------------------------------------------------------------------------------------------------------------
// Party Cards: Android Networking Project
// CSCI-466: Networks
// Jeff Arends, Lee Curran, Angela Gross, Andrew Meissner
// Spring 2015
// --------------------------------------------------------------------------------------------------------------------
// Handles the UI listeners and other logic associated with the NewGameActivity
// ====================================================================================================================

public class NewGameActivity extends Activity
{
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // ===============================================================================================================
    // ONCREATE()
    // ---------------------------------------------------------------------------------------------------------------
    // Prepares layout of the NewGameActivity
    // ===============================================================================================================
	public void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_new_game);
		
		//Set listeners on buttons
		Button buttonCancel = (Button) findViewById(R.id.buttonCancel);
		buttonCancel.setOnClickListener(cancelListener);

		Button buttonNext = (Button) findViewById(R.id.buttonNext);
		buttonNext.setOnClickListener(nextListener);

		configurePickers();
	}

    // ===============================================================================================================
    // CREATEGAME()
    // ---------------------------------------------------------------------------------------------------------------
    // Creates players to set up the game
    // ===============================================================================================================
	private void createGame()
    {
		System.out.println("Score Limit: " + Globals.getPointLimit());
		System.out.println("Num Players: " + Globals.getNumPlayers());

		// creating players
		System.out.println("Creating the players...");

		for (int i = 0; i < Globals.getNumPlayers(); i++)
        {
            // first player is always "You"
			if (i == 0)
				Globals.getPlayers().add(new Player(i, "You", true, false));
            // last player is by default Czar
            else if (i == Globals.getNumPlayers() - 1)
				Globals.getPlayers().add(new Player(i, "Player " + (i + 1), true, true));
            // other players
            else
				Globals.getPlayers().add(new Player(i, "Player " + (i + 1), true, false));
		}

		System.out.println("Players successfully created!");

		Intent intent = new Intent(NewGameActivity.this, PlayerConfigActivity.class);
		startActivity(intent);
		// finish();
	}

    // ===============================================================================================================
    // CREATECARDS()
    // ---------------------------------------------------------------------------------------------------------------
    // Creates cards and shuffles them
    // ===============================================================================================================
	private void createCards()
    {
		Globals.getCardMaker().setContext(this);
		Globals.setWhiteCards(Globals.getCardMaker().readWhiteCards());
		Globals.setBlackCards(Globals.getCardMaker().readBlackCards());

		// Shuffles the decks of cards
		Collections.shuffle(Globals.getWhiteCards());
		Collections.shuffle(Globals.getBlackCards());

		System.out.println("Num White Cards: " + Globals.getWhiteCards().size());
		System.out.println("Num Black Cards: " + Globals.getBlackCards().size());
	}

    // ===============================================================================================================
    // CONFIGUREPICKERS()
    // ---------------------------------------------------------------------------------------------------------------
    // Sets min-max number of players
    // ===============================================================================================================
	private void configurePickers()
    {
		NumberPicker numberPickerPlayers = (NumberPicker) findViewById(R.id.numberPickerPlayers);
		NumberPicker numberPickerPointLimit = (NumberPicker) findViewById(R.id.numberPickerPointLimit);
		numberPickerPlayers.setMinValue(3);
		numberPickerPlayers.setMaxValue(7); //more than 7 requires changes in design (newRound)
		numberPickerPlayers.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

		numberPickerPointLimit.setMinValue(2);
		numberPickerPointLimit.setMaxValue(9);
		numberPickerPointLimit.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
	}

    // ===============================================================================================================
    // SHOWALERT()
    // ---------------------------------------------------------------------------------------------------------------
    // Prepares layout of the InGameActivity
    // ===============================================================================================================
	private void showAlert(String message)
    {
		AlertDialog.Builder builder = new AlertDialog.Builder(NewGameActivity.this);
		builder.setTitle(message);
		// Add the buttons
		builder.setNeutralButton("Ok", new DialogInterface.OnClickListener()
        {
			public void onClick(DialogInterface dialog, int id) {}
		});

		// Create the AlertDialog
		AlertDialog dialog = builder.create();
		dialog.show();
	}

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    // BUTTON LISTENERS
    // \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    // ===============================================================================================================
    // CLICK -> CANCELLISTENER
    // ---------------------------------------------------------------------------------------------------------------
    // What happens if we click the "cancel button" on the navigation
    // ===============================================================================================================
	private OnClickListener cancelListener = new OnClickListener()
    {
		public void onClick(View v)
        {
			Intent intent = new Intent(NewGameActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
	};

    // ===============================================================================================================
    // CLICK -> NEXTLISTENER
    // ---------------------------------------------------------------------------------------------------------------
    // What happens if we click the "next button" on the navigation
    // ===============================================================================================================
	private OnClickListener nextListener = new OnClickListener()
    {
		public void onClick(View v)
        {
			// find controls
			NumberPicker numberPickerPlayers = (NumberPicker) findViewById(R.id.numberPickerPlayers);
			NumberPicker numberPickerPointLimit = (NumberPicker) findViewById(R.id.numberPickerPointLimit);

			// Reset everything, to make sure there is nothing from a possible previous game
			Globals.resetGlobals();

			// store variables and create a game
			Globals.setPointLimit(numberPickerPointLimit.getValue());
			Globals.setNumPlayers(numberPickerPlayers.getValue());
			createCards();
			createGame();
		}
	};

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}