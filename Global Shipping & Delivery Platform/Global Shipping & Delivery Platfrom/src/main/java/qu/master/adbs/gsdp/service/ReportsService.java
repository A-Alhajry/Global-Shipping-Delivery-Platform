package qu.master.adbs.gsdp.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import qu.master.adbs.gsdp.repository.*;

@Path("/reports")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
public class ReportsService extends AbstractService {
	
	@Inject
	@RepositoryModeType(RepositoryMode.MONGO)
	ReportsRepository reportsRepository;
	
	
	@GET
	@Path("/paymentsStats")
	public ServiceResult getPaymentsStatsReport(@QueryParam("startDate") String startDate, @QueryParam("endDate") String endDate) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate parsedStartDate, parsedEndDate;
			
			if (startDate == null || startDate.isEmpty()) {
				parsedStartDate = LocalDate.of(1970, 1, 1);
			}
			
			else {
				parsedStartDate = LocalDate.parse(startDate, formatter);
			}
			
			if (endDate == null || endDate.isEmpty()) {
				parsedEndDate = LocalDate.of(2030, 12, 30);
			}
			
			else {
				parsedEndDate = LocalDate.parse(endDate, formatter);
			}
			
			return ok(reportsRepository.getPaymentsStatsReport(parsedStartDate, parsedEndDate));
		}
		
		catch (Exception e) {
			return error(e.getMessage());
		}
	}
	
	@GET
	@Path("/shipmentsStatuses")
	public ServiceResult getShipmentsStatusesReport(@QueryParam("startDate") String startDate, @QueryParam("endDate") String endDate) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate parsedStartDate, parsedEndDate;
			
			if (startDate == null || startDate.isEmpty()) {
				parsedStartDate = LocalDate.of(1970, 1, 1);
			}
			
			else {
				parsedStartDate = LocalDate.parse(startDate, formatter);
			}
			
			if (endDate == null || endDate.isEmpty()) {
				parsedEndDate = LocalDate.of(2030, 12, 30);
			}
			
			else {
				parsedEndDate = LocalDate.parse(endDate, formatter);
			}
			return ok(reportsRepository.getShipmentsStatusesReport(parsedStartDate, parsedEndDate));
		}
		
		catch (Exception e) {
			e.printStackTrace();
			return error(e.getMessage());
		}
	}
}