package no.fint.xledger.okonomi.fakturagrunnlag;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.Link;
import no.fint.model.resource.okonomi.faktura.FakturagrunnlagResource;
import no.fint.model.resource.okonomi.faktura.FakturalinjeResource;
import no.fint.model.resource.okonomi.faktura.FakturautstederResource;
import no.fint.xledger.graphql.InvoiceBaseItemRepository;
import no.fint.xledger.graphql.SalesOrdersRepository;
import no.fint.xledger.graphql.caches.InvoiceBaseItemCache;
import no.fint.xledger.graphql.caches.SalesOrderCache;
import no.fint.xledger.model.salesOrders.Node;
import no.fint.xledger.okonomi.ConfigProperties;
import no.fint.xledger.okonomi.InvoiceUtil;
import no.fint.xledger.okonomi.fakturautsteder.FakturautstederService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FakturagrunnlagService {

    private final InvoiceBaseItemRepository invoiceBaseItemRepository;

    private final CustomerService customerService;

    private final FakturagrunnlagMapper mapper;

    private final FakturautstederService fakturautstederService;

    private final SalesOrdersRepository salesOrdersRepository;

    private final InvoiceBaseItemCache invoiceBaseItemCache;

    private final SalesOrderCache salesOrderCache;

    public FakturagrunnlagService(InvoiceBaseItemRepository invoiceBaseItemRepository, CustomerService customerService, FakturagrunnlagMapper mapper, ConfigProperties configProperties, FakturautstederService fakturautstederService, SalesOrdersRepository salesOrdersRepository, InvoiceBaseItemCache invoiceBaseItemCache) {
        this.invoiceBaseItemRepository = invoiceBaseItemRepository;
        this.customerService = customerService;
        this.mapper = mapper;
        this.fakturautstederService = fakturautstederService;
        this.salesOrdersRepository = salesOrdersRepository;
        this.invoiceBaseItemCache = invoiceBaseItemCache;
        salesOrderCache = new SalesOrderCache(configProperties.getHoursToCacheSaleOrders());
    }

    public void addFakturagrunnlag(FakturagrunnlagResource fakturagrunnlagResource) throws Exception {

        log.info("addFakturagrunnlag started: " + fakturagrunnlagResource.getOrdrenummer().getIdentifikatorverdi());
        int subledgerDbId = customerService.createOrUpdate(fakturagrunnlagResource.getMottaker().getPerson());
        if (subledgerDbId == 0) throw new Exception("subledgerDbID = 0. Customer create/update has failed");

        if (fakturautstederService.getFakturautstedere() == null)
            fakturautstederService.refreshIfNeeded();

        FakturautstederResource fakturautsteder = getFakturautsteder(fakturagrunnlagResource.getFakturautsteder());

        int lineNumber = 1;
        for (FakturalinjeResource linje : fakturagrunnlagResource.getFakturalinjer()) {
            String dbId = invoiceBaseItemRepository.add(mapper.toXledger(linje, subledgerDbId, fakturautsteder, fakturagrunnlagResource, lineNumber++));
            log.info("invoiceBaseItem added in Xledger with dbID: " + dbId);
        }

        log.info("FAKTURAGRUNNLAG CREATED: " + fakturagrunnlagResource.getOrdrenummer().getIdentifikatorverdi());
    }

    // todo flytte til FintRepository?
    private FakturautstederResource getFakturautsteder(List<Link> links) throws IllegalArgumentException {
        String href = links.get(0).getHref();
        String id = StringUtils.substringAfterLast(href, "/");

        return fakturautstederService
                .getFakturautstedere()
                .stream()
                .filter(f -> f.getSystemId().getIdentifikatorverdi().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Didn't find fakturautsteder with id " + id));
    }

    public FakturagrunnlagResource getFakturagrunnlag(String ordrenummer) {
        log.info("getFakturagrunnlag for " + ordrenummer);

        Node salesOrder = salesOrderCache.get(ordrenummer);
        // don't want to refetch order if it already has invoiceNo. If invoiceNo is missing, update from Xledger to check for updates
        if (salesOrder != null && InvoiceUtil.hasInvoicedNumber(salesOrder)) return mapper.toFint(salesOrder);

        salesOrder = salesOrdersRepository.getSalesOrderByXorder(ordrenummer);
        if (salesOrder != null) {
            salesOrderCache.update(ordrenummer, salesOrder);
            return mapper.toFint(salesOrder);
        }

        no.fint.xledger.model.invoiceBaseItem.invoiceBaseItems.Node invoiceBaseItem = invoiceBaseItemCache.getByXorder(ordrenummer);
        if (invoiceBaseItem != null) return mapper.toFint(invoiceBaseItem);

        return null;
    }
}