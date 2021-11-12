package no.fint.xledger.okonomi;

import lombok.Data;
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
@Data
@ConfigurationProperties(prefix = "fint.xledger")
public class ConfigProperties {
    @NotEmpty
    private Map<String, Long> mva;

    @NotEmpty
    @Value("${fint.client.details.assetId}")
    private String organization;

    @NotEmpty
    private String ownerDbId;

    @Min(value = 1)
    private int invoiceLayoutDbId;

    @NotEmpty
    private String currencyDbId;

    private int digistToCompareSalgsordregruppeAndProduct;

    private Boolean enableInvoiceStatus = true;

    private String enviroment;

    private String baseUrl = "https://%s.felleskomponent.no";

    private String skoleUrl = "/utdanning/utdanningsprogram/skole/";

    private int hoursToCacheInvoice = 48;

    private int hoursToCacheSaleOrders = 3;

    private ConfigFintCache fintCache;
}
