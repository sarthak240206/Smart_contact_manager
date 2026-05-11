package com.smartcontact.dao;

import com.smartcontact.db.DBConnection;
import com.smartcontact.exception.ContactException;
import com.smartcontact.model.Contact;
import com.smartcontact.model.EnterpriseContact;
import com.smartcontact.model.PersonalContact;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactDAO implements ContactOperations {

    private static final String INSERT_SQL = "INSERT INTO contacts(name, phone, email, address, category, company) VALUES(?,?,?,?,?,?)";
    private static final String UPDATE_SQL = "UPDATE contacts SET name=?, phone=?, email=?, address=?, category=?, company=? WHERE id=?";
    private static final String DELETE_SQL = "DELETE FROM contacts WHERE id=?";
    private static final String SEARCH_SQL = "SELECT * FROM contacts WHERE name LIKE ?";
    private static final String GET_ALL_SQL = "SELECT * FROM contacts";
    private static final String SORT_ASC_SQL = "SELECT * FROM contacts ORDER BY name ASC";
    private static final String SORT_DESC_SQL = "SELECT * FROM contacts ORDER BY name DESC";

    @Override
    public boolean addContact(Contact contact) throws ContactException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_SQL)) {

            ps.setString(1, contact.getName());
            ps.setString(2, contact.getPhone());
            ps.setString(3, contact.getEmail());
            ps.setString(4, contact.getAddress());
            ps.setString(5, contact.getCategory());
            ps.setString(6, contact.getCompany());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ContactException("Failed to add contact.", e);
        }
    }

    @Override
    public boolean updateContact(Contact contact) throws ContactException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, contact.getName());
            ps.setString(2, contact.getPhone());
            ps.setString(3, contact.getEmail());
            ps.setString(4, contact.getAddress());
            ps.setString(5, contact.getCategory());
            ps.setString(6, contact.getCompany());
            ps.setInt(7, contact.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ContactException("Failed to update contact.", e);
        }
    }

    @Override
    public boolean deleteContact(int id) throws ContactException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_SQL)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ContactException("Failed to delete contact.", e);
        }
    }

    @Override
    public List<Contact> searchContact(String name) throws ContactException {
        List<Contact> contacts = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SEARCH_SQL)) {

            ps.setString(1, "%" + name + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    contacts.add(mapToContact(rs));
                }
            }
        } catch (SQLException e) {
            throw new ContactException("Failed to search contacts.", e);
        }
        return contacts;
    }

    @Override
    public List<Contact> getAllContacts() throws ContactException {
        return getContactsByQuery(GET_ALL_SQL);
    }

    @Override
    public List<Contact> sortContactsAsc() throws ContactException {
        return getContactsByQuery(SORT_ASC_SQL);
    }

    @Override
    public List<Contact> sortContactsDesc() throws ContactException {
        return getContactsByQuery(SORT_DESC_SQL);
    }

    private List<Contact> getContactsByQuery(String sql) throws ContactException {
        List<Contact> contacts = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                contacts.add(mapToContact(rs));
            }
            return contacts;
        } catch (SQLException e) {
            throw new ContactException("Failed to fetch contacts.", e);
        }
    }

    private Contact mapToContact(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String phone = rs.getString("phone");
        String email = rs.getString("email");
        String address = rs.getString("address");
        String category = rs.getString("category");
        String company = rs.getString("company");

        if ("Enterprise".equalsIgnoreCase(category)) {
            return new EnterpriseContact(id, name, phone, email, address, company);
        }
        return new PersonalContact(id, name, phone, email, address);
    }
}
