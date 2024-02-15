/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package session;

import entity.User;
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
    
    @Override 
    public boolean authenticateUser(String email, String password) {
        Query q;
        
        try {
            q = em.createQuery("SELECT user FROM User user WHERE LOWER(user.email) LIKE :email AND LOWER(user.password) LIKE :password");
            q.setParameter("email", email);
            q.setParameter("password", password);
            User user = (User) q.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }
}
