package no.fint.xledger.model.customer.customers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

public @Data class CustomersResponse{
	@JsonProperty("data")
	private CustomersResponse.Result result;

	@Data
	public static class Result {
		private Customers customers;
	}
}