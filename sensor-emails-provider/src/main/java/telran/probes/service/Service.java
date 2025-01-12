package telran.probes.service;

import org.springframework.beans.factory.annotation.Autowired;
import telran.probes.repo.ProviderRepository;

@org.springframework.stereotype.Service
public class Service implements IProvider {
    @Autowired
    ProviderRepository repository;

    @Override
    public String[] getSensorEmails(long sensorId) {

        return repository.findById(sensorId);
    }
}
