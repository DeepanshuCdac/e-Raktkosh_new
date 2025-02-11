package in.cdac.eraktkosh.config;

import org.springframework.context.annotation.Configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

@Configuration
public class OTP_CONFIG {

	// uat
	// public static final String ip1 = "10.226.25.103";

	public static final String ip1 = "10.226.25.103";
	public static final String ip2 = "10.226.30.41";
	public static final String ipuat = "10.10.10.78";
	public static final String captchaCache = "captchaCache";
	public static final String otpCache = "otpCache";

	private final HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(hazelCastConfig());

	// @Bean
	public Config hazelCastConfig() {

//		return new Config().setInstanceName("hazelcast-instance")
//				.addMapConfig(new MapConfig().setName("captchaCache").setTimeToLiveSeconds(300))
//				.addMapConfig(new MapConfig().setName("otpCache").setTimeToLiveSeconds(300));

		Config config = new Config();
		config.setInstanceName("hazelcast-instance");
		// config.setProperty("hazelcast.socket.client.bind.any", "false");
		// config.setProperty("hazelcast.socket.client.bind.address", "10.226.17.5");
		config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
		config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(true).addMember(ip2);// .addMember(ip1);//
																								// .setMembers(List.of(ip1,ip2));
		// uat
		// config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(true).setEnabled(true).addMember(ipuat);
		config.addMapConfig(mapConfig(captchaCache));
		config.addMapConfig(mapConfig(otpCache));
		return config;

	}

	private MapConfig mapConfig(String mapName) {
		MapConfig mapConfig = new MapConfig(mapName);
		mapConfig.setTimeToLiveSeconds(300);
		// mapConfig.setMaxIdleSeconds(30);
		return mapConfig;
	}

	public HazelcastInstance getHazelcastInstance() {
		return hazelcastInstance;
	}

}