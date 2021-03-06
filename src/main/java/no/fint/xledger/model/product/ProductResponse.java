package no.fint.xledger.model.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

public @Data class ProductResponse {
	@JsonProperty("data")
	private Result result;

	@Data
	public static class Result {
		private Products products;
	}
}