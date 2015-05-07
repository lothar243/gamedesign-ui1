package com.csci491.PartyCards;

// ====================================================================================================================
// BasicGameData.java
// --------------------------------------------------------------------------------------------------------------------
// Party Cards: Android Networking Project
// CSCI-466: Networks
// Jeff Arends, Lee Curran, Angela Gross, Andrew Meissner
// Spring 2015
// ====================================================================================================================

public class BasicGameData
{
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // BASICGAMEDATA ATTRIBUTES
    public int gameId;
    public String gameName;
    public boolean gameIsNew;
    public String[] playerNames;

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    // METHODS THAT RETRIEVE GAME DATA
    // \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    // ===============================================================================================================
    // GAMEIDSMATCH
    // ---------------------------------------------------------------------------------------------------------------
    // Returns whether or not the specified game has the same ID and name as this one.
    // ===============================================================================================================
    public boolean gameIdsMatch(BasicGameData otherGame)
    {
        return (gameId == otherGame.gameId) && gameName.equals(otherGame.gameName);
    }

    // ===============================================================================================================
    // EQUALS
    // ---------------------------------------------------------------------------------------------------------------
    // Returns whether or not the specified game is the same as this one.
    // ===============================================================================================================
    public boolean equals(BasicGameData otherGame)
    {
        boolean match = gameId == otherGame.gameId;
        match &= gameName.equals(otherGame.gameName);
        match &= gameIsNew == otherGame.gameIsNew;
        match &= playerNames.length == otherGame.playerNames.length;
        return match;
    }

    // ===============================================================================================================
    // ARRAYSMATCH
    // ---------------------------------------------------------------------------------------------------------------
    // Checks whether or not the arrays of BasicGameData match or not.
    // ===============================================================================================================
    public static boolean arraysMatch(BasicGameData [] array1, BasicGameData [] array2)
    {
        if(array1 == null || array2 == null)
        {
            return false;
        }
        if(array1.length != array2.length)
        {
            return false;
        }
        boolean match = true;
        for(int i = 0; i < array1.length; i++)
        {
            match &= array1[i].equals(array2[i]);
        }
        return match;
    }

    // ===============================================================================================================
    // TOSTRING
    // ---------------------------------------------------------------------------------------------------------------
    // Basic toString() method that returns the gameID, gameName, and whether or not the game is new
    // ===============================================================================================================
    public String toString() {
        return gameId + "," + gameName + "," + gameIsNew;
    }

    // ===============================================================================================================
    // PLAYERHASJOINEDGAME
    // ---------------------------------------------------------------------------------------------------------------
    // Returns playerId if the player has already joined the game, -1 otherwise
    // ===============================================================================================================
    public int playerHasJoinedGame(String playerNameToSearchFor)
    {
        if(playerNames == null)
        {
            return -1;
        }

        for(int i = 0; i < playerNames.length; i++)
        {
            if(playerNameToSearchFor.equals(playerNames[i]))
            {
                return i;
            }
        }
        return -1;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
