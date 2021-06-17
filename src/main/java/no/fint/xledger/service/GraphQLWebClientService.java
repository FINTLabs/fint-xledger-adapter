package no.fint.xledger.service;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.ProductsGraphQLModel;
import no.fint.repository.GraphQLQuery;
import no.fint.repository.XledgerWebClientRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GraphQLWebClientService {

    private final XledgerWebClientRepository xledgerWebClientRepository;

    public GraphQLWebClientService(XledgerWebClientRepository xledgerWebClientRepository) {
        this.xledgerWebClientRepository = xledgerWebClientRepository;
    }

    @PostConstruct
    public void query() {
        log.info("This is running");
        getProducts();
    }

    private void getProducts() {
        String query = "{\n" +
                "  products(first: 50, after: \"18364637.18364638.18364639\") {\n" +
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
                "}";

        GraphQLQuery graphQLQuery = new GraphQLQuery(query, Collections.singletonMap("brukernavn", "test"));

        boolean hasNext = true;


        do {
            ProductsGraphQLModel graphQLData = xledgerWebClientRepository
                    .post(ProductsGraphQLModel.class, graphQLQuery)
                    .block();
            hasNext = Objects.requireNonNull(graphQLData).getResult().getProducts().getPageInfo().isHasNextPage();
            graphQLData
                    .getResult()
                    .getProducts()
                    .getEdges()
                    .forEach(e -> log.info(e.getNode().getDescription()));
        } while (hasNext);

        log.info("hello");

    }
}
