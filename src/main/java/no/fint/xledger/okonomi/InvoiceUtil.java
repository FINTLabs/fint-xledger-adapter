package no.fint.xledger.okonomi;

import no.fint.xledger.model.salesOrders.Node;
import org.apache.commons.lang3.StringUtils;

public class InvoiceUtil {
    public static boolean hasInvoicedNumber(Node salesOrder){
        return StringUtils.isNoneEmpty(salesOrder.getInvoiceNumber()) && !salesOrder.getInvoiceNumber().equals("0");
    }
}
