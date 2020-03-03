package org.haivo_charity.controller.admin;

import org.haivo_charity.model.Event;
import org.haivo_charity.model.Vote;
import org.haivo_charity.service.EventService;
import org.haivo_charity.service.RegisterEventService;
import org.haivo_charity.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/event_manager")
public class EventManagerRestController {
    @Autowired
    private VoteService voteService;
    @RequestMapping(value = "/getVote/{page}/{size}",method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Vote>> getListProposal(
            @PathVariable("page") int page, @PathVariable("size") int size){
        List<Vote> votes = voteService.getVotesWerePast(page,size);
        if (votes.size()==0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(votes, HttpStatus.OK);
    }
    @RequestMapping(value = "/getVotePage/{size}",method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Integer> getProposal(
            @PathVariable("size") int size){
        return new ResponseEntity<>(voteService.amountPageOfVotesWerePast(size), HttpStatus.OK);
    }

    @Autowired
    private EventService eventService;
    @RequestMapping(value = "/createRest/{id}",method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<HashMap<String,String>> create(Principal principal, @PathVariable long id,
                                                         @RequestBody Event event){
        Vote vote = voteService.findById(id);
        if (vote!=null){
            if ((vote.isAccepted()) && (vote.getEvent()==null)){
                event.setCreated_at(new Date());
                event.setCreated_by(principal.getName());
                event = eventService.save(event);
                vote.setEvent(event);
                voteService.save(vote);
                HashMap<String,String> result = new HashMap<>();
                result.put("result", "Sự Kiện Đã Được Tạo");
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/edit/{idEvent}",method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Event> edit(Principal principal, @PathVariable long idEvent,
                                      @RequestBody Event event){
        Event origin = eventService.findById(idEvent);
        if (origin!=null){
            if (!origin.isFinished()){
                origin.setUpdated_at(new Date());
                origin.setUpdated_by(principal.getName());
                origin.setNote(event.getNote());
                origin.setLocation(event.getLocation());
                origin.setBeginDate(event.getBeginDate());
                origin.setFinishDate(event.getFinishDate());
                origin.setRegistrationDeadline(event.getRegistrationDeadline());
                origin.setNumberVolunteer(event.getNumberVolunteer());
                origin = eventService.save(origin);
                new ResponseEntity<>(origin, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/eventAcceptRegister/{page}/{size}",method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Event>> eventAcceptRegister(@PathVariable int page, @PathVariable int size){
        List<Event> events = eventService.getAllEventMustAcceptRegister(page, size);
        if (events.size()>0){
            return new ResponseEntity<>(events, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @RequestMapping(value = "/countEventAcceptRegister/{size}",method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Integer> countEventAcceptRegister(@PathVariable int size){
        return new ResponseEntity<>(eventService.countEventMustAcceptRegister(size), HttpStatus.OK);
    }
    @RequestMapping(value = "/eventMustFinish/{page}/{size}",method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Event>> eventMustFinish(@PathVariable int page, @PathVariable int size){
        List<Event> events = eventService.getAllEventMustFinish(page, size);
        if (events.size()>0){
            return new ResponseEntity<>(events, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @RequestMapping(value = "/countEventMustFinish/{size}",method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Integer> countEventMustFinish(@PathVariable int size){
        return new ResponseEntity<>(eventService.countEventMustFinish(size), HttpStatus.OK);
    }

    @Autowired
    private RegisterEventService registerEventService;
    @RequestMapping(value = "/checkRegister/{idRegister}", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<HashMap<String,String>> checkRegister(Principal principal,
                                                                @PathVariable long idRegister){
        HashMap<String,String> messenger = new HashMap<>();
        if (registerEventService.confirm(idRegister,principal.getName(), true)){
            messenger.put("messenger","Thay Đổi Trạng Thái Của Yêu Cầu");
            return new ResponseEntity<>(messenger, HttpStatus.OK);
        }
        messenger.put("messenger","Lỗi Khi Thay Đổi");
        return new ResponseEntity<>(messenger, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/deleteRegister/{idRegister}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<HashMap<String,String>> deleteRegister(Principal principal,
                                                                 @PathVariable long idRegister){
        HashMap<String,String> messenger = new HashMap<>();
        if (registerEventService.confirm(idRegister,principal.getName(), false)){
            messenger.put("messenger","Xóa Yêu Cầu");
            return new ResponseEntity<>(messenger, HttpStatus.OK);
        }
        messenger.put("messenger","Lỗi Khi Xóa");
        return new ResponseEntity<>(messenger, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/countRegisterAccepted/{idEvent}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<HashMap<String, Long>> countRegisterAccepted(@PathVariable long idEvent){
        Event event = eventService.findById(idEvent);
        if (event!=null){
            HashMap<String,Long> count = new HashMap<>();
            count.put("limit", event.getNumberVolunteer());
            count.put("accepted", eventService.countRegisterAccept(event));
            return new ResponseEntity<>(count, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/finishEvent/{idEvent}",  method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<HashMap<String, String>> finishEvent(Principal principal, @PathVariable long idEvent){
        if (eventService.confirmFinish(idEvent,principal.getName())){
            HashMap<String,String> messenger = new HashMap<>();
            messenger.put("messenger","Sự Kiện Đã Hoàn Thành");
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
}
