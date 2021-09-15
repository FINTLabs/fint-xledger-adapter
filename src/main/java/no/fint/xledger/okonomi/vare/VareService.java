package no.fint.xledger.okonomi.vare;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.okonomi.kodeverk.VareResource;
import no.fint.xledger.graphql.GraphQLQuery;
import no.fint.xledger.graphql.ProductRepository;
import no.fint.xledger.graphql.XledgerWebClient;
import no.fint.xledger.model.EdgesItem;
import no.fint.xledger.model.GraphQlResponse;
import no.fint.xledger.model.Node;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VareService {

    private final ProductRepository repository;
    private final VareMapper mapper;

    @Getter
    private List<VareResource> varer;

    public VareService(ProductRepository repository, VareMapper vareFactory) {
        this.repository = repository;
        this.mapper = vareFactory;
    }

    @Scheduled(initialDelay = 10000, fixedDelayString = "${fint.xledger.kodeverk.refresh-interval:1500000}")
    public void refresh() {
        log.info("Refreshing Vare...");
        varer = repository.queryProducts()
                .stream()
                .map(mapper::toFint)
                .collect(Collectors.toList());
        log.info("End refreshing Vare");
    }
}
