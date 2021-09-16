package no.fint.xledger.graphql.caches;


import no.fint.xledger.graphql.ContactRepository;
import no.fint.xledger.model.contacts.Node;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

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
}