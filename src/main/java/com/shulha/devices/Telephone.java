package com.shulha.devices;

import com.shulha.types.DeviceTypes;
import com.shulha.types.ScreenTypes;
import lombok.Getter;

import java.util.Objects;

@Getter
public class Telephone extends Device {
    private String model;

    public Telephone() {
        super();
    }

    public Telephone(final String series, final ScreenTypes screenType, final int price, final String model) {
        super(series, screenType, price);
        setDeviceType(DeviceTypes.TELEPHONE);
        this.model = model;
    }

    public void setModel(final String model) {
        if (Objects.nonNull(model) && !model.isBlank()) {
            this.model = model;
        }
    }

    @Override
    public String toString() {
        return String.format("%s: %n" +
                        "model: %s%n" +
                        "serial number: %s%n" +
                        "series: %s%n" +
                        "screen type: %s%n" +
                        "price: %.2f$ %n%n",
                getDeviceType(), model, getSerialNumber(), getSeries(), getScreenType(), getPrice());
    }
}
