package no.fint.xledger.model.contacts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


public @Data class ContactResponse {
	@JsonProperty("data")
	private ContactResponse.Result result;

	@Data
	public static class Result {
		private EntityContacts entityContacts;
	}
}