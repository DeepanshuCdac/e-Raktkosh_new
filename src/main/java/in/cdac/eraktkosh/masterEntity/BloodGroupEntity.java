package in.cdac.eraktkosh.masterEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "HBBT_BLDGRP_MST")
public class BloodGroupEntity {

	@Id
	private String bloodGroupName;
	private String bloodGroupCode;

	public String getBloodGroupName() {
		return bloodGroupName;
	}

	public void setBloodGroupName(String bloodGroupName) {
		this.bloodGroupName = bloodGroupName;
	}

	public String getBloodGroupCode() {
		return bloodGroupCode;
	}

	public void setBloodGroupCode(String bloodGroupCode) {
		this.bloodGroupCode = bloodGroupCode;
	}

}
