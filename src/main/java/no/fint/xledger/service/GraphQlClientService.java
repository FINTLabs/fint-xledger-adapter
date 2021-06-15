package no.fint.xledger.service;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Input;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.apollographql.apollo.exception.ApolloParseException;
import lombok.extern.slf4j.Slf4j;
import no.fint.GetVareregisterQuery;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

@Slf4j
@Service
public class GraphQlClientService {

    private final ApolloClient apolloClient;

    public GraphQlClientService(ApolloClient apolloClient) {
        this.apolloClient = apolloClient;
    }

    @PostConstruct
    public void query() {
        apolloClient.query(new GetVareregisterQuery(Input.fromNullable(100)))
                .enqueue(new ApolloCall.Callback<GetVareregisterQuery.Data>() {
                    @Override
                    public void onParseError(@NotNull ApolloParseException e) {
                        super.onParseError(e);
                    }

                    @Override
                    public void onResponse(@NotNull Response<GetVareregisterQuery.Data> response) {
                        log.info("Apollo", "Launch site: " + response.getData());
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        log.info("Apollo", "Error", e);
                    }
                });
    }
}
