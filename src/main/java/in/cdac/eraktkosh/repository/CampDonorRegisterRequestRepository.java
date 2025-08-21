package in.cdac.eraktkosh.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import in.cdac.eraktkosh.dto.CampDonorRegisterRequestDTO;
import in.cdac.eraktkosh.services.QueryLoader;

@Repository
public class CampDonorRegisterRequestRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private QueryLoader queryLoader;

	public boolean isDonorAlreadyRegistered(Long campId, String mobileNo) {
		String query = queryLoader.getQuery("check.camp.donor.exists");
		String result = jdbcTemplate.queryForObject(query, new Object[] { campId, mobileNo }, String.class);
		return "1".equals(result);
	}

	public void archiveAndDeleteExistingDonor(Long campId, String mobileNo) {
		jdbcTemplate.update(queryLoader.getQuery("insert.campdonor.auditlog"), campId, mobileNo);
		jdbcTemplate.update(queryLoader.getQuery("delete.camp.donor"), campId, mobileNo);
	}

	public boolean isPortalDonorExists(String mobileNo) {
		String query = queryLoader.getQuery("check.portal.donor.exists");
		String result = jdbcTemplate.queryForObject(query, new Object[] { mobileNo }, String.class);
		return "1".equals(result);
	}

	public void insertPortalDonor(CampDonorRegisterRequestDTO req) {
		jdbcTemplate.update(queryLoader.getQuery("insert.portal.donor"), req.getMobileNo(), // 1 - gnum_portal_donor_id
				req.getMobileNo(), // 2 - hbstr_userid
				null, // 3 - hbnum_donor_ref_no
				req.getName(), // 4 - hbstr_fname
				req.getGenderCode(), // 5 - gnum_gender_code
				req.getDob(), // 6 - hbdt_dob
				req.getBloodGroupCode(), // 7 - hbnum_bldgrp_code
				req.getMobileNo(), // 8 - hbstr_mobile_no
				null, // 9 - hbnum_source_ref_no
				req.getSource(), // 10 - hbnum_source
				req.getAddress(), // 11 - hbstr_addr1
				req.getDistrictCode(), // 12 - gnum_district_code
				req.getStateCode(), // 13 - gnum_state_code
				req.getCampId(), // 14 - 1st subquery param
				req.getCampId(), // 15 - 2nd subquery param
				req.getHealthIdNumber(), // 16 - hbnum_healthid_number
				req.getHealthId(), // 17 - hbstr_health_id
				req.getPassword(), // 18 - hbstr_password
				req.getEmail(), req.getPinCode(), req.getFatherName());
	}

	public Long getDonorSequenceNumber() {
		return jdbcTemplate.queryForObject(queryLoader.getQuery("get.donor.seqno"), Long.class);
	}

	public void insertCampDonor(CampDonorRegisterRequestDTO req, Long seqNo) {
		jdbcTemplate.update(queryLoader.getQuery("insert.camp.donor"), req.getFatherName(), req.getAge(),
				req.getEmpId(), req.getName(), req.getDob(), req.getGenderCode(), req.getAddress(), req.getCity(),
				req.getStateCode(), req.getDistrictCode(), req.getPinCode(), req.getEmail(), req.getCampId(),
				req.getCampDate(), seqNo, req.getMobileNo(), req.getCampSource(), req.getIsBloodBankRegister(),
				req.getSource(), seqNo);
	}
}
