package no.fint.xledger.okonomi.vare;

import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.okonomi.kodeverk.VareResource;
import no.fint.xledger.model.Node;
import no.fint.xledger.utilities.ConvertUtilities;
import org.springframework.stereotype.Service;

@Service
public class VareFactory {

    public VareResource toFint(Node product) {
        VareResource vare = new VareResource();

        vare.setNavn(product.getDescription());
        vare.setEnhet(product.getUnit());

        Identifikator systemId = new Identifikator();
        systemId.setIdentifikatorverdi(Integer.toString(product.getDbId()));
        vare.setSystemId(systemId);

        vare.setKode(product.getCode());
        vare.setPris(ConvertUtilities.stringPriceToLongOre(product.getSalesPrice()));

        return vare;
    }
}
