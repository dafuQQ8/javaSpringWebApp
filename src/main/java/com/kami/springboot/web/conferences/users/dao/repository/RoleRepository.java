package com.kami.springboot.web.conferences.users.dao.repository;

import com.kami.springboot.web.conferences.users.dao.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository
        extends JpaRepository<Role, Long> {
    Role findByName(String name);

    @Override
    void delete(Role role);
}
