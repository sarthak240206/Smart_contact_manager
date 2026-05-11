package com.smartcontact.model;

public class EnterpriseContact extends Contact {

    public EnterpriseContact() {
        setCategory("Enterprise");
    }

    public EnterpriseContact(int id, String name, String phone, String email, String address, String company) {
        super(id, name, phone, email, address, "Enterprise", company);
    }

    public EnterpriseContact(String name, String phone, String email, String address, String company) {
        super(name, phone, email, address, "Enterprise", company);
    }
}
