package no.fint.xledger.model.invoiceBaseItem;


import lombok.Getter;
import lombok.Setter;

public class InvoiceBaseItemDTO {

    @Getter
    @Setter
    private String subledgerDbId;

    @Getter
    @Setter
    private String productDbId;

    @Getter
    @Setter
    private String glObject1DbId;

    @Getter
    @Setter
    private String glObject2DbId;

    @Getter
    @Setter
    private String glObject3DbId;

    @Getter
    @Setter
    private String glObject4DbId;

    @Getter
    @Setter
    private String glObject5DbId;

    @Getter
    @Setter
    private String yourReference;

    @Getter
    @Setter
    private int ourRefDbId;

    @Getter
    @Setter
    private String headerInfo;

    @Getter
    @Setter
    private String extOrder;

    @Getter
    @Setter
    private int invoiceLayoutDbId;

    @Getter
    @Setter
    private String currencyDbId;

    @Getter
    @Setter
    private int ownerDbId;

    @Getter
    @Setter
    private int fieldGroupDbId;

    @Getter
    @Setter
    private float unitPrice;

    @Getter
    @Setter
    private float quantity;

    @Getter
    @Setter
    private int lineNumber;
}
