package no.fint.xledger.model.customer.updateCustomer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

public @Data
class UpdateCustomerResponse {

    @JsonProperty("data")
    private UpdateCustomerResponse.Result result;

    @Data
    public static class Result {
        private UpdateCustomer updateCustomer;
    }
}