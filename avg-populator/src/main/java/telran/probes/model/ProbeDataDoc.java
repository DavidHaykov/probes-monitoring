package telran.probes.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Document;
import telran.probes.dto.ProbeData;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Document(collection = "avg_values")
@ToString
@Getter
@NoArgsConstructor
public class ProbeDataDoc {
    long sensorID;
    LocalDateTime timestamp;
    Double value;

    public ProbeDataDoc(ProbeData probeData) {
        sensorID = probeData.id();
        Instant instant = Instant.ofEpochMilli(probeData.timestamp());
        timestamp = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        value = probeData.value();
    }
}