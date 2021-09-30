package no.fint.xledger.okonomi.mva;

import no.fint.event.model.Event;
import no.fint.event.model.ResponseStatus;
import no.fint.model.okonomi.kodeverk.KodeverkActions;
import no.fint.model.resource.FintLinks;
import no.fint.xledger.handler.Handler;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
public class GetAllMerverdiavgiftHandler implements Handler {

    private final MerverdiavgiftService merverdiavgiftService;

    public GetAllMerverdiavgiftHandler(MerverdiavgiftService merverdiavgiftService) {
        this.merverdiavgiftService = merverdiavgiftService;
    }

    @Override
    public void accept(Event<FintLinks> response) {
        if (merverdiavgiftService.getMva() == null) merverdiavgiftService.refreshIfNeeded();

        merverdiavgiftService.getMva().forEach(response::addData);
        response.setResponseStatus(ResponseStatus.ACCEPTED);
    }

    @Override
    public Set<String> actions() {
        return Collections.singleton(KodeverkActions.GET_ALL_MERVERDIAVGIFT.name());
    }
}
