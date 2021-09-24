package no.fint.xledger.model.customer.addCustomer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

public @Data
class AddCustomerResponse {
    @JsonProperty("data")
    private AddCustomerResponse.Result result;

    @Data
    public static class Result {
        private AddCustomer addCustomer;
    }
}