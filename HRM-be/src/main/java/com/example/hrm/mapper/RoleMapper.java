package com.example.hrm.mapper;

import com.example.hrm.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public String map(Role role) {
        return role != null ? role.getName() : null;
    }

    public Role map(String roleName) {
        if (roleName == null) return null;
        Role role = new Role();
        role.setName(roleName);
        return role;
    }
}
