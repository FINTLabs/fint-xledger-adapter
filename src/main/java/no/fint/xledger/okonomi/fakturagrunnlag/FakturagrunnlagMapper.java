package no.fint.xledger.okonomi.fakturagrunnlag;

import no.fint.model.resource.okonomi.faktura.FakturagrunnlagResource;
import no.fint.model.resource.okonomi.faktura.FakturalinjeResource;
import no.fint.model.resource.okonomi.faktura.FakturautstederResource;
import no.fint.model.resource.okonomi.kodeverk.VareResource;
import no.fint.xledger.fintclient.FintRepository;
import no.fint.xledger.graphql.caches.ProductCache;
import no.fint.xledger.model.invoiceBaseItem.InvoiceBaseItemDTO;
import no.fint.xledger.model.product.Node;
import no.fint.xledger.okonomi.ConfigProperties;
import no.fint.xledger.okonomi.SellerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FakturagrunnlagMapper {

    @Autowired
    private FintRepository fintRepository;

    @Autowired
    private ConfigProperties configProperties;

    @Autowired
    private ProductCache productCache;

    public InvoiceBaseItemDTO toXledger(FakturalinjeResource linje, int subledgerDbId, FakturautstederResource fakturautsteder, FakturagrunnlagResource fakturagrunnlag, int lineNumber) {
        InvoiceBaseItemDTO dto = new InvoiceBaseItemDTO();
        dto.setSubledgerDbId(String.valueOf(subledgerDbId));

        VareResource vare = fintRepository.getVare(configProperties.getOrganization(), linje.getVare());
        Node product = productCache.filterVarerByDbId(SellerUtil.extractProductDbId(vare.getSystemId().getIdentifikatorverdi()));

        dto.setProductDbId(String.valueOf(product.getDbId()));
        if (product.getGlObject1() != null) dto.setGlObject1DbId(String.valueOf(product.getGlObject1().getDbId()));
        if (product.getGlObject2() != null) dto.setGlObject2DbId(String.valueOf(product.getGlObject2().getDbId()));
        if (product.getGlObject3() != null) dto.setGlObject3DbId(String.valueOf(product.getGlObject3().getDbId()));
        if (product.getGlObject4() != null) dto.setGlObject4DbId(String.valueOf(product.getGlObject4().getDbId()));
        if (product.getGlObject5() != null) dto.setGlObject5DbId(String.valueOf(product.getGlObject5().getDbId()));

        //dto.setOurRefDbId(0); // Not available through addInvoiceBaseItem

        dto.setHeaderInfo("Kontaktinfo: " + fakturautsteder.getNavn());
        dto.setExtOrder(fakturagrunnlag.getOrdrenummer().getIdentifikatorverdi());
        dto.setInvoiceLayoutDbId(configProperties.getInvoiceLayoutDbId());
        dto.setCurrencyDbId(configProperties.getCurrencyDbId());
        dto.setOwnerDbId(configProperties.getOwnerDbId());
        dto.setFieldGroupDbId(SellerUtil.extractSalgsordregruppeDbId(fakturautsteder.getSystemId().getIdentifikatorverdi()));

        // TODO is this safe?
        dto.setUnitPrice((float) (linje.getPris() / 100.0));
        dto.setQuantity(linje.getAntall());
        dto.setLineNumber(lineNumber);

        return dto;
    }
}
