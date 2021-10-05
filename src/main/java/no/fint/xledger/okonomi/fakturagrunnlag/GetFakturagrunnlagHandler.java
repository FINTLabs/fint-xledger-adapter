package no.fint.xledger.okonomi.fakturagrunnlag;

import lombok.extern.slf4j.Slf4j;
import no.fint.event.model.Event;
import no.fint.event.model.ResponseStatus;
import no.fint.model.okonomi.faktura.FakturaActions;
import no.fint.model.resource.FintLinks;
import no.fint.model.resource.okonomi.faktura.FakturagrunnlagResource;
import no.fint.xledger.handler.Handler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Slf4j
@Service
public class GetFakturagrunnlagHandler implements Handler {

    private final FakturagrunnlagService fakturagrunnlagService;

    public GetFakturagrunnlagHandler(FakturagrunnlagService fakturagrunnlagService) {
        this.fakturagrunnlagService = fakturagrunnlagService;
    }

    @Override
    public void accept(Event<FintLinks> response) {
        log.debug("Handling {} ...", response);
        log.trace("Event data: {}", response.getData());
        if (!StringUtils.startsWith(response.getQuery(), "ordrenummer/")) {
            response.setResponseStatus(ResponseStatus.REJECTED);
            response.setMessage("Invalid query " + response.getQuery());
            return;
        }
        String ordrenummer = StringUtils.removeStart(response.getQuery(), "ordrenummer/");
        FakturagrunnlagResource fakturagrunnlag = fakturagrunnlagService.getFakturagrunnlag(ordrenummer);

        if (fakturagrunnlag == null) {
            response.setResponseStatus(ResponseStatus.ERROR);
            response.setMessage("Fakturagrunnlag/salgsordre not found in Xledger for ordrenummer: " + ordrenummer);
        } else {
            response.addData(fakturagrunnlag);
            response.setResponseStatus(ResponseStatus.ACCEPTED);
        }
    }

    @Override
    public Set<String> actions() {
        return Collections.singleton(FakturaActions.GET_FAKTURAGRUNNLAG.name());
    }
}

