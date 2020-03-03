package org.haivo_charity.controller;

import org.haivo_charity.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
@RequestMapping("/create_account")
public class CreateAccountController {
    @GetMapping("/login")
    public ModelAndView login(Principal principal){
        if (principal==null){
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("userName", "");
            modelAndView.setViewName("/account/login");
            return modelAndView;
        }
        else {
            return new ModelAndView("/donate/home");
        }
    }

    @Autowired
    private AccountService accountService;

    @GetMapping("/create_account")
    public ModelAndView goCreateAccount(Principal principal) {
        ModelAndView modelAndView = new ModelAndView();
        if (principal==null) {
            modelAndView.setViewName("/account/create_account");
            modelAndView.addObject("username", "");
            modelAndView.addObject("password", "");
            modelAndView.addObject("yourName", "");
            modelAndView.addObject("yourEmail", "");
            modelAndView.addObject("yourPhone", "");
        } else {
            modelAndView.setViewName("/donate/home");
        }
        return modelAndView;
    }

    @PostMapping("/create_account")
    public ModelAndView createAccount(Principal principal,
                                      @RequestParam("username") String userName,
                                      @RequestParam("password") String password,
                                      @RequestParam("yourName") String yourName,
                                      @RequestParam("yourEmail") String yourEmail,
                                      @RequestParam("yourPhone") String yourPhone) {
        ModelAndView modelAndView = new ModelAndView();
        if (principal==null) {
            String result = accountService.create(userName,password,yourName,yourEmail,yourPhone);
            if(result.equals("")){
                modelAndView.setViewName("/account/autoLogin");
                modelAndView.addObject("userName", userName);
                modelAndView.addObject("password", password);
            }
            else {
                modelAndView.setViewName("/account/create_account");
                modelAndView.addObject("username", userName);
                modelAndView.addObject("password", password);
                modelAndView.addObject("yourName", yourName);
                modelAndView.addObject("yourEmail", yourEmail);
                modelAndView.addObject("yourPhone", yourPhone);
                modelAndView.addObject("result", result);
            }
        } else {
            modelAndView.setViewName("/donate/home");
        }
        return modelAndView;
    }

//    @GetMapping("/changeAccount")
//    public ModelAndView changeAccount(Principal principal) {
//        Account account = accountService.findByUsername(principal.getName());
//        ModelAndView modelAndView = new ModelAndView();
//        if (account != null) {
//            modelAndView.setViewName("/account/change_password");
//            modelAndView.addObject("account", account);
//        } else {
//            modelAndView.setViewName("/error_404");
//        }
//        return modelAndView;
//    }
//    @PostMapping("/changeAccount")
//    public ModelAndView updateAccount(Principal principal, @RequestBody Account account) {
//        Account accountOrigin = accountService.findByUsername(principal.getName());
//        ModelAndView modelAndView = new ModelAndView();
//        if (accountOrigin != null) {
//            accountService.update(accountOrigin, account);
//            modelAndView.setViewName("/donate/home");
//        } else {
//            modelAndView.setViewName("/error_404");
//        }
//        return modelAndView;
//    }
}
