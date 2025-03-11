# RESTful API for user management

Aqui está um exemplo de como você pode criar um projeto Spring Boot 3.4.3 com Java 17 para um CRUD de usuário (`User`), incluindo model, repository, service, controller e uma exceção personalizada `BusinessException` para validações em inglês, português e espanhol.

### 1. Configuração do Projeto

Primeiro, crie um projeto Spring Boot usando o [Spring Initializr](https://start.spring.io/) com as seguintes dependências:

- Spring Web
- Spring Data JPA
- H2 Database (ou outro banco de dados de sua preferência)
- Spring Boot DevTools (opcional, para desenvolvimento)
- Validation (para validações de entrada)

### 2. Estrutura do Projeto

A estrutura do projeto será algo como:

```
src/main/java/br/com/pawloandre/users
├── controller/
│   └── UserController.java
├── exception/
│   └── BusinessException.java
├── model/
│   └── User.java
├── repository/
│   └── UserRepository.java
├── service/
│   └── UserService.java
└── DemoApplication.java
```

### 3. Model (`User.java`)

```java
package br.com.pawloandre.users.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "{user.name.notblank}")
    private String name;

    @NotBlank(message = "{user.username.notblank}")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "{user.password.notblank}")
    private String password;

    @ElementCollection
    @NotNull(message = "{user.roles.notnull}")
    private List<String> roles;

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
```

### 4. Repository (`UserRepository.java`)

```java
package br.com.pawloandre.users.repository;

import br.com.pawloandre.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
```

### 5. Service (`UserService.java`)

```java
package br.com.pawloandre.users.service;

import br.com.pawloandre.users.exception.BusinessException;
import br.com.pawloandre.users.model.User;
import br.com.pawloandre.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
```

### 6. Controller (`UserController.java`)

```java
package br.com.pawloandre.users.controller;

import br.com.pawloandre.users.model.User;
import br.com.pawloandre.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable int id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    public ResponseEntity<User> save(@RequestBody User user) {
        return ResponseEntity.ok(userService.save(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable int id, @RequestBody User user) {
        return ResponseEntity.ok(userService.update(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
```

### 7. Exceção Personalizada (`BusinessException.java`)

```java
package br.com.pawloandre.users.exception;

public class BusinessException extends RuntimeException {

    private final String code;
    private final String messagePt;
    private final String messageEn;
    private final String messageEs;

    public BusinessException(String code, String messagePt, String messageEn, String messageEs) {
        super(messageEn);
        this.code = code;
        this.messagePt = messagePt;
        this.messageEn = messageEn;
        this.messageEs = messageEs;
    }

    public String getCode() {
        return code;
    }

    public String getMessagePt() {
        return messagePt;
    }

    public String getMessageEn() {
        return messageEn;
    }

    public String getMessageEs() {
        return messageEs;
    }
}
```

### 8. Mensagens de Validação

Crie um arquivo `messages.properties` para as mensagens de validação em inglês, português e espanhol.

**messages.properties**:
```properties
user.name.notblank=O nome não pode estar vazio
user.username.notblank=O nome de usuário não pode estar vazio
user.password.notblank=A senha não pode estar vazia
user.roles.notnull=Os papéis não podem ser nulos
user.notfound=Usuário não encontrado
user.username.exists=Nome de usuário já existe
```

**messages_en.properties**:
```properties
user.name.notblank=Name cannot be blank
user.username.notblank=Username cannot be blank
user.password.notblank=Password cannot be blank
user.roles.notnull=Roles cannot be null
user.notfound=User not found
user.username.exists=Username already exists
```

**messages_es.properties**:
```properties
user.name.notblank=El nombre no puede estar vacío
user.username.notblank=El nombre de usuario no puede estar vacío
user.password.notblank=La contraseña no puede estar vacía
user.roles.notnull=Los roles no pueden ser nulos
user.notfound=Usuario no encontrado
user.username.exists=El nombre de usuario ya existe
```

### 9. Configuração do Spring para Internacionalização

No arquivo `application.properties`, adicione a configuração para suportar múltiplos idiomas:

```properties
spring.messages.basename=messages
spring.messages.cache-duration=3600
spring.messages.encoding=UTF-8
```

### 10. Executando o Projeto

Agora, você pode executar o projeto e testar os endpoints usando ferramentas como Postman ou cURL.

### 11. Testando

- **Criar um usuário**: `POST /users`
- **Listar todos os usuários**: `GET /users`
- **Buscar um usuário por ID**: `GET /users/{id}`
- **Atualizar um usuário**: `PUT /users/{id}`
- **Deletar um usuário**: `DELETE /users/{id}`

### 12. Considerações Finais

Este é um exemplo básico de como criar um CRUD de usuário com Spring Boot. Dependendo das necessidades do seu projeto, você pode adicionar mais funcionalidades, como autenticação, autorização, paginação, etc. Além disso, você pode substituir o banco de dados H2 por um banco de dados mais robusto, como PostgreSQL ou MySQL.
