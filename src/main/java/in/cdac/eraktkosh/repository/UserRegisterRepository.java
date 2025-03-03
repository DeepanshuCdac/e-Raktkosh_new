package in.cdac.eraktkosh.repository;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import in.cdac.eraktkosh.services.QueryLoader;

@Repository
public class UserRegisterRepository {

	@Autowired
	private final JdbcTemplate jdbcTemplate;
	private final QueryLoader queryLoader;

	public UserRegisterRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.queryLoader = new QueryLoader("query.properties");
	}

	public int checkMobileExists(String mobileNo) {
		String query = queryLoader.getQuery("query.CHECK_USERID_EXIST");
		int result = 0;
		try {
			result = jdbcTemplate.queryForObject(query, Integer.class, mobileNo);
			System.out.println("Check Mobile Exists Result: " + result);
		} catch (Exception e) {
			System.out.println("Error while checking mobile existence: " + e.getMessage());
		}
		return result;
	}

	public int getOtpCount(String mobileNo) {
		String query = queryLoader.getQuery("query.GET_OTP_COUNT");
		int count = 0;
		try {
			count = jdbcTemplate.queryForObject(query, Integer.class, mobileNo);
			System.out.println("OTP Count: " + count);
		} catch (Exception e) {
			System.out.println("Error while fetching OTP count: " + e.getMessage());
		}
		return count;
	}

	public void insertOtpLog(String mobileNo, String otp) {
		String message = "Your eRaktKosh Portal Signup OTP for username: " + mobileNo + " is: " + otp
				+ ". Please do not share your OTP with anyone.";

		String query = queryLoader.getQuery("query.INSERT_OTP_LOG");
		jdbcTemplate.update(query, message);
		System.out.println("Inserted OTP log for: " + mobileNo);
	}

	public void insertOtp(String mobileNo) {
		String query = queryLoader.getQuery("query.INSERT.OTP.COUNT");
		jdbcTemplate.update(query, mobileNo);
		System.out.println("Inserted OTP record for: " + mobileNo);
	}

	public boolean saveUserDetails(Map<String, String> userDetails) {
		String query = queryLoader.getQuery("query.INSERT.DONOR.DETAILS");

		int result = jdbcTemplate.update(query, userDetails.get("mobileNo"), userDetails.get("firstName"),
				userDetails.get("lastName"), parseInteger(userDetails.get("bloodGroupCode")),
				userDetails.get("password"), parseInteger(userDetails.get("firstLogin")),
				parseInteger(userDetails.get("demographics")), parseInteger(userDetails.get("isValid")),
				parseInteger(userDetails.get("stateCode")), parseInteger(userDetails.get("districtCode")),
				userDetails.get("pincode"), userDetails.get("emailId"), userDetails.get("genderCode"),
				userDetails.get("userId"), userDetails.get("address"), parseInteger(userDetails.get("allBlood")),
				parseInteger(userDetails.get("repository")), parseInteger(userDetails.get("registrationMode")),
				userDetails.get("fatherName"), userDetails.get("dob"));

		return result > 0;
	}

	private Integer parseInteger(String value) {
		return (value != null && !value.isEmpty()) ? Integer.parseInt(value) : null;
	}

}
