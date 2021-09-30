package no.fint.xledger.okonomi;

import no.fint.model.resource.okonomi.faktura.FakturautstederResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.xledger.model.contacts.Contact;
import no.fint.xledger.model.objectValues.Node;
import org.springframework.util.StringUtils;

/***
 * To be able to seperate each seller/contact in a school.
 * This class is a contract on how to concatenate id for fakturautsteder.
 ***/
public class SellerUtil {
    public static String createFakturautstederId(Node ordresalgsgruppe, Contact contact) {
        return ordresalgsgruppe.getDbId() + "-" + contact.getCode();
    }

    public static String createFakturautstederName(SkoleResource skoleResource, Contact contact) {
        String telefonnummer = "";
        if (skoleResource.getKontaktinformasjon() != null && StringUtils.hasText(skoleResource.getKontaktinformasjon().getTelefonnummer())) {
            telefonnummer = " Tlf: " + skoleResource.getKontaktinformasjon().getTelefonnummer();
        }

        return skoleResource.getNavn() + " - " + contact.getName() + telefonnummer;
    }

    public static String extractSalgsordregruppeDbId(String fakturautstederCombinedId) {
        if (fakturautstederCombinedId == null || fakturautstederCombinedId.length() == 0)
            throw new IllegalArgumentException("combindedId could not be empty");
        if (!fakturautstederCombinedId.contains("-"))
            throw new IllegalArgumentException("combindedId should contain a -");

        int end = fakturautstederCombinedId.indexOf("-");
        return fakturautstederCombinedId.substring(0, end);
    }

    public static String extractContactDbId(String fakturautstederCombinedId) {
        if (fakturautstederCombinedId == null || fakturautstederCombinedId.length() == 0)
            throw new IllegalArgumentException("combindedId could not be empty");
        if (!fakturautstederCombinedId.contains("-"))
            throw new IllegalArgumentException("combindedId should contain a -");

        int index = fakturautstederCombinedId.indexOf("-");
        return fakturautstederCombinedId.substring(index + 1);
    }

    public static String createVareId(FakturautstederResource fakturautstederResource, no.fint.xledger.model.product.Node vare) {
        return createVareId(fakturautstederResource.getSystemId().getIdentifikatorverdi(), vare.getDbId());
    }

    public static String createVareId(String fakturautstederId, int vareId) {
        if (fakturautstederId == null || fakturautstederId.length() == 0)
            throw new IllegalArgumentException("fakturautstederId could not be empty");
        if (vareId <= 0)
            throw new IllegalArgumentException("vareId could not be <= 0");

        return fakturautstederId + "_" + vareId;
    }

    public static String extractProductDbId(String productCombinedId) {
        if (productCombinedId == null || productCombinedId.length() == 0)
            throw new IllegalArgumentException("combindedId could not be empty");
        if (!productCombinedId.contains("_"))
            throw new IllegalArgumentException("combindedId should contain a _");

        return productCombinedId.substring(productCombinedId.indexOf("_") + 1);
    }
}