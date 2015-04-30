package com.csci491.PartyCards.NetworkTasks;

import android.util.Log;

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

    public static final String SERVER_IP_ADDRESS = "192.168.1.2";

    private static final boolean DEBUG = false;

    public static final String WSDL_URL = "http://" + SERVER_IP_ADDRESS + ":52244/ws/partyCards?wsdl"; // for manual inspection of the wsdl
    public static final String NAMESPACE = "http://java.main/";
    public static final String URL = "http://" + SERVER_IP_ADDRESS + ":52244/ws/partyCards/";


    String methodName;
    SoapObject request;
    String soapAction;


    protected String formProperSoapAction(String method) {
        return "\"" + NAMESPACE + "PartyCardsInterface/" + method + "Request\"";
    }

    private void init(String methodName) {
        soapAction = formProperSoapAction(methodName);
        request = new SoapObject(SoapTask.NAMESPACE, methodName);
    }

    private SoapObject makeSoapCall() {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SoapTask.URL);

        httpTransport.debug = DEBUG;
        try {
            httpTransport.call(soapAction, envelope);
        } catch (HttpResponseException e) {
            Log.d("SOAP", "HttpResponseException on " + request.toString());
        } catch (IOException e) {
            Log.d("SOAP", "IOException on " + request.toString());
        } catch (XmlPullParserException e) {
            Log.d("SOAP", "HttpResponseException on " + request.toString());
        } //send request

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
        request.addProperty("arg1", gameName);
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
        return;
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
}
