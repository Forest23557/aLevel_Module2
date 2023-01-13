package com.shulha.devices;

import com.shulha.types.DeviceTypes;
import com.shulha.types.ScreenTypes;
import lombok.Getter;

import java.util.Objects;

@Getter
public class Telephone extends Device {
    private String model;

    public Telephone() {
        this("none", null, 0, "none");
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
        return String.format("%s: " +
                        "model - %s, " +
                        "serial number - %s, " +
                        "series - %s, " +
                        "screen type - %s, " +
                        "price - %.2f$",
                getDeviceType(), model, getSerialNumber(), getSeries(), getScreenType(), getPrice());
    }
}
