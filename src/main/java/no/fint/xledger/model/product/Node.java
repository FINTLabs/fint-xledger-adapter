package no.fint.xledger.model.product;

import lombok.Data;

public @Data class Node{
	private String createdAt;
	private String unit;
	private GlObject5 glObject5;
	private String code;
	private GlObject4 glObject4;
	private int dbId;
	private String salesPrice;
	private String description;
	private GlObject3 glObject3;
	private TaxRule taxRule;
	private GlObject2 glObject2;
	private GlObject1 glObject1;
}