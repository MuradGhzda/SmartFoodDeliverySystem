package org.example.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role implements Serializable {  // Add Serializable interface
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)  // Add column constraints
    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)  // Change to LAZY fetch
    private Set<User> users = new HashSet<>();

    // Constructor with name
    public Role(String name) {
        this.name = name;
    }

    // Explicit toString, equals, and hashCode methods
    @Override
    public String toString() {
        return "Role{id=" + id + ", name='" + name + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return id != null && id.equals(role.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}