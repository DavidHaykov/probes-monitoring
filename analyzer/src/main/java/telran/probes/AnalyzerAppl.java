package telran.probes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import telran.probes.dto.DeviationData;
import telran.probes.dto.ProbeData;
import telran.probes.dto.Range;
import telran.probes.service.RangeProviderClient;

import java.util.function.Consumer;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class AnalyzerAppl {
    @Value("${app.analyzer.consumer.binding.name")
    String producerBindingName;

    final RangeProviderClient service;
    final StreamBridge bridge;
    public static void main(String[] args) {
        SpringApplication.run(AnalyzerAppl.class, args);
    }
    @Bean
    Consumer<ProbeData> analyzerConsumer(){
        return probeData -> {
            log.trace("received probe: {}", probeData);
            Range range = service.getRange(probeData.id());
            if(probeData.value() < range.min() || probeData.value() > range.max()){
                double deviation = Math.abs(probeData.value() < range.min()
                ? probeData.value() - range.min()
                : probeData.value() - range.max()
                );
                log.debug("deviation detected: {}", deviation);
                DeviationData data = new DeviationData(probeData.id(), deviation, probeData.value(), probeData.timestamp());
                bridge.send(producerBindingName, data);
                log.debug("Deviation data {} send to {}", data, producerBindingName);
            }else {
                log.debug("Deviation is not detected");
            }
        };
    }
}