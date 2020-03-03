package org.haivo_charity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DonateController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(){
        return "donate/home";
    }
    @RequestMapping(value = "/vote/{id}", method = RequestMethod.GET)
    public ModelAndView vote(@PathVariable Long id){
        ModelAndView modelAndView = new ModelAndView("donate/vote");
        modelAndView.addObject("id", id);
        return modelAndView;
    }

    @RequestMapping(value = "/category/*", method = RequestMethod.GET)
    public ModelAndView listVoteByCategory(){
        ModelAndView modelAndView = new ModelAndView("category/home");
//        modelAndView.addObject("id", id);
//        modelAndView.addObject("page", 0);
//        modelAndView.addObject("size", 9);
//        modelAndView.addObject("search","");
        return modelAndView;
    }
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ModelAndView listVote(){
        ModelAndView modelAndView = new ModelAndView("category/home");
//        modelAndView.addObject("id", -1);
//        modelAndView.addObject("page", 0);
//        modelAndView.addObject("size", 9);
        //modelAndView.addObject("search","");
        return modelAndView;
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ModelAndView findVoteByTittle(@RequestParam("search") String search){
        ModelAndView modelAndView = new ModelAndView("category/home");
        //modelAndView.addObject("id", -1);
//        modelAndView.addObject("page", 0);
//        modelAndView.addObject("size", 9);
        modelAndView.addObject("search",search);
        return modelAndView;
    }
}
