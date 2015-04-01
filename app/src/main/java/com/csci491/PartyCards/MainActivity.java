package com.csci491.PartyCards;

import com.csci491.PartyCards.R;
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

public class MainActivity extends Activity
{
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // ===============================================================================================================
    // ONCREATE()
    // ---------------------------------------------------------------------------------------------------------------
    // Prepares layout of the MainActivity
    // ===============================================================================================================
	protected void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button buttonStart = (Button) findViewById(R.id.buttonStart);
		buttonStart.setOnClickListener(new OnClickListener()
        {
			public void onClick(View v)
            {
				Intent intent = new Intent(MainActivity.this, NewGameActivity.class);
				startActivity(intent);
				// finish();
			}
		});
	}

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
