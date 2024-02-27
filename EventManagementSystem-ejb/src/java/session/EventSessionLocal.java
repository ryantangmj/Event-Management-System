/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package session;

import entity.Account;
import entity.Event;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author admin
 */
@Local
public interface EventSessionLocal {
    public void createEvent(Account a, Event e);
    
    public void removeOrgEvent(Event e);
    
    public void addParticipant(Account a, Event e);
    
    public void removeParticipant(Account a, Event e);
    
    public void updateAttendees(List<Account> a, Event e);
    
    public Event getEvent(Long id);
    
    public List<Event> getAllEvents();
    
    public List<Account> retrieveParticipants(Long id);
    
    public List<Account> retrieveAttendees(Long id);
}
