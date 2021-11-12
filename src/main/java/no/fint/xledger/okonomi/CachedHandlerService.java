package no.fint.xledger.okonomi;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class CachedHandlerService {

    //private final int maxRunningHours;
    //private final int dontRunAfter;
    //private final int dontRunBefore;
    //private final int hoursBetweenUpdate;

    private ConfigFintCache configCache;
    private LocalDateTime lastRunStarted;
    private LocalDateTime lastRun;

    @Setter
    @Autowired
    private Clock clock;

    public CachedHandlerService(ConfigFintCache configCache) {
      this.configCache = configCache;
    }

    public void refreshIfNeeded() {

        if (isRefreshNeeded()) {
            refresh();
        }
    }

    private void refresh() {
        lastRunStarted = LocalDateTime.now(clock);
        refreshData();
        lastRun = LocalDateTime.now(clock);
    }

    private boolean isRefreshNeeded() {
        return hasNeverRan() && isWithinWorkingHours() && isNotRunning() && hasNotBeenUpdatedLately();
    }

    private boolean isWithinWorkingHours() {
        return LocalDateTime.now(clock).getHour() >= configCache.getDontRunBefore() && LocalDateTime.now(clock).getHour() < configCache.getDontRunAfter();
    }

    private boolean hasNeverRan() {
        return lastRunStarted == null;
    }

    protected abstract void refreshData();

    private boolean isNotRunning() {
        if (lastRunStarted == null) return true;

        Duration duration = Duration.between(lastRunStarted, LocalDateTime.now(clock));
        return duration.getSeconds() >= Duration.ofHours(configCache.getMaxRunningHours()).getSeconds();
    }

    private boolean hasNotBeenUpdatedLately() {
        if (lastRun == null) return true;

        Duration duration = Duration.between(lastRun, LocalDateTime.now(clock));
        return duration.getSeconds() >= Duration.ofHours(configCache.getHoursBetweenUpdate()).getSeconds();
    }
}
