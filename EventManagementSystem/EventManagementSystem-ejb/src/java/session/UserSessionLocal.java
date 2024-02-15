/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package session;

import entity.User;
import javax.ejb.Local;

/**
 *
 * @author admin
 */
@Local
public interface UserSessionLocal {
    public void createUser(User u);
    
    public User getUser(Long id);
    
    public void updateUser(User u);
    
    public boolean authenticateUser(String email, String password);
}
