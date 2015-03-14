package com.csci491.PartyCards;

import java.util.ArrayList;

import com.csci491.PartyCards.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class PlayerConfigActivity extends Activity {
	
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player_config);
		
		// Find Table layout defined in XML
		final TableLayout tl = (TableLayout) findViewById(R.id.Table1);
		// Create new rows to be added.
		for (int i = 0; i < Globals.getPlayers().size(); i++) {
			TableRow tr = new TableRow(this);
			tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
			// Add controls to row
			tr.addView(CreateEditText(i + 1));
			// tr.addView(CreateSpinner(i));
			// Add row to TableLayout
			// tr.setBackgroundResource(R.drawable.sf_gradient_03);
			tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
			
		}
		// Create StartNewGame button
		Button buttonStartNewGame = new Button(this);
		buttonStartNewGame.setText("Start Game");
		buttonStartNewGame.setBackgroundColor(getBaseContext().getResources().getColor(R.color.greenT));
		
		buttonStartNewGame.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				for (int i = 0; i < Globals.getPlayers().size(); i++) {
					TableRow r = (TableRow) tl.getChildAt(i);
					EditText et = (EditText) r.getChildAt(0);
					if (et.getText().toString().length() == 0) {
						Toast.makeText(getBaseContext(), R.string.blank_player_name_toast, Toast.LENGTH_SHORT).show();
						return;
					}
					String name = et.getText().toString();
					
					System.out.println(name);
					
					Globals.getPlayers().get(i).setName(name);
				}
				
				Intent intent = new Intent(PlayerConfigActivity.this, NewRoundActivity.class);
				startActivity(intent);
				finish();
			}
		});
		tl.addView(buttonStartNewGame, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
		
		// Deal out cards to each player
		System.out.println("Dealing out the cards...");
		
		for (int i = 0; i < Globals.getHandSize(); i++) {
			for (int j = 0; j < Globals.getPlayers().size() - 1; j++) {
				Globals.getPlayers().get(j).draw();
			}
		}
		System.out.println("Cards successfully dealt!");
		
	}
	
	private TextView CreateEditText(int i) {
		EditText editText = new EditText(getApplicationContext());
		editText.setTextAppearance(getApplicationContext(), android.R.style.TextAppearance_Medium);
		editText.setText("Player " + i);
		editText.setSelectAllOnFocus(true);
		
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		// int height = size.y;
		
		editText.setWidth(width / 2);
		
		return editText;
	}
	
	private Spinner CreateSpinner(int i) {
		Spinner spinner = new Spinner(this);
		ArrayList<String> spinnerArray = new ArrayList<String>();
		// First player is always you
		if (i == 0) {
			spinnerArray.add("YOU");
			spinner.setEnabled(false);
		} else {
			
			spinnerArray.add("COMPUTER");
			spinnerArray.add("HUMAN");
		}
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
		spinner.setAdapter(spinnerArrayAdapter);
		spinner.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
		return spinner;
	}
}
