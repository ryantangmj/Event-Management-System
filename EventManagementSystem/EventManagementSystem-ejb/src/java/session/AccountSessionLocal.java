/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package session;

import entity.Account;
import javax.ejb.Local;

/**
 *
 * @author admin
 */
@Local
public interface AccountSessionLocal {
    public void createAccount(Account u);
    
    public Account getAccount(Long id);
    
    public String getName(Long id);
    
    public String getContactDetails(Long id);
    
    public Long getAccount(String email, String password);
    
    public void updateAccount(Account u);
    
    public boolean authenticateAccount(String email, String password);
}
