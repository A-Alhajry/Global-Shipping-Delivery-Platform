package qu.master.adbs.gsdp.entity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Shipment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	//@JsonIgnore
	private Supplier supplier;
	@ManyToOne
	//@JsonIgnore
	private Customer customer;
	private String description;
	private int numberOfItems;
	private double weight;	
	@JsonIgnore
	private int addedById;
	@JsonIgnore
	private LocalDateTime addedDate;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "SHIPMENT_ID")
	@JsonIgnore
	private List<ShipmentPayment> payments;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "SHIPMENT_ID")
	@JsonIgnore
	private List<ShipmentHistory> history;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Supplier getSupplier() {
		return supplier;
	}
	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getNumberOfItems() {
		return numberOfItems;
	}
	public void setNumberOfItems(int numberOfItems) {
		this.numberOfItems = numberOfItems;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
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
	public List<ShipmentPayment> getPayments() {
		return payments;
	}
	public void setPayments(List<ShipmentPayment> payments) {
		this.payments = payments;
	}
	public List<ShipmentHistory> getHistory() {
		return history;
	}
	public void setHistory(List<ShipmentHistory> history) {
		this.history = history;
	}
	public ShipmentHistory getLastStatus() {
		if (this.history == null || this.history.isEmpty()) {
			return null;
		}
		
		else {
			Collections.sort(this.history);
			return this.history.get(0);
		}
		
	}
	
	public void setCustomerId(int customerId) {
		if (this.customer == null) {
			this.customer = new Customer();
		}
		
		this.customer.setId(customerId);
	}
	
	public void setSupplierId(int supplierId) {
		if (this.supplier == null) {
			this.supplier = new Supplier();
		}
		
		this.supplier.setId(supplierId);
	}
	
	//Committed out in order to test reports & search
	//@PrePersist
	public void prePersistEvent() {
		this.addedDate = LocalDateTime.now();
		this.addedById = 1;
	}
}
