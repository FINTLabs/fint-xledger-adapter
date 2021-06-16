package no.fint.model;

import lombok.Data;

public @Data class Node{
	private String createdAt;
	private String unit;
	private String code;
	private int dbId;
	private String salesPrice;
	private String description;
	private TaxRule taxRule;
}