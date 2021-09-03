package no.fint.xledger.okonomi;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "fint.xledger")
public class ConfigProperties {
    @Getter
    @Setter
    private Map<String, Long> mva;
}
