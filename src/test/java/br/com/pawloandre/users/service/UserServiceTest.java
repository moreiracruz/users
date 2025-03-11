package br.com.pawloandre.users.service;

import br.com.pawloandre.users.exception.BusinessException;
import br.com.pawloandre.users.model.User;
import br.com.pawloandre.users.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void testFindAll() {
        Page<User> userPage = new PageImpl<>(Collections.singletonList(new User()));
        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);

        Page<User> result = userService.findAll(PageRequest.of(0, 10));
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testSaveUserWithExistingUsername() {
        User user = new User();
        user.setUsername("existinguser");

        when(userRepository.findByUsername("existinguser")).thenReturn(user);

        assertThrows(BusinessException.class, () -> userService.save(user));
    }
}