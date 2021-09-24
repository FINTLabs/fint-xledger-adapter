package no.fint.xledger.okonomi.fakturagrunnlag;

import no.fint.model.felles.kompleksedatatyper.Personnavn;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.resource.felles.kompleksedatatyper.AdresseResource;
import no.fint.xledger.model.customer.CustomerDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.stream.Stream;

@Service
public class CustomerMapper {
    public CustomerDTO toXledger(PersonResource person, int companyDbId) {
        CustomerDTO customer = new CustomerDTO();

        customer.setDescription(getPersonnavnAsString(person.getNavn()));
        customer.setCompanyDbId(companyDbId);
        customer.setCompanyNumber(person.getFodselsnummer().getIdentifikatorverdi());

        if (person.getKontaktinformasjon() != null) {
            customer.setEmail(person.getKontaktinformasjon().getEpostadresse());
            customer.setPhone(person.getKontaktinformasjon().getMobiltelefonnummer());
            // TODO Bruk telefonnummer hvis ikke den har mobiltelefonnummer
        }

        Stream.of(person.getPostadresse(), person.getBostedsadresse())
                .filter(Objects::nonNull)
                .findFirst()
                .ifPresent(adresse -> {
                    customer.setStreetAdress(getStreetAdressAsString(adresse));
                    customer.setZipCode(adresse.getPostnummer());
                    customer.setPlace(adresse.getPoststed());
                });

        return customer;
    }

    private String getPersonnavnAsString(Personnavn navn) {
        if (navn == null) return null;

        String result = "";
        if (StringUtils.hasText(navn.getFornavn())) result = navn.getFornavn() + " ";
        if (StringUtils.hasText(navn.getMellomnavn())) result += navn.getMellomnavn() + " ";
        if (StringUtils.hasText(navn.getEtternavn())) result += navn.getEtternavn();

        return result.trim();
    }

    private String getStreetAdressAsString(AdresseResource adresse) {
        if (adresse.getAdresselinje() == null || adresse.getAdresselinje().size() == 0) return null;

        StringBuilder output = new StringBuilder();
        for (String line : adresse.getAdresselinje()){
            output.append(line + "\n");
        }

        return output.toString().trim();
    }
}