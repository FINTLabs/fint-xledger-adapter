package no.fint.xledger.model.invoiceBaseItem.invoiceBaseItems;

import lombok.Data;

public @Data class Node{
	private String createdAt;
	private String amount;
	private String dbId;
	private Subledger subledger;
	private String taxAmount;
	private String extOrder;
}