package com.csci491.PartyCards;

// ====================================================================================================================
// InGameData.java
// --------------------------------------------------------------------------------------------------------------------
// Party Cards: Android Networking Project
// CSCI-466: Networks
// Jeff Arends, Lee Curran, Angela Gross, Andrew Meissner
// Spring 2015
// --------------------------------------------------------------------------------------------------------------------
// This InGameData is nothing more than a data type used to pass information about the current state of the game
// that a player is already playing. It contains data such as the players current hand, the black card, and whether
// or not the player is currently the card czar.
// ====================================================================================================================

public class InGameData
{
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // INGAMEDATA ATTRIBUTES
    public String blackCard;
    public String roundText;
    public int playerId;
    public int playerIsCardCzar;
    public int turnPhase;
    public int turnNumber;
    public int numberOfPlayersChoosing;
    public String [] hand;

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // ===============================================================================================================
    // TOSTRING
    // ---------------------------------------------------------------------------------------------------------------
    // Basic toString() method that returns all attributes as a string
    // ===============================================================================================================
    public String toString()
    {
        final String DELIM = ",";
        return "round: " + roundText +
                ", PID: " + playerId +
                ", CCz " + playerIsCardCzar +
                ", Turn: " + turnNumber + " - " + turnPhase +
                ", Black: " + blackCard +
                ", hand: " + arrayToString(hand);
    }

    // ===============================================================================================================
    // ARRAYTOSTRING
    // ---------------------------------------------------------------------------------------------------------------
    // Helper method of toString() that prints out the given array
    // ===============================================================================================================
    private String arrayToString(String [] input)
    {
        String output = "";
        for(String s: input)
        {
            output += ", " + s;
        }
        return output;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
