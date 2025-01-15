package telran.probes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telran.probes.dto.ProbeData;
import telran.probes.repo.ProbesList;
import telran.probes.repo.ProbsListRepository;

import java.util.List;

@Service
public class AvgReducerServiceImpl implements AvgReducerService{
    @Autowired
    ProbsListRepository repository;

    @Override
    public double getAvgValue(ProbeData data) {
        ProbesList list = getById(data.id());
        List<Double> values = list.getValues();
        return values.stream().mapToDouble(Double::doubleValue).average().orElseThrow(() -> new RuntimeException("No avg value"));
    }
    ProbesList getById(long sensorId){
        return repository.findById(sensorId).orElseThrow(() -> new RuntimeException("Not Found"));
    }
}
