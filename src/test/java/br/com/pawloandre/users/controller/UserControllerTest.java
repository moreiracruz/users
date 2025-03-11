package br.com.pawloandre.users.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.pawloandre.users.model.User;
import br.com.pawloandre.users.service.UserService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class UserControllerTest {

    @MockitoBean
    private UserService userService;

    @Autowired
    private UserController userController;

    @Test
    void testFindAll() {
        Page<User> userPage = new PageImpl<>(Collections.singletonList(new User()));
        when(userService.findAll(any(Pageable.class))).thenReturn(userPage);

        Page<User> response = userController.findAll(0, 10, "id");

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
    }

    @Test
    void testFindById() {
        User user = new User();
        user.setId(1);
        when(userService.findById(anyInt())).thenReturn(user);

        ResponseEntity<User> response = userController.findById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void testSave() {
        User user = new User();
        user.setRoles(List.of("ADMIN")); // Adicionado roles
        when(userService.save(any(User.class))).thenReturn(user);

        ResponseEntity<User> response = userController.save(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void testUpdate() {
        User user = new User();
        user.setId(1);
        user.setRoles(List.of("USER")); // Adicionado roles
        when(userService.update(anyInt(), any(User.class))).thenReturn(user);

        ResponseEntity<User> response = userController.update(1, user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }
    @Test
    void testDelete() {
        doNothing().when(userService).delete(anyInt());

        ResponseEntity<Void> response = userController.delete(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testPublicEndpoint() {
        ResponseEntity<String> response = userController.publicEndpoint();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Este é um endpoint público!", response.getBody());
    }

    @Test
    void testAdminEndpoint() {
        ResponseEntity<String> response = userController.adminEndpoint();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Este é um endpoint apenas para ADMIN!", response.getBody());
    }

}