package telran.probes.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import telran.probes.dto.ProbeData;
import telran.probes.model.ProbeDataDoc;

import java.util.function.Consumer;


@Service
@Slf4j
@Configuration
public class AvgPopulatorImpl implements AvgPopulator{
    MongoTemplate mongoTemplate;

    @Bean
    public Consumer<ProbeData> averageValuesPopulator() {
        return probeData -> {
            ProbeDataDoc doc = new ProbeDataDoc(probeData);
            mongoTemplate.insert(doc, "avg_values");
            log.trace("Inserted ProbeDataDoc: {}", doc);
        };
    }
}


