package telran.probes.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.annotation.Documented;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode(of = "sensorId")
@Document
public class SensorEmailsDoc {
    @Id
    long sensorId;
    String[] emails;
}
