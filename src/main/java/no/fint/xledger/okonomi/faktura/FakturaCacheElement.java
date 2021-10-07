package no.fint.xledger.okonomi.faktura;

import lombok.Getter;
import lombok.Setter;
import no.fint.xledger.model.salesOrders.Node;

import java.time.LocalDateTime;

public class FakturaCacheElement {
    @Getter
    @Setter
    private Node node;

    @Getter
    @Setter
    private LocalDateTime dateTime;

    public FakturaCacheElement(Node node, LocalDateTime dateTime) {
        this.node = node;
        this.dateTime = dateTime;
    }
}
