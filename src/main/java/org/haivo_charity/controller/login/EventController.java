package org.haivo_charity.controller.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/event")
public class EventController {
    @GetMapping("/")
    public ModelAndView listEvent(){
        return new ModelAndView("/event/list");
    }
    @GetMapping("/single_event/{id}")
    public ModelAndView singleEvent(@PathVariable int id){
        ModelAndView modelAndView = new ModelAndView("/event/single");
        modelAndView.addObject("eventId",id);
        return modelAndView;
    }

    @GetMapping("/user")
    public ModelAndView listEventUser(){
        return new ModelAndView("/event/listUser");
    }
    @GetMapping("/single_event_finish/{id}")
    public ModelAndView eventFinish(@PathVariable int id){
        ModelAndView modelAndView = new ModelAndView("/event/single_user");
        modelAndView.addObject("eventId",id);
        return modelAndView;
    }
    @GetMapping("/userRegister")
    public ModelAndView listEventUserRegister(){
        return new ModelAndView("/event/listUser");
    }
}
