package no.fint.xledger.okonomi.mva;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.okonomi.kodeverk.MerverdiavgiftResource;
import no.fint.xledger.graphql.caches.MerverdiavgiftCache;
import no.fint.xledger.okonomi.CachedHandlerService;
import no.fint.xledger.okonomi.ConfigProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MerverdiavgiftService extends CachedHandlerService {

    private final MerverdiavgiftCache cache;
    private final MerverdiavgiftMapper mapper;
    private List<MerverdiavgiftResource> mva;

    public MerverdiavgiftService(MerverdiavgiftCache cache, MerverdiavgiftMapper mvaFactory, ConfigProperties configProperties) {
        super(configProperties.getFintCache());
        this.cache = cache;
        this.mapper = mvaFactory;
    }

    public List<MerverdiavgiftResource> getMva() {
        if (mva == null) refreshIfNeeded();
        // todo: threadsafe? await until completed
        return mva;
    }

    @Override
    @Scheduled(initialDelay = 8000, fixedDelayString = "${fint.xledger.kodeverk.refresh-interval:1500000}")
    public void refreshIfNeeded() {
        super.refreshIfNeeded();
    }

    @Override
    protected void refreshData() {
        log.info("Refreshing Merverdiavgift...");
        mva = cache.get()
                .stream()
                .map(mapper::toFint)
                .collect(Collectors.toList());
        log.info("End refreshing Merverdiavgift");
    }
}
