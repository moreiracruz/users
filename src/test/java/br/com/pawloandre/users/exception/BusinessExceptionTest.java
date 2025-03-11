package br.com.pawloandre.users.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BusinessExceptionTest {

    @Test
    void testBusinessException() {
        BusinessException exception = new BusinessException("error.code", "Error message", "Error message", "Error message");
        assertEquals("error.code", exception.getCode());
        assertEquals("Error message", exception.getMessage());
    }
}