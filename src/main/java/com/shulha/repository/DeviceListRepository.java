package com.shulha.repository;

import com.shulha.goods.Device;

import java.util.*;
import java.util.function.BiPredicate;

public class DeviceListRepository implements Repository<String, Device> {
    private static final Set<Device> DEVICE_REPOSITORY = new HashSet<>();
    private static final BiPredicate<String, String> CHECK_ID = (checkingId, id) -> checkingId.equals(id);
    private static DeviceListRepository instance;

    private DeviceListRepository() {
    }

    public static DeviceListRepository getInstance() {
        instance = Optional.ofNullable(instance)
                .orElseGet(DeviceListRepository::new);

        return instance;
    }

    @Override
    public void save(final Device device) {
        Optional.ofNullable(device)
                .ifPresentOrElse(
                        device1 -> DEVICE_REPOSITORY.add(device1),
                        () -> System.out.println("Your device is null! It won't be saved!")
                );
    }

    @Override
    public Optional<Device> getById(final String id) {
        return DEVICE_REPOSITORY.stream()
                .filter(checkingDevice -> CHECK_ID.test(checkingDevice.getSerialNumber(), id))
                .findAny();
    }

    @Override
    public Device[] getAll() {
        return DEVICE_REPOSITORY.toArray(new Device[0]);
    }

    @Override
    public void remove(final String id) {
        DEVICE_REPOSITORY.stream()
                .filter(checkingDevice -> CHECK_ID.test(checkingDevice.getSerialNumber(), id))
                .findAny()
                .ifPresent(DEVICE_REPOSITORY::remove);
    }

    @Override
    public void removeAll() {
        DEVICE_REPOSITORY.clear();
    }
}
