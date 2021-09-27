package no.fint.xledger.model.contacts;

import lombok.Data;

public @Data class Contact{
	private String code;
	private Object phone;
	private String dbId;
	private String name;
	private String email;
}