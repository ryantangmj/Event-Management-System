/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package managedbean;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import session.AccountSessionLocal;

/**
 *
 * @author admin
 */
@Named(value = "authenticationManagedBean")
@SessionScoped
public class AuthenticationManagedBean implements Serializable {
    private String username = null;
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
    public String getUsername() {
        return username;
    }

    /**
     * Set the value of username
     *
     * @param username new value of username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    
    private AccountSessionLocal accountSession;
    /**
     * Creates a new instance of AuthenticationManagedBean
     */
    public AuthenticationManagedBean() {
    }
    
    public String login() {
        //by right supposed to use a session bean to
        //do validation here
        //...
        //simulate username/password
        if (accountSession.authenticateAccount(username, password)) {
            //login successful
            //store the logged in user id
            userId = accountSession.getAccount(username, password);
            //do redirect
            return "/secret/secret.xhtml?faces-redirect=true";
        } else {
            //login failed
            username = null;
            password = null;
            userId = -1l;
            return "login.xhtml";
        }
    }

    public String logout() {
        username = null;
        password = null;
        userId = -1l;
        return "/login.xhtml?faces-redirect=true";
    }
    
}
