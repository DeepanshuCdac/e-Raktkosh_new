package in.cdac.eraktkosh.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import in.cdac.eraktkosh.dto.CampDonorRegisterDTO;
import in.cdac.eraktkosh.services.QueryLoader;

@Repository
public class CampDonorRegisterRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private QueryLoader queryLoader;

	@SuppressWarnings("deprecation")
	public List<CampDonorRegisterDTO> getDonorsByMobile(String mobile) {
		String query = queryLoader.getQuery("fetch.donor.details");

		return jdbcTemplate.query(query, new Object[] { mobile }, new RowMapper<CampDonorRegisterDTO>() {
			@Override
			public CampDonorRegisterDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
				CampDonorRegisterDTO dto = new CampDonorRegisterDTO();
				dto.setUsername(rs.getString("username"));
				dto.setGendercode(rs.getString("gendercode"));
				dto.setAge(rs.getString("age"));
				dto.setFathername(rs.getString("fathername"));
				dto.setMobileno(rs.getString("mobileno"));
				dto.setEmail(rs.getString("email"));
				dto.setAddress(rs.getString("address"));
				dto.setPincode(rs.getInt("pincode"));
				dto.setDistrictcode(rs.getInt("districtcode"));
				dto.setStatecode(rs.getInt("statecode"));
				return dto;
			}
		});
	}

}
