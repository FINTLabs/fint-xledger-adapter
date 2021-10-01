package no.fint.xledger.model.salesOrders;

import java.util.List;
import lombok.Data;

public @Data class Node{
	private String xorder;
	private String amount;
	private SoOrderType soOrderType;
	private String yourReference;
	private Object dueDate;
	private String invoiceAmount;
	private int subledgerDbId;
	private double remainingAmount;
	private String dbId;
	private String invoiceNumber;
	private List<DetailsItem> details;
	private Object text;
	private Object deliveryDate;
	private String taxAmount;
	private double remainingInvoice;
}