package no.fint.xledger.service;

import lombok.extern.slf4j.Slf4j;
import no.fint.xledger.model.EdgesItem;
import no.fint.xledger.model.Node;
import no.fint.xledger.model.GraphQlResponse;
import no.fint.xledger.repository.GraphQLQuery;
import no.fint.xledger.repository.XledgerWebClientRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GraphQLWebClientService {

    @Value("${fint.xledger.graphql.pageSize:500}")
    private int pageSize;

    private final XledgerWebClientRepository xledgerWebClientRepository;

    public GraphQLWebClientService(XledgerWebClientRepository xledgerWebClientRepository) {
        this.xledgerWebClientRepository = xledgerWebClientRepository;
    }

    //@PostConstruct
    public void query() {
        log.info("This is running");
        getProducts();
    }

    public List<Node> getProducts() {
        GraphQLQuery query = getQuery();
        boolean hasNext;
        List<Node> products = new ArrayList<>();


        do {
            GraphQlResponse graphQLData = xledgerWebClientRepository
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


        log.info("Found {} products!", products.size());
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
