package no.fint.xledger.okonomi.fakturautsteder;

import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.okonomi.faktura.FakturautstederResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.xledger.model.contacts.Contact;
import no.fint.xledger.model.objectValues.Node;
import no.fint.xledger.okonomi.SellerUtil;
import org.springframework.stereotype.Service;

@Service
public class FakturautstederMapper {

    public FakturautstederResource toFint(Node salgsordregruppe, SkoleResource skoleResource, Contact contact) {
        FakturautstederResource fakturautsteder = new FakturautstederResource();

        Identifikator identifikator = new Identifikator();
        identifikator.setIdentifikatorverdi(SellerUtil.createFakturautstederId(salgsordregruppe, contact));
        fakturautsteder.setSystemId(identifikator);

        fakturautsteder.setNavn(SellerUtil.createFakturautstederName(skoleResource, contact));

        return fakturautsteder;
    }
}
