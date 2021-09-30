package no.fint.xledger.okonomi.fakturagrunnlag;

import no.fint.model.resource.Link;
import no.fint.model.resource.felles.PersonResource;
import no.fint.xledger.fintclient.FintRepository;
import no.fint.xledger.graphql.CustomerRepository;
import no.fint.xledger.okonomi.ConfigProperties;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final ConfigProperties configProperties;

    private final CustomerRepository customerRepository;

    private final FintRepository fintRepository;

    private final CustomerMapper mapper;

    public CustomerService(ConfigProperties configProperties, CustomerRepository customerRepository, FintRepository fintRepository, CustomerMapper mapper) {
        this.configProperties = configProperties;
        this.customerRepository = customerRepository;
        this.fintRepository = fintRepository;
        this.mapper = mapper;
    }

    public int createOrUpdate(List<Link> personLinks) throws Exception {
        PersonResource person = fintRepository.getPerson(configProperties.getOrganization(), personLinks);
        int companyDbId = customerRepository.getCompanyDbId(person.getFodselsnummer().getIdentifikatorverdi());

        int customerDbId = 0;

        if (companyDbId == 0) {
            // add person
            companyDbId = customerRepository.addCompany(person.getFodselsnummer().getIdentifikatorverdi(), configProperties.getOwnerDbId());
            if (companyDbId == 0) throw new Exception("companyDbId should not be 0");
            customerDbId = customerRepository.addCustomer(mapper.toXledger(person, companyDbId));
        } else {
            // update person
            customerDbId = customerRepository.getCustomerDbId(String.valueOf(companyDbId));
            if (customerDbId == 0) {
                customerDbId = customerRepository.addCustomer(mapper.toXledger(person, companyDbId));
            } else {
                customerDbId = customerRepository.updateCustomer(mapper.toXledger(person, companyDbId), customerDbId);
            }
        }

        return customerDbId;
    }
}
