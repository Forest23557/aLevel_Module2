package com.shulha.devices;

import com.shulha.types.DeviceTypes;
import com.shulha.types.ScreenTypes;
import lombok.Getter;

import java.util.Objects;

@Getter
public class Television extends Device {
    private double diagonal;
    private String country;

    public Television() {
        super();
    }

    public Television(final String series, final ScreenTypes screenType, final double price, final double diagonal,
                      final String country) {
        super(series, screenType, price);
        setDeviceType(DeviceTypes.TELEVISION);
        this.diagonal = diagonal;
        this.country = country;
    }

    public void setDiagonal(final double diagonal) {
        if (diagonal > 0) {
            this.diagonal = diagonal;
        }
    }

    public void setCountry(final String country) {
        if (Objects.nonNull(country) && !country.isBlank()) {
            this.country = country;
        }
    }

    @Override
    public String toString() {
        return String.format("%s: %n" +
                        "serial number: %s%n" +
                        "diagonal: %.2finch%n" +
                        "series: %s%n" +
                        "screen type: %s%n" +
                        "country: %s%n" +
                        "price: %.2f$ %n%n",
                getDeviceType(), diagonal, getSerialNumber(), getSeries(), getScreenType(), country, getPrice());
    }
}
