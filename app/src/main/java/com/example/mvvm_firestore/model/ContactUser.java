package com.example.mvvm_firestore.model;

public class ContactUser {
    private String contactId;
    private String contactName;
    private String contactImage;
    private String contactPhone;
    private String contactEmail;

    public ContactUser() {
    }

    public ContactUser(String contactId, String contactName, String contactImage, String contactPhone, String contactEmail) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactImage = contactImage;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactImage() {
        return contactImage;
    }

    public void setContactImage(String contactImage) {
        this.contactImage = contactImage;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
}

