package br.com.pawloandre.users.model;

import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    void testUserValidationSuccess() {
        User user = new User();
        user.setName("John Doe");
        user.setUsername("johndoe");
        user.setPassword("password123");
        user.setRoles(List.of("USER"));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testUserValidationFailure() {
        User user = new User();
        user.setName(""); // Nome vazio
        user.setUsername(""); // Username vazio
        user.setPassword(""); // Senha vazia
        user.setRoles(null); // Roles nulo

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(7, violations.size()); // Espera 4 violações
    }
}