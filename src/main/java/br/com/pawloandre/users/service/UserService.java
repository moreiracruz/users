package br.com.pawloandre.users.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.pawloandre.users.exception.BusinessException;
import br.com.pawloandre.users.model.User;
import br.com.pawloandre.users.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("user.notfound", "Usuário não encontrado", "User not found", "Usuario no encontrado"));
    }

    public User save(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new BusinessException("user.username.exists", "Nome de usuário já existe", "Username already exists", "El nombre de usuario ya existe");
        }
        return userRepository.save(user);
    }

    public User update(int id, User user) {
        User existingUser = findById(id);
        existingUser.setName(user.getName());
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword());
        existingUser.setRoles(user.getRoles());
        return userRepository.save(existingUser);
    }

    public void delete(int id) {
        userRepository.deleteById(id);
    }
}