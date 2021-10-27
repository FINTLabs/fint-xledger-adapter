package no.fint.xledger.okonomi;

import java.time.Duration;
import java.time.LocalDateTime;

public abstract class CachedHandlerService {

    private final int maxRunningHours = 2;
    private final int dontRunAfter = 16;
    private final int dontRunBefore = 7;
    private final int hoursBetweenUpdate = 3;

    private LocalDateTime lastRunStarted;
    private LocalDateTime lastRun;

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
