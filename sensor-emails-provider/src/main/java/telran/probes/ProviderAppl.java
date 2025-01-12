package telran.probes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import telran.probes.service.IProvider;

@SpringBootApplication
public class ProviderAppl {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ProviderAppl.class);
        IProvider service = context.getBean(IProvider.class);
    }
}