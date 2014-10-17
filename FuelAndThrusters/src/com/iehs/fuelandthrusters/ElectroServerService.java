package com.iehs.fuelandthrusters;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

import com.electrotank.electroserver5.client.ElectroServer;
import com.electrotank.electroserver5.client.api.EsConnectionClosedEvent;
import com.electrotank.electroserver5.client.api.EsConnectionResponse;
import com.electrotank.electroserver5.client.api.EsCreateRoomRequest;
import com.electrotank.electroserver5.client.api.EsCreateRoomVariableRequest;
import com.electrotank.electroserver5.client.api.EsJoinRoomEvent;
import com.electrotank.electroserver5.client.api.EsLoginRequest;
import com.electrotank.electroserver5.client.api.EsLoginResponse;
import com.electrotank.electroserver5.client.api.EsMessageType;
import com.electrotank.electroserver5.client.api.EsRoomVariableUpdateAction;
import com.electrotank.electroserver5.client.api.EsRoomVariableUpdateEvent;
import com.electrotank.electroserver5.client.api.EsUpdateRoomVariableRequest;
import com.electrotank.electroserver5.client.connection.AvailableConnection;
import com.electrotank.electroserver5.client.connection.TransportType;
import com.electrotank.electroserver5.client.extensions.api.value.EsObject;
import com.electrotank.electroserver5.client.server.Server;
import com.electrotank.electroserver5.client.zone.Room;

public class ElectroServerService extends Service{
	
	// Server variables
	static final String TAG = "ESCode";
	public final static String NEW_SERVER_MESSAGE = "New_Server_Message";
	public final static String NEW_PUBLIC_MESSAGE = "New_Public_Message";
	public final static String NEW_GAME_MESSAGE = "New_Game_Message";
	private ElectroServer es = null;
	private GameMsgReceiver gmr;
	@SuppressWarnings("unused")
	private boolean isRegistered;
    private String userName = "";
    private Room room = null;
	public final static int NOTIFICATION_ID = 1;
	@SuppressWarnings("unused")
	private Notification _newServerMessageNotification;
	private final IBinder mBinder = new LocalBinder();
	
	// Room variables id
	private String fuelVariables = "Fuel";
	private String roundNumberVariable = "RoundNumber";
	private String playerTurnVariable = "PlayerTurn";
	private String winnerVariable = "Winner";
	private String playAgainVariable = "PlayAgain";
	private String usersInRoomVariable = "UsersInRoom";
	private String playersReadyVariable = "PlayersReady";
	
	// Room variables exists
	private boolean fuelVariablesExists;
	private boolean roundNumberVariableExists;
	private boolean playerTurnVariableExists;
	private boolean winnerVariableExists;
	private boolean playAgainVariableExists;
	private boolean usersInRoomVariableExists;
	private boolean playersReadyVariableExists;
	
	// Room variables names
	// Initialise variables so that you can broadcast something
	String fuel1Name = "None";
	String fuel2Name = "None";
	String fuel3Name = "None";
	String fuel4Name = "None";
	String roundNumberName = "None";
	String playerTurnName = "None";
	String winnerName = "None";
	String playAgainName = "None";
	String usersInRoomName = "None";
	String playersReadyName = "None";
	
	// Room variables values
	int fuel1Value = 0;
	int fuel2Value = 0;
	int fuel3Value = 0;
	int fuel4Value = 0;
	int roundNumberValue= 0;
	boolean playerTurnValue = false;
	int winnerValue = 0;
	int player1WinCount = 0;
	int player2WinCount = 0;
	int playAgainValue = 0;
	int usersInRoomValue = 0;
	int playersReadyValue = 0;
	
	
	
	/**
	 * Start server, create listeners, receivers and connect
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		es = new ElectroServer();
        gmr = new GameMsgReceiver();
        registerReceiver(gmr,new IntentFilter(MainActivity.NEW_PLAYER_MESSAGE ));
        registerReceiver(gmr,new IntentFilter(GameActivity.NEW_PLAYER_GAME_MESSAGE ));
        isRegistered = true;
        es.getEngine().addEventListener(EsMessageType.ConnectionResponse, this, "onConnectionResponse", EsConnectionResponse.class);
        es.getEngine().addEventListener(EsMessageType.ConnectionClosedEvent, this, "onConnectionClosedEvent", EsConnectionClosedEvent.class);
        es.getEngine().addEventListener(EsMessageType.LoginResponse, this, "onLoginResponse", EsLoginResponse.class);
        es.getEngine().addEventListener(EsMessageType.JoinRoomEvent, this, "onJoinRoomEvent", EsJoinRoomEvent.class);
        es.getEngine().addEventListener(EsMessageType.RoomVariableUpdateEvent, this, "onRoomVariableUpdateEvent", EsRoomVariableUpdateEvent.class);
        // Fire off a thread to do some work that we shouldn't do directly in the UI thread
        Thread t = new Thread() {
            public void run() {
                loadAndConnect();
            }
        };
        t.start();
		return Service.START_STICKY;
	}
	/**
	 * Create the server
	 */
	private void loadAndConnect() {
        Server server = new Server("myServer1");
        String ip = getBaseContext().getString(R.string.es5_ip);
   	    int port = getResources().getIntArray(R.array.es5_port)[0];
        AvailableConnection availConn = new AvailableConnection(ip, port, TransportType.BinaryTCP);
        server.addAvailableConnection(availConn);
        //register this server with the API
        es.getEngine().addServer(server);
        es.getEngine().connect(null, null);
	}
	
	
	/**
	 * The response from connecting.
	 * @param e
	 */
	public void onConnectionResponse(EsConnectionResponse e) {
		if (e.isSuccessful()) {
			sendConnectedSuccessful();
		}
	}
	/**
	 * The event for a closed connection
	 * @param e
	 */
	public void onConnectionClosedEvent(EsConnectionClosedEvent e) {
		sendConnectionClosed(e.toString());
	}	
	/**
	 * The response from logging in
	 * @param e
	 */
	public void onLoginResponse(EsLoginResponse e) {
		// Log in as user 1
		if (e.isSuccessful() == true) {
			
			// Update username when user logs in.
            userName = e.getUserName(); 
            sendLoginSuccessful();
        } 
		// When there is already a user logged in, log in as user 2
		else if (e.isSuccessful() == false) {
			doLogin2();
        }
    }
	/**
	 * The event of joining a room
	 * @param e
	 */
	public void onJoinRoomEvent(EsJoinRoomEvent e) {
        int roomId = e.getRoomId();
        int zoneId = e.getZoneId();
        //store the room reference
        room = es.getManagerHelper().getZoneManager().zoneById(zoneId).roomById(roomId);
        sendJoinRoomSuccessful();
    }	
	
	/**
	 * The event for the room variables
	 * @param e
	 */
	public void onRoomVariableUpdateEvent(EsRoomVariableUpdateEvent e) {
		// When variable updates, see what action updated.
		EsRoomVariableUpdateAction action = (EsRoomVariableUpdateAction)e.getAction();
		
		// If the variable is created or updated/changed.
		if(action == EsRoomVariableUpdateAction.VariableCreated || action == EsRoomVariableUpdateAction.VariableUpdated){
			if(e.getName().equals(fuelVariables)){
				fuelVariablesExists = true;
				showFuelVariables();
			}
			if(e.getName().equals(roundNumberVariable)){
				roundNumberVariableExists = true;
				showRoundNumberVariable();
			}
			if(e.getName().equals(playerTurnVariable)){
				playerTurnVariableExists = true;
				showPlayerTurnVariable();
			}
			if(e.getName().equals(winnerVariable)){
				winnerVariableExists = true;
				showWinnerVariable();
			}
			if(e.getName().equals(playAgainVariable)){
				playAgainVariableExists = true;
				showPlayAgainVariable();
			}
			if(e.getName().equals(playersReadyVariable)){
				playersReadyVariableExists = true;
				showPlayersReadyVariable();
			}
			if(e.getName().equals(usersInRoomVariable)){
				usersInRoomVariableExists = true;
				showUsersInRoomVariable();
			}
		}
	}
	
	
	
	
	/**
	 * Logging in as user 1
	 */
	public void doLogin() {
	    String name = "User1";
	    // Build the LoginRequest and populate it
	    EsLoginRequest lr = new EsLoginRequest();
	    lr.setUserName(name);
	    // sent the LoginRequest
	    es.getEngine().send(lr);
	}
	/**
	 * Logging in as user 2
	 */
	public void doLogin2() {
	    String name = "User2";
	    // Build the LoginRequest and populate it
	    EsLoginRequest lr = new EsLoginRequest();
	    lr.setUserName(name);
	    // sent the LoginRequest
	    es.getEngine().send(lr);
	}
	/**
	 * Joining room
	 */
	public void doJoinRoom() {
	    // Create the request
	    EsCreateRoomRequest crr = new EsCreateRoomRequest();
	    crr.setRoomName("FaTRoom");
	    crr.setZoneName("FaTZone");
	    crr.setUsingLanguageFilter(true);
	    // Java clients don't handle video 
	    crr.setReceivingVideoEvents(false);
	    es.getEngine().send(crr);
	}
	
	
	
	
	
	/**
	 * Create fuel variables
	 * @param fuel1
	 * @param fuel2
	 * @param fuel3
	 * @param fuel4
	 */
	public void createFuelVariables(int fuel1, int fuel2, int fuel3, int fuel4) {
		// EsObject to hold the room variable
		EsObject esob = new EsObject();
		esob.setInteger("fuel1", fuel1);
		esob.setInteger("fuel2", fuel2);
		esob.setInteger("fuel3", fuel3);
		esob.setInteger("fuel4", fuel4);
		
		serverVariable(esob, fuelVariables, true);
	}
	/**
	 * Create round number variable
	 * @param roundNumber
	 */
	public void createRoundNumberVariable(int roundNumber) {
		// EsObject to hold the room variable
		EsObject esob = new EsObject();
		esob.setInteger("roundNumber", roundNumber);
		
		serverVariable(esob, roundNumberVariable, true);
	}
	/**
	 * Create player turn variable
	 * @param playerTurn
	 */
	public void createPlayerTurnVariable(boolean playerTurn){
		EsObject esob = new EsObject();
		esob.setBoolean("playerTurn", playerTurn);
		
		serverVariable(esob, playerTurnVariable, true);
	}
	/**
	 * Create winner variable
	 * @param winner
	 */
	public void createWinnerVariable(int winner) {
		EsObject esob = new EsObject();
		esob.setInteger("winner", winner);
		esob.setInteger("player1WinCount", 0);
		esob.setInteger("player2WinCount", 0);
		serverVariable(esob, winnerVariable, true);
	}
	/**
	 * Create play again variable
	 * @param playAgain
	 */
	public void createPlayAgainVariable(int playAgain) {
		EsObject esob = new EsObject();
		esob.setInteger("playAgain", playAgain);
		
		serverVariable(esob, playAgainVariable, true);
	}
	/**
	 * Create players ready variable
	 */
	public void createPlayersReadyVariable() {
		EsObject esob = new EsObject();
		esob.setInteger("playersReady", 0);
		
		serverVariable(esob, playersReadyVariable, true);
	}
	/**
	 * Create users in room variable
	 */
	public void createUsersInRoomVariable() {
		EsObject esob = new EsObject();
		esob.setInteger("usersInRoom", 1);
		
		serverVariable(esob, usersInRoomVariable, true);
	}
	
	/**
	 * Update fuels variables
	 * @param fuel1
	 * @param fuel2
	 * @param fuel3
	 * @param fuel4
	 */
	public void updateFuelVariables(int fuel1, int fuel2, int fuel3, int fuel4) {
		EsObject esob = new EsObject();
		esob.setInteger("fuel1", fuel1);
		esob.setInteger("fuel2", fuel2);
		esob.setInteger("fuel3", fuel3);
		esob.setInteger("fuel4", fuel4);
		
		serverVariable(esob, fuelVariables, false);
	}
	/**
	 * Update round number variable
	 * @param roundNumber
	 */
	public void updateRoundNumberVariable(int roundNumber) {
		EsObject esob = new EsObject();
		esob.setInteger("roundNumber", roundNumber);
		
		serverVariable(esob, roundNumberVariable, false);
	}
	/**
	 * Update player turn variable
	 * @param playerTurn
	 */
	public void updatePlayerTurnVariable(boolean playerTurn){
		playerTurn =! playerTurn;
		EsObject esob = new EsObject();
		esob.setBoolean("playerTurn", playerTurn);
		
		serverVariable(esob, playerTurnVariable, false);
	}
	/**
	 * Update winner variable
	 * @param winner
	 */
	public void updateWinnerVariable(int winner) {
		EsObject esob = new EsObject();
		esob.setInteger("winner", winner);
		
		if(winner == 1){
			player1WinCount++;
		} else if(winner == 2){
			player2WinCount++;
		}
		
		// Reset winner and win count
		else if(winner == 3){
			winner = 0;
			player1WinCount = 0;
			player2WinCount = 0;
		}
		esob.setInteger("player1WinCount", player1WinCount);
		esob.setInteger("player2WinCount", player2WinCount);
		
		serverVariable(esob, winnerVariable, false);
	}
	/**
	 * Update play again variable
	 * @param playAgain
	 */
	public void updatePlayAgainVariable(int playAgain) {
		EsObject esob = new EsObject();
		esob.setInteger("playAgain", playAgain);
		
		serverVariable(esob, playAgainVariable, false);
	}
	/**
	 * Update players ready variable, playersReady is incremented when this 
	 * method is called
	 */
	public void updatePlayersReadyVariable() {
		playersReadyValue++;
		EsObject esob = new EsObject();
		esob.setInteger("playersReady", playersReadyValue);
		
		serverVariable(esob, playersReadyVariable, false);
	}
	/**
	 * Update users in room variable
	 */
	public void updateUsersInRoomVariable() {
		usersInRoomValue = es.getManagerHelper().getUserManager().getUsers().size();
		EsObject esob = new EsObject();
		esob.setInteger("usersInRoom", usersInRoomValue);
		
		serverVariable(esob, usersInRoomVariable, false);
	}
	
	/**
	 * Create or update variables, used by all the create 
	 * and update variable methods
	 * @param variables
	 * @param name
	 * @param isCreateVariable
	 */
	private void serverVariable(EsObject variables, String name, boolean isCreateVariable){
		if(isCreateVariable == true){
			EsCreateRoomVariableRequest request = new EsCreateRoomVariableRequest();
			request.setLocked(false);
			request.setPersistent(true);
			request.setName(name);
			request.setValue(variables);
			request.setRoomId(room.getId());
			request.setZoneId(room.getZoneId());
			es.getEngine().send(request);
		}
		else{
			EsUpdateRoomVariableRequest request = new EsUpdateRoomVariableRequest();
			request.setName(name);
			request.setValue(variables);
			request.setRoomId(room.getId());
			request.setZoneId(room.getZoneId());
			request.setValueChanged(true);
			es.getEngine().send(request);
		}
	}
	
	
	
	/**
	 * Display the fuels variables
	 */
	public void showFuelVariables() {
		if(room.getRoomVariables().size() > 0){
			if(fuelVariablesExists){
				// Get variables
				fuel1Name = room.roomVariableByName(fuelVariables).getName();
				fuel2Name = room.roomVariableByName(fuelVariables).getName();
				fuel3Name = room.roomVariableByName(fuelVariables).getName();
				fuel4Name = room.roomVariableByName(fuelVariables).getName();
				
				fuel1Value = room.roomVariableByName(fuelVariables).getValue().getInteger("fuel1");
				fuel2Value = room.roomVariableByName(fuelVariables).getValue().getInteger("fuel2");
				fuel3Value = room.roomVariableByName(fuelVariables).getValue().getInteger("fuel3");
				fuel4Value = room.roomVariableByName(fuelVariables).getValue().getInteger("fuel4");
			}
			sendFuelVariables();
		}
	}
	/**
	 * Display the round number variable
	 */
	public void showRoundNumberVariable() {
		if(room.getRoomVariables().size() > 0){
			if(roundNumberVariableExists){
				// Get variables
				roundNumberName = room.roomVariableByName(roundNumberVariable).getName();
				roundNumberValue = room.roomVariableByName(roundNumberVariable).getValue().getInteger("roundNumber");
			}
			sendRoundNumberVariable();
		}
	}
	/**
	 * Display the player turn variable
	 */
	public void showPlayerTurnVariable() {
		if(room.getRoomVariables().size() > 0){
			if(playerTurnVariableExists){
				// Get variables
				playerTurnName = room.roomVariableByName(playerTurnVariable).getName();
				playerTurnValue = room.roomVariableByName(playerTurnVariable).getValue().getBoolean("playerTurn");       
			}
			sendPlayerTurnVariable();
		}
	}
	/**
	 * Display the winner variable
	 */
	public void showWinnerVariable() {
		if(room.getRoomVariables().size() > 0){			
			if(winnerVariableExists){
				// Get variables
				winnerName = room.roomVariableByName(winnerVariable).getName();
				winnerValue = room.roomVariableByName(winnerVariable).getValue().getInteger("winner");
				player1WinCount = room.roomVariableByName(winnerVariable).getValue().getInteger("player1WinCount");
				player2WinCount = room.roomVariableByName(winnerVariable).getValue().getInteger("player2WinCount");
			}
			sendWinnerVariable();
		}		
	}
	/**
	 * Display the play again variable
	 */
	public void showPlayAgainVariable() {
		if(room.getRoomVariables().size() > 0){
			if(playAgainVariableExists){
				// Get variables
				playAgainName = room.roomVariableByName(playAgainVariable).getName();
				playAgainValue = room.roomVariableByName(playAgainVariable).getValue().getInteger("playAgain");   
			}
			sendPlayAgainVariable();
		}
	}
	/**
	 * Display the players ready variable
	 */
	public void showPlayersReadyVariable() {
		if(room.getRoomVariables().size() > 0){
			if(playersReadyVariableExists){
				// Get variables
				playersReadyName = room.roomVariableByName(playersReadyVariable).getName();
				playersReadyValue = room.roomVariableByName(playersReadyVariable).getValue().getInteger("playersReady");
			}
			sendPlayersReadyVariable();
		}
	}
	/**
	 * Display the users in room variable
	 */
	public void showUsersInRoomVariable() {
		if(room.getRoomVariables().size() > 0){
			if(usersInRoomVariableExists){
				// Get variables
				usersInRoomName = room.roomVariableByName(usersInRoomVariable).getName();
				usersInRoomValue = room.roomVariableByName(usersInRoomVariable).getValue().getInteger("usersInRoom");   
			}
			sendUsersInRoomVariable();
		}
	}
	
	
	
	
	
	/**
	 * Server binding
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	public class LocalBinder extends Binder {
		ElectroServerService getService() {
            return ElectroServerService.this;
        }
    }
	@Override
	public void onDestroy(){
		unregisterReceiver(gmr);
	}
	
	
	
	
	
	
	
	
	// nested class extending broadcast receiver
	private class GameMsgReceiver extends BroadcastReceiver
	{
	    @Override
	    public void onReceive(Context context, Intent intent)
	    {
	    	String action=intent.getAction();
	    	Bundle bundle = intent.getExtras();
	   		String type = bundle.getString("action");
	    	
	   		// Main activity player message
	    	if(action.equalsIgnoreCase(MainActivity.NEW_PLAYER_MESSAGE)){
	    		
	    		// Create players ready variable
	    		if (type.equals("playersReadyCreate")) {
	    			playersReadyVariableExists = true;
					createPlayersReadyVariable();
	    		}
	    		
	    		// Create users in room variable
	    		if (type.equals("usersInRoomCreate")) {
	    			usersInRoomVariableExists = true;
					createUsersInRoomVariable();
	    		}
	    		
	    		// Update players ready variable
	    		if (type.equals("playersReady")) {
	    			playersReadyVariableExists = true;
					updatePlayersReadyVariable();
	    		}
	    		
	    		// Update users in room variable
	    		if (type.equals("usersInRoom")) {
	    			usersInRoomVariableExists = true;
					updateUsersInRoomVariable();
	    		}
	    	}
	    	
	    	// Game activity player message
	    	else if(action.equalsIgnoreCase(GameActivity.NEW_PLAYER_GAME_MESSAGE)){
	    		
	    		// Reset winner, round wins and losses
	    		if(type.equals("roundsReset")) {
	    			winnerVariableExists = true;
	    			updateWinnerVariable(3);
	    		}
	    		
	    		// Create fuels variables
	    	    if (type.equals("fuelVariablesCreate")) {
	    	    	fuel1Value = bundle.getInt("fuel1");
					fuel2Value = bundle.getInt("fuel2");
					fuel3Value = bundle.getInt("fuel3");
					fuel4Value = bundle.getInt("fuel4");
	    	    	fuelVariablesExists = true;
					createFuelVariables(fuel1Value, fuel2Value, fuel3Value, fuel4Value);
				}
	    	    
	    	    // Create round number variable
	    	    else if (type.equals("roundNumberVariableCreate")) {
					roundNumberValue = bundle.getInt("roundNumber");
					roundNumberVariableExists = true;
					createRoundNumberVariable(roundNumberValue);
				}
	    	    
	    	    // Create player turn variable
	    	    else if (type.equals("playerTurnVariableCreate")) {
					playerTurnValue = bundle.getBoolean("playerTurn");
					playerTurnVariableExists = true;
					createPlayerTurnVariable(playerTurnValue);
				} 
	    	    
	    	    // Create winner variable
	    	    else if (type.equals("winnerVariableCreate")) {
					winnerValue = bundle.getInt("winner");
					winnerVariableExists = true;
					createWinnerVariable(winnerValue);
				} 
	    	    
	    	    // Create play again variable
	    	    else if (type.equals("playAgainVariableCreate")) {
					playAgainValue = bundle.getInt("playAgain");
					playAgainVariableExists = true;
					createPlayAgainVariable(playAgainValue);
				}
	    	    
	    	    
	    	    
	    	    // Update fuels variables
	    	    else if (type.equals("fuelVariablesUpdate")) {
					fuel1Value = bundle.getInt("fuel1");
					fuel2Value = bundle.getInt("fuel2");
					fuel3Value = bundle.getInt("fuel3");
					fuel4Value = bundle.getInt("fuel4");
					fuelVariablesExists = true;
					updateFuelVariables(fuel1Value, fuel2Value, fuel3Value, fuel4Value);
				}
	    	    // Update round number variable
	    	    else if (type.equals("roundNumberVariableUpdate")) {
					roundNumberValue = bundle.getInt("roundNumber");
					roundNumberVariableExists = true;
					updateRoundNumberVariable(roundNumberValue);
				} 
	    	    // Update player turn variable
	    	    else if (type.equals("playerTurnVariableUpdate")) {
					playerTurnValue = bundle.getBoolean("playerTurn");
					playerTurnVariableExists = true;
					updatePlayerTurnVariable(playerTurnValue);
				} 
	    	    // Update winner variable
	    	    else if (type.equals("winnerVariableUpdate")) {
					winnerValue = bundle.getInt("winner");
					winnerVariableExists = true;
					updateWinnerVariable(winnerValue);
				} 
	    	    // Update play again variable
	    	    else if (type.equals("playAgainVariableUpdate")) {
					playAgainValue = bundle.getInt("playAgain");
					playAgainVariableExists = true;
					updatePlayAgainVariable(playAgainValue);
				}
	    	}
	    }
    }
	
	
	
	
	
	
	
	/**
	 * Used for connection response
	 */
	private void sendConnectedSuccessful() {
		Intent intent = new Intent(NEW_SERVER_MESSAGE);
		intent.putExtra("type", "connection");
		intent.putExtra("status", "Connected");
		sendBroadcast(intent);
	}
	/**
	 * Disconnecting from server
	 */
	private void sendConnectionClosed(String message) {
		Intent intent = new Intent(NEW_SERVER_MESSAGE);
		intent.putExtra("type", "connection");
		intent.putExtra("status", "Disconnected");
		sendBroadcast(intent);
	}
	/**
	 * Send successful login
	 */
	private void sendLoginSuccessful() {
		Intent intent = new Intent(NEW_SERVER_MESSAGE);
		intent.putExtra("type", "login");
		intent.putExtra("userName", userName);
		sendBroadcast(intent);
	}
	/**
	 * Send successful join room
	 */
	private void sendJoinRoomSuccessful() {
		Intent intent = new Intent(NEW_SERVER_MESSAGE);
		intent.putExtra("type", "joinRoom");		
		sendBroadcast(intent);
	}
	/**
	 * Display fuels variables
	 * 
	 */
	private void sendFuelVariables() {
		Intent intent = new Intent(NEW_SERVER_MESSAGE);
		intent.putExtra("type", "fuelVariables");
		intent.putExtra("fuel1", fuel1Value);
		intent.putExtra("fuel2", fuel2Value);
		intent.putExtra("fuel3", fuel3Value);
		intent.putExtra("fuel4", fuel4Value);
		sendBroadcast(intent);
	}
	/**
	 * Display room number variable
	 */
	private void sendRoundNumberVariable() {
		Intent intent = new Intent(NEW_SERVER_MESSAGE);
		intent.putExtra("type", "roundNumberVariable");
		intent.putExtra("roundNumber", roundNumberValue);
		sendBroadcast(intent);
	}
	/**
	 * Display player turn variable
	 */
	private void sendPlayerTurnVariable() {
		Intent intent = new Intent(NEW_SERVER_MESSAGE);
		intent.putExtra("type", "playerTurnVariable");
		intent.putExtra("playerTurn", playerTurnValue);
		sendBroadcast(intent);
	}
	/**
	 * Display winner variable
	 */
	private void sendWinnerVariable() {
		Intent intent = new Intent(NEW_SERVER_MESSAGE);
		intent.putExtra("type", "winnerVariable");
		intent.putExtra("winner", winnerValue);
		intent.putExtra("player1WinCount", player1WinCount);
		intent.putExtra("player2WinCount", player2WinCount);
		sendBroadcast(intent);
	}
	/**
	 * Display play again variable
	 */
	private void sendPlayAgainVariable() {
		Intent intent = new Intent(NEW_SERVER_MESSAGE);
		intent.putExtra("type", "playAgainVariable");
		intent.putExtra("playAgain", playAgainValue);
		sendBroadcast(intent);
	}
	/**
	 * Display players ready variable
	 */
	private void sendPlayersReadyVariable() {
		Intent intent = new Intent(NEW_SERVER_MESSAGE);
		intent.putExtra("type", "players");
		intent.putExtra("playersReady", playersReadyValue);
		sendBroadcast(intent);
	}
	/**
	 * Display users in room variable
	 */
	private void sendUsersInRoomVariable() {
		Intent intent = new Intent(NEW_SERVER_MESSAGE);
		intent.putExtra("type", "users");
		intent.putExtra("usersInRoom", usersInRoomValue);
		sendBroadcast(intent);
	}
}