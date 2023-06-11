package com.example.javaee.entities;

import jakarta.persistence.*;

@Table(
        name = "contact_records",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "surname"})
)
@Entity
public final class ContactRecord {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String surname;

    @Column(unique = true, name = "contact_number")
    private String contactNumber;

    public ContactRecord(String name, String surname, String contactNumber) {
        this.name = name;
        this.surname = surname;
        this.contactNumber = contactNumber;
    }

    public ContactRecord() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
