package in.cdac.eraktkosh.dto;

public class OtpCaptchaDataDTO {

	private String otp;
	private String captcha;

	public OtpCaptchaDataDTO() {
	}

	public OtpCaptchaDataDTO(String otp, String captcha) {
		this.otp = otp;
		this.captcha = captcha;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
}
