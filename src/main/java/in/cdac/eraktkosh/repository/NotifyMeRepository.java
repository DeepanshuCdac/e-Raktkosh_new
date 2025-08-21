package in.cdac.eraktkosh.repository;

import java.sql.Array;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import in.cdac.eraktkosh.dto.NotifyMeDTO;
import in.cdac.eraktkosh.services.QueryLoader;

@Repository
public class NotifyMeRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private QueryLoader queryLoader;

	@Autowired
	private DataSource dataSource;

	public List<Map<String, Object>> findByEmail(String email) {
		return jdbcTemplate.queryForList(queryLoader.getQuery("CHECK_EXISTING_EMAIL"), email);
	}

	public Array getHospitalCodeArray(List<Integer> hospitalCodes) throws SQLException {
		return dataSource.getConnection().createArrayOf("INTEGER", hospitalCodes.toArray());
	}

	public List<Map<String, Object>> findExactMatch(String email, int stateCode, int districtCode, int bloodGroupCode,
			Integer bloodComponentId, Long mobileNo, Array hospitalCodesArray) {
		return jdbcTemplate.queryForList(queryLoader.getQuery("CHECK_SAME_NOTIFICATION_EXISTS"), email, stateCode,
				districtCode, bloodGroupCode, bloodComponentId, mobileNo, hospitalCodesArray);
	}

	public int isEntryOlderThanThreeDays(String email, int stateCode, int districtCode, int bloodGroupCode,
			Integer bloodComponentId, Long mobileNo, Array hospitalCodesArray) {
		return jdbcTemplate.queryForObject(queryLoader.getQuery("CHECK_IF_SAME_ENTRY_IS_OLDER_THAN_3_DAYS"),
				Integer.class, email, stateCode, districtCode, bloodGroupCode, bloodComponentId, mobileNo,
				hospitalCodesArray);
	}

	public void insertNotification(NotifyMeDTO request) throws SQLException {
		Array hospitalCodesArray = dataSource.getConnection().createArrayOf("INTEGER",
				request.getHospitalCodes().toArray());

		jdbcTemplate.update(queryLoader.getQuery("INSERT_NOTIFICATION"), request.getStateCode(),
				request.getDistrictCode(), request.getBloodGroupCode(), request.getBloodComponentId(),
				request.getMobileNo(), request.getEmailId(), hospitalCodesArray);

	}

	public List<String> getHospitalNames(List<Integer> hospitalCodes) {
		if (hospitalCodes == null || hospitalCodes.isEmpty())
			return Collections.emptyList();

		String inSql = hospitalCodes.stream().map(String::valueOf).collect(Collectors.joining(","));

		String sql = "SELECT gstr_hospital_name || ' ' || NVL(gstr_city, '') AS hospital_display "
				+ "FROM gblt_portal_hospital_mst " + "WHERE gnum_hospital_code IN (" + inSql + ") "
				+ "AND gnum_isvalid = 1 AND hbnum_isbsu = 0 " + "ORDER BY gstr_hospital_name";

		return jdbcTemplate.queryForList(sql, String.class);
	}

	public boolean hasAlreadyNotifiedToday(String emailId, Integer hospitalCode) {
		String sql = "SELECT COUNT(*) FROM hbbt_bloodsearch_notification_log "
				+ "WHERE hbstr_email_id = ? AND hbnum_hosp_code = ? AND CAST(gdt_entry_date AS DATE) = CURRENT_DATE";

		Integer count = jdbcTemplate.queryForObject(sql, Integer.class, emailId, hospitalCode);
		return count != null && count > 0;
	}

	public void logEmailNotifications(String emailId, List<Integer> hospitalCodes) {
		String sql = "INSERT INTO hbbt_bloodsearch_notification_log (hbstr_email_id, hbnum_hosp_code, gdt_entry_date) "
				+ "VALUES (?, ?, CURRENT_TIMESTAMP)";

		for (Integer code : hospitalCodes) {
			jdbcTemplate.update(sql, emailId, code);
		}
	}

	// Check if hospital has blood/component matching requirements
	public List<Integer> findHospitalsWithRequiredBlood(NotifyMeDTO dto) {
		try {
			Array hospArray = getHospitalCodeArray(dto.getHospitalCodes());

			String sql = queryLoader.getQuery("fetch.blood.stock.availability.ForNotifyMe");

			return jdbcTemplate.queryForList(sql, Integer.class, dto.getStateCode(), dto.getDistrictCode(),
					dto.getBloodGroupCode(), dto.getBloodComponentId(), hospArray);
		} catch (SQLException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public void updateNotifyFlagToSent(String emailId, List<Integer> hospitalCodes, int stateCode, int districtCode) {
		String sql = queryLoader.getQuery("update.notify.flag.to.sent");

		Array hospArray = null;
		try {
			hospArray = getHospitalCodeArray(hospitalCodes);
		} catch (SQLException e) {
			throw new RuntimeException("Failed to create SQL array for hospital codes", e);
		}

		jdbcTemplate.update(sql, emailId, stateCode, districtCode, hospArray);
	}

	public List<Map<String, Object>> getPendingNotificationsWithin3Days() {
		String sql = queryLoader.getQuery("fetch.pending.notification.requests");

		return jdbcTemplate.queryForList(sql);
	}

}
