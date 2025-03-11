package br.com.pawloandre.users.exception.handler;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResponseErrorTest {

    @Test
    void testResponseError() {
        LocalDateTime now = LocalDateTime.now();
        ResponseError error = new ResponseError(now, "Error message", 400, "error");

        assertEquals(now, error.timestamp());
        assertEquals("Error message", error.status());
        assertEquals(400, error.statusCode());
        assertEquals("error", error.error());
    }
}