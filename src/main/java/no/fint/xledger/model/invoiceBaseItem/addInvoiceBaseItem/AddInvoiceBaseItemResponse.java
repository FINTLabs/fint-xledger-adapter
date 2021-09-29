package no.fint.xledger.model.invoiceBaseItem.addInvoiceBaseItem;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

public @Data class AddInvoiceBaseItemResponse{

	@JsonProperty("data")
	private AddInvoiceBaseItemResponse.Result result;

	@Data
	public static class Result {
		private AddInvoiceBaseItem addInvoiceBaseItem;
	}
}