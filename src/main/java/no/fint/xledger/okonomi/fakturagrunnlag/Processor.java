package no.fint.xledger.okonomi.fakturagrunnlag;

import no.fint.event.model.Event;
import no.fint.event.model.Operation;
import no.fint.event.model.Problem;
import org.jooq.lambda.function.Function2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public interface Processor<T> extends Function2<Event,T, T> {
    default void addProblem(Event event, String field, String message) {
        if (Objects.isNull(event.getProblems())) {
            event.setProblems(new ArrayList<>());
        }
        Problem problem = new Problem();
        problem.setField(field);
        problem.setMessage(message);
        event.getProblems().add(problem);
    }

    default boolean empty(List<?> l) {
        return Objects.isNull(l) || l.isEmpty();
    }

    Operation operation();
}
