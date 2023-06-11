package com.example.javaee.services;

import com.example.javaee.entities.ContactRecord;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhoneBookServiceImpl implements PhoneBookService {

    private final Connection connection;

    public PhoneBookServiceImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addContactRecord(ContactRecord contactRecord) throws SQLException {
        var stmnt = this.connection.prepareStatement("INSERT INTO contact_records (first_name, last_name, phone_number) VALUES (?, ?, ?)");
        stmnt.setString(1, contactRecord.getName());
        stmnt.setString(2, contactRecord.getSurname());
        stmnt.setString(3, contactRecord.getContactNumber());

        stmnt.executeUpdate();
    }

    @Override
    public List<ContactRecord> findAllContacts() throws SQLException {
        var stmnt = this.connection.prepareStatement("SELECT * FROM contact_records");
        var results = stmnt.executeQuery();

        var contactRecords = new ArrayList<ContactRecord>();

        while (results.next()) {
            contactRecords.add(new ContactRecord(
                results.getString("name"),
                results.getString("surname"),
                results.getString("contact_number")
            ));
        }

        return contactRecords;
    }
}
