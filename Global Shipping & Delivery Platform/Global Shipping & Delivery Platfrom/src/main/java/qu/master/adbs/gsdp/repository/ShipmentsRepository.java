package qu.master.adbs.gsdp.repository;

import java.util.List;

import qu.master.adbs.gsdp.entity.PaymentMethod;
import qu.master.adbs.gsdp.entity.PaymentMethodType;
import qu.master.adbs.gsdp.entity.Shipment;
import qu.master.adbs.gsdp.entity.ShipmentHistory;
import qu.master.adbs.gsdp.entity.ShipmentPayment;
import qu.master.adbs.gsdp.entity.ShipmentStatusType;

public interface ShipmentsRepository {
	
	public abstract int addShipment(Shipment shipment);
	
	public abstract int addShipmentHistory(ShipmentHistory history);
	
	public abstract int addShipmentStatusType(ShipmentStatusType statusType);
	
	public List<ShipmentStatusType> getShipmentStatusTypes();
	
	public ShipmentStatusType getShipmentStatusType(int id);
	
	public abstract Shipment getShipment(int shipmentId);
	
	public abstract List<Shipment> getShipments();
	
	public abstract List<Shipment> getCustomerShipments(int customerId);
	
	public abstract ShipmentHistory getShipmentLastStatus(int shipmentId);
	
	public abstract List<ShipmentHistory> getShipmentHistory(int shipmentId);
	
	public abstract int addShipmentPayment(ShipmentPayment payment);	
	
	public abstract int addPaymentMethod(PaymentMethod paymentMethod);
	
	public abstract List<PaymentMethod> getCustomerPaymentMethods(int customerId);
	
	public abstract int addPaymentMethodType(PaymentMethodType methodType);
	
	public abstract List<PaymentMethodType> getPaymentsMethodsTypes();
	
	public abstract PaymentMethodType getPaymentMethodType(int id);
	
	public abstract ShipmentPayment getShipmentPayment(int shipmentId);
	
	public abstract List<ShipmentPayment> getShipmentsPayments();
	
	public abstract List<Shipment> searchShipments(int customerId, int supplierId, int statusId);
}