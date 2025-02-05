package in.cdac.eraktkosh;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	@Bean
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(eraktkoshPortalApplication.class);
	}
}
