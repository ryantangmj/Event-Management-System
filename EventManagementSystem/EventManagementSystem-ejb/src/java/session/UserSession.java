/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package session;

import entity.User;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author admin
 */
@Stateless
public class UserSession implements UserSessionLocal {
    @PersistenceContext(unitName = "EventManagementSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public void createUser(User u) {
        em.persist(u);
    }
    
    @Override
    public User getUser(Long id) {
        return em.find(User.class, id);
    }
    
    @Override
    public void updateUser(User u) {
        User oldU = getUser(u.getId());

        oldU.setName(u.getName());
        oldU.setContactDetails(u.getContactDetails());
        oldU.setEmail(u.getEmail());
    }
}
