package com.shulha.person;

import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
public abstract class Person {
    private final String id;
    private PersonNames name;
    private int age;

    public Person() {
        this(PersonNames.BENJAMIN, 18);
    }

    public Person(final PersonNames name, final int age) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        if (age > 0) {
            this.age = age;
        }
    }

    public void setName(final PersonNames name) {
        if (Objects.nonNull(name)) {
            this.name = name;
        }
    }

    public void setAge(final int age) {
        if (age > 0) {
            this.age = age;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("id - %s, " +
                "name - %s, " +
                "age - %d",
                id, name, age);
    }
}
