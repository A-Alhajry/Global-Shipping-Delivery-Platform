package qu.master.adbs.gsdp.repository;

import java.time.LocalDate;

import qu.master.adbs.gsdp.entity.PaymentsStatsReport;
import qu.master.adbs.gsdp.entity.ShipmentsStatusesReport;

public interface ReportsRepository {
	
	public abstract PaymentsStatsReport getPaymentsStatsReport(LocalDate startDate, LocalDate endDate);
	
	public abstract ShipmentsStatusesReport getShipmentsStatusesReport(LocalDate startDate, LocalDate endDate);
}
