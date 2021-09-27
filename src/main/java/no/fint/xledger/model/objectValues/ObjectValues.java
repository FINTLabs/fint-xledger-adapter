package no.fint.xledger.model.objectValues;

import java.util.List;
import lombok.Data;

public @Data class ObjectValues{
	private List<EdgesItem> edges;
	private PageInfo pageInfo;
}