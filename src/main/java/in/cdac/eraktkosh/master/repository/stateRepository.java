package in.cdac.eraktkosh.master.repository;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;


public class stateRepository {

	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcCall simpleJdbcCall;

	@Autowired
	public stateRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;

		this.simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withSchemaName("pkg_bloodbank_mst")
				.withProcedureName("proc_bbpublic_state")
				.declareParameters(new SqlParameter("countrycode", Types.VARCHAR),
						new SqlParameter("hcode", Types.NUMERIC), new SqlParameter("eraktkoshenabled", Types.VARCHAR),
						new SqlOutParameter("err", Types.VARCHAR), new SqlOutParameter("resultSet", Types.REF_CURSOR))
				.withoutProcedureColumnMetaDataAccess();
	}

	public Map<String, Object> getStates(String countryCode, String eraktkoshEnabled) {

		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("countrycode", countryCode);
		inputParams.put("hcode", 96101);
		inputParams.put("eraktkoshenabled", eraktkoshEnabled);
		inputParams.put("err", "");
		inputParams.put("resultSet", "");

		try {
			Map<String, Object> result = simpleJdbcCall.execute(inputParams);

			ResultSet resultSet = (ResultSet) result.get("resultSet");

			return result;
		} catch (Exception error) {
			throw new RuntimeException("Error fetching state data: " + error.getMessage(), error);
		}
	}
}
