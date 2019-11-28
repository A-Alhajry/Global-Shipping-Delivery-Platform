package qu.master.adbs.gsdp.repository;

import qu.master.adbs.gsdp.entity.PaymentMethodType;
import qu.master.adbs.gsdp.entity.ShipmentStatusType;

public class MongoLookup {
	
	private int id;
	private String description;
	private String lookupName;
	
	public MongoLookup(int id, String description, String lookupName) {
		this.id = id;
		this.description = description;
		this.lookupName = lookupName;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLookupName() {
		return lookupName;
	}
	public void setLookupName(String lookupName) {
		this.lookupName = lookupName;
	}
	
	public ShipmentStatusType toShipmentStatus() {
		ShipmentStatusType shipmentStatus = new ShipmentStatusType();
		shipmentStatus.setId(this.id);
		shipmentStatus.setDescription(this.description);
		return shipmentStatus;
	}
	
	public PaymentMethodType toPaymentMethod() {
		PaymentMethodType methodType = new PaymentMethodType();
		methodType.setId(this.id);
		methodType.setDescription(this.description);
		return methodType;
	}
	
	
}
