
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class ReadQuery implements Runnable {

	List<DGIMData> dgimDataList;
	TimeStamp TimeStamp;

	public ReadQuery(List<DGIMData> dgimDataList, TimeStamp TimeStamp) {

		this.dgimDataList = dgimDataList;
		this.TimeStamp = TimeStamp;
	}
	/*
	 * Method: A thread to receive the queries from System.in and find the solution
	 * and print the output for each query. 
	 */
	@Override
	public void run() {
		Print print = new Print();
		BufferedReader br_con = null;
		String line = "";

		try {

			br_con = new BufferedReader(new InputStreamReader(System.in));
			while ((line = P2.br_con.readLine()) != null && !line.equals("end") && !line.equals("End")) {
				if (line.isEmpty()) {
					print.console("Invalid Input...! Expecting Query");
				} else {
					print.console(line);
					long queryValue = Long.valueOf(line.split(" ")[6]);

					long currentTS = TimeStamp.value;
					if (currentTS - queryValue >= 0) {

						double sum = calculateSum(queryValue, currentTS);

						print.console("\n\nThe sum of last " + queryValue + " integers is " + sum + ".");
					} else {
						print.console("\n\nSorry! Not sufficient sockets read yet...!");
					}
				}

			}

		} catch (IOException e) {
			print.console("Error: Invalid Input...!");
			e.printStackTrace();
		} catch (NumberFormatException e) {
			print.console("Error: Invalid Query...!");
			e.printStackTrace();
		}catch (ArrayIndexOutOfBoundsException e) {
			print.console("Error: Invalid Query...!");
			e.printStackTrace();
		}

	}

	/*
	 * Method: To calculate the sum of the stream input 
	 * Input: long QueryValue
	 * Output: long Current TimeStamp
	 */
	private long calculateSum(long queryValue, long currentTS) {

		long sum = 0;

		for (int i = 0; i < dgimDataList.size(); i++) {
			sum = (long) (sum + (Math.pow(2, dgimDataList.size() - 1 - i))
					* DGIMsum(dgimDataList.get(i), currentTS - queryValue + 1));

		}

		return sum;
	}

	/*
	 * Method: To calculate the sum of a binary format number
	 * Input: DGIMData dgimData, 
	 * Output: Long queryValue Pointer index to limit count
	 */
	private long DGIMsum(DGIMData dgimData, long queryValue) {

		long bucketSum = 0;

		for (int i = 0; i < dgimData.StreamBucketList.size(); i++) {

			long counterStart = dgimData.StreamBucketList.get(i).startTimestamp;
			long counterEnd = dgimData.StreamBucketList.get(i).endTimestamp;

			if (queryValue <= counterEnd) {

				bucketSum = bucketSum + dgimData.StreamBucketList.get(i).size;
			} else if (queryValue > counterEnd && queryValue <= counterStart) {
				bucketSum = bucketSum + (dgimData.StreamBucketList.get(i).size) / 2;
				break;
			} else if (queryValue > counterStart) {
				break;
			}

		}

		return bucketSum;
	}

}
