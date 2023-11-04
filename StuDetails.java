

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
public class StuDetails extends JFrame {
    private AddressBook addressBook;
    private JTextField nameField;
    private JTextField phoneNumberField;
    private JTextField emailAddressField;
    private JTextArea displayArea;

    public StuDetails() {
        addressBook = new AddressBook();

        setTitle("Address Book System");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        JLabel nameLabel = new JLabel("Name:");
        JLabel phoneNumberLabel = new JLabel("Phone Number:");
        JLabel emailAddressLabel = new JLabel("Email Address:");

        nameField = new JTextField();
        phoneNumberField = new JTextField();
        emailAddressField = new JTextField();

        JButton addButton = new JButton("Add Contact");
        JButton editButton = new JButton("Edit Contact");
        JButton searchButton = new JButton("Search Contact");
        JButton displayButton = new JButton("Display All Contacts");
        JButton saveButton = new JButton("Save to File");
        JButton loadButton = new JButton("Load from File");
        JButton exitButton = new JButton("Exit");

        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(phoneNumberLabel);
        inputPanel.add(phoneNumberField);
        inputPanel.add(emailAddressLabel);
        inputPanel.add(emailAddressField);
        inputPanel.add(addButton);
        inputPanel.add(editButton);

        JPanel displayPanel = new JPanel(new BorderLayout());
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        displayPanel.add(scrollPane, BorderLayout.CENTER);
        displayPanel.add(displayButton, BorderLayout.SOUTH);

        JPanel fileOperationsPanel = new JPanel(new FlowLayout());
        fileOperationsPanel.add(saveButton);
        fileOperationsPanel.add(loadButton);

        JPanel exitPanel = new JPanel(new FlowLayout());
        exitPanel.add(exitButton);

        add(inputPanel, BorderLayout.NORTH);
        add(displayPanel, BorderLayout.CENTER);
        add(fileOperationsPanel, BorderLayout.SOUTH);
        add(exitPanel, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addContact();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editContact();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchContact();
            }
        });

        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayAllContacts();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveContactsToFile();
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadContactsFromFile();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitApplication();
            }
        });
    }

    private void addContact() {
        String name = nameField.getText();
        String phoneNumber = phoneNumberField.getText();
        String emailAddress = emailAddressField.getText();

        if (name.isEmpty() || phoneNumber.isEmpty() || emailAddress.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!isValidPhoneNumber(phoneNumber) || !isValidEmailAddress(emailAddress)) {
            JOptionPane.showMessageDialog(this, "Invalid phone number or email address format.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            Contact contact = new Contact(name, phoneNumber, emailAddress);
            addressBook.addContact(contact);
            clearInputFields();
            displayArea.append("Contact added: " + contact.getName() + "\n");
        }
    }

    private void editContact() {
        String name = nameField.getText();
        String phoneNumber = phoneNumberField.getText();
        String emailAddress = emailAddressField.getText();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a name to edit.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!isValidPhoneNumber(phoneNumber) || !isValidEmailAddress(emailAddress)) {
            JOptionPane.showMessageDialog(this, "Invalid phone number or email address format.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            Contact existingContact = addressBook.searchContact(name);
            if (existingContact != null) {
                existingContact.setPhoneNumber(phoneNumber);
                existingContact.setEmailAddress(emailAddress);
                clearInputFields();
                displayArea.setText("Contact updated: " + existingContact.getName() + "\n");
            } else {
                displayArea.setText("Contact not found.");
            }
        }
    }

    private void searchContact() {
        String name = nameField.getText();

        if (!name.isEmpty()) {
            Contact contact = addressBook.searchContact(name);
            if (contact != null) {
                displayArea.setText(contact.toString());
            } else {
                displayArea.setText("Contact not found.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a name to search.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayAllContacts() {
        List<Contact> contacts = addressBook.getAllContacts();
        displayArea.setText("");

        if (contacts.isEmpty()) {
            displayArea.append("No contacts in the address book.");
        } else {
            for (Contact contact : contacts) {
                displayArea.append(contact.toString() + "\n\n");
            }
        }
    }

    private void clearInputFields() {
        nameField.setText("");
        phoneNumberField.setText("");
        emailAddressField.setText("");
    }

    private void saveContactsToFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showSaveDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            addressBook.saveContactsToFile(file.getAbsolutePath());
        }
    }

    private void loadContactsFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            addressBook.loadContactsFromFile(file.getAbsolutePath());
        }
    }

    private void exitApplication() {
        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
       
        return phoneNumber.matches("\\d{10}"); 
    }

    private boolean isValidEmailAddress(String emailAddress) {
    
        return emailAddress.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                StuDetails addressBookGUI = new StuDetails();
                addressBookGUI.setVisible(true);
            }
        });
    }

}

class Contact {
    private String name;
    private String phoneNumber;
    private String emailAddress;

    public Contact(String name, String phoneNumber, String emailAddress) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\nPhone Number: " + phoneNumber + "\nEmail Address: " + emailAddress;
    }
}

class AddressBook {
    private List<Contact> contacts;

    public AddressBook() {
        contacts = new ArrayList<>();
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    public Contact searchContact(String name) {
        for (Contact contact : contacts) {
            if (contact.getName().equalsIgnoreCase(name)) {
                return contact;
            }
        }
        return null;
    }

    public List<Contact> getAllContacts() {
        return contacts;
    }

    public void saveContactsToFile(String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (Contact contact : contacts) {
                writer.println(contact.getName());
                writer.println(contact.getPhoneNumber());
                writer.println(contact.getEmailAddress());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadContactsFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String name, phoneNumber, emailAddress;
            while ((name = reader.readLine()) != null) {
                phoneNumber = reader.readLine();
                emailAddress = reader.readLine();
                Contact contact = new Contact(name, phoneNumber, emailAddress);
                contacts.add(contact);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
