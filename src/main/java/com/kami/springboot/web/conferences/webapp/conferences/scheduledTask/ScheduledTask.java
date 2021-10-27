package com.kami.springboot.web.conferences.webapp.conferences.scheduledTask;

import com.kami.springboot.web.conferences.webapp.conferences.dao.Conferences;
import com.kami.springboot.web.conferences.webapp.conferences.dao.Status;
import com.kami.springboot.web.conferences.webapp.conferences.dao.repository.ConferencesRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class ScheduledTask {
    private final ConferencesRepository conferencesRepository;


    @Scheduled(fixedDelay = 1000)
    private void isExpired() {
        log.info("Conferences are up to date: " + DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now().withNano(0)));
        List<Conferences> conferences = conferencesRepository.findAllByOrderByIdAsc();
        for (Conferences conference : conferences) {
            LocalDate now = LocalDate.now();
            //getStartDate - x, getFinishDate - y;
            if (now.isAfter(conference.getStartDate()) &&
                    now.isBefore(conference.getFinishDate())) {
                if (!conference.getStatus().equals(Status.ONGOING)) {
                    log.info("Status of conference with id: {} was changed to " + Status.ONGOING, conference.getId());
                    //[x;y]
                    conference.setStatus(Status.ONGOING);
                    conferencesRepository.save(conference);
                }
            } else if (now.isAfter(conference.getFinishDate()) &&
                    now.isAfter(conference.getStartDate())) {
                if (!conference.getStatus().equals(Status.FINISHED)) {
                    log.info("Status of conference with id: {} was changed to " + Status.FINISHED, conference.getId());
                    //(y;+inf)
                    conference.setStatus(Status.FINISHED);
                    conferencesRepository.save(conference);
                }
            }
        }
    }
}
