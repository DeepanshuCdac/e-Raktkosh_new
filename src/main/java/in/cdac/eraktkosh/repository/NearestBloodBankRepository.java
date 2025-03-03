package in.cdac.eraktkosh.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import in.cdac.eraktkosh.dto.NearestBloodBank;
import in.cdac.eraktkosh.services.QueryLoader;

@Repository
public class NearestBloodBankRepository {

	private final JdbcTemplate jdbcTemplate;

	private final QueryLoader queryLoader;

	private final RowMapper<NearestBloodBank> rowMapper = (rs, rowNum) -> {
		NearestBloodBank hospital = new NearestBloodBank();
		hospital.setName(rs.getString("name"));
		hospital.setAddress(rs.getString("address"));
		hospital.setCount(rs.getInt("count"));
		hospital.setPhone(rs.getString("phone"));
		hospital.setEmail(rs.getString("email"));
		hospital.setFacility(rs.getString("facility"));
		hospital.setHospitalType(rs.getString("hospitalType"));
		hospital.setHospitalCode(rs.getInt("hospitalCode"));
		hospital.setLatitude(rs.getDouble("hgnum_latitude"));
		hospital.setLongitude(rs.getDouble("hgnum_longitude"));
		hospital.setCampSource(rs.getInt("gnum_camp_source"));
		hospital.setStockSource(rs.getInt("gnum_stock_source"));
		hospital.setDistId(rs.getInt("distId"));
		hospital.setStateCode(rs.getInt("stateCode"));
		hospital.setType(rs.getString("type"));
		return hospital;
	};

    NearestBloodBankRepository(JdbcTemplate jdbcTemplate, QueryLoader queryLoader) {
        this.jdbcTemplate = jdbcTemplate;
        this.queryLoader = queryLoader;
    }

	public List<NearestBloodBank> findNearestHospitals(int stateCode, int districtCode) {
		String sql = queryLoader.getQuery("fetch.nearest_blood_bank");

		if (sql == null) {
			throw new RuntimeException("SQL Query for 'fetch.nearest_blood_bank' is NULL. Check query.properties.");
		}

		try {
			return jdbcTemplate.query(sql, rowMapper, stateCode, districtCode, districtCode);
		} catch (Exception e) {
			throw new RuntimeException("Database Query Failed: " + e.getMessage(), e);
		}
	}
}
