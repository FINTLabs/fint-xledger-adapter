package no.fint.xledger.okonomi.fakturagrunnlag;

import no.fint.event.model.Event;
import no.fint.event.model.Problem;
import no.fint.event.model.ResponseStatus;
import no.fint.model.resource.FintLinks;
import no.fint.model.resource.okonomi.faktura.FakturagrunnlagResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javax.validation.Validation.buildDefaultValidatorFactory;

@Service
public class FakturagrunnlagValidator {

    private ValidatorFactory validatorFactory;

    @PostConstruct
    public void init() {
        validatorFactory = buildDefaultValidatorFactory();
    }

    public void validate(Event<FintLinks> event, FakturagrunnlagResource resource) {
        validatorFactory
                .getValidator()
                .validate(resource)
                .forEach(violation -> addProblem(event, violation.getPropertyPath().toString(), violation.getMessage()));
        if (empty(resource.getFakturalinjer())) addProblem(event, "fakturalinjer", "No fakturalinjer defined");
        if (empty(resource.getFakturautsteder())) addProblem(event, "oppdragsgiver", "No oppdragsgiver defined");

        if (!empty(resource.getFakturalinjer()) && resource.getFakturalinjer().stream().anyMatch(r -> empty(r.getVare())))
            addProblem(event, "varelinje", "References to Varelinje missing");

        if (!empty(event.getProblems())) {
            event.setResponseStatus(ResponseStatus.REJECTED);
            event.setStatusCode("XHFHGWE");
            event.setMessage("Payload fails validation");
        }
    }

    public void addProblem(Event<FintLinks> event, String field, String message) {
        if (Objects.isNull(event.getProblems())) {
            event.setProblems(new ArrayList<>());
        }
        Problem problem = new Problem();
        problem.setField(field);
        problem.setMessage(message);
        event.getProblems().add(problem);
    }

    public boolean empty(List<?> l) {
        return Objects.isNull(l) || l.isEmpty();
    }
}
