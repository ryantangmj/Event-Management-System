/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package managedbean;

import entity.Account;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.servlet.http.Part;
import session.AccountSessionLocal;

/**
 *
 * @author admin
 */
@Named(value = "editAccountManagedBean")
@ViewScoped
public class EditAccountManagedBean implements Serializable {

    @EJB
    private AccountSessionLocal accountSession;

    private Long accountId;
    private Account account;
    private String name;
    private String contactDetails;
    private String email;
    private String password;
    private String validatePassword;
    private Part uploadedfile;
    private String filename = "";
    private byte[] fileContent;
    private long imageVersion = System.currentTimeMillis();

    public EditAccountManagedBean() {
    }

    public long getImageVersion() {
        return imageVersion;
    }

    public void setImageVersion(long imageVersion) {
        this.imageVersion = imageVersion;
    }

    public void updateImageVersion() {
        this.imageVersion = System.currentTimeMillis();
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
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

    public String getValidatePassword() {
        return validatePassword;
    }

    public void setValidatePassword(String validatePassword) {
        this.validatePassword = validatePassword;
    }

    public Part getUploadedfile() {
        return uploadedfile;
    }

    public void setUploadedfile(Part uploadedfile) {
        this.uploadedfile = uploadedfile;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public void loadSelectedCustomer() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            accountId = (Long) context.getExternalContext().getSessionMap().get("accountId");
            this.account = accountSession.getAccount(accountId);
            name = this.account.getName();
            contactDetails = this.account.getContactDetails();
            email = this.account.getEmail();
            password = this.account.getPassword();
            validatePassword = password;
            fileContent = this.account.getProfilePicContent();
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to load account"));
        }
    }

    public void editAccount() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        if (accountSession.sameEmail(email)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "This email already has an account"));
        } else {
            if (uploadedfile != null) {
                fileContent = toByteArray(uploadedfile.getInputStream());
            }
            account.setProfilePicContent(fileContent);
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
    }

    public byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = input.read(buffer)) != -1) {
            output.write(buffer, 0, length);
        }

        return output.toByteArray();
    }
}
