package in.cdac.eraktkosh.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class SendMessageToUser {

	public static void SendOTP(String message, String contactNumber) {

		SMSConfig objSMSConfig = new SMSConfig();
		final String sms_username = objSMSConfig.sms_username;
		final String sms_password = objSMSConfig.sms_password;
		final String sms_senderId = objSMSConfig.sms_senderId;
		final String sms_url = objSMSConfig.sms_url;
		final String contactNo = contactNumber;
		final String messageBody = message;
		new Thread(new Runnable() {
			public void run() {
				try {
					sendOTPThroughSMSGateway(sms_username, sms_password, sms_senderId, sms_url, contactNo, messageBody);
				} catch (Exception e) {
					System.out.println("error in thread");
					e.printStackTrace();
				}
			}
		}).start();

	}

	public static int sendOTPThroughSMSGateway(final String username, final String password, final String senderId,
			final String sms_URL, String mobileNumber, String message) {
		try {
			if (mobileNumber == null || mobileNumber.equals(""))
				return 0;
			if (message == null || message.isEmpty())
				return 0;

			String secureKey = SMSConfig.secureKey;
			sendForgetOtpSMS(username, password, message, senderId, mobileNumber, secureKey);

			return 0;
		} catch (Error e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 200;
	}

	public static String sendForgetOtpSMS(String username, String password, String message, String senderId,
			String mobileNumber, String secureKey) {

		String responseString = "";
		SSLSocketFactory sf = null;
		SSLContext context = null;
		String encryptedPassword;
		try {
			secureKey = SMSConfig.secureKey;

			System.setProperty("https.protocols", "SSLv3,TLSv1,TLSv1.1,TLSv1.2");
			context = SSLContext.getInstance("TLSv1.2");
			String version = System.getProperty("java.version");

			System.out.println("==============java version" + version);
			TrustManager[] certs = new TrustManager[] { new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String t) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String t) {
				}
			} };
			context.init(null, certs, new SecureRandom());
			// context.init(null, null, null);
			sf = new SSLSocketFactory(context, SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
			Scheme scheme = new Scheme("https", 443, sf);
			HttpClient client = new DefaultHttpClient();
			client.getConnectionManager().getSchemeRegistry().register(scheme);
			HttpPost post = new HttpPost(SMSConfig.sms_url);
			System.out.println("SMSConfig.sms_url : " + SMSConfig.sms_url);
			encryptedPassword = MD5(password);
			System.out.println(encryptedPassword);
			message = message.trim();
			String genratedhashKey = hashGenerator(username, senderId, message, secureKey);

			// System.out.println("genratedhashKey "+ genratedhashKey);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("mobileno", mobileNumber));
			nameValuePairs.add(new BasicNameValuePair("senderid", senderId));
			nameValuePairs.add(new BasicNameValuePair("content", message));
			nameValuePairs.add(new BasicNameValuePair("smsservicetype", "otpmsg"));
			nameValuePairs.add(new BasicNameValuePair("username", username));
			nameValuePairs.add(new BasicNameValuePair("password", encryptedPassword));
			nameValuePairs.add(new BasicNameValuePair("key", genratedhashKey));
			nameValuePairs.add(new BasicNameValuePair("templateid", SMSConfig.templateidotp));
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = client.execute(post);
			BufferedReader bf = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";

			while ((line = bf.readLine()) != null) {
				responseString = responseString + line;

			}
			System.out.println(responseString);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseString;

	}

	protected static String hashGenerator(String userName, String senderId, String content, String secureKey) {
		// TODO Auto-generated method stub
		StringBuffer finalString = new StringBuffer();
		finalString.append(userName.trim()).append(senderId.trim()).append(content.trim()).append(secureKey.trim());
		// logger.info("Parameters for SHA-512 : "+finalString);
		String hashGen = finalString.toString();
		StringBuffer sb = null;
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-512");
			md.update(hashGen.getBytes());
			byte byteData[] = md.digest();
			// convert the byte to hex format method 1
			sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}

	private static String MD5(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md;
		md = MessageDigest.getInstance("SHA-1");
		byte[] md5 = new byte[64];
		md.update(text.getBytes("iso-8859-1"), 0, text.length());
		md5 = md.digest();
		return convertedToHex(md5);
	}

	private static String convertedToHex(byte[] data) {
		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < data.length; i++) {
			int halfOfByte = (data[i] >>> 4) & 0x0F;
			int twoHalfBytes = 0;

			do {
				if ((0 <= halfOfByte) && (halfOfByte <= 9)) {
					buf.append((char) ('0' + halfOfByte));
				}

				else {
					buf.append((char) ('a' + (halfOfByte - 10)));
				}

				halfOfByte = data[i] & 0x0F;

			} while (twoHalfBytes++ < 1);
		}
		return buf.toString();
	}
}
