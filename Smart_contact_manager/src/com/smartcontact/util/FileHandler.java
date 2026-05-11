package com.smartcontact.util;

import com.smartcontact.model.Contact;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class FileHandler {

    private FileHandler() {
    }

    public static void backupContacts(List<Contact> contacts, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Smart Contact Manager Backup");
            writer.newLine();
            writer.write("Generated At: " + LocalDateTime.now());
            writer.newLine();
            writer.write("------------------------------------------------------------");
            writer.newLine();

            for (Contact contact : contacts) {
                writer.write(
                        contact.getId() + " | "
                                + contact.getName() + " | "
                                + contact.getPhone() + " | "
                                + contact.getEmail() + " | "
                                + contact.getAddress() + " | "
                                + contact.getCategory() + " | "
                                + contact.getCompany()
                );
                writer.newLine();
            }
        }
    }
}
