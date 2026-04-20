package com.fitness.gymManagementSystem.exception;

public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resource, Object id) {
        super(resource + " không tồn tại (id=" + id + ")");
    }

    public ResourceNotFoundException(String resource, String field, Object value) {
        super(resource + " không tồn tại với " + field + ": " + value);
    }
}