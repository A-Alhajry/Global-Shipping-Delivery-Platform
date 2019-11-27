package qu.master.adbs.gsdp.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import qu.master.adbs.gsdp.entity.Address;
import qu.master.adbs.gsdp.entity.Customer;
import qu.master.adbs.gsdp.repository.AccountsRepository;
import qu.master.adbs.gsdp.repository.LocationsRepository;
import qu.master.adbs.gsdp.repository.RepositoryMode;
import qu.master.adbs.gsdp.repository.RepositoryModeType;

@Path("/locations")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
public class LocationsService extends AbstractService {
	
	@Inject
	@RepositoryModeType(RepositoryMode.MONGO)
	LocationsRepository locationsRepository;
	
	@Inject
	@RepositoryModeType(RepositoryMode.MONGO)
	AccountsRepository accountsRepository;
	
	@GET
	@Path("/countries")
	public ServiceResult getCountries() {
		try {
			return ok(locationsRepository.getCountries());
		}
		
		catch (Exception e) {
			return error(e.getMessage());
		}
	}
	
	@GET
	@Path("/addresses/{customerId}")
	public ServiceResult getCustomerAddresses(@PathParam("customerId") int customerId) {
		try {
			return ok(locationsRepository.getCustomerAddresses(customerId));
		}
		
		catch (Exception e) {
			return error(e.getMessage());
		}
	}
	
	@POST
	@Path("/addresses/{customerId}")
	public ServiceResult addAddress(@PathParam("customerId") int customerId, Address address) {
		try {
			Customer targetCustomer = accountsRepository.getCustomer(customerId);
			
			if (targetCustomer == null) {
				return error("Customer with Id : " + customerId + " not found ");
			}
			
			else {
				address.setCustomer(targetCustomer);
				locationsRepository.addAddress(address);
				return ok("Address with Id [" + customerId + "] was added ");
			}
		}
		
		catch (Exception e) {
			return error(e.getMessage());
		}
	}
}