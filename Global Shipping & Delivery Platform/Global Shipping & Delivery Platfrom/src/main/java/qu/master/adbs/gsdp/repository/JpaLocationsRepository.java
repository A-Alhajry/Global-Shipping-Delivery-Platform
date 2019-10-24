package qu.master.adbs.gsdp.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import qu.master.adbs.gsdp.entity.Address;
import qu.master.adbs.gsdp.entity.City;
import qu.master.adbs.gsdp.entity.Country;

public class JpaLocationsRepository implements LocationsRepository {
	
	EntityManagerFactory emf;
	EntityManager em;
	
	public JpaLocationsRepository() {
		this("GSDP_MSSQL");
	}
	
	public JpaLocationsRepository(String persistenceUnitName) {
		emf = Persistence.createEntityManagerFactory(persistenceUnitName);
		em = emf.createEntityManager();
	}
	
	public int addCountry(Country country) {
		em.getTransaction().begin();
		em.persist(country);
		em.getTransaction().commit();
		return country.getId();
	}
	
	public int addCity(City city) {
		em.getTransaction().begin();
		em.persist(city);
		em.getTransaction().commit();
		return city.getId();
	}
	
	public int addAddress(Address address) {
		em.getTransaction().begin();
		em.persist(address);
		em.getTransaction().commit();
		return address.getId();
	}
	
	public List<Country> getCountries() {
		return em.createQuery("select c from Country c").getResultList();
	}
	
	public List<Address> getCustomerAddresses(int customerId) {
		Query query = em.createQuery("select a from Address a where a.customer.id = :customerId");
		query.setParameter("customerId", customerId);
		return query.getResultList();
	}
	
	
	
}
