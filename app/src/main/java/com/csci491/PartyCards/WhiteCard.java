package com.csci491.PartyCards;

// ====================================================================================================================
// WhiteCard.java
// --------------------------------------------------------------------------------------------------------------------
// Party Cards: Android Networking Project
// CSCI-466: Networks
// Jeff Arends, Lee Curran, Angela Gross, Andrew Meissner
// Spring 2015
// --------------------------------------------------------------------------------------------------------------------
// Defines a class for a white card that has information about the player associated with it along with its content
// (from parent).
// ====================================================================================================================

public class WhiteCard extends Card
{
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // WHITE CARD ATTRIBUTES
	private Player owner; // owner of the card

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // WHITE CARD CONSTRUCTOR
	// @param content - The text displayed on the card.
	// @param owner - The owner (a Player) of the card.
	public WhiteCard(String content, Player owner)
    {
		this.setContent(content);
	}

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    // GETTERS AND SETTERS FOR WHITE CARD
    // \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

	public void setContent(String content) { super.content = content; }

	public Player getOwner() { return owner; }
	public void setOwner(Player owner) { this.owner = owner; }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
