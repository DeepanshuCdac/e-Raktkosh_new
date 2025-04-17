package in.cdac.eraktkosh.repository;

import in.cdac.eraktkosh.dto.SubscribeDonorDTO;
import in.cdac.eraktkosh.services.QueryLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SubscribeDonorRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private QueryLoader queryLoader;

    // Check if email already exists
    public boolean existsByEmail(String email) {
        String query = queryLoader.getQuery("check.duplicate.email");
        Integer count = jdbcTemplate.queryForObject(query, Integer.class, email);
        return count != null && count > 0;
    }

    // Get the next available serial number (uniqueKey)
    public Long getNextSerialNo() {
        String query = queryLoader.getQuery("get.max.serialNo");  
        Long maxSerialNo = jdbcTemplate.queryForObject(query, Long.class);
        return (maxSerialNo != null ? maxSerialNo + 1 : 1);  
    }

 // Save user with generated serial number including pincode
    public int saveUser(SubscribeDonorDTO donorDTO) {
        String query = queryLoader.getQuery("insert.subscribe.donor");

        Long generatedSerialNo = getNextSerialNo(); 

        return jdbcTemplate.update(query,
                generatedSerialNo, 
                donorDTO.getStateCode(),
                donorDTO.getDistrictCode(),
                0,
                donorDTO.getMobileNo() != null ? donorDTO.getMobileNo() : null,
                donorDTO.getEmail()
        );
    }

    // Check if serial number exists
    public boolean existsBySerialNo(Long serialNo) {
        String query = queryLoader.getQuery("check.unsubscribe.status");

        try {
            Integer flag = jdbcTemplate.queryForObject(query, Integer.class, serialNo);
            return flag != null;  
        } catch (EmptyResultDataAccessException e) {
            return false;  
        }
    }

    // Update unsubscribe_flag to 1 (unsubscribe)
    public String unsubscribeUserByEmail(String email) {
        // Check if the email exists in the database
        Integer unsubscribeFlag = getUnsubscribeFlagByEmail(email);

        if (unsubscribeFlag == null) {
            return "Error: Email not found!";
        }

        if (unsubscribeFlag == 1) {
            return "User is already unsubscribed";
        }

        // Update unsubscribe_flag to 1 using email
        String updateQuery = queryLoader.getQuery("update.unsubscribe.flag.by.email");
        int rowsUpdated = jdbcTemplate.update(updateQuery, 1, email);

        return rowsUpdated > 0 ? "Successfully unsubscribed" : "Unsubscription failed";
    }


    // Fetch emails of subscribed users
    public List<String> getSubscribedEmails() {
        String query = queryLoader.getQuery("fetch.subscribed.users");
        return jdbcTemplate.queryForList(query, String.class);
    }

    // Get email by serial number
    public String getEmailBySerialNo(Long serialNo) {
        String query = queryLoader.getQuery("get.email.by.serialNo");

        try {
            return jdbcTemplate.queryForObject(query, String.class, serialNo);
        } catch (EmptyResultDataAccessException e) {
            return null;  
        }
    }
    
 // Get serial number by email
    public Long getSerialNoByEmail(String email) {
        String query = queryLoader.getQuery("get.serialNo.by.email");

        try {
            return jdbcTemplate.queryForObject(query, Long.class, email);
        } catch (EmptyResultDataAccessException e) {
            return null; 
        }
    }


    // Check if an email is unsubscribed
    public Integer getUnsubscribeFlagByEmail(String email) {
        String query = queryLoader.getQuery("check.unsubscribe.flag.by.email");

        try {
            return jdbcTemplate.queryForObject(query, Integer.class, email);
        } catch (EmptyResultDataAccessException e) {
            return null;  
        }
    }

    // Re-subscribe a user by updating unsubscribe_flag to 0
    public int reSubscribeUser(SubscribeDonorDTO donorDTO) {
        String query = queryLoader.getQuery("update.resubscribe.flag");

        return jdbcTemplate.update(query,
                donorDTO.getStateCode(),
                donorDTO.getDistrictCode(),
                donorDTO.getMobileNo() != null ? donorDTO.getMobileNo() : null,
                0,  
                donorDTO.getEmail()  
        );
    }
}
