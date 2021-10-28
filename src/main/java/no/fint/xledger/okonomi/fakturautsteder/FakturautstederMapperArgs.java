package no.fint.xledger.okonomi.fakturautsteder;

import lombok.Getter;
import lombok.Setter;
import no.fint.model.resource.utdanning.elev.SkoleressursResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.xledger.model.objectValues.Node;

import java.util.List;

public class FakturautstederMapperArgs {
    @Getter
    @Setter
    private Node salgsordregruppe;

    @Getter
    @Setter
    private String orgnummer;

    @Getter
    @Setter
    private SkoleResource skoleResource;

    @Getter
    @Setter
    private List<SkoleressursResource> skoleressursResources;

    public FakturautstederMapperArgs(Node salgsordregruppe, String orgnummer) {
        this.salgsordregruppe = salgsordregruppe;
        this.orgnummer = orgnummer;
    }
}
