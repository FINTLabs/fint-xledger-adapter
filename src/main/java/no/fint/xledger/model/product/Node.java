package no.fint.xledger.model.product;

import lombok.Data;

public @Data class Node{
	private String createdAt;
	private String unit;
	private GlObject5 glObject5;
	private String code;
	private Object glObject4;
	private int dbId;
	private String salesPrice;
	private String description;
	private GlObject3 glObject3;
	private TaxRule taxRule;
	private Object glObject2;
	private GlObject1 glObject1;
}