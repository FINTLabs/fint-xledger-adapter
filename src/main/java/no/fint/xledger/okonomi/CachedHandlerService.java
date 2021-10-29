package no.fint.xledger.okonomi;

import java.time.Duration;
import java.time.LocalDateTime;

public abstract class CachedHandlerService {

    private final int maxRunningHours;
    private final int dontRunAfter;
    private final int dontRunBefore;
    private final int hoursBetweenUpdate;

    private LocalDateTime lastRunStarted;
    private LocalDateTime lastRun;

    public CachedHandlerService(ConfigProperties configProperties) {
        maxRunningHours = configProperties.getFintCache().getMaxRunningHours();
        dontRunAfter = configProperties.getFintCache().getDontRunAfter();
        dontRunBefore = configProperties.getFintCache().getDontRunBefore();
        hoursBetweenUpdate = configProperties.getFintCache().getHoursBetweenUpdate();
    }

    public void refreshIfNeeded() {

        if (isRefreshNeeded()) {
            refresh();
        }
    }

    private void refresh() {
        lastRunStarted = LocalDateTime.now();
        refreshData();
        lastRun = LocalDateTime.now();
    }

    private boolean isRefreshNeeded() {
        return hasNeverRan() && isWithinWorkingHours() && isNotRunning() && hasNotBeenUpdatedLately();
    }

    private boolean isWithinWorkingHours() {
        return LocalDateTime.now().getHour() >= dontRunBefore && LocalDateTime.now().getHour() < dontRunAfter;
    }

    private boolean hasNeverRan() {
        return lastRunStarted == null;
    }

    protected abstract void refreshData();

    private boolean isNotRunning() {
        if (lastRunStarted == null) return true;

        Duration duration = Duration.between(lastRunStarted, LocalDateTime.now());
        return duration.getSeconds() >= Duration.ofHours(maxRunningHours).getSeconds();
    }

    private boolean hasNotBeenUpdatedLately() {
        if (lastRun == null) return true;

        Duration duration = Duration.between(lastRun, LocalDateTime.now());
        return duration.getSeconds() >= Duration.ofHours(hoursBetweenUpdate).getSeconds();
    }
}
