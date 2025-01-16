package telran.probes.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import telran.probes.dto.Range;
import telran.probes.dto.SensorUpdateData;

import java.util.HashMap;
import java.util.function.Consumer;

@Configuration
@Service
@Slf4j
public class RangeProviderClientImpl implements RangeProviderClient{
    RestTemplate rest = new RestTemplate();
    HashMap <Long, Range> cache = new HashMap<Long, Range>();

    @Value("app.range.provider.host:localhost")
    String host;
    @Value("app.range.provider.port:8080")
    int port;
    @Value("app.range.provider.path:/sensor/range")
    String path;
    @Override
    public Range getRange(long sensorId) {
        String url = host+ ":" + port + "/" + path + "/" + sensorId;
        Range range = cache.get(sensorId);
        if(range == null) {
            try {

                range = rest.exchange(url, HttpMethod.GET, null, Range.class).getBody();
                if (range != null) {
                    log.trace("Range for sensor {} = {}", sensorId, range);
                    cache.put(sensorId, range);
                    return range;
                } else {
                    log.debug("Not found average for sensorId {}", sensorId);
                    return null;
                }

            } catch (Exception e) {
                return new Range(0,0);
            }
        }
        log.trace("Range for sensor {} = {}", sensorId, range);
        return range;

    }

    @Bean
    Consumer<SensorUpdateData> updateRangeConsumer(){
        return updateData -> {
            if(cache.containsKey(updateData.id())){
                cache.put(updateData.id(), updateData.range());
            }
        };
    }
}


