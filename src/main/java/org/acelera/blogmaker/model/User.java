package org.acelera.blogmaker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String photo;

    private Enum<Role> role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonIgnoreProperties("user")
    private List<Post> posts;

    public User() {}

    public User(Long id, String name, String email, String password, String photo, Enum<Role> role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.photo = photo;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public Enum<Role> getRole() {
        return role;
    }

    public void setRole(Enum<Role> role) {
        this.role = role;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}