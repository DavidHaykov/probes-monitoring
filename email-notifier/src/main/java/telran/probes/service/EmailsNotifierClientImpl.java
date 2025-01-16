package telran.probes.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.Arrays;

@Configuration
@Slf4j
@Service
public class EmailsNotifierClientImpl implements EmailsNotifierClient {
    private final RestTemplate rest = new RestTemplate();

    @Value("app.range.provider.host:localhost")
    String host;
    @Value("app.range.provider.port:8080")
    int port;
    @Value("app.range.provider.path:/sensor/emails")
    String path;

    @Override
    public String[] getEmails(long sensorId) {
        String url = host+ ":" + port + "/" + path + "/" + sensorId;

        try {
            String[] emails = rest.exchange(url, HttpMethod.GET, null, String[].class).getBody();
            if (emails != null && emails.length > 0) {
                log.trace("Received emails: {}", Arrays.toString(emails));
                return emails;
            } else {
                log.debug("No emails found for sensorId: {}",  sensorId);
                return new String[0];
            }
        } catch (Exception e) {
            return new String[0];
        }
    }
}
