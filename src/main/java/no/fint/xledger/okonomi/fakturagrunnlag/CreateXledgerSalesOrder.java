package no.fint.xledger.okonomi.fakturagrunnlag;

import lombok.extern.slf4j.Slf4j;
import no.fint.event.model.Event;
import no.fint.event.model.Operation;
import no.fint.event.model.ResponseStatus;
import no.fint.model.felles.kompleksedatatyper.Personnavn;
import no.fint.model.resource.Link;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.resource.okonomi.faktura.FakturagrunnlagResource;
import no.fint.model.resource.okonomi.faktura.FakturalinjeResource;
import no.fint.model.resource.okonomi.faktura.FakturautstederResource;
import no.fint.model.resource.okonomi.kodeverk.MerverdiavgiftResource;
import no.fint.model.resource.okonomi.kodeverk.VareResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
//@ConditionalOnProperty("fint.adapter.order.visma.enabled")
public class CreateXledgerSalesOrder implements Processor<FakturagrunnlagResource> {

    @Autowired
    private FakturagrunnlagService fakturagrunnlagService;

//    @Autowired
//    private ResourceResolverService resolverService;

    @Override
    public FakturagrunnlagResource apply(Event event, FakturagrunnlagResource resource) {
        if (empty(resource.getFakturalinjer())) addProblem(event, "fakturalinjer", "No fakturalinjer defined");
        if (empty(resource.getFakturautsteder())) addProblem(event, "fakturautsteder", "No fakturautsteder defined");

        if (resource.getFakturalinjer().stream().anyMatch(r -> empty(r.getVare())))
            addProblem(event, "varelinje", "References to Varelinje missing");

        if (!empty(event.getProblems())) {
            event.setResponseStatus(ResponseStatus.REJECTED);
            event.setMessage("Payload fails validation");
            return null;
        }

        try {
            fakturagrunnlagService.addFakturagrunnlag(resource);
            log.info("InvoiceBaseItem added");
            return resource;
        } catch (HttpStatusCodeException e) {
            setEventStateFromHttpException(event, e);
        } catch (IllegalArgumentException e) {
            event.setResponseStatus(ResponseStatus.REJECTED);
            event.setMessage(e.getMessage());
        } catch (Exception e) {
            log.warn("Error:", e);
            event.setResponseStatus(ResponseStatus.ERROR);
            event.setStatusCode("XLEDGER");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            event.setMessage(sw.toString());
        }

        return null;
    }

    public static void setEventStateFromHttpException(Event event, HttpStatusCodeException e) {
        log.warn("HTTP error: {}", e.getStatusCode());
        if (e.getStatusCode().is4xxClientError())
            event.setResponseStatus(ResponseStatus.REJECTED);
        else if (e.getStatusCode().is5xxServerError())
            event.setResponseStatus(ResponseStatus.ERROR);
        event.setStatusCode(e.getStatusCode().name());
        event.setMessage(e.getResponseBodyAsString());
    }

    @Override
    public Operation operation() {
        return Operation.CREATE;
    }
}