package no.fint.xledger.model.salesOrders;

import java.util.List;
import lombok.Data;

public @Data class Node{
	private String xorder;
	private String amount;
	private SoOrderType soOrderType;
	private String yourReference;
	private String dueDate;
	private String invoiceAmount;
	private int subledgerDbId;
	private double remainingAmount;
	private String dbId;
	private String invoiceNumber;
	private List<DetailsItem> details;
	private String text;
	private String deliveryDate;
	private String taxAmount;
	private double remainingInvoice;
	private String invoiceDate;
	private Customer customer;
}