package com.csci491.PartyCards;


public class InGameData {
    public String blackCard;
    public String roundText;
    public int playerId;
    public int playerIsCardCzar;
    public int turnPhase;
    public int turnNumber;
    public int numberOfPlayersChoosing;
    public String [] hand;

    public String toString() {
        final String DELIM = ",";
        return "round: " + roundText +
                ", PID: " + playerId +
                ", CCz " + playerIsCardCzar +
                ", Turn: " + turnNumber + " - " + turnPhase +
                ", Black: " + blackCard +
                ", hand: " + arrayToString(hand);
    }
    private String arrayToString(String [] input) {
        String output = "";
        for(String s: input) {
            output += ", " + s;
        }
        return output;
    }
}
