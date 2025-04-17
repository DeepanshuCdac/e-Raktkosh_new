package in.cdac.eraktkosh.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import in.cdac.eraktkosh.dto.HospitalDTO;
import in.cdac.eraktkosh.services.QueryLoader;

import java.util.List;

@Repository
public class HospitalRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private QueryLoader queryLoader;

    private final RowMapper<HospitalDTO> hospitalMapper = (rs, rowNum) -> 
        new HospitalDTO(
            rs.getInt(1),
            rs.getString(2),
            rs.getLong(3)
        );

    public List<HospitalDTO> findByStateAndDistrict(int stateCode, int districtId) {
        String sql = queryLoader.getQuery("get.hospital.byStateDistrict");
        return jdbcTemplate.query(sql, hospitalMapper, stateCode, districtId);
    }
}
