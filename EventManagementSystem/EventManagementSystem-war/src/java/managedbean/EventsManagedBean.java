/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package managedbean;

import entity.Event;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import session.EventSessionLocal;

/**
 *
 * @author admin
 */
@Named(value = "eventsManagedBean")
@ViewScoped
public class EventsManagedBean implements Serializable {

    @EJB
    private EventSessionLocal eventSession;    
    private List<Event> events = new ArrayList<Event>();
    
    @PostConstruct
    public void init() { 
        if (events.size() == 0) {
            events = eventSession.getAllEvents();
        }
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
    
}
