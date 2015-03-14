package com.csci491.PartyCards;

public class BlackCard extends Card {
	
	protected int blanks;

	/**
	 * Constructor for a black card.
	 * @param content - The text displayed on the card.
	 */
	public BlackCard(String content) {
		setContent(content);
	}
	
	
	@Override
	public void setContent(String content) {
		int numBlanks = 0;
		
		if (content.contains("<blank>")) {
			String[] words = content.split(" ");
			for (int i = 0; i < words.length; i++) {
				if (words[i].equalsIgnoreCase("<blank>")) {
					numBlanks++;
				}
			}
		}
		
		setBlanks(numBlanks);
		super.content = content;
		
	}
	
	/**
	 * Sets the number of blanks on a black card, if any.
	 * @param blanks - The number of blank spaces on the card.
	 */
	public void setBlanks(int blanks) {
		this.blanks = blanks;
	}
	
	/**
	 * Returns the number of blank spaces on the card.
	 * @return - The number of blank spaces on the card.
	 */
	public int getBlanks() {
		return this.blanks;
	}
	
}
