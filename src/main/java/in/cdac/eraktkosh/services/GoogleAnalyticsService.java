package in.cdac.eraktkosh.services;

import java.io.InputStream;

import org.springframework.stereotype.Service;

import com.google.analytics.data.v1beta.BetaAnalyticsDataClient;
import com.google.analytics.data.v1beta.BetaAnalyticsDataSettings;
import com.google.analytics.data.v1beta.Metric;
import com.google.analytics.data.v1beta.RunRealtimeReportRequest;
import com.google.analytics.data.v1beta.RunRealtimeReportResponse;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;

@Service
public class GoogleAnalyticsService {

	    private static final String PROPERTY_ID = "properties/494204014"; 

	    public int getActiveUsers() {
	        try {
	            InputStream credentialsStream = getClass().getClassLoader().getResourceAsStream("service-account-key.json");

	            GoogleCredentials credentials = ServiceAccountCredentials.fromStream(credentialsStream);

	            BetaAnalyticsDataSettings settings = BetaAnalyticsDataSettings.newBuilder()
	                    .setCredentialsProvider(() -> credentials)
	                    .build();

	            try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create(settings)) {

	                RunRealtimeReportRequest request = RunRealtimeReportRequest.newBuilder()
	                        .setProperty(PROPERTY_ID)
	                        .addMetrics(Metric.newBuilder().setName("activeUsers"))
	                        .build();

	                RunRealtimeReportResponse response = analyticsData.runRealtimeReport(request);

	                String count = response.getRows(0).getMetricValues(0).getValue();
	                return Integer.parseInt(count);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            return -1; 
	        }
	    }
	}


