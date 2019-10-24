package qu.master.adbs.gsdp.repository;

import java.util.List;

import qu.master.adbs.gsdp.entity.Customer;
import qu.master.adbs.gsdp.entity.Employee;
import qu.master.adbs.gsdp.entity.Supplier;
import qu.master.adbs.gsdp.entity.UserCredentials;

public interface AccountsRepository {
	
	public abstract boolean authenticate(UserCredentials userCredentials);
	
	public abstract int addCustomer(Customer customer);
	
	public abstract Customer getCustomer(int id);
	
	public abstract List<Customer> getCustomers();
	
	public abstract int addEmployee(Employee employee);
	
	public abstract int addSupplier(Supplier supplier);
	
	public abstract List<Supplier> getSuppliers();
}
