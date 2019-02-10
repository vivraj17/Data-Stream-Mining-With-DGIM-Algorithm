

public class Print {

	/*
	 * Method: Synchronized Method to take lock and accessed by one Thread at a time.  
	 * Input: String - S 
	 * Output: Print - Console(system.out)
	 */
	public synchronized void console(String s) {
		
		if(s.contains(",")) {
			System.out.print(s);
		}else {
						
			System.out.println(s);
		}
		
	}
	
}
