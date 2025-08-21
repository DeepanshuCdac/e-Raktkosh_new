package in.cdac.eraktkosh.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import in.cdac.eraktkosh.dto.CampDetailDTO;
import in.cdac.eraktkosh.services.QueryLoader;

@Repository
public class CampDetailRepository {

	private final JdbcTemplate jdbcTemplate;
	private final QueryLoader queryLoader;

	@Autowired
	public CampDetailRepository(JdbcTemplate jdbcTemplate, QueryLoader queryLoader) {
		this.jdbcTemplate = jdbcTemplate;
		this.queryLoader = queryLoader;
	}

	// Existing method - Don't touch
	@SuppressWarnings("deprecation")
	public List<CampDetailDTO> fetchCamps(int stateCode, Integer districtCode, String startDate, String endDate) {
		String query = queryLoader.getQuery("fetch.blood_donation_camps");

		int finalDistrictCode = (districtCode != null) ? districtCode : 0;

		if (startDate == null || startDate.isEmpty()) {
			startDate = java.time.LocalDate.now().toString();
		}
		if (endDate == null || endDate.isEmpty()) {
			endDate = java.time.LocalDate.now().toString();
		}

		Object[] params = { stateCode, finalDistrictCode, finalDistrictCode, startDate, endDate, stateCode,
				finalDistrictCode, finalDistrictCode, startDate, endDate, stateCode, finalDistrictCode,
				finalDistrictCode, startDate, endDate };

		return jdbcTemplate.query(query, params, getRowMapper());
	}

	// New method for camp schedule by hospital code
	public List<CampDetailDTO> fetchCampScheduleByHospitalCode(Integer hospitalCode) {
		String query = queryLoader.getQuery("camp.schedule.query");

		return jdbcTemplate.query(query, new Object[] { hospitalCode, hospitalCode, hospitalCode }, getRowMapper());
	}

	// Reusable RowMapper
	private RowMapper<CampDetailDTO> getRowMapper() {
		return new RowMapper<CampDetailDTO>() {
			@Override
			public CampDetailDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
				CampDetailDTO dto = new CampDetailDTO();
				dto.setCampDate(rs.getString("campdate"));
				dto.setCampTime(rs.getString("camptime"));
				dto.setCampName(rs.getString("campname"));
				dto.setCampVenue(rs.getString("campvenue"));
				dto.setContact(rs.getString("contact"));
				dto.setConductedBy(rs.getString("conductedby"));
				dto.setHospName(rs.getString("hospName"));
				dto.setCampReqNo(rs.getLong("camp_reqno"));
				dto.setType(rs.getInt("type"));
				dto.setIsPortalRegistration(rs.getInt("is_portalregistration"));
				dto.setStateName(rs.getString("stateName"));
				dto.setDistrictName(rs.getString("distName"));
				dto.setDateToSort(rs.getString("dateToSort"));
				return dto;
			}
		};
	}
}
