package no.fint.xledger.okonomi.fakturagrunnlag;

import no.fint.model.resource.okonomi.faktura.FakturalinjeResource;
import no.fint.model.resource.okonomi.kodeverk.VareResource;
import no.fint.xledger.fintclient.FintRepository;
import no.fint.xledger.graphql.caches.ProductCache;
import no.fint.xledger.model.Node;
import no.fint.xledger.model.invoiceBaseItem.InvoiceBaseItemDTO;
import no.fint.xledger.okonomi.ConfigProperties;
import no.fint.xledger.okonomi.SellerUtil;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FakturagrunnlagMapper {

    @Autowired
    private FintRepository fintRepository;

    @Autowired
    private ConfigProperties configProperties;

    @Autowired
    private ProductCache productCache;

    public InvoiceBaseItemDTO toXledger(FakturalinjeResource linje, int subledgerDbId) {
        InvoiceBaseItemDTO dto = new InvoiceBaseItemDTO();
        dto.setSubledgerDbId(String.valueOf(subledgerDbId));

        VareResource vare = fintRepository.getVare(configProperties.getOrganization(), linje.getVare());
        Node product = productCache.filterVarerByDbId(SellerUtil.extractProductDbId(vare.getSystemId().getIdentifikatorverdi()));

        dto.setProductDbId(String.valueOf(product.getDbId()));



//                nullOrQuote(invoiceItem.getGlObject1DbId()),
//                nullOrQuote(invoiceItem.getGlObject2DbId()),
//                nullOrQuote(invoiceItem.getGlObject3DbId()),
//                nullOrQuote(invoiceItem.getGlObject4DbId()),
//                nullOrQuote(invoiceItem.getGlObject5DbId()),

        //dto.setOurRefDbId(0); // Not available through addInvoiceBaseItem
        //dto.setHeaderInfo("Kontaktinfo: " + );
        //dto.setExtOrder();
//                invoiceItem.getExtOrder(),
//                invoiceItem.getInvoiceLayoutDbId(),
//                quote(invoiceItem.getCurrencyDbId()),
//                invoiceItem.getOwnerDbId(),
//                invoiceItem.getFieldGroupDbId(),
//                invoiceItem.getUnitPrice(),
//                invoiceItem.getQuantity(),
//                invoiceItem.getLineNumber()));

return dto;
    }
}
