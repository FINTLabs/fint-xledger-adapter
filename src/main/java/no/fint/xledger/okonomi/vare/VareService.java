package no.fint.xledger.okonomi.vare;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.okonomi.faktura.FakturautstederResource;
import no.fint.model.resource.okonomi.kodeverk.VareResource;
import no.fint.xledger.graphql.caches.ProductCache;
import no.fint.xledger.graphql.caches.SalgsordregruppeCache;
import no.fint.xledger.model.product.Node;
import no.fint.xledger.okonomi.ConfigProperties;
import no.fint.xledger.okonomi.SellerUtil;
import no.fint.xledger.okonomi.fakturautsteder.FakturautstederService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class VareService {

    private final ProductCache cache;
    private final VareMapper mapper;

    @Autowired
    private FakturautstederService fakturautstederService;

    @Autowired
    private SalgsordregruppeCache salgsordregruppeCache;

    private final ConfigProperties configProperties;

    @Getter
    private List<VareResource> varer;

    public VareService(ProductCache cache, VareMapper vareMapper, FakturautstederService fakturautstederService, SalgsordregruppeCache salgsordregruppeCache, ConfigProperties configProperties) {
        this.cache = cache;
        this.mapper = vareMapper;
        this.fakturautstederService = fakturautstederService;
        this.salgsordregruppeCache = salgsordregruppeCache;
        this.configProperties = configProperties;
    }

    @Scheduled(initialDelay = 10000, fixedDelayString = "${fint.xledger.kodeverk.refresh-interval:1500000}")
    public void refresh() {
        log.info("Refreshing Vare...");

        varer = new ArrayList<>();

        for (FakturautstederResource fakturautsteder : fakturautstederService.getFakturautstedere()) {
            // Filtere pr fakturautsteder
            // Vare."code": "500100-1011",

            String salgsordregruppeDbId = SellerUtil.extractSalgsordregruppeDbId(fakturautsteder.getSystemId().getIdentifikatorverdi());
            String salgsordregruppeCode = salgsordregruppeCache.getCodeByDbId(salgsordregruppeDbId);

            for (Node vare : cache.filterVarerByCode(salgsordregruppeCode, configProperties.getDigistToCompareSalgsordregruppeAndProduct())) {
                varer.add(mapper.toFint(vare, fakturautsteder));
            }
        }

        log.info("Found " + varer.size() + " varer");
        log.info("End refreshing Vare");
    }


}
