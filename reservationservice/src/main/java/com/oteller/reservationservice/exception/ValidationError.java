package com.oteller.reservationservice.exception;

public record ValidationError(String field, String message) {}
