/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package session;

import entity.Event;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author admin
 */
@Stateless
public class EventSession implements EventSessionLocal {
    @PersistenceContext(unitName = "EventManagementSystem-ejbPU")
    private EntityManager em;

    @Override
    public void createEvent(Event e) {
        em.persist(e);
    }
}
