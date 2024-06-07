package com.SpringMongoDB.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Data
@Document(collection = "roles")
public class Role {

    @Id
    private String id;

    private RoleType role;

    public GrantedAuthority toGrantedAuthority() {
        return new SimpleGrantedAuthority(role.toString());
    }

    public static Role from(RoleType type) {
        var role = new Role();
        role.setRole(type);
        return role;
    }
}
