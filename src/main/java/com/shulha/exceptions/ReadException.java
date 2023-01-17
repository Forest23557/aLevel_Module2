package com.shulha.exceptions;

import java.io.IOException;

public class ReadException extends IOException {
    public ReadException() {
    }

    public ReadException(final String message) {
        super(message);
    }
}
