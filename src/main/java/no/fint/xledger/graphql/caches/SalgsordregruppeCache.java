package no.fint.xledger.graphql.caches;

import no.fint.xledger.graphql.ObjectKinds;
import no.fint.xledger.graphql.ObjectValuesRepository;
import no.fint.xledger.model.objectValues.Node;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class SalgsordregruppeCache extends Cache<Node> {

    private final ObjectValuesRepository repository;

    public SalgsordregruppeCache(ObjectValuesRepository repository) {
        super(Duration.ofDays(1));
        this.repository = repository;
    }

    @Override
    protected List<Node> getData() {
        return repository.get(ObjectKinds.SALGSORDREGRUPPE);
    }
}