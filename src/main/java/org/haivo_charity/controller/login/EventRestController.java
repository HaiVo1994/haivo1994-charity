package org.haivo_charity.controller.login;

import org.haivo_charity.model.Account;
import org.haivo_charity.model.Event;
import org.haivo_charity.model.RegisterEvent;
import org.haivo_charity.service.AccountService;
import org.haivo_charity.service.EventService;
import org.haivo_charity.service.RegisterEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/eventRest")
public class EventRestController {
    @Autowired
    private EventService eventService;
    @RequestMapping(value = "/{page}/{size}",method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Event>> getListEvent(@PathVariable int page, @PathVariable int size){
        List<Event> events = eventService.getAllEvent(page,size);
        if (events.size()>0){
            return new ResponseEntity<>(events, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @RequestMapping(value = "/{size}",method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Integer> getAmountPageEvent(@PathVariable int size){
        int countPage = eventService.countEventPage(size);
        return new ResponseEntity<>(countPage, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{page}/{size}",method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Event>> getListEventUser(Principal principal,
                                                        @PathVariable int page, @PathVariable int size){
        List<Event> events = eventService.getEventByUser(principal.getName(), page,size);
        if (events.size()>0){
            return new ResponseEntity<>(events, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @RequestMapping(value = "/user/{size}",method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Integer> getAmountPageEventUser(Principal principal,
                                                          @PathVariable int size){
        int countPage = eventService.countEventByUser(principal.getName(),size);
        return new ResponseEntity<>(countPage, HttpStatus.OK);
    }

    @RequestMapping(value = "/finish_event/{id}",method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Event> getFinishEvent(Principal principal, @PathVariable long id){
        Event event = eventService.findById(id);
        Account account = accountService.findByUsername(principal.getName());

        if ((event!=null) && (account!=null)){
            if (!event.isDeleted()){
                boolean check = false;
                if(account.getRoles().equals("ADMIN")){
                    check = true;
                }
                else if(event.getVote().getProposal_by().equals(principal.getName())){
                    check = true;
                }
                else {
                    RegisterEvent registerEvent =
                            registerEventService.getRegisterByUserAndEvent(account,event);
                    if (registerEvent!=null){
                        check = true;
                    }
                }

                if (check)
                    return new ResponseEntity<>(event, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/get_single/{id}",method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Event> getSingle(@PathVariable long id){
        Event event = eventService.findById(id);
        if (event!=null){
            if ((!event.isFinished()) && (!event.isDeleted())){
                return new ResponseEntity<>(event, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/userRegister/{page}/{size}",method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Event>> listEventRegister(Principal principal,
                                                         @PathVariable int page, @PathVariable int size){
        List<Event> events = registerEventService.listEventRegistered(principal.getName(),page,size);
        if (events.size()>0){
            return new ResponseEntity<>(events, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @RequestMapping(value = "/userRegister/{size}",method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Integer> amountPageEventRegister(Principal principal,
                                                           @PathVariable int size){
        int count = registerEventService.amountPageRegistered(principal.getName(),size);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @Autowired
    private AccountService accountService;
    @Autowired
    private RegisterEventService registerEventService;
    @RequestMapping(value = "/register/{id}",method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<HashMap<String,String>> register(Principal principal,
                                                           @PathVariable long id){
        boolean check = registerEventService.register(id, principal.getName());
        if (check){
            HashMap<String,String> result = new HashMap<>();
            result.put("result","Bạn Đã Đăng Ký");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/checkRegister/{id}",method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<HashMap<String,String>> checkRegister(Principal principal,
                                                                @PathVariable long id){
        Account account = accountService.findByUsername(principal.getName());
        Event event = eventService.findById(id);
        HashMap<String,String> result = new HashMap<>();
        if ((event!=null) && (account!=null)){
            RegisterEvent registerEvent = registerEventService.getRegisterByUserAndEvent(account,event);
            if (registerEvent!=null){
                if (registerEvent.isAccept()){
                    result.put("checkRegister","Đăng Ký Của Bạn Đã Được Chấp Nhận");
                }
                else {
                    result.put("checkRegister","Đăng Ký Của Bạn Chưa Được Chấp Nhận");
                }

                return new ResponseEntity<>(result, HttpStatus.OK);
            }

        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
