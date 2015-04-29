package com.csci491.PartyCards.NetworkTasks;

import android.os.AsyncTask;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;


public class SoapTask extends AsyncTask <Void, Void, SoapObject>  {
    private static final boolean DEBUG = false;

    public static final String WSDL_URL = "http://184.166.76.115:52244/ws/partyCards?wsdl"; // for manual inspection of the wsdl
    public static final String NAMESPACE = "http://java.main/";
    public static final String URL = "http://184.166.76.115:52244/ws/partyCards/";
    public static final String METHOD_CREATE_GAME = "createNewGame"; // @WebMethod int createNewGame(String gameName); // returns id of game
    public static final String METHOD_GET_NEW_GAMES = "getGames";
    public static final String METHOD_GET_GAME_NAME = "getGameName";
    public static final String METHOD_JOIN_GAME = "joinGame";
    public static final String METHOD_LIST_PLAYERS = "listPlayers";
    public static final String METHOD_START_GAME = "startNewGame";
    public static final String METHOD_IS_GAME_FORMING = "gameIsForming";

    private static final String SOAP_ACTION = "soapAction";

    SoapObject request;


    @Override
    protected SoapObject doInBackground(Void... args) {

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SoapTask.URL);

        httpTransport.debug = DEBUG;
        try {
            httpTransport.call(SOAP_ACTION, envelope);
        } catch (HttpResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }

        return null;
    }






}