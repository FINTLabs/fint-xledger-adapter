package no.fint.xledger.okonomi;

import no.fint.xledger.handler.Handler;
import no.fint.event.model.Event;
import no.fint.model.okonomi.faktura.FakturaActions;
import no.fint.model.resource.FintLinks;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
public class GetAllFakturautstederHandler implements Handler {
    @Override
    public void accept(Event<FintLinks> fintLinksEvent) {

    }

    @Override
    public Set<String> actions() {
        return Collections.singleton(FakturaActions.GET_ALL_FAKTURAUTSTEDER.name());
    }
}
