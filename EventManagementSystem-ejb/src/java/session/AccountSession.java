/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package session;

import entity.Account;
import entity.Event;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author admin
 */
@Stateless
public class AccountSession implements AccountSessionLocal {
    @PersistenceContext(unitName = "EventManagementSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public void createAccount(Account a) {
        em.persist(a);
    }
    
    @Override
    public Account getAccount(Long id) {
        return em.find(Account.class, id);
    }
    
    @Override 
    public String getName(Long id) {
        Account account = em.find(Account.class, id);
        return account.getName();
    }
    
    @Override
    public String getContactDetails(Long id) {
        Account account = em.find(Account.class, id);
        return account.getContactDetails();
    }
    
    @Override
    public void updateAccount(Account u) {
        Account oldU = getAccount(u.getId());

        oldU.setName(u.getName());
        oldU.setContactDetails(u.getContactDetails());
        oldU.setEmail(u.getEmail());
        oldU.setPassword(u.getPassword());
        oldU.setProfilePicContent(u.getProfilePicContent());
        
        em.merge(oldU);
    }
    
    @Override 
    public boolean authenticateAccount(String email, String password) {
        Query q;
        
        try {
            q = em.createQuery("SELECT a FROM Account a WHERE LOWER(a.email) =:email AND LOWER(a.password) =:password");
            q.setParameter("email", email);
            q.setParameter("password", password);
            Account user = (Account) q.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    public Long getAccount(String email, String password) {
        Query q;
        q = em.createQuery("SELECT a FROM Account a WHERE LOWER(a.email) LIKE :email AND LOWER(a.password) LIKE :password");
        q.setParameter("email", email);
        q.setParameter("password", password);
        Account user = (Account) q.getSingleResult();
        
        return user.getId();
    }

    @Override
    public void addNewEvent(Account a, Event e) {
        List<Event> organisedEvents = a.getOrganisedEvents();
        organisedEvents.add(e);
        a.setOrganisedEvents(organisedEvents); 
        
        em.merge(a);
    }

    @Override
    public void joinNewEvent(Account a, Event e) {
        List<Event> joinedEvents = a.getJoinedEvents();
        joinedEvents.add(e);
        a.setJoinedEvents(joinedEvents);  

        em.merge(a);
    }

    @Override
    public void removeEvent(Account a, Event e) {
        List<Event> joinedEvents = a.getJoinedEvents();
        joinedEvents.remove(e);
        a.setJoinedEvents(joinedEvents); 

        em.merge(a);
    }
    
    @Override
    public void removeOrgEvent(Account a, Event e) {
        List<Event> organisedEvents = a.getOrganisedEvents();
        organisedEvents.remove(e);
        a.setOrganisedEvents(organisedEvents); 

        em.merge(a);
    }

    @Override
    public List<Event> getOrganisedEvents(Long id) {
        Account account = em.find(Account.class, id);
        return account.getOrganisedEvents();
    }

    @Override
    public List<Event> getRegisteredEvents(Long id) {
        Account account = em.find(Account.class, id);
        return account.getJoinedEvents();
    }

    @Override
    public void updateAttendees(List<Account> accounts, Event e) {
        for (Account a: accounts) {
            if (!a.getAttendedEvents().contains(e)) {
                List<Event> events = a.getAttendedEvents();
                events.add(e);
                a.setAttendedEvents(events);
            }
        }
    }
}
