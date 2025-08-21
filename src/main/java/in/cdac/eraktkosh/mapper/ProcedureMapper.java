package in.cdac.eraktkosh.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import in.cdac.eraktkosh.dto.CampNotificationDTO;

public class ProcedureMapper implements RowMapper<ResultSet> {
	@Override
	public ResultSet mapRow(ResultSet rs, int rowNum) throws SQLException {
		CampNotificationDTO dto = new CampNotificationDTO();
		String error = rs.getString("err");
		ResultSet rs1 = null;
		if (error == null || error.trim().equals("")) {
			// rs1 = rs.getResultSet("resultset");
		} else {
			System.out.println("error: " + error);
		}
		return rs1;
	}
}
