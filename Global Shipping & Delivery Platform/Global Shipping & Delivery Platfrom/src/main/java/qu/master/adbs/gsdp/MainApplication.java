package qu.master.adbs.gsdp;

import qu.master.adbs.gsdp.service.RestApplication;

public class MainApplication {
	
	private static final String serviceHost = "localhost";
	private static final int servicePort = 9090;
	
	public static void main(String[] args) {
		
		System.out.println("Welcome to Global Shipping & Delivery Platfrom ! ");
		//insertTestData();
		startService();
		
	}
	
	private static void insertTestData() {
		try {
			System.out.println("Inserting Test Data ...\n");
			TestDataProducer.insertTestData("GSDP_MSSQL");
			System.out.println("\nDone Inserting Test Data ...");
		}
		
		catch (Exception e) {
			System.err.println("Error in Inserting Test Data !!! ");
			e.printStackTrace();
		}
		
	}
	
	private static void startService() {

		try {
			String serviceUrl = serviceHost + ":" + servicePort;
			System.out.println("Starting Rest Service @ " +serviceUrl);
			
			RestApplication.startService(serviceHost, servicePort);
			
			System.out.println("Rest Service Successfully Started @ " + serviceUrl);
		}
		
		catch (Exception e) {
			System.err.println("Error in Starting Rest Service !");
			e.printStackTrace();
		}

		
	}
	

}
