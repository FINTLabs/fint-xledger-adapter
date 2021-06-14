package no.fint.xledger.handler.okonomi;

import no.fint.xledger.handler.Handler;
import no.fint.event.model.Event;
import no.fint.event.model.ResponseStatus;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.okonomi.faktura.FakturaActions;
import no.fint.model.resource.FintLinks;
import no.fint.model.resource.okonomi.faktura.FakturaResource;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
public class GetFakturaHandler implements Handler {
    @Override
    public void accept(Event<FintLinks> response) {

        String query = response.getQuery();


        if (!query.startsWith("fakturanummer/")) {
            throw new IllegalArgumentException("Invalid query: " + query);
        }

        FakturaResource fakturaResource = new FakturaResource();
        Identifikator fakturanummer = new Identifikator();
        fakturanummer.setIdentifikatorverdi("21-998877");
        fakturaResource.setFakturanummer(fakturanummer);

        response.setData(Collections.singletonList(fakturaResource));
        response.setResponseStatus(ResponseStatus.ACCEPTED);

    }

    @Override
    public Set<String> actions() {
        return Collections.singleton(FakturaActions.GET_FAKTURA.name());
    }
}
