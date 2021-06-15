package no.fint.xledger;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.cache.http.DiskLruHttpCacheStore;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class GraphQLClientConfig {

    @Value("${fint.xledger.graphql.endpoint:https://demo.xledger.net/graphql}")
    private String xledgerGraphQLEndpoint;

    @Value("${fint.xledger.graphql.token}")
    private String xledgerToken;

    @Bean
    public ApolloClient apolloClient() {

        return ApolloClient.builder()
                .serverUrl(xledgerGraphQLEndpoint)
                .okHttpClient(okHttpClient())
                        .build();

    }

    private OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder builder = original.newBuilder().method(original.method(), original.body());
                    builder.header("Authorization", "token " + xledgerToken);
                    return chain.proceed(builder.build());
                })
                .addInterceptor(new LoggingInterceptor())
                .build();
    }


}
