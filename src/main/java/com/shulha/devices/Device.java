package com.shulha.devices;

import com.shulha.types.DeviceTypes;
import com.shulha.types.ScreenTypes;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Getter
public abstract class Device {
    private final String serialNumber;
    @Setter(AccessLevel.PROTECTED)
    private DeviceTypes deviceType;
    private String series;
    private ScreenTypes screenType;
    private double price;

    public Device() {
        this.serialNumber = UUID.randomUUID().toString();
    }

    public Device(final String series, final ScreenTypes screenType, final double price) {
        this.serialNumber = UUID.randomUUID().toString();
        this.series = series;
        this.screenType = screenType;

        if (price > 0) {
            this.price = price;
        } else {
            this.price = 0;
        }
    }

    public void setSeries(final String series) {
        if (Objects.nonNull(series) && !series.isBlank()) {
            this.series = series;
        }
    }

    public void setScreenType(final ScreenTypes screenType) {
        if (Objects.nonNull(screenType)) {
            this.screenType = screenType;
        }
    }

    public void setPrice(final double price) {
        if (price > 0) {
            this.price = price;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return Objects.equals(serialNumber, device.serialNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serialNumber);
    }

    @Override
    public String toString() {
        return String.format("Device: %n" +
                        "type of the device: %s%n" +
                        "serial number: %s%n" +
                        "series: %s%n" +
                        "screen type: %s%n" +
                        "price: %.2f$ %n%n",
                deviceType, serialNumber, series, screenType, price);
    }
}
