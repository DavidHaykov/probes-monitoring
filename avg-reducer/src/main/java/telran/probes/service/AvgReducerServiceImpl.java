package telran.probes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import telran.probes.dto.ProbeData;
import telran.probes.repo.ProbesList;
import telran.probes.repo.ProbsListRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class AvgReducerServiceImpl implements AvgReducerService {
    @Autowired
    ProbsListRepository repository;

    @Autowired
    StreamBridge bridge;

    private final Map<Long, List<Double>> cache = new HashMap<>();
    private static final int CACHE_SIZE = 10;

    @Override
    public double getAvgValue(ProbeData data) {

        ProbesList probesList = getById(data.id());
        List<Double> repositoryValues = probesList.getValues();

        cache.putIfAbsent(data.id(), new ArrayList<>());
        List<Double> bufferValues = cache.get(data.id());
        bufferValues.add(data.value());
        if (bufferValues.size() >= CACHE_SIZE) {
            List<Double> combinedValues = new ArrayList<>(repositoryValues);
            combinedValues.addAll(bufferValues);
            probesList.getValues().addAll(bufferValues);
            repository.save(probesList);
            double avgValue = combinedValues.stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElseThrow(() -> new RuntimeException("No avg value"));

            bufferValues.clear();

            return avgValue;
        }

        throw new RuntimeException("Not enough data for averaging");
    }

    ProbesList getById(long sensorId) {
        return repository.findById(sensorId)
                .orElse(repository.save(new ProbesList(sensorId)));
    }
}