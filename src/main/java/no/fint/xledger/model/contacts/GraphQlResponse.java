package no.fint.xledger.model.contacts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


public @Data class GraphQlResponse {
	@JsonProperty("data")
	private GraphQlResponse.Result result;

	@Data
	public static class Result {
		private EntityContacts entityContacts;
	}
}