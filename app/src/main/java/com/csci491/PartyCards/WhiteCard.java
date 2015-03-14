package com.csci491.PartyCards;

public class WhiteCard extends Card {
	private Player owner;
	
	/**
	 * Constructor for the white card.
	 * @param content - The text displayed on the card.
	 * @param owner - The owner (a Player) of the card.
	 */
	public WhiteCard(String content, Player owner) {
		this.setContent(content);
	}
	
	@Override
	public void setContent(String content) {
		super.content = content;
	}
	
	/**
	 * Returns the owner of the card.
	 * @return - An instance of the Player class.
	 */
	public Player getOwner() {
		return owner;
	}
	
	/**
	 * Sets the owner of the card.
	 * @param owner - An instance of the Player class.
	 */
	public void setOwner(Player owner) {
		this.owner = owner;
	}
	
}
