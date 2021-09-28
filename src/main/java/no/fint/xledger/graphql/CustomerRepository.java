package no.fint.xledger.graphql;

import lombok.extern.slf4j.Slf4j;
import no.fint.xledger.model.customer.CustomerDTO;
import no.fint.xledger.model.customer.addCompany.AddCompanyResponse;
import no.fint.xledger.model.customer.companies.CompaniesResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class CustomerRepository extends GraphQLRepository {
    private final XledgerWebClient xledgerWebClient;

    public CustomerRepository(XledgerWebClient xledgerWebClient) {
        this.xledgerWebClient = xledgerWebClient;
    }

    public int getCompanyDbId(String fodselsnummer) {
        GraphQLQuery query = getCompaniesQuery(fodselsnummer);
        CompaniesResponse graphQLData = xledgerWebClient.post(CompaniesResponse.class, query).block();

        List<no.fint.xledger.model.customer.companies.EdgesItem> edgesItems = graphQLData.getResult().getCompanies().getEdges();
        if (edgesItems == null || edgesItems.size() == 0) return 0;

        if (edgesItems.size() > 1) {
            log.warn("There are multiple companies with same compayNumber. See company with dbId=" + edgesItems.get(0).getNode().getDbId());
        }

        return edgesItems.get(0).getNode().getDbId();
    }

    public int addCompany(String companyNumber, String ownerDbId) {
        GraphQLQuery query = addCompanyQuery(companyNumber, ownerDbId);
        AddCompanyResponse graphQLData = xledgerWebClient.post(AddCompanyResponse.class, query).block();
        return graphQLData.getResult().getAddCompany().getDbId();
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

    public int addCustomer(CustomerDTO customerDTO) {
        GraphQLQuery query = addCustomerQuery(customerDTO);
        no.fint.xledger.model.customer.addCustomer.AddCustomerResponse graphQLData = xledgerWebClient.post(no.fint.xledger.model.customer.addCustomer.AddCustomerResponse.class, query).block();
        return graphQLData.getResult().getAddCustomer().getDbId();
    }

    public int updateCustomer(CustomerDTO customerDTO, int customerDbId) {
        GraphQLQuery query = updateCustomerQuery(customerDTO, customerDbId);
        no.fint.xledger.model.customer.updateCustomer.UpdateCustomerResponse graphQLData = xledgerWebClient.post(no.fint.xledger.model.customer.updateCustomer.UpdateCustomerResponse.class, query).block();
        return graphQLData.getResult().getUpdateCustomer().getDbId();
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

    private GraphQLQuery addCustomerQuery(CustomerDTO customerDTO) {
        return new GraphQLQuery(String.format("mutation {\n" +
                        "  addCustomer(" +
                        "description: %s, " +
                        "email: %s, " +
                        "streetAddress: %s, " +
                        "zipCode: %s, " +
                        "place: %s, " +
                        "phone: %s, " +
                        "companyDbId: %s, " +
                        "companyNumber: %s" +
                        ") { \n" +
                        "    dbId\n" +
                        "  }\n" +
                        "}",
                nullOrQuote(customerDTO.getDescription()),
                nullOrQuote(customerDTO.getEmail()),
                nullOrQuote(customerDTO.getStreetAdress()),
                nullOrQuote(customerDTO.getZipCode()),
                nullOrQuote(customerDTO.getPlace()),
                nullOrQuote(customerDTO.getPhone()),
                customerDTO.getCompanyDbId(),
                nullOrQuote(customerDTO.getCompanyNumber())
        ));
    }

    private GraphQLQuery updateCustomerQuery(CustomerDTO customerDTO, int customerDbId) {
        return new GraphQLQuery(String.format("mutation {\n" +
                        "  updateCustomer(" +
                        "dbId: %s, " +
                        "description: %s, " +
                        "email: %s, " +
                        "streetAddress: %s, " +
                        "zipCode: %s, " +
                        "place: %s, " +
                        "phone: %s, " +
                        "companyDbId: %s, " +
                        "companyNumber: %s" +
                        ") { \n" +
                        "    dbId\n" +
                        "  }\n" +
                        "}",
                customerDbId,
                nullOrQuote(customerDTO.getDescription()),
                nullOrQuote(customerDTO.getEmail()),
                nullOrQuote(customerDTO.getStreetAdress()),
                nullOrQuote(customerDTO.getZipCode()),
                nullOrQuote(customerDTO.getPlace()),
                nullOrQuote(customerDTO.getPhone()),
                customerDTO.getCompanyDbId(),
                nullOrQuote(customerDTO.getCompanyNumber())
        ));
    }
}