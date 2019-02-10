

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ReadPackets implements Runnable {

	String ipAddr;
	int port;
	int maxNoBucket;
	TimeStamp TimeStamp;

	List<DGIMData> dgimDataList;
	Print print = new Print();

	public ReadPackets(String ipAddr, int port, List<DGIMData> dgimDataList, int maxNoBucket, TimeStamp TimeStamp) {

		this.ipAddr = ipAddr;
		this.port = port;
		this.dgimDataList = dgimDataList;
		this.maxNoBucket = maxNoBucket;
		this.TimeStamp = TimeStamp;

	}
	
	/*
	 * Method: A thread to receive the sockets stream data from 
	 * Socket.in and storing the stream information according  to DGIM rules 
	 */
	@Override
	public void run() {

		BufferedReader br_con;
		String line;
		InetAddress ip;
		try {

			ip = InetAddress.getByName(ipAddr);
			Socket s = new Socket(ip, port);

			br_con = new BufferedReader(new InputStreamReader(s.getInputStream()));
			while (s.isConnected() && (line = br_con.readLine()) != null) {
				TimeStamp.value = TimeStamp.value + 1;
				int packet = Integer.valueOf(line.toString().trim());
				
				if(packet <= 65535) {

					String binPacket = convertBinary(Integer.valueOf(line.toString().trim()));

					print.console(packet + ", ");
					for (int i = 0; i < binPacket.length(); i++) {

						dgimDataList.set(binPacket.length() - 1 - i,
								updateStream(String.valueOf(binPacket.charAt(binPacket.length() - 1 - i)), i));

					}
				}else {
					throw new Exception();
				}


			}
			s.close();
		} catch (UnknownHostException e) {
			print.console("Error: Invalid IP or port number");
			e.printStackTrace();
		} catch (SocketException e) {
			print.console("Error: Invalid IP or port number");
			e.printStackTrace();
		}catch (IOException e) {
			print.console("Error: Invalid input");
			e.printStackTrace();
		}catch (Exception e) {
			print.console("Error: Invalid input");
			e.printStackTrace();
		}

	}

	
	/*
	 * Method: To convert input stream data in to binary format
	 * Input: int Input Stream Data
	 * Output: String Binary data
	 */
	private String convertBinary(int num) {

		String bin = "";
		int dif;

		bin = Integer.toBinaryString(num);

		if ((dif = 16 - bin.length()) != 0) {
			if (dif < 0) {
				print.console("Error: Socket input more than 16 bit...!");
			} else {
				for (int i = 0; i < dif; i++) {
					bin = "0" + bin;
				}
			}
		}

		return bin;

	}
	
	
	/*
	 * Method: To add and update the received stream socket data in the existing DataStructure  
	 * Input: String InputSocketData, int Index
	 * Output: DGIMData UpdatedDGIMData
	 */
	private DGIMData updateStream(String bit, int index) {

		if (dgimDataList.size() == 16) {

			DGIMData dgimdata = dgimDataList.get(15 - index);

			if (bit.equals("1")) {
				Bucket new_bk = new Bucket(1, TimeStamp.value, TimeStamp.value);
				if (dgimdata.StreamBucketList.isEmpty()) {
					dgimdata.StreamBucketList.add(new_bk);
				} else {

					dgimdata.StreamBucketList.add(0, new_bk);
				}

				dgimdata = updateDGIMRules(dgimdata, 0, 1);

			}

			return dgimdata;
		} else {

			print.console("Error: List not initialized properly.");
			return null;
		}

	}

	/*
	 * Method: To update the streams information according to DGIM rules  
	 * Input: DGIMData - dgimData, int Index, int ComparingValue 
	 * Output: DGIMData UpdatedDGIMData
	 */
	private DGIMData updateDGIMRules(DGIMData dgimData, int index, int value) {

		List<Bucket> testBucket = new ArrayList<Bucket>();
		List<Integer> position = new ArrayList<Integer>();

		for (int i = index; i < dgimData.StreamBucketList.size(); i++) {

			if (dgimData.StreamBucketList.get(i).size == value) {
				position.add(i);
			}

		}

		if ((position.size() == 0) || position.size() < maxNoBucket + 1) {
			return dgimData;
		} else {
			for (int j = 0; j < position.size(); j++) {
				int pos = position.get(j);
				testBucket.add(dgimData.StreamBucketList.remove(pos - j));
			}

			Bucket bucket1 = testBucket.remove(testBucket.size() - 1);
			Bucket bucket2 = testBucket.remove(testBucket.size() - 1);
			testBucket.add(mergeBuckets(bucket1, bucket2));

			dgimData.StreamBucketList.addAll(position.get(0), testBucket);

			dgimData = updateDGIMRules(dgimData, position.get(position.size() - 2), value + value);

		}

		return dgimData;
	}

	
	/*
	 * Method: To merge the two buckets of size crossing the MaxSimilarBucket count  
	 * Input: Bucket bucket1, bucket2 
	 * Output: Bucket MergedBucket
	 */
	private Bucket mergeBuckets(Bucket bucket1, Bucket bucket2) {

		return new Bucket(bucket1.size + bucket2.size,
				bucket1.startTimestamp > bucket2.startTimestamp ? bucket1.startTimestamp : bucket2.startTimestamp,
				bucket1.endTimestamp < bucket2.endTimestamp ? bucket1.endTimestamp : bucket2.endTimestamp);
	}

}
