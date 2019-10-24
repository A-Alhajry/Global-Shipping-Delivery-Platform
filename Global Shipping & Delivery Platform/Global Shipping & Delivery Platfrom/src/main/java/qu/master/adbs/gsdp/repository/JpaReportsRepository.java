package qu.master.adbs.gsdp.repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import qu.master.adbs.gsdp.entity.PaymentsStatsReport;
import qu.master.adbs.gsdp.entity.Shipment;
import qu.master.adbs.gsdp.entity.ShipmentStatusType;
import qu.master.adbs.gsdp.entity.ShipmentsStatusCount;
import qu.master.adbs.gsdp.entity.ShipmentsStatusesReport;

public class JpaReportsRepository implements ReportsRepository {

	private EntityManagerFactory emf;
	private EntityManager em;
	
	private ShipmentsRepository shipmentsRepository;
	
	private final static String  paymentsStatsSql = "select s.id, s.description, sp.amount, rank() over(order by sp.amount) s_order, max(sp.amount) over(), min(sp.amount) over(), sum(sp.amount) over(), avg(sp.amount) over() " + 
													"from Shipment s " + 
													"inner join ShipmentPayment sp on s.id = sp.shipment_id " + 
													"where s.addedDate >= Cast(? as Date) And s.addedDate <= Cast(? as Date) " +
													"order by s_order";
	private final static String shipmentsStatusesSql = "select count(*) from ShipmentHistory sh  " +
													   "where sh.ShipmentStatus_ID = ? and sh.AddedDate >= Cast(? as Date) and sh.AddedDate <= Cast(? As Date) ";
	
	public JpaReportsRepository() {
		this("GSDP_MSSQL");
	}
	
	public JpaReportsRepository(String persistenceUnitName) {
		emf = Persistence.createEntityManagerFactory(persistenceUnitName);
		em = emf.createEntityManager();
		shipmentsRepository = new JpaShipmentsRepository(persistenceUnitName);
	}
	
	public PaymentsStatsReport getPaymentsStatsReport(LocalDate startDate, LocalDate endDate) {
		PaymentsStatsReport report = new PaymentsStatsReport();
		report.setReportStartDate(startDate);
		report.setReportEndDate(endDate);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Query query = em.createNativeQuery(paymentsStatsSql);
		query.setParameter(1, startDate.format(formatter));
		query.setParameter(2, endDate.format(formatter));
		List<Object[]> resultSet = query.getResultList();
		report = readPaymentsStatsResultSet(report, resultSet);				
		return report;
	}
	
	private PaymentsStatsReport readPaymentsStatsResultSet(PaymentsStatsReport report, List<Object[]> resultSet) {
		if (!resultSet.isEmpty()) {
			Object[] firstRow = resultSet.get(0);
			Object[] lastRow = resultSet.get(resultSet.size() - 1);
			double totalAmount = (double) firstRow[6];
			double avgAmount = (double) firstRow[7];
			int leastPaidShipmentId = (int) firstRow[0];
			double lowestPaidAmount = (double) firstRow[2];
			int mostPaidShipmentId = (int) lastRow[0];
			double mostPaidAmount = (double) lastRow[2];
			Shipment bottomShipment = shipmentsRepository.getShipment(leastPaidShipmentId);
			Shipment topShipment = shipmentsRepository.getShipment(mostPaidShipmentId);
						
			report.setBottomShipment(bottomShipment);
			report.setTopShipment(topShipment);
			report.setAvgAmount(avgAmount);
			report.setMinAmount(lowestPaidAmount);
			report.setMaxAmount(mostPaidAmount);
			report.setTotalAmount(totalAmount);					
		}
		
		return report;

	}
	
	public ShipmentsStatusesReport getShipmentsStatusesReport(LocalDate startDate, LocalDate endDate) {
		ShipmentsStatusesReport report = new ShipmentsStatusesReport();
		report.setReportStartDate(startDate);
		report.setReportEndDate(endDate);
		List<ShipmentStatusType> allStatuses = shipmentsRepository.getShipmentStatusTypes();
		if (allStatuses != null && !allStatuses.isEmpty()) {						
			Query query = this.buildShipmentsStatusesQuery(em, allStatuses, startDate, endDate);
			List<Integer> resultSet = query.getResultList();
			report = this.getStatusesReportFromResultList(report, allStatuses, resultSet);			
		}
		
		return report;
		
	}
	
	private Query buildShipmentsStatusesQuery(EntityManager em, List<ShipmentStatusType> allStatuses, LocalDate startDate, LocalDate endDate) {
		StringBuilder queryBuilder = new StringBuilder(shipmentsStatusesSql);
		List<Object> queryParameters = new ArrayList<Object>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		ShipmentStatusType currentStatus = allStatuses.get(0);
		queryParameters.add(currentStatus.getId());
		queryParameters.add(startDate.format(formatter));
		queryParameters.add(endDate.format(formatter));
		
		for(int i = 1; i < allStatuses.size(); i++) {
			currentStatus = allStatuses.get(i);
			queryBuilder.append(" Union All ").append(shipmentsStatusesSql);
			queryParameters.add(currentStatus.getId());
			queryParameters.add(startDate.format(formatter));
			queryParameters.add(endDate.format(formatter));
		}
		
		Query query = em.createNativeQuery(queryBuilder.toString());
		for(int i = 0; i < queryParameters.size(); i++) {
			query.setParameter(i + 1, queryParameters.get(i));
		}
		
		return query;
	}
	
	private ShipmentsStatusesReport getStatusesReportFromResultList(ShipmentsStatusesReport report, List<ShipmentStatusType> allStatuses, List<Integer> resultSet) {
		if (resultSet != null && !resultSet.isEmpty()) {
			for(int i = 0; i < resultSet.size(); i++) {
				int count = resultSet.get(i);
				ShipmentStatusType currentStatus = allStatuses.get(i);
				ShipmentsStatusCount currentStatusCount = new ShipmentsStatusCount();
				currentStatusCount.setStatus(currentStatus);
				currentStatusCount.setTotalCount(count);
				report.addStatusCount(currentStatusCount);
			}
		}
		
		return report;
	}
}
