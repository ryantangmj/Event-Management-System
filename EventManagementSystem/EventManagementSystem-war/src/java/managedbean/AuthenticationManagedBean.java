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

    public AuthenticationManagedBean() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String login() {
        if (accountSession.authenticateAccount(email, password)) {
            userId = accountSession.getAccount(email, password);
            return "home.xhtml?faces-redirect=true";
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
    
    public String navigateToProfile() {
        return "/profile.xhtml?faces-redirect=true&accountId=" + userId;
    }
}
