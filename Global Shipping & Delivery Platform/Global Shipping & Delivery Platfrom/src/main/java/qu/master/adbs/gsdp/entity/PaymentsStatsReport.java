package qu.master.adbs.gsdp.entity;

import java.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import qu.master.adbs.gsdp.adapters.LocalDateAdapter;

public class PaymentsStatsReport {
	
	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	private LocalDate reportStartDate;
	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	private LocalDate reportEndDate;
	private double avgAmount;
	private double totalAmount;
	private double minAmount;
	private double maxAmount;
	private Shipment topShipment;
	private Shipment bottomShipment;
	
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
	public double getAvgAmount() {
		return avgAmount;
	}
	public void setAvgAmount(double avgAmount) {
		this.avgAmount = avgAmount;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public double getMinAmount() {
		return minAmount;
	}
	public void setMinAmount(double minAmount) {
		this.minAmount = minAmount;
	}
	public double getMaxAmount() {
		return maxAmount;
	}
	public void setMaxAmount(double maxAmount) {
		this.maxAmount = maxAmount;
	}
	public Shipment getTopShipment() {
		return topShipment;
	}
	public void setTopShipment(Shipment topShipment) {
		this.topShipment = topShipment;
	}
	public Shipment getBottomShipment() {
		return bottomShipment;
	}
	public void setBottomShipment(Shipment bottomShipment) {
		this.bottomShipment = bottomShipment;
	}
	
	
}
