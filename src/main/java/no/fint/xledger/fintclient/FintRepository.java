package no.fint.xledger.fintclient;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.resource.okonomi.kodeverk.VareResource;
import no.fint.model.resource.utdanning.elev.SkoleressursResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class FintRepository {

    @Autowired
    private ResourceResolverService resolverService;

    //resolverService.resolve(orgid, resolveLink(fakturagrunnlag.getMottaker().getPerson()), PersonResource .class)

    public SkoleResource getSkole(String orgId, String orgNummer) {
        try {
            return resolverService.resolve(orgId, resolveUrlSkole(orgNummer), SkoleResource.class);
        } catch (Exception e) {
            // TODO: Better way to handle this? (EntityNotFoundException)
            // org.springframework.web.client.HttpClientErrorException$NotFound: 404 : [{"message":"912912912","exception":"class no.fint.consumer.exceptions.EntityNotFoundException"}]
            log.info(e.toString());
        }
        return null;
    }

    public List<SkoleressursResource> getSkoleressurser(String orgId, List<Link> links) {
        ArrayList<SkoleressursResource> skoleressurser = new ArrayList<>(links.size());

        for (Link link : links) {
            skoleressurser.add(resolverService.resolve(orgId, link.getHref(), SkoleressursResource.class));
        }

        return skoleressurser;
    }

    public PersonalressursResource getPersonalressurs(String orgId, Link link) {
        try {
            return resolverService.resolve(orgId, link.getHref(), PersonalressursResource.class);
        } catch (Exception e) {
            // TODO: Better way to handle this? (EntityNotFoundException)
            // org.springframework.web.client.HttpClientErrorException$NotFound: 404 : [{"message":"912912912","exception":"class no.fint.consumer.exceptions.EntityNotFoundException"}]
            log.info(e.toString());
        }
        return null;
    }

    private String resolveUrlSkole(String orgNummer) {
        // TODO: Find the proper way to do this (this should not be hardcoded)
        String endpointConfig = String.format("https://beta.felleskomponent.no/utdanning/utdanningsprogram/skole/organisasjonsnummer/%s", orgNummer);
        return endpointConfig;
    }

    /*
    Personalressurs
    Hente personalressurs basert på ansattnummer (kode kommer fra Xledger)
    https://api.felleskomponent.no/administrasjon/personal/personalressurs/ansattnummer/95071
    Model: https://informasjonsmodell.felleskomponent.no/docs/personal_personalressurs?v=v3.8.0

    Hvordan finne en skoleressurs
    https://informasjonsmodell.felleskomponent.no/docs/elev_skoleressurs?v=v3.8.0

    Skole
    https://api.felleskomponent.no/utdanning/utdanningsprogram/skole/organisasjonsnummer/921707134
    Model: https://informasjonsmodell.felleskomponent.no/docs/utdanningsprogram_skole?v=v3.8.0

    Skole -> skoleressurs -> Personalressurs

     */

    public String resolveLink(List<Link> links) {
        return links.stream().map(Link::getHref).findAny().orElseThrow(IllegalArgumentException::new);
    }

    public PersonResource getPerson(String orgId, List<Link> links) {
        return resolverService.resolve(orgId, resolveLink(links), PersonResource.class);
    }

    public VareResource getVare(String orgId, List<Link> vare) {
        return resolverService.resolve(orgId, resolveLink(vare), VareResource.class);
    }
}
