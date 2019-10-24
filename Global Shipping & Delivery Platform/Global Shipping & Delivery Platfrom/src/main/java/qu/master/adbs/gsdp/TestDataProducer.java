package qu.master.adbs.gsdp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import qu.master.adbs.gsdp.entity.City;
import qu.master.adbs.gsdp.entity.Country;
import qu.master.adbs.gsdp.entity.Customer;
import qu.master.adbs.gsdp.entity.PaymentCreditCard;
import qu.master.adbs.gsdp.entity.PaymentMethodType;
import qu.master.adbs.gsdp.entity.PaymentPaypal;
import qu.master.adbs.gsdp.entity.Shipment;
import qu.master.adbs.gsdp.entity.ShipmentHistory;
import qu.master.adbs.gsdp.entity.ShipmentPayment;
import qu.master.adbs.gsdp.entity.ShipmentStatusType;
import qu.master.adbs.gsdp.entity.Supplier;
import qu.master.adbs.gsdp.repository.AccountsRepository;
import qu.master.adbs.gsdp.repository.JpaAccountsRepository;
import qu.master.adbs.gsdp.repository.JpaLocationsRepository;
import qu.master.adbs.gsdp.repository.JpaShipmentsRepository;
import qu.master.adbs.gsdp.repository.LocationsRepository;
import qu.master.adbs.gsdp.repository.ShipmentsRepository;

public class TestDataProducer {
	
	public static void insertTestData(String persistenceUnitName) {
		try {
			insertCustomers(persistenceUnitName);
			insertShipmentStatusesTypes(persistenceUnitName);
			insertSuppliers(persistenceUnitName);
			insertCountries(persistenceUnitName);
			insertPaymentsMethodsTypes(persistenceUnitName);
			insertShipments(persistenceUnitName);
			insertShipmentsHistories(persistenceUnitName);
			insertShipmentsPayments(persistenceUnitName);
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void insertCustomers(String persistenceUnitName) {
		AccountsRepository accountsRepo = new JpaAccountsRepository(persistenceUnitName);
		
		Customer customer1 = new Customer();
		customer1.setFirstName("Abdulrahman");
		customer1.setLastName("Alkhayarin");
		customer1.setEmail("aa084038@qu.edu.qa");
		customer1.setPassword("1234");
		customer1.setBirthDate(LocalDate.of(1991, 1, 9));
		customer1.setRole('C');
		
		System.out.println("Adding Customer " + customer1.getFullName());
		accountsRepo.addCustomer(customer1);
		System.out.println("Successfully added Customer with id = " + customer1.getId());
		
		Customer customer2 = new Customer();
		customer2.setFirstName("Fawaz");
		customer2.setLastName("Kserawi");
		customer2.setEmail("fk1000048@qu.edu.qa");
		customer2.setPassword("qwer");
		customer2.setBirthDate(LocalDate.of(1981, 7, 19));
		customer2.setRole('C');
		
		System.out.println("Adding Customer " + customer2.getFullName());
		accountsRepo.addCustomer(customer2);
		System.out.println("Successfully added Customer with id = " + customer2.getId());
		
		Customer customer3 = new Customer();
		customer3.setFirstName("Joseph");
		customer3.setLastName("Legere");
		customer3.setEmail("jl1903194@qu.edu.qa");
		customer3.setPassword("password");
		customer3.setBirthDate(LocalDate.of(1985, 12, 23));
		customer3.setRole('C');
		
		System.out.println("Adding Customer " + customer3.getFullName());
		accountsRepo.addCustomer(customer3);
		System.out.println("Successfully added Customer with id = " + customer3.getId());
	}
	
	private static void insertShipments(String persistenceUnitName) {
		AccountsRepository accountsRepo = new JpaAccountsRepository(persistenceUnitName);
		ShipmentsRepository shipRepo = new JpaShipmentsRepository(persistenceUnitName);
		
		List<Customer> customers = accountsRepo.getCustomers();
		Customer customer1 = customers.get(0);
		Customer customer2 = customers.get(1);
		Customer customer3 = customers.get(2);
		
		List<Supplier> suppliers = accountsRepo.getSuppliers();
		Supplier supplier1 = suppliers.get(0);
		Supplier supplier2 = suppliers.get(1);
		Supplier supplier3 = suppliers.get(2);
		
		System.out.println("Start adding Shipments ... ");
		Shipment s1 = new Shipment();
		s1.setCustomer(customer1);
		s1.setSupplier(supplier1);
		s1.setDescription("iPhone 11");
		s1.setNumberOfItems(3);
		s1.setWeight(9.6);
		s1.setAddedDate(LocalDateTime.of(2019, 1, 6, 13, 4, 22));
		shipRepo.addShipment(s1);
		
		Shipment s2 = new Shipment();
		s2.setCustomer(customer1);
		s2.setSupplier(supplier1);
		s2.setDescription("Game of Thrones Series");
		s2.setNumberOfItems(9);
		s2.setWeight(1.3);
		s2.setAddedDate(LocalDateTime.of(2019, 2, 13, 23, 4, 22));
		shipRepo.addShipment(s2);
		
		Shipment s3 = new Shipment();
		s3.setCustomer(customer1);
		s3.setSupplier(supplier2);
		s3.setDescription("Samsung Galaxy S8 ");
		s3.setNumberOfItems(1);
		s3.setWeight(8.4);
		s3.setAddedDate(LocalDateTime.of(2019, 3, 19, 13, 24, 22));
		shipRepo.addShipment(s3);
		
		Shipment s4 = new Shipment();
		s4.setCustomer(customer2);
		s4.setSupplier(supplier2);
		s4.setDescription("Java EE Full Guide");
		s4.setNumberOfItems(4);
		s4.setWeight(11.8);
		s4.setAddedDate(LocalDateTime.of(2019, 4, 16, 11, 12, 22));
		shipRepo.addShipment(s4);
		
		Shipment s5 = new Shipment();
		s5.setCustomer(customer2);
		s5.setSupplier(supplier3);
		s5.setDescription("GEFORCE GTX 10");
		s5.setNumberOfItems(2);
		s5.setWeight(6.7);
		s5.setAddedDate(LocalDateTime.of(2019, 5, 27, 3, 8, 22));
		shipRepo.addShipment(s5);
		
		Shipment s6 = new Shipment();
		s6.setCustomer(customer3);
		s6.setSupplier(supplier3);
		s6.setDescription("Apple WatchSeries 5 GPS");
		s6.setNumberOfItems(1);
		s6.setWeight(3.2);
		s6.setAddedDate(LocalDateTime.of(2019, 6, 18, 16, 20, 22));
		shipRepo.addShipment(s6);
		
		System.out.println("Done adding Shipments ... ");
	}
	
	private static void insertShipmentsHistories(String persistenceUnitName) {

		AccountsRepository accountsRepo = new JpaAccountsRepository(persistenceUnitName);
		ShipmentsRepository shipRepo = new JpaShipmentsRepository(persistenceUnitName);
		
		List<Shipment> ships = shipRepo.getShipments();
		Shipment s1 = ships.get(0);
		Shipment s2 = ships.get(1);
		Shipment s3 = ships.get(2);
		Shipment s4 = ships.get(3);
		Shipment s5 = ships.get(4);
		Shipment s6 = ships.get(5);
		
		List<ShipmentStatusType> types = shipRepo.getShipmentStatusTypes();
		ShipmentStatusType received = types.get(0);
		ShipmentStatusType shipped = types.get(1);
		ShipmentStatusType delivered = types.get(2);
		ShipmentStatusType cancelled = types.get(3);
		
		System.out.println("Start adding Shipments Histories ... ");
		
		ShipmentHistory sh11 = new ShipmentHistory();
		sh11.setShipment(s1);
		sh11.setShipmentStatus(received);
		sh11.setLastStatusTime(LocalDateTime.of(2019, 8, 2, 12, 18));
		sh11.setAddedDate(sh11.getLastStatusTime());
		shipRepo.addShipmentHistory(sh11);
		
		ShipmentHistory sh21 = new ShipmentHistory();
		sh21.setShipment(s2);
		sh21.setShipmentStatus(received);
		sh21.setLastStatusTime(LocalDateTime.of(2019, 9, 3, 9, 29));
		sh21.setAddedDate(sh21.getLastStatusTime());
		shipRepo.addShipmentHistory(sh21);
		
		ShipmentHistory sh31 = new ShipmentHistory();
		sh31.setShipment(s3);
		sh31.setShipmentStatus(received);
		sh31.setLastStatusTime(LocalDateTime.of(2019, 6, 14, 8, 17));
		sh31.setAddedDate(sh31.getLastStatusTime());
		shipRepo.addShipmentHistory(sh31);
		
		ShipmentHistory sh32 = new ShipmentHistory();
		sh32.setShipment(s3);
		sh32.setShipmentStatus(shipped);
		sh32.setLastStatusTime(LocalDateTime.of(2019, 6, 16, 9, 17));
		sh32.setAddedDate(sh32.getLastStatusTime());
		shipRepo.addShipmentHistory(sh32);
		
		ShipmentHistory sh41 = new ShipmentHistory();
		sh41.setShipment(s4);
		sh41.setShipmentStatus(received);
		sh41.setLastStatusTime(LocalDateTime.of(2019, 10, 22, 17, 25));
		sh41.setAddedDate(sh41.getLastStatusTime());
		shipRepo.addShipmentHistory(sh41);
		
		ShipmentHistory sh42 = new ShipmentHistory();
		sh42.setShipment(s4);
		sh42.setShipmentStatus(shipped);
		sh42.setLastStatusTime(LocalDateTime.of(2019, 10, 23, 17, 17));
		sh42.setAddedDate(sh42.getLastStatusTime());
		shipRepo.addShipmentHistory(sh42);
		
		ShipmentHistory sh51 = new ShipmentHistory();
		sh51.setShipment(s5);
		sh51.setShipmentStatus(received);
		sh51.setLastStatusTime(LocalDateTime.of(2019, 10, 22, 17, 25));
		sh51.setAddedDate(sh51.getLastStatusTime());
		shipRepo.addShipmentHistory(sh51);
		
		ShipmentHistory sh52 = new ShipmentHistory();
		sh52.setShipment(s5);
		sh52.setShipmentStatus(shipped);
		sh52.setLastStatusTime(LocalDateTime.of(2019, 10, 23, 17, 17));
		sh52.setAddedDate(sh52.getLastStatusTime());
		shipRepo.addShipmentHistory(sh52);
		
		ShipmentHistory sh53 = new ShipmentHistory();
		sh53.setShipment(s5);
		sh53.setShipmentStatus(delivered);
		sh53.setLastStatusTime(LocalDateTime.of(2019, 10, 30, 8, 2));
		sh53.setAddedDate(sh53.getLastStatusTime());
		shipRepo.addShipmentHistory(sh53);
		
		ShipmentHistory sh61 = new ShipmentHistory();
		sh61.setShipment(s6);
		sh61.setShipmentStatus(received);
		sh61.setLastStatusTime(LocalDateTime.of(2019, 7, 3, 5, 25));
		sh61.setAddedDate(sh61.getLastStatusTime());
		shipRepo.addShipmentHistory(sh61);
		
		ShipmentHistory sh62 = new ShipmentHistory();
		sh62.setShipment(s6);
		sh62.setShipmentStatus(cancelled);
		sh62.setLastStatusTime(LocalDateTime.of(2019, 7, 3, 6, 19));
		sh62.setAddedDate(sh62.getLastStatusTime());
		shipRepo.addShipmentHistory(sh62);
		
		System.out.println("Done adding Shipments Histories ... ");
	
	}
	
	private static void insertShipmentsPayments(String persistenceUnitName) {

		AccountsRepository accountsRepo = new JpaAccountsRepository(persistenceUnitName);
		ShipmentsRepository shipRepo = new JpaShipmentsRepository(persistenceUnitName);
		
		List<Shipment> ships = shipRepo.getShipments();
		Shipment s1 = ships.get(0);
		Shipment s2 = ships.get(1);
		Shipment s3 = ships.get(2);
		Shipment s4 = ships.get(3);
		Shipment s5 = ships.get(4);
		Shipment s6 = ships.get(5);
		
		List<Customer> customers = accountsRepo.getCustomers();
		Customer customer1 = customers.get(0);
		Customer customer2 = customers.get(1);
		Customer customer3 = customers.get(2);
		
		List<PaymentMethodType> methodTypes = shipRepo.getPaymentsMethodsTypes();
		PaymentMethodType ccType = methodTypes.get(0);
		PaymentMethodType ppType = methodTypes.get(1);
		
		System.out.println("Start adding Shipments Payments ... ");
		
		PaymentCreditCard cc1 = new PaymentCreditCard();
		cc1.setCreditCardNumber("1111-2222-3333-4444");
		cc1.setCvc(678);
		cc1.setExpiryDate(LocalDate.of(2022, 3, 4));
		cc1.setCustomer(customer1);
		cc1.setType('C');
		shipRepo.addPaymentMethod(cc1);
		
		PaymentPaypal pp1 = new PaymentPaypal();
		pp1.setAccountName("aalkhayarin");
		pp1.setCustomer(customer1);
		pp1.setType('P');
		shipRepo.addPaymentMethod(pp1);
		
		PaymentCreditCard cc2 = new PaymentCreditCard();
		cc2.setCreditCardNumber("6723-5633-1122-8900");
		cc2.setCvc(678);
		cc2.setExpiryDate(LocalDate.of(2021, 3, 14));
		cc2.setCustomer(customer2);
		cc2.setType('C');
		shipRepo.addPaymentMethod(cc2);
		
		PaymentPaypal pp2 = new PaymentPaypal();
		pp2.setAccountName("fawazk");
		pp2.setCustomer(customer2);
		pp2.setType('P');
		shipRepo.addPaymentMethod(pp2);
		
		PaymentCreditCard cc3 = new PaymentCreditCard();
		cc3.setCreditCardNumber("9000-3456-7764-1209");
		cc3.setCvc(678);
		cc3.setExpiryDate(LocalDate.of(2020, 11, 13));
		cc3.setCustomer(customer3);
		cc3.setType('C');
		shipRepo.addPaymentMethod(cc3);
		
		PaymentPaypal pp3 = new PaymentPaypal();
		pp3.setAccountName("josephl");
		pp3.setCustomer(customer3);
		pp3.setType('P');
		shipRepo.addPaymentMethod(pp3);
		
		ShipmentPayment sp1 = new ShipmentPayment();
		sp1.setShipment(s1);
		sp1.setPaymentMethod(cc1);
		sp1.setAmount(2000);
		shipRepo.addShipmentPayment(sp1);
		
		ShipmentPayment sp2 = new ShipmentPayment();
		sp2.setShipment(s2);
		sp2.setPaymentMethod(pp1);
		sp2.setAmount(300);
		shipRepo.addShipmentPayment(sp2);
		
		ShipmentPayment sp3 = new ShipmentPayment();
		sp3.setShipment(s3);
		sp3.setPaymentMethod(cc1);
		sp3.setAmount(8900);
		shipRepo.addShipmentPayment(sp3);
		
		ShipmentPayment sp4 = new ShipmentPayment();
		sp4.setShipment(s4);
		sp4.setPaymentMethod(pp2);
		sp4.setAmount(2000);
		shipRepo.addShipmentPayment(sp4);
		
		ShipmentPayment sp5 = new ShipmentPayment();
		sp5.setShipment(s5);
		sp5.setPaymentMethod(cc2);
		sp5.setAmount(1400);
		shipRepo.addShipmentPayment(sp5);
		
		ShipmentPayment sp6 = new ShipmentPayment();
		sp6.setShipment(s6);
		sp6.setPaymentMethod(pp3);
		sp6.setAmount(3200);
		shipRepo.addShipmentPayment(sp6);
		
		System.out.println("Done adding Shipments Payments ... ");
	
	}
	
	private static void insertShipmentStatusesTypes(String persistenceUnitName) {
		ShipmentsRepository shipRepo = new JpaShipmentsRepository(persistenceUnitName);
		
		ShipmentStatusType status1 = new ShipmentStatusType();
		status1.setId(1);
		status1.setDescription("Received");
		System.out.println("Adding ShipmentStatusType : " + status1.getDescription());
		shipRepo.addShipmentStatusType(status1);
		
		ShipmentStatusType status2 = new ShipmentStatusType();
		status2.setId(2);
		status2.setDescription("Shipped");
		System.out.println("Adding ShipmentStatusType : " + status2.getDescription());
		shipRepo.addShipmentStatusType(status2);
		
		ShipmentStatusType status3 = new ShipmentStatusType();
		status3.setId(3);
		status3.setDescription("Delivered");
		System.out.println("Adding ShipmentStatusType : " + status3.getDescription());
		shipRepo.addShipmentStatusType(status3);
		
		ShipmentStatusType status4 = new ShipmentStatusType();
		status4.setId(4);
		status4.setDescription("Cancelled");
		System.out.println("Adding ShipmentStatusType : " + status4.getDescription());
		shipRepo.addShipmentStatusType(status4);
	}
	
	public static void insertSuppliers(String persistenceUnitName) {
		AccountsRepository accRepo = new JpaAccountsRepository();
		
		System.out.println("Start Adding Suppliers ... ");
		
		Supplier supplier1 = new Supplier();
		supplier1.setName("Amazon");
		supplier1.setDescription(("Amazon"));
		
		Supplier supplier2 = new Supplier();
		supplier2.setName("Ebay");
		supplier2.setDescription(("Ebay"));
		
		Supplier supplier3 = new Supplier();
		supplier3.setName("Play-Asia");
		supplier3.setDescription(("Play-Asia"));
		
		
		System.out.println("Adding Supplier : " + supplier1.getDescription());
		accRepo.addSupplier(supplier1);
		System.out.println("Adding Supplier : " + supplier2.getDescription());
		accRepo.addSupplier(supplier2);
		System.out.println("Adding Supplier : " + supplier3.getDescription());
		accRepo.addSupplier(supplier3);
		
		System.out.println("Done Adding Suppliers ...");
		
	}
	public static void insertCountries(String persistenceUnitName) {
		LocationsRepository locRepo = new JpaLocationsRepository(persistenceUnitName);
		
		System.out.println("Start Adding Countries ... ");
		
		Country country1 = new Country();
		country1.setCountryName("Qatar");
		country1.setCode("QA");
		country1.setCities(new ArrayList<City>());
		
		City city11 = new City();
		city11.setCityName("Rayyan");
		city11.setCode("QA_RYN");
		country1.getCities().add(city11);
		
		City city12 = new City();
		city12.setCityName("Wakrah");
		city12.setCode("QA_WKR");
		country1.getCities().add(city12);
		
		
		City city13 = new City();
		city13.setCityName("Dafnah");
		city13.setCode("QA_DFN");
		country1.getCities().add(city13);
		
		
		Country country2 = new Country();
		country2.setCountryName("Japan");
		country2.setCode("JPN");
		country2.setCities(new ArrayList<City>());
		
		City city21 = new City();
		city21.setCityName("Tokyo");
		city21.setCode("JPN_TKY");
		country2.getCities().add(city21);
		
		City city22 = new City();
		city22.setCityName("Kyoto");
		city22.setCode("JPN_KTO");
		country2.getCities().add(city22);
		
		
		City city23 = new City();
		city23.setCityName("Akihabara");
		city23.setCode("JPN_AKHBR");
		country2.getCities().add(city23);
		
		Country country3 = new Country();
		country3.setCountryName("USA");
		country3.setCode("US");
		country3.setCities(new ArrayList<City>());
		
		City city31 = new City();
		city31.setCityName("New York");
		city31.setCode("US_NY");
		country3.getCities().add(city31);
		
		City city32 = new City();
		city32.setCityName("Los Angeles");
		city32.setCode("US_LA");
		country3.getCities().add(city32);
		
		
		City city33 = new City();
		city33.setCityName("Chicago");
		city33.setCode("US_CHKG");
		country3.getCities().add(city33);
		
		locRepo.addCountry(country1);
		locRepo.addCountry(country2);
		locRepo.addCountry(country3);
		
		System.out.println("Done Adding Countries : ");
	}
	
	private static void insertPaymentsMethodsTypes(String persistenceUnitName) {
		ShipmentsRepository shipRepo = new JpaShipmentsRepository(persistenceUnitName);
		PaymentMethodType methodType1 = new PaymentMethodType();
		methodType1.setId(1);
		methodType1.setDescription("Credit Card");
		
		PaymentMethodType methodType2 = new PaymentMethodType();
		methodType2.setId(2);
		methodType2.setDescription("Paypal");
		
		System.out.println("Start adding Payments Methods Types ... ");
		
		shipRepo.addPaymentMethodType(methodType1);
		shipRepo.addPaymentMethodType(methodType2);
		
		System.out.println("Done adding Payments Methods Types ... ");
		
	}
}
