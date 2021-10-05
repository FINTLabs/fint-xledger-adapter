package no.fint.xledger.graphql.caches;


import lombok.extern.slf4j.Slf4j;
import no.fint.xledger.graphql.ContactRepository;
import no.fint.xledger.model.contacts.Contact;
import no.fint.xledger.model.contacts.Node;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ContactCache extends Cache<Node> {

    private final ContactRepository repository;

    public ContactCache(ContactRepository repository) {
        super(Duration.ofDays(1));
        this.repository = repository;
    }

    @Override
    protected List<Node> getData() {
        return repository.queryContacts();
    }

    public String getDbIdByCode(String contactCode) {
        try {
            if (contactCode == null || contactCode.length() == 0) return "";
            log.info("Finding contact with code: " + contactCode);
            Optional<Node> contact = get()
                    .stream()
                    .filter(c -> c != null
                            && c.getContact() != null
                            && c.getContact().getCode() != null
                            && c.getContact().getCode().equals(contactCode))
                    .findFirst();

            if (contact == null || contact.isEmpty()) {
                log.warn("Didn't find a contact with matching code: " + contactCode);
                return "";
            }

            Node node = contact.get();
            if (node == null) log.warn("getDbIdByCode: node is null");
            Contact nodeContact = node.getContact();
            if (nodeContact == null) log.warn("getDbIdByCode: contact is null");
            String dbId = nodeContact.getDbId();
            if (dbId == null) log.warn("getDbIdByCode: dbId is null");
            return dbId;

            //return contact.get().getContact().getDbId();
        } catch (Exception e) {
            log.error("Exception in getDbIdByCode: " + e.getMessage());
            return "";
        }
    }
}