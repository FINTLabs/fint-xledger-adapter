package no.fint.xledger.okonomi.mva;

import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.okonomi.kodeverk.MerverdiavgiftResource;
import no.fint.xledger.model.objectValues.Node;
import no.fint.xledger.okonomi.ConfigProperties;
import org.springframework.stereotype.Service;

@Service
public class MerverdiavgiftMapper {
    private final ConfigProperties configProperties;

    public MerverdiavgiftMapper(ConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    public MerverdiavgiftResource toFint(Node xledgerMva) {
        MerverdiavgiftResource mva = new MerverdiavgiftResource();

        mva.setKode(xledgerMva.getCode());
        mva.setNavn(xledgerMva.getDescription());

        Identifikator systemId = new Identifikator();
        systemId.setIdentifikatorverdi(Integer.toString(xledgerMva.getDbId()));
        mva.setSystemId(systemId);

        if (xledgerMva.getCode2() != null) {
            // By sept 2021 there is no way to get mva-rates from Xledger, we are getting a code instead.
            // Codes and corresponding rates will therefor be set by configuration.
            // Eksempel:
            //  HIGH_LEVEL = 25%
            //  MID_LEVEL = 15%
            //  LOW_LEVEL = 12%
            //  SPECIAL_RATE 11,11%
            //  ZERO_LEVEL= 0%

            String mvaCode = xledgerMva.getCode2().getName();
            if (configProperties.getMva().containsKey(mvaCode)) {
                mva.setSats(configProperties.getMva().get(mvaCode));
            } else {
                throw new UnsupportedOperationException("Configuration is missing for mvaCode: " + mvaCode);
            }
        }

        return mva;
    }
}
