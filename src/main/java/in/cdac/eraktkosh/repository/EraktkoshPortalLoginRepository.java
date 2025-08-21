package in.cdac.eraktkosh.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import in.cdac.eraktkosh.dto.UpdateDonorDTO;
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

	public EraktkoshPortalLoginRepository() {
		queryLoader = new QueryLoader("query.properties");
	}

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
	public PortalLoginEntity fetchDonorDetails(String MobileNo) {
		try {

			String query = queryLoader.getQuery("query.SELECT.DONOR_DETAILS");

			return jdbcTemplate.queryForObject(query, new Object[] { MobileNo }, (rs, rowNum) -> {
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

				entity = fetchManageProfileDetails(MobileNo);
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
	public int updateDonorDetails(UpdateDonorDTO donor) {
		String query = queryLoader.getQuery("update.donor.details");

		return jdbcTemplate.update(query, donor.getFirstName(), donor.getLastName(), donor.getBloodGroupCode(),
				donor.getStateCode(), donor.getDistrictCode(), donor.getPincode(), donor.getEmail(),
				donor.getMaritalStatusCode(), donor.getSpouseName(), donor.getOccupationCode(), donor.getHouseNo(),
				donor.getLandmark(), donor.getGenderCode(), donor.getReligionCode(), donor.getAddress(),
				donor.getCityLocation(), donor.getCity(), donor.getFatherName(), donor.getDob(),
				donor.getMobileNumber());
	}

	@SuppressWarnings("deprecation")
	public PortalLoginEntity fetchManageProfileDetails(String mobileNo) {
		try {

			String query = queryLoader.getQuery("query.SELECT.DONOR_PORTAL_LOGIN_DETAILS");

			return jdbcTemplate.queryForObject(query, new Object[] { mobileNo }, (rs, rowNum) -> {
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
//				entity.setDonorPass(password);

				entity.setFatherName(rs.getString("hbstr_father_name"));
				entity.setOccupation(rs.getString("gnum_occupation_code"));
				entity.setReligion(rs.getString("gnum_religion_code"));
				entity.setMaritalStatus(rs.getString("gnum_marital_status_code"));
				entity.setSpouce(rs.getString("hbstr_spouse_name"));
				entity.setDonorCity(rs.getString("hbstr_city"));
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
