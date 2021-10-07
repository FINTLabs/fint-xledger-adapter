package no.fint.xledger.okonomi.faktura;

import lombok.extern.slf4j.Slf4j;
import no.fint.xledger.model.salesOrders.Node;
import no.fint.xledger.okonomi.ConfigProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class FakturaCache {

    private final ConfigProperties configProperties;

    private Map<String, FakturaCacheElement> cache;

    public FakturaCache(ConfigProperties configProperties) {
        this.configProperties = configProperties;
        cache = new HashMap<>();
    }

    public Node get(String fakturanummer) {
        try {
            if (!cache.containsKey(fakturanummer)) return null;
            FakturaCacheElement element = cache.get(fakturanummer);
            return needsUpdate(element) ? null : element.getNode();
        } catch (Exception e) {
            log.error("Error in get from cache: " + e.getMessage());
            return null;
        }
    }

    public void update(String fakturanummer, Node salesOrder) {
        try {
            if (salesOrder == null) return;
            if (StringUtils.isBlank(fakturanummer)) return;

            cache.put(fakturanummer, new FakturaCacheElement(salesOrder, LocalDateTime.now()));
        } catch (Exception e) {
            log.error("Error in update cache: " + e.getMessage());
        }
    }

    private boolean needsUpdate(FakturaCacheElement element) {
        return element.getDateTime().plusHours(configProperties.getHoursToCacheInvoice()).compareTo(LocalDateTime.now()) < 0;
    }
}
