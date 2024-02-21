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
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
    private String validatePassword;
    private String error;

    /**
     * Get the value of error
     *
     * @return the value of error
     */
    public String getError() {
        return error;
    }

    /**
     * Set the value of error
     *
     * @param error new value of error
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * Get the value of validatePassword
     *
     * @return the value of validatePassword
     */
    public String getValidatePassword() {
        return validatePassword;
    }

    /**
     * Set the value of validatePassword
     *
     * @param validatePassword new value of validatePassword
     */
    public void setValidatePassword(String validatePassword) {
        this.validatePassword = validatePassword;
    }

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

    public String createAccount() {
        Account account = new Account();
        account.setName(name);
        account.setContactDetails(contactDetails);
        account.setEmail(email);
        account.setPassword(password);

        accountSession.createAccount(account);
        return "index.xhtml?faces-redirect=true";
    }
}
