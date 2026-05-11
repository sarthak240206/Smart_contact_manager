package com.smartcontact.gui;

import com.smartcontact.exception.ContactException;
import com.smartcontact.model.Contact;
import com.smartcontact.model.EnterpriseContact;
import com.smartcontact.model.PersonalContact;
import com.smartcontact.service.ContactService;
import com.smartcontact.util.ValidationUtil;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class ContactForm extends JFrame {

    private final ContactService service;

    private final JTextField idField;
    private final JTextField nameField;
    private final JTextField phoneField;
    private final JTextField emailField;
    private final JTextField addressField;
    private final JComboBox<String> categoryBox;
    private final JTextField companyField;
    private final JTextField searchField;

    private final DefaultTableModel tableModel;
    private final JTable contactTable;

    // Modern Colors
    private final Color primaryColor = new Color(33, 150, 243);
    private final Color darkColor = new Color(25, 35, 45);
    private final Color backgroundColor = new Color(245, 247, 250);
    private final Color whiteColor = Color.WHITE;
    private final Color successColor = new Color(46, 204, 113);
    private final Color dangerColor = new Color(231, 76, 60);

    public ContactForm() {

        service = new ContactService();

        setTitle("Smart Contact Manager");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getContentPane().setBackground(backgroundColor);
        setLayout(new BorderLayout(15, 15));

        // Fields
        idField = createTextField();
        idField.setEditable(false);

        nameField = createTextField();
        phoneField = createTextField();
        emailField = createTextField();
        addressField = createTextField();
        companyField = createTextField();
        searchField = createTextField();

        categoryBox = new JComboBox<>(new String[]{"Personal", "Enterprise"});
        styleComboBox(categoryBox);

        // Table
        tableModel = new DefaultTableModel(
                new String[]{
                    "ID", "Name", "Phone", "Email",
                    "Address", "Category", "Company"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        contactTable = new JTable(tableModel);
        styleTable(contactTable);

        JScrollPane scrollPane = new JScrollPane(contactTable);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Header
        JPanel headerPanel = createHeader();

        // Form Panel
        JPanel formPanel = createFormPanel();

        // Button Panel
        JPanel buttonPanel = createButtonPanel();

        // Top Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(backgroundColor);
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);
        add(topPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);

        // Table Selection
        contactTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedRowToForm();
            }
        });

        loadAllContacts();
    }

   

    private JPanel createHeader() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(darkColor);
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("Smart Contact Manager");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));

        JLabel subtitle = new JLabel("Manage Personal & Enterprise Contacts");
        subtitle.setForeground(new Color(220, 220, 220));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setBackground(darkColor);
        textPanel.add(title);
        textPanel.add(subtitle);

        panel.add(textPanel, BorderLayout.WEST);

        return panel;
    }

    

    private JPanel createFormPanel() {

        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBackground(whiteColor);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setPreferredSize(new Dimension(350, 400));

        panel.add(createLabel("Contact ID"));
        panel.add(idField);

        panel.add(createLabel("Full Name"));
        panel.add(nameField);

        panel.add(createLabel("Phone Number"));
        panel.add(phoneField);

        panel.add(createLabel("Email"));
        panel.add(emailField);

        panel.add(createLabel("Address"));
        panel.add(addressField);

        panel.add(createLabel("Category"));
        panel.add(categoryBox);

        panel.add(createLabel("Company"));
        panel.add(companyField);

        panel.add(createLabel("Search Name"));
        panel.add(searchField);

        return panel;
    }


    private JPanel createButtonPanel() {

        JPanel panel = new JPanel(new GridLayout(2, 4, 10, 10));
        panel.setBackground(backgroundColor);
        panel.setBorder(new EmptyBorder(15, 10, 15, 10));

        JButton addButton = createButton("Add", successColor);
        JButton updateButton = createButton("Update", primaryColor);
        JButton deleteButton = createButton("Delete", dangerColor);
        JButton searchButton = createButton("Search", darkColor);

        JButton sortAscButton = createButton("Sort A-Z", primaryColor);
        JButton sortDescButton = createButton("Sort Z-A", primaryColor);
        JButton clearButton = createButton("Clear", Color.GRAY);

        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(searchButton);
        panel.add(sortAscButton);
        panel.add(sortDescButton);
        panel.add(clearButton);

        // Actions
        addButton.addActionListener(e -> addContact());
        updateButton.addActionListener(e -> updateContact());
        deleteButton.addActionListener(e -> deleteContact());
        searchButton.addActionListener(e -> searchContact());
        sortAscButton.addActionListener(e -> sortAsc());
        sortDescButton.addActionListener(e -> sortDesc());
        clearButton.addActionListener(e -> clearForm());

        return panel;
    }



    private JTextField createTextField() {

        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(200, 35));

        return field;
    }

    private JLabel createLabel(String text) {

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));

        return label;
    }

    private JButton createButton(String text, Color color) {

        JButton button = new JButton(text);

        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));

        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.setPreferredSize(new Dimension(120, 40));

        return button;
    }

    private void styleComboBox(JComboBox<String> comboBox) {

        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
    }

    private void styleTable(JTable table) {

        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        table.getTableHeader().setBackground(primaryColor);
        table.getTableHeader().setForeground(Color.WHITE);

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        table.setSelectionBackground(new Color(184, 207, 229));
    }

    // ================= CRUD =================

    private void addContact() {

        try {

            Contact contact = buildContactFromForm(false);

            boolean saved = service.addContact(contact);

            showMessage(saved
                    ? "Contact added successfully."
                    : "Contact could not be added.");

            loadAllContacts();
            clearForm();

        } catch (ContactException e) {

            showError(e.getMessage());
        }
    }

    private void updateContact() {

        if (idField.getText().trim().isEmpty()) {

            showError("Please select a contact.");
            return;
        }

        try {

            Contact contact = buildContactFromForm(true);

            boolean updated = service.updateContact(contact);

            showMessage(updated
                    ? "Contact updated successfully."
                    : "Contact could not be updated.");

            loadAllContacts();
            clearForm();

        } catch (ContactException e) {

            showError(e.getMessage());
        }
    }

    private void deleteContact() {

        if (idField.getText().trim().isEmpty()) {

            showError("Please select a contact.");
            return;
        }

        try {

            int id = Integer.parseInt(idField.getText());

            boolean deleted = service.deleteContact(id);

            showMessage(deleted
                    ? "Contact deleted successfully."
                    : "Contact not found.");

            loadAllContacts();
            clearForm();

        } catch (Exception e) {

            showError(e.getMessage());
        }
    }

    private void searchContact() {

        String keyword = searchField.getText().trim();

        try {

            if (keyword.isEmpty()) {

                loadAllContacts();

            } else {

                setTableData(service.searchContactByName(keyword));
            }

        } catch (ContactException e) {

            showError(e.getMessage());
        }
    }

    private void sortAsc() {

        try {

            setTableData(service.getSortedContactsAsc());

        } catch (ContactException e) {

            showError(e.getMessage());
        }
    }

    private void sortDesc() {

        try {

            setTableData(service.getSortedContactsDesc());

        } catch (ContactException e) {

            showError(e.getMessage());
        }
    }

    

    private Contact buildContactFromForm(boolean includeId)
            throws ContactException {

        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();
        String category = String.valueOf(categoryBox.getSelectedItem());
        String company = companyField.getText().trim();

        
        if (name.isEmpty()) {

            throw new ContactException(
                    "Name cannot be empty.");
        }

       
        if (phone.isEmpty()) {

            throw new ContactException(
                    "Phone number is required.");
        }

        if (!ValidationUtil.isValidPhone(phone)) {

            throw new ContactException(
                    "Invalid phone number. Enter 10 digits only.");
        }

       
        phone = ValidationUtil.normalizePhone(phone);

       
        if (email.isEmpty()) {

            throw new ContactException(
                    "Email cannot be empty.");
        }

        if (!ValidationUtil.isValidEmail(email)) {

            throw new ContactException(
                    "Invalid email format.");
        }

        
        if (address.isEmpty()) {

            throw new ContactException(
                    "Address cannot be empty.");
        }

        
        if ("Enterprise".equalsIgnoreCase(category)) {

            if (company.isEmpty()) {

                throw new ContactException(
                        "Company name is required.");
            }

            if (includeId) {

                return new EnterpriseContact(
                        Integer.parseInt(idField.getText()),
                        name,
                        phone,
                        email,
                        address,
                        company);
            }

            return new EnterpriseContact(
                    name,
                    phone,
                    email,
                    address,
                    company);
        }

        
        if (includeId) {

            return new PersonalContact(
                    Integer.parseInt(idField.getText()),
                    name,
                    phone,
                    email,
                    address);
        }

        return new PersonalContact(
                name,
                phone,
                email,
                address);
    }

    private void loadAllContacts() {

        try {

            setTableData(service.getAllContacts());

        } catch (ContactException e) {

            showError(e.getMessage());
        }
    }

    private void setTableData(List<Contact> contacts) {

        tableModel.setRowCount(0);

        for (Contact contact : contacts) {

            tableModel.addRow(new Object[]{
                contact.getId(),
                contact.getName(),
                contact.getPhone(),
                contact.getEmail(),
                contact.getAddress(),
                contact.getCategory(),
                contact.getCompany()
            });
        }
    }

    private void loadSelectedRowToForm() {

        int row = contactTable.getSelectedRow();

        if (row < 0) {
            return;
        }

        idField.setText(String.valueOf(
                tableModel.getValueAt(row, 0)));

        nameField.setText(String.valueOf(
                tableModel.getValueAt(row, 1)));

        phoneField.setText(String.valueOf(
                tableModel.getValueAt(row, 2)));

        emailField.setText(String.valueOf(
                tableModel.getValueAt(row, 3)));

        addressField.setText(String.valueOf(
                tableModel.getValueAt(row, 4)));

        categoryBox.setSelectedItem(String.valueOf(
                tableModel.getValueAt(row, 5)));

        companyField.setText(String.valueOf(
                tableModel.getValueAt(row, 6)));
    }

    private void clearForm() {

        idField.setText("");
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        addressField.setText("");
        companyField.setText("");
        searchField.setText("");

        categoryBox.setSelectedIndex(0);

        contactTable.clearSelection();
    }

    

    private void showMessage(String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showError(String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}