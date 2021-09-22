package no.fint.xledger.okonomi.vare;

import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.Link;
import no.fint.model.resource.okonomi.faktura.FakturautstederResource;
import no.fint.model.resource.okonomi.kodeverk.MerverdiavgiftResource;
import no.fint.model.resource.okonomi.kodeverk.VareResource;
import no.fint.xledger.graphql.caches.MerverdiavgiftCache;
import no.fint.xledger.model.Node;
import no.fint.xledger.okonomi.SellerUtil;
import no.fint.xledger.utilities.ConvertUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VareMapper {

    @Autowired
    private MerverdiavgiftCache merverdiavgiftCache;

    public VareMapper(MerverdiavgiftCache merverdiavgiftCache) {
        this.merverdiavgiftCache = merverdiavgiftCache;
    }

    public VareResource toFint(Node product, FakturautstederResource fakturautsteder) {
        VareResource vare = new VareResource();

        vare.setNavn(product.getDescription());
        vare.setEnhet(product.getUnit());

        Identifikator systemId = new Identifikator();
        systemId.setIdentifikatorverdi(SellerUtil.createVareId(fakturautsteder, product));
        vare.setSystemId(systemId);
        // id=17788784-698445_20758347 <fakturautsteder>_<product>

        vare.setKode(product.getCode());
        vare.setPris(ConvertUtilities.stringPriceToLongOre(product.getSalesPrice()));

        vare.addFakturautsteder(Link.with(FakturautstederResource.class,
                "systemid",
                String.valueOf(fakturautsteder.getSystemId().getIdentifikatorverdi())));

        String merverdiavgiftCode = product.getTaxRule().getCode();
        if (merverdiavgiftCode != null && merverdiavgiftCode.length() > 0) {
            vare.addMerverdiavgift(Link.with(MerverdiavgiftResource.class,
                    "systemid",
                    String.valueOf(merverdiavgiftCache.getByCode(merverdiavgiftCode).getDbId())));
        }

        return vare;
    }
}