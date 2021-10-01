package no.fint.xledger.model.salesOrders;

import lombok.Data;

public @Data class Product{
	private String code;
	private int dbId;
	private String description;
}