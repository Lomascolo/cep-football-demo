package com.wso2.axis2.service;

import java.util.HashMap;

public class GameVisualizationService {
	
	private static HashMap<String,Integer> playerIdMap = new HashMap<String, Integer>();
    private static double[][] playersPosition = new double[20][4];  //4 for {ts,x,y,z}   18=16-players,1-ball,1-ShotOnGoal ,1-BallSpeedVector{vx,vy,emptySlot}
    /*
     * 20th will be for offside event detection (passer, passee)
     * shotOnGoal tuple will be as follows: (isFresh,clientMayReadThis, ts, pid)
     * */
    static {
    	playerIdMap.put("Nick Gertje", 0);
    	playerIdMap.put("Dennis Dotterweich", 1);
    	playerIdMap.put("Niklas Waelzlein", 2);
    	playerIdMap.put("Wili Sommer", 3);
    	playerIdMap.put("Philipp Harlass", 4);
    	playerIdMap.put("Roman Hartleb", 5);
    	playerIdMap.put("Erik Engelhardt", 6);
    	playerIdMap.put("Sandro Schneider", 7);
    	playerIdMap.put("Leon Krapf", 8);
    	playerIdMap.put("Kevin Baer", 9);
    	playerIdMap.put("Luca Ziegler", 10);
    	playerIdMap.put("Ben Mueller", 11);
    	playerIdMap.put("Vale Reitstetter", 12);
    	playerIdMap.put("Christopher Lee", 13);
    	playerIdMap.put("Leon Heinze", 14);
    	playerIdMap.put("Leo Langhans", 15);
    	
    	//initialize playersPosition array elements 
    	for (int i = 0; i < playersPosition.length; i++) {
			playersPosition[i][0]	= 0;
			playersPosition[i][1]	= 0;
			playersPosition[i][2]	= 0;
			playersPosition[i][3]	= 0;
		}    	
    }
    
    public GameVisualizationService() {

	}
    
    public double[][] getPlayersPosition() {
/*    	for (int i = 0; i < playersPosition.length; i++) {
    		System.out.println("playersPosition[i][0]:"+playersPosition[i][0]
    				+"playersPosition[i][1]:"+playersPosition[i][1]
    						+" playersPosition[i][2]:"+playersPosition[i][2]
    								+" playersPosition[i][3]:"+playersPosition[i][3]);
    	}*/
    	if(playersPosition[17][0] == 1){
    		playersPosition[17][1] = 1;	//clientMayReadThis 1
    		playersPosition[17][0] = 0;	//isFresh is 0
    	}
    	else{
    		playersPosition[17][1] = 0; //clientMayReadThis 0
    	}
    	if(playersPosition[19][0] == 1){
    		playersPosition[19][1] = 1;	//clientMayReadThis 1
    		playersPosition[19][0] = 0;	//isFresh is 0
    	}
    	else{
    		playersPosition[19][1] = 0; //clientMayReadThis 0
    	}
		return playersPosition;
	}
    
    
    /*
     * A publisher can call notifyEvent method send events to this UC2Service
     * */
	public void notifyPlayersPositionEvent(String event) {
		String[] attributes = event.split(",");
		if(attributes.length!=5){
			System.err.println("Input string sent is invalid. notifyPlayersPositionEvent failed!");
			return;
		}	
		String player_id_str = attributes[0];  
		long ts = Long.parseLong(attributes[1]);
		//I swapped x with y
		double y = ((Double.parseDouble(attributes[2]))/116)+32; //double x = (Double.parseDouble(attributes[2])+52470)/200;
		double x = ((Double.parseDouble(attributes[3])+33960)/116)+41;  //double y = (Double.parseDouble(attributes[3])+33960)/200;
		double z = Double.parseDouble(attributes[4])/116; //this is not used. Thus better be removed. 
		int player_id = findPlayerId(player_id_str);
		
		playersPosition[player_id][0]	= ts;
		playersPosition[player_id][1]	= x;
		playersPosition[player_id][2]	= y;
		playersPosition[player_id][3]	= z;
		//System.out.println("ts:"+ts+" x:"+x+" y:"+y+" z:"+z);
/*		System.out.println("________________playersPosition[player_id][0]:"+playersPosition[player_id][0]
				+"playersPosition[player_id][1]:"+playersPosition[player_id][1]
						+" playersPosition[player_id][2]:"+playersPosition[player_id][2]
								+" playersPosition[player_id][3]:"+playersPosition[player_id][3]);
    	System.out.println("at notify event...");
		for (int i = 0; i < playersPosition.length; i++) {
    		System.out.println("playersPosition[i][0]:"+playersPosition[i][0]
    				+"playersPosition[i][1]:"+playersPosition[i][1]
    						+" playersPosition[i][2]:"+playersPosition[i][2]
    								+" playersPosition[i][3]:"+playersPosition[i][3]);
    	}*/
	}

	
	public void notifyBallPositionEvent(String event) {
		String[] attributes = event.split(",");
		if(attributes.length!=6){
			System.err.println("Input string sent is invalid. notifyBallPositionEvent failed!");
			return;
		}
		long ts = Long.parseLong(attributes[0]);
		double y = ((Double.parseDouble(attributes[1]))/116)+32;  //double x = (Double.parseDouble(attributes[1])+52470)/200;
		double x = ((Double.parseDouble(attributes[2])+33960)/116)+41;  //double y = (Double.parseDouble(attributes[2])+33960)/200;
		double z = Double.parseDouble(attributes[3])/116; //this is not used. Thus better be removed. 
		double vx = Double.parseDouble(attributes[4]);
		double vy = Double.parseDouble(attributes[5]);
		
		//putting ball-position
		playersPosition[16][0]	= ts;
		playersPosition[16][1]	= x;
		playersPosition[16][2]	= y;
		playersPosition[16][3]	= z;
		playersPosition[18][0]	= vx;
		playersPosition[18][1]	= vy;
	}
	
	public void notifyShotOnGoalEvent(String event){
		String[] attributes = event.split(",");
		if(attributes.length!=2){  	//2 will be for {ts, playerId}
			System.err.println("Input string sent is invalid. notifyShotOnGoalEvent failed!");
			return;
		}
		long ts = Long.parseLong(attributes[0]);
		String player_id_str = attributes[1]; 
		int player_id = findPlayerId(player_id_str);
		
		playersPosition[17][0] = 1;
		playersPosition[17][2] = ts;
		playersPosition[17][3] = player_id;
	}
	
	public void notifyOffsideEvent(String event) {
		String[] attributes = event.split(",");
		if(attributes.length!=2){  	//2 will be for {passer, passee}
			System.err.println("Input string sent is invalid. notifyShotOnGoalEvent failed!");
			return;
		}
		String passer_id_str = attributes[0]; 
		int passer_id = findPlayerId(passer_id_str);
		String passee_id_str = attributes[1]; 
		int passee_id = findPlayerId(passee_id_str);
		
		playersPosition[19][0] = 1;
		playersPosition[19][2] = passer_id;
		playersPosition[19][3] = passee_id;
	}
	
	private int findPlayerId(String stringPid){
		return playerIdMap.get(stringPid);
	}
}
