package com.csci491.PartyCards;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

// ====================================================================================================================
// FileIO.java
// --------------------------------------------------------------------------------------------------------------------
// Party Cards: Android Networking Project
// CSCI-466: Networks
// Jeff Arends, Lee Curran, Angela Gross, Andrew Meissner
// Spring 2015
// --------------------------------------------------------------------------------------------------------------------
// This is the file input and output class. It reads the card file in, as well as the user config file.
// ====================================================================================================================

public class FileIO extends Activity
{
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // ATTRIBUTES FOR FILE IO
	private Context c;

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// DEFAULT CONSTRUCTOR
	public FileIO() {}
	
	// MAIN CONSTRUCTOR FOR FILE IO
	// @param c - The context needed so the class can read in the files.
	public FileIO(Context c) {
		this.c = c;
	}

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // SETTERS FOR FILE IO
	public void setContext(Context c) {
		this.c = c;
	}

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    // READERS
    // \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    // ===============================================================================================================
    // READWHITECARDS()
    // ---------------------------------------------------------------------------------------------------------------
    // Reads in from "whitecards.txt", a list of strings representing the text to be displayed on the white cards.
    // @return - A "pile" of white cards.
    // ===============================================================================================================
	public ArrayList<WhiteCard> readWhiteCards()
    {
		ArrayList<WhiteCard> whiteCards = new ArrayList<WhiteCard>();
		
		try
        {
			AssetManager assetManager = c.getAssets();
			InputStream in = assetManager.open("whitecards.txt");
			
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader reader = new BufferedReader(isr);
			
			String mLine;
			while ((mLine = reader.readLine()) != null)
				whiteCards.add(new WhiteCard(mLine, null));
			
			reader.close();
		}
        catch (IOException e)
        {
			// log the exception
		}
		
		return whiteCards;
	}

    // ===============================================================================================================
    // READBLACKCARDS()
    // ---------------------------------------------------------------------------------------------------------------
    // Reads in from "blackcards.txt", a list of strings representing the text to be displayed on the black cards.
    // @return - A "pile" of black cards.
    // ===============================================================================================================
	public ArrayList<BlackCard> readBlackCards()
    {
		ArrayList<BlackCard> blackCards = new ArrayList<BlackCard>();
		
		try
        {
			AssetManager assetManager = c.getAssets();
			InputStream in = assetManager.open("blackcards.txt");
			
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader reader = new BufferedReader(isr);
			
			String mLine;
			while ((mLine = reader.readLine()) != null)
				blackCards.add(new BlackCard(mLine));
			
			reader.close();
		}
        catch (IOException e)
        {
			// log the exception
		}
		
		return blackCards;
	}

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
}
