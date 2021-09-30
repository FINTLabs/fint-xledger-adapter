package no.fint.xledger.okonomi.fakturagrunnlag;

import no.fint.model.resource.Link;
import no.fint.model.resource.felles.PersonResource;
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
import no.fint.xledger.okonomi.vare.VareService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FakturagrunnlagMapper {

    private final FintRepository fintRepository;

    private final ConfigProperties configProperties;

    private final ProductCache productCache;

    private final VareService vareService;

    public FakturagrunnlagMapper(FintRepository fintRepository, ConfigProperties configProperties, ProductCache productCache, VareService vareService) {
        this.fintRepository = fintRepository;
        this.configProperties = configProperties;
        this.productCache = productCache;
        this.vareService = vareService;
    }

    public InvoiceBaseItemDTO toXledger(FakturalinjeResource linje, int subledgerDbId, FakturautstederResource fakturautsteder, FakturagrunnlagResource fakturagrunnlag, int lineNumber) throws Exception {
        InvoiceBaseItemDTO dto = new InvoiceBaseItemDTO();
        dto.setSubledgerDbId(String.valueOf(subledgerDbId));

        if (vareService.getVarer() == null) vareService.refreshIfNeeded();
        VareResource vare = getVare(linje.getVare());
        Node product = productCache.filterVarerByDbId(SellerUtil.extractProductDbId(vare.getSystemId().getIdentifikatorverdi()));

        dto.setProductDbId(String.valueOf(product.getDbId()));
        if (product.getGlObject1() != null) dto.setGlObject1DbId(String.valueOf(product.getGlObject1().getDbId()));
        if (product.getGlObject2() != null) dto.setGlObject2DbId(String.valueOf(product.getGlObject2().getDbId()));
        if (product.getGlObject3() != null) dto.setGlObject3DbId(String.valueOf(product.getGlObject3().getDbId()));
        if (product.getGlObject4() != null) dto.setGlObject4DbId(String.valueOf(product.getGlObject4().getDbId()));
        if (product.getGlObject5() != null) dto.setGlObject5DbId(String.valueOf(product.getGlObject5().getDbId()));

        PersonResource person = fintRepository.getPerson(configProperties.getOrganization(), fakturagrunnlag.getMottaker().getPerson());
        dto.setYourReference(CustomerMapper.personnavnToString(person.getNavn(), false));
        //dto.setOurRefDbId(0); // Not available through addInvoiceBaseItem

        dto.setHeaderInfo("Kontaktinfo: " + fakturautsteder.getNavn());
        dto.setExtOrder(fakturagrunnlag.getOrdrenummer().getIdentifikatorverdi());
        dto.setInvoiceLayoutDbId(configProperties.getInvoiceLayoutDbId());
        dto.setCurrencyDbId(configProperties.getCurrencyDbId());
        dto.setOwnerDbId(configProperties.getOwnerDbId());
        dto.setFieldGroupDbId(SellerUtil.extractSalgsordregruppeDbId(fakturautsteder.getSystemId().getIdentifikatorverdi()));

        dto.setUnitPrice((float) (linje.getPris() / 100.0));
        dto.setQuantity(linje.getAntall());
        dto.setLineNumber(lineNumber);

        return dto;
    }

    private VareResource getVare(List<Link> links) throws Exception {
        String href = links.get(0).getHref();
        String id = StringUtils.substringAfterLast(href, "/");
        VareResource vare = vareService
                .getVarer()
                .stream()
                .filter(f -> f.getSystemId().getIdentifikatorverdi().equals(id))
                .findFirst()
                .orElseThrow(Exception::new);
        // todo better exception type/message
        return vare;
    }
}
