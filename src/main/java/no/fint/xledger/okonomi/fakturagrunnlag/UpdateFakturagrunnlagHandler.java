package no.fint.xledger.okonomi.fakturagrunnlag;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import no.fint.event.model.Operation;
import no.fint.event.model.ResponseStatus;
import no.fint.model.resource.okonomi.faktura.FakturagrunnlagResource;
import no.fint.xledger.handler.Handler;
import no.fint.event.model.Event;
import no.fint.model.okonomi.faktura.FakturaActions;
import no.fint.model.resource.FintLinks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UpdateFakturagrunnlagHandler implements Handler {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    FakturagrunnlagValidator validator;

    @Autowired
    List<Processor<FakturagrunnlagResource>> processors;

    EnumMap<Operation, Processor<FakturagrunnlagResource>> processorMap;

    @PostConstruct
    public void init() {
        processorMap = new EnumMap<>(Operation.class);
        processors.forEach(p -> processorMap.put(p.operation(), p));
    }

    @Override
    public void accept(Event<FintLinks> response) {
        List<FakturagrunnlagResource> data = objectMapper.convertValue(response.getData(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, FakturagrunnlagResource.class));
        log.trace("Converted data: {}", data);

        response.setResponseStatus(ResponseStatus.ACCEPTED);
        response.setData(null);

        data.forEach(it -> validator.validate(response, it));

        if (!validator.empty(response.getProblems())) {
            return;
        }

        Processor<FakturagrunnlagResource> processor = processorMap.get(response.getOperation());
        if (processor != null) {
            log.debug("Processing update ...");
            response.setData(data.stream().map(processor.applyPartially(response)).filter(Objects::nonNull).collect(Collectors.toList()));
            log.debug("Processing completed.");
        }
    }

    @Override
    public Set<String> actions() {
        return Collections.singleton(FakturaActions.UPDATE_FAKTURAGRUNNLAG.name());
    }
}
