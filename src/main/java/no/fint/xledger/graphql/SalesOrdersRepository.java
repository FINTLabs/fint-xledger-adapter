package no.fint.xledger.graphql;

import lombok.extern.slf4j.Slf4j;
import no.fint.xledger.model.salesOrders.EdgesItem;
import no.fint.xledger.model.salesOrders.Node;
import no.fint.xledger.model.salesOrders.SalesOrdersResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class SalesOrdersRepository extends GraphQLRepository {
    private final XledgerWebClient xledgerWebClient;

    public SalesOrdersRepository(XledgerWebClient xledgerWebClient) {
        this.xledgerWebClient = xledgerWebClient;
    }

    public Node getSalesOrderByXorder(String xorder) {
        return getSalesOrder(xorder, "xorder");
    }

    public Node getSalesOrderByInvoiceNumber(String invoiceNumber) {
        return getSalesOrder(invoiceNumber, "invoiceNumber");
    }

    private Node getSalesOrder(String filterValue, String filterField) {
        GraphQLQuery query = getQuery(filterValue, filterField);
        SalesOrdersResponse graphQLData = xledgerWebClient.post(SalesOrdersResponse.class, query).block();

        List<EdgesItem> edgesItems = graphQLData.getResult().getSalesOrders().getEdges();
        if (edgesItems == null || edgesItems.size() == 0) return null;

        if (edgesItems.size() > 1) {
            log.warn("There are multiple salesOrder with same " + filterField + " = " + filterValue);
        }

        return edgesItems.get(0).getNode();
    }

    private GraphQLQuery getQuery(String filterValue, String filterField) {
        return new GraphQLQuery(String.format("{\n" +
                        "  salesOrders(last: 1, filter: {%s: %s}) {\n" +
                        "    edges {\n" +
                        "      node {\n" +
                        "        xorder\n" +
//                        "        details {\n" +
//                        "          amount\n" +
//                        "          quantity\n" +
//                        "          unitPrice\n" +
//                        "          product {\n" +
//                        "            description\n" +
//                        "            code\n" +
//                        "            dbId\n" +
//                        "          }\n" +
//                        "          text\n" +
//                        "        }\n" +
                        "        invoiceNumber\n" +
                        "        amount\n" +
                        "        dbId\n" +
                        "        deliveryDate\n" +
                        "        dueDate\n" +
                        "        invoiceAmount\n" +
                        "        remainingAmount\n" +
                        "        remainingInvoice\n" +
                        "        soOrderType {\n" +
                        "          dbId\n" +
                        "          name\n" +
                        "        }\n" +
                        "        subledgerDbId\n" +
                        "        taxAmount\n" +
                        "        text\n" +
                        "        yourReference\n" +
                        "        invoiceDate\n" +
                        "        customer {\n" +
                        "          description\n" +
                        "        }\n" +
                        "      }\n" +
                        "    }\n" +
                        "  }\n" +
                        "}\n"
                , filterField, nullOrQuote(filterValue)));
    }
}
