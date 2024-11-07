package in.cdac.eraktkosh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;

@Configuration
public class OTP_CONFIG {

	@Bean
	public Config hazelCastConfig() {

		return new Config().setInstanceName("hazelcast-instance")
				.addMapConfig(new MapConfig().setName("captchaCache").setTimeToLiveSeconds(300)) // TTL for 5 minutes
				.addMapConfig(new MapConfig().setName("otpCache").setTimeToLiveSeconds(300)); // TTL for 5 minutes
	}
}