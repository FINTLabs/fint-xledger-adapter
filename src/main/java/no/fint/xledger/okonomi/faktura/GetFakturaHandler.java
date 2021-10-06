package no.fint.xledger.okonomi.faktura;

import lombok.extern.slf4j.Slf4j;
import no.fint.xledger.handler.Handler;
import no.fint.event.model.Event;
import no.fint.event.model.ResponseStatus;
import no.fint.model.okonomi.faktura.FakturaActions;
import no.fint.model.resource.FintLinks;
import no.fint.model.resource.okonomi.faktura.FakturaResource;
import no.fint.xledger.okonomi.ConfigProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class GetFakturaHandler implements Handler {

    private final FakturaService fakturaService;

    private final ConfigProperties configProperties;

    public GetFakturaHandler(FakturaService fakturaService, ConfigProperties configProperties) {
        this.fakturaService = fakturaService;
        this.configProperties = configProperties;
    }

    @Override
    public void accept(Event<FintLinks> response) {
        if (!configProperties.getEnableInvoiceStatus()){
            log.warn("Invoice status not enabled");
            response.setResponseStatus(ResponseStatus.REJECTED);
            response.setMessage("Invoice status not enabled");
            return;
        }

        String query = response.getQuery();
        if (!query.startsWith("fakturanummer/")) {
            throw new IllegalArgumentException("Invalid query: " + query);
        }

        String fakturanummer = StringUtils.removeStart(response.getQuery(), "fakturanummer/");
        FakturaResource faktura = fakturaService.getFaktura(fakturanummer);

        if (faktura == null) {
            response.setResponseStatus(ResponseStatus.REJECTED);
            response.setStatusCode("NOT_FOUND");
            response.setMessage("Faktura not found in Xledger for fakturanummer: " + fakturanummer);
        } else {
            response.addData(faktura);
            response.setResponseStatus(ResponseStatus.ACCEPTED);
        }
    }

    @Override
    public Set<String> actions() {
        if (configProperties.getEnableInvoiceStatus()) {
            return Collections.singleton(FakturaActions.GET_FAKTURA.name());
        } else {
            return new HashSet<>();
        }
    }
}
