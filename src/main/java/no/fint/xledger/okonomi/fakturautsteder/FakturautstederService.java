package no.fint.xledger.okonomi.fakturautsteder;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.okonomi.faktura.FakturautstederResource;
import no.fint.model.resource.utdanning.elev.SkoleressursResource;
import no.fint.xledger.fintclient.FintRepository;
import no.fint.xledger.graphql.caches.ContactCache;
import no.fint.xledger.graphql.caches.SalgsordregruppeCache;
import no.fint.xledger.model.contacts.Contact;
import no.fint.xledger.model.objectValues.Node;
import no.fint.xledger.okonomi.CachedHandlerService;
import no.fint.xledger.okonomi.ConfigProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FakturautstederService extends CachedHandlerService {

    private final SalgsordregruppeCache salgsordregrupper;
    private final ContactCache contacts;
    private final FakturautstederMapper mapper;

    private final FintRepository fintRepository;

    private final ConfigProperties configProperties;

    private List<FakturautstederResource> fakturautstedere;

    public FakturautstederService(SalgsordregruppeCache salgsordregrupper, ContactCache contacts, FakturautstederMapper mapper, FintRepository fintRepository, ConfigProperties configProperties) {
        super(configProperties.getFintCache());
        this.salgsordregrupper = salgsordregrupper;
        this.contacts = contacts;
        this.mapper = mapper;
        this.fintRepository = fintRepository;
        this.configProperties = configProperties;
    }

    public List<FakturautstederResource> getFakturautstedere() {
        if (fakturautstedere == null) refreshIfNeeded();

        while (fakturautstedere == null) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                log.warn(e.getMessage());
            }
        }

        return fakturautstedere;
    }

    @Override
    @Scheduled(initialDelay = 9000, fixedDelayString = "${fint.xledger.kodeverk.refresh-interval:1500000}")
    public void refreshIfNeeded() {
        super.refreshIfNeeded();
    }

    public void refreshData() {
        log.debug("Refreshing Fakturautstedere...");
        List<FakturautstederResource> result = salgsordregrupper.get()
                .stream()
                .map(addOrganisationNumber())
                .filter(hasOrganisationNumber())
                .peek(addSchool())
                .filter(hasSchool())
                .peek(addSkoleressurs())
                .peek(args -> log.info("School " + args.getSkoleResource().getNavn() + " contains " + args.getSkoleressursResources().size() + " skoleressurser"))
                .flatMap(args -> findMatchingContacts(args.getSkoleressursResources())
                        .stream()
                        .peek(matchingContact -> log.debug(args.getSkoleResource().getNavn() + " contact match: " + matchingContact.getName()))
                        .map(matchingContact -> mapper.toFint(args.getSalgsordregruppe(), args.getSkoleResource(), matchingContact, args.getOrgnummer())))
                .collect(Collectors.toList());

        log.info("Found " + result.size() + " fakturautstedere");
        this.fakturautstedere = result;
        log.debug("End refreshing Fakturautstedere");
    }

    private Function<Node, FakturautstederMapperArgs> addOrganisationNumber() {
        return salgsordregruppe ->
                new FakturautstederMapperArgs(
                        salgsordregruppe,
                        extractOrgnummerFromDescription(salgsordregruppe.getDescription())
                );
    }

    private Consumer<FakturautstederMapperArgs> addSkoleressurs() {
        return args -> args.setSkoleressursResources(fintRepository.getSkoleressurser(configProperties.getOrganization(), args.getSkoleResource().getSkoleressurs()));
    }

    private Predicate<FakturautstederMapperArgs> hasSchool() {
        return args -> args.getSkoleResource() != null;
    }

    private Consumer<FakturautstederMapperArgs> addSchool() {
        return args -> args.setSkoleResource(fintRepository.getSkole(configProperties.getOrganization(), args.getOrgnummer()));
    }

    private Predicate<FakturautstederMapperArgs> hasOrganisationNumber() {
        return args -> args.getOrgnummer().length() > 0;
    }

    private List<Contact> findMatchingContacts(List<SkoleressursResource> skoleressursResources) {

        ArrayList<Contact> matchingContacts = new ArrayList<>();

        for (SkoleressursResource skoleressurs : skoleressursResources) {

            List<Link> links = skoleressurs.getPersonalressurs();
            if (links.size() == 0) continue;
            // TODO Avklare hvorfor det er en liste

            PersonalressursResource personalressursResource = fintRepository.getPersonalressurs(configProperties.getOrganization(), links.get(0));
            if (personalressursResource == null) {
                log.error("Didn't find personalresurs for skoleressurs " + links.get(0));
                continue;
            }

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
