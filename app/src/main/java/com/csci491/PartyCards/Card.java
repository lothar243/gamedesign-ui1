package com.csci491.PartyCards;

// ====================================================================================================================
// Card.java
// --------------------------------------------------------------------------------------------------------------------
// Party Cards: Android Networking Project
// CSCI-466: Networks
// Jeff Arends, Lee Curran, Angela Gross, Andrew Meissner
// Spring 2015
// --------------------------------------------------------------------------------------------------------------------
// Basic card class that all cards derive from that simply holds the content inside the card.
// ====================================================================================================================

public abstract class Card
{
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // CARD ATTRIBUTES
	protected String content; // text that's inside the card

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    // GETTERS AND SETTERS FOR CARD
    // \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

	public abstract void setContent(String content);
	public String getContent() {
		return this.content;
	}

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
