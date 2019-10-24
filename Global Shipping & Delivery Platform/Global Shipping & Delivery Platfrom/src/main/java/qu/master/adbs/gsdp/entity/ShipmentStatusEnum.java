package qu.master.adbs.gsdp.entity;

public enum ShipmentStatusEnum {
	RECEVIED(1, "Received"), SHIPPED(2, "Shipped"), DELIVERED(3, "Delivered"), CANCELLED(4, "Cancelled");
	private int id;
	private String desc;
	
	private ShipmentStatusEnum(int id, String desc) {
		this.id = id;
		this.desc = desc;
	}
	
	public ShipmentStatusType getStatusEntity() {
		ShipmentStatusType status = new ShipmentStatusType();
		status.setId(id);
		status.setDescription(desc);
		return status;
	}
	
	public static ShipmentStatusType getStatusById(int id) {
		for(ShipmentStatusEnum statusEnum : ShipmentStatusEnum.values()) {
			if (statusEnum.getId() == id) {
				return statusEnum.getStatusEntity();
			}
		}
		
		return null;
	}
	
	public int getId() {
		return this.id;
	}
}
