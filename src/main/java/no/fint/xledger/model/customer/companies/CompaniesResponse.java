package no.fint.xledger.model.customer.companies;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

public @Data class CompaniesResponse {

	@JsonProperty("data")
	private CompaniesResponse.Result result;

	@Data
	public static class Result {
		private Companies companies;
	}
}