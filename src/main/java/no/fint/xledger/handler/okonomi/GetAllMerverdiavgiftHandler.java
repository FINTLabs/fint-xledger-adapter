package no.fint.xledger.handler.okonomi;

import no.fint.xledger.handler.Handler;
import no.fint.event.model.Event;
import no.fint.model.okonomi.kodeverk.KodeverkActions;
import no.fint.model.resource.FintLinks;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
public class GetAllMerverdiavgiftHandler implements Handler {
    @Override
    public void accept(Event<FintLinks> fintLinksEvent) {

    }

    @Override
    public Set<String> actions() {
        return Collections.singleton(KodeverkActions.GET_ALL_MERVERDIAVGIFT.name());
    }
}
