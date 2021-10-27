package com.kami.springboot.web.conferences.webapp.conferences.dao.repository;

import com.kami.springboot.web.conferences.webapp.conferences.dao.Conferences;
import com.kami.springboot.web.conferences.webapp.conferences.dao.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConferencesRepository extends JpaRepository<Conferences, Long> {
    Conferences findById(long id);

    Conferences findByName(String name);

    Page<Conferences> findByStatus(Status status, Pageable pageable);

    Page<Conferences> findAll(Pageable pageable);
    List<Conferences> findAllByOrderByIdAsc();
}
