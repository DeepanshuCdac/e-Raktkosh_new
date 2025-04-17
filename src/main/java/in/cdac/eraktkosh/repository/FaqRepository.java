package in.cdac.eraktkosh.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import in.cdac.eraktkosh.dto.FaqDTO;
import in.cdac.eraktkosh.services.QueryLoader;

@Repository
public class FaqRepository {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private QueryLoader queryLoader;

    private final RowMapper<FaqDTO> faqRowMapper = (rs, rowNum) -> {
    	FaqDTO dto = new FaqDTO();
        dto.setFaqQuestion(rs.getString("faqQuestion"));
        dto.setFaqAnswer(rs.getString("faqAnswer"));
        return dto;
    };

    public List<FaqDTO> getAllFaqs() {
        String query = queryLoader.getQuery("get.faq.data");
        return jdbcTemplate.query(query, faqRowMapper);
    }

}
