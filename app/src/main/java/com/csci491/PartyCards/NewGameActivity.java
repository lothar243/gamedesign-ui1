package com.csci491.PartyCards;

import java.util.Collections;

import com.csci491.PartyCards.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class NewGameActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_new_game);
		
		//Set listeners on buttons
		Button buttonCancel = (Button) findViewById(R.id.buttonCancel);
		buttonCancel.setOnClickListener(cancelListener);

		Button buttonNext = (Button) findViewById(R.id.buttonNext);
		buttonNext.setOnClickListener(nextListener);
		
		
		configurePickers();

	}

	private void createGame() {

		System.out.println("Score Limit: " + Globals.getPointLimit());
		System.out.println("Num Players: " + Globals.getNumPlayers());

		// creating players
		System.out.println("Creating the players...");

		for (int i = 0; i < Globals.getNumPlayers(); i++) {
			if (i == 0) {
				// First player is always "You"
				Globals.getPlayers().add(new Player(i, "You", true, false));
			} else if (i == Globals.getNumPlayers() - 1) {
				// last player is by default Czar
				Globals.getPlayers().add(new Player(i, "Player " + (i + 1), true, true));
			} else {
				// other players
				Globals.getPlayers().add(new Player(i, "Player " + (i + 1), true, false));
			}
		}

		System.out.println("Players successfully created!");

		Intent intent = new Intent(NewGameActivity.this, PlayerConfigActivity.class);
		startActivity(intent);
		// finish();

	}

	private void createCards() {
		Globals.getCardMaker().setContext(this);
		Globals.setWhiteCards(Globals.getCardMaker().readWhiteCards());
		Globals.setBlackCards(Globals.getCardMaker().readBlackCards());

		// Shuffles the decks of cards
		Collections.shuffle(Globals.getWhiteCards());
		Collections.shuffle(Globals.getBlackCards());

		System.out.println("Num White Cards: " + Globals.getWhiteCards().size());
		System.out.println("Num Black Cards: " + Globals.getBlackCards().size());
	}
	
	private void configurePickers(){
		NumberPicker numberPickerPlayers = (NumberPicker) findViewById(R.id.numberPickerPlayers);
		NumberPicker numberPickerPointLimit = (NumberPicker) findViewById(R.id.numberPickerPointLimit);
		numberPickerPlayers.setMinValue(3);
		numberPickerPlayers.setMaxValue(7); //more than 7 requires changes in design (newRound)
		numberPickerPlayers.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

		numberPickerPointLimit.setMinValue(2);
		numberPickerPointLimit.setMaxValue(9);
		numberPickerPointLimit.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
	}

	private void showAlert(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(NewGameActivity.this);
		builder.setTitle(message);
		// Add the buttons
		builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
			}
		});

		// Create the AlertDialog
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	// ========================================================================
	// BUTTON LISTENERS
	// ========================================================================
	
	private OnClickListener cancelListener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(NewGameActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
	};
	
	private OnClickListener nextListener = new OnClickListener() {
		public void onClick(View v) {
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

}
