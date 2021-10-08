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
        // If it has never run
        if (lastRunStarted == null) {
            lastRunStarted = LocalDateTime.now();
            refreshData();
            return;
        }

        if (isRunning()) return;

        // If the clock is not between 07:00 and 15:59
        if (LocalDateTime.now().getHour() >= dontRunAfter) return;
        if (LocalDateTime.now().getHour() < dontRunBefore) return;

        // If it has been updated last 3 hours
        if (hasBeenUpdatedLatly()) return;

        // If not, refresh
        lastRunStarted = LocalDateTime.now();
        refreshData();
        lastRun = LocalDateTime.now();
    }

    protected abstract void refreshData();

    private boolean isRunning() {
        if (lastRunStarted == null) return false;

        Duration duration = Duration.between(lastRunStarted, LocalDateTime.now());
        return duration.getSeconds() < Duration.ofHours(maxRunningHours).getSeconds();
    }

    private boolean hasBeenUpdatedLatly(){
        if (lastRun == null) return false;

        Duration duration = Duration.between(lastRun, LocalDateTime.now());
        return duration.getSeconds() < Duration.ofHours(hoursBetweenUpdate).getSeconds();
    }
}
