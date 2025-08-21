package in.cdac.eraktkosh.repository;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import in.cdac.eraktkosh.dto.CampNotificationDTO;
import in.cdac.eraktkosh.mapper.CampNotificationRowMapper;
import in.cdac.eraktkosh.services.QueryLoader;

@Repository
public class CampNotificationRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private QueryLoader queryLoader;

	public List<CampNotificationDTO> getUpcomingCampNotifications() {
		try {
			String campQuery = queryLoader.getQuery("get.upcoming.camps.query");
			return jdbcTemplate.query(campQuery, new CampNotificationRowMapper());
		} catch (Exception ex) {
			System.err.println("Error while fetching camp notifications: " + ex.getMessage());
			return Collections.emptyList();
		}
	}

	public void insertLogEntry(CampNotificationDTO dto) {
		String sql = "INSERT INTO hbbt_probable_donor_log_dtl "
				+ "(hbstr_email_id, hbnum_camp_req_no, hbdt_camp_date, hbdt_email_date, gnum_isvalid) "
				+ "VALUES (?, ?, ?, SYSDATE, 1)";

		jdbcTemplate.update(sql, dto.getEmail(), dto.getCampReqNo(),
				java.sql.Date.valueOf(dto.getCampDateAsLocalDate()));
	}
}
