package in.cdac.eraktkosh.repository;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import in.cdac.eraktkosh.entity.RegisterEntity;
import in.cdac.eraktkosh.services.QueryLoader;

@Repository
public class RegisterRepository {

	private static final Logger logger = LoggerFactory.getLogger(RegisterRepository.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final QueryLoader queryLoader;

	public RegisterRepository() {
		queryLoader = new QueryLoader("query.properties");
	}

//  checking otp_count_for_mobile_no.
	public int getOtpCount(String mobileNumber) {
		String query = queryLoader.getQuery("query.GET_OTP_COUNT");
		int count = 0;
		try {
			count = jdbcTemplate.queryForObject(query, Integer.class, mobileNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

//  insert otp count for mobile no.
	public String insertOtpCount(String mobileNumber) {
		String query = queryLoader.getQuery("query.INSERT.OTP.COUNT");
		jdbcTemplate.update(query, mobileNumber);
		return query;
	}

//	Update User with these fields..
	public int UpdateDonor(RegisterEntity Re) {
		String query = queryLoader.getQuery("query.UPDATE.DONOR.DETAILS");
		JSONObject json = new JSONObject(Re);
		Object[] params = { json.get("mobileNo"), json.get("firstName"), json.get("lastName"),
				json.get("bloodGroupCode"), json.get("password"), json.get("firstLogin"), json.get("demographics"),
				json.get("isValid"), json.get("stateCode"), json.get("districtCode"), json.get("pinCode"),
				json.get("emailId"), json.get("genderCode"), json.get("userId"), json.get("address"),
				json.get("allBlood"), json.get("repository"), json.get("registrationMode"), json.get("fatherName") };

		try {
			return jdbcTemplate.update(query, params);
		} catch (Exception e) {
			logger.error("Error while inserting donor details: {}", e.getMessage());
			return 0;
		}
	}

// Register Donor
	public int RegisterNewDonor(RegisterEntity Re) {
		String query = queryLoader.getQuery("query.INSERT.DONOR.DETAILS");
		JSONObject json = new JSONObject(Re);
		Object[] params = { json.get("mobileNo"), json.get("firstName"), json.get("lastName"),
				json.get("bloodGroupCode"), json.get("password"), json.get("firstLogin"), json.get("demographics"),
				json.get("isValid"), json.get("stateCode"), json.get("districtCode"), json.get("pinCode"),
				json.get("emailId"), json.get("genderCode"), json.get("userId"), json.get("address"),
				json.get("allBlood"), json.get("repository"), json.get("registrationMode"), json.get("fatherName"),
				json.get("dob") };

		try {
			return jdbcTemplate.update(query, params);
		} catch (Exception e) {
			logger.error("Error while inserting donor details: {}", e.getMessage());
			return 0;
		}
	}

}
