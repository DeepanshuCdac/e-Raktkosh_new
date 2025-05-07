package in.cdac.eraktkosh.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import in.cdac.eraktkosh.dto.DonorResponseDTO;
import in.cdac.eraktkosh.services.QueryLoader;

@Repository
public class DonorResponseRepository {
	
	 @Autowired
	    private JdbcTemplate jdbcTemplate;

	    @Autowired
	    private QueryLoader queryLoader;

	    public void updateResponse(String email, String campReqNo, Integer isResponse) {
	        jdbcTemplate.update(queryLoader.getQuery("update.donor.response.query"),
	                isResponse, email, campReqNo);
	    }
	    
//	    public void insertEmailLog(String email, String campReqNo) {
//	        jdbcTemplate.update(queryLoader.getQuery("insert.email.log.entry"), email, campReqNo);
//	    }


}
