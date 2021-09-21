package no.fint.xledger.okonomi.vare;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.okonomi.faktura.FakturautstederResource;
import no.fint.model.resource.okonomi.kodeverk.VareResource;
import no.fint.xledger.graphql.caches.ProductCache;
import no.fint.xledger.graphql.caches.SalgsordregruppeCache;
import no.fint.xledger.model.Node;
import no.fint.xledger.okonomi.SellerUtil;
import no.fint.xledger.okonomi.fakturautsteder.FakturautstederService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class VareService {

    private final ProductCache cache;
    private final VareMapper mapper;

    @Autowired
    private FakturautstederService fakturautstederService;

    @Autowired
    private SalgsordregruppeCache salgsordregruppeCache;

    @Getter
    private List<VareResource> varer;

    public VareService(ProductCache cache, VareMapper vareMapper, FakturautstederService fakturautstederService, SalgsordregruppeCache salgsordregruppeCache) {
        this.cache = cache;
        this.mapper = vareMapper;
        this.fakturautstederService = fakturautstederService;
        this.salgsordregruppeCache = salgsordregruppeCache;
    }

    @Scheduled(initialDelay = 10000, fixedDelayString = "${fint.xledger.kodeverk.refresh-interval:1500000}")
    public void refresh() {
        log.info("Refreshing Vare...");

        varer = new ArrayList<>();

        for (FakturautstederResource fakturautsteder : fakturautstederService.getFakturautstedere()) {
            // Filtere pr fakturautsteder
            // Vare."code": "500100-1011",

            String salgsordregruppeCode = SellerUtil.extractSalgsordregruppeDbId(fakturautsteder.getSystemId().getIdentifikatorverdi());

            for (Node vare : filterVarerByCode(salgsordregruppeCode + "-")) {
                varer.add(mapper.toFint(vare, fakturautsteder));
            }
        }

        log.info("End refreshing Vare");
    }

    private List<Node> filterVarerByCode(String startsWith) {
        return cache
                .get()
                .stream()
                .filter(v -> v.getCode().startsWith(startsWith))
                .collect(Collectors.toList());
    }
}
