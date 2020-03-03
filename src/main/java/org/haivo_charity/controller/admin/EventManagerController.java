package org.haivo_charity.controller.admin;

import org.haivo_charity.model.Event;
import org.haivo_charity.model.Vote;
import org.haivo_charity.service.EventService;
import org.haivo_charity.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Date;

@Controller
@RequestMapping("/event_manager")
public class EventManagerController {
    @GetMapping("/")
    public ModelAndView listVote(){
        return new ModelAndView("admin/event_manager/listVote");
    }
    @Autowired
    private VoteService voteService;
    @GetMapping("/create_event/{idVote}")
    public ModelAndView pageCreate(@PathVariable long idVote){
        ModelAndView modelAndView = new ModelAndView();
        Vote vote = voteService.findById(idVote);
        if (vote!=null){
            if ((!vote.getFinishDate().after(new Date())) && (vote.isAccepted()) ){
                modelAndView.setViewName("admin/event_manager/create");
                modelAndView.addObject("vote", vote);
                Event event = new Event();
                event.setVote(vote);
                modelAndView.addObject("event", event);
                return modelAndView;
            }
        }
        return new ModelAndView("admin/event_manager/listVote");
    }

    @Autowired
    private EventService eventService;
    @PostMapping("/create")
    public ModelAndView create(Principal principal,
                               @ModelAttribute Event event){
        event.setCreated_at(new Date());
        event.setCreated_by(principal.getName());
        eventService.save(event);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("messenge", "Sự Kiện Đã Được Tạo");
        return modelAndView;
    }

    @GetMapping("/edit/{idEvent}")
    public ModelAndView pageEdit(@PathVariable long idEvent){
        ModelAndView modelAndView = new ModelAndView();
        Event event = eventService.findById(idEvent);
        if (event!=null){
            if (!event.isFinished()) {
                modelAndView.setViewName("admin/event_manager/edit");
                modelAndView.addObject("event", event);
                return modelAndView;
            }
        }
        return new ModelAndView("admin/event_manager/listVote");
    }

    @PostMapping("/edit")
    public ModelAndView edit(Principal principal,
                             @ModelAttribute Event event){
        Event check = eventService.editEvent(event, principal.getName());

        ModelAndView modelAndView = new ModelAndView("admin/event_manager/edit");
        modelAndView.addObject("event", event);
        modelAndView.addObject("messenge", "Sự Kiện Đã Được Sửa");
        return modelAndView;
    }

    @GetMapping("/acceptRegister")
    public ModelAndView acceptRegister(){
        return new ModelAndView("/event/list");
    }
    @GetMapping("/mustFinish")
    public ModelAndView mustFinish(){
        return new ModelAndView("/event/list");
    }
}
