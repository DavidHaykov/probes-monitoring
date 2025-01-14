package telran.probes.repo;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.ArrayList;
import java.util.List;
@RedisHash
@Getter
public class ProbesList {
    @Id
    @NotNull
    long sensorId;
    List<Double> values = new ArrayList<>();

    public ProbesList(long sensorId) {
        this.sensorId = sensorId;
    }
}
