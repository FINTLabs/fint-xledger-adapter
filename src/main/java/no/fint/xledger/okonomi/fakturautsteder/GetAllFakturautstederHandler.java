package no.fint.xledger.okonomi.fakturautsteder;

import no.fint.event.model.Event;
import no.fint.event.model.ResponseStatus;
import no.fint.model.okonomi.faktura.FakturaActions;
import no.fint.model.resource.FintLinks;
import no.fint.xledger.handler.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
public class GetAllFakturautstederHandler implements Handler {
    @Autowired
    private FakturautstederService fakturautstederService;

    public GetAllFakturautstederHandler(FakturautstederService fakturautstederService) {
        this.fakturautstederService = fakturautstederService;
    }

    @Override
    public void accept(Event<FintLinks> response) {
        fakturautstederService.getFakturautstedere().forEach(response::addData);
        response.setResponseStatus(ResponseStatus.ACCEPTED);
    }

    @Override
    public Set<String> actions() {
        return Collections.singleton(FakturaActions.GET_ALL_FAKTURAUTSTEDER.name());
    }
}
