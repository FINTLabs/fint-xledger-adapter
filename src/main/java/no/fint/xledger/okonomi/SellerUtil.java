package no.fint.xledger.okonomi;

import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.xledger.model.contacts.Contact;
import no.fint.xledger.model.objectValues.Node;

public class SellerUtil {
    public static String createId(Node ordresalgsgruppe, Contact contact) {
        return createId(ordresalgsgruppe.getDescription(), contact.getDbId());
    }

    public static String createId(String salgsordregruppeDbId, String contactDbId) {
        if (salgsordregruppeDbId == null || salgsordregruppeDbId.length() == 0)
            throw new IllegalArgumentException("salgsordregruppeDbId must contain value");
        if (contactDbId == null || contactDbId.length() == 0)
            throw new IllegalArgumentException("contactDbId must contain value");

        return salgsordregruppeDbId + "-" + contactDbId;
    }

    public static String createName(SkoleResource skoleResource, Contact contact) {
        return createName(skoleResource.getNavn(), contact.getName());
    }

    public static String createName(String skoleName, String contactName) {
        return skoleName + " - " + contactName;
    }
}