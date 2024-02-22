/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package managedbean;

import entity.Account;
import entity.Event;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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

    private Long accountId;
    private Account account;
    private String name;
    private String contactDetails;
    private String email;
    private String password;
    private String validatePassword;
    private String error;
    private List<Event> organisedEvents;
    private List<Event> joinedEvents;

    /**
     * Get the value of joinedEvents
     *
     * @return the value of joinedEvents
     */
    public List<Event> getJoinedEvents() {
        return joinedEvents;
    }

    /**
     * Set the value of joinedEvents
     *
     * @param joinedEvents new value of joinedEvents
     */
    public void setJoinedEvents(List<Event> joinedEvents) {
        this.joinedEvents = joinedEvents;
    }

    /**
     * Get the value of organisedEvents
     *
     * @return the value of organisedEvents
     */
    public List<Event> getOrganisedEvents() {
        return organisedEvents;
    }

    /**
     * Set the value of organisedEvents
     *
     * @param organisedEvents new value of organisedEvents
     */
    public void setOrganisedEvents(List<Event> organisedEvents) {
        this.organisedEvents = organisedEvents;
    }

    /**
     * Get the value of account
     *
     * @return the value of account
     */
    public Account getAccount() {
        return account;
    }

    /**
     * Set the value of account
     *
     * @param account new value of account
     */
    public void setAccount(Account account) {
        this.account = account;
    }

    /**
     * Get the value of accountId
     *
     * @return the value of accountId
     */
    public Long getAccountId() {
        return accountId;
    }

    /**
     * Set the value of accountId
     *
     * @param accountId new value of accountId
     */
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

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

    public void loadSelectedCustomer() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            this.account = accountSession.getAccount(accountId);
            name = this.account.getName();
            contactDetails = this.account.getContactDetails();
            email = this.account.getEmail();
            password = this.account.getPassword();
            validatePassword = password;
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to load account"));
        }
    }
    
    public void editAccount() {
        FacesContext context = FacesContext.getCurrentInstance();
        account.setName(name);
        account.setContactDetails(contactDetails);
        account.setEmail(email);
        account.setPassword(password);

        try {
            accountSession.updateAccount(account);
            context.addMessage(null, new FacesMessage("Success", "Successfully updated details"));
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage("Error", "Failed to update account details"));
        }
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
