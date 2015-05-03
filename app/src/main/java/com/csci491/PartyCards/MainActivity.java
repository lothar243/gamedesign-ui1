package com.csci491.PartyCards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


// ====================================================================================================================
// MainActivity.java
// --------------------------------------------------------------------------------------------------------------------
// Party Cards: Android Networking Project
// CSCI-466: Networks
// Jeff Arends, Lee Curran, Angela Gross, Andrew Meissner
// Spring 2015
// --------------------------------------------------------------------------------------------------------------------
// Handles the onCreate method for the MainActivity
// ====================================================================================================================

public class MainActivity extends Activity {
	private static final boolean DEBUG_SOAP_REQUEST_RESPONSE = true;
	private static final String MAIN_REQUEST_URL = "http://www.w3schools.com/webservices/tempconvert.asmx";
	private static final String SOAP_ACTION = "http://www.w3schools.com/webservices/FahrenheitToCelsius";
	private static final String NAMESPACE = "http://www.w3schools.com/webservices/";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// ===============================================================================================================
	// ONCREATE()
	// ---------------------------------------------------------------------------------------------------------------
	// Prepares layout of the MainActivity
	// ===============================================================================================================
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final Button buttonStartMultiplayer = (Button) findViewById(R.id.buttonStartMultiplayer);
		final Button buttonStartSingleplayer = (Button) findViewById(R.id.buttonStartSinglePlayer);
		buttonStartMultiplayer.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, ListMultiplayerGamesActivity.class);
				startActivity(intent);

			}
		});
		buttonStartSingleplayer.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent(MainActivity.this, NewGameActivity.class);
				startActivity(intent);
				// finish();
			}
		});
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////




}