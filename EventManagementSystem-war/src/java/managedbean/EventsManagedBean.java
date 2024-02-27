/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package managedbean;

import entity.Account;
import entity.Event;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Future;
import session.AccountSessionLocal;
import session.EventSessionLocal;

/**
 *
 * @author admin
 */
@Named(value = "eventsManagedBean")
@ViewScoped
public class EventsManagedBean implements Serializable {

    @EJB
    private AccountSessionLocal accountSession;
    @EJB
    private EventSessionLocal eventSession;
    @Inject
    private AuthenticationManagedBean authenticationManagedBean;

    private List<Event> events = new ArrayList<Event>();
    private List<Event> organisedEvents = new ArrayList<Event>();
    private List<Event> registeredEvents = new ArrayList<Event>();
    private Long userId = -1l;
    private Account account;
    private String title;
    private Date currentDate = new Date();
    @Temporal(TemporalType.TIMESTAMP)
    @Future
    private Date date;
    private String location;
    private String description;
    @Temporal(TemporalType.TIMESTAMP)
    @Future
    private Date deadline;
    private Event selectedEvent;
    
    private Long eventId;

    /**
     * Get the value of eventId
     *
     * @return the value of eventId
     */
    public Long getEventId() {
        return eventId;
    }

    /**
     * Set the value of eventId
     *
     * @param eventId new value of eventId
     */
    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    private List<Account> participants = new ArrayList<Account>();
    private List<Account> attendees = new ArrayList<Account>();

    public EventsManagedBean() {
    }

    @PostConstruct
    public void init() {
        if (events.size() == 0) {
            userId = authenticationManagedBean.getUserId();
            events = eventSession.getAllEvents();
            organisedEvents = accountSession.getOrganisedEvents(userId);
            registeredEvents = accountSession.getRegisteredEvents(userId);
        }
    }

    public List<Account> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Account> participants) {
        this.participants = participants;
    }

    public List<Account> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<Account> attendees) {
        this.attendees = attendees;
    }

    public List<Event> getRegisteredEvents() {
        return registeredEvents;
    }

    public void setRegisteredEvents(List<Event> registeredEvents) {
        this.registeredEvents = registeredEvents;
    }

    public Event getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(Event selectedEvent) {
        this.selectedEvent = selectedEvent;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public List<Event> getOrganisedEvents() {
        return organisedEvents;
    }

    public void setOrganisedEvents(List<Event> organisedEvents) {
        this.organisedEvents = organisedEvents;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void loadSelectedCustomer() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            this.account = accountSession.getAccount(userId);
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to load account"));
        }
    }

    public String createEvent(Long userId) {
        FacesContext context = FacesContext.getCurrentInstance();
        if (deadline.after(date)) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validation Error", "The deadline for registration must be before the event date"));
            return null;
        } else {
            account = accountSession.getAccount(userId);
            Event event = new Event();
            event.setTitle(title);
            event.setDate(date);
            event.setLocation(location);
            event.setDescription(description);
            event.setDeadline(deadline);
            event.setOrganiser(account);

            eventSession.createEvent(account, event);
            accountSession.addNewEvent(account, event);
            organisedEvents = accountSession.getOrganisedEvents(userId);
            return "events.xhtml?faces-redirect=true";
        }
    }

    public boolean isUserRegisteredForEvent(Long userId, Event event) {
        selectedEvent = event;
        List<Event> events = accountSession.getRegisteredEvents(userId);
        return events.contains(event);
    }

    public boolean isUserOrganiserForEvent(Long userId) {
        account = accountSession.getAccount(userId);
        return account.getOrganisedEvents().contains(selectedEvent);
    }

    public void registerEvent(Event event, Long userId) {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            if (event.getDeadline().before(new Date())) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Can't register, it is pass the event registration deadline"));
            } else {
                account = accountSession.getAccount(userId);
                eventSession.addParticipant(account, selectedEvent);
                accountSession.joinNewEvent(account, selectedEvent);
                registeredEvents = accountSession.getRegisteredEvents(userId);
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Successfully registered for event"));
            }
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to register for event"));
        }
    }

    public void unregisterEvent(Event event, Long userId) {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            if (event.getDeadline().before(new Date())) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Can't unregister, it is pass the event registration deadline"));
            } else {
                account = accountSession.getAccount(userId);
                eventSession.removeParticipant(account, selectedEvent);
                accountSession.removeEvent(account, selectedEvent);
                registeredEvents = accountSession.getRegisteredEvents(userId);
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Successfully unregistered from event"));
            }
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to unregister from event"));
        }
    }

    public void deleteEvent(Event event, Long userId) {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            int size = eventSession.retrieveParticipants(event.getId()).size();
            if (eventSession.retrieveParticipants(event.getId()).size() > 0) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to delete event as there are already participants"));
                return;
            }
            account = accountSession.getAccount(userId);
            organisedEvents.remove(event);
            accountSession.removeOrgEvent(account, event);
            eventSession.removeOrgEvent(event);
            organisedEvents = accountSession.getOrganisedEvents(userId);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Successfully deleted event"));
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to delete event"));
        }
    }
    
    public void loadSelectedEvent() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            selectedEvent = eventSession.getEvent(eventId);
            participants = eventSession.retrieveParticipants(eventId);
            attendees = eventSession.retrieveAttendees(eventId);
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to load account"));
        }
    }
    
    public void updateAttendance() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            accountSession.updateAttendees(attendees, selectedEvent);
            eventSession.updateAttendees(attendees, selectedEvent);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Successfully updated event"));
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to delete event"));
        }
    }

    public String navigateToAttendance(Long eventId) {
        return "/attendance.xhtml?faces-redirect=true&eventId=" + eventId;
    }
}
