package no.fint.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

public class XledgerWebClientRepository {

    public XledgerWebClientRepository() {
    }

    @Value("${fint.xledger.graphql.endpoint:https://demo.xledger.net/graphql}")
    private String xledgerGraphQLEndpoint;

    @Value("${fint.xledger.graphql.token}")
    private String xledgerToken;

    public <T> Mono<T> post(Class<T> clazz, GraphQLQuery graphQLQuery) {
        return
                createWebClient().post()
                        .uri(xledgerGraphQLEndpoint)
                        .bodyValue(graphQLQuery)
                        .retrieve()
                        .bodyToMono(clazz);

    }

    private WebClient createWebClient(){
        return WebClient.builder()
        .defaultHeaders(header -> header.setBearerAuth("token " + xledgerToken))
                .build();
    }
}
