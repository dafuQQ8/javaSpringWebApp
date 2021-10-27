package com.kami.springboot.web.conferences.webapp.conferences;

import com.kami.springboot.web.conferences.webapp.conferences.dao.Status;
import com.kami.springboot.web.conferences.webapp.exceptions.ConferenceDoesntExistException;
import com.kami.springboot.web.conferences.webapp.conferences.dao.Conferences;
import com.kami.springboot.web.conferences.webapp.conferences.dto.ConferencesDto;
import com.kami.springboot.web.conferences.webapp.conferences.service.ConferencesService;
import com.kami.springboot.web.conferences.webapp.exceptions.InvalidDateException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping
@AllArgsConstructor
@Slf4j
public class ConferencesController {

    private final ConferencesService conferencesService;


    @GetMapping(value = {"/", "/home"})
    public String viewHomePage(Model model) {
        return PaginatedHome(1, model);
    }

    @GetMapping("/home/{pageNo}")
    public String PaginatedHome(@PathVariable(value = "pageNo") int pageNo,
                                Model model) {

        Page<Conferences> total = conferencesService.findConferences(Status.ONGOING, pageNo, 6);

        List<Conferences> conferences = total.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", total.getTotalPages());
        model.addAttribute("conferences", conferences);
        model.addAttribute("pageUrlPrefix", "/home/");

        return "home";
    }

    @GetMapping("/upcoming/{pageNo}")
    public String PaginatedFuture(@PathVariable(value = "pageNo") int pageNo,
                                  Model model) {

        Page<Conferences> notExpired = conferencesService.findConferences(Status.UPCOMING, pageNo, 6);

        List<Conferences> notExpiredConferences = notExpired.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", notExpired.getTotalPages());
        model.addAttribute("conferences", notExpiredConferences);
        model.addAttribute("pageUrlPrefix", "/upcoming/");


        return "home";
    }


    @GetMapping("/expired/{pageNo}")
    public String PaginatedExpired(@PathVariable(value = "pageNo") int pageNo,
                                   Model model) {

        Page<Conferences> expired = conferencesService.findConferences(Status.FINISHED, pageNo, 6);

        List<Conferences> expiredConferences = expired.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", expired.getTotalPages());
        model.addAttribute("conferences", expiredConferences);
        model.addAttribute("pageUrlPrefix", "/expired/");

        return "home";
    }

    @GetMapping("/new")
    public String newConference(Model model) {
        ConferencesDto conferencesDto = new ConferencesDto();
        model.addAttribute("conferenceForm", conferencesDto);
        model.addAttribute("method", "new");
        return "conference";
    }

    @PostMapping("/new")
    public String newConference(@ModelAttribute("conferenceForm") @Valid final ConferencesDto conferenceDto,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            log.error(String.valueOf(bindingResult.getFieldError()));
            model.addAttribute("method", "new");
            return "conference";
        } else {
            try {
                conferencesService.addConference(conferenceDto);
                log.info("Adding new conference with information: {}", conferenceDto);
                return "redirect:/home";
            } catch (InvalidDateException e) {
                bindingResult.addError((new FieldError("accountDto",
                        "Date", "Unfortunately, we are not supposed to go back to the past!")));
                return "conference";
            }
        }
    }

    @GetMapping("/{home|upcoming|expired}/edit/{id}")
    public String editConference(@PathVariable("id") long id,
                                 Model model) {
        Conferences conference = conferencesService.findById(id);
        if (conference != null) {
            ConferencesDto conferencesDto = new ConferencesDto();
            model.addAttribute("conferenceForm", conferencesDto);
            model.addAttribute("method", "edit");
            return "conference";
        } else {
            return "error/404";
        }
    }

    @PostMapping("/{home|upcoming|expired}/edit/{id}")
    public String editConference(@PathVariable("id") long id,
                              @ModelAttribute("conferenceForm") ConferencesDto conferencesDto,
                              BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            log.error(String.valueOf(bindingResult.getFieldError()));
            model.addAttribute("method", "edit");
            return "conference";
        } else {
            try {
                conferencesService.editConference(id, conferencesDto);
                log.debug(String.format("Conference with id: %s has been successfully edited.", id));
                return "redirect:/home";
            } catch (ConferenceDoesntExistException e) {
                return "error/500";
            }
        }
    }

    @GetMapping("/{home|upcoming|expired}/delete/{id}")
    public String deleteConference(@PathVariable("id") long id) {
        Conferences conferences = conferencesService.findById(id);
        if (conferences != null) {
            conferencesService.delete(id);
            log.warn("Conference was successfully deleted. id: {}", conferences.getId());
            return "redirect:/";
        } else {
            return "error/404";
        }
    }
}