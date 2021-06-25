package no.fint.xledger.okonomi.vare;

import no.fint.event.model.Event;
import no.fint.event.model.ResponseStatus;
import no.fint.model.okonomi.kodeverk.KodeverkActions;
import no.fint.model.resource.FintLinks;
import no.fint.xledger.handler.Handler;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
public class GetAllVareHandler implements Handler {

    private final VareService vareService;

    public GetAllVareHandler(VareService vareService) {
        this.vareService = vareService;
    }

    @Override
    public void accept(Event<FintLinks> response) {
        vareService.getVarer().forEach(response::addData);
        response.setResponseStatus(ResponseStatus.ACCEPTED);
    }

    @Override
    public Set<String> actions() {
        return Collections.singleton(KodeverkActions.GET_ALL_VARE.name());
    }
}
