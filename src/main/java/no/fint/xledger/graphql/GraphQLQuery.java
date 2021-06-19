package no.fint.xledger.graphql;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class GraphQLQuery {
    @NonNull
    private String query;
    private Object variables;
}

