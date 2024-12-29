package in.cdac.eraktkosh.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import in.cdac.eraktkosh.entity.PortalLoginEntity;
import in.cdac.eraktkosh.services.QueryLoader;

@Repository
public class EraktkoshPortalLoginRepository {

	private static final Logger logger = LoggerFactory.getLogger(EraktkoshPortalLoginRepository.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	PortalLoginEntity portalLoginVo = new PortalLoginEntity();
	private static final String QUERY = "SELECT hbnum_eraktkosh, gnum_portal_donor_id, COALESCE(gnum_is_lock, 0) AS gnum_is_lock "
			+ "FROM hbbt_portal_donor_dtl " + "WHERE hbstr_mobile_no = ? AND gnum_isvalid = 1 " + "LIMIT 1";

	private final QueryLoader queryLoader;

	// to find all the query from the property file
	public EraktkoshPortalLoginRepository() {
		queryLoader = new QueryLoader("query.properties"); 
	}
	// end


	@SuppressWarnings("deprecation")
	public boolean getPortalDonorDtlByMobileNo(String mobileNo) {
		boolean hasFlag = true;
		logger.info("Executing query for mobile number: {}", mobileNo);
		try {
			List<PortalLoginEntity> results = jdbcTemplate.query(QUERY, new Object[] { mobileNo },
					new PortalDonorDtlRowMapper());

			logger.info("Returning the result for : {}", mobileNo + results.toString());
			if (results.isEmpty()) {
				hasFlag = false; 
			}

			else {
				hasFlag = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return hasFlag;
	}

	@SuppressWarnings("deprecation")
	public PortalLoginEntity fetchdetailsCamp(PortalLoginEntity portalLoginVo) {

		try {
			
			 String query = queryLoader.getQuery("query.SELECT_CAMP_DONOR_DETAIL");
			 
			return jdbcTemplate.queryForObject(query, new Object[] { portalLoginVo.getMobileno() }, (rs, rowNum) -> {
				PortalLoginEntity entity = new PortalLoginEntity();
				boolean hasFlag = true;
				if (rs == null || !rs.next()) {
					hasFlag = false;
				} else {
					rs.beforeFirst();
					hasFlag = true;
					entity.setIsEraktKosh("0");
					entity.setPortalDonorId(rs.getString(1));
				}

				entity.setisValidCredentails(hasFlag);
				return entity;
			});
		} catch (EmptyResultDataAccessException e) {
			logger.info("No results found for mobile number: {}", portalLoginVo.getMobileno());
			return null;
		} catch (Exception e) {
			logger.error("Error executing query", e);
			return null;
		}
	}

	@SuppressWarnings("deprecation")
	public PortalLoginEntity fetchDonorDetails(String mobileNo) {
	    try {
	        String query = queryLoader.getQuery("query.SELECT.DONOR_DETAILS");

	        return jdbcTemplate.queryForObject(query, new Object[]{mobileNo}, (rs, rowNum) -> {
	            PortalLoginEntity entity = new PortalLoginEntity();
	            entity.setEdonorFName(rs.getString("hbstr_fname"));
	            entity.setEdonorLName(rs.getString("hbstr_lname"));
	            entity.setBloodGroup(rs.getString("hbnum_bldgrp_code"));
	            entity.setIsLastLogin(rs.getString("hbdt_lastlogin"));
	            entity.setIsFirstLogin(rs.getString("hbnum_firstlogin"));
	            entity.setUserType(rs.getString("hbnum_usertype"));
	            entity.setPortalDonorId(rs.getString("gnum_portal_donor_id"));
	            entity.setIsEraktKosh(rs.getString("hbnum_eraktkosh"));
	            entity.setMobileno(rs.getString("hbstr_mobile_no"));
	            entity.setEdonorEmail(rs.getString("hbstr_email_id"));
	            entity.setBase64Image(rs.getString("hbstr_image_string"));
	            entity.setLastLoginAttemptCounter(rs.getString("hbnum_login_attempt"));
	            entity.setLastLoginAttemptDate(rs.getString("hbnum_login_attempt_date"));
	            entity.setSourceRefNo(rs.getString("hbnum_donor_ref_no"));
	            entity.setHealthId(rs.getString("hbstr_health_id"));
	            entity.setDonorPass(rs.getString("hbstr_password"));
	            entity.setHealthId(rs.getString("hbnum_healthid_number"));
	            entity.setEdonorStateName(rs.getString("edonorStateName"));
	            entity.setEdonorDistName(rs.getString("edonorDistName"));
	            entity.setLgd_state_code(rs.getString("lgd_state_code"));
	            entity.setLgd_district_code(rs.getString("lgd_district_code"));
	            entity.setisValidCredentails(true);

	            // Fetch additional profile details if necessary
	            entity = fetchManageProfileDetails(mobileNo, rs.getString("hbstr_password"));

	            return entity;
	        });
	    } catch (EmptyResultDataAccessException e) {
	        logger.info("No results found for mobile number: {}", mobileNo);
	        return null;
	    } catch (Exception e) {
	        logger.error("Error executing query", e);
	        return null;
	    }
	}

	/**
	 * Updates the last login timestamp.
	 */
	public void updateLastLoginTimestamp(String mobileNo, String password) {
	    try {
	        String updateQuery = queryLoader.getQuery("query.UPDATE.LAST.LOGIN.TIMESTAMP");
	        int rowsUpdated = jdbcTemplate.update(updateQuery, mobileNo, password);

	        if (rowsUpdated > 0) {
	            System.out.println("Last login updated successfully");
	        } else {
	            System.out.println("No rows updated for last login timestamp");
	        }
	    } catch (Exception e) {
	        logger.error("Error updating last login timestamp", e);
	    }
	}

	@SuppressWarnings("deprecation")
	public List<PortalLoginEntity> getPrevDonationDetailsByMobile(String mobileNo) {
		try {
			String query = queryLoader.getQuery("query.PREV_DONATION_DETAIL_QUERY");
			
			logger.info("Fetching previous donation details for mobile number: {}", mobileNo);
			return jdbcTemplate.query(query,
					new Object[] { mobileNo, mobileNo, mobileNo, mobileNo, mobileNo, mobileNo }, (rs, rowNum) -> {
						PortalLoginEntity prevDonation = new PortalLoginEntity();
						prevDonation.setBagNo(rs.getString("BAGNO"));
						prevDonation.setUsername(rs.getString("USERNAME"));
						prevDonation.setOrganizationName(rs.getString("ORGANIZATIONNAME"));
						prevDonation.setOrganizationType(rs.getString("ORGANIZATIONTYPE"));
						prevDonation.setStateName(rs.getString("STATENAME"));
						prevDonation.setDistrictName(rs.getString("DISTRICTNAME"));
						prevDonation.setDate(rs.getString("date"));
						prevDonation.setBloodbank(rs.getString("BLOODBANK"));
						prevDonation.setBloodGroupName(rs.getString("BLOODGROUPNAME"));
						return prevDonation;
					});
		} catch (Exception e) {
			logger.error("Error fetching previous donation details for mobile number: {}", mobileNo, e);
			return null;
		}
	}

	class PortalDonorDtlRowMapper implements RowMapper<PortalLoginEntity> {
		@Override
		public PortalLoginEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
			PortalLoginEntity donorDtl = new PortalLoginEntity();

			if (rs.next()) {
				donorDtl.setIsEraktKosh(rs.getString("hbnum_eraktkosh"));
				donorDtl.setDonorId(rs.getString("gnum_portal_donor_id"));
			}

			donorDtl.setisValidCredentails(rs.next());
			return donorDtl;
		}
	}

	public int getOtpCount(String mobileNumber) {
		String query = queryLoader.getQuery("query.GET_OTP_COUNT");
		int count = 0;
		try {
			count = jdbcTemplate.queryForObject(query, Integer.class, mobileNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	public String insertOtpCount(String mobileNumber) {
		String query = queryLoader.getQuery("query.INSERT.OTP.COUNT");
		jdbcTemplate.update(query, mobileNumber);
		return query;
	}

	public String getPreviousOtpTimeStamp(String mobileNumber) {
		String query = queryLoader.getQuery("query.SELECT.OTP.TIME");
		String timeStamp = null;
		try {
			List<String> result = jdbcTemplate.queryForList(query, String.class, mobileNumber);
			timeStamp = result.isEmpty() ? null : result.get(0);
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
		}
		return timeStamp;
	}
	
	
//	update donor details in manage profile section...
	public void updateDonorDetails(PortalLoginEntity portalLoginEntity) {
        String query = queryLoader.getQuery("query.INSERT.DONOR.DETAILS");
        System.out.println("First Name: " + portalLoginEntity.getEdonorFName());
        System.out.println("Last Name: " + portalLoginEntity.getEdonorLName());
        System.out.println("dob: " + portalLoginEntity.getDob());
        System.out.println("gender: " + portalLoginEntity.getGender());
        System.out.println("Email: " + portalLoginEntity.getEdonorEmail());
        System.out.println("bldgroup: " + portalLoginEntity.getBloodGroupName());
        System.out.println("father: " + portalLoginEntity.getFatherName());
        System.out.println("marriage: " + portalLoginEntity.getMaritalStatus());
        System.out.println("Spouse: " + portalLoginEntity.getSpouce());
        System.out.println("occupation: " + portalLoginEntity.getOccupation());
        System.out.println("religion: " + portalLoginEntity.getReligion());
        System.out.println("HNo: " + portalLoginEntity.getHno());
        System.out.println("address: " + portalLoginEntity.getAddress());
        System.out.println("Location: " + portalLoginEntity.getLocation());
        System.out.println("city: " + portalLoginEntity.getDonorCity());
        System.out.println("district: " + portalLoginEntity.getEdonorDistName());
        System.out.println("state: " + portalLoginEntity.getEdonorStateName());
        System.out.println("PinCode: " + portalLoginEntity.getDonorPin());
        System.out.println("Landmark: " + portalLoginEntity.getLandmark());
        System.out.println("Donor ID: " + portalLoginEntity.getPortalDonorId());
        System.out.println("Password: " + portalLoginEntity.getEdonorPass());

        jdbcTemplate.update(query, ps -> {
            ps.setString(1, portalLoginEntity.getEdonorFName());             
            ps.setString(2, portalLoginEntity.getEdonorLName()); 
            ps.setString(3, portalLoginEntity.getDob());
            ps.setString(4, portalLoginEntity.getGender());
            ps.setString(5, portalLoginEntity.getEdonorEmail()); 
            ps.setString(6, portalLoginEntity.getBloodGroupName()); 
            ps.setString(7, portalLoginEntity.getFatherName()); 
            ps.setString(8, portalLoginEntity.getMaritalStatus()); 
            ps.setString(9, portalLoginEntity.getSpouce()); 
            ps.setString(10, portalLoginEntity.getOccupation());  
            ps.setString(11, portalLoginEntity.getReligion());  
            ps.setString(12, portalLoginEntity.getHno()); 
            ps.setString(13, portalLoginEntity.getAddress()); 
            ps.setString(14, portalLoginEntity.getLocation()); 
            ps.setString(15, portalLoginEntity.getDonorCity());                 
            ps.setString(16, portalLoginEntity.getEdonorDistName());                 
            ps.setString(17, portalLoginEntity.getEdonorStateName());                 
            ps.setString(18, portalLoginEntity.getDonorPin()); 
            ps.setString(19, portalLoginEntity.getLandmark()); 
            ps.setString(20, portalLoginEntity.getPortalDonorId());              
            ps.setString(21, portalLoginEntity.getEdonorPass());     
            
        });
	}
	
	@SuppressWarnings("deprecation")
	public PortalLoginEntity fetchManageProfileDetails(String mobileNo, String password) {
	    try {
	    	
	        String query = queryLoader.getQuery("query.SELECT.DONOR_PORTAL_LOGIN_DETAILS");
	        
	        return jdbcTemplate.queryForObject(query, new Object[] { mobileNo, password }, (rs, rowNum) -> {
	            PortalLoginEntity entity = new PortalLoginEntity();
	            
	            entity.setEdonorFName(rs.getString("hbstr_fname"));
	            entity.setEdonorLName(rs.getString("hbstr_lname"));
	            entity.setBloodGroup(rs.getString("hbnum_bldgrp_code"));
	            entity.setIsLastLogin(rs.getString("hbdt_lastlogin"));
	            entity.setIsFirstLogin(rs.getString("hbnum_firstlogin"));
	            entity.setUserType(rs.getString("hbnum_usertype"));
	            entity.setMobileno(rs.getString("hbstr_mobile_no"));
	            entity.setGender(rs.getString("gnum_gender_code"));
	            entity.setBloodGroupName(rs.getString("hbnum_bldgrp_code"));
	            
	            
	            entity.setEdonorEmail(rs.getString("hbstr_email_id"));
	            entity.setEdonorStateName(rs.getString("gnum_state_code"));
	            entity.setEdonorDistName(rs.getString("gnum_district_code"));
	            entity.setDob(rs.getString("hbdt_dob"));
	            entity.setDonorPass(password);  
	            
	            entity.setFatherName(rs.getString("hbstr_father_name"));
	            entity.setOccupation(rs.getString("gnum_occupation_code"));
	            entity.setReligion(rs.getString("gnum_religion_code"));
	            entity.setMaritalStatus(rs.getString("gnum_marital_status_code"));
	            entity.setSpouce(rs.getString("hbstr_spouse_name"));
	            entity.setDonorCity(rs.getString("hbstr_city"));
//	            entity.setEdonorCity(rs.getString("hbstr_city"));
	            entity.setAddress(rs.getString("hbstr_addr1"));
	           entity.setLocation(rs.getString("hbstr_city_location"));
	            entity.setHno(rs.getString("hbstr_houesno"));
	            entity.setLandmark(rs.getString("hbstr_landmark"));
	            entity.setDonorPin(rs.getString("hbnum_pincode"));
	            
	            entity.setisValidCredentails(true);
	            return entity;
	        });
	    } catch (EmptyResultDataAccessException e) {
	        logger.info("No results found for mobile number: {}", mobileNo);
	        return null;
	    } catch (Exception e) {
	        logger.error("Error executing query", e);
	        return null;
	    }
	}

}
