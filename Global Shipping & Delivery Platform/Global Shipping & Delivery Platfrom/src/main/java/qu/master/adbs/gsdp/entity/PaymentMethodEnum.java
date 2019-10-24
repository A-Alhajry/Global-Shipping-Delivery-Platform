package qu.master.adbs.gsdp.entity;

public enum PaymentMethodEnum {
	CREDIT_CARD(1, "Credit Card"), PAYPALL(2, "PayPal");
	
	private int id;
	private String desc;
	
	private PaymentMethodEnum(int id, String desc) {
		this.id = id;
		this.desc = desc;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getDesc() {
		return this.desc;
	}
	
	public PaymentMethodType getMethodType(int id) {
		for(PaymentMethodEnum methodEnum : PaymentMethodEnum.values()) {
			if (methodEnum.getId() == id) {
				PaymentMethodType methodType = new PaymentMethodType();
				methodType.setId(id);
				methodType.setDescription(methodEnum.getDesc());
				return methodType;
			}
		}
		
		return null;
	}
}
