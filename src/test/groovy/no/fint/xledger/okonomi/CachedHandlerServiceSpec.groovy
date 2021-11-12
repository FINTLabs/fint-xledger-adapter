package no.fint.xledger.okonomi

import spock.lang.Specification

import java.time.Clock
import java.time.Instant

class CachedHandlerServiceSpec extends Specification {

    def "Don't run before working hours"() {
        given:
        ConfigFintCache config = new ConfigFintCache();
        CachedHandlerServiceMock cache = new CachedHandlerServiceMock(config)

        when:
        config.setDontRunBefore(8);
        cache.clock = getFixedClock("07:52")
        cache.refreshIfNeeded()

        then:
        assert (cache.hasNotRefreshed())
    }


    def "Don't run after working hours"() {
        given:
        ConfigFintCache config = new ConfigFintCache();
        CachedHandlerServiceMock cache = new CachedHandlerServiceMock(config)

        when:
        config.setDontRunAfter(14);
        cache.clock = getFixedClock("16:45")
        cache.refreshIfNeeded()

        then:
        assert (cache.hasNotRefreshed())
    }


    private Clock getFixedClock(String time) {
        //return Clock.fixed(Instant.parse("2021-10-29T" + time + ":00.00Z"), Clock.systemDefaultZone().getZone())
        return Clock.fixed(Instant.parse("2021-10-29T" + time + ":00.00Z"))
    }
}
