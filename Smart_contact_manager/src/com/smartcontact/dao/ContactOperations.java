package com.smartcontact.dao;

import com.smartcontact.exception.ContactException;
import com.smartcontact.model.Contact;
import java.util.List;

public interface ContactOperations {

    boolean addContact(Contact contact) throws ContactException;

    boolean updateContact(Contact contact) throws ContactException;

    boolean deleteContact(int id) throws ContactException;

    List<Contact> searchContact(String name) throws ContactException;

    List<Contact> getAllContacts() throws ContactException;

    List<Contact> sortContactsAsc() throws ContactException;

    List<Contact> sortContactsDesc() throws ContactException;
}
