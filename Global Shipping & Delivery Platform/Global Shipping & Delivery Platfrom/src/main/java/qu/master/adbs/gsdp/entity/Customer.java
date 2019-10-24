package qu.master.adbs.gsdp.entity;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@DiscriminatorValue("C")
public class Customer extends Account {
	
	@OneToMany
	@JoinColumn(name = "CUSTOMER_ID")
	@JsonIgnore
	private List<Address> addresses;
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOMER_ID")
	@JsonIgnore
	private List<Shipment> shipments;
	@OneToMany
	@JoinColumn(name = "CUSTOMER_ID")
	@JsonIgnore
	private List<PaymentMethod> paymentsMethods;

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	public List<Shipment> getShipments() {
		return shipments;
	}

	public void setShipments(List<Shipment> shipments) {
		this.shipments = shipments;
	}

	public List<PaymentMethod> getPaymentsMethods() {
		return paymentsMethods;
	}

	public void setPaymentsMethods(List<PaymentMethod> paymentsMethods) {
		this.paymentsMethods = paymentsMethods;
	}
	
	
}