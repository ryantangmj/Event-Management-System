/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package session;

import entity.Account;
import entity.Event;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author admin
 */
@Stateless
public class EventSession implements EventSessionLocal {

    @PersistenceContext(unitName = "EventManagementSystem-ejbPU")
    private EntityManager em;
    @EJB
    private AccountSessionLocal accountSession;

    @Override
    public void createEvent(Account a, Event e) {
        em.persist(e);
    }

    @Override
    public List<Event> getAllEvents() {
        try {
            Query q;
            q = em.createQuery("SELECT e FROM Event e");

            return q.getResultList();
        } catch (Exception e) {
            return new ArrayList<Event>();
        }
    }

    @Override
    public void addParticipant(Account a, Event e) {
        List<Account> participants = e.getParticipants();
        participants.add(a);
        e.setParticipants(participants);

        em.merge(e);
    }

    @Override
    public void removeParticipant(Account a, Event e) {
        List<Account> participants = e.getParticipants();
        participants.remove(a);
        e.setParticipants(participants);

        em.merge(e);
    }

    @Override
    public void removeOrgEvent(Event e) {
        Event managedEvent = em.merge(e); // Ensure the entity is managed
        em.remove(managedEvent);
    }

    @Override
    public List<Account> retrieveParticipants(Long id) {
        try {
            Event e = em.find(Event.class, id);
            return e.getParticipants();
        } catch (Exception e) {
            return new ArrayList<Account>();
        }
    }

    @Override
    public Event getEvent(Long id) {
        Event event = em.find(Event.class, id);
        return event;
    }
}
