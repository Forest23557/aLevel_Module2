package com.shulha.repository;

import com.shulha.devices.Device;
import com.shulha.sales.Invoice;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class InvoiceMapRepository {
    private static final Map<String, Invoice> invoiceRepository = new LinkedHashMap<>();
    private static InvoiceMapRepository instance;

    private InvoiceMapRepository() {
    }

    public static InvoiceMapRepository getInstance() {
        instance = Optional.ofNullable(instance)
                .orElseGet(InvoiceMapRepository::new);

        return instance;
    }

    public void addInvoice(final Invoice invoice) {
        Optional.ofNullable(invoice)
                .ifPresentOrElse(
                        invoice1 -> invoiceRepository.put(invoice1.getId(), invoice1),
                        () -> System.out.println("Your invoice is null! It won't be saved!")
                );
    }

    public Invoice getInvoice(final String id) {
        Optional.ofNullable(id);
        if (Objects.nonNull(id) && !id.isBlank()) {
            return invoiceRepository.get(id);
        }

        return null;
    }
}
