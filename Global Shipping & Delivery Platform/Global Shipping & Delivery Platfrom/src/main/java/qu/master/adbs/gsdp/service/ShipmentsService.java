package qu.master.adbs.gsdp.service;

import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import qu.master.adbs.gsdp.entity.Customer;
import qu.master.adbs.gsdp.entity.Shipment;
import qu.master.adbs.gsdp.entity.ShipmentHistory;
import qu.master.adbs.gsdp.entity.ShipmentPayment;
import qu.master.adbs.gsdp.entity.ShipmentStatusEnum;
import qu.master.adbs.gsdp.entity.ShipmentStatusType;
import qu.master.adbs.gsdp.repository.AccountsRepository;
import qu.master.adbs.gsdp.repository.ShipmentsRepository;

@Path("/shipments")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
public class ShipmentsService extends AbstractService {
	
	@Inject
	private ShipmentsRepository shipmentsRepository;
	
	//@Inject
	private AccountsRepository accountsRepository;
	
	@POST
	public ServiceResult addShipment(Shipment shipment) {
		try {
			ShipmentStatusType statusType = shipmentsRepository.getShipmentStatusType(ShipmentStatusEnum.RECEVIED.getId());
			Customer customer = accountsRepository.getCustomer(shipment.getCustomer().getId());
			shipment.setCustomer(customer);
			shipment.setAddedDate(LocalDateTime.now());
			shipment.setAddedById(customer.getId());
			shipment.setHistory(new ArrayList<>());
			shipment.getHistory().add(new ShipmentHistory());
			shipment.getHistory().get(0).setLastStatusTime(LocalDateTime.now());
			shipment.getHistory().get(0).setShipmentStatus(statusType);
			
			int id = shipmentsRepository.addShipment(shipment);

			return ok("Shipment with id [" + id + "] was added !");

	    }
		
		catch (Exception e) {
			e.printStackTrace();
			return error(e.getMessage());
		}
	}
	
	
	@GET
	@Path("{shipmentId}/history")
	public ServiceResult getShipmentHistory(@PathParam("shipmentId") int shipmentId, @QueryParam("lastStatusOnly") boolean lastStatusOnly) {
		try {
			
			Shipment targetShipment = shipmentsRepository.getShipment(shipmentId);
			
			if (targetShipment == null) {
				return error("Shipment with id : " + shipmentId + " not found !");						
			}
			
			else {
				if (lastStatusOnly) {
					return ok(shipmentsRepository.getShipmentLastStatus(shipmentId));
				}
				
				else {
					return ok(shipmentsRepository.getShipmentHistory(shipmentId));
				}
			}
						
		}
		
		catch (Exception e) {
			return error(e.getMessage());
		}
	}
	@POST
	@Path("{shipmentId}/history")
	public ServiceResult addShipmentStatus(@PathParam("shipmentId") int shipmentId, ShipmentHistory newStatus) {
		try {
			Shipment targetShipment = shipmentsRepository.getShipment(shipmentId);
			if (targetShipment == null) {
				return error("Shipment with id : " + shipmentId + " not found !");
			}
			
			else {
				newStatus.setShipment(targetShipment);
				ShipmentStatusType lastStatusType = shipmentsRepository.getShipmentStatusType(newStatus.getShipmentStatus().getId());
				newStatus.setShipmentStatus(lastStatusType);
				newStatus.setLastStatusTime(LocalDateTime.now());
				shipmentsRepository.addShipmentHistory(newStatus);
				return ok("Shipment Updated to '" + lastStatusType.getDescription() + "'");
			}
		}
		
		catch (Exception e) {
			return error(e.getMessage());
		}
	}
	
	@GET
	@Path("/customers/{customerId}")
	public ServiceResult getCustomerShipments(@PathParam("customerId") int customerId) {
		try {
			
			return ok(shipmentsRepository.getCustomerShipments(customerId));
			
		}
		
		catch (Exception e) {
			return error(e.getMessage());
		}
		
	}
	
	@POST
	@Path("{shipmentId}/payments")
	public ServiceResult addShipmentPayment(@PathParam("shipmentId") int shipmentId, ShipmentPayment payment) {
		try {
			Shipment targetShipment = shipmentsRepository.getShipment(shipmentId);
			ShipmentPayment oldPayment = shipmentsRepository.getShipmentPayment(shipmentId);
			if (targetShipment == null) {
				return error("Shipment with Id : " + shipmentId + " was not found ");
			}
			
			else if (oldPayment != null) {
				return error("Shipment with id :" + shipmentId + " was already payed ");
			}
			
			else {
				payment.setShipment(targetShipment);
				int newPaymentId = shipmentsRepository.addShipmentPayment(payment);
				return ok("Payment with id [" + newPaymentId + "] was added ");
			}
		}
		
		catch (Exception e) {
			return error(e.getMessage());
		}
	}
	
	@GET
	@Path("{shipmentId}/payments")
	public ServiceResult getShipmentPayment(@PathParam("shipmentId") int shipmentId) {
		try {
			return ok(shipmentsRepository.getShipmentPayment(shipmentId));
		}
		
		catch (Exception e) {
			return error(e.getMessage());
		}
	}
	
	@GET
	@Path("/payments")
	public ServiceResult getAllShipmentsPayments() {
		try {
			return ok(shipmentsRepository.getShipmentsPayments());
		}
		
		catch (Exception e) {
			return error(e.getMessage());
		}
	}
	
	@GET
	//@Path("/")
	public ServiceResult searchShipments(@QueryParam("customerId") int customerId, @QueryParam("supplierId") int supplierId, @QueryParam("statusId") int statusId) {
		try {
			return ok(shipmentsRepository.searchShipments(customerId, supplierId, statusId));
		}
		
		catch (Exception e) {
			return error(e.getMessage());
		}
	}
}
