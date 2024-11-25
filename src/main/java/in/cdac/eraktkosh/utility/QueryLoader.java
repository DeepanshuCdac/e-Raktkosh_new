package in.cdac.eraktkosh.utility;

import java.io.InputStream;
import java.util.Properties;

public class QueryLoader {
    private Properties properties;

    public QueryLoader(String fileName) {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new RuntimeException("Sorry, unable to find " + fileName);
            }
            properties.load(input);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getQuery(String key) {
        return properties.getProperty(key);
    }
}
