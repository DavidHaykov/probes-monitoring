package telran.probes.model.exceprtion;

import java.util.function.Supplier;

public class SensorEmailNotFoundException extends RuntimeException  {
    public SensorEmailNotFoundException(long sensorId) {
        super("Sensor #: " + sensorId + " is notFound");
    }

}
