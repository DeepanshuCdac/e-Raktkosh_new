package in.cdac.eraktkosh.dto;

import java.util.Map;
public class SearchLogRequestDTO {
	
	    private String serviceType;
	    private Map<String, Object> searchParams;
	    private String ipAddress;

	    public SearchLogRequestDTO() {
	    }

	    public SearchLogRequestDTO(String serviceType, Map<String, Object> searchParams, String ipAddress) {
	        this.serviceType = serviceType;
	        this.searchParams = searchParams;
	        this.ipAddress = ipAddress;
	    }

	    public String getServiceType() {
	        return serviceType;
	    }

	    public void setServiceType(String serviceType) {
	        this.serviceType = serviceType;
	    }

	    public Map<String, Object> getSearchParams() {
	        return searchParams;
	    }

	    public void setSearchParams(Map<String, Object> searchParams) {
	        this.searchParams = searchParams;
	    }

	    public String getIpAddress() {
	        return ipAddress;
	    }

	    public void setIpAddress(String ipAddress) {
	        this.ipAddress = ipAddress;
	    }
	}
