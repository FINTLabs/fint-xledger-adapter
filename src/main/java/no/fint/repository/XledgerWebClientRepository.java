package no.fint.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@Repository
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
                        //.uri(xledgerGraphQLEndpoint)
                        .bodyValue(graphQLQuery)
                        .retrieve()
                        .bodyToMono(clazz);


    }

    private WebClient createWebClient(){
        return WebClient.builder()
                .baseUrl(xledgerGraphQLEndpoint)
        .defaultHeaders(header -> header.setBearerAuth("token " + xledgerToken))
                .build();
    }
}
