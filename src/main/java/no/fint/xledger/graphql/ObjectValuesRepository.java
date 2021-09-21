package no.fint.xledger.graphql;

import lombok.extern.slf4j.Slf4j;
import no.fint.xledger.model.objectValues.EdgesItem;
import no.fint.xledger.model.objectValues.GraphQLResponse;
import no.fint.xledger.model.objectValues.Node;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class ObjectValuesRepository {
    @Value("${fint.xledger.graphql.pageSize:500}")
    private int pageSize;

    private final XledgerWebClient xledgerWebClient;

    public ObjectValuesRepository(XledgerWebClient xledgerWebClient) {
        this.xledgerWebClient = xledgerWebClient;
    }

    public List<Node> get(int objectKind) {
        GraphQLQuery query = getQuery(objectKind);
        boolean hasNext;
        List<Node> mvaLines = new ArrayList<>();

        do {
            GraphQLResponse graphQLData = xledgerWebClient
                    .post(GraphQLResponse.class, query)
                    .block();
            List<EdgesItem> edges = Objects.requireNonNull(graphQLData).getResult().getObjectValues().getEdges();

            hasNext = Objects.requireNonNull(graphQLData).getResult().getObjectValues().getPageInfo().isHasNextPage();
            query = getCursorQuery(edges.get(edges.size() - 1).getCursor(), objectKind);

            mvaLines.addAll(graphQLData
                    .getResult()
                    .getObjectValues()
                    .getEdges()
                    .stream().map(EdgesItem::getNode)
                    .collect(Collectors.toList()));

        } while (hasNext);

        log.info("Found {} objectValues of objectKind " + objectKind, mvaLines.size());
        return mvaLines;
    }

    private GraphQLQuery getCursorQuery(String cursor, int objectKind) {
        return new GraphQLQuery(String.format("{\n" +
                "  objectValues(last: %d, after: \"%s\", filter: {objectKindDbId: %d}) {\n" +
                "    edges {\n" +
                "      node {\n" +
                "        dbId\n" +
                "        code\n" +
                "        description\n" +
                "        code2 {\n" +
                "          name\n" +
                "        }\n" +
                "      }\n" +
                "      cursor\n" +
                "    }\n" +
                "    pageInfo {\n" +
                "      hasNextPage\n" +
                "      hasPreviousPage\n" +
                "    }\n" +
                "  }\n" +
                "}", pageSize, cursor, objectKind));
    }

    private GraphQLQuery getQuery(int objectKind) {
        return new GraphQLQuery(String.format("{\n" +
                "  objectValues(last: %d, filter: {objectKindDbId: %d}) {\n" +
                "    edges {\n" +
                "      node {\n" +
                "        dbId\n" +
                "        code\n" +
                "        description\n" +
                "        code2 {\n" +
                "          name\n" +
                "        }\n" +
                "      }\n" +
                "      cursor\n" +
                "    }\n" +
                "    pageInfo {\n" +
                "      hasNextPage\n" +
                "      hasPreviousPage\n" +
                "    }\n" +
                "  }\n" +
                "}", pageSize, objectKind));
    }
}
