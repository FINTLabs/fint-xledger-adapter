package no.fint.xledger.graphql.caches;

import no.fint.xledger.graphql.ProductRepository;
import no.fint.xledger.model.Node;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class ProductCache extends Cache<Node> {

    private final ProductRepository repository;

    public ProductCache(ProductRepository repository) {
        super(Duration.ofDays(1));
        this.repository = repository;
    }

    @Override
    protected List<Node> getData() {
        return repository.queryProducts();
    }
}
