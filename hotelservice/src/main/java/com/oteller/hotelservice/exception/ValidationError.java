package com.oteller.hotelservice.exception;

public record ValidationError(String field, String message) {}
