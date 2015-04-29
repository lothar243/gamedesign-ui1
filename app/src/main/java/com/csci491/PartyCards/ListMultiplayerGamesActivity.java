package com.csci491.PartyCards;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.csci491.PartyCards.NetworkTasks.CreateNewGameSoapTask;
import com.csci491.PartyCards.NetworkTasks.GetGamesSoapTask;


public class ListMultiplayerGamesActivity extends ListActivity {
    Handler listUpdateHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_multiplayer_game);
        final BasicAdapterWithID gameNameAdapter = new BasicAdapterWithID(this);

        // set up the handler to listen for when the game names have been retrieved
        listUpdateHandler = new Handler() {
            @Override
            public void handleMessage(Message incomingMessage) {
                // arg1 signals the method 0: clear, 1: add
                // arg2 contains the id for the add function
                if(incomingMessage.arg1 == 0) {
                    gameNameAdapter.clear();
                }
                else if(incomingMessage.arg1 == 1) {
                    gameNameAdapter.add((String) incomingMessage.obj, incomingMessage.arg2);
                    gameNameAdapter.notifyDataSetChanged();
                }
            }
        };

        refreshList();

        // finally, set up the adapter that takes care of generating the list
        setListAdapter(gameNameAdapter);

        final Button newGameButton = (Button)findViewById(R.id.ButtonNewMultiplayerGame);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createNewGameDialog();
            }
        });
        final Button refreshButton = (Button)findViewById(R.id.ButtonRefreshGameList);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                refreshList();
            }
        });

        if(Globals.userName.equals("")) {
            promptForUserName();
        }

    }

    public void refreshList() {


        // start the aSyncTasks that fetch the gameIds of the active games and then go on to fetch their names
        // they each call the handler individually to update the list that is displayed
        GetGamesSoapTask updateGameList = new GetGamesSoapTask(listUpdateHandler);
        updateGameList.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_join_multiplayer_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onListItemClick(ListView listView, View currentView, int position, long id) {
        Intent joinGameIntent = new Intent(ListMultiplayerGamesActivity.this, JoinGameActivity.class);
        Message viewInfo = (Message) currentView.getTag();

        joinGameIntent.putExtra("gameId", viewInfo.arg1);
        joinGameIntent.putExtra("gameName", String.valueOf(viewInfo.obj));
        startActivity(joinGameIntent);
//        Toast debugToast = Toast.makeText(this, (currentView.getTag()).toString(), Toast.LENGTH_SHORT);
//        debugToast.show();
    }

    private void createNewGameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CreateNewGameSoapTask mySoapTask = new CreateNewGameSoapTask(listUpdateHandler, input.getText().toString());
                mySoapTask.execute();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void promptForUserName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Before you can join, what is your username?");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected;
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Globals.userName = input.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

}
