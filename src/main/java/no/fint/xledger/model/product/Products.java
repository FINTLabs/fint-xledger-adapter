package no.fint.xledger.model.product;

import java.util.List;
import lombok.Data;

public @Data class Products{
	private List<EdgesItem> edges;
	private PageInfo pageInfo;
}