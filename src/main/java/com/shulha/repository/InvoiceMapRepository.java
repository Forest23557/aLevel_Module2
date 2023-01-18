package com.shulha.repository;

import com.shulha.goods.Device;
import com.shulha.sales.Invoice;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class InvoiceMapRepository implements Repository<String, Invoice<Device>> {
    private static final Map<String, Invoice<Device>> INVOICE_REPOSITORY = new LinkedHashMap<>();
    private static InvoiceMapRepository instance;

    private InvoiceMapRepository() {
    }

    public static InvoiceMapRepository getInstance() {
        instance = Optional.ofNullable(instance)
                .orElseGet(InvoiceMapRepository::new);

        return instance;
    }

    @Override
    public void save(final Invoice<Device> invoice) {
        Optional.ofNullable(invoice)
                .ifPresentOrElse(
                        invoice1 -> INVOICE_REPOSITORY.put(invoice1.getId(), invoice1),
                        () -> System.out.println("Your invoice is null! It won't be saved!")
                );
    }

    @Override
    public Optional<Invoice<Device>> getById(final String id) {
        return Optional.ofNullable(id)
                .filter(id1 -> !id.isBlank())
                .map(INVOICE_REPOSITORY::get);
    }

    @Override
    public Invoice[] getAll() {
        return INVOICE_REPOSITORY.values().toArray(new Invoice[0]);
    }

    @Override
    public void remove(final String id) {
        INVOICE_REPOSITORY.remove(id);
    }

    @Override
    public void removeAll() {
        INVOICE_REPOSITORY.clear();
    }
}
