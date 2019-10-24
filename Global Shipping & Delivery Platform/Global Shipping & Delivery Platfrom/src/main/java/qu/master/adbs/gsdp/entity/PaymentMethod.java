package qu.master.adbs.gsdp.entity;

import java.time.LocalDateTime;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.CHAR)
@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = "type"
)
@JsonSubTypes({
	@Type(name = "C", value = PaymentCreditCard.class),
	@Type(name = "P", value = PaymentPaypal.class)
})
public abstract class PaymentMethod {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	@JsonIgnore
	private Customer customer;
	private PaymentMethodType paymentMethodType;
	private char type;
	@JsonIgnore
	private int addedById;
	@JsonIgnore
	private LocalDateTime addedDate;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public PaymentMethodType getPaymentMethodType() {
		return paymentMethodType;
	}
	public void setPaymentMethodType(PaymentMethodType paymentMethodType) {
		this.paymentMethodType = paymentMethodType;
	}
	public char getType() {
		return type;
	}
	public void setType(char type) {
		this.type = type;
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
	
	@PrePersist
	public void prePersistEvent() {
		this.addedDate = LocalDateTime.now();
		this.addedById = 1;
	}
	
}
