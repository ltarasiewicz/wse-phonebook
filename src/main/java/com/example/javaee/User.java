package com.example.javaee;

public class User {
    private final String name;
    private final String surname;
    private final String placeOfBirth;
    private final Object something;

    public User(String john, String apricot, String antarctica, Object o) {
        name = john;
        surname = apricot;
        placeOfBirth = antarctica;
        something = o;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public Object getSomething() {
        return something;
    }
}
