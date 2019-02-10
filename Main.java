import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {
	
	public static BufferedReader br_con = new BufferedReader(new InputStreamReader(System.in));

	public static void main(String[] args) {

		
		String accuracy = "";
		String line = "";
		String splitBy = ":";
		String ip = "";
		int port = 0;
		int maxNoBucket = 0;
		long num = 0;
		TimeStamp TimeStamp = new TimeStamp(num);

		List<DGIMData> dgimDataList = new ArrayList<DGIMData>();
		for (int i = 0; i < 16; i++) {
			DGIMData dgimData = new DGIMData();
			dgimDataList.add(dgimData);
		}

		try {

			//br_con = new BufferedReader(new InputStreamReader(System.in));
			accuracy = br_con.readLine().trim().split("%")[0];
			Double acc = Double.valueOf(accuracy);
			if (!(acc > 0 && acc <= 100)) {
				throw new Exception();
			}

			maxNoBucket = (int) (100 / Double.valueOf(accuracy));
			line = br_con.readLine();
			String[] inputs = line.split(splitBy);
			ip = inputs[0].toString().trim();
			port = Integer.valueOf(inputs[1].toString().trim());

		} catch (IOException e) {
			System.out.println("Please insert a Valid Input...!");
			e.printStackTrace();
		} catch (StringIndexOutOfBoundsException e) {
			System.out.println("Please insert a Valid Input...!");
			e.printStackTrace();
		} catch (ArithmeticException e) {
			System.out.println("Please insert a Valid Input...!");
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.out.println("Please insert a Valid Input...!");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Please insert a Valid Input...!");
			e.printStackTrace();
		}

		Thread readPacket = new Thread(new ReadPackets(ip, port, dgimDataList, maxNoBucket, TimeStamp));
		Thread readQuery = new Thread(new ReadQuery(dgimDataList, TimeStamp));

		readQuery.start();
		readPacket.start();

	}

}
