package no.fint.xledger.model.salesOrders;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

public @Data class SalesOrdersResponse{
	@JsonProperty("data")
	private SalesOrdersResponse.Result result;

	@Data
	public static class Result {
		private SalesOrders salesOrders;
	}
}