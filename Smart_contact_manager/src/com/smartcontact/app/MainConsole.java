package com.smartcontact.app;

import com.smartcontact.exception.ContactException;
import com.smartcontact.model.Contact;
import com.smartcontact.model.EnterpriseContact;
import com.smartcontact.model.PersonalContact;
import com.smartcontact.service.ContactService;
import java.util.List;
import java.util.Scanner;
import com.smartcontact.util.ValidationUtil;

public class MainConsole {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ContactService service = new ContactService();
        boolean running = true;

        while (running) {
            printMenu();
            int choice = readInt(scanner, "Enter choice: ");

            try {
                switch (choice) {
                    case 1:
                        Contact newContact = readContactDetails(scanner, false, 0);
                        System.out.println(service.addContact(newContact) ? "Contact added." : "Contact not added.");
                        break;
                    case 2:
                        displayContacts(service.getAllContacts());
                        break;
                    case 3:
                        int updateId = readInt(scanner, "Enter contact ID to update: ");
                        Contact updatedContact = readContactDetails(scanner, true, updateId);
                        System.out.println(service.updateContact(updatedContact) ? "Contact updated." : "Contact not updated.");
                        break;
                    case 4:
                        int deleteId = readInt(scanner, "Enter contact ID to delete: ");
                        System.out.println(service.deleteContact(deleteId) ? "Contact deleted." : "Contact not found.");
                        break;
                    case 5:
                        System.out.print("Enter name to search: ");
                        String searchName = scanner.nextLine().trim();
                        displayContacts(service.searchContactByName(searchName));
                        break;
                    case 6:
                        displayContacts(service.getSortedContactsAsc());
                        break;
                    case 7:
                        displayContacts(service.getSortedContactsDesc());
                        break;
                    case 0:
                        running = false;
                        System.out.println("Application closed.");
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (ContactException e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println();
        }
    }

    private static void printMenu() {
        System.out.println("====== Smart Contact Manager (Console) ======");
        System.out.println("1. Add Contact");
        System.out.println("2. View Contacts");
        System.out.println("3. Update Contact");
        System.out.println("4. Delete Contact");
        System.out.println("5. Search Contact by Name");
        System.out.println("6. Sort Contacts A-Z");
        System.out.println("7. Sort Contacts Z-A");
        System.out.println("0. Exit");
    }

    private static Contact readContactDetails(Scanner scanner, boolean includeId, int id) {

    String name;
    while (true) {
        System.out.print("Name: ");
        name = scanner.nextLine().trim();

        if (!name.isEmpty()) {
            break;
        }
        System.out.println("Name cannot be empty.");
    }

    String phone;
    while (true) {
        System.out.print("Phone: ");
        phone = scanner.nextLine().trim();

        if (ValidationUtil.isValidPhone(phone)) {
            phone = ValidationUtil.normalizePhone(phone);
            break;
        }
        System.out.println("Invalid phone number. Enter 10 digits only.");
    }

    String email;
    while (true) {
        System.out.print("Email: ");
        email = scanner.nextLine().trim();

        if (ValidationUtil.isValidEmail(email)) {
            break;
        }
        System.out.println("Invalid email format.");
    }

    String address;
    while (true) {
        System.out.print("Address: ");
        address = scanner.nextLine().trim();

        if (!address.isEmpty()) {
            break;
        }
        System.out.println("Address cannot be empty.");
    }

    System.out.print("Category (Personal/Enterprise): ");
    String category = scanner.nextLine().trim();

    if ("Enterprise".equalsIgnoreCase(category)) {

        String company;
        while (true) {
            System.out.print("Company Name: ");
            company = scanner.nextLine().trim();

            if (!company.isEmpty()) {
                break;
            }
            System.out.println("Company name cannot be empty.");
        }

        if (includeId) {
            return new EnterpriseContact(id, name, phone, email, address, company);
        }

        return new EnterpriseContact(name, phone, email, address, company);

    } else {

        if (includeId) {
            return new PersonalContact(id, name, phone, email, address);
        }

        return new PersonalContact(name, phone, email, address);
    }
}
    private static int readInt(Scanner scanner, String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static void displayContacts(List<Contact> contacts) {
        if (contacts.isEmpty()) {
            System.out.println("No contacts found.");
            return;
        }
        for (Contact contact : contacts) {
            System.out.println(contact);
        }
    }
}
