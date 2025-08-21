package in.cdac.eraktkosh.repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import in.cdac.eraktkosh.dto.SearchLogResponseDTO;
import in.cdac.eraktkosh.services.QueryLoader;

@Repository
public class SearchLogRepository {

	private final JdbcTemplate jdbcTemplate;
	private final QueryLoader queryLoader;
	private final SimpleJdbcInsert simpleJdbcInsert;

	@Autowired
	public SearchLogRepository(JdbcTemplate jdbcTemplate, QueryLoader queryLoader) {
		this.jdbcTemplate = jdbcTemplate;
		this.queryLoader = queryLoader;
		this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("hbbt_eraktkosh_service_search_log")
				.usingGeneratedKeyColumns("hbnum_log_id")
				.usingColumns("hbstr_service_type", "hbstr_search_json", "hbnum_ipaddress");
	}

	@SuppressWarnings("deprecation")
	public SearchLogResponseDTO saveSearchLog(String serviceType, String searchJson, String ipAddress) {
		try {
			if (serviceType == null || serviceType.isEmpty()) {
				throw new IllegalArgumentException("Service type cannot be null or empty");
			}

			String finalIpAddress = (ipAddress != null) ? ipAddress : "unknown";

			System.out.println("Attempting to insert record with:");
			System.out.println("Service Type: " + serviceType);
			System.out.println("Search JSON: " + searchJson);
			System.out.println("IP Address: " + finalIpAddress);

			Map<String, Object> parameters = new HashMap<>();
			parameters.put("hbstr_service_type", serviceType);
			parameters.put("hbstr_search_json", searchJson);
			parameters.put("hbnum_ipaddress", finalIpAddress);

			Number logId = simpleJdbcInsert.executeAndReturnKey(parameters);
			System.out.println("Insert successful, generated ID: " + logId);

			String fetchSql = queryLoader.getQuery("fetch.SEARCH_LOG_BY_ID");
			System.out.println("Fetching timestamp with query: " + fetchSql);

			return jdbcTemplate.queryForObject(fetchSql, new Object[] { logId }, (rs, rowNum) -> {
				LocalDateTime timestamp = rs.getTimestamp("gdt_entry_date").toLocalDateTime();
				System.out.println("Retrieved timestamp: " + timestamp);
				return new SearchLogResponseDTO("success", rs.getLong("hbnum_log_id"), timestamp);
			});
		} catch (Exception e) {
			System.err.println("Error saving search log:");
			e.printStackTrace();
			throw e;
		}
	}

	@SuppressWarnings("deprecation")
	public List<SearchLogResponseDTO> getSearchLogsByServiceType(String serviceType) {
		String sql = queryLoader.getQuery("fetch.SEARCH_LOGS_BY_SERVICE");

		return jdbcTemplate.query(sql, new Object[] { serviceType }, searchLogRowMapper);
	}

	private final RowMapper<SearchLogResponseDTO> searchLogRowMapper = (rs, rowNum) -> new SearchLogResponseDTO(
			"success", rs.getLong("hbnum_log_id"), rs.getTimestamp("gdt_entry_date").toLocalDateTime());
}