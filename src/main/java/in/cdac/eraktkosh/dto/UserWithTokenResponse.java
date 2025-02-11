package in.cdac.eraktkosh.dto;

public class UserWithTokenResponse {
	private Object userDetails;
	private String token;

	public UserWithTokenResponse(Object userDetails, String token) {
		this.userDetails = userDetails;
		this.token = token;
	}

	public Object getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(Object userDetails) {
		this.userDetails = userDetails;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}