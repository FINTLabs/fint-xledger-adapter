package no.fint.xledger.graphql;

import lombok.extern.slf4j.Slf4j;
import no.fint.xledger.model.product.EdgesItem;
import no.fint.xledger.model.product.GraphQlResponse;
import no.fint.xledger.model.product.Node;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class ProductRepository {
    @Value("${fint.xledger.graphql.pageSize:500}")
    private int pageSize;

    private final XledgerWebClient xledgerWebClient;

    public ProductRepository(XledgerWebClient xledgerWebClient) {
        this.xledgerWebClient = xledgerWebClient;
    }

    public List<Node> queryProducts() {
        GraphQLQuery query = getQuery();
        boolean hasNext;
        List<Node> products = new ArrayList<>();

        do {
            GraphQlResponse graphQLData = xledgerWebClient
                    .post(GraphQlResponse.class, query)
                    .block();
            List<EdgesItem> edges = Objects.requireNonNull(graphQLData).getResult().getProducts().getEdges();

            hasNext = Objects.requireNonNull(graphQLData).getResult().getProducts().getPageInfo().isHasNextPage();
            query = getCursorQuery(edges.get(edges.size() - 1).getCursor());

            products.addAll(graphQLData
                    .getResult()
                    .getProducts()
                    .getEdges()
                    .stream().map(EdgesItem::getNode)
                    .collect(Collectors.toList()));

        } while (hasNext);


        log.info("Found {} products", products.size());
        return products;
    }

    private GraphQLQuery getCursorQuery(String cursor) {
        return new GraphQLQuery(String.format("{\n" +
                "  products(first: %d, after: \"%s\") {\n" +
                "    edges {\n" +
                "      node {\n" +
                "        dbId\n" +
                "        unit\n" +
                "        salesPrice\n" +
                "        createdAt\n" +
                "        description\n" +
                "        code\n" +
                "        taxRule {\n" +
                "          code\n" +
                "        }\n" +
                "        glObject1 {\n" +
                "          dbId\n" +
                "          description\n" +
                "        }\n" +
                "        glObject2 {\n" +
                "          dbId\n" +
                "          description\n" +
                "        }\n" +
                "        glObject3 {\n" +
                "          dbId\n" +
                "          description\n" +
                "        }\n" +
                "        glObject4 {\n" +
                "          dbId\n" +
                "          description\n" +
                "        }\n" +
                "        glObject5 {\n" +
                "          dbId\n" +
                "          description\n" +
                "        }\n" +
                "      }\n" +
                "      cursor\n" +
                "    }\n" +
                "    pageInfo {\n" +
                "      hasNextPage\n" +
                "    }\n" +
                "  }\n" +
                "}", pageSize, cursor));
    }

    private GraphQLQuery getQuery() {
        return new GraphQLQuery(String.format("{\n" +
                "  products(first: %d) {\n" +
                "    edges {\n" +
                "      node {\n" +
                "        dbId\n" +
                "        unit\n" +
                "        salesPrice\n" +
                "        createdAt\n" +
                "        description\n" +
                "        code\n" +
                "        taxRule {\n" +
                "          code\n" +
                "        }\n" +
                "        glObject1 {\n" +
                "          dbId\n" +
                "          description\n" +
                "        }\n" +
                "        glObject2 {\n" +
                "          dbId\n" +
                "          description\n" +
                "        }\n" +
                "        glObject3 {\n" +
                "          dbId\n" +
                "          description\n" +
                "        }\n" +
                "        glObject4 {\n" +
                "          dbId\n" +
                "          description\n" +
                "        }\n" +
                "        glObject5 {\n" +
                "          dbId\n" +
                "          description\n" +
                "        }\n" +
                "      }\n" +
                "      cursor\n" +
                "    }\n" +
                "    pageInfo {\n" +
                "      hasNextPage\n" +
                "    }\n" +
                "  }\n" +
                "}", pageSize));
    }
}
