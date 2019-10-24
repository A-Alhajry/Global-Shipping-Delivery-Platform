package qu.master.adbs.gsdp.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import qu.master.adbs.gsdp.adapters.LocalDateAdapter;

public class ShipmentsStatusesReport {
	
	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	private LocalDate reportStartDate;
	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	private LocalDate reportEndDate;
	private List<ShipmentsStatusCount> statusesCounts;
	
	public LocalDate getReportStartDate() {
		return reportStartDate;
	}
	public void setReportStartDate(LocalDate reportStartDate) {
		this.reportStartDate = reportStartDate;
	}
	public LocalDate getReportEndDate() {
		return reportEndDate;
	}
	public void setReportEndDate(LocalDate reportEndDate) {
		this.reportEndDate = reportEndDate;
	}
	public List<ShipmentsStatusCount> getStatusesCounts() {
		return statusesCounts;
	}
	public void setStatusesCounts(List<ShipmentsStatusCount> statusesCounts) {
		this.statusesCounts = statusesCounts;
	}
	
	public void addStatusCount(ShipmentsStatusCount statusCount) {
		if (this.statusesCounts == null) {
			this.statusesCounts = new ArrayList<>();
		}
		
		this.statusesCounts.add(statusCount);
	}
}
