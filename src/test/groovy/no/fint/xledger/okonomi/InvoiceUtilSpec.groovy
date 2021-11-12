package no.fint.xledger.okonomi

import no.fint.xledger.model.salesOrders.Node
import spock.lang.Specification

class InvoiceUtilSpec extends Specification {
    def "Invoicenumber is null"() {
        when:
        Node node = new Node();

        then:
        assert !InvoiceUtil.hasInvoicedNumber(node)
    }

    def "Invoicenumber is empty"() {
        when:
        Node node = new Node();
        node.setInvoiceNumber("")

        then:
        assert !InvoiceUtil.hasInvoicedNumber(node)
    }

    def "Invoicenumber is zero"() {
        when:
        Node node = new Node();
        node.setInvoiceNumber("0")

        then:
        assert !InvoiceUtil.hasInvoicedNumber(node)
    }

    def "Contains invoicenumber"() {
        when:
        Node node = new Node();
        node.setInvoiceNumber("1234")

        then:
        assert InvoiceUtil.hasInvoicedNumber(node)
    }


}
