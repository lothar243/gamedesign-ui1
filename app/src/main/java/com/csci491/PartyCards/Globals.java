package com.csci491.PartyCards;

import java.util.ArrayList;
import java.util.Random;

import android.app.Application;
import android.content.Context;

public class Globals extends Application {
	
	private final static String[] names = { "Collen", "Catarina", "Haydee", "Russell", "Prince", "Tarra", "Cara", "Christopher", "Don", "Larae", "Hye", "Sharee", "Ellen", "Pearline", "Yun", "Marna", "Rosia", "Brittanie", "Terrence", "Tamara", "Dennise", "Houston", "Korey", "Mollie", "Sherron", "Gerard", "Clare", "Jason", "Jasmin", "Karlene", "Irvin", "Wilber", "Mignon", "Willis", "Shela", "Bobby", "Susana", "Kristel", "Harriett", "Katrice", "Elfrieda", "Yvonne", "Krystyna", "Tyrone", "Randa", "Ying", "Annemarie", "Zoila", "Adalberto", "Monika", "Halley", "Thora", "Melodi", "Zetta", "Nicola", "Jan", "Alethea", "Hoa", "Theo", "Annie", "Dean", "Kaitlyn", "Stephani", "Hilton", "Valery", "Elke", "Kimber", "Muoi", "Effie", "Daphine", "Olive", "Carolyne", "Ashlea", "Casandra", "May", "Marvin", "Pansy", "Viola", "Ai", "Cecily", "Leone", "Jeff", "Karolyn", "Delcie", "Linsey", "Shawnee", "Tiffaney", "Coleman", "Dusty", "Len", "Collin", "Paula", "Carmella", "Trina", "Jolynn", "Cedric", "Lashandra", "Kittie", "Temeka", "Verdell" };
	private final static Random myRand = new Random();
	private final static int ROUND = 0;
	private final static int HANDSIZE = 7;
	private static ArrayList<BlackCard> blackCards = new ArrayList<BlackCard>();
	private static ArrayList<WhiteCard> whiteCards = new ArrayList<WhiteCard>();
	private static ArrayList<Player> players = new ArrayList<Player>();
	private static ArrayList<WhiteCard> plays = new ArrayList<WhiteCard>();
	private static String userName = new String();
	private static int pointLimit;
	private static int numPlayers;
	private static int roundNum = ROUND;
	private static int indexWhiteCard = 0;
	private static int indexHumanPlayer;
	private static FileIO cardMaker = new FileIO();
	private static boolean changeBlackCard;
	private static boolean isRoundWinner;
	private static boolean isGameWinner;
	private static String winnerName;
	
	private Globals() {
		resetGlobals();
	}
	
	static void resetGlobals() {
		Globals.blackCards = new ArrayList<BlackCard>();
		Globals.whiteCards = new ArrayList<WhiteCard>();
		Globals.userName = new String();
		Globals.roundNum = ROUND;
		Globals.players = new ArrayList<Player>();
		Globals.cardMaker = new FileIO();
		Globals.changeBlackCard = false;
		Globals.indexHumanPlayer = 0;
		Globals.indexWhiteCard = 0;
		Globals.getPlays().clear();
	}
	
	static String generateRandomName() {
		return Globals.names[Globals.myRand.nextInt(Globals.names.length)];
	}
	
	static FileIO getCardMaker() {
		return cardMaker;
	}
	
	static void setCardMaker(FileIO cardMaker) {
		Globals.cardMaker = cardMaker;
	}
	
	static void setCardMakerContext(Context c) {
		Globals.cardMaker.setContext(c);
	}
	
	static int getHandSize() {
		return HANDSIZE;
	}
	
	static int getRoundNum() {
		return roundNum;
	}
	
	static void setRoundNum(int roundNum) {
		Globals.roundNum = roundNum;
	}
	
	static int getNumPlayers() {
		return numPlayers;
	}
	
	static void setNumPlayers(int numPlayers) {
		Globals.numPlayers = numPlayers;
	}
	
	static int getPointLimit() {
		return pointLimit;
	}
	
	static void setPointLimit(int pointLimit) {
		Globals.pointLimit = pointLimit;
	}
	
	static String getUserName() {
		return userName;
	}
	
	static void setUserName(String userName) {
		Globals.userName = userName;
	}
	
	static ArrayList<Player> getPlayers() {
		return players;
	}
	
	static void setPlayers(ArrayList<Player> players) {
		Globals.players = players;
	}
	
	static ArrayList<WhiteCard> getPlays() {
		return plays;
	}
	
	static void setPlays(ArrayList<WhiteCard> plays) {
		Globals.plays = plays;
	}
	
	static ArrayList<BlackCard> getBlackCards() {
		return blackCards;
	}
	
	static void setBlackCards(ArrayList<BlackCard> blackCards) {
		Globals.blackCards = blackCards;
	}
	
	static ArrayList<WhiteCard> getWhiteCards() {
		return whiteCards;
	}
	
	static void setWhiteCards(ArrayList<WhiteCard> whiteCards) {
		Globals.whiteCards = whiteCards;
	}
	
	static int getIndexWhiteCard() {
		return indexWhiteCard;
	}
	
	static void setIndexWhiteCard(int indexWhiteCard) {
		Globals.indexWhiteCard = indexWhiteCard;
	}
	
	static int getIndexHumanPlayer() {
		return indexHumanPlayer;
	}
	
	static void setIndexHumanPlayer(int indexHumanPlayer) {
		Globals.indexHumanPlayer = indexHumanPlayer;
	}
	
	public static boolean changeBlackCard() {
		return changeBlackCard;
	}
	
	public static void setChangeBlackCard(boolean changeBlackCard) {
		Globals.changeBlackCard = changeBlackCard;
	}
	
	public static boolean isRoundWinner() {
		return isRoundWinner;
	}
	
	public static void setIsRoundWinner(boolean isRoundWinner) {
		Globals.isRoundWinner = isRoundWinner;
	}
	
	public static boolean isGameWinner() {
		return isGameWinner;
	}
	
	public static void setIsGameWinner(boolean isGameWinner) {
		Globals.isGameWinner = isGameWinner;
	}
	
	public static String getWinnerName() {
		return winnerName;
	}
	
	public static void setWinnerName(String winnerName) {
		Globals.winnerName = winnerName;
	}
	
}
