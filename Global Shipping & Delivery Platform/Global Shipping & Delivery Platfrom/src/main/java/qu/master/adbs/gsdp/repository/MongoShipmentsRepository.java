package qu.master.adbs.gsdp.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.naming.OperationNotSupportedException;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Projections;

import qu.master.adbs.gsdp.entity.PaymentCreditCard;
import qu.master.adbs.gsdp.entity.PaymentMethod;
import qu.master.adbs.gsdp.entity.PaymentMethodEnum;
import qu.master.adbs.gsdp.entity.PaymentMethodType;
import qu.master.adbs.gsdp.entity.PaymentPaypal;
import qu.master.adbs.gsdp.entity.Shipment;
import qu.master.adbs.gsdp.entity.ShipmentHistory;
import qu.master.adbs.gsdp.entity.ShipmentPayment;
import qu.master.adbs.gsdp.entity.ShipmentStatusEnum;
import qu.master.adbs.gsdp.entity.ShipmentStatusType;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

@ApplicationScoped
@RepositoryModeType(RepositoryMode.MONGO)
public class MongoShipmentsRepository implements ShipmentsRepository {

	private final static String shipmentsCollection = "shipments";
	private final static String paymentsCollection = "payments";
	private final static String shipmentDocument = "shipment_document";
	private final static String paymentDocument = "payment_document";
	private final static String historyDocument = "history_document";
	private final static String shipmentStatusDocument = "shipment_status_document";
	private final static String paymentMethodDocument = "payment_method_document";

	@Override
	public int addShipment(Shipment shipment) {
		var mongoCollection = MongoManager.getCollection(shipmentsCollection);
		shipment.setId(MongoManager.getNewId(shipmentDocument));
		mongoCollection.insertOne(this.getShipmentDocument(shipment));
		if (shipment.getHistory() != null && !shipment.getHistory().isEmpty()) {
			this.addShipmentHistory(shipment.getHistory().get(0));
		}
		return shipment.getId();
	}

	@Override
	public int addShipmentHistory(ShipmentHistory history) {
		var mongoCollection = MongoManager.getCollection(shipmentsCollection);
		var shipmentFilter = eq("_id", history.getShipment().getId());
		var shipmentDocument = mongoCollection.find(shipmentFilter).first();

		if (shipmentDocument == null) {
			throw new IllegalArgumentException("Cannot add history to non-existent shipment");
		}

		List<Document> historyDocuments = (List<Document>) shipmentDocument.get("history");
		if (historyDocuments == null) {
			historyDocuments = new ArrayList<>();
		}

		history.setId(MongoManager.getNewId(historyDocument));
		historyDocuments.add(this.getHistoryDocument(history));
		Collections.reverse(historyDocuments);
		mongoCollection.updateOne(shipmentFilter, new Document("$set", new Document("history", historyDocuments)));
		mongoCollection.updateOne(shipmentFilter, new Document("$set", new Document("laststatusid", history.getShipmentStatus().getId())));

		return history.getId();
	}

	@Override
	public int addShipmentStatusType(ShipmentStatusType status) {
		MongoManager.insertLookup(status.getId(), status.getDescription(), shipmentStatusDocument);
		return status.getId();
	}

	@Override
	public List<ShipmentStatusType> getShipmentStatusTypes() {
		return MongoManager.getLookups(shipmentStatusDocument).stream().map(l -> l.toShipmentStatus())
				.collect(Collectors.toList());
	}

	@Override
	public ShipmentStatusType getShipmentStatusType(int id) {
		Optional<ShipmentStatusType> optionalStatus = this.getShipmentStatusTypes().stream()
				.filter(s -> s.getId() == id).findAny();
		return optionalStatus.isPresent() ? optionalStatus.get() : null;
	}

	@Override
	public Shipment getShipment(int id) {
		var mongoCollection = MongoManager.getCollection(shipmentsCollection);
		var shipmentDocument = mongoCollection.find(eq("_id", id)).first();
		return shipmentDocument == null ? null : this.getShipmentEntity(shipmentDocument);
	}

	@Override
	public List<Shipment> getShipments() {
		var mongoCollection = MongoManager.getCollection(shipmentsCollection);
		List<Shipment> shipments = new ArrayList<>();

		try (var mongoCursor = mongoCollection.find().iterator()) {
			while (mongoCursor.hasNext()) {
				shipments.add(this.getShipmentEntity(mongoCursor.next()));
			}
		}

		return shipments;
	}

	@Override
	public List<Shipment> getCustomerShipments(int customerId) {
		var mongoCollection = MongoManager.getCollection(shipmentsCollection);
		List<Shipment> shipments = new ArrayList<>();
		try (var mongoCursor = mongoCollection.find(eq("customerid", customerId)).iterator()) {
			while (mongoCursor.hasNext()) {
				shipments.add(this.getShipmentEntity(mongoCursor.next()));
			}
		}

		return shipments;
	}

	@Override
	public ShipmentHistory getShipmentLastStatus(int shipmentId) {
		var mongoCollection = MongoManager.getCollection(shipmentsCollection);
		var filter = eq("_id", shipmentId);
		Document projectedDocument = mongoCollection.find(filter)
				.projection(Projections.fields(Projections.include("history"))).first();
		List<Document> historyDocuments = (List<Document>) projectedDocument.get("history");
		return historyDocuments == null || historyDocuments.isEmpty() ? null : this.getHistoryEntity(historyDocuments.get(0));

	}

	@Override
	public List<ShipmentHistory> getShipmentHistory(int shipmentId) {
		var mongoCollection = MongoManager.getCollection(shipmentsCollection);
		List<ShipmentHistory> history = new ArrayList<>();
		var filter = eq("_id", shipmentId);
		var shipmentDocument = mongoCollection.find(filter).projection(Projections.fields(Projections.include("history"))).first();
		if (shipmentDocument != null) {
			List<Document> historyDocuments = (List<Document>) shipmentDocument.get("history");
			if (historyDocuments != null) {
				history = historyDocuments.stream().map(d -> this.getHistoryEntity(d)).collect(Collectors.toList());
			}
		}

		return history;
	}

	@Override
	public int addShipmentPayment(ShipmentPayment payment) {
		var mongoCollection = MongoManager.getCollection(paymentsCollection);
		payment.setId(MongoManager.getNewId(paymentDocument));
		payment.getPaymentMethod().setId(payment.getId());
		mongoCollection.insertOne(this.getPaymentDocument(payment));
		return payment.getId();
	}

	@Override
	public int addPaymentMethod(PaymentMethod paymentMethod) {
		throw new RuntimeException(new OperationNotSupportedException());
	}

	@Override
	public List<PaymentMethod> getCustomerPaymentMethods(int customerId) {
		throw new RuntimeException(new OperationNotSupportedException());
	}

	@Override
	public int addPaymentMethodType(PaymentMethodType methodType) {
		MongoManager.insertLookup(methodType.getId(), methodType.getDescription(), paymentMethodDocument);
		return methodType.getId();
	}

	@Override
	public List<PaymentMethodType> getPaymentsMethodsTypes() {
		return MongoManager.getLookups(paymentMethodDocument).stream().map(l -> l.toPaymentMethod()).collect(Collectors.toList());
	}

	@Override
	public PaymentMethodType getPaymentMethodType(int id) {
		throw new RuntimeException(new OperationNotSupportedException());
	}

	@Override
	public ShipmentPayment getShipmentPayment(int shipmentId) {
		var mongoCollection = MongoManager.getCollection(paymentsCollection);
		var paymentDocument = mongoCollection.find(eq("shipmentid", shipmentId)).first();
		return paymentDocument != null ? this.getPaymentEntity(paymentDocument) : null;
	}

	@Override
	public List<ShipmentPayment> getShipmentsPayments() {
		var mongoCollection = MongoManager.getCollection(paymentsCollection);
		List<ShipmentPayment> payments = new ArrayList<>();
		var paymentCursor = mongoCollection.find();
		
		try (var paymentsIterator = paymentCursor.iterator()) {
			while (paymentsIterator.hasNext()) {
				payments.add(this.getPaymentEntity(paymentsIterator.next()));
			}
		}
		
		return payments;
	}

	@Override
	public List<Shipment> searchShipments(int customerId, int supplierId, int statusId) {
		var mongoCollection = MongoManager.getCollection(shipmentsCollection);
		BasicDBObject filter = new BasicDBObject();
		
		if (customerId > 0) {
			filter.append("customerid", customerId);
		}
		
		if (supplierId > 0) {
			filter.append("supplierid", supplierId);
		}
		
		if (statusId > 0) {
			filter.append("laststatusid", statusId);
		}
		
		List<Shipment> shipments = new ArrayList<>();
		try (var mongoCursor = mongoCollection.find(filter).iterator()) {
			while (mongoCursor.hasNext()) {
				shipments.add(this.getShipmentEntity(mongoCursor.next()));
			}
		}
		
		return shipments;
	}

	private Document getShipmentDocument(Shipment shipment) {
		Document document = new Document();
		document.append("_id", shipment.getId());
		document.append("customerid", shipment.getCustomer().getId());
		document.append("supplierid", shipment.getSupplier().getId());
		document.append("weight", shipment.getWeight());
		document.append("numberofitems", shipment.getNumberOfItems());
		document.append("description", shipment.getDescription());
		document.append("laststatusid", ShipmentStatusEnum.RECEVIED.getId());
		document.append("addeddate", shipment.getAddedDate());
		document.append("laststatustime", LocalDateTime.now());
		return document;
	}

	private Document getHistoryDocument(ShipmentHistory history) {
		Document document = new Document();
		document.append("_id", history.getId());
		document.append("status", this.getStatusDocument(history.getShipmentStatus()));
		document.append("statustime", history.getLastStatusTime());
		return document;
	}

	private Document getStatusDocument(ShipmentStatusType status) {
		Document document = new Document();
		document.append("_id", status.getId());
		document.append("description", status.getDescription());
		return document;
	}
	
	private Document getPaymentDocument(ShipmentPayment payment) {
		Document document = new Document();
		document.append("_id", payment.getId());
		document.append("amount", payment.getAmount());
		document.append("shipmentid", payment.getShipment().getId());
		document.append("method", this.getPaymentMethodDocument(payment.getPaymentMethod()));
		return document;
	}
	
	private Document getPaymentMethodDocument(PaymentMethod paymentMethod) {
		Document document = new Document();
		document.append("_id", paymentMethod.getId());
		document.append("methodtype", this.getPaymentTypeDocument(paymentMethod.getPaymentMethodType()));
		
		if (paymentMethod instanceof PaymentCreditCard) {
			PaymentCreditCard cc = (PaymentCreditCard) paymentMethod;

			document.append("creditcardnumber", cc.getCreditCardNumber());
			document.append("cvc", cc.getCvc());
			document.append("expirydate", cc.getExpiryDate());
			document.append("name", cc.getName());
		}
		
		else if (paymentMethod instanceof PaymentPaypal) {
			PaymentPaypal pp = (PaymentPaypal) paymentMethod;
			document.append("accountname", pp.getAccountName());
		}
		
		return document;
	}
	
	private Document getPaymentTypeDocument(PaymentMethodType paymentType) {
		Document document = new Document();
		document.append("_id", paymentType.getId());
		document.append("description", paymentType.getDescription());
		return document;
	}

	private Shipment getShipmentEntity(Document document) {
		Shipment shipment = new Shipment();
		shipment.setId(document.getInteger("_id"));
		shipment.setCustomerId(document.getInteger("customerid"));
		shipment.setSupplierId(document.getInteger("supplierid"));
		shipment.setDescription(document.getString("description"));
		shipment.setWeight(document.getDouble("weight"));
		shipment.setNumberOfItems(document.getInteger("numberofitems"));
		List<Document> historyDocuments = (List<Document>) document.get("history");
		if (historyDocuments != null) {
			shipment.setHistory(
					historyDocuments.stream().map(d -> this.getHistoryEntity(d)).collect(Collectors.toList()));
		}
		return shipment;
	}

	private ShipmentHistory getHistoryEntity(Document document) {
		ShipmentHistory history = new ShipmentHistory();
		history.setId(document.getInteger("_id"));
		history.setLastStatusTime(MongoManager.getLocalDateTime(document, "statustime"));
		history.setShipmentStatus(this.getStatusEntity((Document) document.get("status")));
		return history;
	}

	private ShipmentStatusType getStatusEntity(Document document) {
		ShipmentStatusType status = new ShipmentStatusType();
		status.setId(document.getInteger("_id"));
		status.setDescription(document.getString("description"));
		return status;
	}
	
	private ShipmentPayment getPaymentEntity(Document document) {
		ShipmentPayment payment = new ShipmentPayment();
		Document methodType = (Document) document.get("methodtype");
		payment.setId(document.getInteger("_id"));
		payment.setAmount(document.getDouble("amount"));
		payment.setPaymentMethod(this.getPaymentMethodEntity((Document) document.get("method")));
		return payment;
	}
	
	private PaymentMethod getPaymentMethodEntity(Document document) {
		Document methodType = (Document) document.get("methodtype");
		int type = methodType.getInteger("_id");
		if (type == PaymentMethodEnum.CREDIT_CARD.getId()) {
			PaymentCreditCard cc = new PaymentCreditCard();
			cc.setId(document.getInteger("_id"));
			cc.setCreditCardNumber(document.getString("creditcardnumber"));
			cc.setCvc(document.getInteger("cvc"));
			cc.setExpiryDate(MongoManager.getLocalDate(document, "expirydate"));
			cc.setName(document.getString("name"));
			return cc;
		}
		
		else {
			PaymentPaypal pp = new PaymentPaypal();
			pp.setId(document.getInteger("_id"));
			pp.setAccountName("accountname");
			return pp;
		}
	}
}
