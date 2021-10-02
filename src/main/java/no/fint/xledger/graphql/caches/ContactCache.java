package no.fint.xledger.graphql.caches;


import lombok.extern.slf4j.Slf4j;
import no.fint.xledger.graphql.ContactRepository;
import no.fint.xledger.model.contacts.Node;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

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
        if (contactCode == null || contactCode.length() == 0) return "";
        List<Node> filteredContacts = get().stream().filter(c -> c.getContact().getCode().equals(contactCode)).collect(Collectors.toList());
        if (filteredContacts.size() == 0) return "";
        if (filteredContacts.size() > 1) log.warn("Multiple contacts with code=" + contactCode);
        return filteredContacts.get(0).getContact().getDbId();
    }
}