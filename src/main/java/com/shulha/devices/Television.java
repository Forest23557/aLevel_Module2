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
        this("none", null, 0, 0, "none");
    }

    public Television(final String series, final ScreenTypes screenType, final double price, final double diagonal,
                      final String country) {
        super(series, screenType, price);
        setDeviceType(DeviceTypes.TELEVISION);
        this.country = country;

        if (diagonal >= 0) {
            this.diagonal = diagonal;
        }
    }

    public void setDiagonal(final double diagonal) {
        if (diagonal >= 0) {
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
        return String.format("%s: " +
                        "serial number - %s, " +
                        "diagonal - %.2f inch, " +
                        "series - %s, " +
                        "screen type - %s, " +
                        "country - %s, " +
                        "price - %.2f$",
                getDeviceType(),getSerialNumber(), diagonal, getSeries(), getScreenType(), country, getPrice());
    }
}
