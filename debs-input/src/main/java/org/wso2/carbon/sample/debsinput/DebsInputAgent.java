package org.wso2.carbon.sample.debsinput;

import org.apache.log4j.Logger;
import org.wso2.carbon.databridge.agent.thrift.DataPublisher;
import org.wso2.carbon.databridge.agent.thrift.exception.AgentException;
import org.wso2.carbon.databridge.commons.Event;
import org.wso2.carbon.databridge.commons.exception.DifferentStreamDefinitionAlreadyDefinedException;
import org.wso2.carbon.databridge.commons.exception.MalformedStreamDefinitionException;
import org.wso2.carbon.databridge.commons.exception.NoStreamDefinitionExistException;
import org.wso2.carbon.databridge.commons.exception.StreamDefinitionException;
import org.wso2.carbon.databridge.commons.exception.TransportException;

import javax.security.sasl.AuthenticationException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.System;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DebsInputAgent {

	private static Logger logger = Logger
			.getLogger(CopyOfDebsInputAgentbackup12JuneMidnight.class);
	public static final String DEBS_INPUTS_STREAM = "sensorStream"; // org.wso2.sample.debs.inputs
																	// //streamDebs
																	// //test
																	// sensorStream
	public static final String VERSION = "1.0.0";

	private static final String[] DIRECTION = { "In", "Out" };
	private static final String[] STATUS_TYPE = { "Proxy", "Sequence",
			"Endpoint" };
	private static final String[] HOST = { "127.0.0.1", "127.0.0.2",
			"127.0.0.3", "127.0.0.4" };
	private static Long[] timestamps;
	private static BufferedReader br = null;
	private static String filename = "small-game6.txt"; // /home/dilini/training/CEP/data/small-game6.txt

	static {

		long currentTimeStamp = System.currentTimeMillis();

		List<Long> timeStampList = new ArrayList<Long>();

		for (int i = 0; i < 10; i++) {
			timeStampList.add(currentTimeStamp - 1000 * (getRandomId(0, 3600)));
			timeStampList.add(currentTimeStamp - 3789
					* getRandomId(3600, 86400));
			timeStampList.add(currentTimeStamp - 6789 * 86400
					* getRandomId(1, 30));
			timeStampList.add(currentTimeStamp - 8789
					* getRandomId(30 * 86400, 12 * 30 * 86400));
			timeStampList.add(currentTimeStamp - 9789
					* getRandomId(12 * 30 * 86400, 5 * 12 * 30 * 86400));
		}

		timestamps = timeStampList.toArray(new Long[timeStampList.size()]);
	}

	public static void main(String[] args)
			throws AgentException,
			MalformedStreamDefinitionException,
			StreamDefinitionException,
			DifferentStreamDefinitionAlreadyDefinedException,
			MalformedURLException,
			AuthenticationException,
			NoStreamDefinitionExistException,
			TransportException,
			SocketException,
			org.wso2.carbon.databridge.commons.exception.AuthenticationException {

		System.out.println("Starting Debs Input Agent");

		KeyStoreUtil.setTrustStoreParams();

		String host = args[0];
		String port = args[1];
		String username = args[2];
		String password = args[3];
		int events = Integer.parseInt(args[4]);

		// create data publisher

		DataPublisher dataPublisher = new DataPublisher("tcp://" + host + ":"
				+ port, username, password);
		String streamId = null;

		try {
			streamId = dataPublisher.findStream(DEBS_INPUTS_STREAM, VERSION);
			System.out.println("Stream already defined");

		} catch (NoStreamDefinitionExistException e) {
			streamId = dataPublisher.defineStream("{" + "  'name':'"
					+ DEBS_INPUTS_STREAM + "'," + "  'version':'" + VERSION
					+ "'," + "  'nickName': ''," + "  'payloadData':["
					+ "          {'name':'sid','type':'STRING'},"
					+ "          {'name':'ts','type':'LONG'},"
					+ "          {'name':'x','type':'DOUBLE'},"
					+ "          {'name':'y','type':'DOUBLE'},"
					+ "          {'name':'z','type':'DOUBLE'},"
					+ "          {'name':'v','type':'DOUBLE'},"
					+ "          {'name':'a','type':'DOUBLE'},"
					+ "          {'name':'vx','type':'DOUBLE'},"
					+ "          {'name':'vy','type':'DOUBLE'},"
					+ "          {'name':'vz','type':'DOUBLE'},"
					+ "          {'name':'ax','type':'DOUBLE'},"
					+ "          {'name':'ay','type':'DOUBLE'},"
					+ "          {'name':'az','type':'DOUBLE'},"
					+ "          {'name':'tsr','type':'LONG'},"
					+ "          {'name':'tsms','type':'LONG'}," + "  ]" + "}");
		}

		// Publish event for a valid stream
		if (!streamId.isEmpty()) {
			System.out.println("Stream ID: " + streamId);

			// code review: buffered reader is only used inside getPayloadData()
			// function. However, if it was initialized there, br==null will
			// have to performed everytime the function is run, which won't be
			// good in terms of performance; hence initialized here.
			try {
				br = new BufferedReader(new FileReader(filename),
						10 * 1024 * 1024);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}

			String line = null;
			try {
				line = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

			int eventCount = 0;
			long sleepCount = 0;
			long prevTimestamp = 0;
            long startTime = 0;
            long endTime = 0;
            long startSystemTime = 0;
            long cumilativeDifference = 0;

            long resetStartTime = System.currentTimeMillis();

			while (line != null) {
                String[] dataStr = line.split(",");
                long time = Long.valueOf(dataStr[1]);

                if (sleepCount == 1){
                    startSystemTime = System.currentTimeMillis();
                    startTime = time;
                    sleepCount++;
                }
				else if (sleepCount == 13000) {
					sleepCount = 1;
					try {
                        long systemTimeDifference = (System.currentTimeMillis() - startSystemTime);
                        long actualTimeDifference = (time - startTime) / 1000000000;
                        cumilativeDifference += (actualTimeDifference - systemTimeDifference);
                        if (cumilativeDifference > 2000){
                            //System.out.println("Reseting Cumilative difference at " + cumilativeDifference);
                            long now = System.currentTimeMillis();
                            long timeToSleep = (1000 - ((now - resetStartTime)/50));
                            System.out.println("Sleeping = " + timeToSleep);
                            resetStartTime = now;
                            if (timeToSleep > 0){
                                Thread.sleep(timeToSleep);
                            }
                            cumilativeDifference = 0;
                        }else{
                            Thread.sleep(150);
                        }
                        //Thread.sleep((actualTimeDifference - systemTimeDifference)/2);
						//Thread.sleep(100); // 200-visu.is.slower,190-visu.is.slower,
											// 180-visu.is.faster,
											// 185-visu.is.faster,
											// 187-visu.is.faster,189-visu.is.slower,
											// 188-vis.solwer
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
					}

				} else {
					sleepCount++;
				}

				double v_kmh = Double.valueOf(dataStr[5]) * 60 * 60 / 1000000000;
				double a_ms = Double.valueOf(dataStr[6]) / 1000000;

				if(time == 10753295594424116l){
					System.out.println(eventCount);
				}
				
				Object[] data = new Object[] { dataStr[0], time,
						Double.valueOf(dataStr[2]), Double.valueOf(dataStr[3]),
						Double.valueOf(dataStr[4]), v_kmh, a_ms,
						Double.valueOf(dataStr[7]), Double.valueOf(dataStr[8]),
						Double.valueOf(dataStr[9]),
						Double.valueOf(dataStr[10]),
						Double.valueOf(dataStr[11]),
						Double.valueOf(dataStr[12]), System.nanoTime(),
						((Double) (time * Math.pow(10, -9))).longValue() };

				Event eventOne = new Event(streamId,
						System.currentTimeMillis(), null, null, data);
				dataPublisher.publish(eventOne);

				eventCount++;
				if (eventCount == 71000) { // this is when the kickoff happens
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				//System.out.println("Event " + eventCount + "published");
				try {
					line = br.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			dataPublisher.stop();
		}
	}

	/*
	 * This is not needed yet. May delete later.
	 */
	public static String getRandomHost() {
		return HOST[getRandomId(63) % 4];
	}

	/*
	 * This is not needed yet. May delete later.
	 */
	private static String getRandomType() {
		return STATUS_TYPE[getRandomId(3)];
	}

	/*
	 * Not sure whether I'll need this.
	 */
	private static Long getMinProcessingTime() {
		return (long) getRandomId(400, 800);
	}

	/*
	 * Not sure whether I'll need this.
	 */
	private static Long getMaxProcessingTime() {
		return (long) getRandomId(1200, 1600);
	}

	/*
	 * Not sure whether I'll need this.
	 */
	private static String getRandomDirection() {
		return DIRECTION[getRandomId(2)];
	}

	/*
	 * Not sure whether I'll need this.
	 */
	private static int getRandomId(int i) {
		Random randomGenerator = new Random();
		return randomGenerator.nextInt(i);
	}

	/*
	 * Not sure whether I'll need this.
	 */
	private static int getRandomId(int i, int j) {
		Random r = new Random();
		return r.nextInt(j - i + 1) + i;
	}

}
