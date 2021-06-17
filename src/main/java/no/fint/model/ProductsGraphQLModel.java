package no.fint.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductsGraphQLModel {
    @JsonProperty("data")
    private Result result;


    @Data
    public static class Result {
        private Products products;
    }

    /*
    @Data
    public static class Products {
        private List<Edges> Edges;
    }

    @Data
    public static class Edges {
        private Node node;
        private String cursor;
    }

    @Data
    public static class Node {
        private Integer dbId;
        private String unit;
        private double salesPrice;
        private Date createdAt;
        private String description;
        private String code;
        private TaxRule taxRule;
    }

    @Data
    public static class TaxRule{
        private String code;
    }

     */
}
