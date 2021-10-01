package no.fint.xledger.model.salesOrders;

import lombok.Data;

public @Data class DetailsItem{
	private String unitPrice;
	private String amount;
	private Product product;
	private double quantity;
	private String text;
}