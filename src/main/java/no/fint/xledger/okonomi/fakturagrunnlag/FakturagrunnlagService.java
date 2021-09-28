package no.fint.xledger.okonomi.fakturagrunnlag;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.okonomi.faktura.FakturagrunnlagResource;
import no.fint.model.resource.okonomi.faktura.FakturalinjeResource;
import no.fint.model.resource.okonomi.faktura.FakturautstederResource;
import no.fint.xledger.fintclient.FintRepository;
import no.fint.xledger.graphql.InvoiceBaseItemRepository;
import no.fint.xledger.okonomi.ConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FakturagrunnlagService {

    @Autowired
    private InvoiceBaseItemRepository invoiceBaseItemRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private FakturagrunnlagMapper mapper;

    @Autowired
    private FintRepository fintRepository;

    @Autowired
    private ConfigProperties configProperties;

    public FakturagrunnlagService(InvoiceBaseItemRepository invoiceBaseItemRepository) {
        this.invoiceBaseItemRepository = invoiceBaseItemRepository;
    }

    public void addFakturagrunnlag(FakturagrunnlagResource fakturagrunnlagResource) throws Exception {

        int subledgerDbId = customerService.createOrUpdate(fakturagrunnlagResource.getMottaker().getPerson());
        if (subledgerDbId == 0) throw new Exception("subledgerDbID = 0. Customer create/update has failed");

        FakturautstederResource fakturautsteder = fintRepository.getFakturautsteder(configProperties.getOrganization(), fakturagrunnlagResource.getFakturautsteder());

        int lineNumber = 1;
        for (FakturalinjeResource linje : fakturagrunnlagResource.getFakturalinjer()) {
            invoiceBaseItemRepository.add(mapper.toXledger(linje, subledgerDbId, fakturautsteder, fakturagrunnlagResource, lineNumber++));
        }

        log.info("Fakturagrunnlag " + fakturagrunnlagResource.getOrdrenummer() + " created");
    }
}
