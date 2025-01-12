package telran.probes.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import telran.probes.model.SensorEmailsDoc;

@Repository
public interface ProviderRepository extends MongoRepository<SensorEmailsDoc, Long> {


}
