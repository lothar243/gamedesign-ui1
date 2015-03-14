package com.csci491.PartyCards;

import com.csci491.PartyCards.R;

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

public class InGameActivity extends Activity {
	// @SuppressLint("ResourceAsColor")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ingame);
		
//		System.out.println("InGameActivity: WhiteCards: " + Globals.getWhiteCards().size());
//		System.out.println("InGameActivity: BlackCards: " + Globals.getBlackCards().size());
		
		for (int i = 0; i < Globals.getPlayers().get(Globals.getIndexHumanPlayer()).getMyHand().size(); i++) {
			System.out.println(Globals.getPlayers().get(Globals.getIndexHumanPlayer()).getMyHand().get(i).getOwner().getName() + " owns " + Globals.getPlayers().get(Globals.getIndexHumanPlayer()).getMyHand().get(i).getContent());
		}
		
		// If this screen is displayed again for another player but in the same
		// round the question
		// must not change. Change only if it's another round. Property
		// "NewBlackCard" defines
		// whether it's a new round or not.
		if (Globals.changeBlackCard()) {
			Globals.getBlackCards().remove(0);
			Globals.setChangeBlackCard(false);
		}
		
		// Set question (Black card)
		TextView question = (TextView) findViewById(R.id.textViewQuestion);
		question.setText(Globals.getBlackCards().get(0).getContent());
		
		// Set white cards based on player's hand
		Button buttonCard = (Button) findViewById(R.id.buttonCard);
		buttonCard.setText(Globals.getPlayers().get(Globals.getIndexHumanPlayer()).getMyHand().get(0).getContent());
		buttonCard.setOnClickListener(cardListener);
		
		// navigation (white cards)
		Globals.setIndexWhiteCard(0);
		Button buttonLeft = (Button) findViewById(R.id.buttonLeft);
		Button buttonRight = (Button) findViewById(R.id.buttonRight);
		
		// display card #
		TextView textViewAditionalInfo = (TextView) findViewById(R.id.textViewAditionalInfo);
		textViewAditionalInfo.setVisibility(0);
		textViewAditionalInfo.setText(Globals.getIndexWhiteCard() + 1 + " / 7");
		
		// Set listeners on navigation
		buttonLeft.setOnClickListener(leftListener);
		buttonRight.setOnClickListener(rightListener);
		
	}
	
	// ========================================================================
	// BUTTON LISTENERS
	// ========================================================================
	
	private OnClickListener leftListener = new OnClickListener() {
		public void onClick(View v) {
			Button card = (Button) findViewById(R.id.buttonCard);
			
			Button submit = (Button) findViewById(R.id.buttonSubmit);
			submit.setVisibility(View.GONE);
			
			// verify if it's possible to go back
			if (Globals.getIndexWhiteCard() > 0) {
				// yes, so go back
				Globals.setIndexWhiteCard(Globals.getIndexWhiteCard() - 1);
			} else {
				// no, so go to last card
				Globals.setIndexWhiteCard(Globals.getPlayers().get(Globals.getIndexHumanPlayer()).getMyHand().size() - 1);
			}
			card.setText(Globals.getPlayers().get(Globals.getIndexHumanPlayer()).getMyHand().get(Globals.getIndexWhiteCard()).getContent());
			
			System.out.println("PLAYER CARD: " + Globals.getIndexWhiteCard() + " --- CARD #: " + (Globals.getIndexWhiteCard() + 1));
			
			TextView textViewAditionalInfo = (TextView) findViewById(R.id.textViewAditionalInfo);
			textViewAditionalInfo.setText(Globals.getIndexWhiteCard() + 1 + " / " + Globals.getHandSize());
			
		}
	};
	
	private OnClickListener rightListener = new OnClickListener() {
		public void onClick(View v) {
			Button card = (Button) findViewById(R.id.buttonCard);
			Button submit = (Button) findViewById(R.id.buttonSubmit);
			submit.setVisibility(View.GONE);
			
			// verify if it's possible to go further
			if (Globals.getIndexWhiteCard() < Globals.getPlayers().get(Globals.getIndexHumanPlayer()).getMyHand().size() - 1) {
				// yes, so go further
				Globals.setIndexWhiteCard(Globals.getIndexWhiteCard() + 1);
			} else {
				// no, so go back to fist card
				Globals.setIndexWhiteCard(0);
			}
			card.setText(Globals.getPlayers().get(Globals.getIndexHumanPlayer()).getMyHand().get(Globals.getIndexWhiteCard()).getContent());
			
			System.out.println("PLAYER CARD: " + Globals.getIndexWhiteCard() + " --- CARD #: " + (Globals.getIndexWhiteCard() + 1));
			
			TextView textViewAditionalInfo = (TextView) findViewById(R.id.textViewAditionalInfo);
			textViewAditionalInfo.setText(Globals.getIndexWhiteCard() + 1 + " / " + Globals.getHandSize());
		}
	};
	
	private OnClickListener cardListener = new OnClickListener() {
		public void onClick(View v) {
			// turn the visibility of the "submit" button to TRUE
			Button buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
			
			if (buttonSubmit.getVisibility() == View.GONE) {
				buttonSubmit.setVisibility(View.VISIBLE);
			} else {
				buttonSubmit.setVisibility(View.GONE);
			}
			
			System.out.println(Globals.getPlayers().get(Globals.getIndexHumanPlayer()).getMyHand().get(Globals.getIndexWhiteCard()).getContent());
			
			// Call the listener of the button
			buttonSubmit.setOnClickListener(submitListener);
		}
	};
	
	private OnClickListener submitListener = new OnClickListener() {
		public void onClick(View arg0) {
			// stores who submitted this card in the property "owner"
			Globals.getPlayers().get(Globals.getIndexHumanPlayer()).getMyHand().get(Globals.getIndexWhiteCard()).setOwner(Globals.getPlayers().get(Globals.getIndexHumanPlayer()));
			
			// Calling this method will remove the card from the players hand
			// and make the player draw a new card from the white pile
			// additionally, if the white pile is empty, the player will
			// re-shuffle the white deck and draw from it
			Globals.getPlays().add(Globals.getPlayers().get(Globals.getIndexHumanPlayer()).playWhiteCard(Globals.getIndexWhiteCard()));
			
			Globals.setIndexWhiteCard(0);
			
			// Redirect to Player Turn screen
			Intent intent = new Intent(InGameActivity.this, PlayerTurnActivity.class);
			startActivity(intent);
			finish();
		}
	};
	
	// ========================================================================
	// MENU
	// ========================================================================
	
	// Show menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ingame, menu);
		
		return true;
	}
	
	// Menu:
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
			case R.id.action_leavegame:
				AlertDialog.Builder builder = new AlertDialog.Builder(InGameActivity.this);
				builder.setTitle(R.string.leave_game_message);
				// Add the buttons
				builder.setPositiveButton(R.string.leave_game, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// Leave Game
						Intent intent = new Intent(InGameActivity.this, MainActivity.class);
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
