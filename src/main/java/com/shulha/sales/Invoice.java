package com.shulha.sales;

import com.shulha.devices.Device;
import com.shulha.devices.Telephone;
import com.shulha.devices.Television;
import com.shulha.person.Customer;
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
        this(new Customer(), new HashSet<>(), 100_000);
    }

    public Invoice(final Customer customer, final Set<E> purchaseSet, final double limit) {
        this.id = UUID.randomUUID().toString();
        this.dateTime = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        this.customer = customer;
        this.type = SaleTypes.RETAIL;

        if (limit > 0) {
            this.limit = limit;
        }

        Optional.ofNullable(purchaseSet)
                .ifPresentOrElse(
                        this::setPurchaseSet,
                        () -> this.purchaseSet = new HashSet<>()
                );

        if (Objects.nonNull(customer) && customer.getAge() < 18) {
            this.type = SaleTypes.LOW_AGE;
        }
    }

    public void setCustomer(final Customer customer) {
        if (Objects.nonNull(customer)) {
            this.customer = customer;

            Optional.of(customer)
                    .filter(customer1 -> customer1.getAge() < 18)
                    .ifPresent(customer1 -> type = SaleTypes.LOW_AGE);
        }
    }

    public void setPurchaseSet(final Set<E> purchaseSet) {
        if (Objects.nonNull(purchaseSet)) {
            this.purchaseSet = purchaseSet;

            totalCost = purchaseSet.stream()
                    .filter(Objects::nonNull)
                    .mapToDouble(Device::getPrice)
                    .sum();

            if (totalCost > limit) {
                type = SaleTypes.WHOLESALE;
            }
        }
    }

    public void setLimit(final double limit) {
        if (limit > 0) {
            this.limit = limit;
        }
    }

    public void addPurchase(final E purchase) {
        if (Objects.nonNull(purchase)) {

            purchaseSet.add(purchase);
            totalCost += purchase.getPrice();

            if (totalCost > limit) {
                type = SaleTypes.WHOLESALE;
            }
        }
    }

    public void removePurchase(final String serialNumber) {
        if (Objects.nonNull(purchaseSet) && Objects.nonNull(serialNumber) && !serialNumber.isBlank()) {
            purchaseSet.stream()
                    .dropWhile(Objects::isNull)
                    .filter(existPurchase -> CHECK_ID.test(existPurchase.getSerialNumber(), serialNumber))
                    .findAny()
                    .ifPresent(found -> {
                        totalCost -= found.getPrice();
                        purchaseSet.remove(found);
                    });

            if (totalCost > limit) {
                type = SaleTypes.WHOLESALE;
            }
        }
    }

    public void removeAll() {
        if (Objects.nonNull(purchaseSet)) {
            purchaseSet.removeAll(purchaseSet);
            totalCost = 0;
            type = SaleTypes.RETAIL;
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
                "data and time: %s%n" +
                "customer: %s%n" +
                "purchase list: %s%n" +
                "type of purchase: %s%n" +
                "total cost: %.2f$",
                id, dateTime, customer, purchaseSet, type, totalCost);
    }
}
