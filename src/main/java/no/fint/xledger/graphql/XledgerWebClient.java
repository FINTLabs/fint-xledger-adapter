package no.fint.xledger.graphql;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Repository
@Component
public class XledgerWebClient {

    public XledgerWebClient() {
    }

    @Value("${fint.xledger.graphql.endpoint:https://demo.xledger.net/graphql}")
    private String xledgerGraphQLEndpoint;

    @Value("${fint.xledger.graphql.token}")
    private String xledgerToken;

    public <T> Mono<T> post(Class<T> clazz, GraphQLQuery graphQLQuery) {
        return
                createWebClient().post()
                        .bodyValue(graphQLQuery)
                        .retrieve()
                        .bodyToMono(clazz);
    }

    private WebClient createWebClient() {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create().wiretap(true)
                ))
                .baseUrl(xledgerGraphQLEndpoint)
                .defaultHeaders(header -> header.add("Authorization", "token " + xledgerToken))
                .build();
    }
}
