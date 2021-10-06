package no.fint.xledger.okonomi.faktura;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.okonomi.faktura.FakturaResource;
import no.fint.xledger.graphql.SalesOrdersRepository;
import no.fint.xledger.model.salesOrders.Node;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FakturaService {

    private FakturaMapper fakturaMapper;

    private final SalesOrdersRepository salesOrdersRepository;

    public FakturaService(FakturaMapper fakturaMapper, SalesOrdersRepository salesOrdersRepository) {
        this.fakturaMapper = fakturaMapper;
        this.salesOrdersRepository = salesOrdersRepository;
    }

    public FakturaResource getFaktura(String fakturanummer) {
        Node salesOrder = salesOrdersRepository.getSalesOrderByInvoiceNumber(fakturanummer);
        if (salesOrder == null) return null;
        return fakturaMapper.toFint(salesOrder);
    }
}
