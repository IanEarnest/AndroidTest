package com.iehs.fuelandthrusters;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends Activity {
	
	// Server variables
	public final static String NEW_PLAYER_GAME_MESSAGE = "New_Player_Game_Message";
    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
    		@Override
    		public void onReceive(Context context, Intent intent) {
    			GameActivity.this.receivedBroadcast(intent);
        }
    };
    
    // Views and buttons
    ImageView fuel1Container;
    ImageView fuel2Container;
    ImageView fuel3Container;
    ImageView fuel4Container;
    
    Button fuel1Button1Btn;
    Button fuel1Button2Btn;
    Button fuel1Button3Btn;
    Button fuel2Button1Btn;
    Button fuel2Button2Btn;
    Button fuel2Button3Btn;
    Button fuel3Button1Btn;
    Button fuel3Button2Btn;
    Button fuel3Button3Btn;
    Button fuel4Button1Btn;
    Button fuel4Button2Btn;
    Button fuel4Button3Btn;
    
    Button playAgainBtn;
    TextView playAgainTV;
    Button activateBtn;
    // Game stats
    TextView gameStatsTV;
 	TextView roundNumberTV;
 	TextView targetNumberTV;
 	// Player stats
 	TextView playerStatsTV;
 	TextView playerTurnTV;
 	TextView movesLeftTV;
 	TextView winnerTV;
 	
 	
 	// Local variables
 	int roundNumber = 1;
 	boolean playerTurn = false;
 	boolean thisPlayersTurn;
 	int moveCount = 0;
	int moveLimit = 10;
 	int winner = 0;
	int playAgain = 0;
	public int playerNumber;
    boolean activate = false;    
    AlphaAnimation alphaAnim;
    float alpha = 0;
    float halfAlpha = 0.5f;
    float fullAlpha = 1f;
	
   
    private FuelManager fuelManager = new FuelManager();
    
    /**
     * Setup the activity views, the text displayed, fuel manager and server variables.
     */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		
		// Views setup
		fuel1Container = (ImageView) findViewById(R.id.fuel1container);
		fuel2Container = (ImageView) findViewById(R.id.fuel2container);
		fuel3Container = (ImageView) findViewById(R.id.fuel3container);
		fuel4Container = (ImageView) findViewById(R.id.fuel4container);
		
		fuel1Button1Btn = (Button) findViewById(R.id.fuel1button1);
		fuel1Button2Btn = (Button) findViewById(R.id.fuel1button2);
		fuel1Button3Btn = (Button) findViewById(R.id.fuel1button3);
		fuel2Button1Btn = (Button) findViewById(R.id.fuel2button1);
		fuel2Button2Btn = (Button) findViewById(R.id.fuel2button2);
		fuel2Button3Btn = (Button) findViewById(R.id.fuel2button3);
		fuel3Button1Btn = (Button) findViewById(R.id.fuel3button1);
		fuel3Button2Btn = (Button) findViewById(R.id.fuel3button2);
		fuel3Button3Btn = (Button) findViewById(R.id.fuel3button3);
		fuel4Button1Btn = (Button) findViewById(R.id.fuel4button1);
		fuel4Button2Btn = (Button) findViewById(R.id.fuel4button2);
		fuel4Button3Btn = (Button) findViewById(R.id.fuel4button3);
		
		playAgainBtn = (Button) findViewById(R.id.playAgain);
		playAgainTV = (TextView) findViewById(R.id.playAgainText);
		activateBtn = (Button) findViewById(R.id.activate);
		// Game stats
		gameStatsTV = (TextView) findViewById(R.id.gameStats);
		roundNumberTV = (TextView) findViewById(R.id.roundNumber);
		targetNumberTV = (TextView) findViewById(R.id.targetNumber);
		// Player stats
		playerStatsTV = (TextView) findViewById(R.id.playerStats);
		playerTurnTV = (TextView) findViewById(R.id.playerTurn);
		movesLeftTV = (TextView) findViewById(R.id.movesLeft);
		winnerTV = (TextView) findViewById(R.id.winner);
		
		
		// Get the player number sent from the main activity
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			playerNumber = extras.getInt("playerNumber");
		}
		
		
		// Default views text
		playAgainBtn.setText("Reset");
		playAgainTV.setText("");
		activateBtn.setText("Open");
		// Game stats
		gameStatsTV.setText("Game Stats");
		roundNumberTV.setText("Round: 1/3");
		targetNumberTV.setText("Target: 1");
		// Player stats
		playerStatsTV.setText("Player Stats");
		playerTurnTV.setText("Your turn");
		movesLeftTV.setText("");
		winnerTV.setText("Won: 0\nLost: 0");
		
		
		// Set puzzle 1 and send buttons to fuelManager
		fuelManager.setRoundNumber(1);
		fuelManager.setImageViews(fuel1Container, fuel2Container, fuel3Container, fuel4Container);
		fuelManager.setButtons(fuel1Button1Btn, fuel1Button2Btn, fuel1Button3Btn, 
						fuel2Button1Btn, fuel2Button2Btn, fuel2Button3Btn, 
						fuel3Button1Btn, fuel3Button2Btn, fuel3Button3Btn, 
						fuel4Button1Btn, fuel4Button2Btn, fuel4Button3Btn);
		fuelManager.updateValues();
		registerRecievers();
		
		
		// Create server variables, only player 1 creates variables
		if(playerNumber == 1){ 
			sendVariableCreate();
		}
		
		// Show/hide display
		checkPlayerTurn(); 
		
		MainActivity.log(MainActivity.stageMessage5);
	}
	/**
	 * Send user to home instead of back to main
	 */
	@Override
    public void onBackPressed() {
		Intent homeIntent = new Intent(Intent.ACTION_MAIN);
		homeIntent.addCategory(Intent.CATEGORY_HOME);
		homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    startActivity(homeIntent);
    }
	
	
	
	/**
     * Receive messages from the server
     * @param intent
     */
	private void receivedBroadcast(Intent intent) {
		Bundle bundle = intent.getExtras();	
		String type = bundle.getString("type");
		// Receive and handle messages
		
		
		// Connection
		if (type.equals("connection")) {
			if(bundle.getString("status").equals("Disconnected")){
				Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show();
				MainActivity.log(MainActivity.stageMessage10);
				// Go home		
				Intent homeIntent = new Intent(Intent.ACTION_MAIN);
				homeIntent.addCategory(Intent.CATEGORY_HOME);
				homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			    startActivity(homeIntent);
			}
		}
		
		
		// variables
		if (type.equals("fuelVariables")) {
			fuelManager.setFuel(1, bundle.getInt("fuel1"));
			fuelManager.setFuel(2, bundle.getInt("fuel2"));
			fuelManager.setFuel(3, bundle.getInt("fuel3"));
			fuelManager.setFuel(4, bundle.getInt("fuel4"));
			// Update fuel values on screen.
			fuelManager.updateValues();
			//sendPlayerTurnUpdate();
		}
		else if (type.equals("roundNumberVariable")) {
			roundNumber = bundle.getInt("roundNumber");
			fuelManager.setRoundNumber(roundNumber);
			// Update round value on screen.
			roundNumber = fuelManager.getRoundNumber();
			roundNumberTV.setText("Round: " + roundNumber + "/3");
			if(fuelManager.getRoundNumber() == 3) {
				targetNumberTV.setText("Target: 2 and 2");
			} else {
				targetNumberTV.setText("Target: " + fuelManager.getTargetNumber());
			}
					
		}
		else if (type.equals("playerTurnVariable")) {
			playerTurn = bundle.getBoolean("playerTurn");
			checkPlayerTurn();
			if(thisPlayersTurn == false){
				playerTurnTV.setText("Other players turn");
			}
			else if(thisPlayersTurn == true){
				playerTurnTV.setText("Your turn");
			}		
		}
		else if (type.equals("winnerVariable")) {
			winner = bundle.getInt("winner");
			int player1WinCount = bundle.getInt("player1WinCount");
			int player2WinCount = bundle.getInt("player2WinCount");
			
			if(playerNumber == 1){
				winnerTV.setText("Won: " + player1WinCount + "\nLost: " + player2WinCount);
			} else if (playerNumber == 2){
				winnerTV.setText("Won: " + player2WinCount + "\nLost: " + player1WinCount);
			}
			
			// When game is over
			if(fuelManager.getRoundNumber() == 3 && (player1WinCount+player2WinCount) == 3){ // or 4 if last is double?
				if(winner == playerNumber){
					winnerTV.setText("You have won");
					Toast.makeText(this, "You win", Toast.LENGTH_SHORT).show();
				} else {
					winnerTV.setText("You have lost");
					Toast.makeText(this, "You lose", Toast.LENGTH_SHORT).show();
				}
				//winnerTV.setText("Player Won: Player " + winner);
				//Toast.makeText(this, "Player " + winner + " has won", Toast.LENGTH_SHORT).show();
				playAgainBtn.setText("Play again");
				setButton(false, activateBtn);
				//setupDisplay(false);
			}
		}
		else if (type.equals("playAgainVariable")) {
			playAgain = bundle.getInt("playAgain");
			// Update winner value on screen.
			if(playAgain == 1){
				playAgainTV.setText("Other player wants to " + playAgainBtn.getText());
			}
			else if(playAgain == 2){
				// Restart game
				// Needs to reset win on fuel
				// Reset all values to puzzle 1
				// Send round number and play again across server
				playAgain = 0;
				winnerTV.setText("");
				playAgainBtn.setText("Reset");
				moveCount = 0;
				setButton(true, playAgainBtn);
				setButton(true, activateBtn);
				sendRoundsReset();
				fuelManager.setWin(false);
				fuelManager.setRoundNumber(1);
				fuelManager.updateValues();
				playAgainTV.setText("");
				sendRoundNumberUpdate();
				MainActivity.log(MainActivity.stageMessage9);
			}
		}
    }
	
	
	
	/**
	 * Allow messages to go from game activity to the server
	 */
	private void registerRecievers(){
		registerReceiver(intentReceiver, new IntentFilter(ElectroServerService.NEW_SERVER_MESSAGE ));
		registerReceiver(intentReceiver, new IntentFilter(ElectroServerService.NEW_GAME_MESSAGE ));
	}
	/**
	 * Button press, sends play again request to other user
	 * @param view
	 */
	public void playAgain(View view){
		setButton(false, playAgainBtn);
		playAgain++;
		sendPlayAgainUpdate();
	}
	/**
	 * Set button to active or deactivated
	 * @param active
	 */
	private void setButton(boolean active, Button button){
		// Show button
    	if(active == true){ 
    		alpha = fullAlpha;
    		button.setClickable(true);
    	}
    	// Hide button
    	if(active == false){ 
    		alpha = halfAlpha;
    		button.setClickable(false);
    	}
    	
    	// Set half alpha for button
    	alphaAnim = new AlphaAnimation(alpha, alpha);
		alphaAnim.setFillAfter(true);
		button.startAnimation(alphaAnim);
	}
	/**
	 * Button press, first press changes background and text, 
	 * second press checks if player has won
	 * @param view
	 */
	public void activate(View view){
		if(activate == false){
			activateBtn.setBackgroundResource(R.drawable.case_no);
			activateBtn.setText("Activate");
			activate = true;
		}
		else if(activate == true){
			
			// Check win, if not won then skip turn.
			if(fuelManager.getWin() == true){
				
				// Player has won stage 1 or 2
				if(fuelManager.getRoundNumber() < 3) {
					winner = playerNumber;
					sendWinnerUpdate();
					winner = 0;
					
					fuelManager.setRoundNumber(fuelManager.getRoundNumber()+1);
					
					sendRoundNumberUpdate();
					sendFuelUpdate();
					fuelManager.setWin(false);
				}
				
				// Player who wins stage 3 wins whole game
				else if(fuelManager.getRoundNumber() == 3){
					winner = playerNumber;
					sendWinnerUpdate();
					winner = 0;
					moveCount = 0;
					sendPlayerTurnUpdate();
				}
			} else{
				
				// This player hasn't won, switch turn
				sendPlayerTurnUpdate();
				moveCount = 0;
			}
			
			// Reset activate button
			activate = false;
			activateBtn.setBackgroundResource(R.drawable.case_yes);
			activateBtn.setText("Open");
		}
	}
	/**
	 * Using fuel manager to transfer fuel volume
	 * values from and to volumes
	 * @param view
	 */
	public void transfer(View view){
		if(thisPlayersTurn == true){
			if(view.getId() == R.id.fuel1button1){
				fuelManager.volumeTransfer(1, 2);
			}
			if(view.getId() == R.id.fuel1button2){
				fuelManager.volumeTransfer(1, 3);
			}
			if(view.getId() == R.id.fuel1button3){
				fuelManager.volumeTransfer(1, 4);
			}
			
			
			if(view.getId() == R.id.fuel2button1){
				fuelManager.volumeTransfer(2, 1);
			}
			if(view.getId() == R.id.fuel2button2){
				fuelManager.volumeTransfer(2, 3);
			}
			if(view.getId() == R.id.fuel2button3){
				fuelManager.volumeTransfer(2, 4);
			}
			
			
			if(view.getId() == R.id.fuel3button1){
				fuelManager.volumeTransfer(3, 1);
			}
			if(view.getId() == R.id.fuel3button2){
				fuelManager.volumeTransfer(3, 2);
			}
			if(view.getId() == R.id.fuel3button3){
				fuelManager.volumeTransfer(3, 4);
			}
			
			
			if(view.getId() == R.id.fuel4button2){
				fuelManager.volumeTransfer(4, 2);
			}
			if(view.getId() == R.id.fuel4button1){
				fuelManager.volumeTransfer(4, 1);
			}
			if(view.getId() == R.id.fuel4button3){
				fuelManager.volumeTransfer(4, 3);
			}
			
			
			sendFuelUpdate();
			
			// Move counter
			moveCount++;
			playerTurnTV.setText("Your turn");
			movesLeftTV.setText("" + (moveLimit - moveCount) + " Moves left");
			
			// Switch turns when out of moves
			if(moveCount >= moveLimit){
				sendPlayerTurnUpdate();
				moveCount = 0;
			}
		}
	}
	/**
	 * Check to see which player's turn it currently is
	 */
	public void checkPlayerTurn(){
		// You are Player 1 and its Player 1 turn
		if(playerNumber == 1 && playerTurn == false){ 
			thisPlayersTurn = true;
		} 
		// You are Player 1 and its Player 2 turn
		if(playerNumber == 1 && playerTurn == true){ 
			thisPlayersTurn = false;
		} 
		// You are Player 2 and its Player 2 turn
		if(playerNumber == 2 && playerTurn == true) { 
			thisPlayersTurn = true;
		}
		// You are Player 2 and its Player 1 turn
		if(playerNumber == 2 && playerTurn == false){ 
			thisPlayersTurn = false;
		} 
		if(thisPlayersTurn == true){
			setupDisplay(true);
			setButton(true, activateBtn);
		}
		
		if(thisPlayersTurn == false){
			setupDisplay(false);
			setButton(false, activateBtn);
		}
	}
	/**
	 * Set the buttons and views visibility when player turn changes
	 * @param visibility
	 */
	public void setupDisplay(boolean visibility){
		if(visibility == true){
			alpha = 1f;
		}
		if(visibility == false){
			alpha = 0.5f;
		}
		
		// Alpha animation
		alphaAnim = new AlphaAnimation(alpha, alpha);
		alphaAnim.setFillAfter(true);
		
		fuel1Container.startAnimation(alphaAnim);
		fuel2Container.startAnimation(alphaAnim);
		fuel3Container.startAnimation(alphaAnim);
		fuel4Container.startAnimation(alphaAnim);
		
		fuel1Button1Btn.startAnimation(alphaAnim);
		fuel1Button2Btn.startAnimation(alphaAnim);
		fuel1Button3Btn.startAnimation(alphaAnim);
		fuel2Button1Btn.startAnimation(alphaAnim);
		fuel2Button2Btn.startAnimation(alphaAnim);
		fuel2Button3Btn.startAnimation(alphaAnim);
		fuel3Button1Btn.startAnimation(alphaAnim);
		fuel3Button2Btn.startAnimation(alphaAnim);
		fuel3Button3Btn.startAnimation(alphaAnim);
		fuel4Button1Btn.startAnimation(alphaAnim);
		fuel4Button2Btn.startAnimation(alphaAnim);
		
		fuel1Button1Btn.setClickable(visibility);
		fuel1Button2Btn.setClickable(visibility);
		fuel1Button3Btn.setClickable(visibility);
		fuel2Button1Btn.setClickable(visibility);
		fuel2Button2Btn.setClickable(visibility);
		fuel2Button3Btn.setClickable(visibility);
		fuel3Button1Btn.setClickable(visibility);
		fuel3Button2Btn.setClickable(visibility);
		fuel3Button3Btn.setClickable(visibility);
		fuel4Button1Btn.setClickable(visibility);
		fuel4Button2Btn.setClickable(visibility);
	}
	
	
	/**
	 * Creating all server variables
	 */
	private void sendVariableCreate(){
		sendFuelVariableCreate();
		sendRoundNumberVariableCreate();
		sendPlayerTurnVariableCreate();
		sendWinnerVariableCreate();
		sendPlayAgainVariableCreate();
		MainActivity.log(MainActivity.stageMessage6);
	}
	/**
     * Creating fuels server variables
     */
	private void sendFuelVariableCreate(){
		Intent intent = new Intent(NEW_PLAYER_GAME_MESSAGE);
		intent.putExtra("action", "fuelVariablesCreate");
		intent.putExtra("fuel1", fuelManager.getFuel(1));
		intent.putExtra("fuel2", fuelManager.getFuel(2));
		intent.putExtra("fuel3", fuelManager.getFuel(3));
		intent.putExtra("fuel4", fuelManager.getFuel(4));
		sendBroadcast(intent);
		
	}
	/**
     * Creating round number server variable
     */
	private void sendRoundNumberVariableCreate(){
		Intent intent = new Intent(NEW_PLAYER_GAME_MESSAGE);
		intent.putExtra("action", "roundNumberVariableCreate");
		intent.putExtra("roundNumber", fuelManager.getRoundNumber());
		sendBroadcast(intent);
	}
	/**
     * Creating player turn server variable
     */
	private void sendPlayerTurnVariableCreate(){
		Intent intent = new Intent(NEW_PLAYER_GAME_MESSAGE);
		intent.putExtra("action", "playerTurnVariableCreate");
		intent.putExtra("playerTurn", playerTurn);
		sendBroadcast(intent);
	}
	/**
     * Creating winner server variable
     */
	private void sendWinnerVariableCreate(){
		Intent intent = new Intent(NEW_PLAYER_GAME_MESSAGE);
		intent.putExtra("action", "winnerVariableCreate");
		intent.putExtra("winner", winner);
		sendBroadcast(intent);
	}
	/**
     * Creating play again server variable
     */
	private void sendPlayAgainVariableCreate(){
		Intent intent = new Intent(NEW_PLAYER_GAME_MESSAGE);
		intent.putExtra("action", "playAgainVariableCreate");
		intent.putExtra("playAgain", playAgain);
		sendBroadcast(intent);
	}
	
	
	
	/**
     * Updating fuels server variables
     */
	private void sendFuelUpdate(){
		Intent intent = new Intent(NEW_PLAYER_GAME_MESSAGE);
		intent.putExtra("action", "fuelVariablesUpdate");
		intent.putExtra("fuel1", fuelManager.getFuel(1));
		intent.putExtra("fuel2", fuelManager.getFuel(2));
		intent.putExtra("fuel3", fuelManager.getFuel(3));
		intent.putExtra("fuel4", fuelManager.getFuel(4));
		sendBroadcast(intent);
	}
	/**
     * Updating round number server variable
     */
	private void sendRoundNumberUpdate(){
		Intent intent = new Intent(NEW_PLAYER_GAME_MESSAGE);
		intent.putExtra("action", "roundNumberVariableUpdate");
		intent.putExtra("roundNumber", fuelManager.getRoundNumber());
		sendBroadcast(intent);
	}
	/**
     * Updating player turn server variable
     */
	private void sendPlayerTurnUpdate(){
		Intent intent = new Intent(NEW_PLAYER_GAME_MESSAGE);
		intent.putExtra("action", "playerTurnVariableUpdate");
		intent.putExtra("playerTurn", playerTurn);
		sendBroadcast(intent);
	}
	/**
     * Updating winner server variable
     */
	private void sendWinnerUpdate(){
		Intent intent = new Intent(NEW_PLAYER_GAME_MESSAGE);
		intent.putExtra("action", "winnerVariableUpdate");
		intent.putExtra("winner", winner);
		sendBroadcast(intent);
	}
	/**
     * Updating play again server variable
     */
	private void sendPlayAgainUpdate(){
		Intent intent = new Intent(NEW_PLAYER_GAME_MESSAGE);
		intent.putExtra("action", "playAgainVariableUpdate");
		intent.putExtra("playAgain", playAgain );
		sendBroadcast(intent);
	}
	/**
     * Resetting round number and winner server variables
     */
	private void sendRoundsReset(){
		Intent intent = new Intent(NEW_PLAYER_GAME_MESSAGE);
		intent.putExtra("action", "roundsReset");
		sendBroadcast(intent);
	}
}
