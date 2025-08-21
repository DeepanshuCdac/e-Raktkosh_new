package in.cdac.eraktkosh.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import in.cdac.eraktkosh.services.QueryLoader;

@Repository
public class ABHARepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private QueryLoader queryLoader;

	public String getFacilityIdBasedOnHOSiptalCode() {

		return "";
	}

}
