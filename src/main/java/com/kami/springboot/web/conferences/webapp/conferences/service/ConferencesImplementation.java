package com.kami.springboot.web.conferences.webapp.conferences.service;

import com.kami.springboot.web.conferences.webapp.conferences.dao.Conferences;
import com.kami.springboot.web.conferences.webapp.conferences.dao.Status;
import com.kami.springboot.web.conferences.webapp.conferences.dao.repository.ConferencesRepository;
import com.kami.springboot.web.conferences.webapp.conferences.dto.ConferencesDto;
import com.kami.springboot.web.conferences.webapp.exceptions.ConferenceDoesntExistException;
import com.kami.springboot.web.conferences.webapp.exceptions.InvalidDateException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class ConferencesImplementation implements ConferencesService {

    private final ConferencesRepository conferencesRepository;
    @Override
    public Page<Conferences> findConferences(Status status, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return conferencesRepository.findByStatus(status, pageable);
    }

    @Override
    public void addConference(final ConferencesDto addConferenceDto) {

        if (!validStartDate(addConferenceDto.getStartDate()) || !addConferenceDto.getStartDate().isBefore(addConferenceDto.getFinishDate())) {
            throw new InvalidDateException(addConferenceDto.getStartDate() + " is invalid Date.");
        }
        final Conferences conference = new Conferences();

        conference.setName(addConferenceDto.getName());
        conference.setCountry(addConferenceDto.getCountry());
        conference.setCity(addConferenceDto.getCity());
        conference.setStartDate(addConferenceDto.getStartDate());
        conference.setFinishDate(addConferenceDto.getFinishDate());
        conference.setLogo(addConferenceDto.getLogo());
        conference.setDescription(addConferenceDto.getDescription());
        conference.setType(addConferenceDto.getType());
        conference.setStatus(Status.UPCOMING);
        conferencesRepository.save(conference);
    }


    @Override
    public void editConference(final long id,
                               final ConferencesDto editConferenceDto) {
        if (conferenceExists(id)) {
            throw new ConferenceDoesntExistException("There is no conference with id: " + id);
        } if (!validStartDate(editConferenceDto.getStartDate()) || !editConferenceDto.getStartDate().isBefore(editConferenceDto.getFinishDate())) {
            throw new InvalidDateException(editConferenceDto.getStartDate() + " is invalid Date.");
        }
        final Conferences toBeEdited = conferencesRepository.getById(id);

        toBeEdited.setName(editConferenceDto.getName());
        toBeEdited.setCountry(editConferenceDto.getCountry());
        toBeEdited.setCity(editConferenceDto.getCity());
        toBeEdited.setStartDate(editConferenceDto.getStartDate());
        toBeEdited.setFinishDate(editConferenceDto.getFinishDate());
        toBeEdited.setLogo(editConferenceDto.getLogo());
        toBeEdited.setDescription(editConferenceDto.getDescription());
        toBeEdited.setType(editConferenceDto.getType());
        toBeEdited.setStatus(Status.UPCOMING);
        conferencesRepository.save(toBeEdited);
    }

    @Override
    public void delete(long id) {
        conferencesRepository.delete(findById(id));
    }

    @Override
    public Conferences findById(long id) {
        return conferencesRepository.findById(id);
    }

    private boolean conferenceExists(final long id) {
        return conferencesRepository.findById(id) != null;
    }

    private boolean validStartDate(LocalDate inputDate) {
        LocalDate localDate = LocalDate.now();
        return inputDate.isAfter(localDate);
    }
}

