package org.haivo_charity.service.impl;

import org.haivo_charity.model.Account;
import org.haivo_charity.model.Event;
import org.haivo_charity.model.RegisterEvent;
import org.haivo_charity.repository.AccountRepository;
import org.haivo_charity.repository.EventRepository;
import org.haivo_charity.repository.RegisterEventRepository;
import org.haivo_charity.service.RegisterEventService;
import com.github.cliftonlabs.json_simple.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RegisterEventServiceImpl implements RegisterEventService {
    @Autowired
    private RegisterEventRepository registerEventRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Override
    public boolean register(Long eventId, String userName) {
        Event event = eventRepository.findById(eventId).orElse(null);
        Account account = accountRepository.findFirstByUsername(userName);
        if ((event!=null) && (account!=null)){
            Date date = new Date();
            if ((!event.isFinished()) && (!event.isDeleted())
                &&(!event.getRegistrationDeadline().before(date))){
                RegisterEvent registerEvent = registerEventRepository.findByAccountAndEvent(account,event);
                if (registerEvent==null){
                    registerEvent = new RegisterEvent();
                    registerEvent.setAccount(account);
                    registerEvent.setEvent(event);
                    registerEvent.setRegister_at(new Date());
                    if (account.getRoles().equals("ADMIN")){
                        registerEvent.setAccept(true);
                        registerEvent.setAcceptBy(userName);
                    }
                    registerEventRepository.save(registerEvent);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean confirm(Long registerId, String userName, boolean isAccept) {
        RegisterEvent registerEvent = registerEventRepository.findById(registerId).orElse(null);
        if (registerEvent!=null){

            if (!registerEvent.isFinish()){
                if (isAccept){
                    if (registerEvent.isAccept()){
                        registerEvent.setAccept(false);
                        registerEvent.setAcceptBy("");
                        registerEventRepository.save(registerEvent);
                        return true;
                    }
                    else {
                        long countAccept = registerEventRepository
                                .countAcceptedByEvent(registerEvent.getEvent());
                        if (countAccept<registerEvent.getEvent().getNumberVolunteer()){
                            registerEvent.setAccept(true);
                            registerEvent.setAcceptBy(userName);
                            registerEventRepository.save(registerEvent);
                            return true;
                        }

                    }
                }
                else {
                    registerEventRepository.delete(registerEvent);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public RegisterEvent getRegisterByUserAndEvent(Account account, Event event) {
        return registerEventRepository.findByAccountAndEvent(account,event);
    }

    @Override
    public List<RegisterEvent> getListRegisterNotAccepted(String userName, int page, int size) {
        Account account = accountRepository.findFirstByUsername(userName);
        if (account!=null){
            return registerEventRepository.
                    getListRegisterNotAccepted(account, PageRequest.of(page,size)).toList();
        }
        return null;
    }

    @Override
    public int getAmountRegisterNotAccepted(String userName) {
        Account account = accountRepository.findFirstByUsername(userName);
        if (account!=null){
            return registerEventRepository.countAmountRegisterNotAccepted(account);
        }
        return 0;
    }

    @Override
    public List<RegisterEvent> getListRegisterAccepted(String userName, int page, int size) {
        Account account = accountRepository.findFirstByUsername(userName);
        if (account!=null){
            return registerEventRepository.
                    getListRegisterAccepted(account, PageRequest.of(page,size)).toList();
        }
        return null;
    }

    @Override
    public int getAmountRegisterAccepted(String userName) {
        Account account = accountRepository.findFirstByUsername(userName);
        if (account!=null){
            return registerEventRepository.countAmountRegisterAccepted(account);
        }
        return 0;
    }

    @Override
    public List<RegisterEvent> getListRegisterFinish(String userName, int page, int size) {
        Account account = accountRepository.findFirstByUsername(userName);
        if (account!=null){
            return registerEventRepository.
                    getListRegisterFinish(account, PageRequest.of(page,size)).toList();
        }
        return null;
    }

    @Override
    public int getAmountRegisterFinish(String userName) {
        Account account = accountRepository.findFirstByUsername(userName);
        if (account!=null){
            return registerEventRepository.countAmountRegisterFinish(account);
        }
        return 0;
    }

    @Override
    public List<RegisterEvent> getListByEvent(Long idEvent, int page, int size) {
        Event event = eventRepository.findById(idEvent).orElse(null);
        if (event!=null){
            return registerEventRepository.findAllByEvent(event, PageRequest.of(page,size)).toList();
        }
        return null;
    }

    @Override
    public int getAmountRegisterEvent(Long idEvent) {
        Event event = eventRepository.findById(idEvent).orElse(null);
        if (event!=null){
            return registerEventRepository.countAllByEvent(event);
        }
        return 0;
    }

    @Override
    public List<JsonObject> getListInfoByEvent(Long idEvent, int page, int size) {
        Event event = eventRepository.findById(idEvent).orElse(null);
        if (event!=null){
            List<RegisterEvent> registerEvents =
                    registerEventRepository.findAllByEvent(event, PageRequest.of(page,size)).toList();
            List<JsonObject> listRegister = new ArrayList<>();
            for (RegisterEvent registerEvent: registerEvents){
                JsonObject register = new JsonObject();
                register.put("name", registerEvent.getAccount().getVolunteer().getName());
                register.put("accepted", registerEvent.isAccept());
                register.put("registerId", registerEvent.getId());
                register.put("registerDate", registerEvent.getRegister_at());
                listRegister.add(register);
            }
            return listRegister;
        }
        return null;
    }

    @Override
    public List<Event> listEventRegistered(String userName, int page, int size) {
        Account account = accountRepository.findFirstByUsername(userName);
        if (account!=null){
            return registerEventRepository
                    .listEventRegisterByAccount(account, PageRequest.of(page, size)).toList();
        }
        return null;
    }

    @Override
    public int amountPageRegistered(String userName, int size) {
        Account account = accountRepository.findFirstByUsername(userName);
        if (account!=null){
            int count = registerEventRepository.countEventRegisterByAccount(account);
            return (int) Math.ceil((double) count/size);
        }
        return 0;
    }
}
