package no.fint.xledger.model.customer.addCompany;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import no.fint.xledger.model.customer.companies.Companies;

public @Data class GraphQLResponse{
	@JsonProperty("data")
	private no.fint.xledger.model.customer.addCompany.AddCompany addCompany;

	@Data
	public static class Result {
		private AddCompany addCompany;
	}
}