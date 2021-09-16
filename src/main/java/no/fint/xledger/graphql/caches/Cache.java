package no.fint.xledger.graphql.caches;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class Cache<T> {
    private final Duration maxDuration;
    private Optional<LocalDateTime> dataCollectedAt;
    private List<T> list;

    public Cache(Duration maxDuration) {
        this.maxDuration = maxDuration;
        list = new ArrayList<>(0);
    }

    public List<T> get() {
        if (cacheExpired()) {
            list = getData();
            dataCollectedAt = Optional.of(LocalDateTime.now());
        }

        return list;
    }

    protected abstract List<T> getData();

    private boolean cacheExpired() {
        if (dataCollectedAt.isEmpty()) return true;

        Duration actualDuration = Duration.between(dataCollectedAt.get(), LocalDateTime.now());
        return actualDuration.getSeconds() > maxDuration.getSeconds();
    }
}
