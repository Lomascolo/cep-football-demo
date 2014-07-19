package com.wso2.axis2.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UC2Service {
	
	private static final int PLAYERS_COUNT = 16;
	private static final int PLAYERS_POSSESSION_TABLE_SIZE = 5;
	private static HashMap<String,Integer> playerIdMap = new HashMap<String, Integer>();
    private static LinkedList<Long[]> playersList = new LinkedList<Long[]>();  //Long[] should have 6 Doubles {player_id,ts,time,shotsOnGoal,successPasses,failedPasses}
    private static double[][] teamsPossession = new double[2][3];  //3 for {ts,time,time_percent} 
    private static Long[][] playerStats = new Long[PLAYERS_COUNT][4]; // 4 for {ts, shotsOnGoal, successPasses, failedPasses}
    private static int[] playerPossession = new int[PLAYERS_POSSESSION_TABLE_SIZE];
    private static int[] scoreBoard = new int[9]; //{pid, scoreA,scoreB,successPassA,successPassB,sogA,sogB,ballPossessA,ballPossessB} 
    
    static{
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
    	
    	for (int i = 0; i < playerPossession.length; i++) {
			playerPossession[i] = 16; 
		}
    	
    	for (int i = 0; i < PLAYERS_COUNT; i++) {
			playerStats[i][0] = (long) 0;
			playerStats[i][1] = (long) 0;
			playerStats[i][2] = (long) 0;
			playerStats[i][3] = (long) 0;
		}
    	
    	scoreBoard[0] = 16;
    	for (int i = 1; i < 9; i++) {
			scoreBoard[i] = 0;
		}
    }
    
    public UC2Service() {

	}
    
    public int[] getScoreBoardData() {
		return scoreBoard;
	}
   
    //this should be updated accordingly
    public Long[][] getPlayersPossessionOld() {
    	Long[][] playersPossession = new Long[16][4];  //3 for {player_id,ts,time,hitcount} 
    	for (int i = 0; i < playersPossession.length; i++) {
			playersPossession[i][0]	= (long) 16;
			playersPossession[i][1]	= (long) 0;
			playersPossession[i][2]	= (long) 0;
			playersPossession[i][3]	= (long) 0;
		}
    	ListIterator<Long[]> listIterator = playersList.listIterator();
    	int index = 0;
    	while (listIterator.hasNext()) {
			Long[] thisPossessor = (Long[]) listIterator.next();
			playersPossession[index] = thisPossessor;
			index++;
		}
		return playersPossession;
	}
    
    //no updates needed
    public double[][] getTeamsPossession() {
		return teamsPossession;
	}
    
    public Long[][] getPlayerStats(){
    	return playerStats;
    }
    
    
    
    public int[] getPlayerPossession(){
    	return playerPossession;
    }
    
    public Long[][] getSuccPassPercentageTopFive() {
    	Long[][] outputArray = new Long[6][2]; //each will have {pid,sog_count}
    	
    	long totalSP = 0; //total succ passes from all the  players.
		for (int i = 0; i < PLAYERS_COUNT; i++) {
			totalSP += playerStats[i][2];
		}
		if (totalSP == 0) {
			outputArray[0][0] = (long) -1; //this will indicate to the flot-data.jag file that no one has made a shot on goal. 
		}
		
		else {
			ArrayList<Long[]> aListSOG = new ArrayList<Long[]>();
	    	for (int i = 0; i < PLAYERS_COUNT; i++) {
	    		aListSOG.add(new Long[]{(long) i,playerStats[i][2]});
	    	}
			Collections.sort(aListSOG, new Comparator<Long[]>() {
			    public int compare(Long[] a, Long[] b) {
			        return (Integer)(a[1]).compareTo(b[1]);
			    }
			});
			Long[] tmpArray = new Long[2];
			for (int i = 0; i < 5; i++) {  //5 bcz we take top 5 players
				tmpArray = aListSOG.remove(aListSOG.size()-1);
				outputArray[i][0] = tmpArray[0];
				outputArray[i][1] = tmpArray[1];
			}
			long sumOthers = 0;
			for (int i = 0; i < aListSOG.size(); i++) {
				tmpArray = aListSOG.remove(i);
				sumOthers += tmpArray[1];
			}
			outputArray[5][0] = (long) -1;  //-1 indicates that this is the sum of others (not a pid)
			outputArray[5][1] = sumOthers;
		}
		return outputArray;
    }
    
    public Long[][] getShotsOnGoalPercentageTopFive() {
		Long[][] outputArray = new Long[6][2]; //each will have {pid,sog_count}
		
		long totalSOG = 0; //total no of shots on goals from all the  players.
		for (int i = 0; i < PLAYERS_COUNT; i++) {
			totalSOG += playerStats[i][1];
		}
		if (totalSOG == 0) {
			outputArray[0][0] = (long) -1; //this will indicate to the flot-data.jag file that no one has made a shot on goal. 
		}
		else {
			ArrayList<Long[]> aListSOG = new ArrayList<Long[]>();
	    	for (int i = 0; i < PLAYERS_COUNT; i++) {
	    		aListSOG.add(new Long[]{(long) i,playerStats[i][1]});
	    	}
			Collections.sort(aListSOG, new Comparator<Long[]>() {
			    public int compare(Long[] a, Long[] b) {
			        return (Integer)(a[1]).compareTo(b[1]);
			    }
			});
			Long[] tmpArray = new Long[2];
			for (int i = 0; i < 5; i++) {  //5 bcz we take top 5 players
				tmpArray = aListSOG.remove(aListSOG.size()-1);
				outputArray[i][0] = tmpArray[0];
				outputArray[i][1] = tmpArray[1];
			}
			long sumOthers = 0;
			for (int i = 0; i < aListSOG.size(); i++) {
				tmpArray = aListSOG.remove(i);
				sumOthers += tmpArray[1];
			}
			outputArray[5][0] = (long) -1;  //-1 indicates that this is the sum of others (not a pid)
			outputArray[5][1] = sumOthers;
		}
		return outputArray;
    }
    
    public long[] getShotsOnGoalPercentageNew() {
		long[] sogPercentages = new long[16];
		long totalSOGTeamA = 0; //total no of shots on goals from all the Team-A players.
		for (int i = 0; i < PLAYERS_COUNT/2; i++) {
			totalSOGTeamA += playerStats[i][1];
		}
		if (totalSOGTeamA == 0) {
			sogPercentages[0] = -1; //this will indicate to the flot-data.jag file that no one has made a shot on goal. 
		}
		else {
			//for team-a
			for (int i = 0; i < PLAYERS_COUNT/2; i++) {
				sogPercentages[i] = (playerStats[i][1]*100)/totalSOGTeamA;
			}
		}	
		long totalSOGTeamB = 0; //total no of shots on goals from all the Team-A players.
		for (int i = 8; i < PLAYERS_COUNT; i++) {
			totalSOGTeamB += playerStats[i][1];
		}
		if (totalSOGTeamB == 0) {
			sogPercentages[8] = -1; //this will indicate to the flot-data.jag file that no one has made a shot on goal. 
		}
		else {
			//for team-b		
			for (int i = 8; i < PLAYERS_COUNT; i++) {
				sogPercentages[i] = (playerStats[i][1]*100)/totalSOGTeamB;
			}
		}
		return sogPercentages;
	}
    
    /*
     * A publisher can call notifyEvent method send events to this UC2Service
     * */
	public void notifyPlayerPossessionEventOld(String event) {
		String[] attributes = event.split(",");
		if(attributes.length!=4){
			System.err.println("Input string sent is invalid. notifyPlayerPossessionEvent failed!");
			return;
		}	
		String player_id_str = attributes[0];  
		long ts = Long.parseLong(attributes[1]);
		long time = Long.parseLong(attributes[2]);
//		System.out.println("time before "+time);
		time = (long) time/1000000000000L;
//		System.out.println("time after "+time);
		if(time<=0){
//			System.out.println("Time is less than or equal to zero!!!");
			return;
		}	
		long hitCount = Long.parseLong(attributes[3]);
		int player_id = findPlayerId(player_id_str);
		
		//added to an object
		Long[] newPossessor = new Long[6];
		newPossessor[0] = (long) player_id;
		newPossessor[1] = ts;
		newPossessor[2] = time;
		newPossessor[3] = (long) 0; //shotOnGoal - this will stay as zero, if the linkedlist contain no element with the same playerid 
		newPossessor[4] = (long) 0; //successPasses
		newPossessor[5] = (long) 0; //failedPasses
		
		if(!playersList.isEmpty()){
			int duplicateIndex = -1;	//There can be an element in playersList for the same player
			int iteratorIndex = 0;
			ListIterator<Long[]> listIterator = playersList.listIterator();
			while (listIterator.hasNext()) {
				Long[] thisPossessor = (Long[]) listIterator.next();
				if(thisPossessor[0] == newPossessor[0]){   //Player's Node Found in List
					duplicateIndex = iteratorIndex;
				}
				iteratorIndex++;
			}
			if(duplicateIndex != -1){  //duplicate entry is there
				Long[] duplicatePossessor = playersList.get(duplicateIndex);
				if(duplicatePossessor[1] > newPossessor[1]){
					return;
				} else{
					newPossessor[3] = duplicatePossessor[3];
					newPossessor[4] = duplicatePossessor[4];
					newPossessor[5] = duplicatePossessor[5];
					playersList.remove(duplicateIndex);
				}
			}
			/*coming to this point means, event should be  inserted to the playersList*/
			ListIterator<Long[]> insertionIterator = playersList.listIterator();   //this iterator is used for inserting an element; thus named insertion
			int iteration = 0;
			while (insertionIterator.hasNext()) {
				Long[] thisPossessor = (Long[]) insertionIterator.next();
				if(thisPossessor[1] < newPossessor[1]){
					playersList.add(iteration, newPossessor);
					return;
				}
				iteration++;
			}
		}else{
			playersList.add(newPossessor);
		}
	}

	public void notifyPlayerPossessionEvent(String event){
		String player_id_str = event;
		int player_id = findPlayerId(player_id_str);
		scoreBoard[0] = player_id;
		
		for (int i = PLAYERS_POSSESSION_TABLE_SIZE-1; i >= 1; i--) {
			playerPossession[i] = playerPossession[i-1];
		}
		playerPossession[0] = player_id;
	}
	
	public void notifyTeamPossessionEvent(String event) {
		String[] attributes = event.split(",");
		if(attributes.length!=4){
			System.err.println("Input string sent is invalid. notifyTeamPossessionEvent failed!");
			return;
		}
		String team_id_str = attributes[0];
		long ts = Long.parseLong(attributes[1]);
		long time = Long.parseLong(attributes[2]);
		double time_percent = Double.parseDouble(attributes[3]);
		int team_id = team_id_str.equals("A") ? 0 : 1;
		//add to teams-possession map
		if(teamsPossession[team_id][0]<ts){
			teamsPossession[team_id][0] = ts;
			teamsPossession[team_id][1] = time;
			teamsPossession[team_id][2] = time_percent;
			
			//for scorebaord
			if(team_id == 0){
				scoreBoard[7] = (int) time_percent;
				scoreBoard[8] = 100 - scoreBoard[7];
			}
		}
//		else
//			System.out.println("Dropping expired event");
	}
	
	public void notifyShotOnGoalEventOld(String event) {
		String[] attributes = event.split(",");
		if(attributes.length!=4){  				//4 for: playerid, ts, time, count
			System.err.println("Input string sent is invalid. notifyShotOnGoalEvent failed!");
			return;
		}
		String player_id_str = attributes[0];  
		long ts = Long.parseLong(attributes[1]);
		long time = Long.parseLong(attributes[2]);
		long shotOnGoalCount = Long.parseLong(attributes[3]);
		
		int player_id = findPlayerId(player_id_str);
		
		time = (long) time/1000000000000L;
		if(time<=0){
			return;
		}
		
		//added to an object
		Long[] newPossessor = new Long[6];
		newPossessor[0] = (long) player_id;
		newPossessor[1] = ts;
		newPossessor[2] = time;
		newPossessor[3] = shotOnGoalCount; //shotOnGoal 
		newPossessor[4] = (long) 0; //successPasses- this will stay as zero, if the linkedlist contain no element with the same playerid 
		newPossessor[5] = (long) 0; //failedPasses
		
		if(!playersList.isEmpty()){
			int duplicateIndex = -1;	//There can be an element in playersList for the same player
			int iteratorIndex = 0;
			ListIterator<Long[]> listIterator = playersList.listIterator();
			while (listIterator.hasNext()) {
				Long[] thisPossessor = (Long[]) listIterator.next();
				if(thisPossessor[0] == newPossessor[0]){   //Player's Node Found in List
					duplicateIndex = iteratorIndex;
				}
				iteratorIndex++;
			}
			if(duplicateIndex != -1){  //duplicate entry is there
				Long[] duplicatePossessor = playersList.get(duplicateIndex);
				if(duplicatePossessor[1] > newPossessor[1]){
					return;
				} else{
					newPossessor[4] = duplicatePossessor[4];
					newPossessor[5] = duplicatePossessor[5];
					playersList.remove(duplicateIndex);
				}
			}
			/*coming to this point means, event should be  inserted to the playersList*/
			ListIterator<Long[]> insertionIterator = playersList.listIterator();   //this iterator is used for inserting an element; thus named insertion
			int iteration = 0;
			while (insertionIterator.hasNext()) {
				Long[] thisPossessor = (Long[]) insertionIterator.next();
				if(thisPossessor[1] < newPossessor[1]){
					playersList.add(iteration, newPossessor);
					return;
				}
				iteration++;
			}
		}else{
			playersList.add(newPossessor);
		}
	}
	
	public void notifyShotOnGoalEvent(String event) {
		String[] attributes = event.split(",");
		if(attributes.length!=3){  				//3 for: playerid, start time, count
			System.err.println("Input string sent is invalid. notifyShotOnGoalEvent failed!");
			return;
		}
		String player_id_str = attributes[0];  
		long startTime = Long.parseLong(attributes[1]);
		long shotOnGoalCount = Long.parseLong(attributes[2]);
		
		int player_id = findPlayerId(player_id_str);
		
		playerStats[player_id][0] = startTime;
		playerStats[player_id][1] = shotOnGoalCount;
		
		//for scorebaord
		if (player_id <8){  //team A
			scoreBoard[5]++;
		}
		else{
			scoreBoard[6]++; //team B
		}
	}
	
	public void notifySuccessPassesEvent(String event){
		String[] attributes = event.split(",");
		if(attributes.length!=3){  				//3 for: playerid, ts, count
			System.err.println("Input string sent is invalid. notifySuccessPassesEvent failed!");
			return;
		}
		String player_id_str = attributes[0];  
		long ts = Long.parseLong(attributes[1]);
		long count = Long.parseLong(attributes[2]);
		
		int player_id = findPlayerId(player_id_str);
		
		//for scorebaord
		if (player_id <8){  //team A
			scoreBoard[3]++;
		}
		else{
			scoreBoard[4]++; //team B
		}
		
		playerStats[player_id][0] = ts;
		playerStats[player_id][2] = count;
	}

	public void notifyFailedPassesEvent(String event){
		String[] attributes = event.split(",");
		if(attributes.length!=3){  				//3 for: playerid, ts, count
			System.err.println("Input string sent is invalid. notifySuccessPassesEvent failed!");
			return;
		}
		String player_id_str = attributes[0];  
		long ts = Long.parseLong(attributes[1]);
		long count = Long.parseLong(attributes[2]);
		
		int player_id = findPlayerId(player_id_str);
		
		playerStats[player_id][0] = ts;
		playerStats[player_id][3] = count;
	}
	
	public void notifyGoalEvent(String event) {
		System.out.println("GAOL!!!"+event);
		if(event.equals("1")){
			//scoreBoard[2]++;
			System.out.println("goal-A");
		}
		if(event.equals("2")){
			scoreBoard[1]++;
			System.out.println("goal-B");
		}
	}
	
	private int findPlayerId(String stringPid){
		return playerIdMap.get(stringPid);
		//return 2;
	}
}
