package qu.master.adbs.gsdp.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonIgnore;

import qu.master.adbs.gsdp.adapters.LocalDateTimeAdapter;

@Entity
public class ShipmentHistory implements Comparable<ShipmentHistory>{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	@JsonIgnore
	private Shipment shipment;
	private City city;
	private ShipmentStatusType shipmentStatus;
	@JsonIgnore
	private int addedById;
	@JsonIgnore
	private LocalDateTime addedDate;
	@XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
	private LocalDateTime lastStatusTime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Shipment getShipment() {
		return shipment;
	}
	public void setShipment(Shipment shipment) {
		this.shipment = shipment;
	}
	public City getCity() {
		return city;
	}
	public void setCity(City city) {
		this.city = city;
	}
	public ShipmentStatusType getShipmentStatus() {
		return shipmentStatus;
	}
	public void setShipmentStatus(ShipmentStatusType shipmentStatus) {
		this.shipmentStatus = shipmentStatus;
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
	
	public LocalDateTime getLastStatusTime() {
		return lastStatusTime;
	}
	public void setLastStatusTime(LocalDateTime lastStatusTime) {
		this.lastStatusTime = lastStatusTime;
	}
	public int compareTo(ShipmentHistory history) {
		return this.lastStatusTime.isAfter(history.getLastStatusTime()) ? -1 : 1;
	}
	
	@PrePersist
	public void prePersistEvent() {
		this.addedDate = LocalDateTime.now();
		this.addedById = 1;
	}
	
	
}
