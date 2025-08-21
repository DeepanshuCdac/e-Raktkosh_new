package in.cdac.eraktkosh.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import in.cdac.eraktkosh.services.QueryLoader;

@Repository
public class UtilityRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private QueryLoader queryLoader;

	public String getLgdStateCode(String stateCode) {
		String query = queryLoader.getQuery("get_lgd_state_code");
		String result = "";
		try {
			result = jdbcTemplate.queryForObject(query, String.class, stateCode);
			System.out.println("getLgdStateCode Result: " + result);
		} catch (Exception e) {
			System.out.println("Error while getLgdStateCode: " + e.getMessage());
		}
		return result;
	}

	public String getLgdDistrictCode(String districtCode) {
		String query = queryLoader.getQuery("get_lgd_district_code");
		String result = "";
		try {
			result = jdbcTemplate.queryForObject(query, String.class, districtCode);
			System.out.println("getLgdDistrictCode Result: " + result);
		} catch (Exception e) {
			System.out.println("Error while getLgdDistrictCode: " + e.getMessage());
		}
		return result;
	}

	public String getStateCode(String lgdStateCode) {
		String query = queryLoader.getQuery("get_state_code");
		String result = "";
		try {
			result = jdbcTemplate.queryForObject(query, String.class, lgdStateCode);
			System.out.println("getStateCode Result: " + result);
		} catch (Exception e) {
			System.out.println("Error while getStateCode: " + e.getMessage());
		}
		return result;
	}

	public String getDistrictCode(String lgdDistrictCode) {
		String query = queryLoader.getQuery("get_district_code");
		String result = "";
		try {
			result = jdbcTemplate.queryForObject(query, String.class, lgdDistrictCode);
			System.out.println("getDistrictCode Result: " + result);
		} catch (Exception e) {
			System.out.println("Error while getDistrictCode: " + e.getMessage());
		}
		return result;
	}

}
