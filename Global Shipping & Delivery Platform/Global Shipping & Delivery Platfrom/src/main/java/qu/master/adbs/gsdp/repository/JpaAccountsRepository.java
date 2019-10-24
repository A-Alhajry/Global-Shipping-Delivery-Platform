package qu.master.adbs.gsdp.repository;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import qu.master.adbs.gsdp.entity.Customer;
import qu.master.adbs.gsdp.entity.Employee;
import qu.master.adbs.gsdp.entity.Supplier;
import qu.master.adbs.gsdp.entity.UserCredentials;

@ApplicationScoped
public class JpaAccountsRepository implements AccountsRepository {
	
	private EntityManagerFactory emf;
	private EntityManager em;
	
	public JpaAccountsRepository() {
		this("GSDP_MSSQL");
	}
	
	public JpaAccountsRepository(String persistenceUnitName) {
		emf = Persistence.createEntityManagerFactory(persistenceUnitName);
		em = emf.createEntityManager();
	}
	
	public boolean authenticate(UserCredentials userCredentials) {
		Query query = em.createQuery("select a from Account a where a.email = :email and a.password = :password");
		query.setParameter("email", userCredentials.getEmail());
		query.setParameter("password", userCredentials.getPassword());
		return !query.getResultList().isEmpty();
	}
	
	public int addCustomer(Customer customer) {
		em.getTransaction().begin();
		em.persist(customer);
		em.getTransaction().commit();
		return customer.getId();
	}
	
	public Customer getCustomer(int id) {
		Customer customer = em.find(Customer.class, id);
		return customer;
	}
	
	public List<Customer> getCustomers() {
		List<Customer> customers = em.createQuery("select c from Customer as c").getResultList();
		return customers;
	}
	
	public int addEmployee(Employee employee) {
		em.getTransaction().begin();
		em.persist(employee);
		em.getTransaction().commit();
		return employee.getId();
	}
	
	public int addSupplier(Supplier supplier) {
		em.getTransaction().begin();
		em.persist(supplier);
		em.getTransaction().commit();
		return supplier.getId();
	}
	
	public List<Supplier> getSuppliers() {
		return em.createQuery("select s from Supplier s ").getResultList();
	}
}
