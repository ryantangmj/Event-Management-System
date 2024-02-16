/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package managedbean;

import entity.Account;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import session.AccountSessionLocal;

/**
 *
 * @author admin
 */
@Named(value = "accountManagedBean")
@ViewScoped
public class AccountManagedBean implements Serializable {
    @EJB
    private AccountSessionLocal accountSession;
    private String name;
    private String contactDetails;
    private String email;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * Creates a new instance of AccountManagedBean
     */
    public AccountManagedBean() {
    }
    
    public void createAccount(ActionEvent evt) {
        Account account = new Account();
        account.setName(name);
        account.setContactDetails(contactDetails);
        account.setEmail(email);
        account.setPassword(password);
        
        accountSession.createAccount(account);
    }
    
}
