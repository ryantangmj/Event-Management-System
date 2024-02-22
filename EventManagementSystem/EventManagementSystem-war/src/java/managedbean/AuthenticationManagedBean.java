/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package managedbean;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import session.AccountSessionLocal;

/**
 *
 * @author admin
 */
@Named(value = "authenticationManagedBean")
@SessionScoped
public class AuthenticationManagedBean implements Serializable {
    @EJB
    private AccountSessionLocal accountSession;
    
    private String email = null;
    private String password = null;
    private Long userId = -1l;

    /**
     * Get the value of userId
     *
     * @return the value of userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Set the value of userId
     *
     * @param userId new value of userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }


    /**
     * Get the value of password
     *
     * @return the value of password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the value of password
     *
     * @param password new value of password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get the value of username
     *
     * @return the value of username
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the value of username
     *
     * @param username new value of username
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Creates a new instance of AuthenticationManagedBean
     */
    public AuthenticationManagedBean() {
    }
    
    public String login() {
        if (accountSession.authenticateAccount(email, password)) {
            userId = accountSession.getAccount(email, password);
            return "/home.xhtml?faces-redirect=true";
        } else {
            email = null;
            password = null;
            userId = -1l;
            return "index.xhtml";
        }
    }

    public String logout() {
        email = null;
        password = null;
        userId = -1l;
        return "/index.xhtml?faces-redirect=true";
    }
    
    public String getName() {
        return accountSession.getName(userId);
    }
    
    public String getContactDetails() {
        return accountSession.getContactDetails(userId);
    }
}
