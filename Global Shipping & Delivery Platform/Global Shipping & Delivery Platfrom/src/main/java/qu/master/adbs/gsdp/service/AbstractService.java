package qu.master.adbs.gsdp.service;

public abstract class AbstractService {
	
	protected ServiceResult getResult(String status, String message, Object data) {
		
		ServiceResult result = new ServiceResult(status, message, data);
		return result;	
	}
	
	protected ServiceResult returnOk(String message, Object data) {
		return getResult("Y", message, data);
	}
	
	protected ServiceResult ok(Object data) {
		return returnOk(null, data);
	}
	
	protected ServiceResult ok(String message) {
		return returnOk(message, null);
	}
	
	protected ServiceResult error(String errorMessage) {
		return getResult("N", errorMessage, null);
	}
	
	
}