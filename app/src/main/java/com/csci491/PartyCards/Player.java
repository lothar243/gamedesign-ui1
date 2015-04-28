package com.csci491.PartyCards;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;

// ====================================================================================================================
// Player.java
// --------------------------------------------------------------------------------------------------------------------
// Party Cards: Android Networking Project
// CSCI-466: Networks
// Jeff Arends, Lee Curran, Angela Gross, Andrew Meissner
// Spring 2015
// --------------------------------------------------------------------------------------------------------------------
// Defines a class to hold information about and help maintain cares for each player.
// ====================================================================================================================

public class Player extends Activity
{
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private ArrayList<WhiteCard> myHand;
	private int myIndex;
	private String name;
	private boolean isHuman;
	private boolean isCzar;
	private int score;
	private int numPlayers; // awareness of other players
	private boolean playedAlready = false;
	private Context c;

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// DEFAULT CONSTRUCTOR FOR PLAYER
	public Player() {
		myHand = new ArrayList<WhiteCard>();
	}

	// CONSTRUCTOR FOR PLAYER
	// @param isHuman - Determines whether the player is human (true) or a computer (false).
	public Player(boolean isHuman)
     {
		setHuman(isHuman);
		myHand = new ArrayList<WhiteCard>();
		isCzar = false;
	}

	// CONSTRUCTOR FOR PLAYER
	// @param name - The name of the player.
	public Player(String name)
    {
		setName(name);
		myHand = new ArrayList<WhiteCard>();
		isCzar = false;
	}

	// CONSTRUCTOR FOR PLAYER
	// @param name - The name of the player.
	// @param isHuman - Determines whether the player is human (true) or a computer (false).
	// @param isCzar - Determines whether the player is acting as the card czar (true) or not (false).
	public Player(String name, boolean isHuman, boolean isCzar)
    {
		setName(name);
		setHuman(isHuman);
		this.isCzar = isCzar;
		myHand = new ArrayList<WhiteCard>();
	}

	// CONSTRUCTOR FOR PLAYER
	// @param index - Index of the player - given upon initialization in a for-loop.
	// @param name - The name of the player.
	// @param isHuman - Determines whether the player is human (true) or a computer (false).
	// @param isCzar - Determines whether the player is acting as the card czar (true) or not (false).
	public Player(int index, String name, boolean isHuman, boolean isCzar)
    {
		setMyIndex(index);
		setName(name);
		setHuman(isHuman);
		this.isCzar = isCzar;
		myHand = new ArrayList<WhiteCard>();
	}

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // ===============================================================================================================
    // PLAYWHITECARD()
    // ---------------------------------------------------------------------------------------------------------------
    // Plays a white card from the player's hand.  The player then draws another card from the white pile.
    // @return Plays the first white card in the player's hand.
    // ===============================================================================================================
	public WhiteCard playWhiteCard()
    {
		WhiteCard play = myHand.remove(0);
		draw();
		return play;
	}

    // ===============================================================================================================
    // PLAYWHITECARD()
    // ---------------------------------------------------------------------------------------------------------------
    // Plays a specific white card from the player's hand.  The player then draws another card from the white pile.
    // @param index - The index of the card being played.
    // @return Plays the card identified by the given index in the player's hand.
    // ===============================================================================================================
	public WhiteCard playWhiteCard(int index)
    {
		WhiteCard play = myHand.remove(index);
		draw();
		return play;
	}

    // ===============================================================================================================
    // DRAW()
    // ---------------------------------------------------------------------------------------------------------------
    // Draws a card from the white pile.  The player also takes ownership of the drawn card.
    // ===============================================================================================================
	public void draw()
    {
		if (!Globals.getWhiteCards().isEmpty())
        {
			WhiteCard newCard = Globals.getWhiteCards().remove(0);
			newCard.setOwner(this);
			myHand.add(newCard);
		}
        else
        {
			shuffleWhiteCards();
			draw();
		}
	}

    // ===============================================================================================================
    // SHUFFLEWHITECARDS()
    // ---------------------------------------------------------------------------------------------------------------
    // Re-reads in the deck of white cards, and shuffles them.
    // ===============================================================================================================
	private void shuffleWhiteCards()
    {
		FileIO myFileIO = new FileIO(this.c);
		Globals.setWhiteCards(myFileIO.readWhiteCards());
		Collections.shuffle(Globals.getWhiteCards());
	}

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    // GETTERS AND SETTERS PLAYER
    // \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	
	// Determines whether the player is acting as the card czar or not.
	public boolean isCzar() { return isCzar;}
	public void setCzar(boolean isCzar) { this.isCzar = isCzar; }
	
	// Tell whether or not the player has played already.
	public boolean isPlayedAlready() { return playedAlready; }
	public void setPlayedAlready(boolean playedAlready) { this.playedAlready = playedAlready; }

    // The initial index of the player in the player queue.
    public int getMyIndex() { return myIndex;}
    public void setMyIndex(int myIndex) { this.myIndex = myIndex; }

    // Tells you whether the player is human or not.
    public boolean isHuman() {return isHuman; }
    public void setHuman(boolean isHuman) { this.isHuman = isHuman; }

    // The current score of the player.
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    // Gives the number of players that this player is aware of.
    public int getNumPlayers() { return numPlayers; }
    public void setNumPlayers(int numPlayers) { this.numPlayers = numPlayers; }

    /// The card hand of the player.
    public ArrayList<WhiteCard> getMyHand() { return myHand; }
    public void setMyHand(ArrayList<WhiteCard> myHand) { this.myHand = myHand; }

    // The name of the player.
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
