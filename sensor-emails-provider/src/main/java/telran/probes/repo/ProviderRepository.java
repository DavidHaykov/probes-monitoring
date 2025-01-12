package telran.probes.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderRepository extends MongoRepository<Long, String[]> {
    String[] findById(long sensorId);

}
