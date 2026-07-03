package com.portfolio.changa_api.shared.builders;

import com.portfolio.changa_api.model.Guest;
import com.portfolio.changa_api.shared.dtos.GuestDTO;

public class GuestBuilder {
    private Long id;
    private String fullName;
    private String phoneNumber;
    private String address;

    public GuestBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public GuestBuilder setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public GuestBuilder setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public GuestBuilder setAddress(String address) {
        this.address = address;
        return this;
    }

    public GuestDTO buildDTO() {
        return new GuestDTO(fullName, phoneNumber, address);
    }

    public Guest buildEntity() {
        return new Guest(id, fullName, phoneNumber, address, null);
    }
}
