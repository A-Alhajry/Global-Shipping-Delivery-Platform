package qu.master.adbs.gsdp.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import qu.master.adbs.gsdp.entity.PaymentMethod;
import qu.master.adbs.gsdp.entity.PaymentMethodType;
import qu.master.adbs.gsdp.entity.Shipment;
import qu.master.adbs.gsdp.entity.ShipmentHistory;
import qu.master.adbs.gsdp.entity.ShipmentPayment;
import qu.master.adbs.gsdp.entity.ShipmentStatusType;

public class JpaShipmentsRepository implements ShipmentsRepository {
	
	private EntityManagerFactory emf;
	private EntityManager em;
	
	
	public JpaShipmentsRepository() {
		this("GSDP_MSSQL");
	}
	
	public JpaShipmentsRepository(String persistenceUnitName) {
		emf = Persistence.createEntityManagerFactory(persistenceUnitName);
		em = emf.createEntityManager();
	}
	
	
	public int addShipment(Shipment shipment) {
		em.getTransaction().begin();
		em.persist(shipment);
		em.getTransaction().commit();
		return shipment.getId();
	}
	
	public int addShipmentHistory(ShipmentHistory history) {
		em.getTransaction().begin();
		em.persist(history);
		em.getTransaction().commit();
		return history.getId();
	}
	
	public int addShipmentStatusType(ShipmentStatusType statusType) {
		em.getTransaction().begin();
		em.persist(statusType);
		em.getTransaction().commit();
		return statusType.getId();
	}
	
	public List<ShipmentStatusType> getShipmentStatusTypes() {
		return em.createQuery("select ss from ShipmentStatusType ss").getResultList();
	}
	
	public ShipmentStatusType getShipmentStatusType(int id) {
		Query query = em.createQuery("select ss from ShipmentStatusType ss where ss.id = :id");
		query.setParameter("id", id);
		return (ShipmentStatusType) query.getSingleResult();
	}
	
	public Shipment getShipment(int shipmentId) {
		return em.find(Shipment.class, shipmentId);
	}
	
	public List<Shipment> getShipments() {
		return em.createQuery("select s from Shipment s").getResultList();
	}
	
	public List<Shipment> getCustomerShipments(int customerId) {
		Query query = em.createQuery("select s from Shipment s where s.customer.id = :customerId");
		query.setParameter("customerId", customerId);
		return query.getResultList();
	}
	
	public ShipmentHistory getShipmentLastStatus(int shipmentId) {
		Query query = em.createQuery("select h from ShipmentHistory h where h.shipment.id = :shipmentId order by h.lastStatusTime Desc ");
		query.setParameter("shipmentId", shipmentId);
		List<ShipmentHistory> result = query.getResultList();
		return result == null ? null : result.get(0);
	}
	
	public List<ShipmentHistory> getShipmentHistory(int shipmentId) {
		Query query = em.createQuery("select h from ShipmentHistory h where h.shipment.id = :shipmentId order by h.lastStatusTime Desc ");
		query.setParameter("shipmentId", shipmentId);
		return query.getResultList();
	}
	
	public int addShipmentPayment(ShipmentPayment payment) {
		em.getTransaction().begin();
		em.persist(payment);
		em.getTransaction().commit();
		return payment.getId();
	}
	
	public int addPaymentMethod(PaymentMethod paymentMethod) {
		em.getTransaction().begin();
		em.persist(paymentMethod);
		em.getTransaction().commit();
		return paymentMethod.getId();
	}
	
	public List<PaymentMethod> getCustomerPaymentMethods(int customerId) {
		Query query = em.createQuery("select p from PaymentMethod p where p.customer.id = :customerId");
		query.setParameter("customerId", customerId);
		return query.getResultList();
	}
	
	public int addPaymentMethodType(PaymentMethodType methodType) {
		em.getTransaction().begin();
		em.persist(methodType);
		em.getTransaction().commit();
		return methodType.getId();
	}
	
	public List<PaymentMethodType> getPaymentsMethodsTypes() {
		return em.createQuery("select p from PaymentMethodType p").getResultList();
	}
	
	public PaymentMethodType getPaymentMethodType(int id) {
		Query query = em.createQuery("select p from PaymentMethodType p where p.id = :id");
		query.setParameter("id", id);
		return (PaymentMethodType) query.getSingleResult();
	}
	
	public ShipmentPayment getShipmentPayment(int shipmentId) {
		Query query = em.createQuery("select p from ShipmentPayment p where p.shipment.id = :shipmentId");
		query.setParameter("shipmentId", shipmentId);
		List<ShipmentPayment> payments = query.getResultList();
		return payments == null || payments.isEmpty() ? null : payments.get(0);
	}
	
	public List<ShipmentPayment> getShipmentsPayments() {
		return em.createQuery("select p from ShipmentPayment p").getResultList();
	}
	
	public List<Shipment> searchShipments(int customerId, int supplierId, int statusId) {
		StringBuilder queryText = new StringBuilder("select distinct(h.shipment.id) from ShipmentHistory h where h.id > 0  "); 
		if (customerId > 0) {
			queryText.append(" and h.shipment.customer.id = :customerId");			
		}
		
		if (supplierId > 0) {
			queryText.append(" and h.shipment.supplier.id = :supplierId");
		}
		
		if (statusId > 0) {
			queryText.append(" and h.shipmentStatus.id = :lastStatusId");
		}
		
		Query query = em.createQuery(queryText.toString());
		
		if (customerId > 0) {
			query.setParameter("customerId", customerId);
		}
		
		if (supplierId > 0) {
			query.setParameter("supplierId", supplierId);
		}
		
		if (statusId > 0) {
			query.setParameter("lastStatusId", statusId);
		}
		
		List<Integer> shipmentIds = query.getResultList();
		
		if (shipmentIds != null && !shipmentIds.isEmpty()) {
			query = em.createQuery("select s from Shipment s where s.id in :shipmentIds ");
			query.setParameter("shipmentIds", shipmentIds);
			
			return query.getResultList();
		}
		
		else {
			return new ArrayList<>();
		}
		
	}
}
