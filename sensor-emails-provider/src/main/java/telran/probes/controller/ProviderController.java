package telran.probes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import telran.probes.service.IProvider;

@RestController
public class ProviderController {
    @Autowired
    IProvider service;
    @GetMapping("sensor/emails/{id}")
    public String[] getSensorEmails(@PathVariable long id){
        return service.getSensorEmails(id);
    }

}
