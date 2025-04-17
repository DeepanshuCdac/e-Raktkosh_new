package in.cdac.eraktkosh.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import in.cdac.eraktkosh.dto.BloodAvailabilityDTO;
import in.cdac.eraktkosh.services.QueryLoader;

@Repository
public class BloodAvailabilityRepository {

    private final JdbcTemplate jdbcTemplate;
    private final QueryLoader queryLoader;

    @Autowired
    public BloodAvailabilityRepository(JdbcTemplate jdbcTemplate, QueryLoader queryLoader) {
        this.jdbcTemplate = jdbcTemplate;
        this.queryLoader = queryLoader;
    }

    @SuppressWarnings("deprecation")
    public List<BloodAvailabilityDTO> fetchBloodAvailability(Integer stateCode, Integer districtId, Integer componentId, Integer bloodGroupId) {
        String query = queryLoader.getQuery("fetch.blood.availability");

        if (componentId == null) {
            componentId = 11;
        }

        if (districtId == null) {
            query = query.replaceFirst("AND num_dist_id = \\?", "AND 1=1");
            query = query.replaceFirst("AND num_dist_id = \\?", "AND 1=1"); 
        }
        if (bloodGroupId == null) {
            query = query.replaceFirst("AND hbnum_bld_grp_id = \\?", "AND 1=1");
            query = query.replaceFirst("AND a.hbnum_bld_grp_id = \\?", "AND 1=1"); 
        }

        List<Object> params = new ArrayList<>();
        params.add(stateCode);
        if (districtId != null) {
            params.add(districtId);
        }
        params.add(componentId);
        if (bloodGroupId != null) {
            params.add(bloodGroupId);
        }
        params.add(stateCode);
        if (districtId != null) {
            params.add(districtId);
        }
        params.add(componentId);
        if (bloodGroupId != null) {
            params.add(bloodGroupId);
        }
        return jdbcTemplate.query(query, params.toArray(), getRowMapper());
    }

    private RowMapper<BloodAvailabilityDTO> getRowMapper() {
        return new RowMapper<BloodAvailabilityDTO>() {
            @Override
            public BloodAvailabilityDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                BloodAvailabilityDTO dto = new BloodAvailabilityDTO();
                dto.setBldgrpcode1(rs.getString("bldgrpcode1"));
                dto.setCompId(rs.getString("compId"));
                dto.setCount(rs.getInt("count"));
                dto.setHospitalname(rs.getString("hospitalname"));
                dto.setHospitaladd(rs.getString("hospitaladd"));
                dto.setHospitalcontact(rs.getString("hospitalcontact"));
                dto.setHospitalCode(rs.getString("hospitalCode"));
                dto.setModerateStock(rs.getString("moderateStock"));
                dto.setHospitalType(rs.getString("hospitalType"));
                dto.setEntrydate(rs.getString("entrydate"));
                dto.setOffline(rs.getString("offline"));
                dto.setType(rs.getString("type"));
                return dto;
            }
        };
    }
}
