package com.csci491.PartyCards.NetworkTasks;

import android.util.Log;

import com.csci491.PartyCards.BasicGameData;
import com.csci491.PartyCards.Globals;
import com.csci491.PartyCards.InGameData;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by Jeff on 4/29/2015.
 */
public class NetworkMethods implements PartyCardsInterface{

    private static final boolean DEBUG = false;

    public static String ipAddress;
    public static String WSDL_URL = "http://" + Globals.multiplayerServerIPAddress + ":52244/ws/partyCards?wsdl"; // for manual inspection of the wsdl
    public static String NAMESPACE = "http://java.main/";
    public static String URL = "http://" + Globals.multiplayerServerIPAddress + ":52244/ws/partyCards/";


    String methodName;
    SoapObject request;
    String soapAction;


    protected String formProperSoapAction(String method) {
        return "\"" + NAMESPACE + "PartyCardsInterface/" + method + "Request\"";
    }

    public NetworkMethods(String ipAddress) {
        WSDL_URL = "http://" + ipAddress + ":52244/ws/partyCards?wsdl"; // for manual inspection of the wsdl
        NAMESPACE = "http://java.main/";
        URL = "http://" + ipAddress + ":52244/ws/partyCards/";

    }
    public NetworkMethods() {
        WSDL_URL = "http://" + Globals.multiplayerServerIPAddress + ":52244/ws/partyCards?wsdl"; // for manual inspection of the wsdl
        NAMESPACE = "http://java.main/";
        URL = "http://" + Globals.multiplayerServerIPAddress + ":52244/ws/partyCards/";

    }

    private void init(String methodName) {
        soapAction = formProperSoapAction(methodName);
        request = new SoapObject(NAMESPACE, methodName);
    }

    private SoapObject makeSoapCall() {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        httpTransport.debug = DEBUG;
        try {
            httpTransport.call(soapAction, envelope);
        } catch (HttpResponseException e) {
            Log.d("SOAP", "HttpResponseException on " + request.toString());
        } catch (IOException e) {
            Log.d("SOAP", "IOException on " + request.toString());
        } catch (XmlPullParserException e) {
            Log.d("SOAP", "HttpResponseException on " + request.toString());
        } catch (Exception e) {
            Log.e("SOAP", "Other error");//send request
        }

        SoapObject result;

        // debugging reports - show the outgoing and incoming xml
        if(DEBUG) {
            Log.d("Dump request: ", httpTransport.requestDump);
            Log.d("Dump response: ", httpTransport.responseDump);
        }

        try {
            result = (SoapObject)envelope.bodyIn;
            return result;
        } catch (Exception e) {
            Log.d("SOAP", "Error with soap result");
        }

        return null;
    }

    private Integer [] parseForIntArray(SoapObject soapResult) {
        Integer [] output = null;
        try {
            SoapObject arrayContainer = (SoapObject) soapResult.getProperty(0);
            output = new Integer[arrayContainer.getPropertyCount()];
            for(int i = 0; i < output.length; i++) {
                output[i] = Integer.valueOf(arrayContainer.getProperty(i).toString());
            }
        }
        catch (Exception e) {
            Log.e("SOAP", "parseForIntArray - " + methodName);
        }
        return output;
    }

    private int parseForInt(SoapObject soapResult) {
        int output = -1;
        try {
            output = Integer.parseInt(soapResult.getProperty(0).toString());

        }
        catch (Exception e) {
            Log.e("SOAP", "parseForInt - " + methodName);
        }
        return output;
    }

    private String [] parseForStringArray(SoapObject soapResult) {
        String [] output = null;
        try {
            SoapObject arrayContainer = (SoapObject) soapResult.getProperty(0);
            output = new String[arrayContainer.getPropertyCount()];
            for(int i = 0; i < output.length; i++) {
                output[i] = arrayContainer.getProperty(i).toString();
            }
        }
        catch (Exception e) {
            Log.e("SOAP", "parseForStringArray - " + methodName);
        }
        return output;
    }

    private boolean parseForBoolean(SoapObject soapResult) {
        boolean output = false;
        try {
            output = Boolean.parseBoolean(soapResult.getProperty(0).toString());
        }
        catch (Exception e) {
            Log.e("SOAP", "parseForBoolean - " + methodName);
        }
        return output;
    }

    private String parseForString(SoapObject soapResult) {
        String output = "";
        try {
            output = soapResult.getProperty(0).toString();
        }
        catch (Exception e){
            Log.e("SOAP", "parseForString - " + methodName);
        }
        return output;
    }

    @Override
    public Integer[] getGames() {
        init("getGames");
        return parseForIntArray(makeSoapCall());
    }
    public int createNewGame(String gameName) {
        init("createNewGame");
        request.addProperty("arg0", gameName);
        return parseForInt(makeSoapCall());
    }
    @Override
    public int joinGame(int gameId, String userName) {
        init("joinGame");
        request.addProperty("arg0", gameId);
        request.addProperty("arg1", userName);
        return parseForInt(makeSoapCall());
    }

    @Override
    public String[] listPlayers(int gameId) {
        init("listPlayers");
        request.addProperty("arg0", gameId);
        return parseForStringArray(makeSoapCall());
    }

    @Override
    public boolean destroyGame(int gameId) {
        init("destroyGame");
        request.addProperty("arg0", gameId);
        return parseForBoolean(makeSoapCall());
    }

    public String getGameName(int gameId) {
        init("getGameName");
        request.addProperty("arg0", gameId);
        return parseForString(makeSoapCall());
    }

    @Override
    public boolean gameIsForming(int gameId) {
        init("gameIsForming");
        request.addProperty("arg0", gameId);
        return parseForBoolean(makeSoapCall());
    }

    @Override
    public int playerIsCardCzar(int gameId, int playerId) {
        init("playerIsCardCzar");
        request.addProperty("arg0", gameId);
        request.addProperty("arg1", playerId);
        return parseForInt(makeSoapCall());
    }

    @Override
    public int getTurnPhase(int gameId) {
        init("getTurnPhase");
        request.addProperty("arg0", gameId);
        return parseForInt(makeSoapCall());
    }

    @Override
    public String[] getHand(int gameId, int playerId) {
        init("getHand");
        request.addProperty("arg0", gameId);
        request.addProperty("arg1", playerId);
        return parseForStringArray(makeSoapCall());
    }

    @Override
    public String getBlackCard(int gameId) {
        init("getBlackCard");
        request.addProperty("arg0", gameId);
        return parseForString(makeSoapCall());
    }

    @Override
    public int chooseCard(int gameId, int playerId, int cardNumber) {
        init("chooseCard");
        request.addProperty("arg0", gameId);
        request.addProperty("arg1", playerId);
        request.addProperty("arg2", cardNumber);
        return parseForInt(makeSoapCall());
    }

    @Override
    public void startNewGame(int gameId) {
        init("startNewGame");
        request.addProperty("arg0", gameId);
        makeSoapCall();
    }

    @Override
    public void reportCurrentStatus() {
        //not used here
    }

    @Override
    public Integer[] getTurnStatus(int gameId) {
        init("getTurnStatus");
        request.addProperty("arg0", gameId);
        return parseForIntArray(makeSoapCall());
    }

    @Override
    public Integer[] getScore(int gameId) {
        init("getScore");
        request.addProperty("arg0", gameId);
        return parseForIntArray(makeSoapCall());
    }

    @Override
    public String[] roundSummary(int gameId) {
        init("roundSummary");
        request.addProperty("arg0", gameId);
        return parseForStringArray(makeSoapCall());
    }

    @Override
    public InGameData getGameData(int gameId, int playerId) {
//        try {
            init("getGameData");
            request.addProperty("arg0", gameId);
            request.addProperty("arg1", playerId);


            SoapObject result = makeSoapCall();

            result = (SoapObject) result.getProperty(0);
            InGameData output = new InGameData();

            //blackCard
//        System.out.println("numberOfPlayersChoosing" + result.getProperty(6).toString());

            output.blackCard = result.getProperty(0).toString();
            output.roundText = result.getProperty(1).toString();
            output.playerId = Integer.parseInt(result.getProperty(2).toString());
            output.playerIsCardCzar = Integer.parseInt(result.getProperty(3).toString());
            output.turnPhase = Integer.parseInt(result.getProperty(4).toString());
            output.turnNumber = Integer.parseInt(result.getProperty(5).toString());
            output.numberOfPlayersChoosing = Integer.parseInt(result.getProperty(6).toString());
            int handSize = result.getPropertyCount() - 7;
            output.hand = new String[handSize];
            for (int i = 0; i < handSize; i++) {
                output.hand[i] = result.getProperty(i + 7).toString();
            }
            return output;
//        }
//        catch (Exception e) {
//            Log.e("NetworkMethods", "Error with getGameData()");
//        }
//        return null;
    }

    @Override
    public BasicGameData [] getBasicGameData() {
        init("getBasicGameData");
        SoapObject result = makeSoapCall();
        try {
            result = (SoapObject) result.getProperty(0);

            int numGames = result.getPropertyCount();
            BasicGameData[] output = new BasicGameData[numGames];
            for (int i = 0; i < numGames; i++) {
                SoapObject container = (SoapObject) result.getProperty(i);
                BasicGameData game = new BasicGameData();
                game.gameId = Integer.parseInt(container.getProperty(0).toString());
                game.gameName = container.getProperty(1).toString();
                game.gameIsNew = Boolean.parseBoolean(container.getProperty(2).toString());
                int numPlayers = container.getPropertyCount() - 3;
                game.playerNames = new String[numPlayers];
                for (int j = 0; j < numPlayers; j++) {
                    game.playerNames[j] = container.getProperty(j + 3).toString();
                }
                output[i] = game;

            }
            return output;
        }
        catch(Exception e) {
            Log.e("NetworkMethods", "Error with getBasicGameData()");
        }
        return null;
    }

    @Override
    public BasicGameData getBasicGameDataSingleGame(int gameId) {
        try {
            init("getBasicGameDataSingleGame");
            request.addProperty("arg0", gameId);

            SoapObject result = (SoapObject) makeSoapCall().getProperty(0);
            BasicGameData game = new BasicGameData();
            game.gameId = Integer.parseInt(result.getProperty(0).toString());
            game.gameName = result.getProperty(1).toString();
            game.gameIsNew = Boolean.parseBoolean(result.getProperty(2).toString());
            int numPlayers = result.getPropertyCount() - 3;
            game.playerNames = new String[numPlayers];
            for (int j = 0; j < numPlayers; j++) {
                game.playerNames[j] = result.getProperty(j + 3).toString();
            }

            return game;
        }
        catch (Exception e) {
            Log.e("NetworkMethods", "Error with getBasicGameDataSingleGame()");
        }
        return null;
    }
}
