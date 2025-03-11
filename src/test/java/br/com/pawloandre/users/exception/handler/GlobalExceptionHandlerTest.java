package br.com.pawloandre.users.exception.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.context.request.WebRequest;

import br.com.pawloandre.users.exception.BusinessException;

@SpringBootTest
class GlobalExceptionHandlerTest {

    @MockitoBean
    private MessageSource messageSource;

    @Autowired // Usando @Autowired
    private GlobalExceptionHandler handler;

    @MockitoBean
    private WebRequest request;

    @Test
    void testHandleBusinessException() {
        when(request.getLocale()).thenReturn(Locale.ENGLISH);
        when(messageSource.getMessage(anyString(), any(), any(Locale.class)))
                .thenReturn("Error message");

        BusinessException exception = new BusinessException("error.code", "Error message", "Error message", "Error message");

        ResponseEntity<Object> response = handler.handleBusinessException(exception, request);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
}