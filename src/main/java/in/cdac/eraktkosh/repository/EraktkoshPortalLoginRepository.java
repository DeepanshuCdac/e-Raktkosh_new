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

//	private static final String QUERY1 = "SELECT hbnum_mobileno from hbbt_portal_campdonor_dtl where hbnum_mobileno=? AND gnum_isvalid=1 limit 1";

//	private static final String QUERY2 = "SELECT initcap(hbstr_fname) AS hbstr_fname,hbstr_lname, hbnum_bldgrp_code,hbdt_lastlogin, hbnum_firstlogin, hbnum_demographics, hbnum_usertype,gnum_portal_donor_id,hbnum_eraktkosh, hbstr_mobile_no, hbstr_email_id,NVL(hbstr_image_string,'transactions/assets/no-image.png') AS hbstr_image_string,hbnum_login_attempt,hbnum_login_attempt_date , hbnum_donor_ref_no , hbstr_health_id, hbstr_password,hbnum_healthid_number,(SELECT INITCAP(C.GSTR_STATE_NAME) FROM GBLT_STATE_MST C WHERE C.GNUM_STATE_CODE = hbbt_portal_donor_dtl.gnum_state_code) as edonorStateName,(SELECT distinct(y.gstr_district_name) FROM gblt_district_mst y WHERE y.gnum_district_code=hbbt_portal_donor_dtl.gnum_district_code and gnum_isvalid=1) as edonorDistName,(SELECT nvl(C.gnum_lgd_state_code,0)FROM GBLT_STATE_MST C WHERE C.GNUM_STATE_CODE  =HBBT_PORTAL_DONOR_DTL.GNUM_STATE_CODE AND GNUM_ISVALID = 1) AS lgd_state_code,(SELECT nvl(Y.gnum_lgd_district_code,0)FROM GBLT_DISTRICT_MST Y	WHERE Y.GNUM_DISTRICT_CODE = HBBT_PORTAL_DONOR_DTL.GNUM_DISTRICT_CODE AND GNUM_ISVALID = 1) AS lgd_district_code FROM hbbt_portal_donor_dtl  WHERE hbstr_userid=? and gnum_isvalid=1 and hbnum_registration_mode=0";

//	private static final String PrevDonationDetailQuery = "SELECT DISTINCT(BAGNO) AS BAGNO, GDT_ENTRY_DATE, USERNAME, FROMPREVDONATIONTABLE, ORGANIZATIONNAME AS ORGANIZATIONNAME, ORGANIZATIONTYPE AS ORGANIZATIONTYPE, STATENAME AS STATENAME, DISTRICTNAME AS DISTRICTNAME, date, BLOODBANK, GNUM_PORTAL_DONOR_ID, STATE, DISTRICT, HBSTR_DONOR_CONTACT_NO, BLOODGROUPNAME AS BLOODGROUPNAME, DONORTYPE FROM ( (SELECT P.GDT_ENTRY_DATE, NVL((HBSTR_FNAME || ' ' || HBSTR_LNAME)::CHARACTER varying,(SELECT HBSTR_FNAME FROM HBBT_DONOR_DTL X,HBBT_DONOR_VISIT_DTL Y WHERE X.HBNUM_DONOR_REGNO = Y.HBNUM_DONOR_REGNO AND X.GNUM_ISVALID = 1 AND Y.GNUM_ISVALID = 1 AND HBSTR_BLDBAG_NO = P.HBNUM_DONATION_ID LIMIT 1)::CHARACTER varying) AS USERNAME, 1 AS FROMPREVDONATIONTABLE, HBSTR_BB_NAME AS ORGANIZATIONNAME, 'NA' AS ORGANIZATIONTYPE, (SELECT GSTR_STATE_NAME FROM GBLT_STATE_MST X WHERE X.GNUM_STATE_CODE = (SELECT GNUM_STATE_CODE FROM GBLT_HOSPITAL_MST D WHERE D.GNUM_HOSPITAL_CODE = P.GNUM_PORTAL_HOSPITAL_CODE AND D.GNUM_ISVALID = 1 ) AND X.GNUM_ISVALID = 1 ) AS STATENAME , (SELECT GSTR_DISTRICT_NAME FROM GBLT_DISTRICT_MST Y WHERE Y.GNUM_DISTRICT_CODE = (SELECT NUM_DIST_ID FROM GBLT_HOSPITAL_MST D WHERE D.GNUM_HOSPITAL_CODE = P.GNUM_PORTAL_HOSPITAL_CODE AND D.GNUM_ISVALID = 1 ) AND Y.GNUM_ISVALID = 1 LIMIT 1) AS DISTRICTNAME, NVL(TO_CHAR(HBDT_DONATION, 'dd-Mon-yy'), '-') AS date, P.HBSTR_BB_NAME AS BLOODBANK, P.GNUM_PORTAL_DONOR_ID:: CHARACTER varying, HBNUM_DONATION_ID AS BAGNO, (SELECT GNUM_STATE_CODE FROM GBLT_HOSPITAL_MST D WHERE D.GNUM_HOSPITAL_CODE = P.GNUM_PORTAL_HOSPITAL_CODE AND D.GNUM_ISVALID = 1 ) AS STATE , (SELECT D.NUM_DIST_ID FROM GBLT_HOSPITAL_MST D WHERE D.GNUM_HOSPITAL_CODE = P.GNUM_PORTAL_HOSPITAL_CODE AND D.GNUM_ISVALID = 1 LIMIT 1) AS DISTRICT, P.HBSTR_DONOR_CONTACT_NO, NVL(( (SELECT HBSTR_INITIALBLD_ABO || '' || DECODE(HBNUM_INITIAL_RH, 1, '+ve', 2, '-ve') FROM HBBT_DONOR_VISIT_DTL Y WHERE HBSTR_BLDBAG_NO = P.HBNUM_DONATION_ID AND GNUM_ISVALID = 1 LIMIT 1)::CHARACTER varying), (SELECT HBSTR_BLDGRP FROM HBBT_BLDGRP_MST X WHERE X.HBNUM_BLDGRP_CODE = A.HBNUM_BLDGRP_CODE )::CHARACTER VARYING) AS BLOODGROUPNAME, 0 AS DONORTYPE FROM HBBT_PORTAL_DONOR_PREVDONATION_DTL P, HBBT_PORTAL_DONOR_DTL A where ( P.GNUM_PORTAL_DONOR_ID = A. gnum_portal_donor_id ) AND A.hbstr_mobile_no=? and P.gnum_isvalid=1 and A.gnum_isvalid=1 and p.HBNUM_DONATION_ID not in (select HBSTR_BLDBAG_NO from hbbt_donor_visit_dtl v, hbbt_donor_dtl d WHERE d.HBNUM_DONOR_REGNO=v.HBNUM_DONOR_REGNO and d.gnum_isvalid=1 and v.gnum_isvalid=1 and d.hbstr_mobile_no=? ) and p.HBNUM_DONATION_ID not in (select d.HBSTR_BLDBAG_NO from HBBT_PORTAL_DONATION_DTL d WHERE d.hbstr_mobile_no=? )) UNION (SELECT B.HBDT_DONOR_VISIT_DATE, A.HBSTR_FNAME || ' ' || A.HBSTR_LNAME AS USERNAME, 0 AS FROMPREVDONATIONTABLE, 'NA' AS ORGANIZATIONNAME, 'NA' AS ORGANIZATIONTYPE, (SELECT GSTR_STATE_NAME FROM GBLT_STATE_MST X WHERE X.GNUM_STATE_CODE = A.GNUM_STATE_CODE AND GNUM_ISVALID = 1) AS STATENAME, (SELECT GSTR_DISTRICT_NAME FROM GBLT_DISTRICT_MST X WHERE X.GNUM_DISTRICT_CODE = A.GNUM_DISTRICT_CODE AND GNUM_ISVALID = 1) DISTRICTNAME, NVL(TO_CHAR(HBDT_DONOR_VISIT_DATE, 'dd-Mon-yy'), '-') AS date, (SELECT GSTR_HOSPITAL_NAME FROM GBLT_HOSPITAL_MST M WHERE M.GNUM_HOSPITAL_CODE = A.GNUM_HOSPITAL_CODE) AS BLOODBANK, '':: CHARACTER varying, HBSTR_BLDBAG_NO AS BAGNO, A.GNUM_STATE_CODE AS STATE, A.GNUM_DISTRICT_CODE AS DISTRICT, A.HBSTR_MOBILE_NO, NVL((HBSTR_INITIALBLD_ABO || '' || DECODE(HBNUM_INITIAL_RH, 1, '+ve', 2, '-ve')), (SELECT HBSTR_BLDGRP FROM HBBT_BLDGRP_MST X WHERE X.HBNUM_BLDGRP_CODE = (SELECT HBNUM_BLDGRP_CODE FROM HBBT_PORTAL_DONOR_DTL WHERE HBSTR_MOBILE_NO = ?))) AS BLOODGROUPNAME, HBNUM_DONOR_TYPE_CODE AS DONORTYPE FROM HBBT_DONOR_VISIT_DTL B, HBBT_DONOR_DTL A WHERE B.HBNUM_DONOR_REGNO = A.HBNUM_DONOR_REGNO AND B.GNUM_HOSPITAL_CODE = A.GNUM_HOSPITAL_CODE AND B.HBNUM_DONATION_STATUS IN (2, 3) AND A.HBSTR_MOBILE_NO = ? AND A.GNUM_ISVALID = 1 AND HBSTR_BLDBAG_NO IS NOT NULL ORDER BY HBDT_DONOR_VISIT_DATE DESC) UNION (SELECT DISTINCT GDT_ENTRY_DATE, HBSTR_FNAME AS USERNAME, 0 AS FROMPREVDONATIONTABLE, (SELECT HBSTR_ORGANISATION_NAME FROM HBBT_CAMP_DETAIL WHERE HBNUM_CAMP_REQ_NO = HBNUM_CAMPID AND GNUM_ISVALID = 1 UNION SELECT HBSTR_CONDUCTED_BY FROM HBBT_PORTAL_CAMP_DETAIL WHERE HBNUM_CAMP_REQ_NO = HBNUM_CAMPID AND GNUM_ISVALID = 1 ) AS ORGANIZATIONNAME, NVL( (SELECT HBNUM_ORGANISATION_TYPE_NEW FROM HBBT_CAMP_DETAIL WHERE HBNUM_CAMP_REQ_NO = B.HBNUM_CAMPID AND GNUM_ISVALID = 1 UNION SELECT HBNUM_ORGANISATION_TYPE FROM HBBT_PORTAL_CAMP_DETAIL WHERE HBNUM_CAMP_REQ_NO = B.HBNUM_CAMPID AND GNUM_ISVALID = 1 )::CHARACTER varying ,'NA')AS ORGANIZATIONTYPE, (SELECT GSTR_STATE_NAME FROM GBLT_STATE_MST WHERE GNUM_STATE_CODE IN (SELECT GNUM_STATE_CODE FROM GBLT_PORTAL_HOSPITAL_MST WHERE (GNUM_HOSPITAL_CODE = B.GNUM_HOSPITAL_CODE OR GNUM_ERAKTKOSH_HOSPITAL_CODE = B.GNUM_HOSPITAL_CODE) ) AND GNUM_ISVALID = 1 ) AS STATENAME, (SELECT GSTR_DISTRICT_NAME FROM GBLT_DISTRICT_MST WHERE GNUM_DISTRICT_CODE IN (SELECT NUM_DIST_ID FROM GBLT_PORTAL_HOSPITAL_MST WHERE (GNUM_HOSPITAL_CODE = B.GNUM_HOSPITAL_CODE OR GNUM_ERAKTKOSH_HOSPITAL_CODE = B.GNUM_HOSPITAL_CODE) ) AND GNUM_ISVALID = 1 ) AS DISTRICTNAME, NVL(TO_CHAR(B.HBDT_DONATION_DATE, 'dd-Mon-yy'), '-') AS DATE, (SELECT GSTR_HOSPITAL_NAME FROM GBLT_PORTAL_HOSPITAL_MST WHERE GNUM_HOSPITAL_CODE = B.GNUM_HOSPITAL_CODE AND GNUM_ISVALID = 1 UNION (SELECT GSTR_HOSPITAL_NAME FROM GBLT_PORTAL_HOSPITAL_MST WHERE GNUM_ERAKTKOSH_HOSPITAL_CODE = B.GNUM_HOSPITAL_CODE AND GNUM_ISVALID = 1 )) AS BLOODBANK, B.GNUM_PORTAL_DONOR_ID :: CHARACTER VARYING ,B.HBSTR_BLDBAG_NO AS BAGNO, (SELECT GNUM_STATE_CODE FROM GBLT_PORTAL_HOSPITAL_MST WHERE (GNUM_HOSPITAL_CODE = B.GNUM_HOSPITAL_CODE OR GNUM_ERAKTKOSH_HOSPITAL_CODE = B.GNUM_HOSPITAL_CODE) AND GNUM_ISVALID = 1 ) AS STATE, B.GNUM_DISTRICT_CODE AS DISTRICT, B.HBSTR_MOBILE_NO, (SELECT HBSTR_BLDGRP FROM HBBT_BLDGRP_MST X WHERE B.HBNUM_BLDGRP_CODE = X.HBNUM_BLDGRP_CODE AND GNUM_ISVALID = 1 LIMIT 1) AS BLOODGROUPNAME, HBNUM_DONOR_TYPE AS DONORTYPE FROM HBBT_PORTAL_DONATION_DTL B WHERE B.HBSTR_MOBILE_NO = ? AND B.HBSTR_BLDBAG_NO IS NOT NULL AND B.GNUM_ISVALID = 1 )) ORDER BY GDT_ENTRY_DATE DESC";

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

				entity = fetchManageProfileDetails(MobileNo, rs.getString("hbstr_password"));
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
		System.out.println("city: " + portalLoginEntity.getEdonorCity());
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
			ps.setString(15, portalLoginEntity.getEdonorCity());
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
