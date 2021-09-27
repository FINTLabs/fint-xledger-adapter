package no.fint.xledger.model.customer.companies;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

public @Data class CompaniesResponse {

	@JsonProperty("data")
	private no.fint.xledger.model.customer.companies.Companies companies;

	@Data
	public static class Result {
		private Companies companies;
	}
}