package com.csci491.PartyCards;

// ====================================================================================================================
// BlackCard.java
// --------------------------------------------------------------------------------------------------------------------
// Party Cards: Android Networking Project
// CSCI-466: Networks
// Jeff Arends, Lee Curran, Angela Gross, Andrew Meissner
// Spring 2015
// --------------------------------------------------------------------------------------------------------------------
// Defines a class for a black card that has information about the number of blanks it holds along with its content
// (from parent).
// ====================================================================================================================

public class BlackCard extends Card
{
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // BLACK CARD ATTRIBUTES
	protected int blanks; // number of blank spaces on a card

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// BLACK CARD CONSTRUCTOR
	// @param content - The text displayed on the card.
	public BlackCard(String content)
    {
		setContent(content);
	}

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    // GETTERS AND SETTERS FOR BLACK CARD
    // \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

	public void setContent(String content)
    {
		int numBlanks = 0;
		
		if (content.contains("<blank>"))
        {
			String[] words = content.split(" ");

			for (int i = 0; i < words.length; i++)
            {
				if (words[i].equalsIgnoreCase("<blank>"))
					numBlanks++;
			}
		}
		
		setBlanks(numBlanks);
		super.content = content;
	}

	public void setBlanks(int blanks) {
		this.blanks = blanks;
	}
	public int getBlanks() {
		return this.blanks;
	}

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
