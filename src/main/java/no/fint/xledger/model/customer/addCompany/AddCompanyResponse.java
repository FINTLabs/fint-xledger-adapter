package no.fint.xledger.model.customer.addCompany;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import no.fint.xledger.model.customer.companies.Companies;

public @Data class AddCompanyResponse {
	@JsonProperty("data")
	private AddCompanyResponse.Result result;

	@Data
	public static class Result {
		private AddCompany addCompany;
	}
}