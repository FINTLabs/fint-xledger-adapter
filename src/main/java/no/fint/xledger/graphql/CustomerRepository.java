package no.fint.xledger.graphql;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class CustomerRepository {
    private final XledgerWebClient xledgerWebClient;

    public CustomerRepository(XledgerWebClient xledgerWebClient) {
        this.xledgerWebClient = xledgerWebClient;
    }

    public int getCompanyDbId(String fodselsnummer) {
        GraphQLQuery query = getCompaniesQuery(fodselsnummer);
        no.fint.xledger.model.customer.companies.GraphQLResponse graphQLData = xledgerWebClient.post(no.fint.xledger.model.customer.companies.GraphQLResponse.class, query).block();

        List<no.fint.xledger.model.customer.companies.EdgesItem> edgesItems = graphQLData.getCompanies().getEdges();
        if (edgesItems == null || edgesItems.size() == 0) return 0;

        if (edgesItems.size() > 1) {
            log.warn("There are multiple companies with same compayNumber. See company with dbId=" + edgesItems.get(0).getNode().getDbId());
        }

        return edgesItems.get(0).getNode().getDbId();
    }

    public int addCompany(String companyNumber, String ownerDbId) {
        GraphQLQuery query = addCompanyQuery(companyNumber, ownerDbId);
        no.fint.xledger.model.customer.addCompany.GraphQLResponse graphQLData = xledgerWebClient.post(no.fint.xledger.model.customer.addCompany.GraphQLResponse.class, query).block();
        return graphQLData.getAddCompany().getDbId();
    }

    public int getCustomerDbId(String companyDbId) {
        GraphQLQuery query = getCustomerQuery(companyDbId);
        no.fint.xledger.model.customer.customers.CustomersResponse graphQLData = xledgerWebClient.post(no.fint.xledger.model.customer.customers.CustomersResponse.class, query).block();

        List<no.fint.xledger.model.customer.customers.EdgesItem> edgesItems = graphQLData.getResult().getCustomers().getEdges();
        if (edgesItems == null || edgesItems.size() == 0) return 0;

        if (edgesItems.size() > 1) {
            log.warn("There are multiple companies with same compayNumber. See company with dbId=" + edgesItems.get(0).getNode().getDbId());
        }

        return edgesItems.get(0).getNode().getDbId();
    }

    private GraphQLQuery getCompaniesQuery(String fodselsnummer) {
        return new GraphQLQuery(String.format("{\n" +
                        "  companies(first: 2, filter: {companyNumber:\"%s\"}) {\n" +
                        "    edges {\n" +
                        "      node {\n" +
                        "        companyNumber\n" +
                        "        dbId\n" +
                        "      }\n" +
                        "    }\n" +
                        "  }\n" +
                        "}\n",
                fodselsnummer));
    }

    private GraphQLQuery addCompanyQuery(String companyNumber, String ownerDbId) {
        return new GraphQLQuery(String.format("mutation {\n" +
                        "  addCompany(companyNumber: \"%s\", ownerDbId: \"%s\") {\n" +
                        "    dbId\n" +
                        "  }\n" +
                        "}\n",
                companyNumber,
                ownerDbId
        ));
    }

    private GraphQLQuery getCustomerQuery(String companyDbId) {
        return new GraphQLQuery(String.format("{\n" +
                "  customers(filter: {companyDbId: %s}, first: 1) {\n" +
                "    edges {\n" +
                "      node {\n" +
                "        dbId\n" +
                "        description\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}",
                companyDbId
        ));
    }
}
