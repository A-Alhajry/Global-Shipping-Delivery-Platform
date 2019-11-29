package qu.master.adbs.gsdp.repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Projections;

import qu.master.adbs.gsdp.entity.PaymentsStatsReport;
import qu.master.adbs.gsdp.entity.ShipmentStatusEnum;
import qu.master.adbs.gsdp.entity.ShipmentsStatusCount;
import qu.master.adbs.gsdp.entity.ShipmentsStatusesReport;

import static com.mongodb.client.model.Aggregates.*;

@ApplicationScoped
@RepositoryModeType(RepositoryMode.MONGO)
public class MongoReportsRepository implements ReportsRepository {

	private final static String shipmentsCollection = "shipments";
	private final static String paymentsCollection = "payments";
	private ShipmentsRepository shipmentsRepository;
	
	public MongoReportsRepository() {
		this.shipmentsRepository = new MongoShipmentsRepository();
	}
	
	@Override
	public PaymentsStatsReport getPaymentsStatsReport(LocalDate startDate, LocalDate endDate) {
		PaymentsStatsReport report = new PaymentsStatsReport();
		report.setReportStartDate(startDate);
		report.setReportEndDate(endDate);
		
		var shipmentsMongoCollection = MongoManager.getCollection(shipmentsCollection);
		var paymentsMongoCollection = MongoManager.getCollection(paymentsCollection);
		var filter = new BasicDBObject("addeddate", new BasicDBObject("$lte", Date.valueOf(endDate)).append("$gte", Date.valueOf(startDate)));
		var shipmentsIds = this.filterShipmentsIds(shipmentsMongoCollection, filter);
		var aggregates = this.getPaymentsReportAggregates(paymentsMongoCollection, shipmentsIds);
		var result = paymentsMongoCollection.aggregate(aggregates);
		int count = 0; 
		int topShipment = 0;
		int bottomShipment = 0;
		double sum = 0;
		double minAmount = 0;
		double maxAmount = 0;
		for(var currentRecord : result) {
			int shipmentId = currentRecord.getInteger("shipmentid");
			double amount = currentRecord.getDouble("amount");
			
			if (count == 0) {
				topShipment = shipmentId;
				minAmount = amount;				
			}
			bottomShipment = shipmentId;
			maxAmount = amount;
			sum += amount;
			count++;
		}
		
		report.setTotalAmount(sum);
		if (count != 0) {
			report.setAvgAmount(sum / count);
		}
		report.setTopShipment(shipmentsRepository.getShipment(topShipment));
		report.setBottomShipment(shipmentsRepository.getShipment(bottomShipment));
		report.setMinAmount(minAmount);
		report.setMaxAmount(maxAmount);
		
		return report;
	}

	@Override
	public ShipmentsStatusesReport getShipmentsStatusesReport(LocalDate startDate, LocalDate endDate) {
		var mongoCollection = MongoManager.getCollection(shipmentsCollection);
		var startTimestamp = Timestamp.valueOf(LocalDateTime.of(startDate, LocalTime.MIN));
		var endTimestamp = Timestamp.valueOf(LocalDateTime.of(endDate, LocalTime.MAX));
		var filter = new BasicDBObject("laststatustime", new BasicDBObject("$lte", endTimestamp).append("$gte", startTimestamp));
		
		ShipmentsStatusCount receivedShipments = new ShipmentsStatusCount();
		receivedShipments.setStatus(ShipmentStatusEnum.RECEVIED.getStatusEntity());
		ShipmentsStatusCount shippedShipments = new ShipmentsStatusCount();
		shippedShipments.setStatus(ShipmentStatusEnum.SHIPPED.getStatusEntity());
		ShipmentsStatusCount deliveredShipments = new ShipmentsStatusCount();
		deliveredShipments.setStatus(ShipmentStatusEnum.DELIVERED.getStatusEntity());
		ShipmentsStatusCount cancelledShipments = new ShipmentsStatusCount();
		cancelledShipments.setStatus(ShipmentStatusEnum.CANCELLED.getStatusEntity());
		
		try(var mongoCursor = mongoCollection.find(filter).projection(Projections.fields(Projections.include("laststatusid"), Projections.excludeId())).iterator()) {
			while (mongoCursor.hasNext()) {
				int statusId = mongoCursor.next().getInteger("laststatusid");
				ShipmentsStatusCount shipmentsCount = null;
				
				if (statusId == ShipmentStatusEnum.RECEVIED.getId()) {
					shipmentsCount = receivedShipments;
				}
				
				else if (statusId == ShipmentStatusEnum.SHIPPED.getId()) {
					shipmentsCount = shippedShipments;
				}
				
				else if (statusId == ShipmentStatusEnum.DELIVERED.getId()) {
					shipmentsCount = deliveredShipments;
				}
				
				else {
					shipmentsCount = cancelledShipments;
				}
				
				int newCount = shipmentsCount.getTotalCount() + 1;
				shipmentsCount.setTotalCount(newCount);
			}
		}
		
		List<ShipmentsStatusCount> shipmentsCounts = Arrays.asList(receivedShipments, shippedShipments, deliveredShipments, cancelledShipments);
		ShipmentsStatusesReport report = new ShipmentsStatusesReport();
		report.setReportStartDate(startDate);
		report.setReportEndDate(endDate);
		report.setStatusesCounts(shipmentsCounts);
		return report;
	};
	
	private List<Integer> filterShipmentsIds(MongoCollection<Document> mongoCollection, BasicDBObject filter) {
		List<Integer> ids = new ArrayList<>();
		try (var mongoCursor = mongoCollection.find(filter).projection(Projections.fields(Projections.include("_id"))).iterator()) {
			while (mongoCursor.hasNext()) {
				ids.add(mongoCursor.next().getInteger("_id"));
			}
		}
		
		return ids;
	}
	
	private List<Document> getPaymentsReportAggregates(MongoCollection<Document> mongoCollection, List<Integer> shipmentsIds) {
		Document match = new Document("$match", new Document("shipmentid", new Document("$in", shipmentsIds)));
		Document sort = new Document("$sort", new Document("amount", 1));
		Document project = new Document("$project", new Document("shipmentid", "$shipmentid").append("amount", "$amount"));
		//Document sum = new Document("$sum", new Document("amount", 1));
		return Arrays.asList(match, sort, project);
	}
	
}