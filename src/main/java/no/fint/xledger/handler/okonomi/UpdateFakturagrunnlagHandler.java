package no.fint.xledger.handler.okonomi;

import no.fint.xledger.handler.Handler;
import no.fint.event.model.Event;
import no.fint.model.okonomi.faktura.FakturaActions;
import no.fint.model.resource.FintLinks;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
public class UpdateFakturagrunnlagHandler implements Handler {
    @Override
    public void accept(Event<FintLinks> fintLinksEvent) {

    }

    @Override
    public Set<String> actions() {
        return Collections.singleton(FakturaActions.UPDATE_FAKTURAGRUNNLAG.name());
    }
}
