package no.fint.xledger.okonomi.fakturautsteder;

import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.okonomi.faktura.FakturautstederResource;
import no.fint.model.resource.okonomi.kodeverk.MerverdiavgiftResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.xledger.model.contacts.Contact;
import no.fint.xledger.model.objectValues.Node;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FakturautstederMapper {

    public FakturautstederResource toFint(Node salgsordregruppe, SkoleResource skoleResource, Contact contact) {
        FakturautstederResource fakturautsteder = new FakturautstederResource();

        Identifikator identifikator = new Identifikator();
        identifikator.setIdentifikatorverdi(salgsordregruppe.getDbId() + "-" + contact.getDbId());
        fakturautsteder.setSystemId(identifikator);

        fakturautsteder.setNavn(skoleResource.getNavn() + " - " + contact.getName());

        return fakturautsteder;
    }
}
