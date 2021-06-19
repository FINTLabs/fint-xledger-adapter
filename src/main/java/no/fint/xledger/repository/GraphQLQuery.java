package no.fint.xledger.repository;

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

