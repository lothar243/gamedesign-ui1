package com.csci491.PartyCards;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.Context;

public class Player extends Activity {
	
	private ArrayList<WhiteCard> myHand;
	private int myIndex;
	private String name;
	private boolean isHuman;
	private boolean isCzar;
	private int score;
	private int numPlayers; // awareness of other players
	private boolean playedAlready = false;
	private Context c;
	
	/**
	 * Default constructor.
	 */
	public Player() {
		myHand = new ArrayList<WhiteCard>();
	}
	
	/**
	 * Constructor for player.
	 * @param isHuman - Determines whether the player is human (true) or a computer (false).
	 */
	public Player(boolean isHuman) {
		setHuman(isHuman);
		myHand = new ArrayList<WhiteCard>();
		isCzar = false;
	}
	
	/**
	 * Constructor for player.
	 * @param name - The name of the player.
	 */
	public Player(String name) {
		setName(name);
		myHand = new ArrayList<WhiteCard>();
		isCzar = false;
	}
	
	/**
	 * Constructor for player.
	 * @param name - The name of the player.
	 * @param isHuman - Determines whether the player is human (true) or a computer (false).
	 * @param isCzar - Determines whether the player is acting as the card czar (true) or not (false).
	 */
	public Player(String name, boolean isHuman, boolean isCzar) {
		setName(name);
		setHuman(isHuman);
		this.isCzar = isCzar;
		myHand = new ArrayList<WhiteCard>();
	}
	
	/**
	 * Constructor for player.
	 * @param index - Index of the player - given upon initialization in a for-loop.
	 * @param name - The name of the player.
	 * @param isHuman - Determines whether the player is human (true) or a computer (false).
	 * @param isCzar - Determines whether the player is acting as the card czar (true) or not (false).
	 */
	public Player(int index, String name, boolean isHuman, boolean isCzar) {
		setMyIndex(index);
		setName(name);
		setHuman(isHuman);
		this.isCzar = isCzar;
		myHand = new ArrayList<WhiteCard>();
	}
	
	/**
	 * Returns the initial index of the player in the player queue.
	 * @return The player's initial index in the queue of players. 
	 */
	public int getMyIndex() {
		return myIndex;
	}

	/**
	 * Sets the index of the player.
	 * @param myIndex - Index of the player in the queue of players.
	 */
	public void setMyIndex(int myIndex) {
		this.myIndex = myIndex;
	}

	/**
	 * Tells you whether the player is human or not.
	 * @return True if the player is human.  False of the player is computer.
	 */
	public boolean isHuman() {
		return isHuman;
	}
	
	/**
	 * Sets the player to be human or computer.
	 * @param isHuman - Determines whether the player is human (true) or a computer (false).
	 */
	public void setHuman(boolean isHuman) {
		this.isHuman = isHuman;
	}
	
	/**
	 * The current score of the player.
	 * @return The score of the player.
	 */
	public int getScore() {
		return score;
	}
	
	/**
	 * Set, or update, the score of the player.
	 * @param score - The new score of the player.
	 */
	public void setScore(int score) {
		this.score = score;
	}
	
	/**
	 * Gives the number of players that this player is aware of.
	 * @return The number of players that this player is aware of.
	 */
	public int getNumPlayers() {
		return numPlayers;
	}
	
	/**
	 * Sets the player aware of the number of other players.
	 * @param numPlayers - The number of total players, including this player.
	 */
	public void setNumPlayers(int numPlayers) {
		this.numPlayers = numPlayers;
	}
	
	/**
	 * The card hand of the player.
	 * @return An ArrayList (White Cards) of the current cards of the player.
	 */
	public ArrayList<WhiteCard> getMyHand() {
		return myHand;
	}
	
	/**
	 * Sets the hand of the player, if a previous ArrayList has already been established.
	 * @param myHand - A previously established ArrayList of white cards.
	 */
	public void setMyHand(ArrayList<WhiteCard> myHand) {
		this.myHand = myHand;
	}
	
	/**
	 * The name of the player.
	 * @return - The name of the player.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set the name of the player.
	 * @param name - The new name of the player.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Plays a white card from the player's hand.  The player then draws another card from the white pile.
	 * @return Plays the first white card in the player's hand.
	 */
	public WhiteCard playWhiteCard() {
		WhiteCard play = myHand.remove(0);
		draw();
		return play;
	}
	
	/**
	 * Plays a specific white card from the player's hand.  The player then draws another card from the white pile.
	 * @param index - The index of the card being played.
	 * @return Plays the card identified by the given index in the player's hand.
	 */
	public WhiteCard playWhiteCard(int index) {
		WhiteCard play = myHand.remove(index);
		draw();
		return play;
	}
	
	/**
	 * Draws a card from the white pile.  The player also takes ownership of the drawn card.
	 */
	public void draw() {
		if (!Globals.getWhiteCards().isEmpty()) {
			WhiteCard newCard = Globals.getWhiteCards().remove(0);
			newCard.setOwner(this);
			myHand.add(newCard);
		} else {
			shuffleWhiteCards();
			draw();
		}
	}
	
	/**
	 * Re-reads in the deck of white cards, and shuffles them.
	 */
	private void shuffleWhiteCards() {
		FileIO myFileIO = new FileIO(this.c);
		Globals.setWhiteCards(myFileIO.readWhiteCards());
		Collections.shuffle(Globals.getWhiteCards());
	}
	
	/**
	 * Determines whether the player is acting as the card czar or not.
	 * @return True if the player is the card czar, and false otherwise.
	 */
	public boolean isCzar() {
		return isCzar;
	}
	
	/**
	 * Sets the player as the card czar, or not, depending on the value.
	 * @param isCzar - True if the player will act as the card czar, and false if not.
	 */
	public void setCzar(boolean isCzar) {
		this.isCzar = isCzar;
	}
	
	/**
	 * Tell whether or not the player has played already.
	 * @return - True if the player has played, and false otherwise.
	 */
	public boolean isPlayedAlready() {
		return playedAlready;
	}
	
	/**
	 * Sets whether or not the player has already played.
	 * @param playedAlready - True if the players has played, and false otherwise.
	 */
	public void setPlayedAlready(boolean playedAlready) {
		this.playedAlready = playedAlready;
	}
	
}
