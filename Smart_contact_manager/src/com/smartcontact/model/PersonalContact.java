package com.smartcontact.model;

public class PersonalContact extends Contact {

    public PersonalContact() {
        setCategory("Personal");
        setCompany("");
    }

    public PersonalContact(int id, String name, String phone, String email, String address) {
        super(id, name, phone, email, address, "Personal", "");
    }

    public PersonalContact(String name, String phone, String email, String address) {
        super(name, phone, email, address, "Personal", "");
    }
}
