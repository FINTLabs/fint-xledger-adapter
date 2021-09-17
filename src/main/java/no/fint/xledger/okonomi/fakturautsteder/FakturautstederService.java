package no.fint.xledger.okonomi.fakturautsteder;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.administrasjon.personal.Personalressurs;
import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.okonomi.kodeverk.MerverdiavgiftResource;
import no.fint.model.resource.utdanning.elev.SkoleressursResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.xledger.fintclient.FintRepository;
import no.fint.xledger.graphql.caches.ContactCache;
import no.fint.xledger.graphql.caches.SalgsordregruppeCache;
import no.fint.xledger.model.objectValues.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FakturautstederService {

    private final SalgsordregruppeCache salgsordregrupper;
    private final ContactCache contacts;
    private final FakturautstederMapper mapper;

    @Autowired
    private final FintRepository fintRepository;

    @Getter
    private List<MerverdiavgiftResource> mva;

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
        log.info("Refreshing Fakturautstedere...");
        //mva = cache.get()
        //        .stream()
        //        .map(mapper::toFint)
        //        .collect(Collectors.toList());



        for (Node salgsordregruppe : salgsordregrupper.get()) {
            String orgNo = extractOrgnummerFromDescription(salgsordregruppe.getDescription());
            if (orgNo == null || orgNo.length() == 0) continue;

            SkoleResource skoleResource = fintRepository.getSkole(organization, orgNo);
            if (skoleResource == null) continue;
            log.debug("Found school: " + skoleResource.getNavn());

            List<SkoleressursResource> skoleressursResources = fintRepository.getSkoleressurser(organization, skoleResource.getSkoleressurs());
            log.debug("That contains " + skoleressursResources.size() + " skoleressurser");

            for (SkoleressursResource skoleressurs : skoleressursResources) {
                List<Link> links = skoleressurs.getPersonalressurs();
                if (links.size() == 0) continue;
                // TODO Avklare hvorfor det er en liste

                PersonalressursResource personalressursResource = fintRepository.getPersonalressurs(organization, links.get(0));

                //- Match kode på personalresurs med contacts fra xledger
                //contacts.get().stream().findFirst()
            }
        }

        log.info("End refreshing Fakturautstedere");
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
