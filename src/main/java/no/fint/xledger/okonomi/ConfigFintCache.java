package no.fint.xledger.okonomi;

import lombok.Getter;
import lombok.Setter;

public class ConfigFintCache {

    @Getter
    @Setter
    private int maxRunningHours = 2;

    @Getter
    @Setter
    private int dontRunAfter = 16;

    @Getter
    @Setter
    private int dontRunBefore = 7;

    @Getter
    @Setter
    private int hoursBetweenUpdate = 3;
}
