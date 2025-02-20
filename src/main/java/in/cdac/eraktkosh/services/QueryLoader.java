package in.cdac.eraktkosh.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.stereotype.Component;

@Component
public class QueryLoader {
	private Properties properties;

	// Default constructor loads "query.properties"
	public QueryLoader() {
		this("query.properties");
	}

	public QueryLoader(String fileName) {
		properties = new Properties();
		try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {
			if (input == null) {
				System.err.println("Unable to find resource: " + fileName);
				throw new IOException("Unable to find resource: " + fileName);
			}
			properties.load(input);
			System.out.println("Successfully loaded properties from: " + fileName); // Log to verify
		} catch (IOException ex) {
			System.err.println("Error loading query file: " + fileName);
			ex.printStackTrace();
			throw new RuntimeException("Failed to load query file: " + fileName, ex);
		}
	}

	public String getQuery(String key) {
		String query = properties.getProperty(key);
		if (query == null) {
			System.err.println("Query not found for key: " + key);
			throw new IllegalArgumentException("Query not found for key: " + key);
		}
		return query;
	}
}
