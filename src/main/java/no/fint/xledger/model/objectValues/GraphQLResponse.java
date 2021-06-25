package no.fint.xledger.model.objectValues;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GraphQLResponse {

	@JsonProperty("data")
	private GraphQLResponse.Result result;

	@Data
	public static class Result {
		private ObjectValues objectValues;
	}
}