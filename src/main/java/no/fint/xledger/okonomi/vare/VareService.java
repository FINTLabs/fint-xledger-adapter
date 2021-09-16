package no.fint.xledger.okonomi.vare;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.okonomi.kodeverk.VareResource;
import no.fint.xledger.graphql.caches.ProductCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VareService {

    private final ProductCache cache;
    private final VareMapper mapper;

    @Getter
    private List<VareResource> varer;

    public VareService(ProductCache cache, VareMapper vareFactory) {
        this.cache = cache;
        this.mapper = vareFactory;
    }

    @Scheduled(initialDelay = 10000, fixedDelayString = "${fint.xledger.kodeverk.refresh-interval:1500000}")
    public void refresh() {
        log.info("Refreshing Vare...");
        varer = cache.get()
                .stream()
                .map(mapper::toFint)
                .collect(Collectors.toList());
        log.info("End refreshing Vare");
    }
}
