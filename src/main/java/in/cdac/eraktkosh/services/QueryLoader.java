package in.cdac.eraktkosh.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class QueryLoader {
	private Properties properties;

	// Constructor loads the properties file
	public QueryLoader(String fileName) {
		properties = new Properties();
		try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {
			if (input == null) {
				throw new IOException("Unable to find resource: " + fileName);
			}
			properties.load(input);
		} catch (IOException ex) {
			// Log the error more clearly or rethrow a custom exception
			System.err.println("Error loading query file: " + fileName);
			ex.printStackTrace();
			// Optionally, rethrow as a runtime exception to propagate failure
			throw new RuntimeException("Failed to load query file: " + fileName, ex);
		}
	}

	// Get a query string by its key
	public String getQuery(String key) {
		String query = properties.getProperty(key);
		if (query == null) {
			// You can log this or throw an exception if the key is critical
			System.err.println("Query not found for key: " + key);
			// You can either return a default query, throw an exception, or return null.
			throw new IllegalArgumentException("Query not found for key: " + key);
		}
		return query;
	}
}
