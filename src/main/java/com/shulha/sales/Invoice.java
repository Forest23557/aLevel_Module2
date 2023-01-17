package com.shulha.sales;

import com.shulha.devices.Device;
import com.shulha.devices.Telephone;
import com.shulha.devices.Television;
import com.shulha.person.Customer;
import com.shulha.person.PersonNames;
import com.shulha.types.SaleTypes;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BiPredicate;

@Getter
public class Invoice<E extends Device> {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private static final BiPredicate<String, String> CHECK_ID = (checkingId, id) -> checkingId.equals(id);
    private final String id;
    private final String dateTime;
    private Customer customer;
    private Set<E> purchaseSet;
    private SaleTypes type;
    private double limit;
    private double totalCost;

    public Invoice() {
        this(new Customer(), new HashSet<>(), 3_000);
    }

    public Invoice(final Customer customer, final Set<E> purchaseSet, final double limit) {
        this.id = UUID.randomUUID().toString();
        this.dateTime = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        this.customer = customer;
        this.type = SaleTypes.RETAIL;

        setLimit(limit);
        setPurchaseSet(purchaseSet);
        setSaleType(customer, totalCost, limit);
    }

    public void setCustomer(final Customer customer) {
        if (Objects.nonNull(customer)) {
            this.customer = customer;

            setSaleType(customer, totalCost, limit);
        }
    }

    public void setPurchaseSet(final Set<E> purchaseSet) {
        Optional.ofNullable(purchaseSet)
                .ifPresentOrElse(
                        purchaseSet1 -> {
                            this.purchaseSet = purchaseSet1;
                            setTotalCost(purchaseSet1);
                        },
                        () -> this.purchaseSet = new HashSet<>()
                );

        setSaleType(customer, totalCost, limit);
    }

    public void setLimit(final double limit) {
        if (limit > 0) {
            this.limit = limit;
        }
    }

    public void addPurchase(final E purchase) {
        if (Objects.nonNull(purchase)) {
            purchaseSet.add(purchase);
            increaseTotalCost(purchase);
            setSaleType(customer, totalCost, limit);
        }
    }

    public void removePurchase(final String serialNumber) {
        if (Objects.nonNull(purchaseSet) && Objects.nonNull(serialNumber) && !serialNumber.isBlank()) {
            purchaseSet.stream()
                    .dropWhile(Objects::isNull)
                    .filter(existPurchase -> CHECK_ID.test(existPurchase.getSerialNumber(), serialNumber))
                    .findAny()
                    .ifPresent(found -> {
                        decreaseTotalCost(found);
                        purchaseSet.remove(found);
                    });

            setSaleType(customer, totalCost, limit);
        }
    }

    public void removeAll() {
        if (Objects.nonNull(purchaseSet)) {
            purchaseSet.removeAll(purchaseSet);
            totalCost = 0;
            setSaleType(customer, totalCost, limit);
        }
    }

    private void setTotalCost(final Set<E> purchaseSet) {
        totalCost = purchaseSet.stream()
                .filter(Objects::nonNull)
                .mapToDouble(Device::getPrice)
                .sum();
    }

    private void increaseTotalCost(final E purchase) {
        totalCost += purchase.getPrice();
    }

    private void decreaseTotalCost(final E purchase) {
        totalCost -= purchase.getPrice();
    }

    private void setSaleType(final Customer customer, final double totalCost, final double limit) {
        if (Objects.nonNull(customer) && customer.getAge() < 18) {
            this.type = SaleTypes.LOW_AGE;
        } else if (totalCost > limit) {
            this.type = SaleTypes.WHOLESALE;
        } else {
            this.type = SaleTypes.RETAIL;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invoice<?> invoice = (Invoice<?>) o;
        return Objects.equals(id, invoice.id) && Objects.equals(dateTime, invoice.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateTime);
    }

    @Override
    public String toString() {
        return String.format("Invoice%n" +
                "id: %s%n" +
                "date and time: %s%n" +
                "customer: %s%n" +
                "purchase list: %s%n" +
                "type of purchase: %s%n" +
                "total cost: %.2f$",
                id, dateTime, customer, purchaseSet, type, totalCost);
    }

//    public static void main(String[] args) {
//        final Customer customer1 = new Customer(PersonNames.AMELIA, 16, "amelia@gmail.com");
//        final Invoice<Device> invoice = new Invoice<>(customer1, new HashSet<>(), 100_000);
//        final Device tv = new Television();
//        final Device phone = new Telephone();
//        tv.setPrice(60_000);
//        phone.setPrice(40_000);
//        invoice.addPurchase(tv);
//        invoice.addPurchase(phone);
//        System.out.println(invoice);
//    }
}
