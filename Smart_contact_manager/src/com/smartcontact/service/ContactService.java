package com.smartcontact.service;

import com.smartcontact.dao.ContactDAO;
import com.smartcontact.exception.ContactException;
import com.smartcontact.model.Contact;
import com.smartcontact.util.FileHandler;
import java.io.IOException;
import java.util.List;

public class ContactService {

    private static final String BACKUP_FILE = "contacts_backup.txt";
    private final ContactDAO contactDAO;

    public ContactService() {
        this.contactDAO = new ContactDAO();
    }

    public boolean addContact(Contact contact) throws ContactException {
        boolean result = contactDAO.addContact(contact);
        if (result) {
            backupToFile();
        }
        return result;
    }

    public boolean updateContact(Contact contact) throws ContactException {
        boolean result = contactDAO.updateContact(contact);
        if (result) {
            backupToFile();
        }
        return result;
    }

    public boolean deleteContact(int id) throws ContactException {
        boolean result = contactDAO.deleteContact(id);
        if (result) {
            backupToFile();
        }
        return result;
    }

    public List<Contact> getAllContacts() throws ContactException {
        return contactDAO.getAllContacts();
    }

    public List<Contact> searchContactByName(String name) throws ContactException {
        return contactDAO.searchContact(name);
    }

    public List<Contact> getSortedContactsAsc() throws ContactException {
        return contactDAO.sortContactsAsc();
    }

    public List<Contact> getSortedContactsDesc() throws ContactException {
        return contactDAO.sortContactsDesc();
    }

    private void backupToFile() throws ContactException {
        try {
            List<Contact> allContacts = contactDAO.getAllContacts();
            FileHandler.backupContacts(allContacts, BACKUP_FILE);
        } catch (IOException e) {
            throw new ContactException("Contact saved, but backup file creation failed.", e);
        }
    }
}
