package no.fint.xledger.model.objectValues;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ObjectValuesResponse {

	@JsonProperty("data")
	private ObjectValuesResponse.Result result;

	@Data
	public static class Result {
		private ObjectValues objectValues;
	}
}