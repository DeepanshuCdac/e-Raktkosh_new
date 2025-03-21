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

    @SuppressWarnings("deprecation")
    public List<CampDetailDTO> fetchCamps(int stateCode, Integer districtCode, String startDate, String endDate) {
        String query = queryLoader.getQuery("fetch.blood_donation_camps");

        // Set districtCode default to null instead of -1 for better query handling
        int finalDistrictCode = (districtCode != null) ? districtCode : 0;

        // Ensure dates are not null
        if (startDate == null || startDate.isEmpty()) {
            startDate = java.time.LocalDate.now().toString();
        }
        if (endDate == null || endDate.isEmpty()) {
            endDate = java.time.LocalDate.now().toString();
        }

        Object[] params = {
        		stateCode, finalDistrictCode, finalDistrictCode,  startDate, endDate , 
        		stateCode, finalDistrictCode, finalDistrictCode,  startDate, endDate , 
        		stateCode, finalDistrictCode, finalDistrictCode,  startDate, endDate , 
        };

        return jdbcTemplate.query(query, params, new RowMapper<CampDetailDTO>() {
            @Override
            public CampDetailDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new CampDetailDTO(
                    rs.getString("campdate"),
                    rs.getString("camptime"),
                    rs.getString("campname"),
                    rs.getString("campvenue"),
                    rs.getString("contact"),
                    rs.getString("conductedby"),
                    rs.getString("hospName"),
                    rs.getLong("camp_reqno"),
                    rs.getInt("type"),
                    rs.getInt("is_portalregistration"),
                    rs.getString("stateName"),
                    rs.getString("distName"),
                    rs.getString("dateToSort")
                );
            }
        });
    }
}
