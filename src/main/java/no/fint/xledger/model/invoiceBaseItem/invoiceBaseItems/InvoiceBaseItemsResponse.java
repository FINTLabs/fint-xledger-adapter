package no.fint.xledger.model.invoiceBaseItem.invoiceBaseItems;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import no.fint.xledger.model.product.ProductResponse;
import no.fint.xledger.model.product.Products;

public @Data class InvoiceBaseItemsResponse{
	@JsonProperty("data")
	private InvoiceBaseItemsResponse.Result result;

	@Data
	public static class Result {
		private InvoiceBaseItems invoiceBaseItems;
	}
}