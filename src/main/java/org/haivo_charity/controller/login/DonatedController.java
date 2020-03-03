package org.haivo_charity.controller.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/donated")
public class DonatedController {
    @GetMapping("/list")
    public ModelAndView listDonated(){
        return new ModelAndView("/donate/donated");
    }
}
