package com.shulha.service;

import com.shulha.goods.Device;
import com.shulha.goods.Telephone;
import com.shulha.goods.Television;
import com.shulha.person.Customer;
import com.shulha.person.PersonNames;
import com.shulha.repository.InvoiceMapRepository;
import com.shulha.repository.Repository;
import com.shulha.sales.Invoice;
import com.shulha.types.DeviceTypes;
import com.shulha.types.SaleTypes;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.*;

class ShopServiceTest {
    private static ShopService target;
    private Invoice<Device> invoice;
    private Device device;
    private Device device1;
    private static Repository<String, Invoice<Device>> invoiceMapRepository;
    private Customer customer;
    private Invoice[] invoices;
    private Set<Device> purchaseSet;

    @BeforeAll
    static void beforeAll() {
        invoiceMapRepository = Mockito.mock(InvoiceMapRepository.class);
        target = ShopService.getInstance(invoiceMapRepository);
    }

    @BeforeEach
    void setUp() {
        device = new Telephone();
        device1 = new Television();
        customer = new Customer();
        invoice = new Invoice<>();
        invoices = new Invoice[2];
        purchaseSet = new HashSet<>();
    }

    @Test
    void getNumberOfSoldGoodsByCategoryIncorrectArgumentNull() {
        final long expected = 0;

        long actual = target.getNumberOfSoldGoodsByCategory(null);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getNumberOfSoldGoodsByCategory() {
        purchaseSet.add(device);
        purchaseSet.add(device1);

        invoice.setPurchaseSet(purchaseSet);
        invoice.setCustomer(customer);

        invoices[0] = invoice;
        invoices[1] = invoice;

        Mockito.when(invoiceMapRepository.getAll())
                .thenReturn(invoices);

        final long expected = 2;

        final long actual = target.getNumberOfSoldGoodsByCategory(DeviceTypes.TELEPHONE);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getInvoiceWithTheLowestCost() {
        final Map<Customer, Double> expected = new HashMap<>();
        expected.put(customer, 10.0);

        device.setPrice(10.0);
        device1.setPrice(20.0);

        purchaseSet.add(device);

        invoice.setPurchaseSet(purchaseSet);
        invoice.setCustomer(customer);
        purchaseSet.add(device1);

        invoices[0] = invoice;
        invoices[1] = new Invoice<>(customer, purchaseSet, 1_000);

        Mockito.when(invoiceMapRepository.getAll())
                .thenReturn(invoices);

        final Map<Customer, Double> actual = target.getInvoiceWithTheLowestCost();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getTotalCostOfAllInvoices() {
        final double expected = 60.0;

        device.setPrice(10.0);
        device1.setPrice(20.0);

        purchaseSet.add(device);
        purchaseSet.add(device1);

        invoice.setPurchaseSet(purchaseSet);
        invoice.setCustomer(customer);

        invoices[0] = invoice;
        invoices[1] = new Invoice<>(customer, purchaseSet, 1_000);

        Mockito.when(invoiceMapRepository.getAll())
                .thenReturn(invoices);

        final double actual = target.getTotalCostOfAllInvoices();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getInvoiceNumberByCategory() {
        final long expected = 2;

        purchaseSet.add(device);
        purchaseSet.add(device1);

        invoice.setPurchaseSet(purchaseSet);
        invoice.setCustomer(customer);

        invoices[0] = invoice;
        invoices[1] = new Invoice<>(customer, purchaseSet, 1_000);

        Mockito.when(invoiceMapRepository.getAll())
                .thenReturn(invoices);

        final long actual = target.getInvoiceNumberByCategory(SaleTypes.RETAIL);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getInvoiceNumberByCategoryIncorrectArgumentNull() {
        final long expected = 0;

        invoices[0] = invoice;
        invoices[1] = new Invoice<>(customer, purchaseSet, 1_000);

        Mockito.when(invoiceMapRepository.getAll())
                .thenReturn(invoices);

        final long actual = target.getInvoiceNumberByCategory(null);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getInvoicesThatContainOnlyOneType() {
        Set<Device> purchaseSet1 = new HashSet<>();
        purchaseSet1.add(device);
        purchaseSet1.add(device1);
        purchaseSet.add(device);

        invoice.setPurchaseSet(purchaseSet);
        invoice.setCustomer(customer);

        invoices[0] = invoice;
        invoices[1] = new Invoice<>(customer, purchaseSet1, 1_000);

        final List<Invoice<Device>> expected = Arrays.asList(invoices[0]);

        Mockito.when(invoiceMapRepository.getAll())
                .thenReturn(invoices);

        final List<Invoice<Device>> actual = target.getInvoicesThatContainOnlyOneType();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getFirstInvoices() {
        invoices[0] = invoice;
        invoices[1] = new Invoice<>(customer, purchaseSet, 1_000);

        final List<Invoice<Device>> expected = Arrays.asList(invoices);

        Mockito.when(invoiceMapRepository.getAll())
                .thenReturn(invoices);

        final List<Invoice<Device>> actual = target.getFirstInvoices(2);

        Assertions.assertEquals(expected.size(), actual.size());
    }

    @Test
    void getFirstInvoicesIncorrectNegativeNumber() {
        final List<Invoice<Device>> expected = new ArrayList<>();

        final List<Invoice<Device>> actual = target.getFirstInvoices(-1);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getLowAgeInvoices() {
        final Customer customer1 = new Customer(PersonNames.AVA, 12, "ava2423@gmail.com");
        invoice.setCustomer(customer1);

        invoices[0] = invoice;
        invoices[1] = new Invoice<>(customer, purchaseSet, 1_000);

        final List<Invoice<Device>> expected = Arrays.asList(invoices[0]);

        Mockito.when(invoiceMapRepository.getAll())
                .thenReturn(invoices);

        final List<Invoice<Device>> actual = target.getLowAgeInvoices();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getSortedInvoiceArray() {
        final Customer customer1 = new Customer(PersonNames.AVA, 12, "ava2423@gmail.com");
        invoice.setCustomer(customer);

        invoices[0] = invoice;
        invoices[1] = new Invoice<>(customer1, purchaseSet, 1_000);

        final  Invoice<Device>[] invoices1 = new Invoice[2];
        invoices1[0] = invoices[1];
        invoices1[1] = invoices[0];

        final List<Invoice<Device>> expected = Arrays.asList(invoices1);

        Mockito.when(invoiceMapRepository.getAll())
                .thenReturn(invoices);

        final List<Invoice<Device>> actual = target.getSortedInvoiceArray();

        Assertions.assertEquals(expected, actual);
    }
}