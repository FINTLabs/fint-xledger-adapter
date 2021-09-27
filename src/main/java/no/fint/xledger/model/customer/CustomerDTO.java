package no.fint.xledger.model.customer;

import lombok.Getter;
import lombok.Setter;

public class CustomerDTO {
    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String streetAdress;

    @Getter
    @Setter
    private String zipCode;

    @Getter
    @Setter
    private String place;

    @Getter
    @Setter
    private String phone;

    @Getter
    @Setter
    private int companyDbId;

    @Getter
    @Setter
    private String companyNumber;
}
