package no.fint.xledger.model.contacts;

import java.util.List;
import lombok.Data;

public @Data class EntityContacts{
	private List<EdgesItem> edges;
	private PageInfo pageInfo;
}