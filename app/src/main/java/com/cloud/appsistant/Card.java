package com.cloud.appsistant;

public class Card {
    private String cardId;
    private String name;
    private String surname;
    private String phone;
    private String email;

    public Card() {

    }

    public Card(String cardId, String name, String surname, String phone, String email) {
        this.cardId = cardId;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
    }

    public String getCardId() {
        return cardId;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}
