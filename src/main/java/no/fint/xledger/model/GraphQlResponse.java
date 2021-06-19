package no.fint.xledger.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GraphQlResponse {
    @JsonProperty("data")
    private Result result;

    @Data
    public static class Result {
        private Products products;
    }
}