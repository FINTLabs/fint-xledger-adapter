package no.fint.xledger.graphql.caches;

import lombok.extern.slf4j.Slf4j;
import no.fint.xledger.graphql.ProductRepository;
import no.fint.xledger.model.product.Node;
import no.fint.xledger.okonomi.ConfigProperties;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductCache extends Cache<Node> {

    private final ProductRepository repository;

    public ProductCache(ProductRepository repository, ConfigProperties configProperties) {
        super(Duration.ofDays(1));
        this.repository = repository;
    }

    @Override
    protected List<Node> getData() {
        return repository.queryProducts();
    }

    public List<Node> filterVarerByCode(String startsWith, int digistToCompare) {

        if (digistToCompare != 0 && startsWith.length() > digistToCompare) {
            startsWith = startsWith.substring(0, digistToCompare);
        }

        String finalStartsWith = startsWith;

        return get()
                .stream()
                .filter(v -> v.getCode().startsWith(finalStartsWith))
                .collect(Collectors.toList());
    }

    public Node filterVarerByDbId(String productDbId) {
        List<Node> rows = get().stream().filter(n -> String.valueOf(n.getDbId()).equals(productDbId)).collect(Collectors.toList());
        if (rows.size() == 0) return null;
        if (rows.size() > 1) log.warn("Multiple products with dbId=" + productDbId);
        return rows.get(0);
    }
}
