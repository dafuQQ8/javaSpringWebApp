package com.kami.springboot.web.conferences.users.dao.repository;

import com.kami.springboot.web.conferences.users.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository
        extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Override
    void delete(User user);
}
