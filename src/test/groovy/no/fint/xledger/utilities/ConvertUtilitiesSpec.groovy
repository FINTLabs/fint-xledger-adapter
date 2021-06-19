package no.fint.xledger.utilities

import spock.lang.Specification

class ConvertUtilitiesSpec extends Specification {

    def "Price should be returned as ore"() {

        when:
        def toLong = ConvertUtilities.stringPriceToLongOre("99.99")
        then:
        toLong == 9999
    }
}
