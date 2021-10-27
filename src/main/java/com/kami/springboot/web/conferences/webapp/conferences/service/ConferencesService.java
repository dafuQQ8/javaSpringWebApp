package com.kami.springboot.web.conferences.webapp.conferences.service;

import com.kami.springboot.web.conferences.webapp.conferences.dao.Conferences;
import com.kami.springboot.web.conferences.webapp.conferences.dao.Status;
import com.kami.springboot.web.conferences.webapp.conferences.dto.ConferencesDto;
import org.springframework.data.domain.Page;

public interface ConferencesService {

    void editConference(long id, ConferencesDto conferenceDto);
    void delete(long id);
    void addConference(ConferencesDto conferenceDto);
    Conferences findById(long id);
    Page<Conferences> findConferences(Status status, int pageNo, int pageSize);



}
