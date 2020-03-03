package org.haivo_charity.controller.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @GetMapping("/")
    public ModelAndView home(){
        return new ModelAndView("donate/home");
    }
}
