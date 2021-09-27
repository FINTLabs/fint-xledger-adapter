package no.fint.xledger.okonomi.fakturautsteder;

import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.fint.model.resource.okonomi.faktura.FakturautstederResource;
import no.fint.model.resource.okonomi.kodeverk.VareResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.xledger.graphql.caches.ProductCache;
import no.fint.xledger.model.contacts.Contact;
import no.fint.xledger.model.objectValues.Node;
import no.fint.xledger.okonomi.ConfigProperties;
import no.fint.xledger.okonomi.SellerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FakturautstederMapper {

    @Autowired
    private final ProductCache products;

    private  final ConfigProperties configProperties;

    public FakturautstederMapper(ProductCache products, ConfigProperties configProperties) {
        this.products = products;
        this.configProperties = configProperties;
    }

    public FakturautstederResource toFint(Node salgsordregruppe, SkoleResource skoleResource, Contact contact, String organisasjonsnummer) {
        FakturautstederResource fakturautsteder = new FakturautstederResource();

        Identifikator identifikator = new Identifikator();
        identifikator.setIdentifikatorverdi(SellerUtil.createFakturautstederId(salgsordregruppe, contact));
        fakturautsteder.setSystemId(identifikator);

        fakturautsteder.setNavn(SellerUtil.createFakturautstederName(skoleResource, contact));

        fakturautsteder.addOrganisasjonselement(Link.with(OrganisasjonselementResource.class, "organisasjonsnummer", organisasjonsnummer));

        for (no.fint.xledger.model.product.Node product : products.filterVarerByCode(salgsordregruppe.getCode(), configProperties.getDigistToCompareSalgsordregruppeAndProduct())) {
            fakturautsteder.addVare(Link.with(VareResource.class, "systemid", SellerUtil.createVareId(fakturautsteder, product)));
        }

        return fakturautsteder;
    }
}
