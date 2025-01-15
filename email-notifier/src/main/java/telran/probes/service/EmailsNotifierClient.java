package telran.probes.service;

public interface EmailsNotifierClient {
    String[] getEmails(long sensorId);
}
