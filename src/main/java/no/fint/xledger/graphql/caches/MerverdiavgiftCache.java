package no.fint.xledger.graphql.caches;


import lombok.extern.slf4j.Slf4j;
import no.fint.xledger.graphql.ObjectKinds;
import no.fint.xledger.graphql.ObjectValuesRepository;
import no.fint.xledger.model.objectValues.Node;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MerverdiavgiftCache extends Cache<Node> {

    private final ObjectValuesRepository repository;

    public MerverdiavgiftCache(ObjectValuesRepository repository) {
        super(Duration.ofDays(1));
        this.repository = repository;
    }

    @Override
    protected List<Node> getData() {
        return repository.get(ObjectKinds.MERVERDIAVGIFT);
    }

    public Node getByCode(String merverdiavgiftCode) {
        List<Node> rows = get().stream().filter(n -> n.getCode().equals(merverdiavgiftCode)).collect(Collectors.toList());
        if (rows.size() == 0) return null;
        if (rows.size() > 1) log.warn("Multiple objectValues for merverdiavgift with code=" + merverdiavgiftCode);
        return rows.get(0);
    }
}