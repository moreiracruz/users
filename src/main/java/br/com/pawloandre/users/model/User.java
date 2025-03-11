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

    @NotNull(message = "{user.roles.notnull}")
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "roles_user", joinColumns = @JoinColumn(name = "user_id"))
	@Column(name = "role_id")
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