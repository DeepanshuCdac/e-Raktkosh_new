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

@Repository
public class EraktkoshPortalLoginRepository2 {

	private static final Logger logger = LoggerFactory.getLogger(EraktkoshPortalLoginRepository2.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	PortalLoginEntity portalLoginVo = new PortalLoginEntity();
	private static final String QUERY = "SELECT hbnum_eraktkosh, gnum_portal_donor_id, COALESCE(gnum_is_lock, 0) AS gnum_is_lock "
			+ "FROM hbbt_portal_donor_dtl " + "WHERE hbstr_mobile_no = ? AND gnum_isvalid = 1 " + "LIMIT 1";

	private static final String QUERY1 = "SELECT hbnum_mobileno from hbbt_portal_campdonor_dtl where hbnum_mobileno=? AND gnum_isvalid=1 limit 1";

	public boolean getPortalDonorDtlByMobileNo(String mobileNo) {
		boolean hasFlag = true;
		logger.info("Executing query for mobile number: {}", mobileNo);
		try {
			List<PortalLoginEntity> results = jdbcTemplate.query(QUERY, new Object[] { mobileNo },
					new PortalDonorDtlRowMapper());

			logger.info("Returning the resullt for : {}", mobileNo + results.toString());
			if (results.isEmpty()) {
				hasFlag = false;
			}

			else {
				hasFlag = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

		// System.out.println(hasFlag);

		return hasFlag;
	}

	@SuppressWarnings("deprecation")
	public PortalLoginEntity fetchdetailsCamp(PortalLoginEntity portalLoginVo) {

		try {
			// String query = "SELECT hbnum_mobileno FROM hbbt_portal_campdonor_dtl WHERE
			// hbnum_mobileno=? AND gnum_isvalid=1 LIMIT 1";
			return jdbcTemplate.queryForObject(QUERY1, new Object[] { portalLoginVo.getMobileno() }, (rs, rowNum) -> {
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

	class PortalDonorDtlRowMapper implements RowMapper<PortalLoginEntity> {
		@Override
		public PortalLoginEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
			boolean hasFlag = true;
			PortalLoginEntity donorDtl = new PortalLoginEntity();

			try {

				if (rs == null || !rs.next()) {
					hasFlag = false;
				}

				else {
					rs.beforeFirst();
					hasFlag = true;
					if (rs.next()) {
						donorDtl.setIsEraktKosh(rs.getString("hbnum_eraktkosh"));
						donorDtl.setDonorId(rs.getString("gnum_portal_donor_id"));

					}
				}

				donorDtl.setisValidCredentails(hasFlag);

			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			// donorDtl.setGnumIsLock(rs.getInt("gnum_is_lock"));
			return donorDtl;
		}
	}
}
