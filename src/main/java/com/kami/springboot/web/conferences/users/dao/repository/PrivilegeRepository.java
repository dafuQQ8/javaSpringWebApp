package com.kami.springboot.web.conferences.users.dao.repository;

import com.kami.springboot.web.conferences.users.dao.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    Privilege findByName(String name);

    @Override
    void delete(Privilege privilege);
}
