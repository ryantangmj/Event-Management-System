/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package session;

import entity.Account;
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
    public void updateAccount(Account u) {
        Account oldU = getAccount(u.getId());

        oldU.setName(u.getName());
        oldU.setContactDetails(u.getContactDetails());
        oldU.setEmail(u.getEmail());
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
}
