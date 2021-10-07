package no.fint.xledger;

import no.fint.xledger.okonomi.ConfigProperties;
import org.springframework.stereotype.Service;

@Service
public class FintEndpointsUtil {
    private final ConfigProperties configProperties;

    public FintEndpointsUtil(ConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    public String getSkoleUrl() {
        return getBaseUrl() + configProperties.getSkoleUrl();
    }

    public String getSkoleByOrganisasjonsnummerUrl(String organisasjonsnummer) {
        return String.format(getSkoleUrl() + "organisasjonsnummer/%s", organisasjonsnummer);
    }

    private String getBaseUrl() {
        return String.format(configProperties.getBaseUrl(), configProperties.getEnviroment());
    }
}
