package no.fint.xledger.okonomi.fakturautsteder;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.okonomi.faktura.FakturautstederResource;
import no.fint.model.resource.utdanning.elev.SkoleressursResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.xledger.fintclient.FintRepository;
import no.fint.xledger.graphql.caches.ContactCache;
import no.fint.xledger.graphql.caches.SalgsordregruppeCache;
import no.fint.xledger.model.contacts.Contact;
import no.fint.xledger.model.objectValues.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FakturautstederService {

    private final SalgsordregruppeCache salgsordregrupper;
    private final ContactCache contacts;
    private final FakturautstederMapper mapper;

    @Autowired
    private final FintRepository fintRepository;

    @Getter
    private List<FakturautstederResource> fakturautstedere;

    @Value("${fint.client.details.assetId}")
    private String organization;

    public FakturautstederService(SalgsordregruppeCache salgsordregrupper, ContactCache contacts, FakturautstederMapper mapper, FintRepository fintRepository) {
        this.salgsordregrupper = salgsordregrupper;
        this.contacts = contacts;
        this.mapper = mapper;
        this.fintRepository = fintRepository;
    }

    @Scheduled(initialDelay = 9000, fixedDelayString = "${fint.xledger.kodeverk.refresh-interval:1500000}")
    public void refresh() {
        log.debug("Refreshing Fakturautstedere...");
        fakturautstedere = new ArrayList<>();

        for (Node salgsordregruppe : salgsordregrupper.get()) {
            String orgNo = extractOrgnummerFromDescription(salgsordregruppe.getDescription());
            if (orgNo == null || orgNo.length() == 0) continue;

            SkoleResource skoleResource = fintRepository.getSkole(organization, orgNo);
            if (skoleResource == null) continue;

            List<SkoleressursResource> skoleressursResources = fintRepository.getSkoleressurser(organization, skoleResource.getSkoleressurs());
            log.info("School " + skoleResource.getNavn() + " contains " + skoleressursResources.size() + " skoleressurser");

            for (Contact matchingContact : findMatchingContacts(skoleressursResources)) {
                fakturautstedere.add(mapper.toFint(salgsordregruppe, skoleResource, matchingContact, orgNo));
                log.debug(skoleResource.getNavn() + " contact match: " + matchingContact.getName());
            }
        }

        log.info("Found " + fakturautstedere.size() + " fakturautstedere");
        log.debug("End refreshing Fakturautstedere");
    }

    private List<Contact> findMatchingContacts(List<SkoleressursResource> skoleressursResources) {

        ArrayList<Contact> matchingContacts = new ArrayList<>();

        for (SkoleressursResource skoleressurs : skoleressursResources) {

            List<Link> links = skoleressurs.getPersonalressurs();
            if (links.size() == 0) continue;
            // TODO Avklare hvorfor det er en liste

            PersonalressursResource personalressursResource = fintRepository.getPersonalressurs(organization, links.get(0));

            String ansattNr = personalressursResource.getAnsattnummer().getIdentifikatorverdi();

            //- Match kode på personalresurs med contacts fra xledger
            List<Contact> singleMatch = contacts
                    .get()
                    .stream()
                    .filter(c -> c != null && c.getContact() != null && c.getContact().getCode() != null && c.getContact().getCode().equals(ansattNr))
                    .map(no.fint.xledger.model.contacts.Node::getContact)
                    .collect(Collectors.toList());

            log.debug("Kontakten " + personalressursResource.getKontaktinformasjon().getEpostadresse() + " gav " + singleMatch.size() + " treff");

            if (singleMatch.size() == 0) {
                continue;
            } else if (singleMatch.size() == 1) {
                matchingContacts.add(singleMatch.stream().findFirst().get());
            } else {
                log.warn("There are " + singleMatch.size() + " matching contacts with kode: " + personalressursResource.getAnsattnummer().getIdentifikatorverdi());
                matchingContacts.add(singleMatch.stream().findFirst().get());
            }

            // TODO: Logg navn på match, for å sjekke
            log.debug("Match: " + personalressursResource.getKontaktinformasjon().getEpostadresse() + " med " + singleMatch.stream().findFirst().get().getEmail());
        }

        return matchingContacts;
    }


    private String extractOrgnummerFromDescription(String salgsordregruppeDescription) {
        // Example input: 91071 Kvadraturen videregående skole (974 595 117)
        // Example output: 974595117

        if (salgsordregruppeDescription == null || salgsordregruppeDescription.length() == 0) return "";

        int start = salgsordregruppeDescription.lastIndexOf('(');
        int end = salgsordregruppeDescription.lastIndexOf(')');

        if (start == -1 || end == -1 || start >= end) return "";
        return salgsordregruppeDescription.substring(start + 1, end).trim().replaceAll("[^0-9]", "");
    }
}
