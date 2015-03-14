/* |===========================================================================
 * |					Cards Against Humanity
 * |
 * | 					Garcia & Meissner
 * |---------------------------------------------------------------------------
 * |                         Main Activity
 * |=========================================================================== 
 */

package com.csci491.PartyCards;

import com.csci491.PartyCards.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button buttonStart = (Button) findViewById(R.id.buttonStart);
		buttonStart.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, NewGameActivity.class);
				startActivity(intent);
				// finish();
			}
		});
		
	}
	
}
