package org.haivo_charity.controller.admin;

import org.haivo_charity.service.RegisterEventService;
import com.github.cliftonlabs.json_simple.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/event_register_manager")
public class EventRegisterRestController {
    @Autowired
    private RegisterEventService registerEventService;
    @RequestMapping(value = "/getRegister/{idEVent}/{page}/{size}",method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<JsonObject>> listRegister
            (@PathVariable long idEVent, @PathVariable int page, @PathVariable int size){
        List<JsonObject> registerEvents = registerEventService.getListInfoByEvent(idEVent,page,size);
        if (registerEvents.size() > 0){
            return new ResponseEntity<>(registerEvents, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @RequestMapping(value = "/getRegisterPage/{idEVent}/{size}",method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Integer> countPageRegisterEvent
            (@PathVariable long idEVent, @PathVariable int size){
        int count = registerEventService.getAmountRegisterEvent(idEVent);
        return new ResponseEntity<>((int) Math.ceil((double) count/size), HttpStatus.OK);
    }
}
