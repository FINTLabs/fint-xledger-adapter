package no.fint.xledger.fintclient;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("fint.client")
@Data
public class FintClient {
    private ResourceOwnerPasswordResourceDetails details;
}