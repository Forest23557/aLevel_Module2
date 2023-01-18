package com.shulha.service;

import com.shulha.person.Customer;
import com.shulha.person.Person;
import com.shulha.person.PersonNames;

import java.util.Optional;
import java.util.Random;

public class PersonService {
    private static final Random RANDOM = new Random();
    private static PersonService instance;

    private PersonService() {
    }

    public static PersonService getInstance() {
         instance = Optional.ofNullable(instance)
                .orElseGet(PersonService::new);

         return instance;
    }

    public Customer getRandomCustomer() {
        final PersonNames[] personNames = PersonNames.values();
        final int randomIndex = RANDOM.nextInt(personNames.length);
        PersonNames name = personNames[randomIndex];
        final int age = RANDOM.nextInt(100) + 1;
        final int numberForEmail = RANDOM.nextInt(2_000_000) + 1;
        final String email = name.toString().toLowerCase() + numberForEmail + "@gmail.com";

        return new Customer(name, age, email);
    }

//    public static void main(String[] args) {
//        final PersonService personService = PersonService.getInstance();
//        System.out.println(personService.getRandomCustomer());
//    }
}
