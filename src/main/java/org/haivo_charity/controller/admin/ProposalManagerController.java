package org.haivo_charity.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/proposal_admin")
public class ProposalManagerController {
    @GetMapping("/")
    public ModelAndView listProposal(){
        return new ModelAndView("/admin/proposal_manager/list");
    }
    @GetMapping("/proposal/{id}")
    public ModelAndView getProposal(@PathVariable Long id){
        return new ModelAndView("/admin/proposal_manager/single");
    }
}
