package qu.master.adbs.gsdp.service;

import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import qu.master.adbs.gsdp.entity.Customer;
import qu.master.adbs.gsdp.entity.UserCredentials;
import qu.master.adbs.gsdp.repository.AccountsRepository;

@Path("/accounts")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
public class AccountsService extends AbstractService {
	
	@Inject
	private AccountsRepository accountsRepository;
	
	private String test = "";
	
	@POST
	@Path("/authenticate")
	public ServiceResult authenticate(UserCredentials userCredentials) {
		try {
			boolean userExists = accountsRepository.authenticate(userCredentials);
			
			if (userExists) {
				return ok(UUID.randomUUID());
			}
			
			else {
				return error("Invalid Email and/or Password ");
			}
		}
		
		catch (Exception e) {
			return error(e.getMessage());
		}
	}
	@GET
	@Path("/customers")
	public ServiceResult getCustomers() {
		try {
			List<Customer> customers = accountsRepository.getCustomers();
			return ok(customers);
		}
		
		catch (Exception e) {
			return error(e.getMessage());
		}
	}
	
	@POST
	@Path("/customers")
	public ServiceResult addCustomer(Customer customer) {
		try {
			int id = accountsRepository.addCustomer(customer);
			return ok("Customer with id [" + id + "] was added !");
		}
		
		catch (Exception e) {
			return error(e.getMessage());
		}
	}
	
	@GET
	@Path("/suppliers")
	public ServiceResult getSuppliers() {
		try {
			return ok(accountsRepository.getSuppliers());
		}
		
		catch (Exception e) {
			return error(e.getMessage());
		}
	}
}
