package no.fint.xledger.okonomi.faktura;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.okonomi.faktura.FakturaResource;
import no.fint.xledger.graphql.SalesOrdersRepository;
import no.fint.xledger.model.salesOrders.Node;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FakturaService {

    private final FakturaMapper fakturaMapper;

    private final SalesOrdersRepository salesOrdersRepository;

    private final FakturaCache fakturaCache;

    public FakturaService(FakturaMapper fakturaMapper, SalesOrdersRepository salesOrdersRepository, FakturaCache fakturaCache) {
        this.fakturaMapper = fakturaMapper;
        this.salesOrdersRepository = salesOrdersRepository;
        this.fakturaCache = fakturaCache;
    }

    public FakturaResource getFaktura(String fakturanummer) {
        Node salesOrder = fakturaCache.get(fakturanummer);

        if (salesOrder == null) {
            salesOrder = salesOrdersRepository.getSalesOrderByInvoiceNumber(fakturanummer);
            fakturaCache.update(fakturanummer, salesOrder);
        }

        return salesOrder == null ? null : fakturaMapper.toFint(salesOrder);
    }
}
