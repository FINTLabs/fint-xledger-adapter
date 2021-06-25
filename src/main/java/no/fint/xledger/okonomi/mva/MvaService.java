package no.fint.xledger.okonomi.mva;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.okonomi.kodeverk.MerverdiavgiftResource;
import no.fint.xledger.graphql.GraphQLQuery;
import no.fint.xledger.graphql.XledgerWebClientRepository;
import no.fint.xledger.model.objectValues.EdgesItem;
import no.fint.xledger.model.objectValues.GraphQLResponse;
import no.fint.xledger.model.objectValues.Node;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MvaService {

    @Value("${fint.xledger.graphql.pageSize:500}")
    private int pageSize;

    private final XledgerWebClientRepository xledgerWebClientRepository;
    private final MvaFactory mvaFactory;
    private final int ObjectKindForMerverdiavgift = 14;

    @Getter
    private List<MerverdiavgiftResource> mva;

    public MvaService(XledgerWebClientRepository xledgerWebClientRepository, MvaFactory mvaFactory) {
        this.xledgerWebClientRepository = xledgerWebClientRepository;
        this.mvaFactory = mvaFactory;
    }

    @Scheduled(initialDelay = 9000, fixedDelayString = "${fint.xledger.kodeverk.refresh-interval:1500000}")
    public void refresh() {
        log.info("Refreshing Merverdiavgift...");
        mva = queryMva()
                .stream()
                .map(mvaFactory::toFint)
                .collect(Collectors.toList());
        log.info("End refreshing Merverdiavgift");
    }

    public List<Node> queryMva() {
        GraphQLQuery query = getQuery();
        boolean hasNext;
        List<Node> mvaLines = new ArrayList<>();

        do {
            GraphQLResponse graphQLData = xledgerWebClientRepository
                    .post(GraphQLResponse.class, query)
                    .block();
            List<EdgesItem> edges = Objects.requireNonNull(graphQLData).getResult().getObjectValues().getEdges();

            hasNext = Objects.requireNonNull(graphQLData).getResult().getObjectValues().getPageInfo().isHasNextPage();
            query = getCursorQuery(edges.get(edges.size() - 1).getCursor());

            mvaLines.addAll(graphQLData
                    .getResult()
                    .getObjectValues()
                    .getEdges()
                    .stream().map(EdgesItem::getNode)
                    .collect(Collectors.toList()));

        } while (hasNext);

        log.info("Found {} merverdiavgift rows", mvaLines.size());
        return mvaLines;
    }

    private GraphQLQuery getCursorQuery(String cursor) {
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
                "}", pageSize, cursor, ObjectKindForMerverdiavgift));
    }

    private GraphQLQuery getQuery() {
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
                        "}", pageSize, ObjectKindForMerverdiavgift));
    }
}
