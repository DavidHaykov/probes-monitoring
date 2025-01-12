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

import java.time.LocalDateTime;
import java.util.function.Consumer;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class AnalyzerAppl {
    @Value("${app.analyzer.producer.binding.name")
    String producerBindingName;

    final RangeProviderClient service;
    final StreamBridge bridge;
    public static void main(String[] args) {
        SpringApplication.run(AnalyzerAppl.class, args);
    }
    @Bean
    Consumer<ProbeData> analyzerConsumer() {
        return probeData -> {
            log.trace("Received probe: {}", probeData);
            Range range = service.getRange(probeData.id());
            double deviation;
            if (probeData.value() < range.min()) {
                deviation = probeData.value() - range.min();
            } else if (probeData.value() > range.max()) {
                deviation = probeData.value() - range.max();
            } else {
                deviation = 0;
            }
            if (deviation != 0) {
                log.debug("Deviation detected: {}", deviation);
                DeviationData deviationData = new DeviationData(
                        probeData.id(),
                        deviation,
                        probeData.value(),
                        System.currentTimeMillis()
                );
                bridge.send(producerBindingName,deviationData);
                log.debug("Deviation data {} sent to {}", deviationData, producerBindingName);
            } else {
                log.debug("deviation is not detected");
            }
        };
    }
}