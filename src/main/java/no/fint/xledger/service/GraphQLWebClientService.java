package no.fint.xledger.service;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Input;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.apollographql.apollo.exception.ApolloParseException;
import lombok.extern.slf4j.Slf4j;
import no.fint.GetVareregisterQuery;
import no.fint.model.Products;
import no.fint.repository.GraphQLQuery;
import no.fint.repository.XledgerWebClientRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
public class GraphQLWebClientService {

    @PostConstruct
    public void query() {
        log.info("This is running");
        getProducts();
    }

    private void getProducts()
    {
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

         Mono<Products> data =  new XledgerWebClientRepository().post(Products.class, graphQLQuery)
                 .map(products -> Optional.ofNullable(products.getEdges())
                         .map(edgesItems -> Optional.ofNullable(edgesItems))
                 .orElseThrow(new Exception()))
                 .onErrorResume(error -> {
                     log.error("I have no idea");
                     return Mono.empty();
                 })                      ;
         log.info("hello");

    }
}
