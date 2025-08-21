package in.cdac.eraktkosh.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import in.cdac.eraktkosh.dto.BloodBankDTO;
import in.cdac.eraktkosh.services.QueryLoader;

@Repository
public class BloodBankRepository {

	private final JdbcTemplate jdbcTemplate;
	private final QueryLoader queryLoader;

	@Autowired
	public BloodBankRepository(JdbcTemplate jdbcTemplate, QueryLoader queryLoader) {
		this.jdbcTemplate = jdbcTemplate;
		this.queryLoader = queryLoader;
	}

	public List<BloodBankDTO> getBloodBankList(int stateCode, int districtCode) {
		String query;
		Object[] params;

		if (districtCode == 0) { // or use -1 or null based on your logic
			query = queryLoader.getQuery("fetch.bloodBank.allDistricts");
			params = new Object[] { stateCode };
		} else {
			query = queryLoader.getQuery("fetch.bloodBank");
			params = new Object[] { stateCode, districtCode };
		}

		return jdbcTemplate.query(query, params, this::mapRowToDTO);
	}

	private BloodBankDTO mapRowToDTO(ResultSet rs, int rowNum) throws SQLException {
		BloodBankDTO dto = new BloodBankDTO();
		dto.setHospitalCode(rs.getInt("gnum_hospital_code"));
		dto.setHospitalName(rs.getString("hospital_name"));
		dto.setMobileNo(rs.getString("mobile_no"));
		return dto;
	}
}
