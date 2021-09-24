package no.fint.xledger.okonomi.fakturagrunnlag;

import no.fint.model.resource.Link;
import no.fint.model.resource.felles.PersonResource;
import no.fint.xledger.fintclient.FintRepository;
import no.fint.xledger.graphql.CustomerRepository;
import no.fint.xledger.okonomi.ConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private ConfigProperties configProperties;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private FintRepository fintRepository;

    @Autowired
    private CustomerMapper mapper;

    public int createOrUpdate(List<Link> personLinks) {
        PersonResource person = fintRepository.getPerson(configProperties.getOrganization(), personLinks);
        int companyDbId = customerRepository.getCompanyDbId(person.getFodselsnummer().getIdentifikatorverdi());

        int customerDbId = 0;

        if (companyDbId == 0) {
            // add person
            companyDbId = customerRepository.addCompany(person.getFodselsnummer().getIdentifikatorverdi(), configProperties.getOwnerDbId());
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
