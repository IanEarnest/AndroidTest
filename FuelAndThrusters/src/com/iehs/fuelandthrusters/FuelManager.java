package com.iehs.fuelandthrusters;

import android.widget.Button;
import android.widget.ImageView;

public class FuelManager {
	
	int roundNumber = 1;
	int targetNumber = 1;
	boolean win = false;
	
	int fuel1 = 0;
	int fuel2 = 0;
	int fuel3 = 0;
	int fuel4 = 0;
	
	int fuel1Max = 0;
	int fuel2Max = 0;
	int fuel3Max = 0;
	int fuel4Max = 0;
	
	
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
	
	/**
	 * Set the images from game activity
	 * @param image1
	 * @param image2
	 * @param image3
	 * @param image4
	 */
	public void setImageViews(ImageView image1, ImageView image2, ImageView image3, ImageView image4){
		fuel1Container = image1;
		fuel2Container = image2;
		fuel3Container = image3;
		fuel4Container = image4;
	}
	/**
	 * Set the buttons from game activity
	 * @param button1
	 * @param button2
	 * @param button3
	 * @param button4
	 * @param button5
	 * @param button6
	 * @param button7
	 * @param button8
	 * @param button9
	 * @param button10
	 * @param button11
	 * @param button12
	 */
	public void setButtons(Button button1, Button button2, Button button3, Button button4, Button button5,
			Button button6, Button button7, Button button8, Button button9, Button button10, 
			Button button11, Button button12){
		fuel1Button1Btn = button1;
		fuel1Button2Btn = button2;
		fuel1Button3Btn = button3;
		fuel2Button1Btn = button4;
		fuel2Button2Btn = button5;
		fuel2Button3Btn = button6;
		fuel3Button1Btn = button7;
		fuel3Button2Btn = button8;
		fuel3Button3Btn = button9;
		fuel4Button1Btn = button10;
		fuel4Button2Btn = button11;
		fuel4Button3Btn = button12;
	}
	
	
	/**
	 * Update on screen values and check win state
	 */
	public void updateValues(){
		
		// Check each volume and update images
		if(roundNumber == 1){
			switch(fuel1){
				case 0: fuel1Container.setImageResource(R.drawable.fuel2_0); break;
				case 1: fuel1Container.setImageResource(R.drawable.fuel2_1); break;
				case 2: fuel1Container.setImageResource(R.drawable.fuel2_2); break;
			}
			switch(fuel2){
				case 0: fuel2Container.setImageResource(R.drawable.fuel3_0); break;
				case 1: fuel2Container.setImageResource(R.drawable.fuel3_1); break;
				case 2: fuel2Container.setImageResource(R.drawable.fuel3_2); break;
				case 3: fuel2Container.setImageResource(R.drawable.fuel3_3); break;
			}
			switch(fuel3){
				case 0: fuel3Container.setImageResource(R.drawable.fuel3_0); break;
				case 1: fuel3Container.setImageResource(R.drawable.fuel3_1); break;
				case 2: fuel3Container.setImageResource(R.drawable.fuel3_2); break;
				case 3: fuel3Container.setImageResource(R.drawable.fuel3_3); break;
			}
			switch(fuel4){
				case 0: fuel4Container.setImageResource(R.drawable.fuel5_0); break;
				case 1: fuel4Container.setImageResource(R.drawable.fuel5_1); break;
				case 2: fuel4Container.setImageResource(R.drawable.fuel5_2); break;
				case 3: fuel4Container.setImageResource(R.drawable.fuel5_3); break;
				case 4: fuel4Container.setImageResource(R.drawable.fuel5_4); break;
				case 5: fuel4Container.setImageResource(R.drawable.fuel5_5); break;
			}
		}
		else if(roundNumber == 2){
			switch(fuel1){
				case 0: fuel1Container.setImageResource(R.drawable.fuel8_0); break;
				case 1: fuel1Container.setImageResource(R.drawable.fuel8_1); break;
				case 2: fuel1Container.setImageResource(R.drawable.fuel8_2); break;
				case 3: fuel1Container.setImageResource(R.drawable.fuel8_3); break;
				case 4: fuel1Container.setImageResource(R.drawable.fuel8_4); break;
				case 5: fuel1Container.setImageResource(R.drawable.fuel8_5); break;
				case 6: fuel1Container.setImageResource(R.drawable.fuel8_6); break;
				case 7: fuel1Container.setImageResource(R.drawable.fuel8_7); break;
				case 8: fuel1Container.setImageResource(R.drawable.fuel8_8); break;
			}
			switch(fuel2){
				case 0: fuel2Container.setImageResource(R.drawable.fuel2_0); break;
				case 1: fuel2Container.setImageResource(R.drawable.fuel2_1); break;
				case 2: fuel2Container.setImageResource(R.drawable.fuel2_2); break;
			}
			switch(fuel3){
				case 0: fuel3Container.setImageResource(R.drawable.fuel5_0); break;
				case 1: fuel3Container.setImageResource(R.drawable.fuel5_1); break;
				case 2: fuel3Container.setImageResource(R.drawable.fuel5_2); break;
				case 3: fuel3Container.setImageResource(R.drawable.fuel5_3); break;
				case 4: fuel3Container.setImageResource(R.drawable.fuel5_4); break;
				case 5: fuel3Container.setImageResource(R.drawable.fuel5_5); break;
			}
			switch(fuel4){
				case 0: fuel4Container.setImageResource(R.drawable.fuel6_0); break;
				case 1: fuel4Container.setImageResource(R.drawable.fuel6_1); break;
				case 2: fuel4Container.setImageResource(R.drawable.fuel6_2); break;
				case 3: fuel4Container.setImageResource(R.drawable.fuel6_3); break;
				case 4: fuel4Container.setImageResource(R.drawable.fuel6_4); break;
				case 5: fuel4Container.setImageResource(R.drawable.fuel6_5); break;
				case 6: fuel4Container.setImageResource(R.drawable.fuel6_6); break;
			}
		}
		else if(roundNumber == 3){
			switch(fuel1){
				case 0: fuel1Container.setImageResource(R.drawable.fuel10_0); break;
				case 1: fuel1Container.setImageResource(R.drawable.fuel10_1); break;
				case 2: fuel1Container.setImageResource(R.drawable.fuel10_2); break;
				case 3: fuel1Container.setImageResource(R.drawable.fuel10_3); break;
				case 4: fuel1Container.setImageResource(R.drawable.fuel10_4); break;
				case 5: fuel1Container.setImageResource(R.drawable.fuel10_5); break;
				case 6: fuel1Container.setImageResource(R.drawable.fuel10_6); break;
				case 7: fuel1Container.setImageResource(R.drawable.fuel10_7); break;
				case 8: fuel1Container.setImageResource(R.drawable.fuel10_8); break;
				case 9: fuel1Container.setImageResource(R.drawable.fuel10_9); break;
				case 10: fuel1Container.setImageResource(R.drawable.fuel10_10); break;
			}
			switch(fuel2){
				case 0: fuel2Container.setImageResource(R.drawable.fuel10_0); break;
				case 1: fuel2Container.setImageResource(R.drawable.fuel10_1); break;
				case 2: fuel2Container.setImageResource(R.drawable.fuel10_2); break;
				case 3: fuel2Container.setImageResource(R.drawable.fuel10_3); break;
				case 4: fuel2Container.setImageResource(R.drawable.fuel10_4); break;
				case 5: fuel2Container.setImageResource(R.drawable.fuel10_5); break;
				case 6: fuel2Container.setImageResource(R.drawable.fuel10_6); break;
				case 7: fuel2Container.setImageResource(R.drawable.fuel10_7); break;
				case 8: fuel2Container.setImageResource(R.drawable.fuel10_8); break;
				case 9: fuel2Container.setImageResource(R.drawable.fuel10_9); break;
				case 10: fuel2Container.setImageResource(R.drawable.fuel10_10); break;
			}
			switch(fuel3){
				case 0: fuel3Container.setImageResource(R.drawable.fuel4_0); break;
				case 1: fuel3Container.setImageResource(R.drawable.fuel4_1); break;
				case 2: fuel3Container.setImageResource(R.drawable.fuel4_2); break;
				case 3: fuel3Container.setImageResource(R.drawable.fuel4_3); break;
				case 4: fuel3Container.setImageResource(R.drawable.fuel4_4); break;
			}
			switch(fuel4){
				case 0: fuel4Container.setImageResource(R.drawable.fuel5_0); break;
				case 1: fuel4Container.setImageResource(R.drawable.fuel5_1); break;
				case 2: fuel4Container.setImageResource(R.drawable.fuel5_2); break;
				case 3: fuel4Container.setImageResource(R.drawable.fuel5_3); break;
				case 4: fuel4Container.setImageResource(R.drawable.fuel5_4); break;
				case 5: fuel4Container.setImageResource(R.drawable.fuel5_5); break;
			}
		}
		
		
		// Set enabled and disabled images of each button
		
		// Fuel 1 Button 2
		if(fuel1 > 0 && fuel3 < fuel3Max){
			fuel1Button2Btn.setBackgroundResource(R.drawable.transfer_yes);
		}
		else{
			fuel1Button2Btn.setBackgroundResource(R.drawable.transfer_no);
		}		
		// Fuel 1 Button 3
		if(fuel1 > 0 && fuel4 < fuel4Max){
			fuel1Button3Btn.setBackgroundResource(R.drawable.transfer_yes);
		}
		else{
			fuel1Button3Btn.setBackgroundResource(R.drawable.transfer_no);
		}
		// Fuel 1 Button 1
		if(fuel1 > 0 && fuel2 < fuel2Max){
			fuel1Button1Btn.setBackgroundResource(R.drawable.transfer_yes);
		}
		else{
			fuel1Button1Btn.setBackgroundResource(R.drawable.transfer_no);
		}
		// Fuel 2 Button 3
		if(fuel2 > 0 && fuel4 < fuel4Max){
			fuel2Button3Btn.setBackgroundResource(R.drawable.transfer_yes);
		}
		else{
			fuel2Button3Btn.setBackgroundResource(R.drawable.transfer_no);
		}
		// Fuel 2 Button 2
		if(fuel2 > 0 && fuel3 < fuel3Max){
			fuel2Button2Btn.setBackgroundResource(R.drawable.transfer_yes);
		}
		else{
			fuel2Button2Btn.setBackgroundResource(R.drawable.transfer_no);
		}
		// Fuel 2 Button 1
		if(fuel2 > 0 && fuel1 < fuel1Max){
			fuel2Button1Btn.setBackgroundResource(R.drawable.transfer_yes);
		}
		else{
			fuel2Button1Btn.setBackgroundResource(R.drawable.transfer_no);
		}
		// Fuel 3 Button 1
		if(fuel3 > 0 && fuel1 < fuel1Max){
			fuel3Button1Btn.setBackgroundResource(R.drawable.transfer_yes);
		}
		else{
			fuel3Button1Btn.setBackgroundResource(R.drawable.transfer_no);
		}
		// Fuel 3 Button 2
		if(fuel3 > 0 && fuel2 < fuel2Max){
			fuel3Button2Btn.setBackgroundResource(R.drawable.transfer_yes);
		}
		else{
			fuel3Button2Btn.setBackgroundResource(R.drawable.transfer_no);
		}
		// Fuel 3 Button 3
		if(fuel3 > 0 && fuel4 < fuel4Max){
			fuel3Button3Btn.setBackgroundResource(R.drawable.transfer_yes);
		}
		else{
			fuel3Button3Btn.setBackgroundResource(R.drawable.transfer_no);
		}
		// Fuel 4 Button 2
		if(fuel4 > 0 && fuel2 < fuel2Max){
			fuel4Button2Btn.setBackgroundResource(R.drawable.transfer_yes);
		}
		else{
			fuel4Button2Btn.setBackgroundResource(R.drawable.transfer_no);
		}
		// Fuel 4 Button 1
		if(fuel4 > 0 && fuel1 < fuel1Max){
			fuel4Button1Btn.setBackgroundResource(R.drawable.transfer_yes);
		}
		else{
			fuel4Button1Btn.setBackgroundResource(R.drawable.transfer_no);
		}
		// Fuel 4 Button 3
		if(fuel4 > 0 && fuel3 < fuel3Max){
			fuel4Button3Btn.setBackgroundResource(R.drawable.transfer_yes);
		}
		else{
			fuel4Button3Btn.setBackgroundResource(R.drawable.transfer_no);
		}
		
		
		
		// Win states
		if(roundNumber == 1){
			// target = 1
			if(fuel1 == targetNumber || fuel2 == targetNumber ||
		       fuel3 == targetNumber || fuel4 == targetNumber){
				win = true;
			}
		}
		else if(roundNumber == 2){
			// target = 3
			if(fuel1 == targetNumber || fuel2 == targetNumber ||
			   fuel3 == targetNumber || fuel4 == targetNumber){
				win = true;
			}
		}
		else if(roundNumber == 3){
			// Check two volumes contain 2.
			if(fuel1 == targetNumber && fuel2 == targetNumber ||
			fuel1 == targetNumber && fuel3 == targetNumber ||
			fuel1 == targetNumber && fuel4 == targetNumber ||
			
			fuel2 == targetNumber && fuel3 == targetNumber ||
			fuel2 == targetNumber && fuel4 == targetNumber ||
			
			fuel3 == targetNumber && fuel4 == targetNumber){
				win = true;
			}
		}
		
		MainActivity.log(MainActivity.stageMessage7);
	}
	
	
	/**
	 * Transferring volume values
	 * @param from
	 * @param to
	 */
	public void volumeTransfer(int from, int to){
		
		if(from == 1 && to == 2){
			// 1 to 2
			while(fuel1 > 0 && 
				  fuel2 < fuel2Max){
				fuel1--;
				fuel2++;
			}
		}
		else if(from == 1 && to == 3){
			// 1 to 3
			while(fuel1 > 0 && 
				  fuel3 < fuel3Max){
				fuel1--;
				fuel3++;
			}
		}
		else if(from == 1 && to == 4){
			// 1 to 4
			while(fuel1 > 0 && 
				  fuel4 < fuel4Max){
				fuel1--;
				fuel4++;
			}
		}
		
		
		if(from == 2 && to == 1){
			// 2 to 1
			while(fuel2 > 0 && 
				  fuel1 < fuel1Max){
				fuel2--;
				fuel1++;
			}
		}
		else if(from == 2 && to == 3){
			// 2 to 3
			while(fuel2 > 0 && 
				  fuel3 < fuel3Max){
				fuel2--;
				fuel3++;
			}
		}
		else if(from == 2 && to == 4){
			// 2 to 4
			while(fuel2 > 0 && 
				  fuel4 < fuel4Max){
				fuel2--;
				fuel4++;
			}
		}
		
		
		if(from == 3 && to == 1){
			// 3 to 1
			while(fuel3 > 0 && 
				  fuel1 < fuel1Max){
				fuel3--;
				fuel1++;
			}
		}
		else if(from == 3 && to == 2){
			// 3 to 2
			while(fuel3 > 0 && 
				  fuel2 < fuel2Max){
				fuel3--;
				fuel2++;
			}
		}
		else if(from == 3 && to == 4){
			// 3 to 4
			while(fuel3 > 0 && 
				  fuel4 < fuel4Max){
				fuel3--;
				fuel4++;
			}
		}
		
		
		if(from == 4 && to == 1){
			// 4 to 1
			while(fuel4 > 0 && 
				  fuel1 < fuel1Max){
				fuel4--;
				fuel1++;
			}
		}
		else if(from == 4 && to == 2){
			// 4 to 2
			while(fuel4 > 0 && 
				  fuel2 < fuel2Max){
				fuel4--;
				fuel2++;
			}
		}
		else if(from == 4 && to == 3){
			// 4 to 3
			while(fuel4 > 0 && 
				  fuel3 < fuel3Max){
				fuel4--;
				fuel3++;
			}
		}
	}
	
	
	
	
	/**
	 * Set the value of a fuel
	 * @param whichFuel
	 * @param value
	 */
	public void setFuel(int whichFuel, int value){
		if(whichFuel == 1){
			fuel1 = value;
		}
		else if(whichFuel == 2){
			fuel2 = value;
		}
		else if(whichFuel == 3){
			fuel3 = value;
		}
		else{
			fuel4 = value;
		}
	}
	/**
	 * Get the value of a fuel
	 * @param whichFuel
	 * @return
	 */
	public int getFuel(int whichFuel){
		if(whichFuel == 1){
			return fuel1;
		}
		else if(whichFuel == 2){
			return fuel2;
		}
		else if(whichFuel == 3){
			return fuel3;
		}
		else{
			return fuel4;
		}
	}
	
	/**
	 * set the round number, changes puzzle
	 * @param value
	 */
	public void setRoundNumber(int value){
		roundNumber = value;
		if(roundNumber == 1){
			setPuzzle1();
		}
		else if(roundNumber == 2){
			setPuzzle2();
		}
		else if(roundNumber == 3){
			setPuzzle3();
		}
	}
	/**
	 * Get round number
	 * @return
	 */
	public int getRoundNumber(){
		return roundNumber;
	}
	
	/**
	 * Set win when a player meets the target
	 * @param value
	 */
	public void setWin(boolean value){
		win = value;
	}
	/**
	 * Get win value
	 * @return
	 */
	public boolean getWin(){
		return win;
	}
	
	/**
	 * Get target number
	 * @return
	 */
	public int getTargetNumber(){
		return targetNumber;
	}
	
	
	/**
	 * Setup puzzle 1
	 */
	private void setPuzzle1(){
		fuel1 = 2;
		fuel2 = 0;
		fuel3 = 0;
		fuel4 = 5;
		
		fuel1Max = 2;
		fuel2Max = 3;
		fuel3Max = 3;
		fuel4Max = 5;
		
		targetNumber = 1;
	}
	/**
	 * Setup puzzle 2
	 */
	private void setPuzzle2(){
		fuel1 = 8;
		fuel2 = 2;
		fuel3 = 0;
		fuel4 = 0;
		
		fuel1Max = 8;
		fuel2Max = 2;
		fuel3Max = 5;
		fuel4Max = 6;
		
		targetNumber = 3;		
	}
	/*
	 * Setup puzzle 3
	 */
	private void setPuzzle3(){
		fuel1 = 10;
		fuel2 = 10;
		fuel3 = 0;
		fuel4 = 0;
		
		fuel1Max = 10;
		fuel2Max = 10;
		fuel3Max = 4;
		fuel4Max = 5;
		
		targetNumber = 2;
	}
}

