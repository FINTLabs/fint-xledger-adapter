package no.fint.xledger.okonomi.fakturagrunnlag;

import no.fint.model.resource.okonomi.faktura.FakturagrunnlagResource;
import no.fint.model.resource.okonomi.faktura.FakturalinjeResource;
import no.fint.model.resource.okonomi.faktura.FakturautstederResource;
import no.fint.model.resource.okonomi.kodeverk.MerverdiavgiftResource;
import no.fint.model.resource.okonomi.kodeverk.VareResource;
import no.fint.xledger.fintclient.FintRepository;
import no.fint.xledger.graphql.InvoiceBaseItemRepository;
import no.fint.xledger.okonomi.ConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class FakturagrunnlagService {

    @Autowired
    private InvoiceBaseItemRepository invoiceBaseItemRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private FakturagrunnlagMapper mapper;

    public FakturagrunnlagService(InvoiceBaseItemRepository invoiceBaseItemRepository) {
        this.invoiceBaseItemRepository = invoiceBaseItemRepository;
    }

    public void addFakturagrunnlag(FakturagrunnlagResource fakturagrunnlagResource) throws Exception {

        int subledgerDbId = customerService.createOrUpdate(fakturagrunnlagResource.getMottaker().getPerson());
        if (subledgerDbId == 0) throw new Exception("subledgerDbID = 0. Customer create/update has failed");

        for (FakturalinjeResource linje : fakturagrunnlagResource.getFakturalinjer()){
            invoiceBaseItemRepository.add(mapper.toXledger(linje, subledgerDbId));

        }

/*      Order order = createOrder(fakturagrunnlag);

        order.setOrderLines(
                fakturagrunnlag.getFakturalinjer().stream().map(fakturalinje -> {
                    VareResource varelinje = resolverService.resolve(orgid, resolveLink(fakturalinje.getVare()), VareResource.class);
                    MerverdiavgiftResource mvakode = resolverService.resolve(orgid,
                            resolveLink(varelinje.getMerverdiavgift()).replace("mvakode/mvakode/",
                                    String.format("mvakode/systemid/%TY--", System.currentTimeMillis())),
                            MerverdiavgiftResource.class);
                    FakturautstederResource oppdragsgiver = resolverService.resolve(orgid, resolveLink(varelinje.getFakturautsteder()), FakturautstederResource.class);
                    return createOrderLine(fakturalinje, varelinje, oppdragsgiver, mvakode);
                }).collect(Collectors.toList()));*/

    }

    /*public OrderLine createOrderLine(FakturalinjeResource fakturalinje, VareResource varelinje, FakturautstederResource oppdragsgiver, MerverdiavgiftResource mvakode) {
        OrderLine orderLine = new OrderLine();
        orderLine.setPrincipalNumber(Integer.valueOf(oppdragsgiver.getSystemId().getIdentifikatorverdi()));
        orderLine.setCommodityNumber(Integer.valueOf(varelinje.getKode()));
        orderLine.setSerialNumber(1); //FIXME Must be incremented per customer for pending orders!!
        orderLine.setQuantity(fakturalinje.getAntall().doubleValue());
        orderLine.setPrice(fakturalinje.getPris() / 100.0);
        orderLine.setAmount(fakturalinje.getAntall() * fakturalinje.getPris() / 100.0);
        orderLine.setVatCode(Integer.valueOf(mvakode.getKode()));
        orderLine.setLineText(fakturalinje.getFritekst());

        return orderLine;
    }*/
}
