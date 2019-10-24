package qu.master.adbs.gsdp.repository;

import java.util.List;

import qu.master.adbs.gsdp.entity.Address;
import qu.master.adbs.gsdp.entity.City;
import qu.master.adbs.gsdp.entity.Country;

public interface LocationsRepository {
	
	public abstract int addCountry(Country country);
	
	public abstract int addCity(City city);
	
	public abstract int addAddress(Address address);
	
	public abstract List<Country> getCountries();
	
	public abstract List<Address> getCustomerAddresses(int customerId);
}
