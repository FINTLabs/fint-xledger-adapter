package no.fint.xledger.okonomi.fakturagrunnlag;

import no.fint.model.felles.kompleksedatatyper.Identifikator;
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

        //String ourRefDbId = SellerUtil.extractContactDbId(fakturautsteder.getSystemId().getIdentifikatorverdi());
        //dto.setOurRefDbIdFromString(ourRefDbId);
        dto.setOurRefDbId(0);
        // Todo: gives error
        //"message": "Argument: \"ourRefDbId\" - the value 440753 is not valid or allowed.",

        dto.setHeaderInfo("Kontaktinfo: " + fakturautsteder.getNavn());
        dto.setExtOrder(fakturagrunnlag.getOrdrenummer().getIdentifikatorverdi());
        dto.setInvoiceLayoutDbId(configProperties.getInvoiceLayoutDbId());
        dto.setCurrencyDbId(configProperties.getCurrencyDbId());
        dto.setOwnerDbId(configProperties.getOwnerDbId());
        dto.setFieldGroupDbId(SellerUtil.extractSalgsordregruppeDbId(fakturautsteder.getSystemId().getIdentifikatorverdi()));
        dto.setText(fritekstToString(linje.getFritekst()));

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
                .orElseThrow(() -> new IllegalArgumentException("Didn't find vare with id " + id));
                           
        return vare;
    }

    private String fritekstToString(List<String> list) {
        if (list == null || list.size() == 0) return "";
        String output = "";

        for (String line : list) {
            if (!output.equals(""))
                output += "\n";
            output += line;
        }

        return output.trim();
    }

    public FakturagrunnlagResource toFint(no.fint.xledger.model.salesOrders.Node salesOrder) {
        FakturagrunnlagResource fakturagrunnlag = new FakturagrunnlagResource();

        Identifikator identifikator = new Identifikator();
        identifikator.setIdentifikatorverdi(salesOrder.getXorder());
        fakturagrunnlag.setOrdrenummer(identifikator);

        return fakturagrunnlag;
    }
}
