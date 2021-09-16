package no.fint.xledger.okonomi.mva;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.okonomi.kodeverk.MerverdiavgiftResource;
import no.fint.xledger.graphql.caches.MerverdiavgiftCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MerverdiavgiftService {

    private final MerverdiavgiftCache cache;
    private final MerverdiavgiftMapper mapper;

    @Getter
    private List<MerverdiavgiftResource> mva;

    public MerverdiavgiftService(MerverdiavgiftCache cache, MerverdiavgiftMapper mvaFactory) {
        this.cache = cache;
        this.mapper = mvaFactory;
    }

    @Scheduled(initialDelay = 9000, fixedDelayString = "${fint.xledger.kodeverk.refresh-interval:1500000}")
    public void refresh() {
        log.info("Refreshing Merverdiavgift...");
        mva = cache.get()
                .stream()
                .map(mapper::toFint)
                .collect(Collectors.toList());
        log.info("End refreshing Merverdiavgift");
    }
}
