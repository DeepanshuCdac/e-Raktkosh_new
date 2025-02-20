package in.cdac.eraktkosh.config;

import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

@Component
public class QueryConfig {

	@Autowired
	private Environment env;

	public String getQuery(String key) {
		String query = env.getProperty(key);

		// Debugging: Print all available properties
		if (env instanceof ConfigurableEnvironment) {
			ConfigurableEnvironment configurableEnvironment = (ConfigurableEnvironment) env;
			StreamSupport.stream(configurableEnvironment.getPropertySources().spliterator(), false)
					.map(PropertySource::getName).forEach(System.out::println);
		}

		if (query == null) {
			throw new RuntimeException(
					"Query not found for key: " + key + ". Check if 'query.properties' is loaded properly.");
		}

		return query;
	}
}