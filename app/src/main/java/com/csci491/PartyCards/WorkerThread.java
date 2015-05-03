package com.csci491.PartyCards;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.csci491.PartyCards.NetworkTasks.NetworkMethods;
import com.csci491.PartyCards.NetworkTasks.PartyCardsInterface;

public class WorkerThread extends Thread {
    private final static String TAG = WorkerThread.class.getSimpleName(); // for the debugger
    static NetworkMethods networkMethods;

    // when we need to update the ui
    private static Handler uiHandler;

    // to pass message back to itself
    private static Handler workerHandler;

    public WorkerThread(Handler uiHandler) {
        this.uiHandler = uiHandler;
    }

    public void setUiHandler(Handler uiHandler) {
        this.uiHandler = uiHandler;
    }

    public void run() {
        // Thread by default doesn't have a msg queue, to attach a msg queue to this thread
        Looper.prepare();
        networkMethods = new NetworkMethods();
        // this will bind the Handler to the msg queue
        // notice that msg queue is FIFO, if u send 2 runable objects 1 after the other, second 1 will wait till first one finish
        workerHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case PartyCardsInterface.CHOOSE_CARD:
                        networkMethods.chooseCard(Globals.multiplayerGameId, Globals.multiplayerGamePlayerId, MultiplayerGameActivity.handIndex);
                        break;
                    case PartyCardsInterface.GET_GAME_DATA:
                        InGameData updatedData = networkMethods.getGameData(Globals.multiplayerGameId, Globals.multiplayerGamePlayerId);
                        if(MultiplayerGameActivity.thisGame == null || MultiplayerGameActivity.thisGame.turnNumber < updatedData.turnNumber) {
                            MultiplayerGameActivity.previewPhase = true;
                        }
                        if(MultiplayerGameActivity.previewPhase) {
                            updatedData.hand = networkMethods.roundSummary(Globals.multiplayerGameId);
                        }
                        MultiplayerGameActivity.thisGame = updatedData;
                        break;
                    case PartyCardsInterface.GET_GAMES:
                        BasicGameData [] updatedGameList = networkMethods.getBasicGameData();
                        if(!BasicGameData.arraysMatch(ListMultiplayerGamesActivity.listedGames, updatedGameList)) {
                            // if the game data has changed
                            ListMultiplayerGamesActivity.listedGames = updatedGameList;
                            uiHandler.sendEmptyMessage(0);// call for the main thread to update the ui
                        }
                        break;
                    case PartyCardsInterface.GET_BASIC_GAME_DATA_SINGLE_GAME:
                        JoinGameActivity.gameInfo = networkMethods.getBasicGameDataSingleGame(Globals.multiplayerGameId);
                        uiHandler.sendEmptyMessage(0); // call for the main thread to update the ui
                        break;
                    case PartyCardsInterface.CREATE_NEW_GAME:
                        networkMethods.createNewGame((String) msg.obj);
                        break;
                    case PartyCardsInterface.STAR_NEW_GAME:
                        networkMethods.startNewGame(Globals.multiplayerGameId);
                        break;
                    case PartyCardsInterface.JOIN_GAME:
                        int playerId = networkMethods.joinGame(Globals.multiplayerGameId, Globals.userName);
                        if(playerId >= 0) {
                            Globals.multiplayerGamePlayerId = playerId;
                        }
                        break;
                    default:

                        break;

                }
                // keep refreshing while the window is in focus
                if(!getHandlerToMsgQueue().hasMessages(Globals.defaultMessage)) {
                    if(Globals.windowIsInFocus) {
                        getHandlerToMsgQueue().sendEmptyMessageDelayed(Globals.defaultMessage, Globals.multiplayerRefreshRate);
                    }
                }
                uiHandler.sendEmptyMessage(0); // cause refresh of ui

            }
        };
        // handles msgs/runnables receive to msgqueue, this will start a loop that listens msg receiving
        Looper.loop();
    }

    // this keep refreshing the window while it's in focus

    public Handler getHandlerToMsgQueue() {
        return workerHandler;
    }



}
