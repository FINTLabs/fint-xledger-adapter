package no.fint.xledger.graphql;

import lombok.extern.slf4j.Slf4j;
import no.fint.xledger.model.invoiceBaseItem.InvoiceBaseItemDTO;
import no.fint.xledger.model.objectValues.GraphQLResponse;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class InvoiceBaseItemRepository extends GraphQLRepository {
    private final XledgerWebClient xledgerWebClient;

    public InvoiceBaseItemRepository(XledgerWebClient xledgerWebClient) {
        this.xledgerWebClient = xledgerWebClient;
    }

    public void add(InvoiceBaseItemDTO invoiceItem) {
        GraphQLQuery query = getQuery(invoiceItem);

        GraphQLResponse graphQLData = xledgerWebClient
                .post(GraphQLResponse.class, query)
                .block();

        //mvaLines.addAll(graphQLData
        //        .getResult()
        //        .getObjectValues()
        //        .getEdges()
        //        .stream().map(EdgesItem::getNode)
        //        .collect(Collectors.toList()));

        // TODO Handle respons from addInvoiceBaseItem
    }

    private GraphQLQuery getQuery(InvoiceBaseItemDTO invoiceItem) {
        return new GraphQLQuery(String.format("mutation {\n" +
                        "addInvoiceBaseItem(\n" +
                        "subledgerDbId: %s,\n" +
                        "productDbId: %s,\n" +
                        "glObject1DbId: %s,\n" +
                        "glObject2DbId: %s,\n" +
                        "glObject3DbId: %s,\n" +
                        "glObject4DbId: %s,\n" +
                        "glObject5DbId: %s,\n" +
                        "yourReference: \"\",\n" + // will be used when we get parent information
                        "ourRefDbId: %d,\n" +
                        "headerInfo: %s,\n" +
                        "approved: true,\n" +
                        "extOrder: %d,\n" +
                        "invoiceLayoutDbId: %d,\n" +
                        "currencyDbId: %s,\n" +
                        "ownerDbId: %d,\n" +
                        "fieldGroupDbId: %d,\n" +
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
                invoiceItem.getOurRefDbId(),
                quote(invoiceItem.getHeaderInfo()),
                invoiceItem.getExtOrder(),
                invoiceItem.getInvoiceLayoutDbId(),
                quote(invoiceItem.getCurrencyDbId()),
                invoiceItem.getOwnerDbId(),
                invoiceItem.getFieldGroupDbId(),
                invoiceItem.getUnitPrice(),
                invoiceItem.getQuantity(),
                invoiceItem.getLineNumber()));
    }
}
