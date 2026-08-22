package com.example.exoplanetes.exceptions;

public class ExoplaneteNotFound extends NotFoundException {
    public ExoplaneteNotFound(Long id) {
        super("Exoplanete not found with id " + id);
    }
}
