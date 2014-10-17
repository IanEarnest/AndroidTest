package com.iehs.fuelandthrusters;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	static final String TAG = "Main";
	
	// Server variables
	public final static String NEW_PLAYER_MESSAGE = "New_Player_Message";
	ElectroServerService mBoundService;
	boolean mIsBound;
	boolean isRegistered;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mBoundService = ((ElectroServerService.LocalBinder)service).getService();
           	startService(new Intent(MainActivity.this,ElectroServerService.class));
            }
        public void onServiceDisconnected(ComponentName className) {
            mBoundService = null;
        }
    };
    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
    		@Override
    		public void onReceive(Context context, Intent intent) {
    			MainActivity.this.receivedBroadcast(intent);
        }
    };
    
    // Connection status messages
    public static String stageMessage1 = "Stage 1 - Connection - Main activity";
    public static String stageMessage2 = "Stage 2 - Connection - Connection/LoadAndConnect";
    public static String stageMessage3 = "Stage 3 - Connection - Login, user";
    public static String stageMessage4 = "Stage 4 - Connection - Join room, join game";
    public static String stageMessage5 = "Stage 5 -  Game - Game activity";
    public static String stageMessage6 = "Stage 6 -  Game - Create all variables";
    public static String stageMessage7 = "Stage 7 -  Game - Update views";
    public static String stageMessage8 = "Stage 8 -  Game - Finish game";
    public static String stageMessage9 = "Stage 9 -  Game - Restart game";
    public static String stageMessage10 = "Stage 10 -  Game - Connection closed";
    
    // Views
    TextView playerCountTV;
    Button playerReadyBtn;
    Button storyBtn;
    Button rulesBtn;	
    Button helpBtn;
    Button cheatBtn;
	TextView storyTV;
	AlphaAnimation alphaAnim;
	
	// Local integers
	int playerNumber = 0;
	int playersReady = 0;
	int usersInRoom = 0;
	
	
    /**
     * Setup the activity views, the text displayed, register and bind to server.
     */
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Views setup
		playerCountTV = (TextView) findViewById(R.id.playerCount);		
		playerReadyBtn = (Button)findViewById(R.id.playerReadyBtn);
		storyBtn = (Button) findViewById(R.id.storyBtn);		
		cheatBtn = (Button)findViewById(R.id.cheatBtn);
		rulesBtn = (Button)findViewById(R.id.rulesBtn);	
	    helpBtn = (Button)findViewById(R.id.helpBtn);
		storyTV = (TextView) findViewById(R.id.story);
		
		// Default views text
		playerCountTV.setText("Players 0/0");
		playerReadyBtn.setText("Ready up");
		storyBtn.setText("Story");
		rulesBtn.setText("Rules");
		helpBtn.setText("Help");
		cheatBtn.setText("Cheat");
		storyTV.setText("The story so far..");
		
        isRegistered = false;
        setButton(false);
        doBindService();
    	registerRecievers();
    	
        log(stageMessage1);
	}
    
    /**
     * Receive messages from the server
     * @param intent
     */
	private void receivedBroadcast(Intent intent) {
		Bundle bundle = intent.getExtras();
		if(bundle.containsKey("type")){
			String type = bundle.getString("type");
			
			// Players ready messages
			if (type.equals("players")) {
				
				// Display players ready and users in room
				playersReady = bundle.getInt("playersReady");
				playerCountTV.setText("Players: " + playersReady + "/" + usersInRoom);
				
				// When a user has pressed ready
				if(playersReady == 1 && usersInRoom == 2){
					Toast.makeText(this, "A player is ready", Toast.LENGTH_SHORT).show();
				}
				
				// When both players are ready, start game
				if(playersReady == 2){
					Toast.makeText(this, "Both players ready, starting game", Toast.LENGTH_SHORT).show();
					Intent gameIntent = new Intent(getApplicationContext(), GameActivity.class);
			    	gameIntent.putExtra("playerNumber", playerNumber);
			    	startActivity(gameIntent);
				}
			}
			
			// Users in room messages
			if (type.equals("users")) {
				
				// Display players ready and users in room
				usersInRoom = bundle.getInt("usersInRoom");
				playerCountTV.setText("Players: " + playersReady + "/" + usersInRoom);
				
				// When both players are in the room, allow them to ready up
				if(usersInRoom == 2){
					setButton(true);
					Toast.makeText(this, "Press ready up to play", Toast.LENGTH_SHORT).show();
				}
			}
			
			// Login and disconnect messages
			if (type.equals("connection")) {
				String connection = bundle.getString("status");
				
				// When activity is connected to server, log user in
				if (connection.equals("Connected")) {
		    	    log(stageMessage2);
		    	    mBoundService.doLogin();
				}
				
				// If the server has gone down or a player has closed the application
				// send the players to home screen
				if(connection.equals("Disconnected")){
					Log.d("Disconnect", "Disconnected");
					Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show();
					Intent homeIntent = new Intent(Intent.ACTION_MAIN);
					homeIntent.addCategory(Intent.CATEGORY_HOME);
					homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				    startActivity(homeIntent);
				}
			} 
			// Login messages
			else if (type.equals("login")) {
				
				// set player number according to userName then join room
				if(bundle.getString("userName").equals("User1")){
					playerNumber = 1;
				} else if(bundle.getString("userName").equals("User2")){
					playerNumber = 2;
				}
				log(stageMessage3 + playerNumber);
				mBoundService.doJoinRoom();
			} 
			// Join room messages
			else if (type.equals("joinRoom")) {
		    	log(stageMessage4);
		    	
		    	// When player 1 joins room, create playerReady and usersInRoom variables
		    	if(playerNumber == 1){
		    		setPlayersReady();
		    		setUsersInRoom();
		    	}
		    	if(playerNumber == 2){
		    		getUsersInRoom();
		    	}
			} 
		}
    }
	
	
	
    /**
     * Bind activity to service
     */
	void doBindService() {
        bindService(new Intent(MainActivity.this, 
        		ElectroServerService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }
	/**
	 * Allow messages to go from main activity to the server
	 */
	void registerRecievers(){
		registerReceiver(intentReceiver, new IntentFilter(ElectroServerService.NEW_SERVER_MESSAGE )); 
      	registerReceiver(intentReceiver, new IntentFilter(ElectroServerService.NEW_GAME_MESSAGE ));
        isRegistered = true;
	}
	/**
	 * Button press, displays story on main activity
	 * @param view
	 */
    public void story(View view){
    	setStory((String) getString(R.string.story));
    }
    /**
	 * Button press, displays rules on main activity
	 * @param view
	 */
    public void rules(View view){
    	setStory((String) getString(R.string.rules));
    }
    /**
	 * Button press, displays help on main activity
	 * @param view
	 */
    public void help(View view){
    	setStory((String) getString(R.string.help));
    }
    /**
	 * Button press, displays cheats on main activity
	 * @param view
	 */
    public void cheat(View view){
    	setStory((String) getString(R.string.cheat));
    }
    /**
     * Set the main activity text view contents
     * @param story
     */
    private void setStory(String story){
    	storyTV.setText(story);
    }
    
    /**
     * Turn ready up button unclickable and half transparent or clickable and visable
     * @param active
     */
    private void setButton(boolean active){
    	float halfAlpha = 0.5f;
        float fullAlpha = 1f;
        float alpha = 0;
        
    	if(active == true){ // Show button
    		alpha = fullAlpha;
    		playerReadyBtn.setClickable(true);
    	}
    	if(active == false){ // Hide button
    		alpha = halfAlpha;
    		playerReadyBtn.setClickable(false);
    	}
    	
    	 // Set half alpha for button
    	alphaAnim = new AlphaAnimation(alpha, alpha);
		alphaAnim.setFillAfter(true);
    	playerReadyBtn.startAnimation(alphaAnim);
    }
    /**
     * Button press, ready up button sends ready to other user
     * @param view
     */
    public void ready(View view){
    	setButton(false);
    	getPlayersReady();
    }
    
    /**
     * Creating playersReady server variable
     */
    private void setPlayersReady(){
		Intent intent = new Intent(NEW_PLAYER_MESSAGE);
		intent.putExtra("action", "playersReadyCreate");
		sendBroadcast(intent);
	}
    /**
     * Creating usersInRoom server variable
     */
    private void setUsersInRoom(){
		Intent intent = new Intent(NEW_PLAYER_MESSAGE);
		intent.putExtra("action", "usersInRoomCreate");
		sendBroadcast(intent);
	}
    
    /**
     * Updating playersReady server variable
     */
    private void getPlayersReady(){
		Intent intent = new Intent(NEW_PLAYER_MESSAGE);
		intent.putExtra("action", "playersReady");
		sendBroadcast(intent);
	}
    /**
     * Updating playersReady server variable
     */
    private void getUsersInRoom(){
		Intent intent = new Intent(NEW_PLAYER_MESSAGE);
		intent.putExtra("action", "usersInRoom");
		sendBroadcast(intent);
	}
    
    
    /**
     * Send a log message to debug with the tag of this activity
     * @param message
     */
    public static void log(String message){
    	Log.d(TAG,message);  	
    }
}
