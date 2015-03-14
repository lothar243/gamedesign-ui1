package com.csci491.PartyCards;

public abstract class Card {
	
	protected String content;
	
	/**
	 * Sets the content, the text, of the card.
	 * @param content - The text displayed on the card.
	 */
	public abstract void setContent(String content);
	
	/**
	 * Returns the content on of the card.
	 * @return - The content of the card.
	 */
	public String getContent() {
		return this.content;
	}
	
}
