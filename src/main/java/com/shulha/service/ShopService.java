package com.shulha.service;

import com.shulha.goods.Device;
import com.shulha.exceptions.ReadException;
import com.shulha.goods.Telephone;
import com.shulha.goods.Television;
import com.shulha.person.Customer;
import com.shulha.repository.InvoiceMapRepository;
import com.shulha.repository.Repository;
import com.shulha.sales.Invoice;
import com.shulha.types.DeviceTypes;
import com.shulha.types.SaleTypes;
import com.shulha.types.ScreenTypes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ShopService {
    private static final PersonService PERSON_SERVICE = PersonService.getInstance();
    private static final Random RANDOM = new Random();
    private static ShopService instance;
    private final Repository<String, Invoice<Device>> invoiceRepository;
    private Map<String, List<String>> valuesForGoods;

    private ShopService(final Repository<String, Invoice<Device>> invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public static ShopService getInstance() {
        instance = Optional
                .ofNullable(instance)
                .orElseGet(() -> new ShopService(InvoiceMapRepository.getInstance()));
        return instance;
    }

    public Invoice[] getAllInvoices() {
        return invoiceRepository.getAll();
    }

    public long getNumberOfSoldGoodsByCategory(final DeviceTypes deviceType) {
        long numberOfGoods = 0;

        if (Objects.nonNull(deviceType) && invoiceRepository.getAll().length != 0) {
            numberOfGoods = Stream.of(invoiceRepository.getAll())
                    .flatMap(invoice -> invoice.getPurchaseSet().stream())
                    .filter(device -> device.getDeviceType().equals(deviceType))
                    .count();
        }

        System.out.println();
        System.out.println("Number of sold goods by the category " + deviceType + " is " + numberOfGoods);
        System.out.println();

        return numberOfGoods;
    }

    public Map<Customer, Double> getInvoiceWithTheLowestCost() {
        final Comparator<Invoice<Device>> comparator = (firstInvoice, secondInvoice) ->
                (int) (firstInvoice.getTotalCost() - secondInvoice.getTotalCost());
        final Map<Customer, Double> customerLowestCostMap = Stream.of(invoiceRepository.getAll())
                .sorted(comparator)
                .limit(1)
                .collect(Collectors.toMap(Invoice::getCustomer, Invoice::getTotalCost));

        System.out.println("The lowest cost of invoice: " + customerLowestCostMap.values());
        System.out.println("Customer: " + customerLowestCostMap.keySet());
        System.out.println();

        return customerLowestCostMap;
    }

    public double getTotalCostOfAllInvoices() {
        double totalCost = 0.0;

        if (invoiceRepository.getAll().length != 0) {
            totalCost = Stream.of(invoiceRepository.getAll())
                    .mapToDouble(Invoice::getTotalCost)
                    .sum();
        }

        System.out.println("Total cost of all invoices is " + totalCost + "$");
        System.out.println();

        return totalCost;
    }

    public long getInvoiceNumberByCategory(final SaleTypes saleType) {
        long invoiceNumber = 0;

        if (Objects.nonNull(saleType) && invoiceRepository.getAll().length != 0) {
            invoiceNumber = Stream.of(invoiceRepository.getAll())
                    .map(Invoice::getType)
                    .filter(type -> type.equals(saleType))
                    .count();
        }

        System.out.println("Number of invoices by category " + saleType + " is " + invoiceNumber);
        System.out.println();

        return invoiceNumber;
    }

    public List<Invoice<Device>> getInvoicesThatContainOnlyOneType() {
        final List<Invoice<Device>> invoiceList;
        final Predicate<Invoice<Device>> invoicePredicate = invoice -> {
            Iterator<Device> iterator = invoice.getPurchaseSet().iterator();
            DeviceTypes previous = null;
            Boolean answer = true;

            while (iterator.hasNext()) {
                DeviceTypes current = iterator.next().getDeviceType();

                if (previous != null && previous != current) {
                    answer = false;
                    break;
                }
                previous = current;
            }
            return answer;
        };

        if (invoiceRepository.getAll().length != 0) {
            invoiceList = Stream.of(invoiceRepository.getAll())
                    .filter(invoicePredicate).collect(Collectors.toCollection(ArrayList::new));
        } else {
            invoiceList = new ArrayList<>();
        }

        System.out.println("Invoices that contain only one type of goods: ");
        System.out.println(invoiceList);
        System.out.println();

        return invoiceList;
    }

    public List<Invoice<Device>> getFirstInvoices(final int invoiceNumber) {
        final List<Invoice<Device>> invoiceList;

        if (invoiceNumber > 0 && invoiceRepository.getAll().length != 0) {
            invoiceList = Stream.of(invoiceRepository.getAll())
                    .limit(invoiceNumber)
                    .collect(Collectors.toCollection(ArrayList::new));
        } else {
            invoiceList = new ArrayList<>();
        }

        System.out.println(invoiceNumber + " first invoices: ");
        System.out.println(invoiceList);
        System.out.println();

        return invoiceList;
    }

    public List<Invoice<Device>> getLowAgeInvoices() {
        final List<Invoice<Device>> invoiceList;
        final Predicate<Invoice<Device>> invoicePredicate = invoice -> invoice.getType().equals(SaleTypes.LOW_AGE);

        if (invoiceRepository.getAll().length != 0) {
            invoiceList = Stream.of(invoiceRepository.getAll())
                    .filter(invoicePredicate)
                    .collect(Collectors.toCollection(ArrayList::new));
        } else {
            invoiceList = new ArrayList<>();
        }

        System.out.println("Invoices that type is " + SaleTypes.LOW_AGE + ": ");
        System.out.println(invoiceList);
        System.out.println();

        return invoiceList;
    }

    public List<Invoice<Device>> getSortedInvoiceArray() {
        final List<Invoice<Device>> invoiceList;
        final Comparator<Invoice<Device>> ageComparator =
                (firstInvoice, secondInvoice) -> firstInvoice.getCustomer().getAge() -
                        secondInvoice.getCustomer().getAge();
        final Comparator<Invoice<Device>> deviceNumberComparator =
                (firstInvoice, secondInvoice) -> firstInvoice.getPurchaseSet().size() -
                        secondInvoice.getPurchaseSet().size();
        final Comparator<Invoice<Device>> totalCostComparator =
                (firstInvoice, secondInvoice) -> (int) (firstInvoice.getTotalCost() - secondInvoice.getTotalCost());
        final Comparator<Invoice<Device>> generalComparator = ageComparator.thenComparing(deviceNumberComparator)
                .thenComparing(totalCostComparator);

        if (invoiceRepository.getAll().length != 0) {
            invoiceList = Stream.of(invoiceRepository.getAll())
                    .sorted(generalComparator)
                    .collect(Collectors.toCollection(ArrayList::new));
        } else {
            invoiceList = new ArrayList<>();
        }

        System.out.println("Sorted invoice list: ");
        System.out.println(invoiceList);
        System.out.println();

        return invoiceList;
    }

    public Invoice<Device> getRandomInvoice(final String path) {
        getGoodsFromCsv(path);
        final Invoice<Device> invoice = new Invoice<>();

        if (!valuesForGoods.isEmpty()) {
            final int randomBound = RANDOM.nextInt(5) + 1;
            final int numberOfGoodsInCsv = valuesForGoods.keySet().size();

            for (int i = 0; i < randomBound; i++) {
                final int randomDeviceNumber = RANDOM.nextInt(numberOfGoodsInCsv);
                getDeviceFromStringMap(randomDeviceNumber, path).ifPresent(device -> invoice.addPurchase(device));
            }

            if (!invoice.getPurchaseSet().isEmpty()) {
                invoice.setCustomer(PERSON_SERVICE.getRandomCustomer());
                invoiceRepository.save(invoice);
            }

            invoiceLogs(invoice);
        }

        return invoice;
    }

    private void invoiceLogs(final Invoice<Device> invoice) {
        if (Objects.nonNull(invoice)) {
            System.out.println(String.format("[%s][CUSTOMER: %s][INVOICE: id - %s, type - %s, purchase list - %s, total cost - %.2f$]",
                    invoice.getDateTime(), invoice.getCustomer(), invoice.getId(), invoice.getType(),
                    invoice.getPurchaseSet(), invoice.getTotalCost()));
        }
    }

    public Optional<Device> getDeviceFromStringMap(final int deviceNumber, final String path) {
        getGoodsFromCsv(path);

        final int numberOfGoodsInCsv = valuesForGoods.keySet().size();
        final Optional<Device> optionalDevice = Optional.of(deviceNumber)
                .filter(number -> number >= 0 && number < numberOfGoodsInCsv)
                .map(number -> createDevice(valuesForGoods, deviceNumber));

        return optionalDevice;
    }

    private Device createDevice(final Map<String, List<String>> values, final int deviceNumber) {
        final Device device;
        final DeviceTypes deviceType = Enum.valueOf(DeviceTypes.class, values.get("type")
                .get(deviceNumber).toUpperCase());
        final String series = values.get("series")
                .get(deviceNumber);
        final ScreenTypes screenType = Enum.valueOf(ScreenTypes.class, values.get("screen type")
                .get(deviceNumber).toUpperCase());
        final double price = Double.parseDouble(values.get("price")
                .get(deviceNumber));

        if (deviceType == DeviceTypes.TELEPHONE) {
            final String model = values.get("model")
                    .get(deviceNumber);

            device = new Telephone(series, screenType, price, model);
        } else {
            final double diagonal = Double.parseDouble(values.get("diagonal")
                    .get(deviceNumber));
            final String country = values.get("country")
                    .get(deviceNumber);

            device = new Television(series, screenType, price, diagonal, country);
        }

        return device;
    }

    private String[] checkReadableLine(final String line, final int lineNumber) {
        String[] values = line.split(",");
        final String error = "Your data in the readable string №" + lineNumber + " is wrong!";

        try {
            for (String value : values) {
                if (value.isBlank()) {
                    values = null;
                    throw new ReadException(error);
                }
            }
        } catch (final ReadException readException) {
            System.err.println(readException.getMessage());
        }

        return values;
    }

    private Map<String, List<String>> getGoodsFromCsv(final String path) {
        if (Objects.nonNull(valuesForGoods) && !valuesForGoods.isEmpty()) {
            return valuesForGoods;
        }

        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        valuesForGoods = new LinkedHashMap<>();

        try (final BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(classLoader.getResourceAsStream(path)))) {

            String line;
            int lineNumber = 1;

            while ((line = bufferedReader.readLine()) != null) {
                String[] values = checkReadableLine(line, lineNumber);

                if (lineNumber == 1 && Objects.isNull(values)) {
                    break;
                }

                if (lineNumber == 1) {
                    Stream.of(values)
                            .forEach(key -> valuesForGoods.put(key, new ArrayList<>()));
                } else if (Objects.nonNull(values)) {
                    Iterator<Map.Entry<String, List<String>>> iterator = valuesForGoods.entrySet().iterator();
                    int j = 0;

                    while (iterator.hasNext()) {
                        iterator.next()
                                .getValue()
                                .add(values[j]);

                        j++;
                    }
                }

                lineNumber++;
            }

        } catch (final IOException ex) {
            System.out.println(ex.getMessage());
        } catch (final NullPointerException ex) {
            ex.printStackTrace();
        }

        return valuesForGoods;
    }
}
