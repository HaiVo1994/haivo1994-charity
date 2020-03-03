package org.haivo_charity.controller.login;

import org.haivo_charity.model.Account;
import org.haivo_charity.model.Category;
import org.haivo_charity.model.Vote;
import org.haivo_charity.service.AccountService;
import org.haivo_charity.service.CategoryService;
import org.haivo_charity.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.HashMap;

@Controller
@RequestMapping("/loginManagement")
public class ForLoginController {
    @Autowired
    private VoteService voteService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/managerProposal/{id}")
    public ModelAndView goToPageProposal(Principal principal, @PathVariable long id){
        Vote vote = voteService.findById(id);
        if (vote!=null){
            if(!vote.isAccepted()){
                Account account =accountService.findByUsername(principal.getName());
                if ((account.getRoles().equals("ADMIN")) || (vote.getProposal_by().equals(principal.getName()))){
                    ModelAndView modelAndView = new ModelAndView("/proposal/editProposal");
                    modelAndView.addObject("proposal",vote);
                    Iterable<Category> categories = categoryService.findAll();
                    modelAndView.addObject("categories", categories);
                    Iterable<Category> categoriesOfProposal = categoryService.findAllByVote(vote);
                    HashMap<Long, Boolean> checkCategory = new HashMap<>();
                    boolean check = false;
                    for (Category category:
                            categories) {
                        for (Category categoryOfProposal:
                                categoriesOfProposal) {
                            if (categoryOfProposal.getId().equals(category.getId())){
                                check = true;
                                break;
                            }
                        }
                        checkCategory.put(category.getId(), check);
                        check = false;
                    }

                    modelAndView.addObject("checkCategory", checkCategory);
                    vote.setCategories(null);
                    return modelAndView;
                }
            }
        }
        return new ModelAndView("/error_404");
    }

    @PostMapping("/editProposal")
    public ModelAndView editProposal(Principal principal, @ModelAttribute Vote newVote){
        Vote vote = voteService.findById(newVote.getId());
        if (vote!=null){
            if(!vote.isAccepted()){
                ModelAndView modelAndView = new ModelAndView("/proposal/editProposal");
                Iterable<Category> categories = categoryService.findAll();
                modelAndView.addObject("proposal",vote);
                modelAndView.addObject("categories", categories);
                Account account =accountService.findByUsername(principal.getName());
                if (account.getRoles().equals("ADMIN")){
                    voteService.editProposal(vote, newVote,true, principal.getName());
                }
                else if (vote.getProposal_by().equals(principal.getName())){
                    voteService.editProposal(vote, newVote,false, principal.getName());
                }
                Iterable<Category> categoriesOfProposal = categoryService.findAllByVote(vote);
                HashMap<Long, Boolean> checkCategory = new HashMap<>();
                boolean check = false;
                for (Category category:
                     categories) {
                    for (Category categoryOfProposal:
                            categoriesOfProposal) {
                        if (categoryOfProposal.getId().equals(category.getId())){
                            check = true;
                            break;
                        }
                    }
                    checkCategory.put(category.getId(), check);
                    check = false;
                }

                modelAndView.addObject("checkCategory", checkCategory);
                vote.setCategories(null);
                modelAndView.addObject("proposalSuccess","Đề Nghị Đã Được Sửa");
                return modelAndView;
            }
        }
        return new ModelAndView("/error_404");
    }
}
