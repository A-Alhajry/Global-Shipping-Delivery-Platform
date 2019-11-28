package qu.master.adbs.gsdp.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import org.bson.Document;

import qu.master.adbs.gsdp.entity.Address;
import qu.master.adbs.gsdp.entity.City;
import qu.master.adbs.gsdp.entity.Country;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

@ApplicationScoped
@RepositoryModeType(RepositoryMode.MONGO)
public class MongoLocationsRepository implements LocationsRepository {
	
	private final static String collection = "locations";
	private final static String countryDocument = "country_document";
	private final static String citiesDocument = "city_document";
	private final static String accountsDocument = "accounts_document";

	@Override
	public int addCountry(Country country) {
		var mongoCollection = MongoManager.getCollection(collection);
		int newId = MongoManager.getNewId(countryDocument);
		country.setId(newId);
		if (country.getCities() != null) {
			for(var city : country.getCities()) {
				city.setId(MongoManager.getNewId(citiesDocument));
			}
		}
		mongoCollection.insertOne(this.getCountryDocument(country));
		return newId;
	}

	@Override
	public int addCity(City city) {
		if (city.getCountry() == null) {
			throw new IllegalArgumentException("Cannot add city without an associated country ");
		}
		var mongoCollection = MongoManager.getCollection(collection);
		var countryFilter = eq("_id", city.getCountry().getId());
		var countryDocument = mongoCollection.find(countryFilter).first();
		
		if (countryDocument == null) {
			throw new IllegalArgumentException("Cannot add city without an associated country ");
		}
		
		city.setId(MongoManager.getNewId(citiesDocument));
		List<Document> citiesDocuments = (List<Document>) countryDocument.get("cities");
		
		if (citiesDocuments == null) {
			citiesDocuments = new ArrayList<>();
		}
		
		citiesDocuments.add(this.getCityDocument(city, false));
		mongoCollection.updateOne(countryFilter, new Document("$set", new Document("cities", citiesDocuments)));
		
		return city.getId();
	}

	@Override
	public int addAddress(Address address) {
		var mongoCollection = MongoManager.getCollection(accountsDocument);
		if (address.getCustomer() == null) {
			throw new IllegalArgumentException("Cannot add address to non-existent customer ");
		}
		var customerFilter = eq("_id", address.getCustomer().getId());
		var customerDocument = mongoCollection.find(customerFilter).first();
		
		if (customerDocument == null) {
			throw new IllegalArgumentException("Cannot add address to non-existent customer ");
		}
		
		List<Document> addressesDocuments = (List<Document>) customerDocument.get("addresses");
		
		if (addressesDocuments == null) {
			addressesDocuments = new ArrayList<>();
		}
		
		addressesDocuments.add(this.getAddressDocument(address));
		mongoCollection.updateOne(customerFilter, new Document("$set", new Document("addresses", addressesDocuments)));
		
		return address.getId();
	}

	@Override
	public List<Country> getCountries() {
		var mongoCollection = MongoManager.getCollection(collection);
		List<Country> countries = new ArrayList<>();
		try(var mongoCursor = mongoCollection.find().iterator()) {
			while(mongoCursor.hasNext()) {
				countries.add(this.getCountryEntity(mongoCursor.next()));
			}
		}
		
		return countries;
		
	}

	@Override
	public List<Address> getCustomerAddresses(int customerId) {
		List<Address> addresses = new ArrayList<>();
		var mongoCollection = MongoManager.getCollection(accountsDocument);
		var customerDocument = mongoCollection.find(eq("_id", customerId)).first();
		if (customerDocument != null) {
			List<Document> addressesDocuments = (List<Document>) customerDocument.get("addresses");
			if (addressesDocuments == null) {
				addresses = addressesDocuments.stream().map(d -> this.getAddressEntity(d)).collect(Collectors.toList());
			}
		}
		
		return addresses;
	}
	
	private Document getCountryDocument(Country country) {
		List<Document> citiesDocument = new ArrayList<>();
		if (country.getCities() != null) {
			citiesDocument = country.getCities().stream().map(c -> this.getCityDocument(c, false)).collect(Collectors.toList());
		}
		
		Document countryDocument = new Document();
		countryDocument.append("_id", country.getId());
		countryDocument.append("code", country.getCode());
		countryDocument.append("countryname", country.getCountryName());
		countryDocument.append("cities", citiesDocument);
		return countryDocument;
	}
	
	private Country getCountryEntity(Document document) {
		List<City> cities = new ArrayList<>();
		List<Document> citiesDocuments = (List<Document>) document.get("cities");
		if (citiesDocuments != null) {
			cities = citiesDocuments.stream().map(d -> this.getCityEntity(d, false)).collect(Collectors.toList());
		}
		
		Country country = new Country();
		country.setId(document.getInteger("_id"));
		country.setCode(document.getString("code"));
		country.setCountryName(document.getString("countryname"));
		country.setCities(cities);
		return country;
	}
	
	private Document getCityDocument(City city, boolean addCountry) {
		Document document = new Document();
		document.append("_id", city.getId());
		document.append("code", city.getCode());
		document.append("cityname", city.getCityName());
		if (addCountry) {
			document.append("country", this.getCountryDocument(city.getCountry()));
		}
	    return document;
	}
	
	private City getCityEntity(Document document, boolean loadCountry) {
		City city = new City();
		city.setId(document.getInteger("_id"));
		city.setCityName(document.getString("cityname"));
		city.setCode(document.getString("code"));
		
		if (loadCountry) {
			city.setCountry(this.getCountryEntity((Document)document.get("country")));
		}
		return city;
	}
	
	private Document getAddressDocument(Address address) {
		Document document = new Document();
		document.append("_id", address.getId());
		document.append("street", address.getStreet());
		document.append("zone", address.getZone());
		document.append("city", this.getCityDocument(address.getCity(), true));
		return document;
	}
	
	private Address getAddressEntity(Document document) {
		Address address = new Address();
		address.setId(document.getInteger("_id"));
		address.setStreet(document.getString("street"));
		address.setZone(document.getString("zone"));
		address.setCity(this.getCityEntity((Document) document.get("city"), true));
		return address;
	}
}