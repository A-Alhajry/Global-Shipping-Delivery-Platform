package qu.master.adbs.gsdp.repository;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.bson.Document;

import qu.master.adbs.gsdp.entity.Address;
import qu.master.adbs.gsdp.entity.City;
import qu.master.adbs.gsdp.entity.Country;

@ApplicationScoped
@RepositoryModeType(RepositoryMode.MONGO)
public class MongoLocationsRepository implements LocationsRepository {
	
	private final static String collection = "locations";
	private final static String countryDocument = "country_document";

	@Override
	public int addCountry(Country country) {
		var mongoCollection = MongoManager.getCollection(collection);
		int newId = MongoManager.getNewId(countryDocument);
		country.setId(newId);
		mongoCollection.insertOne(this.getCountryDocument(country));
		return newId;
	}

	@Override
	public int addCity(City city) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int addAddress(Address address) {
		// TODO Auto-generated method stub
		return 0;
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
		// TODO Auto-generated method stub
		return null;
	}
	
	private Document getCountryDocument(Country country) {
		Document document = new Document();
		document.append("_id", country.getId());
		document.append("code", country.getCode());
		document.append("countryname", country.getCountryName());
		return document;
	}
	
	private Country getCountryEntity(Document document) {
		Country country = new Country();
		country.setId(document.getInteger("_id"));
		country.setCode(document.getString("code"));
		country.setCountryName(document.getString("countryname"));
		return country;
	}
}