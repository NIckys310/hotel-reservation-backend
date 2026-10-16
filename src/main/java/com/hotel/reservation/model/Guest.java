package com.hotel.reservation.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Guest {

    private String fullName;
    private String documentId;
    private String email;
    private String phone;

    protected Guest() {
    }

    public Guest(String fullName, String documentId, String email, String phone) {
        this.fullName = fullName;
        this.documentId = documentId;
        this.email = email;
        this.phone = phone;
    }

    public String getFullName() { return fullName; }
    public String getDocumentId() { return documentId; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
}
