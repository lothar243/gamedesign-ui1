package com.csci491.PartyCards;

import com.csci491.PartyCards.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class NewRoundActivity extends Activity {
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_new_round);

		// Assign Round #
		Globals.setRoundNum(Globals.getRoundNum() + 1);

		// display round #
		TextView round = (TextView) findViewById(R.id.lblRoundNum);
		round.setText(" " + Globals.getRoundNum());

		// display player name
		TextView textViewPlayerName = (TextView) findViewById(R.id.textViewPlayerName);
		textViewPlayerName.setText("It's " + Globals.getPlayers().get(Globals.getIndexHumanPlayer()).getName());

		// Clean plays (from previous rounds)
		Globals.getPlays().clear();

		// Clean winners from precious rounds
		Globals.setIsRoundWinner(false);
		Globals.setIsGameWinner(false);

		System.out.println("Next Round Number: " + Globals.getRoundNum());

		Button buttonSkip = (Button) findViewById(R.id.buttonSkip);
		buttonSkip.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(NewRoundActivity.this, InGameActivity.class);
				startActivity(intent);
				finish();
			}
		});

		// Find Table Layout defined in XML
		TableLayout tl = (TableLayout) findViewById(R.id.tablePlayers);
		// Create new rows to be added.
		for (int i = 0; i < Globals.getPlayers().size(); i++) {
			TableRow tr = new TableRow(this);
			tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
			// Add controls to row
			tr.addView(CreateTextView(i + 1, true));
			tr.addView(CreateTextView(i, false));

			// Add row to TableLayout
			// tr.setBackgroundResource(R.drawable.sf_gradient_03);
			tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

		}
	}

	private TextView CreateTextView(int i, boolean showName) {
		TextView textView = new TextView(getApplicationContext());
		textView.setTextAppearance(getApplicationContext(), android.R.style.TextAppearance_Medium);
		if (showName) {
			textView.setText(" " + Globals.getPlayers().get(i - 1).getName() + " " + (Globals.getPlayers().get(i - 1).isCzar() ? "(Czar)" : ""));

		} else {
			textView.setText(Globals.getPlayers().get(i).getScore() + " points ");
			textView.setGravity(Gravity.RIGHT);
		}

		return textView;
	}
}
//
