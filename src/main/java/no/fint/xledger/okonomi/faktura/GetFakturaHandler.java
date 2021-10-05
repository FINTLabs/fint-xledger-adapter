package no.fint.xledger.okonomi.faktura;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.okonomi.faktura.FakturagrunnlagResource;
import no.fint.xledger.handler.Handler;
import no.fint.event.model.Event;
import no.fint.event.model.ResponseStatus;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.okonomi.faktura.FakturaActions;
import no.fint.model.resource.FintLinks;
import no.fint.model.resource.okonomi.faktura.FakturaResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Slf4j
@Service
public class GetFakturaHandler implements Handler {

    private final FakturaService fakturaService;

    public GetFakturaHandler(FakturaService fakturaService) {
        this.fakturaService = fakturaService;
    }

    @Override
    public void accept(Event<FintLinks> response) {
        String query = response.getQuery();
        if (!query.startsWith("fakturanummer/")) {
            throw new IllegalArgumentException("Invalid query: " + query);
        }

        String fakturanummer = StringUtils.removeStart(response.getQuery(), "fakturanummer/");
        FakturaResource faktura = fakturaService.getFaktura(fakturanummer);

        if (faktura == null) {
            response.setResponseStatus(ResponseStatus.ERROR);
            response.setMessage("Faktura not found in Xledger for fakturanummer: " + fakturanummer);
        } else {
            response.addData(faktura);
            response.setResponseStatus(ResponseStatus.ACCEPTED);
        }
    }

    @Override
    public Set<String> actions() {
        return Collections.singleton(FakturaActions.GET_FAKTURA.name());
    }
}
