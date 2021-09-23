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

    public int createOrUpdate(List<Link> personLinks) {
        PersonResource person = fintRepository.getPerson(configProperties.getOrganization(), personLinks);
        int companyDbId = customerRepository.getCompanyDbId(person.getFodselsnummer().getIdentifikatorverdi());

        if (companyDbId == 0) {
            companyDbId = customerRepository.addCompany(person.getFodselsnummer().getIdentifikatorverdi(), configProperties.getOwnerDbId());

            //int customerDbId = customerRepository.getCustomerDbId(companyDbId);
            // add person
        } else {
            // update person
        }

        int subledgerDbId = 0;

        return subledgerDbId;
    }
}
