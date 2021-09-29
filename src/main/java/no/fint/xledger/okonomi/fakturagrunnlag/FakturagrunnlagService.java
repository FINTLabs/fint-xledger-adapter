package no.fint.xledger.okonomi.fakturagrunnlag;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.Link;
import no.fint.model.resource.okonomi.faktura.FakturagrunnlagResource;
import no.fint.model.resource.okonomi.faktura.FakturalinjeResource;
import no.fint.model.resource.okonomi.faktura.FakturautstederResource;
import no.fint.xledger.fintclient.FintRepository;
import no.fint.xledger.graphql.InvoiceBaseItemRepository;
import no.fint.xledger.okonomi.ConfigProperties;
import no.fint.xledger.okonomi.fakturautsteder.FakturautstederService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Autowired
    private FakturautstederService fakturautstederService;

    public FakturagrunnlagService(InvoiceBaseItemRepository invoiceBaseItemRepository) {
        this.invoiceBaseItemRepository = invoiceBaseItemRepository;
    }

    public void addFakturagrunnlag(FakturagrunnlagResource fakturagrunnlagResource) throws Exception {

        int subledgerDbId = customerService.createOrUpdate(fakturagrunnlagResource.getMottaker().getPerson());
        if (subledgerDbId == 0) throw new Exception("subledgerDbID = 0. Customer create/update has failed");

        if (fakturautstederService.getFakturautstedere() == null)
            throw new Exception("Fakturautstedere-cache ikke fylt.");

        FakturautstederResource fakturautsteder = getFakturautsteder(fakturagrunnlagResource.getFakturautsteder());

        int lineNumber = 1;
        for (FakturalinjeResource linje : fakturagrunnlagResource.getFakturalinjer()) {
            String dbId = invoiceBaseItemRepository.add(mapper.toXledger(linje, subledgerDbId, fakturautsteder, fakturagrunnlagResource, lineNumber++));
            log.trace("invoiceBaseItem added in Xledger with dbID: " + dbId);
        }

        log.info("FAKTURAGRUNNLAG CREATED: " + fakturagrunnlagResource.getOrdrenummer());
    }

    private FakturautstederResource getFakturautsteder(List<Link> links) throws Exception {
        String href = links.get(0).getHref();
        String id = StringUtils.substringAfterLast(href, "/");

        FakturautstederResource fakturautsteder =
                fakturautstederService
                        .getFakturautstedere()
                        .stream()
                        .filter(f -> f.getSystemId().getIdentifikatorverdi().equals(id))
                        .findFirst()
                        .orElseThrow(Exception::new);
        // todo better exception type/message
        return fakturautsteder;
    }
}