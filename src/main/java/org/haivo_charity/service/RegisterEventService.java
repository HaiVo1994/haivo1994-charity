package org.haivo_charity.service;

import org.haivo_charity.model.Account;
import org.haivo_charity.model.Event;
import org.haivo_charity.model.RegisterEvent;
import com.github.cliftonlabs.json_simple.JsonObject;

import java.util.List;

public interface RegisterEventService {
    boolean register(Long eventId, String userName);
    boolean confirm(Long eventId, String userName, boolean isAccept);
    RegisterEvent getRegisterByUserAndEvent(Account account, Event event);

    List<RegisterEvent> getListRegisterNotAccepted(String userName, int page, int size);
    int getAmountRegisterNotAccepted(String userName);

    List<RegisterEvent> getListRegisterAccepted(String userName, int page, int size);
    int getAmountRegisterAccepted(String userName);

    List<RegisterEvent> getListRegisterFinish(String userName, int page, int size);
    int getAmountRegisterFinish(String userName);

    List<RegisterEvent> getListByEvent(Long idEvent, int page, int size);
    int getAmountRegisterEvent(Long idEvent);
    List<JsonObject> getListInfoByEvent(Long idEvent, int page, int size);

    List<Event> listEventRegistered(String userName, int page, int size);
    int amountPageRegistered(String userName, int size);
}
