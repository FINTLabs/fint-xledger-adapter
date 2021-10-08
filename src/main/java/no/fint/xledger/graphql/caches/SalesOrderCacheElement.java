package no.fint.xledger.graphql.caches;

import lombok.Getter;
import lombok.Setter;
import no.fint.xledger.model.salesOrders.Node;

import java.time.LocalDateTime;

public class SalesOrderCacheElement {
    @Getter
    @Setter
    private Node node;

    @Getter
    @Setter
    private LocalDateTime dateTime;

    public SalesOrderCacheElement(Node node, LocalDateTime dateTime) {
        this.node = node;
        this.dateTime = dateTime;
    }
}
