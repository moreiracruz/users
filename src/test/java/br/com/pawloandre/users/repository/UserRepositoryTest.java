package br.com.pawloandre.users.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import br.com.pawloandre.users.model.User;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByUsername() {
        User user = new User();
        user.setName("Test User"); // Nome obrigatório
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRoles(List.of("USER")); // Roles obrigatório
        userRepository.save(user);

        User foundUser = userRepository.findByUsername("testuser");
        assertNotNull(foundUser);
        assertEquals("testuser", foundUser.getUsername());
    }

    @Test
    void testFindAllWithPagination() {
        for (int i = 1; i <= 10; i++) {
            User user = new User();
            user.setName("User " + i); // Nome obrigatório
            user.setUsername("user" + i);
            user.setPassword("password");
            user.setRoles(List.of("USER")); // Roles obrigatório
            userRepository.save(user);
        }

        Pageable pageable = PageRequest.of(0, 5);
        Page<User> userPage = userRepository.findAll(pageable);

        assertEquals(5, userPage.getContent().size());
        assertEquals(10, userPage.getTotalElements());
    }
}