package no.fint.xledger.graphql.caches;

import lombok.extern.slf4j.Slf4j;
import no.fint.xledger.model.salesOrders.Node;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
/****
 * This cache is different from Cache that it will not fill all data with one query.
 * This cache will be filled one by one instance.
 ****/
public class SalesOrderCache {

    private final int hoursToCache;

    private final Map<String, SalesOrderCacheElement> cache;

    public SalesOrderCache(int hoursToCache) {
        this.hoursToCache = hoursToCache;
        cache = new HashMap<>();
    }

    public Node get(String id) {
        if (StringUtils.isBlank(id)) return null;

        try {
            if (!cache.containsKey(id)) return null;
            SalesOrderCacheElement element = cache.get(id);
            return needsUpdate(element) ? null : element.getNode();
        } catch (Exception e) {
            log.error("Error in get from cache: " + e.getMessage());
            return null;
        }
    }

    public void update(String id, Node salesOrder) {
        try {
            if (salesOrder == null) return;
            if (StringUtils.isBlank(id)) return;

            cache.put(id, new SalesOrderCacheElement(salesOrder, LocalDateTime.now()));
        } catch (Exception e) {
            log.error("Error in update cache: " + e.getMessage());
        }
    }

    private boolean needsUpdate(SalesOrderCacheElement element) {
        return element.getDateTime().plusHours(hoursToCache).compareTo(LocalDateTime.now()) < 0;
    }
}
