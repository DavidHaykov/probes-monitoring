package telran.probes.service;

import org.springframework.beans.factory.annotation.Autowired;
import telran.probes.model.SensorEmailsDoc;
import telran.probes.model.exceprtion.SensorEmailNotFoundException;
import telran.probes.repo.ProviderRepository;

@org.springframework.stereotype.Service
public class Service implements IProvider {
    @Autowired
    ProviderRepository repository;

    @Override
    public String[] getSensorEmails(long sensorId) {
        SensorEmailsDoc doc = repository.findById(sensorId).orElse(null);
        if(doc == null){
            throw new SensorEmailNotFoundException(sensorId);
        }
        return doc.getEmails();
    }
}