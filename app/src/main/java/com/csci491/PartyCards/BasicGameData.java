package com.csci491.PartyCards;

public class BasicGameData {
    public int gameId;
    public String gameName;
    public boolean gameIsNew;
    public String[] playerNames;

    public boolean gameIdsMatch(BasicGameData otherGame) {
        return (gameId == otherGame.gameId) && gameName.equals(otherGame.gameName);
    }

    public boolean equals(BasicGameData otherGame) {
        boolean match = gameId == otherGame.gameId;
        match &= gameName.equals(otherGame.gameName);
        match &= gameIsNew == otherGame.gameIsNew;
        match &= playerNames.length == otherGame.playerNames.length;
        return match;

    }

    public static boolean arraysMatch(BasicGameData [] array1, BasicGameData [] array2) {
        if(array1 == null || array2 == null) {
            return false;
        }
        if(array1.length != array2.length) {
            return false;
        }
        boolean match = true;
        for(int i = 0; i < array1.length; i++) {
            match &= array1[i].equals(array2[i]);
        }
        return match;
    }

    public String toString() {
        return gameId + "," + gameName + "," + gameIsNew;
    }

    // returns playerId if the player has already joined the game, -1 otherwise
    public int playerHasJoinedGame(String playerNameToSearchFor) {
        if(playerNames == null) {
            return -1;
        }

        for(int i = 0; i < playerNames.length; i++) {
            if(playerNameToSearchFor.equals(playerNames[i])) {
                return i;
            }
        }
        return -1;
    }



}
