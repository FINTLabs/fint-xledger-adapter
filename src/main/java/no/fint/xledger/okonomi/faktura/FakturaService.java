package no.fint.xledger.okonomi.faktura;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.okonomi.faktura.FakturaResource;
import no.fint.xledger.graphql.SalesOrdersRepository;
import no.fint.xledger.graphql.caches.SalesOrderCache;
import no.fint.xledger.model.salesOrders.Node;
import no.fint.xledger.okonomi.ConfigProperties;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FakturaService {

    private final FakturaMapper fakturaMapper;

    private final SalesOrdersRepository salesOrdersRepository;

    private final SalesOrderCache salesOrderCache;

    private final ConfigProperties configProperties;

    public FakturaService(FakturaMapper fakturaMapper, SalesOrdersRepository salesOrdersRepository, ConfigProperties configProperties) {
        this.fakturaMapper = fakturaMapper;
        this.salesOrdersRepository = salesOrdersRepository;
        this.configProperties = configProperties;
        salesOrderCache = new SalesOrderCache(configProperties.getHoursToCacheInvoice());
    }

    public FakturaResource getFaktura(String fakturanummer) {
        Node salesOrder = salesOrderCache.get(fakturanummer);

        if (salesOrder == null) {
            salesOrder = salesOrdersRepository.getSalesOrderByInvoiceNumber(fakturanummer);
            salesOrderCache.update(fakturanummer, salesOrder);
        }

        return salesOrder == null ? null : fakturaMapper.toFint(salesOrder);
    }
}
