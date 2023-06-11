package com.example.javaee.services;

import com.example.javaee.entities.ContactRecord;

import java.sql.SQLException;
import java.util.List;

public interface PhoneBookService {
    void addContactRecord(ContactRecord contactRecord) throws SQLException;

    List<ContactRecord> findAllContacts() throws SQLException;
}
