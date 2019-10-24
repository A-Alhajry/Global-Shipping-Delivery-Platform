package qu.master.adbs.gsdp.entity;

import java.time.LocalDate;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import qu.master.adbs.gsdp.adapters.LocalDateAdapter;

@Entity
@DiscriminatorValue("C")
public class PaymentCreditCard extends PaymentMethod {
	
	private String name;
	private String creditCardNumber;
	private int cvc;
	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	private LocalDate expiryDate;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCreditCardNumber() {
		return creditCardNumber;
	}
	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}
	public int getCvc() {
		return cvc;
	}
	public void setCvc(int cvc) {
		this.cvc = cvc;
	}
	public LocalDate getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}
	
	
}