package com.wso2.axis2.service;  

import java.util.HashMap;

public class UC1Service {
	
	private static HashMap<String,Double> playerIdMap = new HashMap<String, Double>();
	private static HashMap<String,Double> intensityMap = new HashMap<String, Double>();
    private static double[][] playerState = new double[16][2];
    
    public UC1Service() {
    	playerIdMap.put("Nick Gertje", (double) 0);
    	playerIdMap.put("Dennis Dotterweich", (double) 1);
    	playerIdMap.put("Niklas Waelzlein", (double) 2);
    	playerIdMap.put("Wili Sommer", (double) 3);
    	playerIdMap.put("Philipp Harlass", (double) 4);
    	playerIdMap.put("Roman Hartleb", (double) 5);
    	playerIdMap.put("Erik Engelhardt", (double) 6);
    	playerIdMap.put("Sandro Schneider", (double) 7);
    	playerIdMap.put("Leon Krapf", (double) 8);
    	playerIdMap.put("Kevin Baer", (double) 9);
    	playerIdMap.put("Luca Ziegler", (double) 10);
    	playerIdMap.put("Ben Mueller", (double) 11);
    	playerIdMap.put("Vale Reitstetter", (double) 12);
    	playerIdMap.put("Christopher Lee", (double) 13);
    	playerIdMap.put("Leon Heinze", (double) 14);
    	playerIdMap.put("Leo Langhans", (double) 15);
    	
    	intensityMap.put("stop", (double) 0);
    	intensityMap.put("trot", (double) 1);
    	intensityMap.put("low", (double) 2);
    	intensityMap.put("medium", (double) 3);
    	intensityMap.put("high", (double) 4);
    	intensityMap.put("sprint", (double) 5);
	}

    public double[][] getPlayerState() {  //was getStockQuote()
		return playerState;  
	}
	
	public void notifyPlayerState(String event) {   //was submitOrder
		String[] items = event.split(",");
		if(items.length!=3){
			System.err.println("Input string sent is invalid. submitOrder failed!");
			return;
		}	
		double playerId = findPlayerId(items[0]);       
		double intensityId = findIntensityId(items[1]);
		double speed = Double.parseDouble(items[2]);
		playerState[(int) playerId][0]=intensityId;
		playerState[(int) playerId][1]=speed;
	}
	
	private double findPlayerId(String stringPid){
		return playerIdMap.get(stringPid);
	}
	
	private double findIntensityId(String stringIntensity) {
		return intensityMap.get(stringIntensity);
	}
}
