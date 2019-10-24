package qu.master.adbs.gsdp.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Address {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	private City city;
	private String zone;
	private String street;
	@JsonIgnore
	private int addedById;
	@JsonIgnore
	private LocalDateTime addedDate;
	@ManyToOne
	@JsonManagedReference
	@JsonIgnore
	private Customer customer;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public City getCity() {
		return city;
	}
	public void setCity(City city) {
		this.city = city;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public int getAddedById() {
		return addedById;
	}
	public void setAddedById(int addedById) {
		this.addedById = addedById;
	}
	public LocalDateTime getAddedDate() {
		return addedDate;
	}
	public void setAddedDate(LocalDateTime addedDate) {
		this.addedDate = addedDate;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	@PrePersist
	public void prePersistEvent() {
		this.addedDate = LocalDateTime.now();
		this.addedById = 1;
	}
	
}