package no.fint.xledger.okonomi.faktura;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.Link;
import no.fint.model.resource.okonomi.faktura.FakturaResource;
import no.fint.model.resource.okonomi.faktura.FakturagrunnlagResource;
import no.fint.xledger.model.salesOrders.Node;
import no.fint.xledger.okonomi.InvoiceUtil;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

@Slf4j
@Service
public class FakturaMapper {

    private final FastDateFormat xledgerDateFormat = FastDateFormat.getInstance("yyyy-MM-dd");

    public FakturaResource toFint(Node salesOrder) {
        FakturaResource faktura = new FakturaResource();
        faktura.setBelop(toOre(salesOrder.getAmount()));
        faktura.setDato(parseDate(salesOrder.getInvoiceDate()));

        Identifikator identifikator = new Identifikator();
        identifikator.setIdentifikatorverdi(salesOrder.getInvoiceNumber());
        faktura.setFakturanummer(identifikator);
        faktura.setFakturert(InvoiceUtil.hasInvoicedNumber(salesOrder));
        faktura.setForfallsdato(parseDate(salesOrder.getDueDate()));
        if (salesOrder.getCustomer() != null) faktura.setMottaker(salesOrder.getCustomer().getDescription());
        faktura.setRestbelop((long) salesOrder.getRemainingAmount() * 100L);

        faktura.addFakturagrunnlag(Link.with(FakturagrunnlagResource.class, "ordrenummer", salesOrder.getXorder()));

        faktura.setKreditert(false);
        faktura.setBetalt(salesOrder.getRemainingAmount() <= 0);

        return faktura;
    }

    private long toOre(String input) {
        try {
            if (input == null || input.length() == 0) return 0;
            return (Long.parseLong(input)) * 100L;
        } catch (Exception e) {
            log.error("Error converting input (" + input + ")in toOre: " + e.getMessage());
            return 0L;
        }
    }

    private Date parseDate(String date) {
        try {
            return xledgerDateFormat.parse(String.valueOf(date));
        } catch (ParseException e) {
            return null;
        }
    }
}
