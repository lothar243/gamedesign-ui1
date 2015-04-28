package com.csci491.PartyCards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

// ====================================================================================================================
// InGameActivity.java
// --------------------------------------------------------------------------------------------------------------------
// Party Cards: Android Networking Project
// CSCI-466: Networks
// Jeff Arends, Lee Curran, Angela Gross, Andrew Meissner
// Spring 2015
// --------------------------------------------------------------------------------------------------------------------
// Handles the UI listeners and other logic associated with the PlayerTurnActivity
// ====================================================================================================================

public class PlayerTurnActivity extends Activity
{
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // PLAYERTURNACTIVITY ATTRIBUTES
	private static int indexCzarSkipped = -1;

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // ===============================================================================================================
    // ONCREATE()
    // ---------------------------------------------------------------------------------------------------------------
    // Prepares layout of the PlayerTurnActivity
    // ===============================================================================================================
	public void onCreate(Bundle savedInstanceState)
    {
		// this method may change the theme if there's a winner for the game. remember: changing themes must be the
		// very first thing to do
		changeThemeIfWinnerGame();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player_turn);

		nextCzar();
		nextPlayer();
		lookForWinner();
	}

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // ===============================================================================================================
    // NEXTCZAR()
    // ---------------------------------------------------------------------------------------------------------------
    // Choose the next czar.
    // ===============================================================================================================
	public void nextCzar()
    {
		if (Globals.getPlayers().get(Globals.getIndexHumanPlayer()).isCzar())
        {
			Globals.getPlayers().get(Globals.getIndexHumanPlayer()).setCzar(false);

			// choose the new Czar
			if (Globals.getIndexHumanPlayer() > 0)
				Globals.getPlayers().get(Globals.getIndexHumanPlayer() - 1).setCzar(true);
			else
				Globals.getPlayers().get(Globals.getNumPlayers() - 1).setCzar(true);
		}
	}

    // ===============================================================================================================
    // NEXTPLAYER()
    // ---------------------------------------------------------------------------------------------------------------
    // Play the round for each player (currently just passing around).
    // TODO: There may be a bug with the Czar in this method.
    // ===============================================================================================================
	public void nextPlayer()
    {
		// Is there a Czar skipped?
		if (Globals.getIndexHumanPlayer() == indexCzarSkipped)
        {
			// Yes, make him play and finalize this round
			indexCzarSkipped = -1;
			playCzarPlayer();

		}
        else
        {
			// No, go to next player
			changePlayer();

			for (int i = 0; i < Globals.getNumPlayers(); i++)
            {
				// this one already played?
				if (!Globals.getPlayers().get(i).isPlayedAlready())
                {
					// is Czar?
					if (!Globals.getPlayers().get(Globals.getIndexHumanPlayer()).isCzar())
                    {
						// no, so play!
						playNormalPlayer();
					}
                    else
                    {
						// yes it's Czar
						if (Globals.getIndexHumanPlayer() == Globals.getNumPlayers() - 1)
                        {
							// if it's the last one to play, play!
							playCzarPlayer();
						}
                        else
                        {
							// it's not the last one to play, so store his index to play later
							indexCzarSkipped = Globals.getIndexHumanPlayer();
							
							// skip him for now!
							changePlayer();
						}
					}
				}
			}

			if (indexCzarSkipped != -1 && !Globals.getPlayers().get(indexCzarSkipped).isPlayedAlready())
				Globals.setIndexHumanPlayer(indexCzarSkipped);
		}
	}

    // ===============================================================================================================
    // CHANGEPLAYER()
    // ---------------------------------------------------------------------------------------------------------------
    // Change player index
    // ===============================================================================================================
	public void changePlayer()
    {
		if (Globals.getIndexHumanPlayer() < Globals.getNumPlayers() - 1)
			Globals.setIndexHumanPlayer(Globals.getIndexHumanPlayer() + 1);
		else
			Globals.setIndexHumanPlayer(0);
	}

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // ===============================================================================================================
    // PLAYCZARPLAYER()
    // ---------------------------------------------------------------------------------------------------------------
    // Set up layout when it's the czar's turn
    // ===============================================================================================================
	public void playCzarPlayer()
    {
		TextView textViewPlayerTurnMessage = (TextView) findViewById(R.id.textViewPlayerTurnMessage);
		Button buttonContinue = (Button) findViewById(R.id.buttonContinue);

		textViewPlayerTurnMessage.setText(Globals.getPlayers().get(Globals.getIndexHumanPlayer()).getName() + " You're the card Czar!");

		Globals.getPlayers().get(Globals.getIndexHumanPlayer()).setPlayedAlready(true);

		buttonContinue.setOnClickListener(new OnClickListener()
        {
			public void onClick(View v)
            {
				Intent intent = new Intent(PlayerTurnActivity.this, CzarActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

    // ===============================================================================================================
    // PLAYNORMALPLAYER()
    // ---------------------------------------------------------------------------------------------------------------
    // Set up layout when it's a normal player's turn
    // ===============================================================================================================
	public void playNormalPlayer()
    {
		TextView textViewPlayerTurnMessage = (TextView) findViewById(R.id.textViewPlayerTurnMessage);
		Button buttonContinue = (Button) findViewById(R.id.buttonContinue);

		textViewPlayerTurnMessage.setText("It's your turn " + Globals.getPlayers().get(Globals.getIndexHumanPlayer()).getName() + " !");

		Globals.getPlayers().get(Globals.getIndexHumanPlayer()).setPlayedAlready(true);

		buttonContinue.setOnClickListener(new OnClickListener()
        {
			public void onClick(View v)
            {
				Intent intent = new Intent(PlayerTurnActivity.this, InGameActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // ===============================================================================================================
    // CHANGETHEMEIFWINNERGAME()
    // ---------------------------------------------------------------------------------------------------------------
    // We look for a winner and, if found, the Holo Light theme is applied to the winner.
    // ===============================================================================================================
	public void changeThemeIfWinnerGame()
    {
		for (int i = 0; i < Globals.getNumPlayers(); i++)
        {
			if (Globals.getPlayers().get(i).getScore() == Globals.getPointLimit())
            {
				setTheme(android.R.style.Theme_Holo_Light);
				Globals.setIsGameWinner(true);
			}
		}
	}

    // ===============================================================================================================
    // LOOKFORWINNER()
    // ---------------------------------------------------------------------------------------------------------------
    // Uses methods lookForWinnerRound and Game to find the winner.
    // ===============================================================================================================
	public void lookForWinner()
    {
		lookForWinnerRound();
		lookForWinnerGame();
	}

    // ===============================================================================================================
    // LOOKFORWINNERROUND()
    // ---------------------------------------------------------------------------------------------------------------
    // Sets up layout to say who won that round along with being able to review cards.
    // ===============================================================================================================
	public void lookForWinnerRound()
    {
		TextView textViewPlayerTurnMessage = (TextView) findViewById(R.id.textViewPlayerTurnMessage);

		if (Globals.isRoundWinner())
        {
			textViewPlayerTurnMessage.setText(Globals.getWinnerName() + " won this round!");

			// reset control for players that already played in a round
			for (int i = 0; i < Globals.getNumPlayers(); i++)
				Globals.getPlayers().get(i).setPlayedAlready(false);

			Button buttonContinue = (Button) findViewById(R.id.buttonContinue);
			buttonContinue.setText("Review all cards played this round");
			buttonContinue.setOnClickListener(new OnClickListener()
            {
				public void onClick(View v)
                {
					Intent intent = new Intent(PlayerTurnActivity.this, CzarActivity.class);
					startActivity(intent);
					finish();
				}
			});

			Globals.setIndexHumanPlayer(0);
		}
	}

    // ===============================================================================================================
    // LOOKFORWINNERGAME()
    // ---------------------------------------------------------------------------------------------------------------
    // Sets up layout to say who won if it's the end of the game
    // ===============================================================================================================
	public void lookForWinnerGame()
    {
		// Game winner after changing theme
		if (Globals.isGameWinner())
        {
			TextView textViewPlayerTurnMessage = (TextView) findViewById(R.id.textViewPlayerTurnMessage);
			TextView textViewPlayerTurnWinnerMessage = (TextView) findViewById(R.id.textViewPlayerTurnWinnerMessage);

			for (int i = 0; i < Globals.getNumPlayers(); i++)
            {
				if (Globals.getPlayers().get(i).getScore() == Globals.getPointLimit())
                {
					textViewPlayerTurnMessage.setText("Congratulations " + Globals.getPlayers().get(i).getName() + "!");
					textViewPlayerTurnWinnerMessage.setText(R.string.game_winner);
				}

			}

			Button buttonContinue = (Button) findViewById(R.id.buttonContinue);
			buttonContinue.setText("Review all cards played this round");
			buttonContinue.setOnClickListener(new OnClickListener()
            {
				public void onClick(View v) {
					Intent intent = new Intent(PlayerTurnActivity.this, CzarActivity.class);
					startActivity(intent);
					finish();
				}
			});

		}
	}

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
