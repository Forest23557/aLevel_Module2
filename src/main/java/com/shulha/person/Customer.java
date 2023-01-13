package com.shulha.person;

import lombok.Getter;

import java.util.Objects;

@Getter
public class Customer extends Person {
    private String email;

    public Customer() {
        super();
        this.email = "none";
    }

    public Customer(final PersonNames name, final int age, final String email) {
        super(name, age);
        this.email = email;
    }

    public void setEmail(final String email) {
        if (Objects.nonNull(email) && !email.isBlank()) {
            this.email = email;
        }
    }

    @Override
    public String toString() {
        return String.format("id - %s, " +
                        "name - %s, " +
                        "age - %d, " +
                        "email - %s",
                getId(), getName(), getAge(), email);
    }
}
