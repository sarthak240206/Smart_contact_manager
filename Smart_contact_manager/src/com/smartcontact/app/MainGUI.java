package com.smartcontact.app;

import com.smartcontact.gui.ContactForm;
import javax.swing.SwingUtilities;

public class MainGUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ContactForm form = new ContactForm();
            form.setVisible(true);
        });
    }
}
