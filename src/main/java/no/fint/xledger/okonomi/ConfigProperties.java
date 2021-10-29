package no.fint.xledger.okonomi;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.Map;

@Validated
@Component
@ConfigurationProperties(prefix = "fint.xledger")
public class ConfigProperties {
    @NotEmpty
    @Getter
    @Setter
    private Map<String, Long> mva;

    @NotEmpty
    @Getter
    @Setter
    @Value("${fint.client.details.assetId}")
    private String organization;

    @NotEmpty
    @Getter
    @Setter
    private String ownerDbId;

    @Min(value = 1)
    @Getter
    @Setter
    private int invoiceLayoutDbId;

    @NotEmpty
    @Getter
    @Setter
    private String currencyDbId;

    @Getter
    @Setter
    private int digistToCompareSalgsordregruppeAndProduct;

    @Getter
    @Setter
    private Boolean enableInvoiceStatus = true;

    @Getter
    @Setter
    private String enviroment;

    @Getter
    @Setter
    private String baseUrl = "https://%s.felleskomponent.no";

    @Getter
    @Setter
    private String skoleUrl = "/utdanning/utdanningsprogram/skole/";

    @Getter
    @Setter
    private int hoursToCacheInvoice = 48;

    @Getter
    @Setter
    private int hoursToCacheSaleOrders = 3;

    @Getter
    @Setter
    private ConfigFintCache fintCache;
}
