package no.fint.xledger.fintclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

@Service
@Slf4j
public class ResourceResolverService {

    private final OAuth2RestTemplate oAuth2RestTemplate;

    public ResourceResolverService(FintClient fintClient) {
        oAuth2RestTemplate = new OAuth2RestTemplate(fintClient.getDetails());
    }

    public <T> T resolve(String orgid, String uri, Class<T> clazz) {
        try {
            HttpHeaders h = new HttpHeaders();
            h.add("x-org-id", orgid);
            h.add("x-client", "fint-xledger-betaling-adapter");
            h.add("Accept", "application/json");
            log.debug("Resolve {}", uri);
            return oAuth2RestTemplate.exchange(uri,
                    HttpMethod.GET,
                    new HttpEntity<>(h),
                    clazz).getBody();
        } catch (HttpStatusCodeException e) {
            log.warn("Error when resolving {}: {}", uri, e.getStatusCode());
            throw e;
        }
    }
}
