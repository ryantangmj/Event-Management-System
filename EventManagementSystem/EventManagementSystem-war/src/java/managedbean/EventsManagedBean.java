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

    private List<Event> events = new ArrayList<Event>();
    private List<Event> organisedEvents = new ArrayList<Event>();
    private List<Event> registeredEvents = new ArrayList<Event>();
    private Long userId = -1l;
    private Account account;
    private String title;
    private Date currentDate = new Date();
    @Temporal(TemporalType.DATE)
    @Future
    private Date date;
    private String location;
    private String description;
    @Temporal(TemporalType.DATE)
    @Future
    private Date deadline;
    private Event selectedEvent;

    public List<Event> getRegisteredEvents() {
        return registeredEvents;
    }

    public void setRegisteredEvents(List<Event> registeredEvents) {
        this.registeredEvents = registeredEvents;
    }

    /**
     * Get the value of selectedEvent
     *
     * @return the value of selectedEvent
     */
    public Event getSelectedEvent() {
        return selectedEvent;
    }

    /**
     * Set the value of selectedEvent
     *
     * @param selectedEvent new value of selectedEvent
     */
    public void setSelectedEvent(Event selectedEvent) {
        this.selectedEvent = selectedEvent;
    }

    @PostConstruct
    public void init() {
        if (events.size() == 0) {
            events = eventSession.getAllEvents();
            organisedEvents = new ArrayList<Event>();
        }
    }

    /**
     * Get the value of currentDate
     *
     * @return the value of currentDate
     */
    public Date getCurrentDate() {
        return currentDate;
    }

    /**
     * Set the value of currentDate
     *
     * @param currentDate new value of currentDate
     */
    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    /**
     * Get the value of account
     *
     * @return the value of account
     */
    public Account getAccount() {
        return account;
    }

    /**
     * Set the value of account
     *
     * @param account new value of account
     */
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

    /**
     * Get the value of events
     *
     * @return the value of events
     */
    public List<Event> getEvents() {
        return events;
    }

    /**
     * Set the value of events
     *
     * @param events new value of events
     */
    public void setEvents(List<Event> events) {
        this.events = events;
    }

    /**
     * Creates a new instance of EventsManagedBean
     */
    public EventsManagedBean() {
    }

    public String createEvent() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (deadline.after(date)) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validation Error", "The deadline for registration must be before the event date"));
            return null;
        } else {
            Event event = new Event();
            event.setTitle(title);
            event.setDate(date);
            event.setLocation(location);
            event.setDescription(description);
            event.setDeadline(deadline);
            event.setOrganiser(account);

            eventSession.createEvent(account, event);
            accountSession.addNewEvent(account, event);
            return "events.xhtml?faces-redirect=true";
        }
    }

    public boolean isUserRegisteredForEvent(Event event) {
        return registeredEvents.contains(event);
    }

    public void registerEvent(Event event, Long userId) {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            account = accountSession.getAccount(userId);
            registeredEvents.add(event);
            eventSession.addParticipant(account, selectedEvent);
            accountSession.joinNewEvent(account, selectedEvent);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Successfully registered for event"));
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to register for event"));
        }
    }

    public void unregisterEvent(Event event, Long userId) {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            account = accountSession.getAccount(userId);
            registeredEvents.remove(event);
            eventSession.removeParticipant(account, selectedEvent);
            accountSession.removeEvent(account, selectedEvent);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Successfully unregistered from event"));
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to register for event"));
        }
    }
}
