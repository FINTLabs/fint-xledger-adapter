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

    public String getCodeByDbId(String salgsordregruppeDbId) {
        List<Node> rows = get().stream().filter(n -> String.valueOf(n.getDbId()).equals(salgsordregruppeDbId)).collect(Collectors.toList());
        if (rows.size() == 0) return "";
        if (rows.size() > 1) log.warn("Multiple objectValues for Salgsordregruppe with dbId=" + salgsordregruppeDbId);
        return rows.get(0).getCode();
    }
}