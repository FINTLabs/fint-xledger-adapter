package no.fint.xledger.graphql.caches;

import lombok.extern.slf4j.Slf4j;
import no.fint.xledger.graphql.InvoiceBaseItemRepository;
import no.fint.xledger.model.invoiceBaseItem.invoiceBaseItems.Node;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InvoiceBaseItemCache extends Cache<Node> {

    private final InvoiceBaseItemRepository repository;

    public InvoiceBaseItemCache(InvoiceBaseItemRepository repository) {
        super(Duration.ofHours(3));
        this.repository = repository;
    }

    @Override
    protected List<Node> getData() {
        return repository.get();
    }

    public Node getByXorder(String ordrenummer) {
        List<Node> rows = get().stream().filter(n -> n.getExtOrder().equals(ordrenummer)).collect(Collectors.toList());
        if (rows.size() == 0) return null;
        if (rows.size() > 1) log.warn("Multiple invoiceBaseItems with extOrder=" + ordrenummer);
        return rows.get(0);
    }
}