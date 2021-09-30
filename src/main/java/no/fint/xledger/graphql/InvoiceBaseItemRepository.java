package no.fint.xledger.graphql;

import lombok.extern.slf4j.Slf4j;
import no.fint.xledger.model.invoiceBaseItem.InvoiceBaseItemDTO;
import no.fint.xledger.model.invoiceBaseItem.addInvoiceBaseItem.AddInvoiceBaseItemResponse;
import org.springframework.stereotype.Repository;

import java.util.Locale;

@Slf4j
@Repository
public class InvoiceBaseItemRepository extends GraphQLRepository {
    private final XledgerWebClient xledgerWebClient;

    public InvoiceBaseItemRepository(XledgerWebClient xledgerWebClient) {
        this.xledgerWebClient = xledgerWebClient;
    }

    public String add(InvoiceBaseItemDTO invoiceItem) {
        GraphQLQuery query = getAddQuery(invoiceItem);

        //log.info("Query that fails: " + query.getQuery());
        AddInvoiceBaseItemResponse graphQLData = xledgerWebClient.post(AddInvoiceBaseItemResponse.class, query).block();
        return graphQLData.getResult().getAddInvoiceBaseItem().getDbId();
    }

//    public String get() {
//        GraphQLQuery query = getAddQuery(invoiceItem);
//        AddInvoiceBaseItemResponse graphQLData = xledgerWebClient.post(AddInvoiceBaseItemResponse.class, query).block();
//        return graphQLData.getResult().getAddInvoiceBaseItem().getDbId();
//    }

    private GraphQLQuery getAddQuery(InvoiceBaseItemDTO invoiceItem) {
        return new GraphQLQuery(String.format(Locale.ROOT, "mutation {\n" +
                        "addInvoiceBaseItem(\n" +
                        "subledgerDbId: %s,\n" +
                        "productDbId: %s,\n" +
                        "glObject1DbId: %s,\n" +
                        "glObject2DbId: %s,\n" +
                        "glObject3DbId: %s,\n" +
                        "glObject4DbId: %s,\n" +
                        "glObject5DbId: %s,\n" +
                        "yourReference: %s,\n" +
                        "ourRefDbId: %d,\n" +
                        "headerInfo: %s,\n" +
                        "approved: true,\n" +
                        "extOrder: %s,\n" +
                        "invoiceLayoutDbId: %d,\n" +
                        "currencyDbId: %s,\n" +
                        "ownerDbId: %s,\n" +
                        "fieldGroupDbId: %s,\n" +
                        "text: %s,\n" +
                        "unitPrice: %f,\n" +
                        "quantity: %f,\n" +
                        "lineNumber: %d" +
                        ") {\n" +
                        "    dbId\n" +
                        "  }\n" +
                        "}",
                quote(invoiceItem.getSubledgerDbId()),
                quote(invoiceItem.getProductDbId()),
                nullOrQuote(invoiceItem.getGlObject1DbId()),
                nullOrQuote(invoiceItem.getGlObject2DbId()),
                nullOrQuote(invoiceItem.getGlObject3DbId()),
                nullOrQuote(invoiceItem.getGlObject4DbId()),
                nullOrQuote(invoiceItem.getGlObject5DbId()),
                nullOrQuote(invoiceItem.getYourReference()),
                invoiceItem.getOurRefDbId(),
                quote(invoiceItem.getHeaderInfo()),
                nullOrQuote(invoiceItem.getExtOrder()),
                invoiceItem.getInvoiceLayoutDbId(),
                quote(invoiceItem.getCurrencyDbId()),
                invoiceItem.getOwnerDbId(),
                invoiceItem.getFieldGroupDbId(),
                nullOrQuote(invoiceItem.getText()),
                invoiceItem.getUnitPrice(),
                invoiceItem.getQuantity(),
                invoiceItem.getLineNumber()));
    }

    private GraphQLQuery getGetQuery() {
        return new GraphQLQuery(String.format("{\n" +
                "  invoiceBaseItems(last: 500) {\n" +
                "    edges {\n" +
                "      node{\n" +
                "        dbId\n" +
                "        createdAt\n" +
                "        extOrder\n" +
                "        amount\n" +
                "        taxAmount\n" +
                "        subledger {\n" +
                "          dbId\n" +
                "          description\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}\n"));
    }
}
