package no.fint.xledger.okonomi;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "fint.xledger")
public class ConfigProperties {
    @Getter
    @Setter
    private Map<String, Long> mva;

    @Getter
    @Setter
    @Value("${fint.client.details.assetId}")
    private String organization;

    @Getter
    @Setter
    private String ownerDbId;

    @Getter
    @Setter
    private int invoiceLayoutDbId;

    @Getter
    @Setter
    private String currencyDbId;

}
