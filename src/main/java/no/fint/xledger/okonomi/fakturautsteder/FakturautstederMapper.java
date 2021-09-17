package no.fint.xledger.okonomi.fakturautsteder;

import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.okonomi.kodeverk.MerverdiavgiftResource;
import no.fint.xledger.model.objectValues.Node;
import org.springframework.stereotype.Service;

@Service
public class FakturautstederMapper {

    public MerverdiavgiftResource toFint(Node xledgerMva) {
        MerverdiavgiftResource mva = new MerverdiavgiftResource();

        mva.setKode(xledgerMva.getCode());
        mva.setNavn(xledgerMva.getDescription());

        Identifikator systemId = new Identifikator();
        systemId.setIdentifikatorverdi(Integer.toString(xledgerMva.getDbId()));
        mva.setSystemId(systemId);

        return mva;
    }
}
