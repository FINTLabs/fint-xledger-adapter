package no.fint.xledger.graphql;

import lombok.extern.slf4j.Slf4j;
import no.fint.xledger.model.contacts.EdgesItem;
import no.fint.xledger.model.contacts.GraphQlResponse;
import no.fint.xledger.model.contacts.Node;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class ContactRepository {

    @Value("${fint.xledger.graphql.pageSize:500}")
    private int pageSize;

    private final XledgerWebClient xledgerWebClient;

    public ContactRepository(XledgerWebClient xledgerWebClient) {
        this.xledgerWebClient = xledgerWebClient;
    }

    public List<Node> queryContacts() {
        GraphQLQuery query = getQuery();
        boolean hasNext;
        List<Node> products = new ArrayList<>();

        do {
            GraphQlResponse graphQLData = xledgerWebClient
                    .post(GraphQlResponse.class, query)
                    .block();
            List<EdgesItem> edges = Objects.requireNonNull(graphQLData).getResult().getEntityContacts().getEdges();

            hasNext = Objects.requireNonNull(graphQLData).getResult().getEntityContacts().getPageInfo().isHasNextPage();
            query = getCursorQuery(edges.get(edges.size() - 1).getCursor());

            products.addAll(graphQLData
                    .getResult()
                    .getEntityContacts()
                    .getEdges()
                    .stream().map(EdgesItem::getNode)
                    .collect(Collectors.toList()));

        } while (hasNext);


        log.info("Found {} contacts", products.size());
        return products;
    }

    private GraphQLQuery getCursorQuery(String cursor) {
        return new GraphQLQuery(String.format("{\n" +
                "  entityContacts(first: %d, after: \"%s\") {\n" +
                "    edges {\n" +
                "      node {\n" +
                "        contact {\n" +
                "          dbId\n" +
                "          code\n" +
                "          name\n" +
                "          phone\n" +
                "          email\n" +
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
                "  entityContacts(first: %d) {\n" +
                "    edges {\n" +
                "      node {\n" +
                "        contact {\n" +
                "          dbId\n" +
                "          code\n" +
                "          name\n" +
                "          phone\n" +
                "          email\n" +
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

