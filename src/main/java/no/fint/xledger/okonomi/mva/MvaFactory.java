package no.fint.xledger.okonomi.mva;

import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.okonomi.kodeverk.MerverdiavgiftResource;
import no.fint.xledger.model.objectValues.Node;
import org.springframework.stereotype.Service;

@Service
public class MvaFactory {
    public MerverdiavgiftResource toFint(Node xledgerMva) {
        MerverdiavgiftResource mva = new MerverdiavgiftResource();

        mva.setKode(xledgerMva.getCode());
        mva.setNavn(xledgerMva.getDescription());

        Identifikator systemId = new Identifikator();
        systemId.setIdentifikatorverdi(Integer.toString(xledgerMva.getDbId()));
        mva.setSystemId(systemId);

        // TODO Trond: finn en måte å hente mva-prosentverdier via GraphQL. This is not the rigth way to do it:
        switch(xledgerMva.getCode2().getName())
        {
            case "ZERO_LEVEL":
                mva.setSats(0L);
                break;
            case "LOW_LEVEL":
                mva.setSats(120L);
                break;
            case "MID_LEVEL":
                mva.setSats(150L);
                break;
            case "HIGH_LEVEL":
                mva.setSats((250L));
                break;
            default:
                throw new UnsupportedOperationException("Ingen støtte for mva-typen:" + xledgerMva.getCode2().getName());
        }

        return mva;
    }
}
